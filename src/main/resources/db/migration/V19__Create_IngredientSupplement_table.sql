CREATE TABLE IngredientSupplement (
    SupId INTEGER NOT NULL PRIMARY KEY,
    IngredientId VARCHAR2(30) NOT NULL,
    FOREIGN KEY (SupId) REFERENCES supplement(SupId), 
    FOREIGN KEY (IngredientId) REFERENCES ingredient(IngredientId) 
);
