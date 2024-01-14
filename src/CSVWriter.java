import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class CSVWriter {
    public static void writeToCSV(String filePath, List<List<String>> data) throws IOException {
        int testNumber = getTestNumber(filePath, data);

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath), StandardOpenOption.APPEND, StandardOpenOption.CREATE)) {

            StringBuilder sb = new StringBuilder();
            sb.append(testNumber).append(",");
            for (List<String> record : data) {
                for (int i = 0; i < record.size(); i++) {
                    String field = record.get(i);
                    if (field.contains(",") && (field.startsWith("[") && field.endsWith("]"))) {
                        sb.append("\"").append(field).append("\"");
                    } else if (i == record.size() - 1) {
                        sb.append("\"").append(field).append("\"");
                    } else {
                        sb.append(field);
                    }
                    if (i != record.size() - 1) {
                        sb.append(",");
                    }
                }
                sb.append(System.lineSeparator()); // Adiciona quebra de linha
                writer.write(sb.toString());
            }
        }
    }

    private static int getTestNumber(String filePath, List<List<String>> data) throws IOException {
        String lastLine = null;
        int testNumber = 1;

        File file = new File(filePath);

        if (!file.exists()) {
            file.createNewFile();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lastLine = line;
            }
        }

        if (lastLine != null) {
            String[] lastValues = lastLine.split(",");
            String testName = data.get(0).get(0);
            testNumber = (lastValues[1].equals(testName)) ? Integer.parseInt(lastValues[0]) + 1 : testNumber;
        }
        return testNumber;
    }
}
