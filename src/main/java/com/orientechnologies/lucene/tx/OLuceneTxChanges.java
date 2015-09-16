/*
 *
 *  * Copyright 2014 Orient Technologies.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.orientechnologies.lucene.tx;

import com.orientechnologies.lucene.engine.OLuceneIndexEngine;
import com.orientechnologies.orient.core.db.record.OIdentifiable;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;

import java.io.IOException;

/**
 * Created by Enrico Risa on 15/09/15.
 */
public class OLuceneTxChanges {

    IndexWriter writer;
    private OLuceneIndexEngine engine;
    private long deletedDoc = 0;

    public OLuceneTxChanges(OLuceneIndexEngine engine, IndexWriter writer) {
        this.writer = writer;
        this.engine = engine;
    }

    public void put(Object key, OIdentifiable value, Document doc) throws IOException {
        writer.addDocument(doc);
    }

    public void remove(Object key, OIdentifiable value) throws IOException {

        if (value.getIdentity().isTemporary()) {
            writer.deleteDocuments(engine.deleteQuery(key, value));
        } else {
            deletedDoc++;
        }
    }

    public IndexSearcher searcher() {
        // TODO optimize
        try {
            return new IndexSearcher(DirectoryReader.open(writer, true));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public long numDocs() {
        return searcher().getIndexReader().numDocs() - deletedDoc;
    }
}
