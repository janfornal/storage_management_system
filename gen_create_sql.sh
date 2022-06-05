#!/bin/bash
cat clear.sql create_tables.sql create_triggers.sql functions.sql inserter.sql > create.sql
python3 generate_data.py >> create.sql
