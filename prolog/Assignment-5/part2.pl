/*
-- How to Run

-- To test the function on your test cases:
-- 1.) in the code directory type the command : "swipl part2.pl"
-- 2.) then type the command : "sublist(list1,list2)." where list1 and list 2 are standard list for example "sublist([a,b],[a,e,b,d,s,e])."
-- 3.) we will get the output true or false depending on is list1 is sublist of list2
*/

/*empty list is always a sublist of any list*/
sublist([],_).

/*removing head element if head of both list is same */
sublist([H|A],[H|B]):- sublist(A,B).

/*removing any arbitrary elemrnts of second list and matching rest of second list with the first list*/
sublist([H|A],[_|B]):- sublist([H|A],B).
