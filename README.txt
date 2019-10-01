*************************
Grocery Bagging 1A
CS457
09/29/2019
Collin Beckly, Steven Kim, Rohit Gangurde
**************************

OVERVIEW :

A program that determines a way to bag groceries that has constraints on what items can be bagged 
with what, how much you can put in a bag, and the number of bags available.

INCLUDED FILES :

* Item.java - source file, builds the item objects with the proper attributes.
* Bag.java - source file, builds the bag objects with the proper attributes.
* State.java - source file, builds the states containing bag and item objects. 
* InvalidFileFormatException.java - source file, handles the exceptions in the programs.
* Driver.java - driver file, calls the item.java, bag.java, state.java and InvalidFileFormatException.jav
  a to start the process and find solutions using either breadth first or depth first. 

COMPILING AND RUNNING :

To compile and run the compiled class file, run this:
$ bagit <filename> [-breadth / -depth]

Console output will give the results after the program finishes and it will clear the .class files

PROGRAM DESIGN AND IMPORTANT CONCEPTS :

The Item.java and Bag.java are the building blocks of this program. The Item.java class has the following 
attributes : weight, name, constraintsMap. The constraintsMap is the key in handling the constraints on the items.
The hashmap has a value 1 where that item can be with another item. Let me demonstrate, consider an input to the 
program as such,

3   //number of bags available
7   // maximum bag size
bread 3 + rolls
rolls 2 + bread
squash 3 - meat
meat 5
lima_beans 1 - meat 

The hashMap for the bread can be visualised like this,
      | bread | rolls | squash | meat | lima_beans |
bread |   1   |   1   |    0   |   0  |     0      |

Hence, the hashMap for the item - bread, dictates the constraints and controls items going in the bag.

The Bag.java class has the following attributes : maxSize, currentWeight, bagConstraints, itemsInBag.
The maxSize variable is used to set a limit on the weight of the items in the bag. The currentWeight 
variable helps in keeping a check on the items in the bag not exceeding the limit for that bag.
The bagConstraints is a hashMap. The utility of this hashMap is to check if the item we are putting
in the bag agrees with the items already in the bag. We chose hashMap as the data structure to check 
for constraints as this helps us keep the constraint checking efficient as we are retrieving value in 
constant time and we are just using the '&' operator to check the retrieved values. The itemsInBag 
variable is an ArrayList of type Items. This helps us check if anymore items can be added to the bag and 
if they satisfy the constraint.

The State.java class has the following attributes : totalBags, itemsNotAdded, allItems.
The totalBags variable is an ArrayList of type Bag. It has the number of Bags specfied as input. 
The itemsNotAdded variable is an ArrayList of type Item. It has the items not added to the bags for that state.
The allItems variable is an ArrayList of type Item. This does not change during any point in the program.
The State.java class plays a key role when implementing depth first or breadth first search. It creates the 
nextPossibleStates from its attributes. If an empty bag is encountered, it puts the available item in that bag,
terminates and returns that state. It also has a isComplete method which checks if the itemsNotAdded list 
is empty, if it's empty then it returns true, false otherwise. 

The Driver.java class is possibly the most important class. This is where we parse the input file for the 
bag information using the Scanner class. After parsing the first two lines of the file for the bag information, 
we go through the rest of the files to obtain all the items we have at our disposal. We also initilaize bag objects
in Bags arrayList. After getting all the items and initializng them, we parse the file again. This time we parse
the file to obtain the item constraints and initialize the item constraints map. The bag constraints map is initialized with
one's (true) because there is nothing in the bag at this time. We maintain a copy of all items in the dupeItems arrayList.
We have a 'if' statement which checks the command line argument for either a breadth or depth command. 
If the input command is 'depth', we initialize a stack of type state. We add a new state to the list using the 
bags, items, dupeItems arrayList. We chose to perform depth first search iteratively. We have while loop which terminates
if we the states arrayList is empty or if we find a successStates. Hence we perform depth first search greedily. 
If the input command is 'breadth', we initialize a linkedList od type state. We add a new state to the list using
the bags, items, dupeItems arrayList. We chose to perform breadth first search iteratively. We have a while loop which 
terminates if we the states arrayList is empty. So, our depth first search implementation terminates and prints one 
of the successful states. While the breadth first search implementation returns all the successful states.

TESTING :

We tested our program against all sorts of input. We handle our exceptions with a try-catch method. 
Almost all of the erros all handled by us. We haven't handled the Java Heap Space Memory Error.
When parsing the files, we split a line using the whitespace character. We don't do this :
 "String[] splitLine = line.split(" ");"
Instead we used a regular expression to split the line like this :
 "String[] splitLine = line.split("\\s+");"
To handle any trailing or leading whitespaces when parsing, we use trim() method to get rid of them.

DISCUSSION :

One of the main problems for this program was to maintain a deep and a shallow copy. When implementing states
we had to be sure that few of our array lists don't change when transitioning between states. So we had to 
research about it. Because there were so many duplicate array lists, it was getting difficult to keep track
of the copies. 

OUTPUT: 

If the program is ran with the -breadth option, the program would output all the possible states :
success
item0 item2 item11 
item1 item4 
item3 item12 
item5 item14 
item6 item9 
item7 item10 
item8 
item13 
success
item0 item2 item11 
item1 item4 
item3 item12 
item5 
item6 item9 
item7 item10 item14 
item8 
item13 
.
.
.
.
success
item0 item14 
item1 
item2 item13 
item3 item11 item12 
item4 item5 
item6 item9 
item7 item10 
item8 
success
item0 
item1 
item2 item13 
item3 item11 item12 
item4 item5 
item6 item9 
item7 item10 item14 
item8 

If the program is ran with the -depth option, the program would output the first success state:
success
item0 
item1 
item2 item13 
item3 item11 item12 
item4 item5 
item6 item9 
item7 item10 item14 
item
