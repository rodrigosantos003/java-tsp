import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.Scanner;

public class Utilities {
    /**
     * Gera a matriz a partir de um ficheiro de texto
     *
     * @param fileName Nome do ficheiro
     * @return Matriz inicializada
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
        }

        return matrix;
    }


    /**
     * Calcula a distância de um dado caminho
     *
     * @param path      Caminho para calcular a distância
     * @param distances Matriz de distâncias
     * @return Valor da distância
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
     * Gera um caminho aleatório
     *
     * @param size Tamanho do caminho
     * @return Caminho aleatório
     */
    static int[] generateRandomPath(int size) {
        int[] path = new int[size];

        int count = 0;
        Random random = new Random();

        while (count < size) {
            int num = random.nextInt(size);
            boolean repeated = false;

            for (int i = 0; i < count; i++) {
                if (num == path[i]) {
                    repeated = true;
                    break;
                }
            }

            if (!repeated) {
                path[count] = num;
                count++;
            }
        }

        return path;
    }

    /**
     * Troca os elementos de um caminho
     *
     * @param originalPath Caminho original
     * @param rand         Objeto Random
     * @return Caminho com elementos trocados
     */
    static int[] elementRandomSwitch(int[] originalPath, Random rand) {
        int pos1 = rand.nextInt(originalPath.length);
        int pos2;

        do {
            pos2 = rand.nextInt(originalPath.length);
        } while (pos2 == pos1);

        int temp = originalPath[pos1];
        originalPath[pos1] = originalPath[pos2];
        originalPath[pos2] = temp;

        return originalPath;
    }

    /**
     * Atualiza os valores da memória central
     *
     * @param results Array de resultados
     * @param thread  Thread a atualizar
     */
    static void updateValues(Results[] results, Main.TSPThread thread) {
        int index = thread.getThreadIndex();
        int[] path = thread.getPopulation().get(0);

        results[index].setBestPath(path);
        results[index].setDistance(thread.getBestDistance());
        results[index].setExecutionTime(thread.getEndTime() - thread.getStartTime());
        results[index].setIterations(thread.getIterations());
    }

    /**
     * Apresenta os resultados
     *
     * @param results Array de resultados
     */
    static void showResults(Results[] results) {
        Arrays.sort(results, Comparator.comparing(Results::getDistance));

        System.out.println(results[0].toString());
    }
}
