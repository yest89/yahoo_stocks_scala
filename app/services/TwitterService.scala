package services

import javax.inject.Inject
import play.api.Configuration
import play.api.libs.ws.{WSAuthScheme, WSClient, WSResponse}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class TwitterService @Inject()(ws: WSClient, config: Configuration) {

  /*
   * Docs: https://dev.twitter.com/docs/auth/application-only-auth
   * 
   * API:
   * 
   *     POST /oauth2/token HTTP/1.1
   *     Host: api.twitter.com
   *     User-Agent: My Twitter App v1.0.23
   *     Authorization: Basic eHZ6MWV2RlM0d0VFUFRHRUZQSEJvZzpMOHFxOVBaeVJnNmllS0dFS2hab2xHQzB2SldMdzhpRUo4OERSZHlPZw==
   *     Content-Type: application/x-www-form-urlencoded;charset=UTF-8
   *     Content-Length: 29
   *     Accept-Encoding: gzip
   *     
   *     grant_type=client_credentials
   *     
   *     
   *     HTTP/1.1 200 OK
   *     Status: 200 OK
   *     Content-Type: application/json; charset=utf-8
   *     Content-Encoding: gzip
   *     Content-Length: 140
   *     
   *     {"token_type":"bearer","access_token":"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA%2FAAAAAAAAAAAAAAAAAAAA%3DAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"}
   *     
   */
  lazy val bearerToken: Future[String] = {
    require(!config.underlying.getString("twitter.consumer.key").isEmpty)
    require(!config.underlying.getString("twitter.consumer.secret").isEmpty)

    val twitterKey = "w7uTuuYHK13Y39eYJwrDs5CBj"
    val twitterSecret = "xTh9DBrBeBhNv216AKfpGnYxR51VwvkmdfJxcZIH93yP6ZA5sr"

    ws.url("https://api.twitter.com/oauth2/token")
      .withAuth(twitterKey, twitterSecret, WSAuthScheme.BASIC)
      .post(Map("grant_type" -> Seq("client_credentials")))
      .withFilter(response => (response.json \ "token_type").asOpt[String].contains("bearer"))
      .map(response => (response.json \ "access_token").as[String])
  }

  def fetchTweets(bearerToken: String, query: String): Future[WSResponse] = {
    //        ws.url("https://api.twitter.com/1.1/search/tweets.json")
    //          .withQueryString("q" -> query)
    //          .withHeaders("Authorization" -> s"Bearer $bearerToken")
    //          .get()
    //
    //    URIBuilder uriBuilder = new URIBuilder("https://api.twitter.com/2/tweets");
    //    ArrayList<NameValuePair> queryParameters;
    //    queryParameters = new ArrayList<>();
    //    queryParameters.add(new BasicNameValuePair("ids", ids));
    //    queryParameters.add(new BasicNameValuePair("tweet.fields", "created_at"));
    //    uriBuilder.addParameters(queryParameters);

    val bearer = config.underlying.getString("twitter.bearer")
    val auth = "Bearer " + bearer

    ws.url("https://api.twitter.com/2/tweets/search/recent")
      //    ws.url("https://api.twitter.com/1.1/search/tweets.json")
      //      .withQueryString("q" -> query)
      .withQueryString("query" -> query)
      .withHeaders("Authorization" -> auth)
      .get()


    //    //    ws.url("https://api.twitter.com/2/tweets")
    //    ws.url("https://api.twitter.com/2/tweets/search/all")
    //      .withQueryString("query" -> "eth")
    //      .withHeaders("Authorization" -> s"Bearer $bearerToken")
    //      .get()
  }

  def fetchTweetById(bearerToken: String, id: String): Future[WSResponse] = {
    //        ws.url("https://api.twitter.com/1.1/search/tweets.json")
    //          .withQueryString("q" -> query)
    //          .withHeaders("Authorization" -> s"Bearer $bearerToken")
    //          .get()
    //
    //    URIBuilder uriBuilder = new URIBuilder("https://api.twitter.com/2/tweets");
    //    ArrayList<NameValuePair> queryParameters;
    //    queryParameters = new ArrayList<>();
    //    queryParameters.add(new BasicNameValuePair("ids", ids));
    //    queryParameters.add(new BasicNameValuePair("tweet.fields", "created_at"));
    //    uriBuilder.addParameters(queryParameters);

    val bearer = config.underlying.getString("twitter.bearer")
    val auth = "Bearer " + bearer

    ws.url("https://api.twitter.com/2/tweets/"+id)
      //    ws.url("https://api.twitter.com/1.1/search/tweets.json")
      //      .withQueryString("q" -> query)
      .withHeaders("Authorization" -> auth)
      .get()


    //    //    ws.url("https://api.twitter.com/2/tweets")
    //    ws.url("https://api.twitter.com/2/tweets/search/all")
    //      .withQueryString("query" -> "eth")
    //      .withHeaders("Authorization" -> s"Bearer $bearerToken")
    //      .get()
  }

}

