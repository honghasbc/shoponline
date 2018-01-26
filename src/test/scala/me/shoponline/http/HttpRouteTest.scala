package com.shoponline.http

import akka.http.scaladsl.server.Route
import com.shoponline.BaseServiceTest
import com.shoponline.restapi.core.auth.AuthService
import com.shoponline.restapi.core.categories.CategoryService
import com.shoponline.restapi.core.product.ProductService
import com.shoponline.restapi.core.subcategories.SubcategoryService
import com.shoponline.restapi.http.HttpRoute

class HttpRouteTest extends BaseServiceTest {

  "HttpRoute" when {

    "GET /shoponline" should {

      "return 200 OK" in new Context {
        Get("/shoponline") ~> httpRoute ~> check {
          responseAs[String] shouldBe "OK"
          status.intValue() shouldBe 200
        }
      }

    }

  }

  trait Context {
    val secretKey = "secret"
    val authService: AuthService = mock[AuthService]
    val categoryService: CategoryService = mock[CategoryService]
    val subcategoryService: SubcategoryService = mock[SubcategoryService]
    val productService: ProductService = mock[ProductService]

    val httpRoute: Route = new HttpRoute(authService,categoryService,
      subcategoryService , productService, secretKey).route
  }

}
