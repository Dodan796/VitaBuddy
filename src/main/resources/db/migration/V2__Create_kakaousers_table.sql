CREATE TABLE kakaousers (
    UserID VARCHAR2(100) NOT NULL,
    UserName VARCHAR2(20),
    UserEmail VARCHAR2(80) UNIQUE,
    PRIMARY KEY (UserID)                   
);

