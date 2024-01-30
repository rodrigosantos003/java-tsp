import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class SimulatedAnnealingTSP {
    static Results[] results;
    static Random rand = new Random();

    static class SAThread extends Thread implements TSPThread {
        private double temperature;
        private final double coolingRate;
        private final int[][] distances;
        private int bestDistance;
        private int[] bestPath;
        private final long startTime;
        private long endTime;
        private int iterations;
        private boolean isRunning;
        private final int threadIndex;

        public SAThread(double temperature, double coolingRate, int[][] distances, int threadIndex) {
            this.temperature = temperature;
            this.coolingRate = coolingRate;
            this.distances = distances;
            this.bestDistance = Integer.MAX_VALUE;
            this.startTime = System.nanoTime();
            this.endTime = 0;
            this.iterations = 0;
            this.isRunning = true;
            this.threadIndex = threadIndex;
        }

        public int getBestDistance() {
            return bestDistance;
        }

        public int[] getBestPath() {
            return bestPath;
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

        /**
         * Evaluate if the new solution is better than the current one
         *
         * @param currentDistance Current distance
         * @param newDistance     New distance
         * @param temperature     Current temperature
         * @return True if the new solution is better than the current one, false
         *         otherwise
         */
        private static boolean acceptNewSolution(double currentDistance, double newDistance, double temperature) {
            if (newDistance < currentDistance) {
                return true;
            } else {
                double probability = Math.exp((currentDistance - newDistance) / temperature);
                return Math.random() < probability;
            }
        }

        @Override
        public void run() {
            int[] currentSolution = Utilities.generateRandomPath(distances.length, rand);
            int currentDistance = Utilities.calculateDistance(currentSolution, distances);

            while (isRunning && temperature > 1) {
                iterations++;

                int[] newSolution = Utilities.elementRandomSwitch(currentSolution, rand);
                int newDistance = Utilities.calculateDistance(newSolution, distances);

                if (acceptNewSolution(currentDistance, newDistance, temperature)) {
                    currentSolution = Arrays.copyOf(newSolution, newSolution.length);
                    currentDistance = newDistance;
                }

                if (currentDistance < bestDistance) {
                    bestDistance = currentDistance;
                    bestPath = currentSolution;
                    endTime = System.nanoTime();
                }

                // Lowers the temperature
                temperature *= coolingRate;
            }

            Utilities.updateValues(results, this);
        }
    }

    public static void main(String[] args) {
        if (args.length != 5) {
            System.out.println("ERRO: Número de argumentos inválido!");
            System.out.println(
                    "Formato correto dos argumentos: <file_name> <threads> <time> <initial_temperature> <cooling_rate>");

            System.exit(-1);
        }

        // Get the arguments
        String fileName = args[0];
        int nThreads = Integer.parseInt(args[1]);
        int time = Integer.parseInt(args[2]);
        double initialTemperature = Double.parseDouble(args[3]);
        double coolingRate = Double.parseDouble(args[4]);

        // Initialize the distances matrix
        int[][] distances = Utilities.generateMatrix(fileName);

        SAThread[] threads = new SAThread[nThreads];

        results = new Results[nThreads];

        // Start the threads
        for (int i = 0; i < nThreads; i++) {
            threads[i] = new SAThread(initialTemperature, coolingRate, distances, i);
            results[i] = new Results(distances.length);
            threads[i].start();
        }

        // After the time has passed, stop the threads
        CompletableFuture.delayedExecutor(time, TimeUnit.SECONDS).execute(() -> {
            for (SAThread thread : threads) {
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
            throw new RuntimeException(e);
        }
    }
}
