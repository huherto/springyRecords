---
layout: page
title: Home
---

SpringyRecords is a framework to generate Data Access Classes based on a database schema. It can be used out of the box or it can be extended to generate the code according to your preferences.


## Goals of the project:

- Generate boilerplate code to interact with an existing Database.

- Take advantage of the infrastructure already avaiable in spring-jdbc.

- Make it light, simple and flexible.

- It is a light ORM or a micro ORM.

## How is it compares with an ORM.

It is lighter than a traditional ORM. It maps records and tables not domain objects. This approach is better if you are dealing with a legacy relational database that may not be perfectly designed. It doesn't attempt to map complex structures.

Other benefits:

 * It is lighter. The initialization time is very small, even for databases with many tables.

 * You handcraft your database access code. Furthermore, your code is reusable and it is organized by table.

 * It is very flexible and highly customizable.

