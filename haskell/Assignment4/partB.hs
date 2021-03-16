-- How to Run

-- To run the code and to input the comma seperated numbers:
-- 1.) in the code directory type the command: ghc -o partB partB.hs
-- 2.) then type the command: ./partB 
-- 3.) Give the input (comma seperated numbers in the format "number1,number2,number3" for example "1,2,3,4,5") and then press enter
-- 4.) LCM of the generated list is printed

-- To load the function (use listLcm function to generate LCM of the list):
-- 1.) in the code directory type the command : ghci
-- 2.) then type the command : ":l partB.hs" to load the function
-- 3.) the type the command : inputToLcm
-- 3.) Give the input (comma seperated numbers in the format "number1,number2,number3" for example "1,2,3,4,5") and then press enter
-- 4.) LCM of the numbers is printed

--Test Cases
-- Input : 1,2,3,4,5,6
-- Output : 60
--
-- Input : 11
-- Output : 11
--
-- Input : 10000,54,2344,1212,45,12
-- Output : 7990110000
--
-- Input : 10,15
-- Output : 30

getNumber :: IO (Int,String)  
getNumber = do 
    c <- getChar  
    if c == '\n'|| c == ','  then 
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


getList :: IO [Integer]
getList = do 
    (flag,x) <- getNumber
    if x == "" then
        return []
    else do
        let int = read x :: Integer
        if flag == 0 then
            return [int]
        else
            do
            xs <- getList
            return (int:xs)


listLcm :: [Integer] -> Integer
listLcm x = 
    if length x > 2 then
        lcm (head x) (listLcm (tail x))
    else
        lcm (head x) (last x)

inputToLcm :: IO ()
inputToLcm = do
    z <- getList
    print (listLcm z)

main :: IO ()
main = do
    inputToLcm
