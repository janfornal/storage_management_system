BEGIN;
DROP TABLE if exists HISTORIA_CEN;  --delete before PRODUKTY
DROP TABLE if exists PRODUKTY_SPRZEDAZ;   -- delete before PRODUKTY (COMMENT)
DROP TABLE if exists SPRZEDAZE;    --delete after PRODUKTY_SPRZEDAZ
DROP TABLE if exists STAN_MAGAZYNU;  --delete before PRODUKTY
DROP TABLE if exists PRODUKTY;   --(COMMENT) 
DROP TABLE if exists MARKA;    --delete after PRODUKTY

CREATE TABLE MARKA (
    id NUMERIC(4) PRIMARY KEY,
    nazwa VARCHAR(20)
);

CREATE TABLE PRODUKTY (   
    id NUMERIC(10) PRIMARY KEY,
    id_kategoria NUMERIC(6),   --some reference here
    nazwa VARCHAR(20),
    id_marki NUMERIC(4) REFERENCES MARKA(id)
);

CREATE TABLE STAN_MAGAZYNU (
    id_produktu NUMERIC(10) REFERENCES PRODUKTY(id) UNIQUE, --check if types agree
    ilosc NUMERIC(6) CHECK (ilosc >= 0)
);

CREATE TABLE SPRZEDAZE (
    id_sprzedazy NUMERIC(10) PRIMARY KEY, --check if types agree
    data DATE
);

CREATE TABLE PRODUKTY_SPRZEDAZ (
    id_sprzedazy NUMERIC(10) REFERENCES SPRZEDAZE(id_sprzedazy),
    id_produktu NUMERIC(10) REFERENCES PRODUKTY(id),
    CONSTRAINT ps_key UNIQUE (id_sprzedazy, id_produktu),
    ilosc NUMERIC(6) CHECK (ilosc >= 0)
);

CREATE TABLE HISTORIA_CEN (
    id_produktu NUMERIC(10) REFERENCES PRODUKTY(id), --check if types agree
    data_wprowadzenia DATE,
    cena_netto NUMERIC(8,2)
);


CREATE OR REPLACE FUNCTION update_stan_magazynu()
    RETURNS trigger AS
$$
BEGIN
    UPDATE STAN_MAGAZYNU
    SET ilosc = ilosc - NEW.ilosc
    WHERE id_produktu = NEW.id_produktu;
    RETURN NEW;
END;
$$ LANGUAGE 'plpgsql';

CREATE TRIGGER my_trigger
AFTER INSERT
ON PRODUKTY_SPRZEDAZ
FOR EACH ROW
EXECUTE PROCEDURE update_stan_magazynu();

COMMIT;