CREATE OR REPLACE VIEW view_users_supplement AS
SELECT us.UserID, us.SupID, s.SupName, s.SupBrand, us.created_at
FROM users_supplement us
JOIN supplement s ON us.SupID = s.SupID
ORDER BY us.created_at;