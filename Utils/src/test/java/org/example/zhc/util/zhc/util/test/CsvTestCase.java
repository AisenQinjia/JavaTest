package org.example.zhc.util.zhc.util.test;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.example.zhc.util.CsvUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;

@Slf4j
public class CsvTestCase {
    @Test
    public void readWriteTest() throws IOException {
        CSVParser csvRecords = CsvUtil.readCsvFileWithHeader("testFile/csvtest/test.csv");
        log.info("csv records size:{}",csvRecords.getRecordNumber());
        CsvUtil.writesToCsvFileWithHeader("testFile/csvtest/writeback.csv",
                CSVFormat.DEFAULT.withHeader(csvRecords.getHeaderMap().keySet().toArray(new String[0])),
                csvRecords.getRecords());
    }
}
