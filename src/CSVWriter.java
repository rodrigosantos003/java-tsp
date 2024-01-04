import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class CSVWriter {

    public static void writeToCSV(String filePath, List<List<String>> data) throws IOException {
        try (
                Writer writer = Files.newBufferedWriter(Paths.get(filePath), java.nio.file.StandardOpenOption.APPEND, java.nio.file.StandardOpenOption.CREATE);
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)
        ) {
            for (List<String> record : data) {
                csvPrinter.printRecord(record);
            }
            csvPrinter.flush();
        }
    }
}
