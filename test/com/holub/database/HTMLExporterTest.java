package com.holub.database;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class HTMLExporterTest {

    private Table people;
    private String[] col, row1, row2, row3;

    @BeforeEach
    public void init(){
        col = new String[]{ "First", "Last"	};
        row1 = new String[]{ "Allen",	"Holub" };
        row2 = new String[]{ "Ichabod",	"Crane" };
        row3 = new String[]{ "Rip",		"VanWinkle"};
        Table people = TableFactory.create( "people", col);
        people.insert( row1 );
        people.insert( row2 );
        people.insert( row3 );
    }

    @Test
    void startTable() throws IOException{
        Writer writer = new StringWriter();
        HTMLExporter tableBuilder = new HTMLExporter(writer);
        tableBuilder.startTable();
        assertEquals(writer.toString(), "<html><body><table>");
        writer.close();
    }

    @Test
    void endTable() throws IOException {
        Writer writer = new StringWriter();
        HTMLExporter tableBuilder = new HTMLExporter(writer);
        tableBuilder.endTable();
        assertEquals(writer.toString(), "</tbody></table></body></html>");
        writer.close();
    }


    @Test
    void storeRow() throws IOException {
        Writer writer = new StringWriter();
        HTMLExporter tableBuilder = new HTMLExporter(writer);
        tableBuilder.storeRow(Arrays.stream(col).iterator());
        assertEquals(writer.toString(), "<tr><td>First</td><td>Last</td></tr>");
        writer.close();
    }

    @Test
    void exportResult() throws IOException {
        Writer writer = new StringWriter();
        HTMLExporter tableBuilder = new HTMLExporter(writer);
        tableBuilder.startTable();
        tableBuilder.storeMetadata("people", 2, 3, Arrays.stream(col).iterator());
        tableBuilder.storeRow(Arrays.stream(row1).iterator());
        tableBuilder.storeRow(Arrays.stream(row2).iterator());
        tableBuilder.storeRow(Arrays.stream(row3).iterator());
        tableBuilder.endTable();
        assertEquals(writer.toString(), "<html><body><table><caption>people</caption><thead><tr><th>First</th><th>Last</th></tr></thead><tbody><tr><td>Allen</td><td>Holub</td></tr><tr><td>Ichabod</td><td>Crane</td></tr><tr><td>Rip</td><td>VanWinkle</td></tr></tbody></table></body></html>");
        writer.close();
    }
}
