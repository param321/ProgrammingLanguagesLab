//How To Run
//In the terminal to compile java code enter command : javac Bank.java
//then to run the code enter command : java Part_C <Total_Number_of_Requests>

//Output
//it will print Execution time in miliseconds

import java.util.*;
import java.util.concurrent.ExecutorService; 
import java.util.concurrent.Executors;
import java.util.concurrent.locks.*;

class Bank{ 

    //this is our random variable that will generate all random values
    Random random = new Random(); 

    //Counters for each branch are maintained. While generation of unique account numbers each time, counter of that bramch will be increased.
    int counter[] = new int[10];

    //this will help us to lock the branch while doing add/delete/transfer customer operation
    Lock[] lockForBranchList = new Lock[10];

    //Class for customer
    public class Customer{
        private String accountNumber; //account number of customer
        private long balance; //account balance of customer

        public Customer(String accountNumber,long balance){ //Constructor for initialisation
            this.accountNumber = accountNumber;
            this.balance = balance;
        }
        
        public String getAccountNumber(){ //function to get account number
            return this.accountNumber;
        }

        public void setAccountNumber(String accountNumber){ //function to set account number of a CUstomer object
            this.accountNumber = accountNumber;
        }

        public long getBalance(){ //function to get account balance
            return this.balance;
        }

        public void setBalance(long balance){ //function to set account balance of a customer object
            synchronized(this){
                this.balance = balance;
            }
        }
    }

    HashMap <Integer,LinkedList<Customer>> customerList = new HashMap<Integer,LinkedList<Customer>>();  //list of customers branchwise

    public void makeCustomerList(){ //Function for initialisation of list

        String accountNumber="";
        long balance;

        for(int i=0;i<10;i++){
            counter[i] = 0; //Initialise counter value of each branch as 0
            LinkedList<Customer> branchCustomers= new LinkedList<Customer>();
            for(int j=0;j<10000;j++){ //We have to add 10^4 customers in each branch
                accountNumber = Long.toString((long)(i*(long)1000000000) + (long)counter[i]);  //Generate account no
                /*
                If branch value is 2 and counter value of branch is 1012
                then account number that will be generated: 20000001012
                */

                if(i==0){
                    //Special case if branch value is zero, add some initial zeros in beginning
                    while(accountNumber.length()<10){
                        accountNumber = "0" +accountNumber ;
                    }
                }

                balance = (random.nextInt(1000000))+((long)1000000000); //Generate balance
                Customer customer = new Customer(accountNumber, balance); //Create object for customer
                branchCustomers.add(customer); //Add it in branch list 

                counter[i]++; //Increment counter
            }
            customerList.put(i,branchCustomers); //Adding list of that branch in the Hashmap
        }
    }


    public class DepositCash implements Runnable{ //THread for depositing cash

        long depositAmount; //Deposit amount
        String accountNumber; //Account numbner

        DepositCash(long depositAmount,String accountNumber){ //Cpnstructor for initialisation
            this.depositAmount = depositAmount;
            this.accountNumber = accountNumber;
        }

        Customer customer; //Customer object
        public void run(){
            int branch = Integer.parseInt(Character.toString((accountNumber.charAt(0)))); //Get branch value

            ListIterator<Customer> it = customerList.get(branch).listIterator(); //Iterator pointing to starting of list

            Boolean customerFound = false; //Bool value to check if customer with this account number is found in list

            //Iterating through list to find given customer
            while(it.hasNext()){
                customer = it.next();
                if(customer.getAccountNumber().equals(accountNumber)){
                    customerFound = true;
                    break;
                }
            }

            if(!customerFound){ //If customer not found, do nothing
                //System.out.println("Customer Not Found");
                return;
            }

            long currentBalance = customer.getBalance(); //Get balance
            currentBalance += depositAmount; //Add deposit amount
            customer.setBalance(currentBalance); //Set new balance

            return;
        }
         
    }

    //function same as DepositCase just decrementing account balance to withdraw cash
    public class WithdrawCash implements Runnable{

        long withdrawAmount;
        String accountNumber;

        WithdrawCash(long withdrawAmount,String accountNumber){
            this.withdrawAmount = withdrawAmount;
            this.accountNumber = accountNumber;
        }

