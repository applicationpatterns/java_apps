package org.ap.compare.lib.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ComparableFieldResult {
    String fieldKey;
    CompareResult fieldResult;
    String thisFieldValue;
    String baseFieldValue;
    String notes;
}
