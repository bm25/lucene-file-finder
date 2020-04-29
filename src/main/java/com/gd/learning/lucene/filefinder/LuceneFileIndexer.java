package com.gd.learning.lucene.filefinder;


import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

public class LuceneFileIndexer {
    private Analyzer analyzer;
    private String indexDirectoryPath;
    private IndexWriter indexWriter;

    public LuceneFileIndexer(Analyzer analyzer, String indexDirectoryPath) throws IOException {
        this.analyzer = analyzer;
        this.indexDirectoryPath = indexDirectoryPath;
        Directory indexDirectory = FSDirectory.open(Paths.get(indexDirectoryPath));
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        indexWriter = new IndexWriter(indexDirectory, indexWriterConfig);
    }

    public void close() throws IOException {
        indexWriter.close();
    }

    public Document getDocument(File file) throws IOException {
        Document document = new Document();

        FileReader fileReader = new FileReader(file);
        document.add(new TextField("contents", fileReader));
        document.add(new StringField("path", file.getPath(), Field.Store.YES));
        document.add(new StringField("filepath", file.getPath(), Field.Store.YES));

        return document;
    }

    public void indexFile(File file) throws IOException{
        Document document = getDocument(file);
        indexWriter.addDocument(document);
    }

    public int createIndex(String dataDirPath) throws IOException {
        //get all files in the data directory
        File[] files = new File(dataDirPath).listFiles();

        for (File file : files) {
            if(!file.isDirectory()
                    && !file.isHidden()
                    && file.exists()
                    && file.canRead()
            ){
                indexFile(file);
            }
        }
        return indexWriter.numDocs();
    }
}
