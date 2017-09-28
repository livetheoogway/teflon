package com.teflon.task.framework.container;

import java.util.Set;

/**
 * A container that allows registration of items againsta a key, and its retrieval
 *
 * @author tushar.naik
 * @version 1.0  16/09/17 - 1:37 AM
 */
public interface IContainer<Key, Item> {

    /**
     * register item against a key
     *
     * @param key  key
     * @param item item
     */
    void register(Key key, Item item);

    /**
     * @return the item registered to the key
     */
    Item get(Key key);

    /**
     * @return all registered keys
     */
    Set<Key> keys();
}

