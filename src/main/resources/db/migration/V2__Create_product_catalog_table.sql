CREATE TABLE IF NOT EXISTS "Product" (
	"id" serial NOT NULL,
	"category_id" INT NOT NULL,
	"subcategory_id" INT NOT NULL,
	"product_display_name" varchar(200) NOT NULL,
	"price" DOUBLE PRECISION NOT NULL,
	"product_short_desc" varchar(100) NOT NULL,
	"product_long_desc" varchar(500) NOT NULL,
	"is_active" BOOLEAN NOT NULL,
	"thumbnail_image" varchar NOT NULL,
	"small_image" varchar NOT NULL,
	"create_date" varchar NOT NULL,
	"last_update_date" varchar NOT NULL,
	"manufacturer" varchar NOT NULL,
	"weight" FLOAT NOT NULL,
	CONSTRAINT Product_pk PRIMARY KEY ("id")
);



CREATE TABLE IF NOT EXISTS"Categories" (
	"id" serial NOT NULL,
	"name" varchar(200) NOT NULL,
	CONSTRAINT Categories_pk PRIMARY KEY ("id")
);



CREATE TABLE IF NOT EXISTS "Subcategories" (
	"id" serial NOT NULL,
	"category_id" INT NOT NULL,
	"name" varchar(200) NOT NULL,
	CONSTRAINT Subcategories_pk PRIMARY KEY ("id")
);



ALTER TABLE "Product" ADD CONSTRAINT "Product_fk0" FOREIGN KEY ("category_id") REFERENCES "Categories"("id");
ALTER TABLE "Product" ADD CONSTRAINT "Product_fk1" FOREIGN KEY ("subcategory_id") REFERENCES "Subcategories"("id");


ALTER TABLE "Subcategories" ADD CONSTRAINT "Subcategories_fk0" FOREIGN KEY ("category_id") REFERENCES "Categories"("id");

