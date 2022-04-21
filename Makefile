JVC = javac
.SUFFIXES:.java .class
.java.class:
	$(JVC) $*.java

CLASSES = \
	client.java\
	Server.java\
	ServerNode.java


default: classes

classes:$(CLASSES:.java=.class)


clean:
	$(RM) *.class
