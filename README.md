# Implementing Restful services in an Scala ecosystem with Play Framework #

This project is a proof of concept about the basic architeture of a Play Framework multimodule project.

### What will you find here? ###

* A basic multimodule architecture that will let you implement and process Restful WebServices in Scala programming language, it can be a template for any multimodule Play Framework project.
* An implementation of HTTP protocol Content-Negotiation:
    * Accept
    * Content-Type
* How generate CSV response depending of Content-Negotiation:
    * Implementing custom template using twirl engine for return a CSV file depending de Accept Request Header.
    
* How implement TDD in our development process:
    * We have create the base of Spec with FlatSpec style: We have not covered the whole lines of code but we have create enoguh test suites for give an idea of how to make with the rest. We have covered at least Controllers, action,implicit conversions and services. 
    
### What you will not find here that should be? ###

* ErrorHandler implementation as indicate play framework specification. 
* Internationalization of messages.
* LogHandler: 
    * Although we register some log, must exist a log handler, which could be a a trait. Esta trait could be implemented by all those classes that at some point can generate a log.

    
### Requirements, Installation and Launching ###


For any project in this repository you will need to install the following softwares:

* jdk 1.7+
* scala 2.11.x
* sbt 0.13.11+

We have implemented here:

* Rest Service with normal actions
* Rest Services with action builder [processing http resquest with JSON][modifiying request and response][parsing body]
* Implementing logging in all activities.
* Error hanldling of request/response.

**Each package in the source code has a file (package.txt) that explains the fundamentals of that specific package.**  

Enjoy :+1:



https://mojitoverdeintw.blogspot.com 

https://mojitoverde.blogspot.com
