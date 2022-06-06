# storage_management_system

To initialize database use

    CREATE ROLE storagemanagementsystem LOGIN SUPERUSER PASSWORD 'systempassword';
    CREATE DATABASE storage WITH OWNER storagemanagementsystem;

    psql -d storage < create.sql
    
To delete all data use

    DROP DATABASE storage;
    DROP OWNED BY storagemanagementsystem;
    DROP ROLE storagemanagementsystem;
