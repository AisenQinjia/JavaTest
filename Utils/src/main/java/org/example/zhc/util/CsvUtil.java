package org.example.zhc.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
public class CsvUtil {
    private CsvUtil(){}
    public static CSVParser readCsvFileWithHeader(String path) throws IOException {
        return new CSVParser(new FileReader(new File(path)), CSVFormat.DEFAULT
        .withFirstRecordAsHeader().withTrim()
        );
    }

    public static void writesToCsvFileWithHeader(String path, CSVFormat format, List<CSVRecord> records) throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(Paths.get(path));
        CSVPrinter csvPrinter = new CSVPrinter(writer, format);
        csvPrinter.printRecords(records);
        csvPrinter.flush();
    }
}
