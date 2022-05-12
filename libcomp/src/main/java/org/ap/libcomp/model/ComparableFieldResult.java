package org.ap.libcomp.model;

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
