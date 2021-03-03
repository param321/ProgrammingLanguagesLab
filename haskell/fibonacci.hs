-- How to Run

-- To run the test cases:
-- 1.) in the code directory type the command: ghc -o fibonacci fibonacci.hs
-- 2.) then type the command: ./fibonacci

-- To test the function on your test cases:
-- 1.) in the code directory type the command : ghci
-- 2.) then type the command : ":l fibonacci.hs" to load the function
-- 3.) the type the command : fibonacci <number> to test the fibonacci function on the number

fibonacci :: Int -> Integer
fibonacci i = recu i 0 1
  where
    recu 0 a b = a
    recu i a b = recu (i-1) b (a+b)

main = do 
    -- 10 test cases

    let input = 10
    print (fibonacci input)

    let input = 20
    print (fibonacci input)

    let input = 100
    print (fibonacci input)

    let input = 200
    print (fibonacci input)

    let input = 1
    print (fibonacci input)

    let input = 1000
    print (fibonacci input)

    let input = 10000
    print (fibonacci input)

    let input = 100000
    print (fibonacci input)

    let input = 33
    print (fibonacci input)

    let input = 100002
    print (fibonacci input)
