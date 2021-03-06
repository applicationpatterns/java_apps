package org.ap.compare.lib.model;

import lombok.Builder;
import lombok.Data;

import java.util.*;

@Data
@Builder
public class ComparableNode {
    // fields are a map of key value pairs
    // each key is a string (field name)
    // each value is either a string or ComparableNode or Map<String, String> (for custom fields)
    Map<String, Object> fields;
    Map<String, Object> primaryKeys;

    public ComparableNodeResult compare(ComparableNode baseNode) {
        List<ComparableFieldResult> fieldResults = new ArrayList<>();
        Map<String, List<ComparableNodeResult>> childNodesResultsMap = new HashMap<>();
        for (String thisFieldKey : fields.keySet()) {
            if (!baseNode.getFields().containsKey(thisFieldKey)) {
                fieldResults.add(ComparableFieldResult.builder()
                        .fieldKey(thisFieldKey)
                        .fieldResult(CompareResult.EXTRA)
                        .thisFieldValue(fields.get(thisFieldKey).toString())
                        .build());
            } else {
                Object thisField = fields.get(thisFieldKey);
                Object baseField = baseNode.getFields().get(thisFieldKey);
                if (thisField == null && baseField == null) {
                    // nothing really to compare, so just skip this
                } else if (thisField == null && baseField != null) {
                    fieldResults.add(ComparableFieldResult.builder()
                            .fieldKey(thisFieldKey)
                            .fieldResult(CompareResult.DIFFERENT)
                            .thisFieldValue(null)
                            .baseFieldValue(baseField.toString())
                            .build());
                } else if (thisField != null && baseField == null) {
                    fieldResults.add(ComparableFieldResult.builder()
                            .fieldKey(thisFieldKey)
                            .fieldResult(CompareResult.DIFFERENT)
                            .thisFieldValue(thisField.toString())
                            .baseFieldValue(null)
                            .build());
                } else {
                    if (thisField.getClass().getCanonicalName() != baseField.getClass().getCanonicalName()) {
                        // thisField has a different type than baseField
                        fieldResults.add(ComparableFieldResult.builder()
                                .fieldKey(thisFieldKey)
                                .fieldResult(CompareResult.DIFFERENT)
                                .thisFieldValue(thisField.toString())
                                .baseFieldValue(baseField.toString())
                                .notes("different types " + thisField.getClass().getCanonicalName() + " vs " + baseField.getClass().getCanonicalName())
                                .build());
                    } else {
                        if (thisField instanceof String || thisField instanceof Long || thisField instanceof Integer) {
                            // both are strings, so lets compare the values
                            if (!thisField.equals(baseField)) {
                                fieldResults.add(ComparableFieldResult.builder()
                                        .fieldKey(thisFieldKey)
                                        .fieldResult(CompareResult.DIFFERENT)
                                        .thisFieldValue(thisField.toString())
                                        .baseFieldValue(baseField.toString())
                                        .build());
                            }
                        } else if (thisField instanceof List<?>) {
                            List<ComparableNode> thisNodeList = (List<ComparableNode>)thisField;
                            List<ComparableNode> baseNodeList = (List<ComparableNode>)baseField;
                            // ideally we should be checking if the list is of ComparableNode
                            // for now, just assume that (eventually this could bite us)
                            // let's see how long we can ride this
                            List<ComparableNodeResult> comparableNodeResults = ComparableNode.compareList(thisNodeList, baseNodeList);
                            childNodesResultsMap.put(thisFieldKey, comparableNodeResults);
                        } else if (thisField instanceof ComparableNode thisFieldObject) {
                            List<ComparableNodeResult> comparableNodeResults = new ArrayList<>();
                            ComparableNode baseFieldObject = (ComparableNode) baseField;
                            ComparableNodeResult nodeResult = thisFieldObject.compare(baseFieldObject);
                            comparableNodeResults.add(nodeResult);
                            childNodesResultsMap.put(thisFieldKey, comparableNodeResults);
                        } else if (thisField instanceof Map) {
                            // TODO compare object
                            Map<String, String> thisFieldMap = (Map<String, String>) thisField;
                            Map<String, String> baseFieldMap = (Map<String, String>) baseField;
                            for (String thisFieldMapKey : thisFieldMap.keySet()) {
                                if (!baseFieldMap.containsKey(thisFieldMapKey)) {
                                    fieldResults.add(ComparableFieldResult.builder()
                                            .fieldKey(thisFieldMapKey)
                                            .fieldResult(CompareResult.EXTRA)
                                            .thisFieldValue(thisFieldMap.get(thisFieldMapKey))
                                            .build());
                                } else {
                                    String thisFieldMapValue = thisFieldMap.get(thisFieldMapKey);
                                    String baseFieldMapValue = baseFieldMap.get(thisFieldMapKey);
                                    if (!thisFieldMapValue.equals(baseFieldMapValue)) {
                                        fieldResults.add(ComparableFieldResult.builder()
                                                .fieldKey(thisFieldMapKey)
                                                .fieldResult(CompareResult.DIFFERENT)
                                                .thisFieldValue((String) thisFieldMapValue)
                                                .baseFieldValue((String) baseFieldMapValue)
                                                .build());
                                    }
                                }
                            }
                            for (String baseFieldMapKey : baseFieldMap.keySet()) {
                                if (!thisFieldMap.containsKey(baseFieldMapKey)) {
                                    fieldResults.add(ComparableFieldResult.builder()
                                            .fieldKey(baseFieldMapKey)
                                            .fieldResult(CompareResult.MISSING)
                                            .baseFieldValue(baseFieldMap.get(baseFieldMapKey))
                                            .build());
                                }
                            }

                        } else {
                            // this is bad type, this should not happen
                        }
                    }
                }
            }
        }
        for (String baseFieldKey : baseNode.fields.keySet()) {
            if (!this.getFields().containsKey(baseFieldKey)) {
                fieldResults.add(ComparableFieldResult.builder()
                        .fieldKey(baseFieldKey)
                        .fieldResult(CompareResult.MISSING)
                        .baseFieldValue(baseNode.fields.get(baseFieldKey).toString())
                        .build());
            }
        }
        ComparableNodeResult comparableNodeResult = ComparableNodeResult.builder().build();
        comparableNodeResult.setNode(this);
        comparableNodeResult.setNodeResult(fieldResults.size() == 0 ? CompareResult.SAME : CompareResult.DIFFERENT);
        comparableNodeResult.setFieldResults(fieldResults);
        comparableNodeResult.setChildNodeResults(childNodesResultsMap);

        return comparableNodeResult;
    }

