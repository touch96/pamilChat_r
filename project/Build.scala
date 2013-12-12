import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "pamilChat"
  val appVersion      = "1.1"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    javaJdbc,
    javaEbean,
    "com.googlecode.javapns" % "javapns" % "2.2",
    "mysql" % "mysql-connector-java" % "5.1.6"
    ,"bouncycastle" % "bcprov-jdk14" % "138"
    ,"log4j" % "log4j" % "1.2.17"
    ,"com.notnoop.apns" % "apns" % "0.2.3"
    ,"org.hibernate" % "hibernate-entitymanager" % "3.6.9.Final"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here
    resolvers += "maven kungfuters" at "http://maven.kungfuters.org/content/groups/public",
    javaOptions ++= Seq("-Xmx1024M", "-Xmx2048M", "-XX:MaxPermSize=2048M")
  )

}
