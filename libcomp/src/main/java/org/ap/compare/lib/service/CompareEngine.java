package org.ap.compare.lib.service;

import org.ap.compare.lib.model.CompareDataProvider;
import org.ap.compare.lib.model.ComparableNodeResult;
import org.ap.compare.lib.model.ComparePair;

import java.util.ArrayList;
import java.util.List;

/**
 * we have a need to compare data all the time.
 * compare new vs old
 * compare modern vs legacy
 */
public class CompareEngine {
    private CompareDataProvider compareDataProvider;
    private CompareNodeService compareNodeService;

    public CompareEngine(CompareDataProvider currentDataProvider) {
        this.compareDataProvider = currentDataProvider;
        this.compareNodeService = new CompareNodeService();
    }

    public List<ComparableNodeResult> compare() {
        List<ComparableNodeResult> comparableNodeResults = new ArrayList<>();
        while (compareDataProvider.hasNextComparePair()) {
            ComparePair comparePair = compareDataProvider.getNextComparePair();
            ComparableNodeResult comparableNodeResult = compareNodeService.compareNode(comparePair);
            comparableNodeResults.add(comparableNodeResult);
        }
        return comparableNodeResults;
    }
}
