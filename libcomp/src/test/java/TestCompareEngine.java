import com.fasterxml.jackson.databind.ObjectMapper;
import org.ap.compare.app.service.DataProviderFromJsonFile;
import org.ap.compare.lib.model.ComparableNodeResult;
import org.ap.compare.lib.model.CompareDataProvider;
import org.ap.compare.lib.service.CompareEngine;
import org.ap.compare.lib.service.CompareNodeService;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class TestCompareEngine {

  /**
   * this scenario tests two customers, with one customer matches exactly with the base while the
   * other one has one field different
   */
  @Test
  void testScenario1() throws IOException {
    String currentFile = "src/test/resources/TestCompareEngineScenario1_current.json";
    String baseFile = "src/test/resources/TestCompareEngineScenario1_base.json";
    CompareDataProvider compareDataProvider = new DataProviderFromJsonFile(currentFile, baseFile);
    CompareEngine compareEngine = new CompareEngine(compareDataProvider);
    List<ComparableNodeResult> results = compareEngine.compare();
    System.out.println("results");
    ObjectMapper objectMapper = new ObjectMapper();
    String s = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(results);
    System.out.println(s);
  }

  /**
   * this scenario tests two customers, with one customer matches exactly with the base while the
   * other one has one field different
   */
  @Test
  void testScenario2() throws IOException {
    String currentFile = "src/test/resources/TestCompareEngineScenario2_current.json";
    String baseFile = "src/test/resources/TestCompareEngineScenario2_base.json";
    CompareDataProvider compareDataProvider = new DataProviderFromJsonFile(currentFile, baseFile);
    CompareEngine compareEngine = new CompareEngine(compareDataProvider);
    List<ComparableNodeResult> results = compareEngine.compare();
    System.out.println("results");
    ObjectMapper objectMapper = new ObjectMapper();
    String s = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(results);
    System.out.println(s);
  }

  /**
   * this scenario tests two customers, with one customer matches exactly with the base while the
   * other one has one field different
   */
  @Test
  void testScenario3() throws IOException {
    String currentFile = "src/test/resources/TestCompareEngineScenario3_current.json";
    String baseFile = "src/test/resources/TestCompareEngineScenario3_base.json";
    CompareDataProvider compareDataProvider = new DataProviderFromJsonFile(currentFile, baseFile);
    CompareEngine compareEngine = new CompareEngine(compareDataProvider);
    List<ComparableNodeResult> results = compareEngine.compare();
    System.out.println("results");
    ObjectMapper objectMapper = new ObjectMapper();
    String s = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(results);
    System.out.println(s);
  }
}
