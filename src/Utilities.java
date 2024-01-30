import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Utilities {
    /**
     * Generates a matrix from a given textfile
     *
     * @param fileName Name of the file
     * @return Initialized matrix
     */
    static int[][] generateMatrix(String fileName) {
        int[][] matrix = new int[0][];

        try {
            File file = new File("./tsp_tests/" + fileName);

            Scanner scanner = new Scanner(file);

            int matrixSize = scanner.nextInt();

            matrix = new int[matrixSize][matrixSize];

            for (int i = 0; i < matrixSize; i++) {
                for (int j = 0; j < matrixSize; j++) {
                    matrix[i][j] = scanner.nextInt();
                }
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Erro ao abrir o ficheiro!");
            System.exit(-1);
        }

        return matrix;
    }

    /**
     * Calculates the distance of a given path
     *
     * @param path      Path to calculate the distance
     * @param distances Distances matrix
     * @return Distance of the path
     */
    static int calculateDistance(int[] path, int[][] distances) {
        int totalDistance = 0;

        for (int i = 0; i < distances.length; i++) {
            int src = path[i];
            int dest = path[(i + 1) % distances.length];
            totalDistance += distances[src][dest];
        }

        return totalDistance;
    }

    /**
     * Generates a random path
     *
     * @param size Size of the path
     * @return Random path
     */
    static int[] generateRandomPath(int size, Random random) {
        int[] path = new int[size];

        for (int i = 0; i < size; i++) {
            path[i] = i;
        }

        shuffleArray(path, random);

        return path;
    }

    /**
     * Shuffles an array, ensuring that each element is in a different position
     *
     * @param array Array to shuffle
     * @param rand  Random object
     */
    private static void shuffleArray(int[] array, Random rand) {
        for (int i = array.length - 1; i > 0; i--) {
            int index = rand.nextInt(i + 1);
            int temp = array[i];
            array[i] = array[index];
            array[index] = temp;
        }
    }

    /**
     * Generates a random neighbor solution
     * 
     * @param path Path to generate the neighbor solution
     * @param rand Random object
     * @return Neighbor solution
     */
    static int[] elementRandomSwitch(int[] path, Random rand) {
        int[] neighborSolution = Arrays.copyOf(path, path.length);

        int swapIndex1 = rand.nextInt(path.length);
        int swapIndex2 = rand.nextInt(path.length);

        // Swap two cities in the solution
        int temp = neighborSolution[swapIndex1];
        neighborSolution[swapIndex1] = neighborSolution[swapIndex2];
        neighborSolution[swapIndex2] = temp;

        return neighborSolution;
    }

    /**
     * Updates the values of the results array
     * 
     * @param results Array of results
     * @param thread  Thread to get the values
     */
    static void updateValues(Results[] results, TSPThread thread) {
        int index = thread.getThreadIndex();
        int[] path = thread.getBestPath();

        results[index].setBestPath(path);
        results[index].setDistance(thread.getBestDistance());
        results[index].setExecutionTime(thread.getEndTime() - thread.getStartTime());
        results[index].setIterations(thread.getIterations());
    }

    /**
     * Displays the results
     * 
     * @param results Array of results
     */
    static void showResults(Results[] results) {
        Arrays.sort(results, Comparator.comparing(Results::getDistance));

        System.out.println(results[0].toString());
    }

    /**
     * Exports the results to a CSV file
     * 
     * @param results Array of results
     * @param args    Arguments of the program
     * @throws IOException
     */
    static void exportResults(Results[] results, String[] args) throws IOException {
        Arrays.sort(results, Comparator.comparing(Results::getDistance));

        List<List<String>> data = List.of(Arrays.asList(
                args[0], args[2], args[1], args[3],
                args[4], Arrays.toString(results[0].getBestPath()), String.valueOf(results[0].getDistance()),
                String.valueOf(results[0].getIterations()), String.valueOf(results[0].writeTime())));

        CSVWriter.writeToCSV("results.csv", data);
    }
}
