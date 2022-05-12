package org.ap.libcomp.model;

public enum FieldResult {
    DIFFERENT, // value is different from base
    MISSING, // value is missing in this compared to the base
    EXTRA // value is extra in this compared to the base
}
