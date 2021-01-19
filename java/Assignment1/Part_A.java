import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

class Part_A {
    static int noOfPoints = 1000000;

    AtomicInteger noOfPointsInsideCircle = new AtomicInteger(0);

    public Boolean check(double x,double y){
        if((x*x)+(y*y)<=1){
            return true;
        }else {
            return false;
        }
    }

    public class TaskThread implements Runnable{
        int threadId;
        int noOfThreads;

        TaskThread(int threadId,int noOfThreads){
            this.threadId=threadId;
            this.noOfThreads=noOfThreads;
        }
        public void run(){
            computeAndCheck(threadId,noOfThreads);
        }
    }

    public void computeAndCheck(int threadId,int noOfThreads){
        Random xGen = new Random();
        Random yGen = new Random();
        if(threadId!=(noOfThreads-1)){
            for(int i=0;i<(noOfPoints/noOfThreads);i++){
                double x = (xGen.nextDouble()*2)-1;
                double y = (yGen.nextDouble()*2)-1;
                if(check(x,y)){
                    noOfPointsInsideCircle.getAndIncrement();
                }
            }
        }else{
            for(int i=0;i<(noOfPoints/noOfThreads)+(noOfPoints%noOfThreads);i++){
                double x = (xGen.nextDouble()*2)-1;
                double y = (yGen.nextDouble()*2)-1;
                if(check(x,y)){
                    noOfPointsInsideCircle.getAndIncrement();
                }
            }
        }
        System.out.println(threadId);
    }

    public static void main(String args[]){
        try {
            Part_A part_A = new Part_A();
            part_A.run(args);
        } catch (Exception e) {
            System.out.println(e);
        }
        
    }

    public void run(String args[]){
        if(args.length==0){
            System.out.println("Error!! Please Enter an argument which should be number of threads");
            return;
        }

        if(args.length>1){
            System.out.println("Error!! Please Enter only one argument which should be number of threads");
            return;
        }

        int noOfThreads;
        try{
            noOfThreads = Integer.parseInt(args[0]);
        }catch(Exception e){
            System.out.println("Error!! The argument should be a number");
            return;
        }

        // if(noOfThreads<4||noOfThreads>16){
        //     System.out.println("Error!! Numbers of threads should be between 4 and 16");
        //     return;
        // }

        long startTime = System.currentTimeMillis();

        System.out.println(noOfThreads);
        Thread threads[] = new Thread[noOfThreads];
        for(int i=0;i<noOfThreads;i++){
            TaskThread taskThread = new TaskThread(i,noOfThreads);
            threads[i] = new Thread(taskThread);
            threads[i].start();
        }
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

        double pi = 4 * ((double)noOfPointsInsideCircle.get()/noOfPoints);
        System.out.println(pi);
    }
}
