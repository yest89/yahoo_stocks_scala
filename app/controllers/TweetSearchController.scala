package controllers

import play.api.cache.SyncCacheApi
import javax.inject.{Inject, Singleton}
import play.api.libs.json.JsValue
import play.api.mvc._
import services.TwitterService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._

@Singleton
class TweetSearchController @Inject()(cache: SyncCacheApi, twitter: TwitterService, val controllerComponents: ControllerComponents) extends BaseController {

  def tweets(query: String) = Action.async {
//    val bearerToken = "AAAAAAAAAAAAAAAAAAAAACmXXwEAAAAA5f1He8PsoKPCcrZCJA8JG7TaxOk%3DpnW9jI9Q6Wqss92c6p22qtF5neMRi9pw0NHuCYGSwUi5juOJGO"
    cache.get[JsValue](query).fold {
      try {
        twitter.bearerToken.flatMap { bearerToken =>
          twitter.fetchTweets(bearerToken, query).map { response =>
            cache.set(query, response.json, 1.hour)
            Ok(response.json)
          }
        }
      } catch {
        case illegalArgumentException: IllegalArgumentException =>
//          Logger.("Twitter Bearer Token is missing", illegalArgumentException)
          Future(InternalServerError("Error talking to Twitter"))
      }
    } { result =>
      Future.successful(Ok(result))
    }
  }

  def tweetById(id: String) = Action.async {
    //    val bearerToken = "AAAAAAAAAAAAAAAAAAAAACmXXwEAAAAA5f1He8PsoKPCcrZCJA8JG7TaxOk%3DpnW9jI9Q6Wqss92c6p22qtF5neMRi9pw0NHuCYGSwUi5juOJGO"
    cache.get[JsValue](id).fold {
      try {
        twitter.bearerToken.flatMap { bearerToken =>
          twitter.fetchTweetById(bearerToken, id).map { response =>
            cache.set(id, response.json, 1.hour)
            Ok(response.json)
          }
        }
      } catch {
        case illegalArgumentException: IllegalArgumentException =>
          //          Logger.("Twitter Bearer Token is missing", illegalArgumentException)
          Future(InternalServerError("Error talking to Twitter"))
      }
    } { result =>
      Future.successful(Ok(result))
    }
  }
}
