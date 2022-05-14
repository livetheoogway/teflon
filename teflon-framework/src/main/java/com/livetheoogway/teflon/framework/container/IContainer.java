/*
 * Copyright 2022. Live the Oogway, Tushar Naik
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and limitations
 * under the License.
 */

package com.livetheoogway.teflon.framework.container;

import java.util.Set;

/**
 * A container that allows registration of items against a key, and its retrieval
 *
 * @param <K> Key
 * @param <I> Item
 * @author tushar.naik
 * @version 1.0  16/09/17 - 1:37 AM
 */
public interface IContainer<K, I> {

    /**
     * register item against a key
     *
     * @param k key
     * @param i item
     */
    void register(K k, I i);

    /**
     * @param k key
     * @return the item registered to the key
     */
    I get(K k);

    /**
     * @return all registered keys
     */
    Set<K> keys();
}

