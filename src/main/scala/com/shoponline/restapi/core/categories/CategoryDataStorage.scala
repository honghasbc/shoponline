package com.shoponline.restapi.core.categories

import com.shoponline.restapi.core.CategoryDta
import com.shoponline.restapi.utils.db.DatabaseConnector

import scala.concurrent.{ExecutionContext, Future}

sealed trait CategoryDataStorage {

  def getCategories(): Future[Seq[CategoryDta]]
  def getCategory(id: Int): Future[Option[CategoryDta]]
  def saveCategory(category: CategoryDta): Future[CategoryDta]

}

class JdbcCategoryStorage(
  val databaseConnector: DatabaseConnector
)(implicit executionContext: ExecutionContext) extends CategoryDataTable with CategoryDataStorage {

  import databaseConnector._
  import databaseConnector.profile.api._

  def getCategories(): Future[Seq[CategoryDta]] = db.run(categories.result)

  def getCategory(id: Int): Future[Option[CategoryDta]] = db.run(categories.filter(_.id === id).result.headOption)

  def saveCategory(categoryDta: CategoryDta): Future[CategoryDta] =
    db.run(categories.insertOrUpdate(categoryDta)).map(_ => categoryDta)

}

class InMemoryCategoryStorage extends CategoryDataStorage {

  private var state: Seq[CategoryDta] = Nil

  override def getCategories(): Future[Seq[CategoryDta]] =
    Future.successful(state)

  override def getCategory(id: Int): Future[Option[CategoryDta]] =
    Future.successful(state.find(_.id == id))

  override def saveCategory(categoryDta: CategoryDta): Future[CategoryDta] =
    Future.successful {
      state = state.filterNot(_.id == categoryDta.id)
      state = state :+ categoryDta
      categoryDta
    }

}