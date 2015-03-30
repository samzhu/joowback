import AssemblyKeys._

name := "joowback"

version := "1.0"

scalaVersion := "2.11.5"

scalacOptions ++= Seq("-feature", "-unchecked", "-deprecation", "-encoding", "utf8")

libraryDependencies ++= {
  val sprayV = "1.3.2"
  Seq(
    "com.typesafe.akka"             %  "akka-actor_2.11"              % "2.3.8",
    "com.typesafe.akka"             %  "akka-http-experimental_2.11"  % "1.0-M2",
    "io.spray"                      %  "spray-routing_2.11"           % sprayV,
    "io.spray"                      %  "spray-client_2.11"            % sprayV,
    "io.spray"                      %  "spray-testkit_2.11"           % sprayV    % "test",
    "com.sksamuel.elastic4s"        %  "elastic4s_2.11"               % "1.4.11",
    "org.json4s"                    %  "json4s-native_2.11"           % "3.2.11",
    "org.json4s"                    %  "json4s-jackson_2.11"          % "3.2.11",
    "com.fasterxml.jackson.core"    %  "jackson-core"                 % "2.4.2",
    "com.fasterxml.jackson.core"    %  "jackson-databind"             % "2.4.2",
    "com.fasterxml.jackson.module"  %% "jackson-module-scala"         % "2.4.2",
    "com.typesafe.scala-logging"    %  "scala-logging-slf4j_2.11"     % "2.1.2",
    "ch.qos.logback"                %  "logback-classic"              % "1.1.2",
    "org.scalatest"                 %  "scalatest_2.11"               % "2.2.3"   % "test",
    "org.mockito"                   %  "mockito-all"                  % "1.10.19" % "test",
    "commons-codec"                 %  "commons-codec"                % "1.10",
    "com.hazelcast"                 %  "hazelcast"                    % "3.4.1"
  )
}


//libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value
