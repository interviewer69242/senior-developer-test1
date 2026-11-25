import React,{useState} from 'react';
import {createRoot} from 'react-dom/client';
import {submitTrade,type TradeRequest,type Trade} from './api';
import './style.css';
function App(){
 const [form,setForm]=useState<TradeRequest>({clientId:'client-a',clientTradeId:'order-1',instrument:'ABC',side:'BUY',quantity:'10',price:'12.50'});
 const [history,setHistory]=useState<Trade[]>([]);
 const [message,setMessage]=useState('Ready');
 async function submit(e:React.FormEvent){e.preventDefault();setMessage('Sending request...');try{const trade=await submitTrade(form);setHistory(h=>[trade,...h]);setMessage('Accepted: '+trade.id);}catch(error){setMessage(String(error));}}
 return <main><header><small>ENGINEERING ASSESSMENT / SYNTHETIC DATA</small><h1>Trade Operations Portal</h1><p>Submit and inspect trade requests.</p></header><section><h2>Submit a trade</h2><form onSubmit={submit}><div className="grid">{(['clientId','clientTradeId','instrument','quantity','price'] as const).map(key=><label key={key}>{key}<input required value={form[key]} onChange={e=>setForm({...form,[key]:e.target.value})}/></label>)}<label>side<select value={form.side} onChange={e=>setForm({...form,side:e.target.value as TradeRequest['side']})}><option>BUY</option><option>SELL</option></select></label></div><button>Submit trade</button><button type="button" onClick={()=>setForm({...form,clientTradeId:'order-'+Date.now()})}>New reference</button></form><p role="status">{message}</p></section><section><h2>Recent submissions</h2><table><thead><tr><th>Reference</th><th>Instrument</th><th>Trade ID</th></tr></thead><tbody>{history.map((t,i)=><tr key={t.id+'-'+i}><td>{t.request.clientTradeId}</td><td>{t.request.instrument}</td><td>{t.id}</td></tr>)}</tbody></table><p>History is local to this browser session.</p></section></main>
}
createRoot(document.getElementById('root')!).render(<App/>);
