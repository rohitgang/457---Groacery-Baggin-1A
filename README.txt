# 457---Grocery-Baggin-1A
*************************
Grocery Bagging 1A
CS457
09/29/2019
Collin Beckly, Steven Kim, Rohit Gangurde
**************************

OVERVIEW:

A program that determines a way to bag groceries that has constraints on what items can be bagged with what,
how much you can put in a bag, and the number of bags available.

INCLUDED FILES:

* Item.java - source file, builds the item objects with the proper attributes.
* Bag.java - source file, builds the bag objects with the proper attributes.
* State.java - source file, builds the states containing bag and item objects. 
* InvalidFileFormatException.java - source file, handles the exceptions in the programs.
* Driver.java - driver file, calls the item.java, bag.java, state.java and InvalidFileFormatException.java to start the process and find solutions using either breadth first or depth first. 

COMPILING AND RUNNING:

From the directory containing all source files, compile the driver class with the command :
$ javac Driver.java

Run the compiled class file with the command:
$ java Driver <filename> [-breadth / -depth]

Console output will give the results after the program finishes.

PROGRAM DESIGN AND IMPORTANT CONCEPTS:

The Item.java and Bag.java are the building blocks of this program. The Item.java class has the following attributes : weight, name, constraintsMap. The constraintsMap is the key in handling the constraints on the items. The hashmap has a value 1 where that item can be with another item. Let me demonstrate, consider an input to the program as such,
3   //number of bags available
7   // maximum bag size
bread 3 + rolls
rolls 2 + bread
squash 3 - meat
meat 5
lima_beans 1 - meat 

The hashMap for the bread will look like this,
      | bread | rolls | squash | meat | lima_beans |
bread |   1   |   1   |    0   |   0  |     0      |

Hence, the hashMap for the item - bread, dictates the constraints and controls items going in the bag.

