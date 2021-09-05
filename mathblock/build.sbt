scalaVersion := "3.0.0"

initialCommands in console := """
  println("Hello Scala 3!")
"""

scalacOptions ++= Seq(
  "-encoding", "utf8", 
  "-source", "future",  // remove this if you want to allow old Scala 2 syntax
  "-Xfatal-warnings",  
  "-deprecation",
  "-unchecked",
)

fork                := true
connectInput        := true
outputStrategy      := Some(StdoutOutput)
