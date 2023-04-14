import java.util.Random;
import java.util.Collections;
import java.util.concurrent.*;

public class Problem2 {

    static class Sensor implements Runnable {
        private final int HOURS = 24;
        int id;

        public Sensor(int num)
        {
            this.id = num;
        }

        public void run() {
            for (int i = 0; i < HOURS; i++) {

                // get reading every minute
                for (int j = 0; j < 60; j++) {
                    int nextReading = r.nextInt(MIN, MAX + 1);

                    if (!max_temps.contains(nextReading))
                    {
                        max_temps.offer(nextReading);

                        if (max_temps.size() > 5) {
                            max_temps.poll();
                        }
                    }
                    else if (max_temps.size() > 5) {
                        max_temps.poll();
                    }

                    if (!min_temps.contains(nextReading))
                    {
                        min_temps.offer(nextReading);

                        if (min_temps.size() > 5)
                        {
                            min_temps.poll();
                        }
                    }
                    else if (min_temps.size() > 5)
                    {
                        min_temps.poll();
                    }
                        


                    list.add(j, nextReading);
                }

                // reset for formatting
                if (id == 0) {
                    System.out.println("[" + (i + 1) + "] report:");
                    doReport();
                }
            }
        }

        public void doReport() {
            System.out.println("minimum 5 temperatures: ");
            while (min_temps.size() > 0) {
                System.out.print(min_temps.poll() + " ");
            }

            System.out.println();

            System.out.println("max 5 temperatures: ");
            while (max_temps.size() > 0) {
                System.out.print(max_temps.poll() + " ");
            }

            System.out.println();
            
            list.solve();
            list = new ParallelList_Problem2();
        }
    }

    public static ExecutorService service;
    public static PriorityBlockingQueue<Integer> max_temps;
    public static PriorityBlockingQueue<Integer> min_temps;
    public static ParallelList_Problem2 list;
    public static Random r = new Random();
    public static final int MIN = -100;
    public static final int MAX = 70;

    public static void main(String[] args) throws Exception {
        final int N = 8;
        long start = System.currentTimeMillis();
        max_temps = new PriorityBlockingQueue<Integer>(5);
        min_temps = new PriorityBlockingQueue<Integer>(5, Collections.reverseOrder());
        list = new ParallelList_Problem2();
        service = Executors.newFixedThreadPool(N);

        for (int i = 0; i < N; i++) {
            service.submit(new Sensor(i));
        }

        service.shutdown();
        service.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
        System.out.println("[" + ((System.currentTimeMillis() - start)) + "ms]");
    }
}