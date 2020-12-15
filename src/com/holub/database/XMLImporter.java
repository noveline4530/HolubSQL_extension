package com.holub.database;

import com.holub.tools.ArrayIterator;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class XMLImporter implements Table.Importer {
    private BufferedReader in;
    private ArrayList<String>  columnNames;
    private String             tableName;
    private LinkedList<String> buffer;

    public XMLImporter(Reader in)
    {
        columnNames = new ArrayList<>();
        buffer = new LinkedList<>();
        this.in = in instanceof BufferedReader
                ? (BufferedReader)in
                : new BufferedReader(in)
        ;
    }

    @Override
    public void startTable() throws IOException {
    	String XMLInitialLine = in.readLine().trim();
        String nameTag = in.readLine().trim();
        tableName = nameTag.substring(1, nameTag.length()-1);

        String line;
        if ((line = in.readLine().trim()).equals("<row>")) {
            while (!(line = in.readLine().trim()).equals("</row>")) {
                String columnName = "";
                for (int i = 1; i < line.length(); i++) {
                    if (line.charAt(i) == '>') {
                        buffer.offer(line.substring(i+1));
                        break;
                    }
                    else {
                        columnName += line.charAt(i);
                    }
                }
                columnNames.add(columnName);
            }
        }
    }

    @Override
    public String loadTableName() throws IOException {
        return tableName;
    }

    @Override
    public int loadWidth() throws IOException {
        return columnNames.size();
    }

    @Override
    public Iterator loadColumnNames() throws IOException {
        return new ArrayIterator(columnNames.toArray());
    }

    @Override
    public Iterator loadRow() throws IOException {
        String[] rowContent = new String[columnNames.size()];

        if (!buffer.isEmpty()) {
            while (!buffer.isEmpty()) {
                for (int i=0; i < rowContent.length; i++) {
                    String line = buffer.poll();
                    int endTagIndex = line.indexOf('<');
                    String cell = line.substring(0, endTagIndex);
                    rowContent[i] = cell;
                }
            }
        }
        else {
            String line = in.readLine().trim();
            if (line != null && line.equals("<row>")) {
                int idx = 0;
                while (!(line = in.readLine().trim()).equals("</row>")) {
                    int tagEndIndex = line.indexOf(">") + 1;
                    int tagStartIndex = line.indexOf("</");
                    String cell = line.substring(tagEndIndex, tagStartIndex);
                    rowContent[idx++] = cell;
                }
            }
            else { return null; }
        }
        return new ArrayIterator( rowContent );
    }

    @Override
    public void endTable() throws IOException {}
}
