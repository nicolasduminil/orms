# Quarkus issue #...

This project aims at illustrating an apparent issue that I found and submitted 
to the support under the reference ...

The project exposes two simple REST services that CRUD two simple JPA entities, 
using Panache repositories. The entities maintain a bidirectional one-to-many
relationship.

In order to avoid #29225, I'm using Jackson2, instead of Jakarta JSON Binding, 
to serialize payloads. And in order to avoid infinite redundancy during serialization,
`@JsonManagedReference/@JsonBackReference` are used at the JPA entities definition
level.

Running a simple RESTassured test raises the following exception:

    ERROR [io.qua.ver.htt.run.QuarkusErrorHandler] (executor-thread-1) HTTP Request to /customers failed, error id: 3cb2f243-8560-4e34-865b-6ffa1b214a3b-1: java.lang.IllegalArgumentException: Cannot handle managed/back reference 'defaultReference': type: value deserializer of type com.fasterxml.jackson.databind.deser.std.UntypedObjectDeserializerNR does not support them

This exception seems to be raised only with Quarkus as doing the identical test 
with Jakarta EE (Wildfly), works as expected.

## Testing

    $ git clone 
    $ cd ...
    $ mvn -DskipTests package
    $ cd quarkus-orm     #Testing with Quarkus
    $ mvn test           #Test fails, exception 
    $ cd ../jakarta-orm  #Test with Jakarta EE 10 (Wildfly 32)
    $ mvn test           # Test succeeds