CREATE TABLE ingredient_recommendMapping (
    RecommendId VARCHAR2(30) NOT NULL,
    IngredientId VARCHAR2(50) NOT NULL,
    FOREIGN KEY (RecommendId) REFERENCES ingredient_recommend(IngredientRecommendID),
    FOREIGN KEY (IngredientId) REFERENCES Ingredient(IngredientId)
);