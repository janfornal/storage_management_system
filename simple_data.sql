BEGIN;
COPY BRAND (id_brand, name) FROM stdin;
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

COPY PARAMETERS (id_parameter, name, unit) FROM stdin;
1	RAM	GB
2	"CPU threads"	\N
\.

COPY CATEGORIES (id_category, name, vat) FROM stdin;
4	Laptops	23
5	UNKNOWN	0
6	Phones	23
8	TVs	23
9	"Household appliances"	23
\.

COPY PRODUCTS (id, id_category, name, id_brand) FROM stdin;
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

COPY POSSIBLE_PARAMETERS (id_category, id_parameter) FROM stdin;
4	1
4	2
\.

COPY PARAMETER_PRODUCTS (id_parameter, id_product, quantity) FROM stdin;
1	1	1
2	1	2
1	2	8
2	2	6
\.

UPDATE STORE_STATUS SET quantity = 40 WHERE id_product = 3;
UPDATE STORE_STATUS SET quantity = 29 WHERE id_product = 4;
UPDATE STORE_STATUS SET quantity = 13 WHERE id_product = 5;
UPDATE STORE_STATUS SET quantity = 27 WHERE id_product = 6;
UPDATE STORE_STATUS SET quantity = 8 WHERE id_product = 9;
UPDATE STORE_STATUS SET quantity = 11 WHERE id_product = 10;
UPDATE STORE_STATUS SET quantity = 9 WHERE id_product = 11;
UPDATE STORE_STATUS SET quantity = 13 WHERE id_product = 12;

COPY SALES (id_sale, sales_date) FROM stdin;
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

COPY PRODUCTS_SOLD (id_sale, id_product, quantity) FROM stdin;
20	3	7
20	4	20
70	10	3
\.

COPY PRICE_HISTORY (id_product, launch_date, net_price) FROM stdin;
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

COPY SUPPLIERS (id_supplier, name) FROM stdin;
100	LG
101	Samsung
102	Philips
\.

COPY DELIVERIES (id_delivery, id_supplier, date_delivery) FROM stdin;
200	100	2015-11-15 10:15:00
201	100	2015-08-02 09:00:00
202	101	2015-03-25 06:00:00
\.

COPY PRODUCTS_DELIVERIES (id_delivery, id_product, quantity) FROM stdin;
200	10	5
200	11	7
201	9	50
\.

COPY CLIENTS_RETURN (id_return, id_sale, id_product, quantity, return_date) FROM stdin;
1	20	3	3	2015-01-26 22:00:00
2	20	4	3	2015-01-26 22:00:00
3	20	3	4	2015-01-26 23:00:00
\.

COMMIT;
