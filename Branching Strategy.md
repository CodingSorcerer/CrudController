# branching strategy

## branches
for now we'll keep this simple. one branch will be made for each major relase. A major release is defined as a rlease that breaks old functionality in favor of new ways of doing things. IE removing depricated functions or changing contracts.

## Tags
Depending on how much has changed is how much we'll change the tag. The first number reprezents the major verson. This only changes when a branch changes The second number reprezents the maven relase. each time code is pushed to maven central, we increase the second number. so version 1.1.x would be the next available package on maven central. the last number reprezents features and bugs fixed. every time we change the code we increment the third number. So if you patch a bug, that would be 1.0.1, but then lets say your buddy adds an annotation. well now that's 1.0.2... etc. after so many features are added we release. 10 - 15 sounds like a good number for now unless they're particularly big changes.

## A note about Master
Master is where we'll keep all information like readmes and stuff like that. We dont really want it commited to code, however the license will stay with the code and here, as that's kind of important.
