begin;
COPY MARKA (id, nazwa) FROM stdin;
206	Lenovo
207	Intel
208	Dell
209	HP
210	ACER
211	Beko
212	Panasonic
213	LG
214	Samsung
215	Philips
216	Sony
217	Whirlpool
218	Indesit
219	Haier
220	ASUS
221	Logitech
\.

COPY PRODUKTY (id, id_kategoria, nazwa, id_marki) FROM stdin;
1	4	Ideapad 330	206
2	4	XPS15	207
3	5	ABC	209
4	5	ACB	209
5	5	BAC	209
6	5	BCA	209
7	5	CAB	209
8	5	CBA	209
9	6	Galaxy A70	214
10	8	XR-55A90J	216
11	8	XR-85Z9JAEP	216
12	8	KD-55X85J	216
13	9	HW80-B14959U1-S	219
14	9	HTF-610DSN7	219
15	9	HWD120-B14979	219
16	9	HRC-45D2H	219
\.

UPDATE STAN_MAGAZYNU SET ilosc = 40 WHERE id_produktu = 3;
UPDATE STAN_MAGAZYNU SET ilosc = 29 WHERE id_produktu = 4;
UPDATE STAN_MAGAZYNU SET ilosc = 13 WHERE id_produktu = 5;
UPDATE STAN_MAGAZYNU SET ilosc = 27 WHERE id_produktu = 6;
UPDATE STAN_MAGAZYNU SET ilosc = 8 WHERE id_produktu = 9;
UPDATE STAN_MAGAZYNU SET ilosc = 11 WHERE id_produktu = 10;
UPDATE STAN_MAGAZYNU SET ilosc = 9 WHERE id_produktu = 11;
UPDATE STAN_MAGAZYNU SET ilosc = 13 WHERE id_produktu = 12;

COPY SPRZEDAZE (id_sprzedazy, data) FROM stdin;
10	2014-07-25 07:42:02.368
20	2014-01-26 21:53:23.456
30	2014-10-21 22:59:42.848
40	2014-01-25 11:58:15.04
50	2014-08-01 10:08:37.504
60	2014-10-08 10:03:18.016
70	2014-07-29 23:16:42.752
80	2014-03-24 23:27:04.704
90	2014-11-08 02:24:28.16
100	2014-08-30 01:23:58.72
\.

COPY PRODUKTY_SPRZEDAZ (id_sprzedazy, id_produktu, ilosc) FROM stdin;
20	3	7
20	4	20
70	10	3
\.

COPY HISTORIA_CEN (id_produktu, data_wprowadzenia, cena_netto) FROM stdin;
1	2015-02-01 12:51:01	2399.99
1	2015-04-16 06:39:56	2449.99
1	2015-05-03 13:53:21	2499.99
2	2015-07-19 19:24:46	2099.99
2	2015-08-27 04:50:30	1899.99
2	2015-11-04 02:45:55	2199.99
3	2015-09-18 14:43:23	139.99
3	2015-10-14 00:19:02	149.99
3	2015-12-27 11:03:05	99.99
4	2015-02-11 03:31:21	99.99
4	2015-02-22 02:49:56	139.99
4	2015-06-30 10:08:27	129.99
9	2015-06-26 21:01:08	1199.99
9	2015-10-26 05:44:12	999.99
9	2015-11-23 18:58:42	1199.99
10	2015-04-24 00:28:17	2099.99
10	2015-06-20 01:20:36	1899.99
10	2015-12-16 10:13:01	2199.99
11	2015-04-24 00:28:17	2199.99
11	2015-06-20 01:20:36	2099.99
11	2015-12-16 10:13:01	1899.99
12	2015-04-24 00:28:17	2099.99
12	2015-06-20 01:20:36	2099.99
12	2015-12-16 10:13:01	2399.99
\.

COMMIT;