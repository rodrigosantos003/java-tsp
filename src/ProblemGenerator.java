import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;

public class ProblemGenerator {
    public static void main(String[] args) {
        try {
            int n = generateNumberOfCities(new int[]{22, 24, 26, 42, 48});

            int[] xCoordinates = generateCoordinates(n);
            int[] yCoordinates = generateCoordinates(n);

            int[][] distancesMatrix = calculateDistancesMatrix(n, xCoordinates, yCoordinates);

            //Nome do ficheiro
            String fileName = "./tsp_tests/custom_" + n + ".txt";

            saveDistancesMatrix(fileName, n, distancesMatrix);

        } catch (IOException e) {
            System.out.println("ERRO: " + e.getMessage());
        }
    }

    /**
     * Gera o número de cidades
     * @param exclusions Cidades excluídas
     * @return Número de cidades
     */
    private static int generateNumberOfCities(int[] exclusions) {
        Random random = new Random();
        int n;

        do {
            n = random.nextInt(50 - 18 + 1) + 18;
        } while (Arrays.asList(exclusions).contains(n));

        return n;
    }

    /**
     * Gera coordenadas x e y usando distribuição normal
     * @param n Tamaho das coordenadas
     * @return Valor das coordenadas
     */
    private static int[] generateCoordinates(int n) {
        int[] coordinates = new int[n];
        Random random = new Random();

        for (int i = 0; i < n; i++) {
            coordinates[i] = (int) (60 + random.nextGaussian() * 30);
        }

        return coordinates;
    }

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
     * Calcula a distância de um caminho
     */
    private static int calculateDistance(int x1, int y1, int x2, int y2) {
        return (int) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    /**
     * Guarda a matriz num ficheiro de texto
     * @param file Ficheiro da matriz
     * @param n Tamanho da matriz
     * @param distancesMatrix Matriz de distâncias
     */
    private static void saveDistancesMatrix(String file, int n, int[][] distancesMatrix) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file, true))) {
            writer.println(n);

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    writer.printf("%4d ", distancesMatrix[i][j]);
                }
                writer.println();
            }
            writer.println();
        }
    }
}
