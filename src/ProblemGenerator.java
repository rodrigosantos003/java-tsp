import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class ProblemGenerator {
    public static void main(String[] args) {
        try {
            // Definir o número de cidades
            int n = gerarNumeroDeCidades(18, 50, new int[]{22, 24, 26, 42, 48});

            // Gerar coordenadas x e y usando distribuição normal
            int[] coordenadasX = gerarCoordenadas(n);
            int[] coordenadasY = gerarCoordenadas(n);

            // Calcular a matriz de distâncias
            int[][] matrizDistancias = calcularMatrizDistancias(n, coordenadasX, coordenadasY);

            // Gerar o nome do arquivo dinamicamente
            String nomeArquivo = "custom_" + n + ".txt";

            // Salvar resultados no arquivo gerado dinamicamente
            salvarMatrizDistancias(nomeArquivo, n, matrizDistancias);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int gerarNumeroDeCidades(int min, int max, int[] exclusoes) {
        Random random = new Random();
        int n;
        do {
            n = random.nextInt(max - min + 1) + min;
        } while (contem(exclusoes, n));

        return n;
    }

    private static boolean contem(int[] array, int valor) {
        for (int elemento : array) {
            if (elemento == valor) {
                return true;
            }
        }
        return false;
    }

    private static int[] gerarCoordenadas(int n) {
        int[] coordenadas = new int[n];
        Random random = new Random();

        for (int i = 0; i < n; i++) {
            coordenadas[i] = (int) (60 + random.nextGaussian() * 30);
        }

        return coordenadas;
    }

    private static int[][] calcularMatrizDistancias(int n, int[] coordenadasX, int[] coordenadasY) {
        int[][] matrizDistancias = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int distancia = calcularDistancia(coordenadasX[i], coordenadasY[i], coordenadasX[j], coordenadasY[j]);
                matrizDistancias[i][j] = distancia;
            }
        }

        return matrizDistancias;
    }

    private static int calcularDistancia(int x1, int y1, int x2, int y2) {
        return (int) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    private static void salvarMatrizDistancias(String arquivo, int n, int[][] matrizDistancias) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(arquivo, true))) {
            writer.println(n);

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    writer.printf("%4d ", matrizDistancias[i][j]);
                }
                writer.println();
            }
            writer.println();
        }
    }
}
