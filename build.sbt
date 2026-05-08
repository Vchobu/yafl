lazy val root = project
  .in(file("."))
  .settings(
    name := "Yafl",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := "3.8.3",
    libraryDependencies += "org.scalameta" %% "munit" % "1.3.0" % Test,
    libraryDependencies += "com.dylibso.chicory" % "runtime" % "1.7.5",
    libraryDependencies += "com.dylibso.chicory" % "wasm-tools" % "1.7.5",
    scalacOptions ++= Seq("-deprecation")
  )
