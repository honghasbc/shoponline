package com.shoponline.restapi.http.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import com.shoponline.restapi.core.CategoryUpdate
import com.shoponline.restapi.core.categories.CategoryService
import com.shoponline.restapi.utils.SecurityDirectives
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.generic.auto._
import io.circe.syntax._

import scala.concurrent.ExecutionContext

class CategoryRoute(
  categoryService: CategoryService
)(implicit executionContext: ExecutionContext) extends FailFastCirceSupport {

  import SecurityDirectives._
  import StatusCodes._
  import categoryService._

  val route = pathPrefix("categories") {
    pathEndOrSingleSlash {
      get {
        complete(getCategories().map(_.asJson))
      }
//    } ~
//      pathPrefix("me") {
//        pathEndOrSingleSlash {
//          authenticate(secretKey) { userId =>
//            get {
//              complete(getProfile(userId))
//            } ~
//              post {
//                entity(as[UserProfileUpdate]) { userUpdate =>
//                  complete(updateProfile(userId, userUpdate).map(_.asJson))
//                }
//              }
//          }
//        }
      } ~
      pathPrefix(Segment) { id =>
        pathEndOrSingleSlash {
          get {
            complete(getCategory(id).map {
              case Some(categoryDta) =>
                OK -> categoryDta.asJson
              case None =>
                BadRequest -> None.asJson
            })
          } ~
            post {
              entity(as[CategoryUpdate]) { categoryUpdate =>
                complete(updateCategory(id, categoryUpdate).map {
                  case Some(categoryDta) =>
                    OK -> categoryDta.asJson
                  case None =>
                    BadRequest -> None.asJson
                })
              }
            }~
            delete {
              complete(deleteCategory(id))
            }
        }
      }
  }

}
