rm Space0.class
rm Space0.dis
cp Space0.java Space.java
javac Space.java
rm Space.java
mv Space.class Space0.class
javap -c -v Space0.class >> Space0.dis

rm Space1.class
rm Space1.dis
cp Space1.java Space.java
javac Space.java
rm Space.java
mv Space.class Space1.class
javap -c -v Space1.class >> Space1.dis

java -jar target/instrumenter-maven-plugin-0.0.1-SNAPSHOT.jar Space0.class Space1.class parse
