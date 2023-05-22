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
package org.apache.logging.log4j.plugins.di.resolver;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.function.Supplier;

import org.apache.logging.log4j.plugins.di.InstanceFactory;
import org.apache.logging.log4j.plugins.di.NotInjectableException;
import org.apache.logging.log4j.plugins.di.spi.ResolvableKey;
import org.apache.logging.log4j.plugins.model.PluginType;

/**
 * Factory resolver for {@link PluginType}. Keys must have a namespace defined as default namespace plugins are
 * not indexed and therefore have no plugin type metadata.
 */
public class PluginTypeFactoryResolver extends AbstractPluginFactoryResolver {
    @Override
    protected boolean supportsType(final Type rawType, final Type... typeArguments) {
        return rawType == PluginType.class && typeArguments.length == 1;
    }

    @Override
    public Supplier<?> getFactory(final ResolvableKey<?> resolvableKey, final InstanceFactory instanceFactory) {
        final ParameterizedType type = (ParameterizedType) resolvableKey.getType();
        final String namespace = resolvableKey.getNamespace();
        final Type requestedType = type.getActualTypeArguments()[0];
        return () -> Plugins.streamPluginTypesMatching(instanceFactory, namespace, requestedType)
                .findFirst()
                .orElseThrow(() -> new NotInjectableException(resolvableKey));
    }
}
