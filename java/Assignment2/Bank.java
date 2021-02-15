import java.util.*;
import java.io.*;

class Bank{ 

    Random random = new Random(); 

    int counter[] = new int[10];

    public class Customer{
        private String accountNumber;
        private long balance;

        public Customer(String accountNumber,long balance){
            this.accountNumber = accountNumber;
            this.balance = balance;
        }
        
        public String getAccountNumber(){
            return this.accountNumber;
        }

        public void setAccountNumber(String accountNumber){
            this.accountNumber = accountNumber;
        }

        public long getBalance(){
            return this.balance;
        }

        public void setBalance(long balance){
            this.balance = balance;
        }
    }

    HashMap <Integer,LinkedList<Customer>> customerList = new HashMap<Integer,LinkedList<Customer>>(); 

    public void makeCustomerList(){

        String accountNumber="";
        long balance;

        for(int i=0;i<10;i++){
            counter[i] = 0;
            //accountNumber = Character.toString((char)(i+'0'));
            LinkedList<Customer> branchCustomers= new LinkedList<Customer>();
            for(int j=0;j<10000;j++){
                accountNumber = Long.toString((long)(i*(long)1000000000) + (long)counter[i]);

                if(i==0){
                    while(accountNumber.length()<10){
                        accountNumber = "0" +accountNumber ;
                    }
                }

                balance = random.nextInt(1000000000);
                Customer customer = new Customer(accountNumber, balance);
                branchCustomers.add(customer);

                counter[i]++;
                //System.out.println(accountNumber);
            }
            customerList.put(i,branchCustomers);
        }
    }


    public class DepositCash implements Runnable{

        long depositAmount;
        String accountNumber;

        DepositCash(long depositAmount,String accountNumber){
            this.depositAmount = depositAmount;
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
                System.out.println("Customer Not Found");
                return;
            }

