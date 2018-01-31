package com.shoponline.restapi.core.product

import com.shoponline.restapi.core.{ProductDta, ProductUpdate}
import com.shoponline.restapi.utils.MonadTransformers._
import com.shoponline.restapi.utils.helps.Converters

import scala.concurrent.{ExecutionContext, Future}

class ProductService(productStorage: ProductStorage
)(implicit executionContext: ExecutionContext) {

  def getProducts(): Future[Seq[ProductDta]] =
    productStorage.getProducts()

  def getProduct(id: String): Future[Option[ProductDta]] =
    productStorage.getProduct(Converters.stringToInt(id))

  def createProduct(product: ProductDta): Future[ProductDta] =
    productStorage.saveProduct(product)

  def updateProduct(id: String, productUpdate: ProductUpdate): Future[Option[ProductDta]] =
    productStorage
      .getProduct(Converters.stringToInt(id))
      .mapT(productUpdate.merge)
      .flatMapTOuter(productStorage.saveProduct)

  def deleteProduct(id: String): Future[Option[Int]] =
    productStorage.deleteProduct(Integer.parseInt(id))

}
