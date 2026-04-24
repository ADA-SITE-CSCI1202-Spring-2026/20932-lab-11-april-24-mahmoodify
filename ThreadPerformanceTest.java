public class ThreadPerformanceTest {
    static class MathTask implements Runnable {
        private final int j;
        MathTask(int j) {
            this.j = j;
        }
        @Override
        public void run() {
            long sum = 0;
            for(int i =0; i <  10000000; i++){
                sum +=(long)i*i*i + (long)i*j;
            }

        }
    }
    public static void main(String[] agrs) throws InterruptedException {
        int corecount = Runtime.getRuntime().availableProcessors();
        System.out.println("Number of cores: " + corecount);
        runSingleThreaded();
        runMultiThreaded(corecount);
    }
    private static void runSingleThreaded() throws InterruptedException {
        long singleStart = System.currentTimeMillis();
        Thread t = new Thread(new MathTask(0));
        t.start();
        t.join();
        long singleEnd = System.currentTimeMillis();
        System.out.println("Single-threaded execution time: " + (singleEnd - singleStart) + " ms");

    }
    private static void runMultiThreaded(int threadCount) throws InterruptedException {
        long multiStart = System.currentTimeMillis();
        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(new MathTask(i));
            threads[i].start();
        }
        for (Thread t : threads) {
            t.join();
        }
        long multiEnd = System.currentTimeMillis();
        System.out.println("Multi-threaded execution time with " + threadCount + " threads: " + (multiEnd - multiStart) + " ms");
    }
}
