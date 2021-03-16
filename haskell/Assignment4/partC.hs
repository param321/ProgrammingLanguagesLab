-- How to Run

-- To run the code and to input the comma seperated numbers:
-- 1.) in the code directory type the command: ghc -o partC partC.hs
-- 2.) then type the command: ./partC
-- 3.) Give the input (comma seperated numbers in the format "number1,number2,number3" for example "1,2,3,4,5") and then press enter
-- 4.) BST , inorder of BST , preorder of BST, postorder of BST are printed respectively

-- To load the function (use inputToprintDifferentOrdersInBST function to take input of comma seperated numbers and generate BST and print different different order of BST):
-- 1.) in the code directory type the command : ghci
-- 2.) then type the command : ":l partC.hs" to load the function
-- 3.) the type the command : inputToprintDifferentOrdersInBST
-- 3.) Give the input (comma seperated numbers in the format "number1,number2,number3" for example "1,2,3,4,5") and then press enter
-- 4.) BST , inorder of BST , preorder of BST, postorder of BST are printed respectively

--Test Cases
-- Input : 1,2,3,4,5,6
-- Output : 
-- Node Null 1 (Node Null 2 (Node Null 3 (Node Null 4 (Node Null 5 (Node Null 6 Null)))))
-- [1,2,3,4,5,6]
-- [1,2,3,4,5,6]
-- [6,5,4,3,2,1]
--
-- Input : 11
-- Output : 
-- Node Null 11 Null
-- [11]
-- [11]
-- [11]
--
-- Input : 10000,54,2344,1212,45,12
-- Output : 
-- Node (Node (Node (Node Null 12 Null) 45 Null) 54 (Node (Node Null 1212 Null) 2344 Null)) 10000 Null
-- [12,45,54,1212,2344,10000]
-- [10000,54,45,12,2344,1212]
-- [12,45,1212,2344,54,10000]
--
-- Input : 10,15
-- Output : 
-- Node Null 10 (Node Null 15 Null)
-- [10,15]
-- [10,15]
-- [15,10]

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

data BST a = Null | Node (BST a) a (BST a) deriving Show

insertNode :: Ord a => BST a -> a -> BST a
insertNode Null x = Node Null x Null
insertNode (Node left v right) x = 
    if v <= x then
         Node left v (insertNode right x)
    else 
        Node (insertNode left x) v right

insertListElementsToBST :: Ord a => BST a -> [a] -> BST a
insertListElementsToBST tr [] = tr
insertListElementsToBST tr (h:t) = insertListElementsToBST (insertNode tr h) t

listToBST :: Ord a => [a] -> BST a
listToBST [] = Null
listToBST (x:xs) = insertListElementsToBST (Node Null x Null) xs 

inorder :: BST a -> [a]
inorder Null = []
inorder (Node left v right) = inorder left ++ [v] ++ inorder right

preorder :: BST a -> [a]
preorder Null = []
preorder (Node left v right) = [v] ++ preorder left ++ preorder right

postorder :: BST a -> [a]
postorder Null = []
postorder (Node left v right) = postorder left ++ postorder right ++ [v]

inputToprintDifferentOrdersInBST :: IO ()
inputToprintDifferentOrdersInBST = do
    z <- getList
    let x = listToBST z
    print x
    print (inorder x)
    print (preorder x)
    print (postorder x)

main :: IO ()
main = do
    inputToprintDifferentOrdersInBST
    

