package com.rain.spiritleveling.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;

public class UniqueQueue<T> {
    private final LinkedHashSet<T> set = new LinkedHashSet<>();

    // returns false if it is already in the queue.
    public boolean enqueue(T e) {
        return set.add(e);
    }

    // false if even one is not unique
    public boolean enqueueAll(Collection<T> allE) {
        boolean success = true;
        for (T e : allE) {
            if (!enqueue(e))
                success = false;
        }

        return success;
    }

    // returns null if no elements in queue otherwise deletes the element and returns it
    public T dequeue() {
        Iterator<T> it = set.iterator();
        if(!it.hasNext()) return null;
        T item = it.next();
        it.remove();
        return item;
    }

    public boolean isEmpty() {
        return set.isEmpty();
    }
}
