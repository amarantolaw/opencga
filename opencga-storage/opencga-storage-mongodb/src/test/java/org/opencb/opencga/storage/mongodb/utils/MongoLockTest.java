/*
 * Copyright 2015-2016 OpenCB
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opencb.opencga.storage.mongodb.utils;

import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.opencb.commons.datastore.core.QueryOptions;
import org.opencb.commons.datastore.mongodb.MongoDBCollection;
import org.opencb.commons.datastore.mongodb.MongoDataStoreManager;
import org.opencb.opencga.storage.mongodb.variant.MongoVariantStorageManagerTestUtils;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.opencb.opencga.storage.core.variant.VariantStorageManagerTestUtils.DB_NAME;

/**
 * Created on 13/06/16
 *
 * @author Jacobo Coll &lt;jacobo167@gmail.com&gt;
 */
public class MongoLockTest implements MongoVariantStorageManagerTestUtils{

    private MongoLock mongoLock;

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private MongoDBCollection collection;

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        clearDB(DB_NAME);
        MongoDataStoreManager mongoDataStoreManager = getMongoDataStoreManager(DB_NAME);
        collection = mongoDataStoreManager.get(DB_NAME).getCollection("locks");
        mongoLock = new MongoLock(collection);
    }

    @Test
    public void testLock() throws Exception {
        int lockId = 1;
        insertDocument(lockId);
        for (int i = 0; i < 10; i++) {
            System.out.println("i = " + i);

            long lock = mongoLock.lock(lockId, 10, 10);
            System.out.println("lock = " + lock);

            mongoLock.unlock(lockId, lock);
        }
    }

    @Test
    public void testConcurrentLock() throws Exception {
        int lockId = 2;
        insertDocument(lockId);

        AtomicInteger counter = new AtomicInteger(0);
        Set<String> threadWithLock = Collections.synchronizedSet(new HashSet<>());

        int nThreads = 20;
        ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
        List<Future> futures = new ArrayList<>();
        for (int t = 0; t < nThreads; t++) {
            futures.add(executorService.submit(() -> {
                try {
                    for (int i = 0; i < 5; i++) {
                        System.out.println("i = " + i);

                        long lock = mongoLock.lock(lockId, 1000, 20000);
                        System.out.println("[" + Thread.currentThread().getName() + "] Enter LOCK");
                        assertEquals(threadWithLock.toString(), 0, threadWithLock.size());
                        threadWithLock.add(Thread.currentThread().getName());
                        assertEquals(threadWithLock.toString(), 1, threadWithLock.size());
                        int value = counter.addAndGet(1);
                        Thread.sleep(100);
                        assertEquals(threadWithLock.toString(), 1, threadWithLock.size());
                        assertEquals(threadWithLock.toString(), value, counter.get());
                        threadWithLock.remove(Thread.currentThread().getName());
                        System.out.println("lock = " + lock);
                        System.out.println("[" + Thread.currentThread().getName() + "] Exit LOCK");

                        mongoLock.unlock(lockId, lock);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }));
        }
        executorService.shutdown();
        executorService.awaitTermination(2000, TimeUnit.SECONDS);

        for (Future future : futures) {
            assertTrue(future.isDone());
            future.get();
        }

    }

    @Test
    public void testLockAndLock() throws Exception {
        int lockId = 3;
        insertDocument(lockId);

        long lock = mongoLock.lock(lockId, 1000, 2000);
        System.out.println("lock = " + lock);

        thrown.expect(TimeoutException.class);

        mongoLock.lock(lockId, 1000, 1000);

    }

    @Test
    public void testLockAfterExpiring() throws Exception {
        int lockId = 4;
        insertDocument(lockId);
        long lock = mongoLock.lock(lockId, 1000, 1000);
        System.out.println("lock = " + lock);

        Thread.sleep(2000);
        System.out.println("Expired lock = " + lock);

        lock = mongoLock.lock(lockId, 1000, 1000);
        System.out.println("Unlock = " + lock);

        mongoLock.unlock(lockId, lock);

    }

    public void insertDocument(Object id) {
        collection.update(new Document("_id", id),
                Updates.set("_id", id),
                new QueryOptions(MongoDBCollection.UPSERT, true));

    }
}