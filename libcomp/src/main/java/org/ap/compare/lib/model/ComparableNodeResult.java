package org.ap.compare.lib.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class ComparableNodeResult {
    ComparableNode node;
    CompareResult nodeResult;
    List<ComparableFieldResult> fieldResults;
    Map<String, List<ComparableNodeResult>> childNodeResults;
}
