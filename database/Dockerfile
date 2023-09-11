FROM postgres:alpine

COPY ./init_database.sql /docker-entrypoint-initdb.d/

WORKDIR /docker-entrypoint-initdb.d