# USAGE (assuming you have python3 on your PATH)
#
# pip3 install Faker
# python3 gen_upper.py > gen.sql
# psql < gen.sql

from random import choice, randint, sample, random
from collections import defaultdict
from faker import Faker
from datetime import datetime, timedelta


def convert(inp) -> str:
    if inp is None:
        return '\\N'
    return str(inp)


def print_tab(lst: list, name: str) -> None:
    print(f'COPY {name} FROM stdin; ')
    for el in lst:
        print('\t'.join(map(convert, el)))
    print('\\.\n')


def load_products() -> list:
    ids = list()
    for line in open("data/products.csv"):
        try:
            a, comma, b = line.partition(',')
            ids.append(int(a))
        except Exception:
            pass

    return ids


def load_brands() -> list:
    brands = list()
    for line in open("data/brand.csv"):
        try:
            a, comma, b = line.partition(',')
            int(a)
            brands.append(b.strip())
        except Exception:
            pass

    return brands


def gen_upper():
    PRODUCTS, BRANDS = load_products(), load_brands()

    SUPPLIERS, EMPLOYEES, DELIVERIES, PRODUCTS_DELIVERIES = list(), list(), list(), list()
    ADDRESSES, CLIENTS, SALES, PRODUCTS_SOLD = list(), list(), list(), list()
    CLIENTS_RETURN, COMPLAINT, PRODUCTS_PROBLEMS, INTERNET_SALE = list(), list(), list(), list()

    EMPLOYEES_C, ADDRESSES_C, CLIENTS_C = 15, 35, 70
    SIM_BEGINS, DAYS_TO_SIM, EVENTS_PER_DAY = datetime(2019, 10, 1), 700, range(0, 50)
    DELIVERY_ENTRIES_C, DELIVERY_PRODUCT_QUANTITY = range(10, 100), range(10, 100)
    SALE_ENTRIES_C, SALE_PRODUCT_QUANTITY = range(1, 8), range(1, 5)
    MX_PROD, PROBABILITY_DELIVERY, PROBABILITY_INTERNET = 1000, 0.01, 0.33

    status = defaultdict(int)
    fake = Faker()

    # SUPPLIERS
    for name in BRANDS:
        rn = randint(1, 3)
        if rn == 1:
            name += " GLOBAL"
        if rn == 2:
            name += " EUROPE"
        SUPPLIERS.append((name, ))
    SUPPLIERS_C = len(BRANDS)

    # ADDRESSES
    for _ in range(ADDRESSES_C):
        post = fake.postcode()
        city = fake.city()
        street = fake.street_name()
        build = fake.building_number()
        flat = randint(1, 20)
        ADDRESSES.append((post, city, street, build, flat))

    # EMPLOYEES
    for _ in range(EMPLOYEES_C):
        fname = fake.first_name()
        lname = fake.last_name()
        username = lname.lower() + str(randint(1, 999))
        password = randint(0, 2 ** 30)
        EMPLOYEES.append((username, password, fname, lname))

    # CLIENTS
    for _ in range(CLIENTS_C):
        fname = fake.first_name()
        lname = fake.last_name()
        username = lname.lower() + str(randint(1, 999))
        password = randint(0, 2 ** 30)
        email = f'{lname}{choice([".", "_", ""])}{choice([fname, ""])}{str(randint(1, 9999))}@{fake.domain_name()}'
        tele = choice([None, f'+48{fake.msisdn()[4:]}'])
        addr = randint(1, ADDRESSES_C)
        CLIENTS.append((username, password, fname, lname, email, tele, addr))

    def try_to_generate_delivery(when: datetime) -> bool:
        nonlocal DELIVERIES, DELIVERY_PRODUCT_QUANTITY, MX_PROD, DELIVERY_ENTRIES_C, PRODUCTS_DELIVERIES

        del_id = len(DELIVERIES) + 1
        del_prods = [(del_id, id_p, choice(DELIVERY_PRODUCT_QUANTITY)) for id_p in sample(PRODUCTS, choice(DELIVERY_ENTRIES_C))]
        del_prods = list(filter(lambda x: status[x[1]] + x[2] < MX_PROD, del_prods))

        if len(del_prods) not in DELIVERY_ENTRIES_C:
            return False

        for _, id_p, q in del_prods:
            status[id_p] += q

        DELIVERIES.append((randint(1, SUPPLIERS_C), when))
        PRODUCTS_DELIVERIES += del_prods
        return True

    def try_to_generate_sale(when: datetime) -> bool:
        nonlocal SALES, SALE_PRODUCT_QUANTITY, PRODUCTS, SALE_ENTRIES_C, PRODUCTS_SOLD, ADDRESSES

        sale_id = len(SALES) + 1
        sale_prods = [(sale_id, id_p, choice(SALE_PRODUCT_QUANTITY)) for id_p in sample(PRODUCTS, choice(SALE_ENTRIES_C))]
        sale_prods = list(filter(lambda x: status[x[1]] >= x[2], sale_prods))

        if len(sale_prods) not in SALE_ENTRIES_C:
            return False

        for _, id_p, q in sale_prods:
            status[id_p] -= q

        SALES.append((when, ))
        PRODUCTS_SOLD += sale_prods

        if random() < PROBABILITY_INTERNET:
            INTERNET_SALE.append((sale_id, when + timedelta(days=randint(1, 3)), 'poczta', randint(1, ADDRESSES_C)))

        return True

    def try_to_generate(when: datetime) -> bool:
        if random() < PROBABILITY_DELIVERY:
            return try_to_generate_delivery(when)
        else:
            return try_to_generate_sale(when)

    # DELIVERIES, SALES, INTERNET_SALE
    for event in sorted([fake.date_time_between(SIM_BEGINS, SIM_BEGINS + timedelta(days=DAYS_TO_SIM)) for _ in
                         range(choice(EVENTS_PER_DAY) * DAYS_TO_SIM)]):
        while not try_to_generate(event):
            pass

    # CLIENTS_RETURN, COMPLAINT, PRODUCTS_PROBLEMS

    print('BEGIN;')

    print_tab(SUPPLIERS, 'SUPPLIERS (name)')
    print_tab(EMPLOYEES, 'EMPLOYEES (login, password, first_name, last_name)')
    print_tab(DELIVERIES, 'DELIVERIES (id_supplier, date_delivery)')
    print_tab(PRODUCTS_DELIVERIES, 'PRODUCTS_DELIVERIES (id_delivery, id_product, quantity)')

    print_tab(ADDRESSES, 'ADDRESSES (postal_code, city, street, building_number, flat_number)')
    print_tab(CLIENTS, 'CLIENTS (login, password, first_name, last_name, email, phone, id_address)')
    print_tab(SALES, 'SALES (sales_date)')
    print_tab(PRODUCTS_SOLD, 'PRODUCTS_SOLD (id_sale, id_product, quantity)')

    print_tab(CLIENTS_RETURN, 'CLIENTS_RETURN (id_sale, id_product, quantity, return_date)')
    print_tab(COMPLAINT, 'COMPLAINT (id_product, id_sale, quantity, complaint_date, complaint_description, result_date, complaint_accepted, id_employee)')
    print_tab(PRODUCTS_PROBLEMS, 'PRODUCTS_PROBLEMS (id_product, quantity, exhibition, returned, problem_description, discount)')
    print_tab(INTERNET_SALE, 'INTERNET_SALE (id_sale, date_delivery, method_delivery, id_address)')

    print('COMMIT;')


if __name__ == '__main__':
    gen_upper()
