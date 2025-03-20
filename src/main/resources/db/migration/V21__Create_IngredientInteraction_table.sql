CREATE TABLE IngredientInteraction(
    InteractionNo VARCHAR2(30) NOT NULL PRIMARY KEY,
    InteractionIngredient1 VARCHAR2(30) NOT NULL,
    InteractionIngredient2 VARCHAR2(30),  
    InteractionDetail VARCHAR2(500),
    InteractionDosage VARCHAR2(500),
    FOREIGN KEY (InteractionIngredient1) REFERENCES Ingredient(IngredientId),
    FOREIGN KEY (InteractionIngredient2) REFERENCES Ingredient(IngredientId)
);