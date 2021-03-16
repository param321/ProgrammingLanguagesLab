
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


getArray :: IO [Integer]
getArray = do 
    (flag,x) <- getNumber
    if x == "" then
        return []
    else do
        let int = read x :: Integer
        if flag == 0 then
            return [int]
        else
            do
            xs <- getArray
            return (int:xs)



main :: IO ()
main = do
    z <- getArray
    print z
