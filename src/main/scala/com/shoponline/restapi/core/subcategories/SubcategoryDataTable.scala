package com.shoponline.restapi.core.subcategories

import com.shoponline.restapi.core.SubcategoryDta
import com.shoponline.restapi.core.categories.CategoryDataTable
import com.shoponline.restapi.utils.db.DatabaseConnector

trait SubcategoryDataTable extends CategoryDataTable {

  protected val databaseConnector: DatabaseConnector
  import databaseConnector.profile.api._

  class Subcategory(tag: Tag) extends Table[SubcategoryDta](tag, "Subcategories") {

    def id = column[Int]("id", O.PrimaryKey)
    def idCategory = column[Int]("category_id")
    def name = column[String]("name")

    def * = (id, idCategory, name ) <> ((SubcategoryDta.apply _).tupled, SubcategoryDta.unapply)
    // A reified foreign key relation that can be navigated to create a join
    def catagorier = foreignKey("Subcategories_fk0", idCategory, categories)(_.id)
  }

  val subcategories = TableQuery[Subcategory]

}
