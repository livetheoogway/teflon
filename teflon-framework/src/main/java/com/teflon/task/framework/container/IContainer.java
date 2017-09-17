package com.teflon.task.framework.container;

/**
 * @author tushar.naik
 * @version 1.0  16/09/17 - 1:37 AM
 */
public interface IContainer<Key, Item> {
    void register(Key key, Item item);

    Item get(String name);
}