        Customer customer;
        public void run(){
            int branch = Integer.parseInt(Character.toString((accountNumber.charAt(0))));

            ListIterator<Customer> it = customerList.get(branch).listIterator();

            Boolean customerFound = false;

            while(it.hasNext()){
                customer = it.next();
                if(customer.getAccountNumber().equals(accountNumber)){
                    customerFound = true;
                    break;
                }
            }

            if(!customerFound){
                //System.out.println("Customer Not Found");
                return;
            }

            long currentBalance = customer.getBalance();

            if(currentBalance<withdrawAmount){
                //System.out.println("Not Enough Balance");
                return;
            }

            currentBalance -= withdrawAmount;
            customer.setBalance(currentBalance);
            return;

        }    
    }

    public class TransferMoney implements Runnable{

        long transferAmount; //transfer amount
        String sourceAccountNumber; //source account number
        String destinationAccountNumber; //destination account no

        TransferMoney(long transferAmount,String sourceAccountNumber,String destinationAccountNumber){ //Constructor for initialisation
            this.transferAmount = transferAmount;
            this.sourceAccountNumber = sourceAccountNumber;
            this.destinationAccountNumber = destinationAccountNumber;
        }

        Customer sourceCustomer; //object for source
        Customer destinationCustomer; //object for destination

        public void run(){
            int branchSource = Integer.parseInt(Character.toString((sourceAccountNumber.charAt(0)))); //find source branch

            int branchDestination = Integer.parseInt(Character.toString((destinationAccountNumber.charAt(0)))); //find destination branch

            ListIterator<Customer> it = customerList.get(branchSource).listIterator();

            Boolean sourceCustomerFound = false; //bool variable for checking existence of source 

            Boolean destinationCustomerFound = false; //bool variable for checking existence of destination

            //iterating through branch list to check source's existence
            while(it.hasNext()){
                sourceCustomer = it.next();
                if(sourceCustomer.getAccountNumber().equals(sourceAccountNumber)){
                    sourceCustomerFound = true;
                    break;
                }
            }

            if(!sourceCustomerFound){ //if not found do nothing
                //System.out.println("Source Customer Not Found");
                return;
            }

            it = customerList.get(branchDestination).listIterator();

            //checking if destination account exists by traversing the List of destination branch
            while(it.hasNext()){
                destinationCustomer = it.next();
                if(destinationCustomer.getAccountNumber().equals(destinationAccountNumber)){
                    destinationCustomerFound = true;
                    break;
                }
            }

            if(!destinationCustomerFound){ //if not found, do nothing
                //System.out.println("Destination Customer Not Found");
                return;
            }

            long sourceBalance = sourceCustomer.getBalance(); //Get source balance
            long destinationBalance = destinationCustomer.getBalance(); //Get destination balance

            if(sourceBalance<transferAmount){ //If source balance is lesser, withdrawal from source cant take place
                //System.out.println("Not Enough Balance in Source Account");
                return;
            }

            sourceBalance -= transferAmount; //withdraw transfer amount from source
            destinationBalance += transferAmount; //deposit in destination account
            sourceCustomer.setBalance(sourceBalance); //set new balance of source
            destinationCustomer.setBalance(destinationBalance); //set new balance of destination
            return;

        }    
    }

    public class AddCustomer implements Runnable{ //Tjread for adding new customer

        String accountNumber; //Account number
        long balance; //Balance
        int branch; //Branch

        AddCustomer(int updaterNumber){ //Constructor
            this.branch = updaterNumber; //Setting branch value
            this.balance = (random.nextInt(1000000))+((long)1000000000); //generating balance
            this.accountNumber = Long.toString((long)(updaterNumber*(long)1000000000) + (long)counter[updaterNumber]); //generating account number
            counter[updaterNumber]++; //increment counter corresponding to that branch  
            if(updaterNumber==0){
                //handle special case if branch=0, add some initial zeros in beginning 
                while(this.accountNumber.length()<10){
                    this.accountNumber= "0"+this.accountNumber;
                }
            }
        }

        public void run(){
            try{
                Customer customer = new Customer(accountNumber,balance); //create new customer object

                try{
                    lockForBranchList[branch].lock(); //apply lock
                    customerList.get(branch).addLast(customer); //add this account in branch list
                }finally{
                    lockForBranchList[branch].unlock(); //release lock
                }
                return;
            }catch(Exception e){
                return;
            }
        }
         
    }

    public class DeleteCustomer implements Runnable{ //Thread for deleting customer

        String accountNumber; //Account number

