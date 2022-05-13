package org.ap.compare.lib.model;

public interface CompareDataProvider {
    boolean hasNextComparePair();

    ComparePair getNextComparePair();
}
