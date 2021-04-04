/*
-- How to Run

-- To test the function on your test cases:
-- 1.) in the code directory type the command : "swipl part1.pl"
-- 2.) then type the command : "squareroot(X,1,Accuracy)." where X will be the positive number whose squareroot we want and accuracy is the value of accuracy
        For Example - "squareroot(100,1,0.001)." here we want the sqaureroot of 100 with accuracy 0.001
-- 3.) we will get the output 
*/

/*recurring the function and getting closer to squareroot to find the root according to the Accuracy*/
squareroot(X,Result,Accuracy):-
    New is (Result + X/Result)/2,
	abs(Result*Result - X, Z),
	Z > Accuracy,
	squareroot(X,New,Accuracy).

/*if the Result is squareroot of X according to Accuracy then print the Result*/
squareroot(X,Result,Accuracy):-
	abs(Result*Result - X, Z),
	Z =< Accuracy,
    write('Square root of '),
    write(X),
    write(' is '),
	write(Result).