package org.ap.libcomp.model;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class ComparableNode {
    Map<String, Object> fields;
    Map<String, String> primaryKeys;

    public ComparableNodeResult compare(ComparableNode baseNode) {
        List<ComparableFieldResult> fieldResults = new ArrayList<>();
        for (String thisFieldKey : fields.keySet()) {
            if (!baseNode.getFields().containsKey(thisFieldKey)) {
                fieldResults.add(ComparableFieldResult.builder().fieldKey(thisFieldKey).fieldResult(FieldResult.EXTRA).thisFieldValue(fields.get(thisFieldKey).toString()).build());
            } else {
                Object thisField = fields.get(thisFieldKey);
                Object baseField = baseNode.getFields().get(thisFieldKey);
                if (thisField.getClass().getCanonicalName() != baseField.getClass().getCanonicalName()) {
                    // thisField has a different type than baseField
                    fieldResults.add(ComparableFieldResult.builder()
                            .fieldKey(thisFieldKey)
                            .fieldResult(FieldResult.DIFFERENT)
                            .thisFieldValue(thisField.toString())
                            .baseFieldValue(baseField.toString())
                            .notes("different types")
                            .build());
                } else {
                    if (thisField instanceof String) {
                        // both are strings, so lets compare the values
                        if (!thisField.equals(baseField)) {
                            fieldResults.add(ComparableFieldResult.builder()
                                    .fieldKey(thisFieldKey)
                                    .fieldResult(FieldResult.DIFFERENT)
                                    .thisFieldValue((String) thisField)
                                    .baseFieldValue((String) baseField)
                                    .build());
                        }
                    } else if (thisField instanceof ComparableNode) {
                        // TODO compare object
                    } else {
                        // this is bad type, this should not happen
                    }
                }
            }
        }
        for (String baseFieldKey : baseNode.fields.keySet()) {
            if (!this.getFields().containsKey(baseFieldKey)) {
                fieldResults.add(ComparableFieldResult.builder().fieldKey(baseFieldKey).fieldResult(FieldResult.MISSING).baseFieldValue(baseNode.fields.get(baseFieldKey).toString()).build());
            }
        }
        ComparableNodeResult comparableNodeResult = ComparableNodeResult.builder().build();
        comparableNodeResult.setNode(this);
        comparableNodeResult.setResults(fieldResults);

//        for each thisField in this.fields
//            if thisField exists in base.fields
//                if thisField type != baseFieldType
//                    // DONE thisField type is different from the baseField
//                else if thisField type == string
//                    // both are strings, so lets compare values
//                    if thisField value is same as baseField value
//                        // all good
//                    else
//                        // DONE thisField is different from baseField
//                else if thisField type == ComparableNode
//                    TODO compareObject(thisField.value, baseField.value)
//                else
//                    // unknown field type
//                end if
//            else
//                // DONE thisField is extra (compared to the base)
//        end for
//        for each baseField in base.fields
//            if baseField does not exist in this.fields
//                // this field is missing (compared to the base)
//        end for

        return comparableNodeResult;
    }
}
