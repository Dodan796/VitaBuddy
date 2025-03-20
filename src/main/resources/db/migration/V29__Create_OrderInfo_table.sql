create table OrderInfo(
	
	OrderId VARCHAR2(60) NOT NULL, 
    OrdDate TIMESTAMP (6) DEFAULT SYSDATE, 
    UserID VARCHAR2(100) NOT NULL,
    OrdRcvReceiver VARCHAR2(100) NOT NULL, 
    OrdRcvZipcode VARCHAR2(10) NOT NULL,   
    OrdRcvAddress1 VARCHAR2(200) NOT NULL, 
    OrdRcvAddress2 VARCHAR2(200),          
    OrdRcvPhone VARCHAR2(20) NOT NULL,     
    OrdRcvEmail VARCHAR2(100) NOT NULL,      
    OrdRcvMsg VARCHAR2(500),               
    OrdPay VARCHAR2(50) NOT NULL,
    Payment_key VARCHAR2(50),
    primary key(OrderId),
	foreign key(UserID) references users(UserID)
	
);