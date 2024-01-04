import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class Base {
    static Results[] results; //Resultados

    static class TSPThread extends Thread {
        private final ArrayList<int[]> population = new ArrayList<>(); //População
        private final int populationSize; //Tamanho da população
        private final float mutationChance; //Probabilidade de mutação
        private final int[][] distances; //Matriz de distâncias
        private int bestDistance; //Melhor distância
        private final long startTime; //Tempo inicial
        private long endTime; //Tempo final
        private int iterations; //Iterações

        private boolean isRunning; //Condição de execução
        private final int threadIndex; //Índice de criação da thread

        public TSPThread(int populationSize, float mutationChance, int[][] distances, int threadIndex) {
            this.populationSize = populationSize;
            this.mutationChance = mutationChance;
            this.distances = distances;
            this.bestDistance = 0;
            this.startTime = System.currentTimeMillis();
            this.endTime = 0;
            this.iterations = 0;
            this.isRunning = true;
            this.threadIndex = threadIndex;

            //Inicializa as populações
            for (int i = 0; i < populationSize; i++) {
                population.add(Utilities.generateRandomPath(distances.length));
            }
        }

        public ArrayList<int[]> getPopulation() {
            return population;
        }

        public int getBestDistance() {
            return bestDistance;
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
            bestDistance = Utilities.calculateDistance(population.get(0), distances);
            endTime = System.currentTimeMillis();

            while (isRunning) {
                iterations++;

                //Ordena a população
                population.sort((path1, path2) -> {
                    int distance1 = Utilities.calculateDistance(path1, distances);
                    int distance2 = Utilities.calculateDistance(path2, distances);
                    return Integer.compare(distance1, distance2);
                });

                //Aplicação do crossover
                int[][] pmxResult = PMXCrossover.pmxCrossover(population.get(0), population.get(1));
                population.set(populationSize - 2, pmxResult[0]);
                population.set(populationSize - 1, pmxResult[1]);

                //Mutação dos elementos
                Random rand = new Random();
                float mutationValue = rand.nextFloat(1);

                if (mutationValue < mutationChance) {
                    population.set(populationSize - 2, Utilities.elementRandomSwitch(population.get(populationSize - 2), rand));
                    population.set(populationSize - 1, Utilities.elementRandomSwitch(population.get(populationSize - 1), rand));
                }

                int currentBestDistance = Utilities.calculateDistance(population.get(0), distances);
                if (currentBestDistance < bestDistance) {
                    bestDistance = currentBestDistance;
                    endTime = System.currentTimeMillis();
                }
            }

            Utilities.updateBaseValues(results, this);
        }
    }

    public static void main(String[] args) {
        if (args.length != 5) {
            System.out.println("ERRO: Número de argumentos inválido!");
            System.out.println("Formato correto dos argumentos: <file_name> <threads> <time> <population> <mutation>");

            System.exit(-1);
        }

        //Obtém os argumentos
        String fileName = args[0];
        int nThreads = Integer.parseInt(args[1]);
        int time = Integer.parseInt(args[2]);
        int populationSize = Integer.parseInt(args[3]);
        float mutationChance = Float.parseFloat(args[4]);

        //Inicializa a matriz
        int[][] distances = Utilities.generateMatrix(fileName);

        TSPThread[] threads = new TSPThread[nThreads];

        results = new Results[nThreads];

        //Início da execução das threads
        for (int i = 0; i < nThreads; i++) {
            threads[i] = new TSPThread(populationSize, mutationChance, distances, i);
            results[i] = new Results(distances.length);
            threads[i].start();
        }

        //Após o tempo definido, terminar as threads
        CompletableFuture.delayedExecutor(time, TimeUnit.SECONDS).execute(() -> {
            for (TSPThread thread : threads) {
                thread.setRunning(false);
            }
        });

        //Espera que as threads terminem
        for (int i = 0; i < nThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                System.out.println("ERRO: " + e.getMessage());
            }
        }

        Utilities.showResults(results);
    }
}