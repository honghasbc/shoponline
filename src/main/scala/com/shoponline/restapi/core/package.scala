package com.shoponline.restapi

package object core {

  type UserId = String
  type AuthToken = String

  final case class AuthTokenContent(userId: UserId)

  final case class AuthData(id: UserId, username: String, email: String, password: String) {
    require(id.nonEmpty, "id.empty")
    require(username.nonEmpty, "username.empty")
    require(email.nonEmpty, "email.empty")
    require(password.nonEmpty, "password.empty")
  }

  final case class ProductDta(id: Int, categoryId: Int, subcategoryId: Int,
                              productDisplayName: String, price: Double, productShortDesc: String,
                              productLongDesc: String, isActive: Boolean, thumbnailImage: String,
                              smallImage: String, createDate: String, lastUpdateDate: String,
                              manufacturer: String, weight: Float) {
//    require(id.nonEmpty, "id.empty")
//    require(categoryId.nonEmpty, "categoryId.empty")
//    require(subcategoryId.nonEmpty, "subcategoryId.empty")
    require(productDisplayName.nonEmpty, "productDisplayName.empty")
//    require(price, "price.empty")
    require(productShortDesc.nonEmpty, "productShortDesc.empty")
    require(productLongDesc.nonEmpty, "productLongDesc.empty")
    require(thumbnailImage.nonEmpty, "thumbnailImage.empty")
    require(smallImage.nonEmpty, "smallImage.empty")
//    require(createDate.nonEmpty, "createDate.empty")
//    require(lastUpdateDate.nonEmpty, "lastUpdateDate.empty")
    require(manufacturer.nonEmpty, "manufacturer.empty")
  }

  final case class ProductCategoryDta(id: Int, categoryId: Int, subcategoryId: Int,
                              productDisplayName: String, categoryName:String, subcategoryName:String, price: Double, productShortDesc: String,
                              productLongDesc: String, isActive: Boolean, thumbnailImage: String,
                              smallImage: String, createDate: String, lastUpdateDate: String,
                              manufacturer: String, weight: Float) {
  }
  final case class ProductUpdate(categoryId :Option[Int] = None ,subcategoryId :Option[Int] = None , productDisplayName: Option[String] = None, price: Option[Double] = None,
                                 productShortDesc: Option[String] = None, productLongDesc: Option[String] = None,
                                 isActive: Option[Boolean] = None, thumbnailImage: Option[String] = None, smallImage: Option[String] = None,
                                 createDate: Option[String] = None, lastUpdateDate: Option[String] = None,
                                 manufacturer: Option[String] = None, weight: Option[Float] = None) {
    def merge(product: ProductDta): ProductDta = {
      ProductDta(product.id, categoryId.getOrElse(product.categoryId),subcategoryId.getOrElse(product.subcategoryId),
        productDisplayName.getOrElse(product.productDisplayName),price.getOrElse(product.price),
        productShortDesc.getOrElse(product.productShortDesc), productLongDesc.getOrElse(product.productLongDesc),
        isActive.getOrElse(product.isActive),
        thumbnailImage.getOrElse(product.thumbnailImage), smallImage.getOrElse(product.smallImage),
        createDate.getOrElse(product.createDate),lastUpdateDate.getOrElse(product.lastUpdateDate), manufacturer.getOrElse(product.manufacturer),
        weight.getOrElse(product.weight))
    }
  }
  final case class CategoryDta(id: Int, name: String) {
    require(name.nonEmpty, "name.empty")
  }

  final case class CategoryUpdate(name: Option[String] = None) {
    def merge(categoryDta: CategoryDta): CategoryDta = {
      CategoryDta(categoryDta.id, name.getOrElse(categoryDta.name))
    }
  }

  final case class SubcategoryDta(id: Int, idCategory: Int, name: String) {
    require(name.nonEmpty, "name.empty")
  }

  final case class SubcategoryUpdate(name: Option[String] = None, idCategory: Option[Int] = None) {
    def merge(subcategoryDta: SubcategoryDta): SubcategoryDta = {
      SubcategoryDta(subcategoryDta.id, idCategory.getOrElse(subcategoryDta.idCategory), name.getOrElse(subcategoryDta.name))
    }
  }

}
