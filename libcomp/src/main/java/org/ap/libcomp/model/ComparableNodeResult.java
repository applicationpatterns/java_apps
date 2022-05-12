package org.ap.libcomp.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class ComparableNodeResult {
    ComparableNode node;
    List<ComparableFieldResult> results;
}
