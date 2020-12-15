package com.holub.database;

import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class XMLExporterTest {
    private final  String OUTPUT_FILE = "people_export.xml";
    private final  String EXPECTED_FILE = "people.xml";
    private Writer writer;
    private Table  table;

    public XMLExporterTest() throws IOException {
        writer = new FileWriter(OUTPUT_FILE);
        Reader in = new FileReader(EXPECTED_FILE);
        table = new ConcreteTable(new XMLImporter(in));
        in.close();
    }

    @Test
    void testConcreteBuilder() throws IOException {
        Reader in = new FileReader(OUTPUT_FILE);
        Reader expected_in = new FileReader(EXPECTED_FILE);
        table.export(new XMLExporter(writer));
        writer.close();

        Table actual = new ConcreteTable(new XMLImporter(in));
        Table expected = new ConcreteTable(new XMLImporter(expected_in));

        expected_in.close();
        in.close();

        assertEquals(expected.toString(), actual.toString());
    }
} 