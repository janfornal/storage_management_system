BEGIN;
DROP TABLE IF EXISTS kategorie;
DROP TABLE IF EXISTS wymagane_parametry;
DROP TABLE IF EXISTS parametry_produktu;
DROP TABLE IF EXISTS parametry;

CREATE TABLE kategorie(
    id NUMERIC(4) PRIMARY KEY,
    nazwa varchar(100) NOT NULL UNIQUE,
    VAT NUMERIC(3) NOT NULL
);

CREATE TABLE wymagane_parametry(
    id_kategoria NUMERIC(4) NOT NULL REFERENCES kategorie(id),
    id_parametru NUMERIC(4) NOT NULL REFERENCES parametry(id)
);

CREATE TABLE parametry_produktu(
    id_parametru NUMERIC(4) PRIMARY KEY,
    id_produktu NUMERIC(4) NOT NULL REFERENCES produkty(id),
    ilosc NUMERIC(4)
);
CREATE TABLE parametry(
    id NUMERIC(4) PRIMARY KEY,
    nazwa varchar(100),
    jednostka varchar(100)
);
--size of id,nazwa needs to be discussed

COMMIT;