        DeleteCustomer(String accountNumber){
            this.accountNumber = accountNumber; //Constructor
        }

        public void run(){

            try{

            int branch = Integer.parseInt(Character.toString((accountNumber.charAt(0)))); //Get branch value

            ListIterator<Customer> it = customerList.get(branch).listIterator(); 

            Boolean customerFound = false; //Variable to check existence of customer in branch list
            int pos=0; //to find at what position account to be deleted is located

            //Traversing list for above purpose
            while(it.hasNext()){
                if(it.next().getAccountNumber().equals(accountNumber)){ //if account found
                    customerFound = true; //set variable true
                    break;
                }
                pos++; //increment value of pos
            }

            if(!customerFound){
                //System.out.println("Customer not found");
                return;
            }

            try{
                lockForBranchList[branch].lock(); //Apply lock
                customerList.get(branch).remove(pos); //Remove account from the list
            }finally{
                lockForBranchList[branch].unlock(); //Release lock
            }

            return;
            }catch(Exception e){
                return;
            }
        }
         
    }

    public class TransferCustomer implements Runnable{ //thread to transfer customer from one branch to another

        String accountNumber;
        int sourceBranch;
        int destinationBranch;

        TransferCustomer(String accountNumber,int destinationBranch){ //constructor for initialisaton
            this.accountNumber = accountNumber;
            this.sourceBranch = Integer.parseInt(Character.toString((accountNumber.charAt(0))));
            this.destinationBranch = destinationBranch;
        }

        Customer customer;
        public void run(){
            try{
            if(sourceBranch == destinationBranch){ //if source branch = destination branch, do nothing
                return;
            }

            ListIterator<Customer> it = customerList.get(sourceBranch).listIterator();

            Boolean customerFound = false;
            int pos=0; //to check at what index in branch list, initial account was located

            //traversing source list to check if initial account with account no exists in given branch
            while(it.hasNext()){
                customer = it.next();
                if(customer.getAccountNumber().equals(accountNumber)){
                    customerFound = true; //set as true
                    break;
                }
                pos++; //increase pos value
            }

            if(!customerFound){ //if not found, do nothing
                //System.out.println("Customer not found");
                return;
            }

            try{
                lockForBranchList[sourceBranch].lock(); //apply lock on source branch
                lockForBranchList[destinationBranch].lock(); //apply lock on destination branch

                customerList.get(sourceBranch).remove(pos); //remove account from source branch

                //generate new account number for new account in destination branch
                customer.accountNumber = Long.toString((long)(destinationBranch*(long)1000000000) + (long)counter[destinationBranch]);
                counter[destinationBranch]++;
                if(destinationBranch==0){
                    while(customer.accountNumber.length()<10){
                        customer.accountNumber = "0" + customer.accountNumber;
                    }
                }
                customerList.get(destinationBranch).addLast(customer); //add new account in the destination branch

            }finally{

                //Release all locks acquired
                lockForBranchList[sourceBranch].unlock(); 
                lockForBranchList[destinationBranch].unlock();
            }

            return;

            }catch(Exception e){
                return;
            }
        }
         
    }

