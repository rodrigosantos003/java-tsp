import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class ProblemGenerator {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Por favor, forneça a semente (que também será o número de cidades) como argumento.");
            return;
        }

        try {
            int seed = Integer.parseInt(args[0]);

            Random random = new Random(seed);

            int n = seed;

            int[] coordinates = generateCoordinates(n, random);

            int[] xCoordinates = new int[n];
            int[] yCoordinates = new int[n];

            for (int i = 0, j=0; i < n * 2; i+=2, j++) {
                xCoordinates[j] = coordinates[i];
                yCoordinates[j] = coordinates[i+1];

            }

            int[][] distancesMatrix = calculateDistancesMatrix(n, xCoordinates, yCoordinates);

            // Nome do arquivo
            String fileName = "./ex_gau" + n + ".txt";

            saveDistancesMatrix(fileName, n, distancesMatrix);

            System.out.println("Novo problema gerado: " + n + " cidades");
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
            coordinates[i] = (int) (random.nextGaussian(60,30)) ;
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
        writer.close(); // Fechar o PrintWriter após o uso
    }
}
