import sbt._

class Migpanel(info: ProjectInfo) extends DefaultProject(info) {
	val mig = "com.miglayout" % "miglayout" % "3.7.3.1" classifier "swing" 
	val swing = "org.scala-lang" % "scala-swing" % "2.8.1"
}

