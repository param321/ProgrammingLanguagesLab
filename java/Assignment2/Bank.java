import java.util.*;
import java.io.*;

class Bank{ 

    public class Customer{
        private String accountNumber;
        private long balance;

        public Customer(String accountNumber,long balance){
            this.accountNumber = accountNumber;
            this.balance = balance;
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

        Random random = new Random(); 

        String accountNumber="";
        long balance;

        for(int i=0;i<10;i++){
            //accountNumber = Character.toString((char)(i+'0'));
            LinkedList<Customer> branchCustomers= new LinkedList<Customer>();
            for(int j=0;j<10000;j++){
                accountNumber = Long.toString((long)(i*(long)1000000000) + (long)j);

                if(i==0){
                    while(accountNumber.length()<10){
                        accountNumber = "0" +accountNumber ;
                    }
                }

                balance = random.nextInt(1000000000);
                Customer customer = new Customer(accountNumber, balance);
                branchCustomers.add(customer);
                //System.out.println(accountNumber);
            }
            customerList.put(i,branchCustomers);
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
