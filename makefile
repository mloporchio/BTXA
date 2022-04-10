#
#	File:				makefile
#	Author:			Matteo Loporchio
# Thanks to: 	https://www.cs.swarthmore.edu/~newhall/unixhelp/javamakefiles.html
#

JC=javac
JFLAGS=-cp ".:./lib/guava-31.1-jre.jar"

.SUFFIXES: .java .class

.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	Geometry.java \
	Hash.java \
	Interval.java \
	IntNode.java \
	LeafNode.java \
	Node.java \
	Record.java \
	Test.java \
	Tree.java \
	Utility.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
