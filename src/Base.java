import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class Base {
    static Results[] results;
    static Random rand = new Random();

    static class BaseThread extends Thread implements TSPThread {
        private final Individual[] population;
        private final int populationSize;
        private final float mutationChance;
        private final int[][] distances;
        private int bestDistance;
        private final long startTime;
        private long endTime;
        private int iterations;

        private boolean isRunning;
        private final int threadIndex;

        public BaseThread(int populationSize, float mutationChance, int[][] distances, int threadIndex) {
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

        @Override
        public void run() {
            bestDistance = population[0].getDistance();
            endTime = System.nanoTime();
            int localIterations = 0;

            int idx1 = populationSize - 2;
            int idx2 = populationSize - 1;

            while (isRunning) {
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

    public static void main(String[] args) {
        if (args.length != 5) {
            System.out.println("ERRO: Número de argumentos inválido!");
            System.out.println("Formato correto dos argumentos: <file_name> <threads> <time> <population> <mutation>");

            System.exit(-1);
        }

        // Get the arguments
        String fileName = args[0];
        int nThreads = Integer.parseInt(args[1]);
        int time = Integer.parseInt(args[2]);
        int populationSize = Integer.parseInt(args[3]);
        float mutationChance = Float.parseFloat(args[4]);

        // Initialize the distances matrix
        int[][] distances = Utilities.generateMatrix(fileName);

        BaseThread[] threads = new BaseThread[nThreads];

        results = new Results[nThreads];

        // Start the threads
        for (int i = 0; i < nThreads; i++) {
            threads[i] = new BaseThread(populationSize, mutationChance, distances, i);
            results[i] = new Results(distances.length);
            threads[i].start();
        }

        // After the time has passed, stop the threads
        CompletableFuture.delayedExecutor(time, TimeUnit.SECONDS).execute(() -> {
            for (BaseThread thread : threads) {
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