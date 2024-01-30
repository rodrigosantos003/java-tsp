import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProblemGenerator {
    public static void main(String[] args) {
        try {
            // Generate problems with 18 to 60 cities
            for (int seed = 18; seed <= 60; seed++) {
                if (testExists(seed)) {
                    continue;
                }

                Random random = new Random(seed);

                int[] coordinates = generateCoordinates(seed, random);

                int[] xCoordinates = new int[seed];
                int[] yCoordinates = new int[seed];

                for (int i = 0, j = 0; i < seed * 2; i += 2, j++) {
                    xCoordinates[j] = coordinates[i];
                    yCoordinates[j] = coordinates[i + 1];
                }

                int[][] distancesMatrix = calculateDistancesMatrix(seed, xCoordinates, yCoordinates);

                // Name of the file
                String fileName = "./tsp_tests/ex_gau" + seed + ".txt";

                saveDistancesMatrix(fileName, seed, distancesMatrix);

                System.out.println("Novo problema gerado: " + seed + " cidades");
            }
        } catch (NumberFormatException e) {
            System.out.println("ERRO: A semente (número de cidades) deve ser um inteiro válido.");
        } catch (IOException e) {
            System.out.println("ERRO: " + e.getMessage());
        }
    }

    /**
     * Generate x and y coordinates using a Gaussian distribution
     *
     * @param n      Size of the coordinates array
     * @param random Random object
     * @return Coordinates array
     */
    private static int[] generateCoordinates(int n, Random random) {
        int[] coordinates = new int[n * 2];

        for (int i = 0; i < n * 2; i++) {
            coordinates[i] = (int) (random.nextGaussian(60, 30));
        }

        return coordinates;
    }

    /**
     * Calculates the distances matrix
     *
     * @param n            Size of the matrix
     * @param xCoordinates X coordinates
     * @param yCoordinates Y coordinates
     * @return Distances matrix
     */
    private static int[][] calculateDistancesMatrix(int n, int[] xCoordinates, int[] yCoordinates) {
        int[][] distancesMatrix = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int distance = calculateDistance(xCoordinates[i], yCoordinates[i], xCoordinates[j], yCoordinates[j]);
                distancesMatrix[i][j] = distance;
            }
        }

        return distancesMatrix;
    }

    /**
     * Calculates the distance between two points
     *
     * @param x1 X coordinate of point 1
     * @param y1 Y coordinate of point 1
     * @param x2 X coordinate of point 2
     * @param y2 Y coordinate of point 2
     * @return Distance between the two points
     */
    private static int calculateDistance(int x1, int y1, int x2, int y2) {
        return (int) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    /**
     * Saves the distances matrix to a text file
     *
     * @param file            File name
     * @param n               Size of the matrix
     * @param distancesMatrix Distances matrix
     */
    private static void saveDistancesMatrix(String file, int n, int[][] distancesMatrix) throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(file, true));
        writer.println(n);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                writer.printf("%4d ", distancesMatrix[i][j]);
            }
            writer.println();
        }
        writer.println();
        writer.close();
    }

    /**
     * Verifies if a test with the given number of cities already exists
     *
     * @param test Number of cities
     * @return True if the test exists, false otherwise
     */
    private static boolean testExists(int test) {
        File folder = new File("./tsp_tests/");
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile() && file.getName().matches(".*\\d+.*")) {
                    Pattern pattern = Pattern.compile("\\d+");
                    Matcher matcher = pattern.matcher(file.getName());
                    while (matcher.find()) {
                        int extractedNumber = Integer.parseInt(matcher.group());
                        if (extractedNumber == test) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
