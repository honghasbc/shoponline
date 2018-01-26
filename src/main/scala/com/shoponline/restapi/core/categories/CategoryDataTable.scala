package com.shoponline.restapi.core.categories

import com.shoponline.restapi.core.CategoryDta
import com.shoponline.restapi.utils.db.DatabaseConnector

trait CategoryDataTable {

  protected val databaseConnector: DatabaseConnector
  import databaseConnector.profile.api._

  class Category(tag: Tag) extends Table[CategoryDta](tag, "Categories") {

//    CONSTRAINT Product_pk PRIMARY KEY ("id")

    def id = column[Int]("id", O.PrimaryKey)
    def name = column[String]("name")

    def * = (id, name ) <> ((CategoryDta.apply _).tupled, CategoryDta.unapply)
  }

  val categories = TableQuery[Category]

}
