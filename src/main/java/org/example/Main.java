package org.example;

import org.example.db.WorkWithFile;
import org.example.service.QueryService;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        WorkWithFile workWithFile = new WorkWithFile();
        workWithFile.cleanFile();
        List<List<String>> strings = workWithFile.readFileByName();
        QueryService queryService = new QueryService();
        queryService.processInputLists(strings);
    }
}