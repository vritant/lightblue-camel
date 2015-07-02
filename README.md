# lightblue-camel

A camel component for lightblue.

For examples, see included integration tests using in memory lightblue instance.
* Route using producer: [TestInboundRoute](src/test/java/com/redhat/lightblue/camel/TestInboundRoute.java)
* Route using polling consumer: [TestOutboundRoute](src/test/java/com/redhat/lightblue/camel/TestOutboundRoute.java)

The component uses Guice for dependency injection and expects lightblue client (producer) or both client and find request (polling consumer). See [TestCamelModule](src/test/java/com/redhat/lightblue/camel/TestCamelModule.java).
