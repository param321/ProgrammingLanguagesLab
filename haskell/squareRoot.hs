-- squareRoot a b c = 
--     if c-a < 0.0000001
--         then c
--     else if ((a+c)*(a+c))/4 >= b*b
--             then squareRoot a b (a+c)/2
--         else squareRoot (a+c)/2 b c

squareRoot :: (Ord a, Fractional a) => a -> a -> a -> a
squareRoot a b c
  | c - a < 0.0000001 = c
  | ((a + c) * (a + c)) / 4 >= b = squareRoot a b ((a + c) / 2)
  | otherwise = squareRoot ((a + c) / 2) b c

main :: IO ()
main = do 
    putStrLn "Enter Input"
    input <- readLn 
    print (squareRoot 0 input 99)