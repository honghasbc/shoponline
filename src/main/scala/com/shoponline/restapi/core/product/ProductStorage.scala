package com.shoponline.restapi.core.product

import com.shoponline.restapi.utils.db.DatabaseConnector
import com.shoponline.restapi.core.ProductDta

import scala.concurrent.{ExecutionContext, Future}

sealed trait ProductStorage {

  def getProducts(): Future[Seq[ProductDta]]
  def getProduct(id: Int): Future[Option[ProductDta]]
  def saveProduct(product: ProductDta): Future[ProductDta]

}

class JdbcProductStorage(
  val databaseConnector: DatabaseConnector
)(implicit executionContext: ExecutionContext) extends ProductTable with ProductStorage {

  import databaseConnector._
  import databaseConnector.profile.api._

  def getProducts(): Future[Seq[ProductDta]] = db.run(product.result)

  def getProduct(id: Int): Future[Option[ProductDta]] = db.run(product.filter(_.id === id).result.headOption)

  def saveProduct(productDta: ProductDta): Future[ProductDta] =
    db.run(product.insertOrUpdate(productDta)).map(_ => productDta)

}

class InMemoryProductStorage extends ProductStorage {

  private var state: Seq[ProductDta] = Nil

  override def getProducts(): Future[Seq[ProductDta]] =
    Future.successful(state)

  override def getProduct(id: Int): Future[Option[ProductDta]] =
    Future.successful(state.find(_.id == id))

  override def saveProduct(product: ProductDta): Future[ProductDta] =
    Future.successful {
      state = state.filterNot(_.id == product.id)
      state = state :+ product
      product
    }

}