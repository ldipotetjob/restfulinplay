# Implementing Restful services in scala ecosystem with Play Framework #

This project is a proof of concept about the basic architeture of a Play Framework multimodule project.

## What will you find here? ##

* A basic multimodule architecture that will let you implement and process Restful WebServices in Scala programming language, it can be a template for any multimodule Play Framework project.
* An implementation of HTTP protocol Content-Negotiation:
    * Accept
    * Content-Type
* How generate CSV response depending of Content-Negotiation:
    * Implementing custom template using twirl engine for return a CSV file depending of the Accept header field.
    * Implement at the same time json responses
    
* How implement TDD in our development process:
    * We have create the base of Spec with FlatSpec style: We have NOT covered the whole lines of code but we have create enoguh test suites for give an idea of how to make with the rest. We have covered at least Controllers, action,implicit conversions and services. 
    
## What you will not find here that should be? ##

* ErrorHandler implementation as indicate play framework specification. 
* Internationalization of messages.
* LogHandler: 
    * Although we register some log, must exist a log handler, which could be a a trait. Esta trait could be implemented by all those classes that at some point can generate a log.
   
## Requirements, Installation, Launching, Testing ##

### Requirements ###

* jdk 1.7+ -> how check java version: java -version
* scala 2.11.x -> how check scala version: scala -version
* sbt 0.13.11+ -> how check sbt version: sbt about

### Installation ###

* clone repository
* go to root project
* type in terminal: sbt run

### Launching ###

You have several rest sevices to call:

This is [the route file](https://github.com/ldipotetjob/restfulinplay/blob/master/modules/apirest/conf/apirest.routes) and it has commented several examples on how call all services exposed in this project.
The commented examples has the following structure:

\# pattern: 

\# example: 

You can paste the \# example: in your **terminal** in case of **curl** o in your **browser** in case **http**


### Testing ###

* go to root project
* type in terminal: sbt test


The basic information is [here on gitHub](https://github.com/ldipotetjob/restfulinplay/blob/master/package.txt) and contains the main project information 

**Each package in the source code has a file (package.txt) that explains the fundamentals of that specific package.**  





https://mojitoverdeintw.blogspot.com 

https://mojitoverde.blogspot.com
