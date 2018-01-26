package com.shoponline.restapi.core.product

import java.util.Date

import com.shoponline.restapi.core.ProductDta
import com.shoponline.restapi.utils.db.DatabaseConnector
import com.shoponline.restapi.core.categories.CategoryDataTable

private[product] trait ProductTable extends CategoryDataTable{

  protected val databaseConnector: DatabaseConnector
  import databaseConnector.profile.api._

  class Product(tag: Tag) extends Table[ProductDta](tag, "Product") {

//    CONSTRAINT Product_pk PRIMARY KEY ("id")

    def id = column[Int]("id", O.PrimaryKey)
    def categoryId = column[Int]("category_id")
    def subcategoryId = column[Int]("subcategory_id")
    def productDisplayName = column[String]("product_display_name")
    def price = column[Double]("price")
    def productShortDesc = column[String]("product_short_desc")
    def productLongDesc = column[String]("product_long_desc")
    def isActive = column[Boolean]("is_active")
    def thumbnailImage = column[String]("thumbnail_image")
    def smallImage = column[String]("small_image")
    def createDate = column[String]("create_date")
    def lastUpdateDate = column[String]("last_update_date")
    def manufacturer = column[String]("manufacturer")
    def weight = column[Float]("weight")

    def * = (id, categoryId, subcategoryId, productDisplayName, price,
      productShortDesc , productLongDesc, isActive, thumbnailImage, smallImage,
      createDate, lastUpdateDate ,manufacturer , weight) <> ((ProductDta.apply _).tupled, ProductDta.unapply)

    // A reified foreign key relation that can be navigated to create a join
    def categorier = foreignKey("Product_fk0", categoryId, categories)(_.id)
//    def subcategorier = foreignKey("Product_fk1", subcategoryId, subcategories)(_.id)
  }

  protected val product = TableQuery[Product]

}
