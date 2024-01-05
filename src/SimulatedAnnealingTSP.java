import jdk.jshell.execution.Util;

import java.util.Arrays;
import java.util.Random;

public class SimulatedAnnealingTSP {

    private static final double INITIAL_TEMPERATURE = 100;
    private static final double COOLING_RATE = 0.9999999999999;
    private static final int NUM_ITERATIONS = 1000000000;

    public static void main(String[] args) {
        // Example usage
        int[][] distances = Utilities.generateMatrix("fri26.txt");

        int[] solution = simulatedAnnealingTSP(distances);

        System.out.println("Optimal Tour: " + Arrays.toString(solution));
        System.out.println("Total Distance: " + calculateTotalDistance(solution, distances));
    }

    private static int[] simulatedAnnealingTSP(int[][] distances) {
        int numCities = distances.length;
        int[] currentSolution = generateRandomSolution(numCities);
        double currentEnergy = calculateTotalDistance(currentSolution, distances);

        double temperature = INITIAL_TEMPERATURE;

        for (int iteration = 0; iteration < NUM_ITERATIONS; iteration++) {
            int[] newSolution = generateNeighborSolution(currentSolution);
            double newEnergy = calculateTotalDistance(newSolution, distances);

            if (acceptMove(currentEnergy, newEnergy, temperature)) {
                currentSolution = Arrays.copyOf(newSolution, newSolution.length);
                currentEnergy = newEnergy;
            }

            temperature *= COOLING_RATE;
        }

        return currentSolution;
    }

    private static int[] generateRandomSolution(int numCities) {
        int[] solution = new int[numCities];
        for (int i = 0; i < numCities; i++) {
            solution[i] = i;
        }
        shuffleArray(solution);
        return solution;
    }

    private static void shuffleArray(int[] array) {
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            int temp = array[i];
            array[i] = array[index];
            array[index] = temp;
        }
    }

    private static int[] generateNeighborSolution(int[] solution) {
        int[] neighborSolution = Arrays.copyOf(solution, solution.length);
        Random random = new Random();

        int swapIndex1 = random.nextInt(solution.length);
        int swapIndex2 = random.nextInt(solution.length);

        // Swap two cities in the solution
        int temp = neighborSolution[swapIndex1];
        neighborSolution[swapIndex1] = neighborSolution[swapIndex2];
        neighborSolution[swapIndex2] = temp;

        return neighborSolution;
    }

    private static double calculateTotalDistance(int[] solution, int[][] distances) {
        double totalDistance = 0;
        for (int i = 0; i < solution.length - 1; i++) {
            totalDistance += distances[solution[i]][solution[i + 1]];
        }
        // Add the distance from the last city back to the starting city
        totalDistance += distances[solution[solution.length - 1]][solution[0]];
        return totalDistance;
    }

    private static boolean acceptMove(double currentEnergy, double newEnergy, double temperature) {
        if (newEnergy < currentEnergy) {
            return true;
        } else {
            double probability = Math.exp((currentEnergy - newEnergy) / temperature);
            return Math.random() < probability;
        }
    }
}
