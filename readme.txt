David Weinstein

Known bugs
The input handler does not recognize decimal numbers that lack a leading digit (eg 0.6 works, but .6 fails).
The error messages aren't always descriptive.

Design details specifics to your code
KDNodes are designed to be able to wrap anything as long as they have a location that can be expressed as a list of doubles. This could be made more portable by making doubles into a more abstract class.
I have a separate KDNode and KDTree class. This is perhaps less elegant than having KDTree be tree that recursively consists of KDTrees, but it means that I can separate tree logic (the tree creation and helper functions (functions that call recursive functions)) from the KDNode logic (recursive functions and data accessor methods).
The CSV Reader is intended to be entirely decoupled from this project.
The graphics are done via having a normal html form and then override the effect of clicking the button to be sending an ajax request and writing the response string to the page.

Any runtime/space optimizations you made beyond the minimum requirements
I used a minmaxPriorityQueue to keep track of the nearest neighbors rather than manually resorting the list of neighbors every time I have to add to it.

How to run any tests you wrote/tried by hand
I haven't added tests yet. I tried manually calling neighbors 1 7.0 -0.78 -0.3 to see if it returns the right location - it does.

How to build/run your program
Use the included run file. The first argument is the address of the datafile and is mandatory. The second argument "--gui" is optional and launches a gui accessible at http://localhost:4567/gui
Example run order: ./run /home/d/workspace2/WeinsteinStars/stardata.csv --gui
Answers to design questions

I would add recognition for that command (eg a regular expression) to the CommandHandler class, functionality to the KDNode class, and the first call to that functionality to the KDTree class.

Earth is curved, so the euclidean distance between two lat and long points doesn't map to the actual distance between those points. 
