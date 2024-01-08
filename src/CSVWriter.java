import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class CSVWriter {

    public static void writeToCSV(String filePath, List<List<String>> data) throws IOException {
        String lastLine = null;
        int testNumber = 1;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lastLine = line;
            }
        }

        if(lastLine != null){
            String[] lastValues = lastLine.split(",");
            String testName = data.get(0).get(0);
            testNumber = (lastValues[1].equals(testName)) ? Integer.parseInt(lastValues[0])+1 : testNumber;
        }

        try (BufferedWriter writer = Files.newBufferedWriter(
                Paths.get(filePath), StandardOpenOption.APPEND, StandardOpenOption.CREATE)) {

            StringBuilder sb = new StringBuilder();
            sb.append(testNumber).append(",");
            for (List<String> record : data) {
                for (String field : record) {
                    sb.append(field).append(",");
                }
                sb.deleteCharAt(sb.length() - 1); // Remove a última vírgula
                sb.append(System.lineSeparator()); // Adiciona quebra de linha
                writer.write(sb.toString());
            }
        }
    }
}
