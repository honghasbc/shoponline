package com.shoponline.restapi.core.subcategories

import com.shoponline.restapi.core.SubcategoryDta
import com.shoponline.restapi.utils.db.DatabaseConnector

import scala.concurrent.{ExecutionContext, Future}

sealed trait SubcategoryDataStorage {

  def getSubcategories(): Future[Seq[SubcategoryDta]]
  def getSubcategory(id: Int): Future[Option[SubcategoryDta]]
  def saveSubcategory(category: SubcategoryDta): Future[SubcategoryDta]

}

class JdbcSubcategoryStorage(
  val databaseConnector: DatabaseConnector
)(implicit executionContext: ExecutionContext) extends SubcategoryDataTable with SubcategoryDataStorage {

  import databaseConnector._
  import databaseConnector.profile.api._

  def getSubcategories(): Future[Seq[SubcategoryDta]] = db.run(subcategories.result)

  def getSubcategory(id: Int): Future[Option[SubcategoryDta]] = db.run(subcategories.filter(_.id === id).result.headOption)

  def saveSubcategory(SubcategoryDta: SubcategoryDta): Future[SubcategoryDta] =
    db.run(subcategories.insertOrUpdate(SubcategoryDta)).map(_ => SubcategoryDta)

}

class InMemorySubcategoryStorage extends SubcategoryDataStorage {

  private var state: Seq[SubcategoryDta] = Nil

  override def getSubcategories(): Future[Seq[SubcategoryDta]] =
    Future.successful(state)

  override def getSubcategory(id: Int): Future[Option[SubcategoryDta]] =
    Future.successful(state.find(_.id == id))

  override def saveSubcategory(SubcategoryDta: SubcategoryDta): Future[SubcategoryDta] =
    Future.successful {
      state = state.filterNot(_.id == SubcategoryDta.id)
      state = state :+ SubcategoryDta
      SubcategoryDta
    }

}