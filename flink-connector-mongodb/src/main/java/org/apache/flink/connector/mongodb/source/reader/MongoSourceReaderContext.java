/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.flink.connector.mongodb.source.reader;

import org.apache.flink.annotation.Internal;
import org.apache.flink.api.connector.source.SourceEvent;
import org.apache.flink.api.connector.source.SourceReaderContext;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.connector.mongodb.source.reader.split.MongoScanSourceSplitReader;
import org.apache.flink.metrics.groups.SourceReaderMetricGroup;
import org.apache.flink.util.UserCodeClassLoader;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * A wrapper class that wraps {@link SourceReaderContext} for sharing message between {@link
 * MongoSourceReader} and {@link MongoScanSourceSplitReader}.
 */
@Internal
public class MongoSourceReaderContext implements SourceReaderContext {

    private final SourceReaderContext readerContext;
    private final AtomicInteger readCount = new AtomicInteger(0);
    private final int limit;

    public MongoSourceReaderContext(SourceReaderContext readerContext, int limit) {
        this.readerContext = readerContext;
        this.limit = limit;
    }

    @Override
    public SourceReaderMetricGroup metricGroup() {
        return readerContext.metricGroup();
    }

    @Override
    public Configuration getConfiguration() {
        return readerContext.getConfiguration();
    }

    @Override
    public String getLocalHostName() {
        return readerContext.getLocalHostName();
    }

    @Override
    public int getIndexOfSubtask() {
        return readerContext.getIndexOfSubtask();
    }

    @Override
    public void sendSplitRequest() {
        readerContext.sendSplitRequest();
    }

    @Override
    public void sendSourceEventToCoordinator(SourceEvent sourceEvent) {
        readerContext.sendSourceEventToCoordinator(sourceEvent);
    }

    @Override
    public UserCodeClassLoader getUserCodeClassLoader() {
        return readerContext.getUserCodeClassLoader();
    }

    public AtomicInteger getReadCount() {
        return readCount;
    }

    public boolean isLimitPushedDown() {
        return limit > 0;
    }

    public boolean isOverLimit() {
        return limit > 0 && readCount.get() >= limit;
    }

    public int getLimit() {
        return limit;
    }
}
