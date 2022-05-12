import org.ap.libcomp.model.ComparableFieldResult;
import org.ap.libcomp.model.ComparableNode;
import org.ap.libcomp.model.ComparableNodeResult;
import org.ap.libcomp.model.FieldResult;
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

        assertThat(actualResults.getResults()).hasSize(0);
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
        ComparableFieldResult diff1 = ComparableFieldResult.builder().fieldKey("location").fieldResult(FieldResult.DIFFERENT).thisFieldValue("new york").baseFieldValue("new orleans").build();
        ComparableFieldResult diff2 = ComparableFieldResult.builder().fieldKey("deptCode").fieldResult(FieldResult.DIFFERENT).thisFieldValue("HR1234").baseFieldValue("HR1235").build();
        ComparableNodeResult expectedResults = ComparableNodeResult.builder().node(currentComparableNode).results(expectedFieldResults).build();
        assertThat(actualResults.getResults()).hasSize(2);
        assertThat(actualResults.getResults()).contains(diff1);
        assertThat(actualResults.getResults()).contains(diff2);
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
        ComparableFieldResult diff1 = ComparableFieldResult.builder().fieldKey("state").fieldResult(FieldResult.MISSING).baseFieldValue("oh").build();
        ComparableNodeResult expectedResults = ComparableNodeResult.builder().node(currentComparableNode).results(expectedFieldResults).build();
        assertThat(actualResults.getResults()).hasSize(1);
        assertThat(actualResults.getResults()).contains(diff1);
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
        ComparableFieldResult diff1 = ComparableFieldResult.builder().fieldKey("state").fieldResult(FieldResult.EXTRA).thisFieldValue("oh").build();
        ComparableNodeResult expectedResults = ComparableNodeResult.builder().node(currentComparableNode).results(expectedFieldResults).build();
        assertThat(actualResults.getResults()).hasSize(1);
        assertThat(actualResults.getResults()).contains(diff1);
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
        ComparableFieldResult diff1 = ComparableFieldResult.builder().fieldKey("location").fieldResult(FieldResult.DIFFERENT).thisFieldValue("1001").baseFieldValue("1001").notes("different types").build();
        ComparableNodeResult expectedResults = ComparableNodeResult.builder().node(currentComparableNode).results(expectedFieldResults).build();
        assertThat(actualResults.getResults()).hasSize(1);
        assertThat(actualResults.getResults()).contains(diff1);
    }

}
