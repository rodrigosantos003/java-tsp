import java.io.*;
import java.util.LinkedList;
import java.util.List;

class TestResult{
    double initialTemperature;
    double coolingRate;
    double distance;
    double timeExec;

    public TestResult(double initialTemperature, double coolingRate, double distance, double timeExec){
        this.initialTemperature = initialTemperature;
        this.coolingRate = coolingRate;
        this.distance = distance;
        this.timeExec = timeExec;
    }
}
public class ArgumentFinder {
    public static void main(String[] args) throws IOException, InterruptedException {
        // Definir valores iniciais para os argumentos
        int[] initialTemperatures = {100, 1000, 1000, 10000, 100000, 500000};
        double[] coolingRates = {0.1, 0.3, 0.5, 0.9, 0.95, 0.99, 0.999, 0.9999, 0.99999};
        String[] files = {"ex4.txt", "ex7.txt", "ex8.txt", "ex9.txt", "ex10.txt", "sp11.txt", "uk12.txt", "ex13.txt", "burma14.txt", "lau15.txt", "ulysses16.txt", "gr17.txt", "ulysses22.txt", "gr24.txt", "fri26.txt", "dantzig42.txt", "att48.txt"};
        int threads = 8;
        int time = 1000;

        int testAmount = 10;

        int totalTests = initialTemperatures.length * coolingRates.length * files.length;
        int currentTest = 1;

        boolean executaTestes = true;

        List<TestResult> results = new LinkedList<>();

        if (executaTestes) {
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

                        System.out.println("Test " + currentTest + " / " + totalTests + " (" + fileName + ", " + initialTemperature + ", " + coolingRate + ") - " + result.distance + " | " + result.timeExec + " ms \n");
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
                    if (o1.timeExec < o2.timeExec) {
                        return -1;
                    } else if (o1.timeExec > o2.timeExec) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            });

            //Escreve os resultados para um ficheiro de texto
            BufferedWriter bw = new BufferedWriter(new FileWriter("test_results_" + fileName));
            for (TestResult result : results) {
                bw.write("Temperatura inicial: " + result.initialTemperature);
                bw.newLine();
                bw.write("Cooling rate: " + result.coolingRate);
                bw.newLine();
                bw.write("Custo medio do melhor caminho: " + result.distance);
                bw.newLine();
                bw.write("Tempo medio de execucao: " + result.timeExec + " ms");
                bw.newLine();
                bw.newLine();
            }
            bw.close();

            results.clear();
        }
        }
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
        double distance = 0;
        for (String[] lineData : data) {
            distance += Double.parseDouble(lineData[7]);
        }
        distance /= data.size();

        //Calcula a média do tempo de execução
        double timeExec = 0;
        for (String[] lineData : data) {
            //remove os ms do tempo
            lineData[9] = lineData[9].substring(1, lineData[9].length() - 4);
            lineData[9] = lineData[9].replace(',', '.');
            timeExec += Double.parseDouble(lineData[9]);
        }
        timeExec /= data.size();
        timeExec = Math.round(timeExec * 1000.0) / 1000.0;

        double initialTemperature = Double.parseDouble(data.get(0)[4]);;
        double coolingRate = Double.parseDouble(data.get(0)[5]);

        return new TestResult(initialTemperature, coolingRate, distance, timeExec);
    }
}