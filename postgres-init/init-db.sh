#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE DATABASE orders_db;
    CREATE DATABASE inventory_db;
    CREATE DATABASE payments_db;
    CREATE DATABASE orders_test;
    CREATE DATABASE inventory_test;
EOSQL
