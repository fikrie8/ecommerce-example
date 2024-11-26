ALTER TABLE users MODIFY username varchar(255) AFTER id;
ALTER TABLE users MODIFY password varchar(255) AFTER username;
ALTER TABLE users MODIFY name varchar(255) AFTER password;
ALTER TABLE users MODIFY email varchar(255) AFTER name;
ALTER TABLE users MODIFY address varchar(255) AFTER email;
ALTER TABLE users MODIFY role varchar(255) AFTER address;

INSERT INTO users (username, password, name, email, address, role)
VALUES ('fikrie88', 'sheila8886', 'Mohd Fikrie', 'fikrie@123.com', 'Bandar Hillpark, Puncak Alam', 'ADMIN')
ON DUPLICATE KEY UPDATE
username = 'fikrie8886',
password = 'Sheila8886',
name = 'Mohd Fikrie88',
email = 'fikrie@123.com',
address = 'Kuala Lumpur',
role = 'ADMIN';