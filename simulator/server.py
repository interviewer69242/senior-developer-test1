from http.server import BaseHTTPRequestHandler, ThreadingHTTPServer
import json, threading, time
lock = threading.Lock()
records = []
mode = 'normal'
class Handler(BaseHTTPRequestHandler):
    def respond(self, status, body):
        self.send_response(status); self.send_header('Content-Type','application/json'); self.end_headers()
        try: self.wfile.write(json.dumps(body).encode())
        except (BrokenPipeError, ConnectionResetError): pass
    def do_GET(self):
        with lock: snapshot = list(records)
        if self.path == '/settlements': self.respond(200,snapshot)
        else: self.respond(404,{})
    def do_POST(self):
        global mode
        try: data = json.loads(self.rfile.read(int(self.headers.get('Content-Length',0))))
        except (ValueError, TypeError): self.respond(400,{}); return
        if self.path == '/mode':
            if data.get('mode') not in ('normal','fail','timeout_after_accept'): self.respond(400,{}); return
            with lock: mode=data['mode']
            self.respond(200,data); return
        if self.path != '/settlements': self.respond(404,{}); return
        with lock:
            current=mode
            if current != 'fail': records.append(data)
        if current == 'fail': self.respond(503,{}); return
        if current == 'timeout_after_accept': time.sleep(4)
        self.respond(201,{'accepted':True})
    def do_DELETE(self):
        global mode
        if self.path != '/settlements': self.respond(404,{}); return
        with lock: records.clear(); mode='normal'
        self.respond(200,{})
ThreadingHTTPServer(('0.0.0.0',8090),Handler).serve_forever()
