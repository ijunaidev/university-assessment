# University Assessment

## Create database
Use the below command to create a database in MySQL
> Create database university;


## Create Table (Optional) - Hibernate will take care of it
Use the below command to create a table in `university` database in MySQL

```
  CREATE TABLE university_details
  (
     id             INT PRIMARY KEY,
     alpha_two_code VARCHAR(2),
     web_pages      TEXT,
     state_province VARCHAR(255),
     name           VARCHAR(255),
     domains        TEXT,
     country        VARCHAR(255)
  ); 
```