package org.ap.compare.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.ap.compare.app.model.Customer;
import org.ap.compare.app.utils.JsonHelper;
import org.ap.compare.lib.model.ComparableNode;
import org.ap.compare.lib.model.CompareDataProvider;
import org.ap.compare.lib.model.ComparePair;
import org.ap.compare.lib.service.CompareNodeService;

import java.io.IOException;
import java.util.List;

public class DataProviderFromJsonFile implements CompareDataProvider {

  CompareNodeService compareNodeService = new CompareNodeService();
  List<Customer> customerListCurrent;
  List<Customer> customerListBase;
  int iterIndex = 0;

  public DataProviderFromJsonFile(String currentFilename, String baseFilename) throws IOException {
    customerListCurrent = JsonHelper.readCustomerListFromFile(currentFilename);
    customerListBase = JsonHelper.readCustomerListFromFile(baseFilename);
  }

  @Override
  public boolean hasNextComparePair() {
    return iterIndex < customerListCurrent.size();
  }

  @Override
  public ComparePair getNextComparePair() {
    try {
      if (hasNextComparePair()) {
        ComparableNode currentNode =
            compareNodeService.toComparableNode(customerListCurrent.get(iterIndex));
        System.out.println("currentNode read\n" + JsonHelper.writeObject(currentNode));
        ComparableNode baseNode =
            compareNodeService.toComparableNode(customerListBase.get(iterIndex));
        iterIndex++;
        return ComparePair.builder().currentNode(currentNode).baseNode(baseNode).build();
      } else {
        return null;
      }
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
