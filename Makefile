# compile all classes and then run App
runServer: *.java
	@javac WebApp.java
	@java WebApp 80
# compile and run JUnit tests
runTests: *.java
	@javac -cp .:../junit5.jar IntegrationTests.java BackendTests.java
	@java -jar ../junit5.jar -cp . -c IntegrationTests
	@java -jar ../junit5.jar -cp . -c BackendTests
# remove all .class files
clean:
	rm -f *.class
