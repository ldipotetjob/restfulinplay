package com.ldg.security

import com.ldg.model.CSVUtilities.processCloseable
import java.io.{File, PrintWriter}
import java.text.SimpleDateFormat
import java.util.Date
import scala.concurrent.Future
import scala.io.Source
import scalaoauth2.provider.{AccessToken, AuthInfo, ClientCredential, _}
import scala.concurrent.ExecutionContext.Implicits.global

case class User(name: String, hashedPassword: String)

/**https://github.com/playframework/playframework/blob/master/framework/src/play/src/main/scala/play/api/mvc/Security.scala*/
class ApiDataHandler  extends DataHandler[User] {

  def validateClient(maybeClientCredential: Option[ClientCredential], request: AuthorizationRequest): Future[Boolean] =
    Future {
      maybeClientCredential.fold {
        false
      } {
        findClientCredentials(_)
      }
    }

  def findUser(maybeClientCredential: Option[ClientCredential], request: AuthorizationRequest): Future[Option[User]] = {
    // case request: ClientCredentialsRequest
    //val requestClientCredential = request.asInstanceOf[ClientCredentialsRequest]
    Future.successful {
      if (
        (request.params("client_id")(0)=="bob_client_id") &&
          (request.params("client_secret")(0)=="bob_client_secret")
      ) {
        Some(User("client_id","client_secret"))
      } else None
    }
  }

  def createAccessToken(authInfo: AuthInfo[User]): Future[AccessToken] = {
    val refreshToken = None
    val scope = None
    val accessToken = "48181acd22b3edaebc8a447868a7df7ce629920a"
    val now = new Date()
    val accessTokenExpire = 3600
    val tokenObject = AccessToken(accessToken, refreshToken, scope, Option(accessTokenExpire),now )
    saveToken(authInfo, tokenObject)
    Future.successful(tokenObject)
  }

  def getStoredAccessToken(authInfo: AuthInfo[User]): Future[Option[AccessToken]] = {
    val accessToken = "48181acd22b3edaebc8a447868a7df7ce629920a"
    val timeStr:String = getStoredToken("fakedb.txt")(2)
    val sdf:SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")

    Future.successful(Option(AccessToken(accessToken, None, None, Option(3600),sdf.parse(timeStr))))
  }

  def refreshAccessToken(authInfo: AuthInfo[User], refreshToken: String): Future[AccessToken] = Future.successful{

    //Just for our PoC returning the same token t ehat getStoredAccessToken
    val accessToken = "48181acd22b3edaebc8a447868a7df7ce629920a"
    val timeStr:String = getStoredToken("fakedb.txt")(2)
    val sdf:SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
    AccessToken(accessToken, None, None, Option(3600),sdf.parse(timeStr))
  }

  def findAuthInfoByCode(code: String): Future[Option[AuthInfo[User]]] = Future.successful(None)

  def findAuthInfoByRefreshToken(refreshToken: String): Future[Option[AuthInfo[User]]] = Future.successful(None)

  def deleteAuthCode(code: String): Future[Unit] = Future.successful(println("nothing to do"))

  def findAccessToken(token: String): Future[Option[AccessToken]] = {
    //Just for our PoC so your token should be accessToken in other case error
    val accessToken = "48181acd22b3edaebc8a447868a7df7ce629920a"
    val timeStr:String = getStoredToken("fakedb.txt")(2)
    val sdf:SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")

    if (token==accessToken)
      Future.successful(Option(AccessToken(accessToken, None, None, Option(3600),sdf.parse(timeStr))))
    else
      Future.successful(None)
  }

  def findAuthInfoByAccessToken(accessToken: AccessToken): Future[Option[AuthInfo[User]]] ={
    Future.successful{
     Option{
       AuthInfo(
                  user = User("client_id","client_secret"),
                  clientId = Some("client_id"),
                  scope = None,
                  redirectUri = None
       )
     }
   }
  }

  def findClientCredentials(clientCredentials: ClientCredential): Boolean = {
    clientCredentials.clientId == "bob_client_id" && clientCredentials.clientSecret.
      fold(false)(clientSecret => clientSecret.equals("bob_client_secret"))
  }

  def saveToken(authinfo: AuthInfo[User], accessToken: AccessToken) = {
    val sdf:SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
    processCloseable(new PrintWriter(new File("fakedb.txt")) ){
      writer =>{
        val stringExpirationTime:String = accessToken.lifeSeconds.getOrElse(0).toString
        val createAt:String = sdf.format(accessToken.createdAt)
        val token: String = accessToken.token

        //for our test in fakedb <- client-id,client-secret,expiration-time,date
        //expiration time must be converted to log:stringExpirationTime.toLong
        //date must be converted string->date: for return date value: sdf.parse(createAt)
        writer.write(s"$token,$stringExpirationTime,$createAt")
      }
    }

  }

  def getStoredToken(file: String): Array[String] = {
    val storedToken = Source.fromFile(file)
    storedToken.getLines().toSeq.mkString.split(",")
  }
}