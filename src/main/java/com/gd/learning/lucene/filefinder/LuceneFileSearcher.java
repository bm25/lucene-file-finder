package com.gd.learning.lucene.filefinder;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LuceneFileSearcher {
    private Analyzer analyzer;
    private String indexDirectoryPath;

    public LuceneFileSearcher(Analyzer analyzer, String indexDirectoryPath) {
        this.analyzer = analyzer;
        this.indexDirectoryPath = indexDirectoryPath;
    }

    public List<Document> searchFiles(String inField, String queryString) throws ParseException, IOException {
        Query query = new QueryParser(inField, analyzer).parse(queryString);
        Directory indexDirectory = FSDirectory.open(Paths.get(indexDirectoryPath));
        IndexReader indexReader = DirectoryReader.open(indexDirectory);
        IndexSearcher searcher = new IndexSearcher(indexReader);
        TopDocs topDocs = searcher.search(query, 10);

        List<Document> result = new ArrayList<>();
        for (ScoreDoc scoreDoc: topDocs.scoreDocs){
            result.add(searcher.doc(scoreDoc.doc));
        }

        return result;
    }
}
