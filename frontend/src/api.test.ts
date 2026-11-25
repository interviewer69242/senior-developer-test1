import {describe,it,expect,vi,afterEach} from 'vitest';
import {submitTrade,type TradeRequest} from './api';
const request:TradeRequest={clientId:'a',clientTradeId:'one',instrument:'ABC',side:'BUY',quantity:'10',price:'12.50'};
afterEach(()=>vi.unstubAllGlobals());
describe('trade API',()=>{it('submits JSON and returns trade',async()=>{const f=vi.fn().mockResolvedValue(new Response(JSON.stringify({id:'abc',request}),{status:201}));vi.stubGlobal('fetch',f);expect((await submitTrade(request)).id).toBe('abc');expect(f).toHaveBeenCalledWith('/trades',expect.objectContaining({method:'POST',body:JSON.stringify(request)}));});it('reports a non-success response',async()=>{vi.stubGlobal('fetch',vi.fn().mockResolvedValue(new Response('',{status:500})));await expect(submitTrade(request)).rejects.toThrow('HTTP 500');});});
