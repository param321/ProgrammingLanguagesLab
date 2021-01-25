import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

class Part_A {

    //total number of points in the square
    static int noOfPoints = 1000000;

    //variable which will have value equal to number of points inside circle till the end of the program
    AtomicInteger noOfPointsInsideCircle = new AtomicInteger(0);

    //function that checks that the coordinates (x,y) are inside or on boundary of circle of radius 1 unit or not
    //this function return bool value , true if (x,y) is inside or on boundary of circle of radius 1 unit , otherwise false
    public Boolean check(double x,double y){
        if((x*x)+(y*y)<=1){
            return true;
        }else {
            return false;
        }
    }

    //this is a class that will implement Runnable interface to have thread functionality
    public class TaskThread implements Runnable{
        int threadId;
        int noOfThreads;

        //constructor which will help us to assign threadId and total number of threads
        TaskThread(int threadId,int noOfThreads){
            this.threadId=threadId;
            this.noOfThreads=noOfThreads;
        }

        //contains all the logic which will run on the thread
        public void run(){
            computeAndCheck(threadId,noOfThreads);
        }
    }

    //this function will be running on each thread 
    //this function will generate random points (x,y) and check that if they lie in or on the cirle or not
    //will increment the variable noOfPointsInsideCircle if (x,y) lie in or on the circle
    public void computeAndCheck(int threadId,int noOfThreads){

        //this is our random number generator
        Random randGen = new Random();

        //if this is not our last thread by threadId
        if(threadId!=(noOfThreads-1)){
            //we have equally divided all the points among all threads
            for(int i=0;i<(noOfPoints/noOfThreads);i++){

                //generate x coordinate which will lie in range [-1,1]
                double x = (randGen.nextDouble()*2)-1;

                //generate y coordinate which will lie in range [-1,1]
                double y = (randGen.nextDouble()*2)-1;

                //if (x,y) lie in or on the circle we will increment  noOfPointsInsideCircle variable
                if(check(x,y)){
                    noOfPointsInsideCircle.getAndIncrement();
                }
            }
        }else{
            //if this is our last thread by threadId

            //this thread will contain extra (noOfPoints)%(noOfThreads) points as compared to other threads
            for(int i=0;i<(noOfPoints/noOfThreads)+(noOfPoints%noOfThreads);i++){

                //generate x coordinate which will lie in range [-1,1]
                double x = (randGen.nextDouble()*2)-1;

                //generate y coordinate which will lie in range [-1,1]
                double y = (randGen.nextDouble()*2)-1;

                //if (x,y) lie in or on the circle we will increment  noOfPointsInsideCircle variable
                if(check(x,y)){
                    noOfPointsInsideCircle.getAndIncrement();
                }
            }
        }
    }

    //our code will first run this method
    public static void main(String args[]){
        try {

            Part_A part_A = new Part_A();

            //we will run the run method
            part_A.run(args);

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

        long startTime = System.currentTimeMillis();

        //made an array of threads whose length is equal to number of threads required to estimate the value of pi
        Thread threads[] = new Thread[noOfThreads];

        //this for loop will start the execution of all the threads
        for(int i=0;i<noOfThreads;i++){

            //making an object of class TaskThread
            TaskThread taskThread = new TaskThread(i,noOfThreads);

            //assign element of thread array to the required thread which will execute the run function in TaskThread class
            threads[i] = new Thread(taskThread);

            //it will start the execution of thread which will run the run method of TaskThread class
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

        long endTime = System.currentTimeMillis();

        long timeElapsed = endTime - startTime;
        System.out.println("Execution time in milliseconds: " + timeElapsed);

        //estimate the value of pi from the number of points inside circle
        double pi = 4 * ((double)noOfPointsInsideCircle.get()/noOfPoints);

        //we will print our answer
        System.out.println("The estimated value of pi using "+ args[0] +" is: "+pi);
    }
}