    public static List<ComparableNodeResult> compareList(List<ComparableNode> thisList, List<ComparableNode> baseList) {
        List<ComparableNodeResult> comparableNodeResults = new ArrayList<>();
        for (ComparableNode thisObject : thisList) {
            ComparableNode foundMatchingBaseObject = null;
            for (ComparableNode baseObject : baseList) {
                if (baseObject.getPrimaryKeys().equals(thisObject.getPrimaryKeys())) {
                    System.out.println("found match for " + thisObject.getPrimaryKeys());
                    foundMatchingBaseObject = baseObject;
                    break;
                }
            }
            if (foundMatchingBaseObject == null) {
                // this object does not exist in base
                // extra compared to base
                ComparableNodeResult nodeResult = ComparableNodeResult.builder().build();
                nodeResult.setNode(thisObject);
                nodeResult.setNodeResult(CompareResult.EXTRA);
                comparableNodeResults.add(nodeResult);
            } else {
                ComparableNodeResult nodeResult = thisObject.compare(foundMatchingBaseObject);
                comparableNodeResults.add(nodeResult);
            }
        }
        for (ComparableNode baseObject : baseList) {
            ComparableNode foundMatchingThisObject = null;
            for (ComparableNode thisObject : thisList) {
                if (thisObject.getPrimaryKeys().equals(baseObject.getPrimaryKeys())) {
                    System.out.println("found match for " + baseObject.getPrimaryKeys());
                    foundMatchingThisObject = thisObject;
                    break;
                }
            }
            if (foundMatchingThisObject == null) {
                // base object does not exist in this list
                // this object missing to base
                ComparableNodeResult nodeResult = ComparableNodeResult.builder().build();
                nodeResult.setNode(baseObject);
                nodeResult.setNodeResult(CompareResult.MISSING);
                comparableNodeResults.add(nodeResult);
            }
        }
        return comparableNodeResults;
    }
}
