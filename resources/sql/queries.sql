--name: create-listing!
-- creates a new listing record
INSERT INTO listings
(author, title, sku, price)
VALUES (:author, :title, :sku, :price)

--name: update-listing!
-- update an existing listing record
UPDATE listings
SET author = :author, title = :title, sku = :sku, price = :price
WHERE id = :id

-- name: get-listing
-- retrieve a used given the id.
SELECT * FROM listings
WHERE id = :id

-- name: get-all-listings
-- retrieve a used given the id.
SELECT * FROM listings
