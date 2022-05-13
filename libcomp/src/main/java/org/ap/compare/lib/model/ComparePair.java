package org.ap.compare.lib.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ComparePair {
    ComparableNode currentNode;
    ComparableNode baseNode;
}
