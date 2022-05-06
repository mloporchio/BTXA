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
	FilterInputs.java \
	FilterOutputs.java \
	Geometry.java \
	Hash.java \
	Interval.java \
	IntNode.java \
	LeafNode.java \
	Node.java \
	Output.java \
	Query.java \
	Record.java \
	Test.java \
	TestQuery.java \
	Tree.java \
	Utility.java \
	VContainer.java \
	VLeaf.java \
	VObject.java \
	VPruned.java \
	VResult.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
