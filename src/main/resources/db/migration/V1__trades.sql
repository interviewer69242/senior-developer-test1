CREATE TABLE trades (
 id UUID PRIMARY KEY,
 client_id VARCHAR(80) NOT NULL,
 client_trade_id VARCHAR(80) NOT NULL,
 instrument VARCHAR(20) NOT NULL,
 side VARCHAR(4) NOT NULL CHECK (side IN ('BUY','SELL')),
 quantity NUMERIC(16,4) NOT NULL CHECK (quantity > 0),
 price NUMERIC(16,4) NOT NULL CHECK (price > 0),
 created_at TIMESTAMPTZ NOT NULL
);
