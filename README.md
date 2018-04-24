
<img width="928" alt="captura de pantalla 2017-10-07 a las 15 09 49" src="https://user-images.githubusercontent.com/8100363/31313078-665da9a6-abcf-11e7-9266-932880ea6ed2.png">



# Implementing Restful services in scala ecosystem with Play Framework #

This project is a proof of concept about the basic architecture of a Play Framework multi module project.

## What will you find here? ##

* A basic multi module architecture that will let you implement and process Restful WebServices in Scala programming language, it can be a template for any multi module Play Framework project.

* An implementation of HTTP protocol Content-Negotiation:
    * Accept
    * Content-Type
    * You can add here any other header fields involved in content-negotiation.
    
* How generate CSV response depending of Content-Negotiation:
    * Implementing custom template using twirl engine for return a CSV file depending of the Accept header field.
    * Implement at the same time json responses.
    
* How implement TDD in our development process:
    * We have create the base of Spec with FlatSpec style: We have NOT covered the whole lines of code but we have created enoguh test suites for give you an idea of how to make with the rest. We have covered at least Controllers, action,implicit conversions and services. 
    
## What you will not find here, but you should ##

* ErrorHandler implementation as indicate play framework specification. 
* Internationalization of messages.
* LogHandler: 
    * Although we register some log, must exist a log handler, which could be a trait. This trait could be implemented by all those classes that, at some point, can generate a log.
   
## Requirements, Installation, Launching, Testing ##

### Requirements ###

* jdk 1.7+ -> how check java version: java -version
* scala 2.11.x -> how check scala version: scala -version
* sbt 0.13.11+ -> how check sbt version: sbt about

### Installation ###

* clone repository
* go to root project
* type in terminal: sbt run

video: [running sbt](https://youtu.be/AWP7ODqjYmI)

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

video: [running test](https://youtu.be/s-jO1PFaUR4)

The basic information is [here on gitHub](https://github.com/ldipotetjob/restfulinplay/blob/master/package.txt) and contains the main project information.


**Each package in the source code has a file (package.txt) that explains the fundamentals of that specific package.**  
<br>
<br>
<br>
https://mojitoverdeintw.blogspot.com 

