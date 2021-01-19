import java.util.Random;

class Part_C {
    static int rowSize = 1000;

    static int totalNoOfCells = rowSize*rowSize;

    double A[][] = new double[rowSize][rowSize];
    double B[][] = new double[rowSize][rowSize];
    double C[][] = new double[rowSize][rowSize];

    public class InitializeThread implements Runnable{
        int threadId;
        int noOfThreads;

        InitializeThread(int threadId,int noOfThreads){
            this.threadId=threadId;
            this.noOfThreads=noOfThreads;
        }

        public void run(){
            initialize(threadId,noOfThreads);
        }
    }

    public void initialize(int threadId,int noOfThreads){
        Random random = new Random();
        int start = threadId * (totalNoOfCells/noOfThreads);
        int end;
        if(threadId == (noOfThreads-1)){
            end = totalNoOfCells;
        }else{
            end = ((threadId+1) * (totalNoOfCells/noOfThreads)) ;
        }
        for(int i=start;i<end;i++){
            A[i/rowSize][i%rowSize] = random.nextDouble()*10;
            B[i/rowSize][i%rowSize] = random.nextDouble()*10;
        }
        System.out.println(threadId);
    }

    public class MultiplyThread implements Runnable{
        int threadId;
        int noOfThreads;

        MultiplyThread(int threadId,int noOfThreads){
            this.threadId=threadId;
            this.noOfThreads=noOfThreads;
        }

        public void run(){
            multiply(threadId,noOfThreads);
        }
    }

    public void multiply(int threadId,int noOfThreads){
        int start = threadId * (totalNoOfCells/noOfThreads);
        int end;
        if(threadId == (noOfThreads-1)){
            end = totalNoOfCells;
        }else{
            end = ((threadId+1) * (totalNoOfCells/noOfThreads)) ;
        }
        for(int i=start;i<end;i++){
            double ans=0;
            for(int j=0;j<rowSize;j++){
                ans+=A[i/rowSize][j]*B[j][i%rowSize];
            }
            C[i/rowSize][i%rowSize]=ans;
        }
        System.out.println(threadId);
    }

    public static void main(String args[]){
        try {
            Part_C part_C = new Part_C();
            part_C.run(args);
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

        if(noOfThreads<4||noOfThreads>16){
            System.out.println("Error!! Numbers of threads should be between 4 and 16");
            return;
        }

        long startTime = System.currentTimeMillis();

        System.out.println(noOfThreads);
        Thread threads[] = new Thread[noOfThreads];
        for(int i=0;i<noOfThreads;i++){
            InitializeThread initializeThread = new InitializeThread(i,noOfThreads);
            threads[i] = new Thread(initializeThread);
            threads[i].start();
        }

        for(int i=0;i<noOfThreads;i++){
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for(int i=0;i<noOfThreads;i++){
            MultiplyThread multiplyThread = new MultiplyThread(i,noOfThreads);
            threads[i] = new Thread(multiplyThread);
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

        for(int i=0;i<rowSize;i++){
            for(int j=0;j<rowSize;j++){
                System.out.print(C[i][j]);
                System.out.print(' ');
            }
            System.out.print('\n');
        }

        System.out.println("Execution time in milliseconds: " + timeElapsed);
    }
}
