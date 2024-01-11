import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

class TestResult {
    double initialTemperature;
    double coolingRate;
    double distance;
    double timeExec;
    int threads;
    String fileName;

    public TestResult(double initialTemperature, double coolingRate, double distance, double timeExec, int threads, String fileName) {
        this.initialTemperature = initialTemperature;
        this.coolingRate = coolingRate;
        this.distance = distance;
        this.timeExec = timeExec;
        this.threads = threads;
        this.fileName = fileName;
    }
}

public class ArgumentFinder {
    public static void main(String[] args) throws IOException, InterruptedException {
        // Definir valores iniciais para os argumentos
        int[] initialTemperatures = {100, 1000, 1000, 10000, 100000, 500000};
        double[] coolingRates = {0.1, 0.3, 0.5, 0.9, 0.95, 0.99, 0.999, 0.9999, 0.99999};

        String[] files = {"att48.txt"};

        boolean executeAllFiles =  true;

        if(executeAllFiles){
            File folder = new File("./tsp_tests/");
            File[] folderItems = folder.listFiles();
            if (folderItems != null) {
                files = new String[folderItems.length];

                for (int i = 0; i < files.length; i++) {
                    files[i] = folderItems[i].getName();
                }
            }
        }

        int threads = 8;
        int time = 1000;

        int testAmount = 10;

        int totalTests = initialTemperatures.length * coolingRates.length * files.length;
        int currentTest = 1;

        List<TestResult> results = new LinkedList<>();
        List<TestResult> bestResults = new LinkedList<>();

        for (String fileName : files) {
            for (int initialTemperature : initialTemperatures) {
                for (double coolingRate : coolingRates) {
                    for (int i = 0; i < testAmount; i++) {
                        ProcessBuilder pb = new ProcessBuilder("java", "-cp", "out/production/java-tsp", "SimulatedAnnealingTSP", fileName, String.valueOf(threads), String.valueOf(time), String.valueOf(initialTemperature), String.valueOf(coolingRate));
                        Process p = pb.start();
                        p.waitFor();
                    }

                    TestResult result = apresentarDados(testAmount);

                    results.add(result);

                    String executionTime = (result.timeExec < 1000 ? String.format("%.3f", result.timeExec) + " ms" : String.format("%.3f", (result.timeExec / 1000)) + " s");
                    System.out.println("Test " + currentTest + " / " + totalTests + " (" + fileName + ", " + initialTemperature + ", " + coolingRate + ") - " + result.distance + " | " + executionTime + "\n");
                    currentTest++;
                }
            }

            //Apresenta os resultados por ordem do custo do melhor caminho e depois por ordem de tempo de execução
            results.sort((o1, o2) -> {
                if (o1.distance < o2.distance) {
                    return -1;
                } else if (o1.distance > o2.distance) {
                    return 1;
                } else {
                    return Double.compare(o1.timeExec, o2.timeExec);
                }
            });

            //Escreve os resultados para um ficheiro de texto
            BufferedWriter bw = getBufferedWriter(fileName, results);
            bw.close();

            bestResults.add(results.get(0));
            results.clear();
        }

        Path filePath = Paths.get("results.csv");

        try{
            Files.delete(filePath);
        } catch (IOException e) {
            System.err.println("Error deleting the file: " + e.getMessage());
        }
        for (TestResult result : bestResults) {
            System.out.println("A realizar os testes para o ficheiro: " + result.fileName + " (" + result.initialTemperature + " | " + result.coolingRate + ")");
            for (int i = 0; i < testAmount; i++) {
                ProcessBuilder pb = new ProcessBuilder("java", "-cp", "out/production/java-tsp", "SimulatedAnnealingTSP", result.fileName, String.valueOf(result.threads), String.valueOf(time), String.valueOf(result.initialTemperature), String.valueOf(result.coolingRate));
                Process p = pb.start();
                p.waitFor();
            }
        }
    }

    private static BufferedWriter getBufferedWriter(String fileName, List<TestResult> results) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("test_results_" + fileName));
        for (TestResult result : results) {
            bw.write("Temperatura inicial: " + result.initialTemperature);
            bw.newLine();
            bw.write("Cooling rate: " + result.coolingRate);
            bw.newLine();
            bw.write("Custo medio do melhor caminho: " + result.distance);
            bw.newLine();
            String executionTime = (result.timeExec < 1000 ? String.format("%.3f", result.timeExec) + " ms" : String.format("%.3f", (result.timeExec / 1000)) + " s");
            bw.write("Tempo medio de execucao: " + executionTime);
            bw.newLine();
            bw.newLine();
        }
        return bw;
    }

    static TestResult apresentarDados(int testAmount) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("results.csv"));
        List<String> lines = new LinkedList<>();
        String line;
        while ((line = br.readLine()) != null) {
            lines.add(line);
            if (lines.size() > testAmount) {
                lines.remove(0);
            }
        }
        br.close();

        List<String[]> data = new LinkedList<>();

        for (String linha : lines) {
            data.add(linha.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1));
        }

        // 0: número do teste
        // 1: nome do ficheiro
        // 2: tempo
        // 3: threads
        // 4: temperatura inicial
        // 5: cooling rate
        // 6: melhor caminho
        // 7: custo do melhor caminho
        // 8: numero de iteracoes
        // 9: tempo de execucao

        //Calcula a média do custo do melhor caminho
        double distance = getDistance(data);

        //Calcula a média do tempo de execução
        double timeExec = 0;
        for (String[] lineData : data) {
            if (lineData[9].contains("ms")) {
                lineData[9] = lineData[9].substring(1, lineData[9].length() - 4);
                lineData[9] = lineData[9].replace(',', '.');
                timeExec += Double.parseDouble(lineData[9]);
            } else {
                lineData[9] = lineData[9].substring(1, lineData[9].length() - 3);
                lineData[9] = lineData[9].replace(',', '.');
                timeExec += Double.parseDouble(lineData[9]) * 1000;
            }
        }
        timeExec /= data.size();
        timeExec = Math.round(timeExec * 1000.0) / 1000.0;

        double initialTemperature = Double.parseDouble(data.get(0)[4]);
        double coolingRate = Double.parseDouble(data.get(0)[5]);
        String fileName = data.get(0)[1];
        int threads = Integer.parseInt(data.get(0)[3]);

        return new TestResult(initialTemperature, coolingRate, distance, timeExec, threads, fileName);
    }

    private static double getDistance(List<String[]> data) {
        double distance = 0;
        for (String[] lineData : data) {
            distance += Double.parseDouble(lineData[7]);
        }
        distance /= data.size();
        return distance;
    }
}