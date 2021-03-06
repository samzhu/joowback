import AssemblyKeys._

name := "joowback"

version := "1.0"

scalaVersion := "2.11.5"

scalacOptions ++= Seq("-feature", "-unchecked", "-deprecation", "-encoding", "utf-8")

resolvers += "nlpcn" at "http://maven.nlpcn.org/"

resolvers += "ansj" at "http://maven.ansj.org/"

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
    "com.hazelcast"                 %  "hazelcast"                    % "3.4.1",
    "org.nlpcn"                     %  "nlp-lang"                     % "0.3",
    "org.ansj"                      %  "ansj_seg"                     % "2.0.8"
  )
}
//image op
//libraryDependencies += "com.sksamuel.scrimage" %% "scrimage-core" % "1.4.2"

//libraryDependencies += "com.sksamuel.scrimage" %% "scrimage-canvas" % "1.4.2"

//libraryDependencies += "com.sksamuel.scrimage" %% "scrimage-filters" % "1.4.2"

//toCheck image type

libraryDependencies += "net.sf.jmimemagic" % "jmimemagic" % "0.1.4"

libraryDependencies += "log4j" % "log4j" % "1.2.17"

//libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value
