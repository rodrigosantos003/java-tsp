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
            // Gera problemas de 18 a 60 cidades
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

                // Nome do ficheiro
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
     * Gera coordenadas x e y usando distribuição normal
     *
     * @param n      Tamanho das coordenadas
     * @param random Objeto Random com semente definida
     * @return Valor das coordenadas
     */
    private static int[] generateCoordinates(int n, Random random) {
        int[] coordinates = new int[n * 2];

        for (int i = 0; i < n * 2; i++) {
            coordinates[i] = (int) (random.nextGaussian(60, 30));
        }

        return coordinates;
    }

    /**
     * Calcula a matriz de distâncias
     *
     * @param n            Tamanho da matriz
     * @param xCoordinates Coordenadas x
     * @param yCoordinates Coordenadas y
     * @return Matriz de distâncias
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
     * Calcula a distância entre dois pontos
     *
     * @param x1 Coordenada x do ponto 1
     * @param y1 Coordenada y do ponto 1
     * @param x2 Coordenada x do ponto 2
     * @param y2 Coordenada y do ponto 2
     * @return Distância entre os dois pontos
     */
    private static int calculateDistance(int x1, int y1, int x2, int y2) {
        return (int) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    /**
     * Guarda a matriz num ficheiro de texto
     *
     * @param file            Ficheiro da matriz
     * @param n               Tamanho da matriz
     * @param distancesMatrix Matriz de distâncias
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
     * Verifica se já existe um teste com o número de cidades indicado
     *
     * @param test Número de cidades
     * @return True se já existir um teste com o número de cidades indicado, false
     * caso contrário
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
