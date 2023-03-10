package org.example.utils;

import lombok.Getter;
import lombok.Setter;

/**
 * Board class
 * @author furkangunes
 * @version 1.0.0
 */
@Getter
@Setter
public class IndexValue {
    /**
     * indexvalue
     */
    private int index;
    private double value;

    public IndexValue() {

    }

    public IndexValue(int index, double value) {
        this.index = index;
        this.value = value;
    }

    /**
     * @return indexin kopyasini yaratir.
     */
    public IndexValue makeCopy() {
        IndexValue clone = new IndexValue();
        clone.setValue(value);
        clone.setIndex(index);
        return clone;
    }

    /**
     * @param rhs
     * @return Gelen objenin indexe degere olan esitligini kontrol eder.
     */
    @Override
    public boolean equals(Object rhs) {
        if (rhs != null && rhs instanceof IndexValue) {
            IndexValue rhs2 = (IndexValue) rhs;
            return index == rhs2.index && value == rhs2.value;
        }
        return false;
    }

    public boolean isValid() {
        return index != -1;
    }

}
