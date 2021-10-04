scalaVersion := "3.0.1"

initialCommands in console := """
  println("Hello Scala 3!")
"""

scalacOptions ++= Seq(
  "-encoding",
  "utf8",
  "-source",
  "future", // remove this if you want to allow old Scala 2 syntax
  "-Xfatal-warnings",
  "-deprecation",
  "-unchecked"
)

libraryDependencies ++= Seq(
  "dev.zio" %% "zio" % "1.0.11",
  "dev.zio" %% "zio-streams" % "1.0.11",
  "dev.zio" %% "zio-interop-cats" % "3.1.1.0",
  "com.github.ghostdogpr" %% "caliban" % "1.1.1",
  "com.github.ghostdogpr" %% "caliban-zio-http" % "1.1.1",
  "org.tpolecat" %% "doobie-core" % "1.0.0-RC1",
  "org.tpolecat" %% "doobie-postgres" % "1.0.0-RC1",
  "com.github.pureconfig" %% "pureconfig-core" % "0.16.0"
)

fork := true
connectInput := true
outputStrategy := Some(StdoutOutput)
