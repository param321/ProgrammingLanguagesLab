//How To Run
//In the terminal to compile java code enter command : javac Part_B.java
//then to run the code enter command : java Part_B <Number_of_threads>
//Number_of_threads should be a number between 4 and 16 both inclusive

//Output
//First it will print Execution time to calculate the approximate value of given integration in miliseconds
//then it will print the approximate value of given integration

class Part_B {

    //this variable contains total number of points
    static int noOfPoints = 10000001;

    //this variable is equal to delta which is eual to (1-(-1))/(noOfPoints-1)
    static double delta = (double)2 / (noOfPoints-1);
    
    //this is a class that will implement Runnable interface to have thread functionality
    public class TaskThread extends Thread{
        int threadId;
        int noOfThreads;

        //store partial sum of functions assigned to each thread
        double partialSum = 0;

        //constructor which will help us to assign threadId and total number of threads
        TaskThread(int threadId,int noOfThreads){
            this.threadId=threadId;
            this.noOfThreads=noOfThreads;
        }

        //contains all the logic which will run on the thread
        public void run(){
            //will compute the partialSum of function assigned to each thread 
            compute();
        }

        void compute(){
            //start variable for i where i is lower bound of interval 
            int start = threadId*(noOfPoints/noOfThreads);

            //end variable for i where i is lower bound of interval 
            int end;

            //if thread is last thread by threadId
            if(threadId==(noOfThreads-1)){
                //if thread is last thread it will have extra points which are left after distribution among the threads
                end=noOfPoints;
            }else{
                end = ((threadId+1)*(noOfPoints/noOfThreads));
            }

            for(int i=start;i<end;i++){

                //if it is the first or last point we will add 1*function value as seen from the series
                if(i==0||i==(noOfPoints-1)){
                    partialSum += (1*function((i*delta)-1));
                }else{
                    //if it is even term in the series we will add 4*function value as seen from the series
                    if(i%2==1){
                        partialSum += (4*function((i*delta)-1));
                    }else{
                    //if it is odd term in the series we will add 4*function value as seen from the series
                        partialSum += (2*function((i*delta)-1));
                    }
                }
            }
        }
    }

    //will compute the value of fucntion whose integral we have to find at a specific point x
    public double function(double x){
        double val = (1/(Math.sqrt(2*(Math.PI))))*Math.exp(-(x*x)/2);
        return val;
    }

    //our code will first run this method
    public static void main(String args[]){
        try {
            Part_B part_B = new Part_B();
            part_B.run(args);
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

        //made an array of TaskThread object type whose length is equal to number of threads required to calculate approximate value of integration
        TaskThread threads[] = new TaskThread[noOfThreads];

        //this for loop will start the execution of all the threads
        for(int i=0;i<noOfThreads;i++){

            //making an object of class TaskThread and passing thread specific value to constructor
            TaskThread taskThread = new TaskThread(i,noOfThreads);

            //assign element of ThreadTask array to the specific object
            threads[i] = taskThread;

            //it will start the execution of thread which will run the run method of TaskThread class
            threads[i].start();
        }

        //this variable will conatain the sum of contribution of function at all the points
        double sum_of_function_value = 0;
        
        //we will wait for all the threads to complete their execution and die
        for(int i=0;i<noOfThreads;i++){
            try {
                //will wait for the i(th) thread to die
                threads[i].join();

                //will add the sum contribution of each thread
                sum_of_function_value += (threads[i].partialSum);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long endTime = System.currentTimeMillis();

        //time elapsed for computing the answer using given number of threads
        long timeElapsed = endTime - startTime;
        System.out.println("Execution time in milliseconds: " + timeElapsed);

        //computing value of integration by formula value of integration = ((delta)/3)*(sum)
        double valueOfIntegration = (sum_of_function_value*delta)/3;

        System.out.print("The approximate value of integration using "+args[0]+" threads is: ");
        System.out.println(valueOfIntegration);
    }
}
