package com.shoponline.restapi.core.product

import com.shoponline.restapi.utils.db.DatabaseConnector
import com.shoponline.restapi.core.{ProductDta,ProductCategoryDta}

import scala.concurrent.{ExecutionContext, Future}

sealed trait ProductStorage {

  def getProducts(): Future[Seq[ProductCategoryDta]]
  def getProduct(id: Int): Future[Option[ProductDta]]
  def saveProduct(product: ProductDta): Future[ProductDta]
  def deleteProduct(id: Int): Future[Option[Int]]
  def getProductCategory(id: Int): Future[Option[ProductCategoryDta]]
}

class JdbcProductStorage(
  val databaseConnector: DatabaseConnector
)(implicit executionContext: ExecutionContext) extends ProductTable with ProductStorage {

  import databaseConnector._
  import databaseConnector.profile.api._

  def getProducts(): Future[Seq[ProductCategoryDta]] = {
    val join = for {
      ((p, c),s) <- product join categories on (_.categoryId === _.id) join subcategories on (_._1.subcategoryId === _.id)
    } yield (p,c,s)

    val query = join.map{
      case (p,c,s) => ((p.id, p.categoryId, p.subcategoryId, p.productDisplayName,c.name, s.name, p.price,p.productShortDesc, p.productLongDesc,
        p.isActive, p.thumbnailImage, p.smallImage,p.createDate,p.lastUpdateDate,p.manufacturer,p.weight) <> (ProductCategoryDta.tupled, ProductCategoryDta.unapply _))
    }
    val productCategoryDta:Future[Seq[ProductCategoryDta]]= db.run(query.result)
    productCategoryDta
  }

  def getProduct(id: Int): Future[Option[ProductDta]] = db.run(product.filter(_.id === id).result.headOption)

  def getProductCategory(id: Int): Future[Option[ProductCategoryDta]] = {
    val join = for {
      ((p, c),s) <- product.filter(_.id === id) join categories on (_.categoryId === _.id) join subcategories on (_._1.subcategoryId === _.id)
    } yield (p,c,s)

    val query = join.map{
      case (p,c,s) => ((p.id, p.categoryId, p.subcategoryId, p.productDisplayName,c.name, s.name, p.price,p.productShortDesc, p.productLongDesc,
        p.isActive, p.thumbnailImage, p.smallImage,p.createDate,p.lastUpdateDate,p.manufacturer,p.weight) <> (ProductCategoryDta.tupled, ProductCategoryDta.unapply _))
    }
    val productCategoryDta:Future[Option[ProductCategoryDta]]= db.run(query.result.headOption)
    productCategoryDta
  }


  def saveProduct(productDta: ProductDta): Future[ProductDta] =
    db.run(product.insertOrUpdate(productDta)).map(_ => productDta)

  def deleteProduct(id: Int): Future[Option[Int]] = {
    val q = product.filter(_.id === id)
    val action = q.delete
    val affectedRowsCount: Future[Int] = db.run(action)
    val sql = action.statements.head
    Future(Option(id))
  }

}

class InMemoryProductStorage extends ProductStorage {

  private var state: Seq[ProductDta] = Nil
  private var state2: Seq[ProductCategoryDta] = Nil

  override def getProducts(): Future[Seq[ProductCategoryDta]] =
    Future.successful(state2)

  override def getProduct(id: Int): Future[Option[ProductDta]] =
    Future.successful(state.find(_.id == id))

  override def saveProduct(product: ProductDta): Future[ProductDta] =
    Future.successful {
      state = state.filterNot(_.id == product.id)
      state = state :+ product
      product
    }

  override def deleteProduct(id: Int): Future[Option[Int]] =
    Future.successful(state.find(_.id == id).map(_ => id ))

  override def getProductCategory(id: Int): Future[Option[ProductCategoryDta]] =
    Future.successful(state2.find(_.id == id))
}