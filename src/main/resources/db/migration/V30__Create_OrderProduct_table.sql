create table OrderProduct(
	
	OrderId not null,
	SupID NUMBER not null,
	OrdQty NUMBER not null,
	foreign key(OrderId) references OrderInfo(OrderId),
	foreign key(SupID) references supplement(SupID)

);