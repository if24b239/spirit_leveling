package com.rain.spiritleveling.util;

public class CachedValue<T> {
    private static final int MAX_COUNT = 100;

    private T value;
    private boolean isValid = false;
    private int count = 0;

    public CachedValue(T value) {
        this.value = value;
    }

    public T getValue() {
        if (++count > MAX_COUNT)
            markStale();
        return value;
    }

    public void setValue(T value) {
        this.value = value;
        this.isValid = true;
    }

    public boolean isValid() {
        return isValid;
    }

    public void markStale() {
        isValid = false;
    }
}
