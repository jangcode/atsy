# Atsy [![Build Status](https://travis-ci.org/epam-debrecen-rft-2015/atsy.svg?branch=master)](https://travis-ci.org/epam-debrecen-rft-2015/atsy)
Applicant Tracking System

Building the project:
=========================
As easy as:

     mvn clean install

Running Atsy locally:
=========================

    mvn tomcat7:run

Prerequisites: You have mysql locally which has a database called 'atsy', and a user in it with details:
     'travis' - no password

Running integration tests as part of the build:
===============================================

    mvn clean install -Pintegration

Definition of Done
==================

1. Code is well documented:
  * All public service methods have JavaDoc comments. 
  * Where necessary for understanding, non-public methods have JavaDoc as well. 
1. Data container objects have (Lombok can be used for method generation)
  * private fields
  * getter methods, 
  * setter methods,
  * toString() method overridden, 
  * hashCode() method overridden, 
  * equals() method overridden.
1. Logging is added
  * when an exepction happens,
  * when a significant event happens (user is deleted, candidate added, etc.). 
1. Migration script is added if the changes require database modifications.
1. README.md is updated if there are some changes in the application setup process.
1. Code is placed in a task related branch (feature branch)
1. Code for subtasks have a separate branch (sub-task branch)
1. Before pushing changes in branch which is not tracked remotely use git rebase
1. Pull request is created
1. mvn tomcat7:run works (manual check)
1. changed feature does not break (manual check)
1. All unit tests pass
1. All integration tests pass
1. CI server could build the project
1. CI server could run tests

User credentials
=================

User | Password | Stored hash | Environment description  
---- | -------- | ----------- | -----------------------  
Dev | pass2 | $2a$04$QSJkUouEDGfbWAtuxnlf/.Ajen6lviIhrNwKFPPZ.juRn6nLgvBi6 | integration  
Test | pass3 | $2a$04$6r2eBlucnE3/m0fDYgE3e.eI5akUx55qPMID3O/SlptCIqOFFcCPK | integration  
Brigi | pass1 | $2a$04$BkbZFudrQjtETU83hFiS3uWNsh7us3wOOnLF3yop9BByd9ZQLdIlq | production  


