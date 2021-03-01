fib :: (Eq a, Num a, Num p) => a -> p
fib 1 = 1
fib 2 = 1
fib n = fib(n-2) + fib (n-1)

main :: IO ()
main = do 
    putStrLn "Enter Input"
    inputString <- getLine
    let input = read inputString :: Integer
    let x = [0,1]
    print x
    print (fib input)
