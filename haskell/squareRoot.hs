-- How to Run

-- To run the test cases:
-- 1.) in the code directory type the command: ghc -o squareRoot squareRoot.hs
-- 2.) then type the command: ./squareRoot

-- To test the function on your test cases:
-- 1.) in the code directory type the command : ghci
-- 2.) then type the command : ":l squareRoot.hs" to load the function
-- 3.) the type the command : squareRoot <number> to test the squareRoot function on the number

squareRootFun :: (Ord a, Fractional a) => a -> a -> a -> a
squareRootFun lowerBound number upperBound
  | upperBound - lowerBound < 0.0000001 = upperBound
  | (((lowerBound + upperBound)/2) * ((lowerBound + upperBound)/2)) >= number = squareRootFun lowerBound number ((lowerBound + upperBound) / 2)
  | otherwise = squareRootFun ((lowerBound + upperBound) / 2) number upperBound

squareRoot :: (Ord a, Fractional a) => a -> a
squareRoot number = squareRootFun 0 number (max 1 number)

main = do
    --10 test cases

    let input = 23.56
    print (squareRoot input)

    let input = 0.04
    print (squareRoot input)

    let input = 10000.16
    print (squareRoot input)

    let input = 196
    print (squareRoot input)

    let input = 58784594.4574
    print (squareRoot input)

    let input = 34878549845985
    print (squareRoot input)

    let input = 1.44
    print (squareRoot input)

    let input = 9.09
    print (squareRoot input)

    let input = 0.25
    print (squareRoot input)

    let input = 1.01
    print (squareRoot input)