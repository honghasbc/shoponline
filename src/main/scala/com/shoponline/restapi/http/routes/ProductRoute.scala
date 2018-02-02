package com.shoponline.restapi.http.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import com.shoponline.restapi.core.{ProductDta, ProductUpdate}
import com.shoponline.restapi.core.product.ProductService
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.generic.auto._
import io.circe.syntax._

import scala.concurrent.ExecutionContext

class ProductRoute(
                    productService: ProductService
)(implicit executionContext: ExecutionContext) extends FailFastCirceSupport {

  import StatusCodes._
  import productService._

  val route = pathPrefix("product") {
    pathEndOrSingleSlash {
      get {
        complete(getProducts().map(_.asJson))
      }
      } ~
      pathPrefix(Segment) { id =>
        pathEndOrSingleSlash {
          get {
            complete(getProductCategory(id).map {
              case Some(productCategoryDta) =>
                OK -> productCategoryDta.asJson
              case None =>
                BadRequest -> None.asJson
            })
          } ~
            post {
              entity(as[ProductUpdate]) { productUpdate =>
                complete(updateProduct(id, productUpdate).map {
                  case Some(productDta) =>
                    OK -> productDta.asJson
                  case None =>
                    BadRequest -> None.asJson
                })
              }
            }~
            delete {
              complete(deleteProduct(id))
            }
        }
      } ~
      post {
        entity(as[ProductDta]) { productDta =>
          complete(createProduct(productDta).map {
            case productDta =>
              OK -> productDta.asJson
          })
        }
      }
  }

}
