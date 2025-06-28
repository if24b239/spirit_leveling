package com.rain.spiritleveling.util;

public class CachedValue<T> {
    private T value;
    private boolean isValid = false;

    public CachedValue(T value) {
        this.value = value;
    }

    public T getValue() {
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
