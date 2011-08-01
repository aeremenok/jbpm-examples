This project is an example how to use [http://www.jboss.org/jbpm jBPM 3].

To run it you'll need:

# [http://maven.apache.org/download.html Apache Maven]
# [http://www.eclipse.org/downloads/packages/eclipse-ide-java-ee-developers/indigor Eclipse] or [http://www.jetbrains.com/idea/download/index.html IntelliJ Idea]

Install apache maven, then run in command line:

```
$ mkdir repo
$ cd repo
$ git clone git@github.com:aeremenok/jbpm-examples.git

$ cd jbpm-examples
; the following will download library dependencies from maven repository
$ mvn clean compile
; the following will create an eclipse config from the existing maven config (pom.xml)
$ mvn eclipse:eclipse
```

Then import the existing project into Eclipse.

The project also uses [http://testng.org/doc/index.html TestNG] to implement unit-tests and [http://www.slf4j.org/ SLF4J] for logging.

The examples of usage are located in test classes like src/test/java/edu/leti/jbpm/SuccessfulProcessTest.java
