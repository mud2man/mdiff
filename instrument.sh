rm -rf parse
javac oldTestCase/Solution.java
javac oldTestCase/Space.java
javac newTestCase/Solution.java
javac newTestCase/Space.java
java -jar target/mdiff-maven-plugin-0.0.1-SNAPSHOT.jar oldTestCase newTestCase parse outpot 
