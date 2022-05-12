import org.ap.libcomp.model.ComparableFieldResult;
import org.ap.libcomp.model.ComparableNode;
import org.ap.libcomp.model.ComparableNodeResult;
import org.ap.libcomp.model.CompareResult;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestComparableNode {

    @Test
    void testCompareObjectExactMatch() {
        ComparableNode currentComparableNode = null;
        {
            ComparableNode employee = ComparableNode.builder().build();
            Map<String, String> pk = Map.of("deptId", "10");
            Map<String, Object> fields = Map.ofEntries(
                    Map.entry("deptName", "HR"),
                    Map.entry("deptCode", "HR1234"),
                    Map.entry("location", "new york"),
                    Map.entry("country", "usa"),
                    Map.entry("employee", employee)
            );

            currentComparableNode = ComparableNode.builder().primaryKeys(pk).fields(fields).build();
        }

        ComparableNode baseComparableNode = null;
        {
            ComparableNode employee = ComparableNode.builder().build();
            Map<String, String> pk = Map.of("deptId", "10");
            Map<String, Object> fields = Map.ofEntries(
                    Map.entry("deptName", "HR"),
                    Map.entry("deptCode", "HR1234"),
                    Map.entry("location", "new york"),
                    Map.entry("country", "usa"),
                    Map.entry("employee", employee)
            );
            baseComparableNode = ComparableNode.builder().primaryKeys(pk).fields(fields).build();
        }
        ComparableNodeResult actualResults = currentComparableNode.compare(baseComparableNode);

        assertThat(actualResults.getFieldResults()).hasSize(0);
    }

    @Test
    void testCompareObjectDifferentValues() {
        ComparableNode currentComparableNode = null;
        {
            Map<String, String> pk = Map.of("deptId", "10");
            Map<String, Object> fields = Map.ofEntries(
                    Map.entry("deptName", "HR"),
                    Map.entry("deptCode", "HR1234"),
                    Map.entry("location", "new york"),
                    Map.entry("country", "usa")
            );

            currentComparableNode = ComparableNode.builder().primaryKeys(pk).fields(fields).build();
        }

        ComparableNode baseComparableNode = null;
        {
            Map<String, String> pk = Map.of("deptId", "10");
            Map<String, Object> fields = Map.ofEntries(
                    Map.entry("deptName", "HR"),
                    Map.entry("deptCode", "HR1235"),
                    Map.entry("location", "new orleans"),
                    Map.entry("country", "usa")
            );
            baseComparableNode = ComparableNode.builder().primaryKeys(pk).fields(fields).build();
        }
        ComparableNodeResult actualResults = currentComparableNode.compare(baseComparableNode);
        System.out.println("result=" + actualResults);

        List<ComparableFieldResult> expectedFieldResults = new ArrayList<>();
        ComparableFieldResult diff1 = ComparableFieldResult.builder().fieldKey("location").fieldResult(CompareResult.DIFFERENT).thisFieldValue("new york").baseFieldValue("new orleans").build();
        ComparableFieldResult diff2 = ComparableFieldResult.builder().fieldKey("deptCode").fieldResult(CompareResult.DIFFERENT).thisFieldValue("HR1234").baseFieldValue("HR1235").build();
        ComparableNodeResult expectedResults = ComparableNodeResult.builder().node(currentComparableNode).fieldResults(expectedFieldResults).build();
        assertThat(actualResults.getFieldResults()).hasSize(2);
        assertThat(actualResults.getFieldResults()).contains(diff1);
        assertThat(actualResults.getFieldResults()).contains(diff2);
    }

    @Test
    void testCompareObjectMissingField() {
        ComparableNode currentComparableNode = null;
        {
            Map<String, String> pk = Map.of("deptId", "10");
            Map<String, Object> fields = Map.ofEntries(
                    Map.entry("deptName", "HR"),
                    Map.entry("deptCode", "HR1234"),
                    Map.entry("location", "new york"),
                    Map.entry("country", "usa")
            );

            currentComparableNode = ComparableNode.builder().primaryKeys(pk).fields(fields).build();
        }

        ComparableNode baseComparableNode = null;
        {
            Map<String, String> pk = Map.of("deptId", "10");
            Map<String, Object> fields = Map.ofEntries(
                    Map.entry("deptName", "HR"),
                    Map.entry("deptCode", "HR1234"),
                    Map.entry("location", "new york"),
                    Map.entry("state", "oh"),
                    Map.entry("country", "usa")
            );
            baseComparableNode = ComparableNode.builder().primaryKeys(pk).fields(fields).build();
        }
        ComparableNodeResult actualResults = currentComparableNode.compare(baseComparableNode);

        List<ComparableFieldResult> expectedFieldResults = new ArrayList<>();
        ComparableFieldResult diff1 = ComparableFieldResult.builder().fieldKey("state").fieldResult(CompareResult.MISSING).baseFieldValue("oh").build();
        ComparableNodeResult expectedResults = ComparableNodeResult.builder().node(currentComparableNode).fieldResults(expectedFieldResults).build();
        assertThat(actualResults.getFieldResults()).hasSize(1);
        assertThat(actualResults.getFieldResults()).contains(diff1);
    }

    @Test
    void testCompareObjectExtraField() {
        ComparableNode currentComparableNode = null;
        {
            Map<String, String> pk = Map.of("deptId", "10");
            Map<String, Object> fields = Map.ofEntries(
                    Map.entry("deptName", "HR"),
                    Map.entry("deptCode", "HR1234"),
                    Map.entry("location", "new york"),
                    Map.entry("state", "oh"),
                    Map.entry("country", "usa")
            );

            currentComparableNode = ComparableNode.builder().primaryKeys(pk).fields(fields).build();
        }

        ComparableNode baseComparableNode = null;
        {
            Map<String, String> pk = Map.of("deptId", "10");
            Map<String, Object> fields = Map.ofEntries(
                    Map.entry("deptName", "HR"),
                    Map.entry("deptCode", "HR1234"),
                    Map.entry("location", "new york"),
                    Map.entry("country", "usa")
            );
            baseComparableNode = ComparableNode.builder().primaryKeys(pk).fields(fields).build();
        }
        ComparableNodeResult actualResults = currentComparableNode.compare(baseComparableNode);

        List<ComparableFieldResult> expectedFieldResults = new ArrayList<>();
        ComparableFieldResult diff1 = ComparableFieldResult.builder().fieldKey("state").fieldResult(CompareResult.EXTRA).thisFieldValue("oh").build();
        ComparableNodeResult expectedResults = ComparableNodeResult.builder().node(currentComparableNode).fieldResults(expectedFieldResults).build();
        assertThat(actualResults.getFieldResults()).hasSize(1);
        assertThat(actualResults.getFieldResults()).contains(diff1);
    }

    @Test
    void testCompareObjectDifferentTypes() {
        ComparableNode currentComparableNode = null;
        {
            Map<String, String> pk = Map.of("deptId", "10");
            Map<String, Object> fields = Map.ofEntries(
                    Map.entry("deptName", "HR"),
                    Map.entry("deptCode", "HR1234"),
                    Map.entry("location", Long.valueOf(1001)),
                    Map.entry("country", "usa")
            );

            currentComparableNode = ComparableNode.builder().primaryKeys(pk).fields(fields).build();
        }

        ComparableNode baseComparableNode = null;
        {
            Map<String, String> pk = Map.of("deptId", "10");
            Map<String, Object> fields = Map.ofEntries(
                    Map.entry("deptName", "HR"),
                    Map.entry("deptCode", "HR1234"),
                    Map.entry("location", Integer.valueOf(1001)),
                    Map.entry("country", "usa")
            );
            baseComparableNode = ComparableNode.builder().primaryKeys(pk).fields(fields).build();
        }
        ComparableNodeResult actualResults = currentComparableNode.compare(baseComparableNode);

        List<ComparableFieldResult> expectedFieldResults = new ArrayList<>();
        ComparableFieldResult diff1 = ComparableFieldResult.builder().fieldKey("location").fieldResult(CompareResult.DIFFERENT).thisFieldValue("1001").baseFieldValue("1001").notes("different types java.lang.Long vs java.lang.Integer").build();
        ComparableNodeResult expectedResults = ComparableNodeResult.builder().node(currentComparableNode).fieldResults(expectedFieldResults).build();
        assertThat(actualResults.getFieldResults()).hasSize(1);
        assertThat(actualResults.getFieldResults()).contains(diff1);
    }

    @Test
    void testCompareObjectListExactMatch() {
        List<ComparableNode> currentComparableNodeList = new ArrayList<>();
        {
            ComparableNode currentComparableNode = null;
            Map<String, String> pk = Map.of("deptId", "10");
            Map<String, Object> fields = Map.ofEntries(
                    Map.entry("deptName", "HR"),
                    Map.entry("deptCode", "HR1234"),
                    Map.entry("location", "new york"),
                    Map.entry("country", "usa")
            );
            currentComparableNode = ComparableNode.builder().primaryKeys(pk).fields(fields).build();
            currentComparableNodeList.add(currentComparableNode);
        }
        {
            ComparableNode currentComparableNode = null;
            Map<String, String> pk = Map.of("deptId", "11");
            Map<String, Object> fields = Map.ofEntries(
                    Map.entry("deptName", "Marketing"),
                    Map.entry("deptCode", "MR1234"),
                    Map.entry("location", "london"),
                    Map.entry("country", "uk")
            );
            currentComparableNode = ComparableNode.builder().primaryKeys(pk).fields(fields).build();
            currentComparableNodeList.add(currentComparableNode);
        }

        List<ComparableNode> baseComparableNodeList = new ArrayList<>();
        {
            ComparableNode currentComparableNode = null;
            Map<String, String> pk = Map.of("deptId", "10");
            Map<String, Object> fields = Map.ofEntries(
                    Map.entry("deptName", "HR"),
                    Map.entry("deptCode", "HR1234"),
                    Map.entry("location", "new york"),
                    Map.entry("country", "usa")
                    );
            currentComparableNode = ComparableNode.builder().primaryKeys(pk).fields(fields).build();
            baseComparableNodeList.add(currentComparableNode);
        }
        {
            ComparableNode currentComparableNode = null;
            Map<String, String> pk = Map.of("deptId", "11");
            Map<String, Object> fields = Map.ofEntries(
                    Map.entry("deptName", "Marketing"),
                    Map.entry("deptCode", "MR1234"),
                    Map.entry("location", "london"),
                    Map.entry("country", "uk")
                    );
            currentComparableNode = ComparableNode.builder().primaryKeys(pk).fields(fields).build();
            baseComparableNodeList.add(currentComparableNode);
        }

        List<ComparableNodeResult> actualResultList = ComparableNode.compareList(currentComparableNodeList, baseComparableNodeList);

        assertThat(actualResultList).hasSize(2);
        assertThat(actualResultList.get(0).getNodeResult()).isEqualTo(CompareResult.SAME);
        assertThat(actualResultList.get(1).getNodeResult()).isEqualTo(CompareResult.SAME);
    }

    @Test
    void testCompareObjectListExtraObject() {
        // this tests an extra object in this list is handled properly
        List<ComparableNode> currentComparableNodeList = new ArrayList<>();
        {
            ComparableNode currentComparableNode = null;
            Map<String, String> pk = Map.of("deptId", "10");
            Map<String, Object> fields = Map.ofEntries(
                    Map.entry("deptName", "HR"),
                    Map.entry("deptCode", "HR1234"),
                    Map.entry("location", "new york"),
                    Map.entry("country", "usa")
            );
            currentComparableNode = ComparableNode.builder().primaryKeys(pk).fields(fields).build();
            currentComparableNodeList.add(currentComparableNode);
        }
        {
            ComparableNode currentComparableNode = null;
            Map<String, String> pk = Map.of("deptId", "11");
            Map<String, Object> fields = Map.ofEntries(
                    Map.entry("deptName", "Marketing"),
                    Map.entry("deptCode", "MR1234"),
                    Map.entry("location", "london"),
                    Map.entry("country", "uk")
            );
            currentComparableNode = ComparableNode.builder().primaryKeys(pk).fields(fields).build();
            currentComparableNodeList.add(currentComparableNode);
        }

        List<ComparableNode> baseComparableNodeList = new ArrayList<>();
        {
            ComparableNode currentComparableNode = null;
            Map<String, String> pk = Map.of("deptId", "10");
            Map<String, Object> fields = Map.ofEntries(
                    Map.entry("deptName", "HR"),
                    Map.entry("deptCode", "HR1234"),
                    Map.entry("location", "new york"),
                    Map.entry("country", "usa")
            );
            currentComparableNode = ComparableNode.builder().primaryKeys(pk).fields(fields).build();
            baseComparableNodeList.add(currentComparableNode);
        }


        List<ComparableNodeResult> actualResultList = ComparableNode.compareList(currentComparableNodeList, baseComparableNodeList);

        assertThat(actualResultList).hasSize(2);
        assertThat(actualResultList.get(0).getNodeResult()).isEqualTo(CompareResult.SAME);
        assertThat(actualResultList.get(1).getNodeResult()).isEqualTo(CompareResult.EXTRA);
    }

    @Test
    void testCompareObjectListMissingObject() {
        // this tests a missing object in this list is handled properly
        List<ComparableNode> currentComparableNodeList = new ArrayList<>();
        {
            ComparableNode currentComparableNode = null;
            Map<String, String> pk = Map.of("deptId", "10");
            Map<String, Object> fields = Map.ofEntries(
                    Map.entry("deptName", "HR"),
                    Map.entry("deptCode", "HR1234"),
                    Map.entry("location", "new york"),
                    Map.entry("country", "usa")
            );
            currentComparableNode = ComparableNode.builder().primaryKeys(pk).fields(fields).build();
            currentComparableNodeList.add(currentComparableNode);
        }

        List<ComparableNode> baseComparableNodeList = new ArrayList<>();
        {
            ComparableNode currentComparableNode = null;
            Map<String, String> pk = Map.of("deptId", "10");
            Map<String, Object> fields = Map.ofEntries(
                    Map.entry("deptName", "HR"),
                    Map.entry("deptCode", "HR1234"),
                    Map.entry("location", "new york"),
                    Map.entry("country", "usa")
            );
            currentComparableNode = ComparableNode.builder().primaryKeys(pk).fields(fields).build();
            baseComparableNodeList.add(currentComparableNode);
        }
        {
            ComparableNode currentComparableNode = null;
            Map<String, String> pk = Map.of("deptId", "11");
            Map<String, Object> fields = Map.ofEntries(
                    Map.entry("deptName", "Marketing"),
                    Map.entry("deptCode", "MR1234"),
                    Map.entry("location", "london"),
                    Map.entry("country", "uk")
            );
            currentComparableNode = ComparableNode.builder().primaryKeys(pk).fields(fields).build();
            baseComparableNodeList.add(currentComparableNode);
        }


        List<ComparableNodeResult> actualResultList = ComparableNode.compareList(currentComparableNodeList, baseComparableNodeList);

        assertThat(actualResultList).hasSize(2);
        assertThat(actualResultList.get(0).getNodeResult()).isEqualTo(CompareResult.SAME);
        assertThat(actualResultList.get(1).getNodeResult()).isEqualTo(CompareResult.MISSING);
    }

    @Test
    void testCompareObjectWithCustomFieldsExactMatch() {
        // this tests a missing object in this list is handled properly
        List<ComparableNode> currentComparableNodeList = new ArrayList<>();
        {
            ComparableNode currentComparableNode = null;
            Map<String, String> pk = Map.of("deptId", "10");

            Map<String, String> customFields = Map.ofEntries(
                    Map.entry("color", "red"),
                    Map.entry("shape", "square"),
                    Map.entry("animal", "cat")
            );
            Map<String, Object> fields = Map.ofEntries(
                    Map.entry("deptName", "HR"),
                    Map.entry("deptCode", "HR1234"),
                    Map.entry("location", "new york"),
                    Map.entry("country", "usa"),
                    Map.entry("customFields", customFields)
            );
            currentComparableNode = ComparableNode.builder().primaryKeys(pk).fields(fields).build();
            currentComparableNodeList.add(currentComparableNode);
        }
        {
            ComparableNode currentComparableNode = null;
            Map<String, String> pk = Map.of("deptId", "11");
            Map<String, String> customFields = Map.ofEntries(
                    Map.entry("shade", "dark"),
                    Map.entry("sheen", "gloss")
            );
            Map<String, Object> fields = Map.ofEntries(
                    Map.entry("deptName", "Marketing"),
                    Map.entry("deptCode", "MR1234"),
                    Map.entry("location", "london"),
                    Map.entry("country", "uk"),
                    Map.entry("customFields", customFields)
            );
            currentComparableNode = ComparableNode.builder().primaryKeys(pk).fields(fields).build();
            currentComparableNodeList.add(currentComparableNode);
        }

        List<ComparableNode> baseComparableNodeList = new ArrayList<>();
        {
            ComparableNode currentComparableNode = null;
            Map<String, String> pk = Map.of("deptId", "10");
            Map<String, String> customFields = Map.ofEntries(
                    Map.entry("color", "red"),
                    Map.entry("shape", "square"),
                    Map.entry("animal", "cat")
            );
            Map<String, Object> fields = Map.ofEntries(
                    Map.entry("deptName", "HR"),
                    Map.entry("deptCode", "HR1234"),
                    Map.entry("location", "new york"),
                    Map.entry("country", "usa"),
                    Map.entry("customFields", customFields)
            );
            currentComparableNode = ComparableNode.builder().primaryKeys(pk).fields(fields).build();
            baseComparableNodeList.add(currentComparableNode);
        }
        {
            ComparableNode currentComparableNode = null;
            Map<String, String> pk = Map.of("deptId", "11");
            Map<String, String> customFields = Map.ofEntries(
                    Map.entry("shade", "dark"),
                    Map.entry("sheen", "gloss")
            );
            Map<String, Object> fields = Map.ofEntries(
                    Map.entry("deptName", "Marketing"),
                    Map.entry("deptCode", "MR1234"),
                    Map.entry("location", "london"),
                    Map.entry("country", "uk"),
                    Map.entry("customFields", customFields)
            );
            currentComparableNode = ComparableNode.builder().primaryKeys(pk).fields(fields).build();
            baseComparableNodeList.add(currentComparableNode);
        }


        List<ComparableNodeResult> actualResultList = ComparableNode.compareList(currentComparableNodeList, baseComparableNodeList);

        assertThat(actualResultList).hasSize(2);
        assertThat(actualResultList.get(0).getNodeResult()).isEqualTo(CompareResult.SAME);
        assertThat(actualResultList.get(1).getNodeResult()).isEqualTo(CompareResult.SAME);
    }

    @Test
    void testCompareObjectWithCustomFieldsDifferentValues() {
        // this tests a missing object in this list is handled properly
        List<ComparableNode> currentComparableNodeList = new ArrayList<>();
        {
            ComparableNode currentComparableNode = null;
            Map<String, String> pk = Map.of("deptId", "10");

            Map<String, String> customFields = Map.ofEntries(
                    Map.entry("color", "red"),
                    Map.entry("shape", "square"),
                    Map.entry("animal", "cat")
            );
            Map<String, Object> fields = Map.ofEntries(
                    Map.entry("deptName", "HR"),
                    Map.entry("deptCode", "HR1234"),
                    Map.entry("location", "new york"),
                    Map.entry("country", "usa"),
                    Map.entry("customFields", customFields)
            );
            currentComparableNode = ComparableNode.builder().primaryKeys(pk).fields(fields).build();
            currentComparableNodeList.add(currentComparableNode);
        }
        {
            ComparableNode currentComparableNode = null;
            Map<String, String> pk = Map.of("deptId", "11");
            Map<String, String> customFields = Map.ofEntries(
                    Map.entry("shade", "dark"),
                    Map.entry("sheen", "gloss")
            );
            Map<String, Object> fields = Map.ofEntries(
                    Map.entry("deptName", "Marketing"),
                    Map.entry("deptCode", "MR1234"),
                    Map.entry("location", "london"),
                    Map.entry("country", "uk"),
                    Map.entry("customFields", customFields)
            );
            currentComparableNode = ComparableNode.builder().primaryKeys(pk).fields(fields).build();
            currentComparableNodeList.add(currentComparableNode);
        }

        List<ComparableNode> baseComparableNodeList = new ArrayList<>();
        {
            ComparableNode currentComparableNode = null;
            Map<String, String> pk = Map.of("deptId", "10");
            Map<String, String> customFields = Map.ofEntries(
                    Map.entry("color", "blue"),
                    Map.entry("shape", "square"),
                    Map.entry("animal", "cat")
            );
            Map<String, Object> fields = Map.ofEntries(
                    Map.entry("deptName", "HR"),
                    Map.entry("deptCode", "HR1234"),
                    Map.entry("location", "new york"),
                    Map.entry("country", "usa"),
                    Map.entry("customFields", customFields)
            );
            currentComparableNode = ComparableNode.builder().primaryKeys(pk).fields(fields).build();
            baseComparableNodeList.add(currentComparableNode);
        }
        {
            ComparableNode currentComparableNode = null;
            Map<String, String> pk = Map.of("deptId", "11");
            Map<String, String> customFields = Map.ofEntries(
                    Map.entry("shade", "light"),
                    Map.entry("sheen", "gloss")
            );
            Map<String, Object> fields = Map.ofEntries(
                    Map.entry("deptName", "Marketing"),
                    Map.entry("deptCode", "MR1234"),
                    Map.entry("location", "london"),
                    Map.entry("country", "uk"),
                    Map.entry("customFields", customFields)
            );
            currentComparableNode = ComparableNode.builder().primaryKeys(pk).fields(fields).build();
            baseComparableNodeList.add(currentComparableNode);
        }


        List<ComparableNodeResult> actualResultList = ComparableNode.compareList(currentComparableNodeList, baseComparableNodeList);

        assertThat(actualResultList).hasSize(2);
        assertThat(actualResultList.get(0).getNodeResult()).isEqualTo(CompareResult.DIFFERENT);
        assertThat(actualResultList.get(1).getNodeResult()).isEqualTo(CompareResult.DIFFERENT);
    }

    @Test
    void testCompareObjectWithCustomFieldsOneMissingOneExtra() {
        // this tests a missing object in this list is handled properly
        List<ComparableNode> currentComparableNodeList = new ArrayList<>();
        {
            ComparableNode currentComparableNode = null;
            Map<String, String> pk = Map.of("deptId", "10");

            Map<String, String> customFields = Map.ofEntries(
                    Map.entry("color", "red"),
                    Map.entry("shape", "square"),
                    Map.entry("animal", "cat") // make this extra
            );
            Map<String, Object> fields = Map.ofEntries(
                    Map.entry("deptName", "HR"),
                    Map.entry("deptCode", "HR1234"),
                    Map.entry("location", "new york"),
                    Map.entry("country", "usa"),
                    Map.entry("customFields", customFields)
            );
            currentComparableNode = ComparableNode.builder().primaryKeys(pk).fields(fields).build();
            currentComparableNodeList.add(currentComparableNode);
        }
        {
            ComparableNode currentComparableNode = null;
            Map<String, String> pk = Map.of("deptId", "11");
            Map<String, String> customFields = Map.ofEntries(
                    // Map.entry("shade", "dark"), // make this missing
                    Map.entry("sheen", "gloss"),
                    Map.entry("texture", "smooth")
            );
            Map<String, Object> fields = Map.ofEntries(
                    Map.entry("deptName", "Marketing"),
                    Map.entry("deptCode", "MR1234"),
                    Map.entry("location", "london"),
                    Map.entry("country", "uk"),
                    Map.entry("customFields", customFields)
            );
            currentComparableNode = ComparableNode.builder().primaryKeys(pk).fields(fields).build();
            currentComparableNodeList.add(currentComparableNode);
        }

        List<ComparableNode> baseComparableNodeList = new ArrayList<>();
        {
            ComparableNode currentComparableNode = null;
            Map<String, String> pk = Map.of("deptId", "10");
            Map<String, String> customFields = Map.ofEntries(
                    Map.entry("color", "red"),
                    Map.entry("shape", "square")
                    // this is extra in the current
                    // Map.entry("animal", "cat")
            );
            Map<String, Object> fields = Map.ofEntries(
                    Map.entry("deptName", "HR"),
                    Map.entry("deptCode", "HR1234"),
                    Map.entry("location", "new york"),
                    Map.entry("country", "usa"),
                    Map.entry("customFields", customFields)
            );
            currentComparableNode = ComparableNode.builder().primaryKeys(pk).fields(fields).build();
            baseComparableNodeList.add(currentComparableNode);
        }
        {
            ComparableNode currentComparableNode = null;
            Map<String, String> pk = Map.of("deptId", "11");
            Map<String, String> customFields = Map.ofEntries(
                    Map.entry("shade", "dark"), // make this missing in the current
                    Map.entry("sheen", "gloss"),
                    Map.entry("texture", "smooth")
            );
            Map<String, Object> fields = Map.ofEntries(
                    Map.entry("deptName", "Marketing"),
                    Map.entry("deptCode", "MR1234"),
                    Map.entry("location", "london"),
                    Map.entry("country", "uk"),
                    Map.entry("customFields", customFields)
            );
            currentComparableNode = ComparableNode.builder().primaryKeys(pk).fields(fields).build();
            baseComparableNodeList.add(currentComparableNode);
        }


        List<ComparableNodeResult> actualResultList = ComparableNode.compareList(currentComparableNodeList, baseComparableNodeList);

        assertThat(actualResultList).hasSize(2);
        assertThat(actualResultList.get(0).getNodeResult()).isEqualTo(CompareResult.DIFFERENT);
        assertThat(actualResultList.get(1).getNodeResult()).isEqualTo(CompareResult.DIFFERENT);

        ComparableFieldResult diff = ComparableFieldResult.builder().fieldKey("animal").fieldResult(CompareResult.EXTRA).thisFieldValue("cat").build();
        assertThat(actualResultList.get(0).getFieldResults()).hasSize(1);
        assertThat(actualResultList.get(0).getFieldResults()).contains(diff);

        diff = ComparableFieldResult.builder().fieldKey("shade").fieldResult(CompareResult.MISSING).baseFieldValue("dark").build();
        assertThat(actualResultList.get(1).getFieldResults()).hasSize(1);
        assertThat(actualResultList.get(1).getFieldResults()).contains(diff);
    }
}
