---
layout: page
title: Goals
---
Generate record and table data gateway classes that run on top of spring-jdbc.

## Goals of the project:

- Generate boilerplate code to interact with an existing Database.

- Take advantage of the infrastructure already avaiable in spring-jdbc.

- Make it light, simple and flexible.

- It is a Record Mapper, but it is not an ORM.

## Why do we need another Object-Relational Mapper?

It is not an ORM. It maps records and tables not domain objects. This approach is better if you are dealing with a legacy relational database that may not be perfectly designed. It doesn't attempt to map complex structures.

Other benefits:

 * It is lighter. The initialization time is very small, even for databases with many tables.

 * You handcraft your database access code. Furthermore, your code is reusable and it is organized by table.

 * It is flexible, you can customize the generated code.

### Why the Records classes don't have getters and setters?
 
Because there is nothing to encapsulate on the records. Remember, the records are not domain objects. They are simple data transfer objects that hold the same information as a record in a database table.
