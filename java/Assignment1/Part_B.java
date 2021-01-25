class Part_B {

    //this variable contains total number of points
    static int noOfPoints = 10000001;

    //this variable is equal to delta which is eual to (1-(-1))/noOfPoints
    static double delta = (double)2 / (noOfPoints-1);

    //this variable will conatain the estimated value of our integration
    double ans = 0;
    
    //this is a class that will implement Runnable interface to have thread functionality
    public class TaskThread extends Thread{
        int threadId;
        int noOfThreads;

        double valueOfIntegration = 0;

        //constructor which will help us to assign threadId and total number of threads
        TaskThread(int threadId,int noOfThreads){
            this.threadId=threadId;
            this.noOfThreads=noOfThreads;
        }

        //contains all the logic which will run on the thread
        public void run(){
            compute();
        }

        void compute(){
            int start = threadId*(noOfPoints/noOfThreads);
            int end;
            if(threadId==(noOfThreads-1)){
                end=noOfPoints;
            }else{
                end = ((threadId+1)*(noOfPoints/noOfThreads));
            }
            for(int i=start;i<end;i++){
                if(i==0||i==(noOfPoints-1)){
                    valueOfIntegration+=(1*function((i*delta)-1));
                }else{
                    if(i%2==1){
                        valueOfIntegration+=(4*function((i*delta)-1));
                    }else{
                        valueOfIntegration+=(2*function((i*delta)-1));
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

        long startTime = System.currentTimeMillis();

        //made an array of threads whose length is equal to number of threads required to estimate the value of pi
        TaskThread threads[] = new TaskThread[noOfThreads];

        //this for loop will start the execution of all the threads
        for(int i=0;i<noOfThreads;i++){

            //making an object of class TaskThread
            TaskThread taskThread = new TaskThread(i,noOfThreads);

            //assign element of thread array to the required thread which will execute the run function in TaskThread class
            threads[i] = taskThread;

            //it will start the execution of thread which will run the run method of TaskThread class
            threads[i].start();
        }
        
        //we will wait for all the threads to complete their execution and die
        for(int i=0;i<noOfThreads;i++){
            try {
                threads[i].join();
                ans+=(threads[i].valueOfIntegration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long endTime = System.currentTimeMillis();

        long timeElapsed = endTime - startTime;
        System.out.println("Execution time in milliseconds: " + timeElapsed);

        System.out.println(ans*delta/3);
        
        //System.out.println("Estimated value of integration is: "+ (delta*valueOfIntegration/3));
    }
}
