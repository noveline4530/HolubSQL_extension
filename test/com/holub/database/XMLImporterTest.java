package com.holub.database;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;

class XMLImporterTest {
    private Reader in;
    Table.Importer importer;

    XMLImporterTest() throws IOException {
        in = new FileReader("people.xml");
        importer = new XMLImporter(in);
        importer.startTable();
    }

    @Test
    void loadTableName() throws IOException {
        String expected = "people";
        String tableName = importer.loadTableName();
        assertEquals(expected, tableName);
    }

    @Test
    void loadWidth() throws IOException {
        int expected = 3;
        int width = importer.loadWidth();
        assertEquals(expected, width);
    }

    @Test
    void loadColumnNames() throws IOException {
        String tableName = importer.loadTableName();
        int width = importer.loadWidth();
        Iterator columns = importer.loadColumnNames();

        String[] columnNames = new String[width];
        for (int i = 0; columns.hasNext();) {
            columnNames[i++] = (String) columns.next();
        }
        
        assertEquals("first", columnNames[0]);
        assertEquals("last", columnNames[1]);
        assertEquals("addrId", columnNames[2]);
    }

    @Test
    void loadRow() throws IOException {
        int width = importer.loadWidth();
        Iterator columns = importer.loadColumnNames();
        String[] columnNames = new String[width];
        Object[] current = new Object[width];
        
        for (int i = 0; columns.hasNext();) {
            columnNames[i++] = (String) columns.next();
        }
        while ((columns = importer.loadRow()) != null) {
            for (int i = 0; columns.hasNext();)
                current[i++] = columns.next();
        }
        
        assertEquals("Allen", current[0].toString());
        assertEquals("Holub", current[1].toString());
        assertEquals("1", current[2].toString());
    }
}
