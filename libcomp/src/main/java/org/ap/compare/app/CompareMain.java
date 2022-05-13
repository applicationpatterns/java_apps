package org.ap.compare.app;

import org.ap.compare.lib.model.CompareDataProvider;
import org.ap.compare.lib.service.CompareEngine;
import org.ap.compare.lib.service.CompareNodeService;

public class CompareMain {
    public static void main(String[] args) {
        System.out.println("hello world");

        CompareDataProvider currentDataProvider= null;
        CompareNodeService compareNodeService = null;
        CompareEngine compareEngine = new CompareEngine(currentDataProvider);
    }
}

