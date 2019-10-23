JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	State.java \
	Bag.java \
	Item.java \
	Driver.java

default: all

all:	$(CLASSES:.java=.class)

.run:
	java Driver

clean:
	$(RM) *.class
