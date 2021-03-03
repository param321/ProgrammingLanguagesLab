-- How to Run

-- To run the test cases:
-- 1.) in the code directory type the command: ghc -o quickSort quickSort.hs
-- 2.) then type the command: ./quickSort

-- To test the function on your test cases:
-- 1.) in the code directory type the command : ghci
-- 2.) then type the command : ":l quickSort.hs" to load the function
-- 3.) the type the command : quickSort <list of numbers> to test the quickSort function on the list

quickSort :: Ord a => [a] -> [a]
quickSort [] = []
quickSort (x : xs) =
  quickSort leftarray ++ [x] ++ quickSort rightarray
  where
    leftarray = [i | i <- xs, i <= x]
    rightarray = [i | i <- xs, i > x]

main = do 
    -- 10 test cases

    let x =[3,2,1]
    print (quickSort x)

    let x =[1]
    print (quickSort x)

    let x =[1,3,2.99]
    print (quickSort x)

    let x =[1.1,1.0,1.3]
    print (quickSort x)

    let x =[30,30,30,30]
    print (quickSort x)

    let x =[1,6,3,8,2,4,3,2]
    print (quickSort x)

    let x =[1,7,7,7,7,7,4,4,4,4,4,4,2,2,2,2,2,2]
    print (quickSort x)

    let x =[8,5,3,1]
    print (quickSort x)

    let x =[1,9,2,8,3,7,4,6,5]
    print (quickSort x)

    let x =[1,5,3]
    print (quickSort x)

