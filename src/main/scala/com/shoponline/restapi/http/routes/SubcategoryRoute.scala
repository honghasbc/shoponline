package com.shoponline.restapi.http.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import com.shoponline.restapi.core.SubcategoryUpdate
import com.shoponline.restapi.core.subcategories.SubcategoryService
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.generic.auto._
import io.circe.syntax._

import scala.concurrent.ExecutionContext

class SubcategoryRoute(
                        subcategoryService: SubcategoryService
)(implicit executionContext: ExecutionContext) extends FailFastCirceSupport {

  import StatusCodes._
  import subcategoryService._

  val route = pathPrefix("subcategories") {
    pathEndOrSingleSlash {
      get {
        complete(getSubcategories().map(_.asJson))
      }
      } ~
      pathPrefix(Segment) { id =>
        pathEndOrSingleSlash {
          get {
            complete(getSubcategory(id).map {
              case Some(categoryDta) =>
                OK -> categoryDta.asJson
              case None =>
                BadRequest -> None.asJson
            })
          } ~
            post {
              entity(as[SubcategoryUpdate]) { subcategoryUpdate =>
                complete(updateSubcategory(id, subcategoryUpdate).map {
                  case Some(subcategoryDta) =>
                    OK -> subcategoryDta.asJson
                  case None =>
                    BadRequest -> None.asJson
                })
              }
            }
        }
      }
  }

}
