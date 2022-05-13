package org.ap.compare.lib.model;

public enum CompareResult {
    SAME, // value is the same as the base
    DIFFERENT, // value is different from base
    MISSING, // value is missing in this compared to the base
    EXTRA // value is extra in this compared to the base
}
