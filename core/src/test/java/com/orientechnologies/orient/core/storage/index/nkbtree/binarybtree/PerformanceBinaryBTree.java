package com.orientechnologies.orient.core.storage.index.nkbtree.binarybtree;

import com.ibm.icu.text.Collator;
import com.orientechnologies.common.util.ORawPair;
import com.orientechnologies.orient.core.config.OGlobalConfiguration;
import com.orientechnologies.orient.core.db.*;
import com.orientechnologies.orient.core.id.ORecordId;
import com.orientechnologies.orient.core.index.OCompositeKey;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.storage.impl.local.OAbstractPaginatedStorage;
import com.orientechnologies.orient.core.storage.impl.local.paginated.atomicoperations.OAtomicOperationsManager;
import com.orientechnologies.orient.core.storage.index.nkbtree.normalizers.KeyNormalizers;

import java.io.File;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Random;

public class PerformanceBinaryBTree {

    private static final int RUNS = 5;

    private OAtomicOperationsManager atomicOperationsManager;
    private BinaryBTree binaryBTree;
    private OrientDB orientDB;
    private String dbName;
    private OAbstractPaginatedStorage storage;

    private static KeyNormalizers keyNormalizers = new KeyNormalizers(Locale.ENGLISH, Collator.NO_DECOMPOSITION);
    private static OType[] types = new OType[]{OType.STRING};

    public static void main(String[] args) throws Exception {
        PerformanceBinaryBTree performanceTest = new PerformanceBinaryBTree();
        performanceTest.setUp();

        System.out.println(String.format("%-30s %-15s %-15s", "Test", "Execution Time (ms)", "Storage Size (bytes)"));

        long totalTime1 = 0, totalTime2 = 0, totalTime3 = 0;
        long totalStorage1 = 0, totalStorage2 = 0, totalStorage3 = 0;

        for (int i = 0; i < RUNS; i++) {
            long startTime = System.currentTimeMillis();
            performanceTest.testKeyPut();
            totalTime1 += System.currentTimeMillis() - startTime;
            totalStorage1 += performanceTest.getStorageSize();

            startTime = System.currentTimeMillis();
            performanceTest.testKeyPutRandomUniform();
            totalTime2 += System.currentTimeMillis() - startTime;
            totalStorage2 += performanceTest.getStorageSize();

            startTime = System.currentTimeMillis();
            performanceTest.testKeyPutRandomGaussian();
            totalTime3 += System.currentTimeMillis() - startTime;
            totalStorage3 += performanceTest.getStorageSize();
        }

        NumberFormat numberFormat = NumberFormat.getInstance();
        System.out.println(String.format("%-30s %-15s %-15s", "testKeyPut", numberFormat.format(totalTime1 / RUNS), numberFormat.format(totalStorage1 / RUNS)));
        System.out.println(String.format("%-30s %-15s %-15s", "testKeyPutRandomUniform", numberFormat.format(totalTime2 / RUNS), numberFormat.format(totalStorage2 / RUNS)));
        System.out.println(String.format("%-30s %-15s %-15s", "testKeyPutRandomGaussian", numberFormat.format(totalTime3 / RUNS), numberFormat.format(totalStorage3 / RUNS)));

        performanceTest.tearDown();
    }
    public void setUp() throws Exception {
        OGlobalConfiguration.DISK_CACHE_PAGE_SIZE.setValue(4);
        OGlobalConfiguration.SBTREE_MAX_KEY_SIZE.setValue(1024);

        final String buildDirectory = System.getProperty("buildDirectory", ".") + File.separator + BinaryBTree.class.getSimpleName();
        dbName = "binaryBTreeTest";
        final File dbDirectory = new File(buildDirectory, dbName);

        final OrientDBConfig config = OrientDBConfig.builder()
                .addConfig(OGlobalConfiguration.DISK_CACHE_PAGE_SIZE, 4)
                .addConfig(OGlobalConfiguration.SBTREE_MAX_KEY_SIZE, 1024)
                .build();

        orientDB = new OrientDB("plocal:" + buildDirectory, config);
        orientDB.create(dbName, ODatabaseType.PLOCAL);

        try (ODatabaseSession databaseDocumentTx = orientDB.open(dbName, "admin", "admin")) {
            storage = (OAbstractPaginatedStorage) ((ODatabaseInternal<?>) databaseDocumentTx).getStorage();
        }
        binaryBTree = new BinaryBTree(1, 1024, 16, storage, "singleBTree", ".bbt");
        atomicOperationsManager = storage.getAtomicOperationsManager();
        atomicOperationsManager.executeInsideAtomicOperation(null, binaryBTree::create);
    }

    public void tearDown() {
        orientDB.drop(dbName);
        orientDB.close();
    }
    public long getStorageSize() {
        String buildDirectory = System.getProperty("buildDirectory", ".")
                + File.separator
                + BinaryBTree.class.getSimpleName();
        File dbDirectory = new File(buildDirectory, dbName);

        return getDirectorySize(dbDirectory);
    }

    private static long getDirectorySize(File directory) {
        long length = 0;
        for (File file : directory.listFiles()) {
            if (file.isFile()) {
                length += file.length();
            } else {
                length += getDirectorySize(file);
            }
        }
        return length;
    }

    public void testKeyPut() throws Exception {
        final int keysCount = 1_000_000;

        for (int i = 0; i < keysCount; i++) {
            final int iteration = i;
            atomicOperationsManager.executeInsideAtomicOperation(null, atomicOperation -> {
                final String key = Integer.toString(iteration);
                binaryBTree.put(atomicOperation, stringToLexicalBytes(key), new ORecordId(iteration % 32000, iteration));
            });
        }
    }

    public void testKeyPutRandomUniform() throws Exception {
        final Random random = new Random();
        final int keysCount = 1_000_000;

        for (int i = 0; i < keysCount; i++) {
            atomicOperationsManager.executeInsideAtomicOperation(null, atomicOperation -> {
                int val = random.nextInt(Integer.MAX_VALUE);
                String key = Integer.toString(val);
                binaryBTree.put(atomicOperation, stringToLexicalBytes(key), new ORecordId(val % 32000, val));
            });
        }
    }

    public void testKeyPutRandomGaussian() throws Exception {
        Random random = new Random();
        final int keysCount = 1_000_000;

        for (int i = 0; i < keysCount; i++) {
            atomicOperationsManager.executeInsideAtomicOperation(null, atomicOperation -> {
                int val;
                do {
                    val = (int) (random.nextGaussian() * Integer.MAX_VALUE / 2 + Integer.MAX_VALUE);
                } while (val < 0);

                final String key = Integer.toString(val);
                binaryBTree.put(atomicOperation, stringToLexicalBytes(key), new ORecordId(val % 32000, val));
            });
        }
    }

    private static byte[] stringToLexicalBytes(final String value) {
        return keyNormalizers.normalize(new OCompositeKey(value), types);
    }
}
