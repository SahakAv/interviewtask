CREATE TABLE  part(
   name VARCHAR(255) NOT NULL,
   number VARCHAR(255) NOT NULL,
   vendor VARCHAR(255) NOT NULL,
   qty INT NOT NULL,
   shipped date NOT NULL,
   received date NOT NULL,
   PRIMARY KEY (name)
);

