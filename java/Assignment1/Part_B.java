class Part_B {
    static int noOfPoints = 10000001;

    static double delta = (double)2 / (noOfPoints-1);

    double valueOfIntegration = 0;

    public class TaskThread implements Runnable{
        int threadId;
        int noOfThreads;

        TaskThread(int threadId,int noOfThreads){
            this.threadId=threadId;
            this.noOfThreads=noOfThreads;
        }

        public void run(){
            compute(threadId,noOfThreads);
        }
    }

    public double function(double x){
        double val = (1/(Math.sqrt(2*(Math.PI))))*Math.exp(-(x*x)/2);
        return val;
    }

    public void compute(int threadId,int noOfThreads){
        int start = threadId*(noOfPoints/noOfThreads);
        int end;
        if(threadId==(noOfThreads-1)){
            end=noOfPoints;
        }else{
            end = ((threadId+1)*(noOfPoints/noOfThreads));
        }
        for(int i=start;i<end;i++){
            if(i==0||i==(noOfPoints-1)){
                synchronized (this){
                    valueOfIntegration+=(1*function((i*delta)-1));
                }
            }else{
                if(i%2==1){
                    synchronized (this){
                        valueOfIntegration+=(4*function((i*delta)-1));
                    }
                }else{
                    synchronized (this){
                        valueOfIntegration+=(2*function((i*delta)-1));
                    }
                }
            }
        }
        System.out.println(threadId);
    }

    public static void main(String args[]){
        try {
            Part_B part_B = new Part_B();
            part_B.run(args);
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

    //    if(noOfThreads<4||noOfThreads>16){
    //        System.out.println("Error!! Numbers of threads should be between 4 and 16");
    //        return;
    //    }

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
        
        System.out.println(delta*valueOfIntegration/3);
    }
}
