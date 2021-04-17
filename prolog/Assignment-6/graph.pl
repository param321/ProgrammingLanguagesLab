/* this enables us to see the whole path when that path is long */
:- set_prolog_flag(answer_write_options,[max_depth(0)]).

/*import the maze from Mazedata.pl which is generated when generateMaze.cpp is executed */
:- include('Mazedata.pl').

/*to enable dynamic addition/removal of fault nodes in a query*/
:- dynamic(faultynode/1).

/*to check that the edge X-Y is connected or not*/
connected(X,Y,L) :- 
       mazelink(X,Y),
       mazelink(Y,X),
       \+faultynode(X),
       \+faultynode(Y),
       L=1.

/*to find a path from source A to destination B*/
path(A,B,Path,Len) :-
       travel(A,B,[A],Q,Len), 
       reverse(Q,Path).

travel(A,B,P,[B|P],L) :- 
       connected(A,B,L).
travel(A,B,Visited,Path,L) :-
       connected(A,C,D),           
       C \== B,
       \+member(C,Visited),
       travel(C,B,[C|Visited],Path,L1),
       L is D+L1.  

/*to find shortest path among all the paths*/
shortest_path(A,B,Path,Length) :-
   setof([P,L],path(A,B,P,L),Set),
   Set = [_|_],
   minimal(Set,[Path,Length]).

minimal([F|R],M) :- min(R,F,M).

min([],M,M).
min([[P,L]|R],[_,M],Min) :- L < M, !, min(R,[P,L],Min). 
min([_|R],M,Min) :- min(R,M,Min).

/*add faulty node during a query*/
add_faulty(X):-
    assert(faultynode(X)).

/*remove a faulty node during a query*/
remove_faulty(X):-
    retract(faultynode(X)).
