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

5. Build the task
```
mvn clean package
```

6. Copy the created jar file to a new directory, and create a manifest there:
```yml
---
applications:
- name: demotask
  memory: 1024M
  instances: 1
  random-route: true
  path: ./repo/demo-task-0.0.1-SNAPSHOT.jar
  services:
    - mysql
  env:
    JAVA_OPTS: -Djava.security.egd=file:///dev/urando
    SPRING_PROFILES_ACTIVE: cloud
```

You're going to install a static website from which SCDF task can pull the task jar
```
mkdir repo
cp ~/.m2/repository/demo/task/demo-task/0.0.1-SNAPSHOT/demo-task-0.0.1-SNAPSHOT.jar repo
cf push -f manifest-task.yml
```