    //our code will first run this method
    public static void main(String args[]){
        try {
            Bank bank = new Bank();
            bank.run(args);
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    //this contains all the main code
    public void run(String args[]){

        //error if no arguments given 
        if(args.length==0){
            System.out.println("Error!! Please Enter an argument which should be number of requests");
            return;
        }

        //error of more than one arguments given
        if(args.length>1){
            System.out.println("Error!! Please Enter only one argument which should be number of requests");
            return;
        }

        int totalNoOfRequests;

        try{
            totalNoOfRequests = Integer.parseInt(args[0]);
        }catch(Exception e){
            System.out.println("Error!! The argument should be a number");
            return;
        }

        long timeStart = System.currentTimeMillis(); //initialise  start time

        makeCustomerList(); //initalise list

        ExecutorService executorServices[] = new ExecutorService[10]; //Array of executor services 
        
        for(int i=0;i<10;i++){ //assigning 10 threads to each branch via threadpool
            executorServices[i] = Executors.newFixedThreadPool(10);
        }

        int sourceBranch;
        int destinationBranch;
        int amount;
        int accInBranch;
        String accountNumber;
        String destinationAccountNumber;
        for(int i=0;i<totalNoOfRequests;i++){ //carry out total requests given as command line argument
            double randomVariable = random.nextDouble(); //choose a random double value
            if(randomVariable<0.33){ //probability= 0.33 --> deposit cash

                sourceBranch = random.nextInt(10); //choose source branch randomly
                amount = random.nextInt(100000); //choose amount
                accInBranch = random.nextInt(10000); //choosing account in that branch
                accountNumber = Long.toString(((long)sourceBranch*(long)1000000000)+(long)accInBranch); //generating account number
                if(sourceBranch==0){
                    while(accountNumber.length()<10){
                        accountNumber="0"+accountNumber;
                    }
                }
                executorServices[sourceBranch].execute(new DepositCash(amount,accountNumber)); //execute deposit cash thread
            }else if(randomVariable<0.66){
                //Probability= 0.33 ----> Withdraw cash request
                sourceBranch = random.nextInt(10); //source branch randomly
                amount = random.nextInt(100000); //choose amount
                accInBranch = random.nextInt(10000); //choose account in branch
                accountNumber = Long.toString(((long)sourceBranch*(long)1000000000)+(long)accInBranch); //generate account number
                if(sourceBranch==0){
                    while(accountNumber.length()<10){
                        accountNumber="0"+accountNumber;
                    }
                }
                executorServices[sourceBranch].execute(new WithdrawCash(amount,accountNumber)); //carry out withdrawal
            }else if(randomVariable<0.99){
                //probability= 0.33 ----> transfer amount from one account to another account
                amount = random.nextInt(100000); //choose amount

                sourceBranch = random.nextInt(10); //choose source branch
                accInBranch = random.nextInt(10000); //choose source account
                accountNumber = Long.toString(((long)sourceBranch*(long)1000000000)+(long)accInBranch); //generate account no
                if(sourceBranch == 0){
                    while(accountNumber.length()<10){
                        accountNumber="0"+accountNumber;
                    }
                }

                destinationBranch = random.nextInt(10); //choose destination branch
                accInBranch = random.nextInt(10000); //choose destination account
                destinationAccountNumber = Long.toString(((long)destinationBranch*(long)1000000000)+(long)accInBranch); //generate account no of destination
                if(destinationBranch == 0){
                    while(destinationAccountNumber.length()<10){
                        destinationAccountNumber = "0" + destinationAccountNumber;
                    }
                }

                executorServices[sourceBranch].execute(new TransferMoney(amount,accountNumber,destinationAccountNumber)); //carry out transfer money 
            }else if(randomVariable<0.993){
                //probability= 0.003 ===> Add customer query
                sourceBranch = random.nextInt(10); //choose branch in which account to be added
                executorServices[sourceBranch].execute(new AddCustomer(sourceBranch)); //Add account
            }else if(randomVariable<0.996){
                //probability= 0.003 ===> Delete customer query
                sourceBranch = random.nextInt(10); //choose branch in which account to be deleted
                accInBranch = random.nextInt(10000); //choose account to be deleted
                accountNumber = Long.toString(((long)sourceBranch*(long)1000000000)+(long)accInBranch); //generate account no
                if(sourceBranch == 0){
                    while(accountNumber.length()<10){
                        accountNumber="0"+accountNumber;
                    }
                }

                executorServices[sourceBranch].execute(new DeleteCustomer(accountNumber)); //delete account with given account number
            }else{
                //probability= 0.004 ===> Transfer customer from one branch to another branch
                sourceBranch = random.nextInt(10); //choose source branch
                accInBranch = random.nextInt(10000); //choose source account
                accountNumber = Long.toString(((long)sourceBranch*(long)1000000000)+(long)accInBranch); //generate account number
                if(sourceBranch == 0){
                    while(accountNumber.length()<10){
                        accountNumber="0"+accountNumber;
                    }
                }

                destinationBranch = random.nextInt(10); //choose destination branch in which account has to be added
                executorServices[sourceBranch].execute(new TransferCustomer(accountNumber,destinationBranch)); //transfer account from source branch to destination branch
            }
        }

        for(int i=0;i<10;i++){
            executorServices[i].shutdown();
            while(!executorServices[i].isTerminated()){ //wait for threads of each executer service to terminate
                
            }
        }

        long timeEnd = System.currentTimeMillis(); //initialise end time
        long timeElapsed = timeEnd - timeStart; //calculate total time elapsed
        System.out.println("Execution time in milliseconds: " + timeElapsed); //print time of execution
    }
}
