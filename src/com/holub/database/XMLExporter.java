package com.holub.database;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;

public class XMLExporter implements Table.Exporter {
    private final Writer out;
    private int width;
    private int height;
    private String[] columnNames;
    private String tableName;

    public XMLExporter( Writer out ) {
        this.out = out;
    }

    @Override
    public void startTable() throws IOException {
        out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
    }

    @Override
    public void storeMetadata(String tableName,
                              int width,
                              int height,
                              Iterator columnNames) throws IOException {
        this.width = width;
        this.height = height;
        this.tableName = tableName;
        out.write(tableName == null ? "<anonymous>" : "<" + tableName + ">");
        out.write("\n");
        this.columnNames = new String[width];
        for (int i = 0; columnNames.hasNext();) {
            this.columnNames[i++] = columnNames.next().toString();
        }
    }

    @Override
    public void storeRow(Iterator data) throws IOException {
        int columnIdx = 0;
        out.write("\t<row>\n");
        while (data.hasNext())
        {
            Object datum = data.next();
            if( datum != null ) {
                out.write("\t\t<" + columnNames[columnIdx] + ">");
                out.write(datum.toString());
                out.write("</" + columnNames[columnIdx] + ">");
            }
            else {
                out.write("\t\t<" + columnNames[columnIdx] + ">");
                out.write("</" + columnNames[columnIdx] + ">");
            }

            out.write("\n");
            if (columnIdx < width) {
                columnIdx += 1;
            }
        }
        out.write("\t</row>\n");
    }

    @Override
    public void endTable() throws IOException {
        out.write("</" + this.tableName + ">");
    }
}
