from random import choice, randint, sample
from faker import Faker

def convert(inp) -> str:
    if inp is None:
        return '\\N'
    return str(inp)

def print_tab(lst : list, name : str):
    print(f'COPY {name} FROM stdin; ')
    for el in lst:
        print('\t'.join(map(convert, el)))
    print('\\.\n')

def load_product_ids():
    ids = list()
    for line in open("data/products.csv"):
        try:
            a, comma, b = line.partition(',')
            ids.append(int(a))
        except Exception:
            pass 
    return ids

def load_brands():
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
    PRODUCT_IDS = load_product_ids()
    BRANDS = load_brands()

    SUPPLIERS = list()
    EMPLOYEES = list()
    DELIVERIES = list()
    PRODUCTS_DELIVERIES = list()
    
    ADDRESSES = list()
    CLIENTS = list()

    SALES = list()
    PRODUCTS_SOLD = list()
    CLIENTS_RETURN = list()
    COMPLAINT = list()
    PRODUCTS_PROBLEMS = list()
    INTERNET_SALE = list()

    fake = Faker()

    for name in BRANDS:
        rn = randint(1,3)
        if rn == 0: 
            name += " GLOBAL"
        if rn == 1: 
            name += " EUROPE"
        SUPPLIERS.append((len(SUPPLIERS), name))

    for i in range(50):
        fname = fake.first_name()
        lname = fake.last_name()
        username = lname.lower() + str(randint(1,999))
        password = randint(0, 2**30)
        EMPLOYEES.append((i, username, password, fname, lname))

    for i in range(50):
        date = fake.date_time_between('-5y','-1y');
        DELIVERIES.append((i, choice(SUPPLIERS)[0], date))

    for id_del, supp, dt in DELIVERIES:
        for id_prod in sample(PRODUCT_IDS, randint(1, 15)):
            PRODUCTS_DELIVERIES.append((id_del, id_prod, randint(1,20)*10))

    for i in range(20):
        post = fake.postcode()
        city = fake.city()
        street = fake.street_name()
        build = fake.building_number()
        flat = randint(1,20)
        ADDRESSES.append((i, post, city, street, build, flat))


    for i in range(50):
        fname = fake.first_name()
        lname = fake.last_name()
        username = lname.lower() + str(randint(1,999))
        password = randint(0, 2**30)
        email = f'{lname}{choice([".","_",""])}{choice([fname, ""])}{str(randint(1,9999))}@{fake.domain_name()}'
        tele = choice([None, f'+48{fake.msisdn()[4:]}'])
        addr = choice(ADDRESSES)[0]
        CLIENTS.append((i, username, password, fname, lname, email, tele, addr))

    for i in range(50):
        date = fake.date_time_between('-5y','-1y');
        SALES.append((i, date))

    #for id_del, dt in SALES:
    #    for id_prod in sample(PRODUCT_IDS, randint(1, 15)):
    #        PRODUCTS_SOLD.append((id_del, id_prod, randint(1,20)*10))



    print('BEGIN;')
    print_tab(SUPPLIERS, 'SUPPLIERS (id_supplier, name)')
    print_tab(EMPLOYEES, 'EMPLOYEES (id_employee, login, password, first_name, last_name)')
    print_tab(DELIVERIES, 'DELIVERIES (id_delivery, id_supplier, date_delivery)')
    print_tab(PRODUCTS_DELIVERIES, 'PRODUCTS_DELIVERIES (id_delivery, id_product, quantity)')
    
    print_tab(ADDRESSES, 'ADDRESSES (id_address, postal_code, city, street, building_number, flat_number)')
    print_tab(CLIENTS, 'CLIENTS (id_client, login, password, first_name, last_name, email, phone, id_address)')
    
    print_tab(SALES, 'SALES (id_sale, sales_date)')
    print_tab(PRODUCTS_SOLD, 'PRODUCTS_SOLD (id_sale, id_product, quantity)')
    print_tab(CLIENTS_RETURN, 'CLIENTS_RETURN (id_return, id_sale, id_product, quantity, return_date)')
    print_tab(COMPLAINT, 'COMPLAINT (id_complaint, id_product, id_sale, quantity, complaint_date, complaint_description, result_date, complaint_accepted, id_employee)')
    print_tab(PRODUCTS_PROBLEMS, 'PRODUCTS_PROBLEMS (id_product, quantity, exhibition, returned, problem_description, discount)')
    print_tab(INTERNET_SALE, 'INTERNET_SALE (id_sale, date_delivery, method_delivery, id_address)')
    print('COMMIT;')

    # gen_upper END

if __name__ == '__main__':
    gen_upper()