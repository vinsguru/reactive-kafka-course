DROP TABLE IF EXISTS product;
CREATE TABLE product AS SELECT * FROM CSVREAD('classpath:product.csv');