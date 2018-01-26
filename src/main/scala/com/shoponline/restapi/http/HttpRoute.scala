package com.shoponline.restapi.http

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.shoponline.restapi.http.routes._
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import com.shoponline.restapi.core.auth.AuthService
import com.shoponline.restapi.core.categories.CategoryService
import com.shoponline.restapi.core.product.ProductService
import com.shoponline.restapi.core.subcategories.SubcategoryService

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

  val route: Route =
    cors() {
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
