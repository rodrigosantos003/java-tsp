import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class Advanced {
    static Results[] results;
    static Random rand = new Random();

    static class AdvancedThread extends Thread implements TSPThread {
        private Individual[] population;
        private final int populationSize;
        private final float mutationChance;
        private final int[][] distances;
        private int bestDistance;
        private final long startTime;
        private long endTime;
        private int iterations;

        private boolean isRunning;
        private final int threadIndex;
        private volatile boolean paused = false;
        private final Object pauseLock = new Object();

        public AdvancedThread(int populationSize, float mutationChance, int[][] distances, int threadIndex) {
            this.populationSize = populationSize;
            this.population = new Individual[this.populationSize];
            this.mutationChance = mutationChance;
            this.distances = distances;
            this.bestDistance = 0;
            this.endTime = 0;
            this.iterations = 0;
            this.isRunning = true;
            this.threadIndex = threadIndex;

            this.startTime = System.nanoTime();

            // Initialize the population
            for (int i = 0; i < populationSize; i++) {
                population[i] = new Individual(Utilities.generateRandomPath(distances.length, rand), distances);
            }
        }

        public Individual[] getPopulation() {
            return population;
        }

        public void setPopulation(Individual[] population) {
            this.population = population;
        }

        public int getBestDistance() {
            return bestDistance;
        }

        public int[] getBestPath() {
            return population[0].getPath();
        }

        public long getStartTime() {
            return startTime;
        }

        public long getEndTime() {
            return endTime;
        }

        public int getIterations() {
            return iterations;
        }

        public void setRunning(boolean running) {
            isRunning = running;
        }

        public int getThreadIndex() {
            return threadIndex;
        }

        public void pauseThread() {
            paused = true;
        }

        public void resumeThread() {
            synchronized (pauseLock) {
                paused = false;
                pauseLock.notifyAll();
            }
        }

        @Override
        public void run() {
            bestDistance = population[0].getDistance();
            endTime = System.nanoTime();
            int localIterations = 0;

            int idx1 = populationSize - 2;
            int idx2 = populationSize - 1;

            while (isRunning) {
                synchronized (pauseLock) {
                    while (paused) {
                        try {
                            pauseLock.wait();
                        } catch (InterruptedException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }

                localIterations++;

                // Sort the population
                Arrays.sort(population, Comparator.comparing(Individual::getDistance));

                // Apply PMX crossover
                int[][] pmxResult = PMXCrossover.pmxCrossover(population[0].getPath(), population[1].getPath(), rand);
                population[idx1] = new Individual(pmxResult[0], distances);
                population[idx2] = new Individual(pmxResult[1], distances);

                // Element mutation
                float mutationValue = rand.nextFloat(1);

                if (mutationValue < mutationChance) {
                    population[idx1] = new Individual(Utilities.elementRandomSwitch(population[idx1].getPath(), rand),
                            distances);
                    population[idx2] = new Individual(Utilities.elementRandomSwitch(population[idx2].getPath(), rand),
                            distances);
                }

                int currentBestDistance = Utilities.calculateDistance(population[0].getPath(), distances);

                if (currentBestDistance < bestDistance) {
                    bestDistance = currentBestDistance;
                    endTime = System.nanoTime();
                    iterations = localIterations;
                }
            }

            Utilities.updateValues(results, this);
        }
    }

    /**
     * Merge populations from all threads
     *
     * @param mergeAmount Merge amount
     * @param mergeTime   Merge time
     * @param threads     Array of threads
     * @param popSize     Population size
     */
    static void executeMerge(int mergeAmount, float mergeTime, AdvancedThread[] threads, int popSize) {
        if (mergeAmount == 0)
            return;

        CompletableFuture.delayedExecutor((long) mergeTime, TimeUnit.SECONDS).execute(() -> {
            Individual[] populations = new Individual[threads.length * popSize];
            int index = 0;

            // Pause all threads and copy their populations
            for (AdvancedThread thread : threads) {
                thread.pauseThread();
                System.arraycopy(thread.getPopulation(), 0, populations, index, popSize);
                index += popSize;
            }

            Arrays.sort(populations, Comparator.comparing(Individual::getDistance));

            // Copy the best individuals to the new population
            Individual[] newPopulations = Arrays.copyOfRange(populations, 0, popSize * threads.length);

            // Update the population of each thread and resume them
            for (AdvancedThread thread : threads) {
                thread.setPopulation(Arrays.copyOf(newPopulations, popSize));
                thread.resumeThread();
            }

            if (mergeAmount > 0)
                executeMerge(mergeAmount - 1, mergeTime, threads, popSize);
        });
    }

    public static void main(String[] args) {
        if (args.length != 6) {
            System.out.println("ERRO: Número de argumentos inválido!");
            System.out.println("Formato correto dos argumentos: <file_name> <threads> <time> <population> <mutation> " +
                    "<merge_time_percentage>");

            System.exit(-1);
        }

        // Get the arguments
        String fileName = args[0];
        int nThreads = Integer.parseInt(args[1]);
        int time = Integer.parseInt(args[2]);
        int populationSize = Integer.parseInt(args[3]);
        float mutationChance = Float.parseFloat(args[4]);
        float timePercentage = Float.parseFloat(args[5]);

        float mergeAmount = 1 / timePercentage;
        float mergeTime = time * timePercentage;

        // Initialize the distances matrix
        int[][] distances = Utilities.generateMatrix(fileName);

        AdvancedThread[] threads = new AdvancedThread[nThreads];

        results = new Results[nThreads];

        // Start the threads
        for (int i = 0; i < nThreads; i++) {
            threads[i] = new AdvancedThread(populationSize, mutationChance, distances, i);
            results[i] = new Results(distances.length);
            threads[i].start();
        }

        executeMerge((int) Math.floor(mergeAmount) - 1, mergeTime, threads, populationSize);

        // After the time has passed, stop the threads
        CompletableFuture.delayedExecutor(time, TimeUnit.SECONDS).execute(() -> {
            for (AdvancedThread thread : threads) {
                thread.setRunning(false);
            }
        });

        // Wait for all threads to finish
        for (int i = 0; i < nThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                System.out.println("ERRO: " + e.getMessage());
            }
        }

        Utilities.showResults(results);

        try {
            Utilities.exportResults(results, args);
        } catch (IOException e) {
            System.out.println("ERRO: " + e.getMessage());
        }
    }
}