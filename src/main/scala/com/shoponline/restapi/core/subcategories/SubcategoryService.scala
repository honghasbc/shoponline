package com.shoponline.restapi.core.subcategories

import com.shoponline.restapi.core.{SubcategoryDta, SubcategoryUpdate}
import com.shoponline.restapi.utils.MonadTransformers._
import com.shoponline.restapi.utils.helps.Converters
import scala.concurrent.{ExecutionContext, Future}

class SubcategoryService(subcategoryStorage: SubcategoryDataStorage
)(implicit executionContext: ExecutionContext) {

  def getSubcategories(): Future[Seq[SubcategoryDta]] =
    subcategoryStorage.getSubcategories()

  def getSubcategory(id: String): Future[Option[SubcategoryDta]] =
    subcategoryStorage.getSubcategory(Converters.stringToInt(id))

  def createSubcategory(subcategoryDta: SubcategoryDta): Future[SubcategoryDta] =
    subcategoryStorage.saveSubcategory(subcategoryDta)

  def updateSubcategory(id: String, subcategoryUpdate: SubcategoryUpdate): Future[Option[SubcategoryDta]] =
    subcategoryStorage
      .getSubcategory(Converters.stringToInt(id))
      .mapT(subcategoryUpdate.merge)
      .flatMapTOuter(subcategoryStorage.saveSubcategory)

}
