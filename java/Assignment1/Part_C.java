//How To Run
//In the terminal to compile java code enter command : javac Part_C.java
//then to run the code enter command : java Part_C <Number_of_threads>
//Number_of_threads should be a number between 4 and 16 both inclusive

//Output
//First it will print whole C(A X B) matrix
//then it will print no of threads used
//then it will print Execution time to initialize and compute multiplication in miliseconds
 
import java.util.Random;

class Part_C {

    //contains the number of rows of our 2-D square matrix
    static int rowSize = 1000;

    //total number of elements in our matrix
    static int totalNoOfCells = rowSize*rowSize;

    //Our 2-D sqare matrix A
    double A[][] = new double[rowSize][rowSize];

    //Our 2-D sqare matrix B
    double B[][] = new double[rowSize][rowSize];

    //Our 2-D square matrix C which will be equal to A x B
    double C[][] = new double[rowSize][rowSize];

    //this is a class that will implement Runnable interface to have thread functionality
    //will be useful in initializing matrices A and B
    public class InitializeThread implements Runnable{
        int threadId;
        int noOfThreads;

        //constructor which will help us to assign threadId and total number of threads
        InitializeThread(int threadId,int noOfThreads){
            this.threadId=threadId;
            this.noOfThreads=noOfThreads;
        }

        //contains all the logic which will run on the thread
        public void run(){
            initialize(threadId,noOfThreads);
        }
    }

    //this function will be running on each thread 
    //this function will initialize matrices A and B with random double values between 0 and 10
    public void initialize(int threadId,int noOfThreads){
        
        //this is our random number generator
        Random random = new Random();

        //start index from which the this thread will start operation
        int start = threadId * (totalNoOfCells/noOfThreads);

        //end index till which the this thread will end operation
        int end;

        //if thread is last thread by threadId
        if(threadId == (noOfThreads-1)){
            //if thread is last thread it will have extra elements which are left after distribution among the threads
            end = totalNoOfCells;
        }else{
            end = ((threadId+1) * (totalNoOfCells/noOfThreads)) ;
        }

        //perform initialization of elements of array given to the thread 
        for(int i=start;i<end;i++){

            // we are given total rowSize*rowSize elements in 2D array index from 0....((rowSize*rowSize)-1)
            // index/rowSize will give us row index and index%rowSize will give us column index
            A[i/rowSize][i%rowSize] = random.nextDouble()*10;

            B[i/rowSize][i%rowSize] = random.nextDouble()*10;

            C[i/rowSize][i%rowSize] = 0;
        }
    }

    //this is a class that will implement Runnable interface to have thread functionality
    //will be useful in multiplying matrix A and B and store it in C
    public class MultiplyThread implements Runnable{
        int threadId;
        int noOfThreads;

        //constructor which will help us to assign threadId and total number of threads
        MultiplyThread(int threadId,int noOfThreads){
            this.threadId=threadId;
            this.noOfThreads=noOfThreads;
        }

        //contains all the logic which will run on the thread
        public void run(){
            multiply(threadId,noOfThreads);
        }
    }

    //this function will be running on each thread 
    //this function will multiplying matrix A and B and store it in C
    public void multiply(int threadId,int noOfThreads){

        //start index from which the this thread will start operation
        int start = threadId * (totalNoOfCells/noOfThreads);

        //end index till which the this thread will end operation
        int end;

        //if thread is last thread by threadId
        if(threadId == (noOfThreads-1)){
            //if thread is last thread it will have extra elements which are left after distribution among the threads
            end = totalNoOfCells;
        }else{
            end = ((threadId+1) * (totalNoOfCells/noOfThreads)) ;
        }

        //perform multiplication of elements of array given to the thread 
        for(int i=start;i<end;i++){

            //ans here will store value of element of array C
            double ans=0;

            //will compute element of array C i.e multiplying elements of A and B to obtain element of C
            for(int j=0;j<rowSize;j++){
                // we are given total rowSize*rowSize elements in 2D array index from 0....((rowSize*rowSize)-1)
                // index/rowSize will give us row index and index%rowSize will give us column index
                ans += (A[i/rowSize][j]*B[j][i%rowSize]);
            }
            //assign ans variable to element of array C
            C[i/rowSize][i%rowSize]=ans;
        }
    }

    //our code will first run this method
    public static void main(String args[]){
        try {
            Part_C part_C = new Part_C();
            part_C.run(args);
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

        //variable will store number of threads 
        int noOfThreads;

        //will check that the argument given is a integer or not and assign it to the variable noOfThreads
        try{
            noOfThreads = Integer.parseInt(args[0]);
        }catch(Exception e){
            System.out.println("Error!! The argument should be a number");
            return;
        }

        //will check that number of threads are in range [4,16]
        if(noOfThreads<4||noOfThreads>16){
            System.out.println("Error!! Numbers of threads should be between 4 and 16");
            return;
        }

        //start the time
        long startTime = System.currentTimeMillis();

        //made an array of threads whose length is equal to number of threads required
        Thread threads[] = new Thread[noOfThreads];

        //this for loop will start the execution of all the threads to initialize matrices A and B 
        for(int i=0;i<noOfThreads;i++){

            //making an object of class InitializeThread
            InitializeThread initializeThread = new InitializeThread(i,noOfThreads);

            //assign element of thread array to the required thread which will execute the run function in InitializeThread class
            threads[i] = new Thread(initializeThread);

            //it will start the execution of thread which will run the run method of InitializeThread class
            threads[i].start();
        }

        //we will wait for all the threads to complete their execution and die
        for(int i=0;i<noOfThreads;i++){
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //this for loop will start the execution of all the threads to compute matric C(A X B)
        for(int i=0;i<noOfThreads;i++){ 

            //making an object of class MultiplyThread
            MultiplyThread multiplyThread = new MultiplyThread(i,noOfThreads);

            //assign element of thread array to the required thread which will execute the run function in MultiplyThread class
            threads[i] = new Thread(multiplyThread);

            //it will start the execution of thread which will run the run method of MultiplyThread class
            threads[i].start();
        }

        //we will wait for all the threads to complete their execution and die
        for(int i=0;i<noOfThreads;i++){
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //time elapsed for computing the answer using given number of threads
        long endTime = System.currentTimeMillis();

        long timeElapsed = endTime - startTime;

        //print the resultant matrix C(A X B)
        for(int i=0;i<rowSize;i++){
            for(int j=0;j<rowSize;j++){
                System.out.print(C[i][j]);
                System.out.print(' ');
            }
            System.out.print('\n');
        }

        System.out.println("Number of threads: " + args[0]);

        System.out.println("Execution time in milliseconds: " + timeElapsed);
    }
}
