CREATE TABLE users (
    UserID VARCHAR2(50) NOT NULL,          
    UserName VARCHAR2(20) NOT NULL,        
    UserPwd VARCHAR2(80) NOT NULL,
    UserRole VARCHAR2(20) NOT NULL,
    UserEmail VARCHAR2(80) NOT NULL UNIQUE, 
    UserPh VARCHAR2(20) NOT NULL,          
    UserZipcode VARCHAR2(20) NOT NULL,     
    UserAddress1 VARCHAR2(100) NOT NULL,   
    UserAddress2 VARCHAR2(50) NOT NULL,             
    PRIMARY KEY (UserID)                   
);