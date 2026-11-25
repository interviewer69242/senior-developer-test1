export type TradeRequest = {clientId:string;clientTradeId:string;instrument:string;side:'BUY'|'SELL';quantity:string;price:string};
export type Trade = {id:string;request:TradeRequest;createdAt:string};
export async function submitTrade(request:TradeRequest):Promise<Trade>{
  const response=await fetch('/trades',{method:'POST',headers:{'Content-Type':'application/json'},body:JSON.stringify(request)});
  if(!response.ok) throw new Error('HTTP '+response.status);
  return response.json();
}
