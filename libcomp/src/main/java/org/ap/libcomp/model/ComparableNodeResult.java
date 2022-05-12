package org.ap.libcomp.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ComparableNodeResult {
    ComparableNode node;
    CompareResult nodeResult;
    List<ComparableFieldResult> fieldResults;
}
