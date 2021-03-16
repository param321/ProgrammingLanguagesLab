-- How to Run

-- To run the code and to input the comma seperated numbers:
-- 1.) in the code directory type the command: ghc -o partA partA.hs
-- 2.) then type the command: ./partA 
-- 3.) Give the input (comma seperated numbers in the format "number1,number2,number3" for example "1,2,3,4,5") and then press enter
-- 4.) Output list is generated and printed

-- To load the function (use getList function to convert comma seperated numbers to list):
-- 1.) in the code directory type the command : ghci
-- 2.) then type the command : ":l partA.hs" to load the function
-- 3.) the type the command : getList 
-- 3.) Give the input (comma seperated numbers in the format "number1,number2,number3" for example "1,2,3,4,5") and then press enter
-- 4.) Output list printed

--Test Cases
-- Input : 1,2,3,4,5,6
-- Output : [1,2,3,4,5,6]
--
-- Input : 11
-- Output : [11]
--
-- Input : 10000,54,2344,1212,45,12
-- Output : [10000,54,2344,1212,45,12]

--this function parse each number from comma seperated numbers and return the number and also indicated using a flag that we have parsed all the numbers or not
getNumber :: IO (Int,String)  
getNumber = do 
    c <- getChar  
    if c == '\n'|| c == ','  then --if delimiter is encountered returrn the number
        if c == ',' then
            return (1,"")
        else
            return (0,"")
    else 
        do 
        (flag,s) <- getNumber  
        if flag == 0 then
            return (0,c:s)
        else 
            return (1,c:s)

-- this function converts comma seperated numbers to list of numbers
getList :: IO [Integer]
getList = do 
    (flag,x) <- getNumber
    if x == "" then --if there are no numbers left
        return []
    else do
        let int = read x :: Integer --converts string to integer
        if flag == 0 then --if the line has ended
            return [int]
        else
            do
            xs <- getList --re calling the function to add other numbers to list
            return (int:xs)

main :: IO ()
main = do
    z <- getList -- we will get the list in variable z
    print z
