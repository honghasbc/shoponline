package com.shoponline.restapi.http

import java.lang.Iterable
import java.util
import akka.http.javadsl.model.HttpMethod
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import ch.megard.akka.http.cors.scaladsl.settings
import com.shoponline.restapi.core.auth.AuthService
import com.shoponline.restapi.core.categories.CategoryService
import com.shoponline.restapi.core.product.ProductService
import com.shoponline.restapi.core.subcategories.SubcategoryService
import com.shoponline.restapi.http.routes._

import scala.concurrent.ExecutionContext

class HttpRoute( authService: AuthService,
                 categoryService : CategoryService,
                 subcategoryService : SubcategoryService,
                 productService : ProductService,
                 secretKey: String
)(implicit executionContext: ExecutionContext) {

  private val authRouter = new AuthRoute(authService)
  private val categoryRouter = new CategoryRoute(categoryService)
  private val subcategoryRouter = new SubcategoryRoute(subcategoryService)
  private val productRouter = new ProductRoute(productService)

  // Your CORS settings
  val list: util.List[HttpMethod] = new util.ArrayList[HttpMethod]
  list.add(GET)
  list.add(POST)
  list.add(HEAD)
  list.add(OPTIONS)
  list.add(DELETE)
  list.add(PUT)
  val iterable: Iterable[HttpMethod] = list
  val corsSettings = settings.CorsSettings.defaultSettings.withAllowedMethods(iterable)
  val route: Route =
    cors(corsSettings) {
      pathPrefix("v1") {
          authRouter.route ~
          categoryRouter.route ~
          subcategoryRouter.route ~
          productRouter.route
      } ~
        pathPrefix("shoponline") {
          get {
            complete("OK")
          }
        }
    }

}
