package com.shoponline.restapi.core.categories

import com.shoponline.restapi.core.{CategoryDta, CategoryUpdate}
import com.shoponline.restapi.utils.MonadTransformers._

import scala.concurrent.{ExecutionContext, Future}

class CategoryService(categoryStorage: CategoryDataStorage
)(implicit executionContext: ExecutionContext) {

  def getCategories(): Future[Seq[CategoryDta]] =
    categoryStorage.getCategories()

  def getCategory(id: String): Future[Option[CategoryDta]] =
    categoryStorage.getCategory(Integer.parseInt(id))

  def createCategory(product: CategoryDta): Future[CategoryDta] =
    categoryStorage.saveCategory(product)

  def updateCategory(id: String, categoryUpdate: CategoryUpdate): Future[Option[CategoryDta]] =
    categoryStorage
      .getCategory(Integer.parseInt(id))
      .mapT(categoryUpdate.merge)
      .flatMapTOuter(categoryStorage.saveCategory)

}
