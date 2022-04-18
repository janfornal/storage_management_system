BEGIN;
DROP TABLE IF EXISTS ZWROTY_KLIENTOW    CASCADE;
DROP TABLE IF EXISTS PRODUKTY_SPRZEDAZ  CASCADE; --delete before PRODUKTY (COMMENT)
DROP TABLE IF EXISTS SPRZEDAZE          CASCADE; --delete after PRODUKTY_SPRZEDAZ
DROP TABLE IF EXISTS PRODUKTY_DOSTAWY   CASCADE;
DROP TABLE IF EXISTS DOSTAWY            CASCADE;
DROP TABLE IF EXISTS STAN_MAGAZYNU      CASCADE; --delete before PRODUKTY
DROP TABLE IF EXISTS DOSTAWCY           CASCADE;
DROP TABLE IF EXISTS HISTORIA_CEN       CASCADE; --delete before PRODUKTY
DROP TABLE IF EXISTS PARAMETR_PRODUKTU  CASCADE;
DROP TABLE IF EXISTS WYMAGANE_PARAMETRY CASCADE;
DROP TABLE IF EXISTS PRODUKTY           CASCADE; --(COMMENT) 
DROP TABLE IF EXISTS PARAMETRY          CASCADE;
DROP TABLE IF EXISTS KATEGORIE          CASCADE;
DROP TABLE IF EXISTS MARKA              CASCADE; --delete after PRODUKTY

---------------------------------------------------------------

CREATE TABLE MARKA (
    id NUMERIC(4) PRIMARY KEY,
    nazwa VARCHAR(20)
);

CREATE TABLE KATEGORIE (
);

CREATE TABLE PARAMETRY (
);

CREATE TABLE PRODUKTY (
    id NUMERIC(10) PRIMARY KEY,
    id_kategoria NUMERIC(6),   --some reference here
    nazwa VARCHAR(20),
    id_marki NUMERIC(4) REFERENCES MARKA(id)
);

CREATE TABLE WYMAGANE_PARAMETRY (
);

CREATE TABLE PARAMETR_PRODUKTU (
);

CREATE TABLE HISTORIA_CEN (
    id_produktu NUMERIC(10) REFERENCES PRODUKTY(id), --check if types agree
    data_wprowadzenia DATE,
    cena_netto NUMERIC(8,2)
);

CREATE TABLE DOSTAWCY (
    id_dostawcy NUMERIC(10) PRIMARY KEY,
    nazwa VARCHAR(20)
);

CREATE TABLE STAN_MAGAZYNU (
    id_produktu NUMERIC(10) REFERENCES PRODUKTY(id) UNIQUE, --check if types agree
    ilosc NUMERIC(6) CHECK (ilosc >= 0)
);

CREATE TABLE DOSTAWY (
    id_dostawy NUMERIC(10) PRIMARY KEY,
    id_dostawcy NUMERIC(10) REFERENCES DOSTAWCY(id_dostawcy),
    data_dostawy DATE
);

CREATE TABLE PRODUKTY_DOSTAWY (
    id_dostawy NUMERIC(10) REFERENCES DOSTAWY(id_dostawy),
    id_produktu NUMERIC(10) REFERENCES PRODUKTY(id),
    CONSTRAINT pd_key UNIQUE (id_dostawy, id_produktu),
    ilosc NUMERIC(6) CHECK (ilosc > 0)
);

CREATE TABLE SPRZEDAZE (
    id_sprzedazy NUMERIC(10) PRIMARY KEY,
    data DATE
);

CREATE TABLE PRODUKTY_SPRZEDAZ (
    id_sprzedazy NUMERIC(10) REFERENCES SPRZEDAZE(id_sprzedazy),
    id_produktu NUMERIC(10) REFERENCES PRODUKTY(id),
    CONSTRAINT ps_key UNIQUE (id_sprzedazy, id_produktu),
    ilosc NUMERIC(6) CHECK (ilosc > 0)
);

CREATE TABLE ZWROTY_KLIENTOW (
    id_zwrotu NUMERIC(10) PRIMARY KEY,
    id_sprzedazy NUMERIC(10) REFERENCES SPRZEDAZE(id_sprzedazy),
    id_produktu NUMERIC(10) REFERENCES PRODUKTY(id),
    ilosc NUMERIC(6) CHECK (ilosc > 0),
    data DATE
);

---------------------------------------------------------------

CREATE OR REPLACE FUNCTION zwroty_ilosc_check(NUMERIC, NUMERIC)
    RETURNS NUMERIC(10) AS
$$
BEGIN
    RETURN (
        SELECT COALESCE(SUM(ilosc), 0)
        FROM PRODUKTY_SPRZEDAZ
        WHERE id_sprzedazy = $1 AND id_produktu = $2
    )-(
        SELECT COALESCE(SUM(ilosc), 0)
        FROM ZWROTY_KLIENTOW
        WHERE id_sprzedazy = $1 AND id_produktu = $2
    );
END;
$$ LANGUAGE 'plpgsql';

--additional constraint to control zwroty ilosc
ALTER TABLE ZWROTY_KLIENTOW
ADD CONSTRAINT zwroty_check CHECK(zwroty_ilosc_check(id_sprzedazy, id_produktu) >= 0);

CREATE OR REPLACE FUNCTION update_decrease_stan_magazynu()
    RETURNS trigger AS
$$
BEGIN
    UPDATE STAN_MAGAZYNU
    SET ilosc = ilosc - NEW.ilosc
    WHERE id_produktu = NEW.id_produktu;
    RETURN NEW;
END;
$$ LANGUAGE 'plpgsql';

CREATE OR REPLACE FUNCTION update_increase_stan_magazynu()
    RETURNS trigger AS
$$
BEGIN
    UPDATE STAN_MAGAZYNU
    SET ilosc = ilosc + NEW.ilosc
    WHERE id_produktu = NEW.id_produktu;
    RETURN NEW;
END;
$$ LANGUAGE 'plpgsql';

--trigger below disables putting new record in PRODUKTY_SPRZEDAZ where ilosc > ilosc in 
--respective record in stan_magazynu, also changes respective ilosc value
CREATE TRIGGER trigger_ilosc_ps  
AFTER INSERT
ON PRODUKTY_SPRZEDAZ
FOR EACH ROW
EXECUTE PROCEDURE update_decrease_stan_magazynu();

--updates record in stan_magazynu, after inserting into PRODUKTY_DOSTAWY
CREATE TRIGGER trigger_ilosc_pd  
AFTER INSERT
ON PRODUKTY_DOSTAWY
FOR EACH ROW
EXECUTE PROCEDURE update_increase_stan_magazynu();

CREATE OR REPLACE FUNCTION insert_id_products_to_stan_magazynu()
    RETURNS trigger AS
$$
BEGIN
    INSERT INTO STAN_MAGAZYNU (id_produktu, ilosc) VALUES (NEW.id, 0);
    RETURN NEW;
END;
$$ LANGUAGE 'plpgsql';

--name of trigger is long, but self-explanatory
CREATE TRIGGER trigger_add_id_product_to_stan_magazynu  
AFTER INSERT
ON PRODUKTY
FOR EACH ROW
EXECUTE PROCEDURE insert_id_products_to_stan_magazynu();

COMMIT;

