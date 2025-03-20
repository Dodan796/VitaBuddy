create table cart(
    CartID NUMBER GENERATED ALWAYS AS IDENTITY MINVALUE 1 MAXVALUE 99999999 INCREMENT BY 1 START WITH 1 NOT NULL, 
    UserID VARCHAR2(100) not null,
    SupID NUMBER not null,
    CartQty NUMBER not null,
    primary key(CartID),
    foreign key(UserID) references users(UserID),
    foreign key(SupID) references supplement(SupID)
); 