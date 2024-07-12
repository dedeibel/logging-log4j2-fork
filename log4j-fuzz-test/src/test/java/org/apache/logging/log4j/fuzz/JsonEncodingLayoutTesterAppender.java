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

import static org.apache.logging.log4j.fuzz.FuzzingUtil.assertValidJson;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.Serializable;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Core;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

/**
 * A layout testing appender validating the JSON formatting of the layout's output.
 */
@Plugin(
        name = JsonEncodingLayoutTesterAppender.PLUGIN_NAME,
        category = Core.CATEGORY_NAME,
        elementType = Appender.ELEMENT_TYPE)
public final class JsonEncodingLayoutTesterAppender extends AbstractAppender {

    public static final String PLUGIN_NAME = "JsonEncodingLayoutTesterAppender";

    private JsonEncodingLayoutTesterAppender(final String name, final Layout<? extends Serializable> layout) {
        super(name, null, layout, true, null);
        // Guard `PLUGIN_NAME` against copy-paste mistakes
        assertThat(PLUGIN_NAME).isEqualTo(getClass().getSimpleName());
    }

    @PluginFactory
    public static JsonEncodingLayoutTesterAppender createAppender(
            final @PluginAttribute("name") String name, final @PluginElement("layout") Layout<?> layout) {
        return new JsonEncodingLayoutTesterAppender(name, layout);
    }

    @Override
    public void append(final LogEvent event) {
        final byte[] jsonBytes;
        try {
            jsonBytes = getLayout().toByteArray(event);
        } catch (final Exception ignored) {
            // We are inspecting unexpected access.
            // Hence, event encoding failures are not of interest.
            return;
        }
        assertValidJson(jsonBytes);
    }
}
