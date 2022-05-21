BEGIN;
-- let's create a temp table to bulk data into
create temporary table temp_json (text_values text) on commit drop;
\copy temp_json from './data/XXXXXXX.json';

-- uncomment the line above to insert records into your table
insert into categories (name, vat) values ('XXXXXXX', (array[0, 8, 23])[floor(random() * 3 + 1)]);
insert into brand (name) 
    select distinct values->>'brand'
    from (
        select text_values::json as values from temp_json
    ) as tj on conflict do nothing;
do $$
begin
    CREATE TEMP TABLE id_table(id_parameter int);
    with inserted as (
        insert into parameters (name, unit)
            select 
            properties_keys.simple_value, NULL
            from (
                select distinct
                json_object_keys((values->>'properties')::json) as simple_value
                from (
                    select text_values::json as values from temp_json
                ) as tj
            ) as properties_keys on conflict do nothing returning id_parameter
    )
    insert into id_table
    select id_parameter
    from inserted;
    insert into possible_parameters (id_category, id_parameter)
        select
        (select id_category from categories where name='XXXXXXX'),
        id_parameter
        from (select id_parameter from id_table) as foo;
end$$; 
do $$
declare
object_line json;
returned_id integer;
new_year timestamp;
price numeric(8, 2);
appender numeric(8, 2) = 0.00;
begin
    for object_line in select text_values::json as values 
    from temp_json
    loop 
        insert into products (id_category, name, id_brand) 
            values 
            ((select id_category from categories where name='XXXXXXX'),
            object_line->>'product',
            (select id_brand from brand where name=object_line->>'brand'))
            returning id into returned_id; 
        insert into parameter_products (id_parameter, id_product, quantity)
            select 
            (select id_parameter from parameters where name=entries.key),
            returned_id,
            entries.value::varchar(100)
            from json_each((object_line->>'properties')::json) as entries;
        price = replace((object_line->>'price'), ',', '')::numeric(8, 2);
        if (30.00 <= price AND price < 100.00) then
            appender = (array[8.00, 10.00, 12.00])[floor(random() * 3 + 1)];
        elsif (100.00 <= price AND price < 500.00) then
            appender = (array[30.00, 40.00])[floor(random() * 2 + 1)];
        elsif (500.00 <= price) then
            appender = (array[120.00, 180.00])[floor(random() * 2 + 1)];
        end if;
        FOREACH new_year IN ARRAY array['2019-01-01 00:00:00', '2020-01-01 00:00:00', '2021-01-01 00:00:00']::timestamp[]
        LOOP
            insert into price_history (id_product, launch_date, net_price) 
                values
                (returned_id,
                new_year + (array['3 months', '6 months', '9 months']::interval[])[floor(random() * 3 + 1)],
                price + appender*((array[0, 1])[floor(random() * 2 + 1)]));
        end loop;
    end loop;
end$$;
COMMIT;