package MonteCarloPI;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.Random;

public class MonteCarloPi {

    static final long NUM_POINTS = 50_000_000L;
    static final int NUM_THREADS = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // Without Threads
        System.out.println("Single threaded calculation started: ");
        long startTime = System.nanoTime();
        double piWithoutThreads = estimatePiWithoutThreads(NUM_POINTS);
        long endTime = System.nanoTime();
        System.out.println("Monte Carlo Pi Approximation (single thread): " + piWithoutThreads);
        System.out.println("Time taken (single thread): " + (endTime - startTime) / 1_000_000 + " ms");

        // With Threads
        System.out.printf("Multi-threaded calculation started: (your device has %d logical threads)\n", NUM_THREADS);
        startTime = System.nanoTime();
        double piWithThreads = estimatePiWithThreads(NUM_POINTS, NUM_THREADS);
        endTime = System.nanoTime();
        System.out.println("Monte Carlo Pi Approximation (Multi-threaded): " + piWithThreads);
        System.out.println("Time taken (Multi-threaded): " + (endTime - startTime) / 1_000_000 + " ms");
    }

    // Monte Carlo Pi Approximation without threads
    public static double estimatePiWithoutThreads(long numPoints) {
        long insideCircle = 0;
        Random random = new Random();

        for (long i = 0; i < numPoints; i++) {
            double x = random.nextDouble();
            double y = random.nextDouble();
            if (x * x + y * y <= 1) {
                insideCircle++;
            }
        }

        return 4.0 * insideCircle / numPoints;
    }

    // Monte Carlo Pi Approximation with threads
    public static double estimatePiWithThreads(long numPoints, int numThreads) throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        long pointsPerThread = numPoints / numThreads;
        List<Future<Long>> futures = new ArrayList<>();

        for (int i = 0; i < numThreads; i++) {
            futures.add(executor.submit(() -> {
                long insideCircle = 0;
                Random random = new Random();
                for (long j = 0; j < pointsPerThread; j++) {
                    double x = random.nextDouble();
                    double y = random.nextDouble();
                    if (x * x + y * y <= 1) {
                        insideCircle++;
                    }
                }
                return insideCircle;
            }));
        }

        long totalInsideCircle = 0;
        for (Future<Long> future : futures) {
            totalInsideCircle += future.get();
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        return 4.0 * totalInsideCircle / numPoints;
    }
}
