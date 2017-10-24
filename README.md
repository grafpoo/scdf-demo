# scdf-demo
Demo for Spring Cloud Data Flow - creates a Spring Task, serves it from static web site (to SCDF), then runs it in a cron-like way via ...

- Uses MySQL tike in PCF for database(s)
- Uses PivotalMySQLWeb to set up MySQL - see https://github.com/pivotal-cf/PivotalMySQLWeb

1. create MySQL db service, e.g. "logdb"
2. load PivotalMySQLWeb 
3. *cf push* PivotalMySQLWeb, e.g.
```
---
applications:
- name: mysqlweb
  memory: 1024M
  instances: 1
  random-route: true
  path: ./target/PivotalMySQLWeb-0.0.1-SNAPSHOT.jar
  services:
    - logdb
  env:
    JAVA_OPTS: -Djava.security.egd=file:///dev/urando
    SPRING_PROFILES_ACTIVE: cloud
```

4. Browse to PivotalMySQLWeb app - click **SQL Worksheet**, and run this sql
```sql
create table names (name varchar(255));
```
followed by
```sql
create table emans (name varchar(255), eman varchar(255));
```
