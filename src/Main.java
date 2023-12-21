import jdk.jshell.execution.Util;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class Main {
    static Results results;

    static class TSPThread extends Thread {
        private int populationSize;
        private float mutationChance;
        private int[][] distances;

        private ArrayList<int[]> population = new ArrayList<>();

        public TSPThread(int populationSize, float mutationChance, int[][] distances) {
            this.populationSize = populationSize;
            this.mutationChance = mutationChance;
            this.distances = distances;
        }

        public ArrayList<int[]> getPopulation() {
            return population;
        }

        @Override
        public void run() {
            for (int i = 0; i < populationSize; i++) {
                population.add(Utilities.generateRandomPath(distances.length));
            }

            while (true) {
                population.sort((path1, path2) -> {
                    int distance1 = Utilities.calculateDistance(path1, distances);
                    int distance2 = Utilities.calculateDistance(path2, distances);
                    return Integer.compare(distance1, distance2);
                });


                int[][] pmxResult = PMXCrossover.pmxCrossover(population.get(0), population.get(1));

                population.set(populationSize - 2, pmxResult[0]);
                population.set(populationSize - 1, pmxResult[1]);

                Random rand = new Random();
                float mutationValue = rand.nextFloat(1);

                if (mutationValue < mutationChance) {
                    population.set(populationSize - 2, Utilities.elementRandomSwitch(population.get(populationSize - 2), rand));
                    population.set(populationSize - 1, Utilities.elementRandomSwitch(population.get(populationSize - 1), rand));
                }


                //            for(int i = 0; i < populationSize; i++){
//                System.out.print("[");
//                for(int j = 0; j < distances.length; j++){
//                    System.out.print(population.get(i)[j]);
//                }
//                System.out.print("]" + Utilities.calculateDistance(population.get(i), distances) + "\n");
//            }

            }
        }

        public static void main(String[] args) {
            if (args.length != 5) {
                System.out.println("ERRO: Número de argumentos inválido!");
            }

            //Obter os argumentos
            String fileName = args[0];
            int nThreads = Integer.parseInt(args[1]);
            int time = Integer.parseInt(args[2]);
            int populationSize = Integer.parseInt(args[3]);
            float mutationChance = Float.parseFloat(args[4]);

            //Inicialização da matriz
            int[][] distances = Utilities.generateMatrix(fileName);

            TSPThread[] threads = new TSPThread[nThreads];

            results = new Results(distances.length);

            for (int i = 0; i < nThreads; i++) {
                threads[i] = new TSPThread(populationSize, mutationChance, distances);
                threads[i].start();
            }

            CompletableFuture.delayedExecutor(time, TimeUnit.SECONDS).execute(() -> {
                for (int i = 0; i < nThreads; i++) {
                    threads[i].interrupt();
                }
            });
        }
    }
}