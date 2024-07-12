/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.logging.log4j.fuzz;

import static org.apache.logging.log4j.fuzz.FuzzingUtil.createLayoutFuzzingLoggerContext;
import static org.apache.logging.log4j.fuzz.FuzzingUtil.logWithParams;
import static org.apache.logging.log4j.fuzz.FuzzingUtil.logWithoutParams;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;
import com.code_intelligence.jazzer.junit.FuzzTest;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

class PatternLayoutFuzzTest {

    private static LoggerContext LOGGER_CONTEXT;

    private static Logger LOGGER;

    @BeforeAll
    static void initLoggerContext() {
        LOGGER_CONTEXT =
                createLayoutFuzzingLoggerContext(LayoutTesterAppender.PLUGIN_NAME, configBuilder -> configBuilder
                        .newLayout("PatternLayout")
                        // Enforce using a single message-based converter, i.e., `MessagePatternConverter`
                        .addAttribute("pattern", "%m"));
        LOGGER = LOGGER_CONTEXT.getLogger(PatternLayoutFuzzTest.class);
    }

    @AfterAll
    static void stopLoggerContext() {
        LOGGER_CONTEXT.terminate();
    }

    @FuzzTest
    void fuzz_no_params(final FuzzedDataProvider dataProvider) {
        logWithoutParams(LOGGER, dataProvider);
    }

    @FuzzTest
    void fuzz_with_params(final FuzzedDataProvider dataProvider) {
        logWithParams(LOGGER, dataProvider);
    }
}
