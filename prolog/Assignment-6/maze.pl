/*
-- How to Run

-- To test the function on your test cases:
-- 1.) in the code directory type the command : "swipl maze.pl"
-- 2.) 
       a.) to see the shortest path between nodes A(source) and B(destination) type the command : "shortest_path(A,B,Result,_)." where A and B are not fault nodes.
       b.) to add new faulty node type command : "add_faulty_node(X)." where 0 <= X <= 99
       c.) to remove faulty node type command : "remove_faulty_node(X)." where 0 <= X <= 99
*/

/*Sample Example Queries
-------------------Example1(Refer to SampleExample.png and SampleMazedata.pl) ------------------------------ 
Query1- "shortest_path(11,99,Result,_)." Output - "Result = [11,12,13,14,24,25,26,36,46,56,57,58,59,69,79,89,99]."
Query2- "add_faulty_node(24)."      
Query3- "shortest_path(11,99,Result,_)." Output - "Result = [11,12,13,14,4,5,6,7,8,18,19,29,39,49,59,69,79,89,99]."
Query4- "remove_faulty_node(23)." 
Query5- "shortest_path(11,99,Result,_)." Output - "Result = [11,12,13,23,33,34,35,36,46,56,57,58,59,69,79,89,99]."
*/

/* this enables us to see the whole path when that path is long */
:- set_prolog_flag(answer_write_options,[max_depth(0)]).

/*import the maze from Mazedata.pl which is generated when generateMaze.cpp is executed */
:- include('Mazedata.pl').

/*to enable dynamic addition/removal of fault nodes in a query*/
:- dynamic(faultynode/1).

/*to check that the edge X-Y is connected or not*/
isConnected(X,Y,Length) :- 
       mazelink(X,Y),
       mazelink(Y,X),
       \+faultynode(X),
       \+faultynode(Y),
       Length=1.

/*to find a path from Source to Destination*/
path(Source,Destination,Path,Length) :-
       travel(Source,Destination,[Source],Q,Length), 
       reverse(Q,Path).

/*if Source and Destination is directly connected by an edge*/
travel(A,B,P,[B|P],L) :- 
       isConnected(A,B,L).

/*we are doing a dfs here to find a path from source to destination*/
travel(A,B,Visited,Path,L) :-
       isConnected(A,C,D),        
       C \== B,   
       \+member(C,Visited),
       travel(C,B,[C|Visited],Path,L1),
       L is D+L1.  

/*if source and destination is same*/
shortest_path(A,A,Path,Length) :-
       Length=0,
       Path=[A].

/*to find shortest path among all the paths*/
shortest_path(A,B,Path,Length) :-
   setof([P,L],path(A,B,P,L),Set),
   Set = [_|_],
   minimal(Set,[Path,Length]).

/*to find path of minimal length in the set*/
minimal([F|R],M) :- min(R,F,M).

min([],M,M).
min([[P,L]|R],[_,M],Min) :- L < M, !, min(R,[P,L],Min). 
min([_|R],M,Min) :- min(R,M,Min).

/*add faulty node during a query*/
add_faulty_node(Node):-
    assert(faultynode(Node)).

/*remove a faulty node during a query*/
remove_faulty_node(Node):-
    retract(faultynode(Node)).