            long currentBalance = customer.getBalance();
            currentBalance += depositAmount;
            customer.setBalance(currentBalance);
            return;
        }
         
    }

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
                System.out.println("Customer Not Found");
                return;
            }

            long currentBalance = customer.getBalance();

            if(currentBalance<withdrawAmount){
                System.out.println("Not Enough Balance");
                return;
            }

            currentBalance -= withdrawAmount;
            customer.setBalance(currentBalance);
            return;

        }    
    }

    public class TransferMoney implements Runnable{

        long transferAmount;
        String sourceAccountNumber;
        String destinationAccountNumber;

        TransferMoney(long transferAmount,String sourceAccountNumber,String destinationAccountNumber){
            this.transferAmount = transferAmount;
            this.sourceAccountNumber = sourceAccountNumber;
            this.destinationAccountNumber = destinationAccountNumber;
        }

        Customer sourceCustomer;
        Customer destinationCustomer;

        public void run(){
            int branchSource = Integer.parseInt(Character.toString((sourceAccountNumber.charAt(0))));

            int branchDestination = Integer.parseInt(Character.toString((destinationAccountNumber.charAt(0))));

            ListIterator<Customer> it = customerList.get(branchSource).listIterator();

            Boolean sourceCustomerFound = false;

            Boolean destinationCustomerFound = false;

            while(it.hasNext()){
                sourceCustomer = it.next();
                if(sourceCustomer.getAccountNumber().equals(sourceAccountNumber)){
                    sourceCustomerFound = true;
                    break;
                }
            }

            if(!sourceCustomerFound){
                System.out.println("Source Customer Not Found");
                return;
            }

            it = customerList.get(branchDestination).listIterator();

            while(it.hasNext()){
                destinationCustomer = it.next();
                if(destinationCustomer.getAccountNumber().equals(destinationAccountNumber)){
                    destinationCustomerFound = true;
                    break;
                }
            }

            if(!destinationCustomerFound){
                System.out.println("Destination Customer Not Found");
                return;
            }

            long sourceBalance = sourceCustomer.getBalance();
            long destinationBalance = destinationCustomer.getBalance();

            if(sourceBalance<transferAmount){
                System.out.println("Not Enough Balance in Source Account");
                return;
            }

            sourceBalance -= transferAmount;
            destinationBalance += transferAmount;
            sourceCustomer.setBalance(sourceBalance);
            destinationCustomer.setBalance(destinationBalance);
            return;

        }    
    }

    public class AddCustomer implements Runnable{

        String accountNumber;
        long balance;
        int branch;

        AddCustomer(int updaterNumber){
            this.branch = updaterNumber;
            this.balance = random.nextInt(1000000000);
            this.accountNumber = Long.toString((long)(updaterNumber*(long)1000000000) + (long)counter[updaterNumber]);
            counter[updaterNumber]++;
            if(updaterNumber==0){
                while(this.accountNumber.length()<10){
                    this.accountNumber= "0"+this.accountNumber;
                }
            }
        }

        public void run(){
            Customer customer = new Customer(accountNumber,balance);
            customerList.get(branch).addLast(customer);
            return;
        }
         
    }

    public class DeleteCustomer implements Runnable{

        String accountNumber;

        DeleteCustomer(String accountNumber){
            this.accountNumber = accountNumber;
        }

        public void run(){

            int branch = Integer.parseInt(Character.toString((accountNumber.charAt(0))));

            ListIterator<Customer> it = customerList.get(branch).listIterator();

            Boolean customerFound = false;
            int pos=0;

            while(it.hasNext()){
                if(it.next().getAccountNumber().equals(accountNumber)){
                    customerFound = true;
                    break;
                }
                pos++;
            }

            if(!customerFound){
                System.out.println("Customer not found");
                return;
            }
            customerList.get(branch).remove(pos);
            return;
        }
         
    }

    public class TransferCustomer implements Runnable{

        String accountNumber;
        int sourceBranch;
        int destinationBranch;

        TransferCustomer(String accountNumber,int destinationBranch){
            this.accountNumber = accountNumber;
            this.sourceBranch = Integer.parseInt(Character.toString((accountNumber.charAt(0))));
            this.destinationBranch = destinationBranch;
        }

        Customer customer;
        public void run(){

            if(sourceBranch == destinationBranch){
                return;
            }

            ListIterator<Customer> it = customerList.get(sourceBranch).listIterator();

            Boolean customerFound = false;
            int pos=0;

            while(it.hasNext()){
                customer = it.next();
                if(customer.getAccountNumber().equals(accountNumber)){
                    customerFound = true;
                    break;
                }
                pos++;
            }

            if(!customerFound){
                System.out.println("Customer not found");
                return;
            }

            customerList.get(sourceBranch).remove(pos);
            customer.accountNumber = Long.toString((long)(destinationBranch*(long)1000000000) + (long)counter[destinationBranch]);
            counter[destinationBranch]++;
            if(destinationBranch==0){
                while(customer.accountNumber.length()<10){
                    customer.accountNumber = "0" + customer.accountNumber;
                }
            }
            customerList.get(destinationBranch).addLast(customer);
            return;
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
            System.out.println("Error!! Please Enter an argument which should be number of threads");
            return;
        }

        //error of more than one arguments given
        if(args.length>1){
            System.out.println("Error!! Please Enter only one argument which should be number of threads");
            return;
        }

        int noOfTransPerUpdater;

        try{
            noOfTransPerUpdater = Integer.parseInt(args[0]);
        }catch(Exception e){
            System.out.println("Error!! The argument should be a number");
            return;
        }

        long startTime = System.currentTimeMillis();

        makeCustomerList();

        System.out.println(customerList.get(0).size());
        System.out.println(customerList.get(1).size());

        Thread threads[] = new Thread[1];

        //this for loop will start the execution of all the threads to initialize matrices A and B 
        for(int i=0;i<1;i++){

            TransferCustomer depositCash = new TransferCustomer("0000000000",1);

            //assign element of thread array to the required thread which will execute the run function in InitializeThread class
            threads[i] = new Thread(depositCash);

            //it will start the execution of thread which will run the run method of InitializeThread class
            threads[i].start();
        }

        for(int i=0;i<1;i++){
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println(customerList.get(0).size());
        System.out.println(customerList.get(1).get(10000).getAccountNumber());

        long endTime = System.currentTimeMillis();
        long timeElapsed = endTime - startTime;
        System.out.println("Execution time in milliseconds: " + timeElapsed);

        //start the time
        // long startTime = System.currentTimeMillis();

        //BankData bankDate = new BankData();

        // //made an array of threads whose length is equal to number of threads required
        // Thread threads[] = new Thread[noOfThreads];

        // //this for loop will start the execution of all the threads to initialize matrices A and B 
        // for(int i=0;i<noOfThreads;i++){

        //     //making an object of class InitializeThread
        //     InitializeThread initializeThread = new InitializeThread(i,noOfThreads);

        //     //assign element of thread array to the required thread which will execute the run function in InitializeThread class
        //     threads[i] = new Thread(initializeThread);

        //     //it will start the execution of thread which will run the run method of InitializeThread class
        //     threads[i].start();
        // }

        // //we will wait for all the threads to complete their execution and die
        // for(int i=0;i<noOfThreads;i++){
        //     try {
        //         threads[i].join();
        //     } catch (InterruptedException e) {
        //         e.printStackTrace();
        //     }
        // }

        // //this for loop will start the execution of all the threads to compute matric C(A X B)
        // for(int i=0;i<noOfThreads;i++){ 

        //     //making an object of class MultiplyThread
        //     MultiplyThread multiplyThread = new MultiplyThread(i,noOfThreads);

        //     //assign element of thread array to the required thread which will execute the run function in MultiplyThread class
        //     threads[i] = new Thread(multiplyThread);

        //     //it will start the execution of thread which will run the run method of MultiplyThread class
        //     threads[i].start();
        // }

        // //we will wait for all the threads to complete their execution and die
        // for(int i=0;i<noOfThreads;i++){
        //     try {
        //         threads[i].join();
        //     } catch (InterruptedException e) {
        //         e.printStackTrace();
        //     }
        // }

        // //time elapsed for computing the answer using given number of threads
        // long endTime = System.currentTimeMillis();

        // long timeElapsed = endTime - startTime;

        // //print the resultant matrix C(A X B)
        // for(int i=0;i<rowSize;i++){
        //     for(int j=0;j<rowSize;j++){
        //         System.out.print(C[i][j]);
        //         System.out.print(' ');
        //     }
        //     System.out.print('\n');
        // }

        // System.out.println("Number of threads: " + args[0]);

        // System.out.println("Execution time in milliseconds: " + timeElapsed);
    }
}
