package org.ap.compare.app.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ap.compare.app.model.Customer;
import org.ap.compare.app.model.Order;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

public class JsonHelper {
    public static String writeObject(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String prettyString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        return prettyString;
    }

    public static String writeCustomer(Customer customer) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String canonicalJsonString = objectMapper.writeValueAsString(customer);
        System.out.println("RAW JSON=" + canonicalJsonString);
        System.out.println("===================================");
        String prettyString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(customer);
        System.out.println("Pretty JSON=" + prettyString);
        return canonicalJsonString;
    }

    public static Customer readCustomer(String jsonString) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Customer customer = objectMapper.readValue(jsonString, Customer.class);
        return customer;
    }

    public static Customer readCustomerFromFile(String filename) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        // this flavor reads file relative to working directory
        Customer customer = objectMapper.readValue(new File(filename), Customer.class);
        return customer;
    }


    public static List<Customer> readCustomerListFromFile(String filename) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        // this flavor reads file relative to working directory
        List<Customer> customer = objectMapper.readValue(new File(filename), new TypeReference<List<Customer>>(){});
        return customer;
    }
}
