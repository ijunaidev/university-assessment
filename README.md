1- Create database
  Create database university;

1- Create table
CREATE TABLE university_details (
    id INT AUTO_INCREMENT PRIMARY KEY,
    alpha_two_code VARCHAR(2),
    web_pages TEXT,
    state_province VARCHAR(255),
    name VARCHAR(255),
    domains TEXT,
    country VARCHAR(255)
);
