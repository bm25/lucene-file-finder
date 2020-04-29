package com.gd.learning.lucene.filefinder;

import org.apache.lucene.analysis.standard.StandardAnalyzer;

import org.junit.Test;
import java.io.IOException;
import java.nio.file.Paths;

public class TestFileFinder {

    private static final String indexDir = Paths.get("").toAbsolutePath().toString() + "\\lucene\\index";
    private static final String dataDir = Paths.get("").toAbsolutePath().toString() + "\\lucene\\data";

    @Test
    public void testIndexer() throws IOException {
        LuceneFileIndexer indexer = new LuceneFileIndexer(new StandardAnalyzer(), indexDir);
        long startTime = System.currentTimeMillis();
        int numIndexed = indexer.createIndex(dataDir);
        long endTime = System.currentTimeMillis();

        indexer.close();


        System.out.println(numIndexed+" File indexed, time taken: "
                +(endTime-startTime)+" ms");
    }

}
