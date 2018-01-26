package com.shoponline.restapi

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.shoponline.restapi.core.auth.{AuthService, JdbcAuthDataStorage}
import com.shoponline.restapi.core.categories.{CategoryService, JdbcCategoryStorage}
import com.shoponline.restapi.core.product.{JdbcProductStorage, ProductService}
import com.shoponline.restapi.core.subcategories.{JdbcSubcategoryStorage, SubcategoryService}
import com.shoponline.restapi.http.HttpRoute
import com.shoponline.restapi.utils.Config
import com.shoponline.restapi.utils.db.{DatabaseConnector, DatabaseMigrationManager}

import scala.concurrent.ExecutionContext

object Boot extends App {

  def startApplication() = {
    implicit val actorSystem = ActorSystem()
    implicit val executor: ExecutionContext = actorSystem.dispatcher
    implicit val materializer: ActorMaterializer = ActorMaterializer()

    val config = Config.load()

    new DatabaseMigrationManager(
      config.database.jdbcUrl,
      config.database.username,
      config.database.password
    ).migrateDatabaseSchema()

    val databaseConnector = new DatabaseConnector(
      config.database.jdbcUrl,
      config.database.username,
      config.database.password
    )

    val authDataStorage = new JdbcAuthDataStorage(databaseConnector)
    val categoryStorage = new JdbcCategoryStorage(databaseConnector)
    val subcategoryStorage = new JdbcSubcategoryStorage(databaseConnector)
    val productStorage = new JdbcProductStorage(databaseConnector)

    val authService = new AuthService(authDataStorage, config.secretKey)
    val categoryService = new CategoryService(categoryStorage)
    val subcategoryService = new SubcategoryService(subcategoryStorage)
    val productService = new ProductService(productStorage)
    val httpRoute = new HttpRoute(authService, categoryService,
      subcategoryService, productService, config.secretKey)

    Http().bindAndHandle(httpRoute.route, config.http.host, config.http.port)
  }

  startApplication()

}
