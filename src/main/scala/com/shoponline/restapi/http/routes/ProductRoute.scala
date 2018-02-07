package com.shoponline.restapi.http.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import com.shoponline.restapi.core.{ProductDta, ProductUpdate}
import com.shoponline.restapi.core.product.ProductService
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.generic.auto._
import io.circe.syntax._
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContext

class ProductRoute(
                    productService: ProductService
)(implicit executionContext: ExecutionContext) extends FailFastCirceSupport {

  import StatusCodes._
  import productService._

  val logger = LoggerFactory.getLogger(classOf[ProductRoute])
  val route = pathPrefix("product") {
    pathEndOrSingleSlash {
      get {
        complete(getProducts().map(_.asJson))
      }
      } ~
      pathPrefix(Segment) { id =>
        pathEndOrSingleSlash {
          get {
            logger.info("--- GET Method ---")
            complete(getProductCategory(id).map {
              case Some(productCategoryDta) =>
                OK -> productCategoryDta.asJson
              case None =>
                BadRequest -> None.asJson
            })
          } ~
            post {
              entity(as[ProductUpdate]) { productUpdate =>
                logger.info("POST product update data :"+productUpdate)
                complete(updateProduct(id, productUpdate).map {
                  case Some(productDta) =>
                    OK -> productDta.asJson
                  case None =>
                    BadRequest -> None.asJson
                })
              }
            }~
            delete {
              logger.info("--- DELETE product id :"+ id)
              complete(deleteProduct(id))
            }
        }
      } ~
      post {
        entity(as[ProductDta]) { productDta =>
          logger.info("POST product data :"+productDta)
          complete(createProduct(productDta).map {
            case productDta =>
              OK -> productDta.asJson
          })
        }
      }
  }

}
