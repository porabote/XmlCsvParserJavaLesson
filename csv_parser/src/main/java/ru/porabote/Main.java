package ru.porabote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {

        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "sources/data.csv";

        List<Employee> list = parseCSV(columnMapping, fileName);
        String json = listToJson(list);
        writeString("data.json", json);
    }

    public static List<Employee> parseCSV(String[] columnMapping, String filePath) {
        try {

            // Set reader
            FileReader filereader = new FileReader(filePath);
            CSVReader csvReader = new CSVReader(filereader);

            // Set strategu
            ColumnPositionMappingStrategy strategy = new ColumnPositionMappingStrategy();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);

            //Parse
            CsvToBean<Employee> csvToBean = new CsvToBeanBuilder(csvReader)
                    .withMappingStrategy(strategy)
                    .build();
            List<Employee> list = csvToBean.parse();

            System.out.println(list);

            return list;

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String listToJson(List<Employee> list) throws IOException {

        Type listType = new TypeToken<List<Employee>>() {}.getType();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(list, listType);
    }

    public static void writeString(String filePath, String jsonData) throws IOException {

        File f = new File(filePath);

        try {
            if (f.canWrite()) { //canRead, canExecute
                FileWriter writer = new FileWriter(f);
                writer.write(jsonData, 0, jsonData.length());
                writer.flush();
                writer.close();
            } else {
                throw new IOException("Файл " + filePath + " не доступен для записи");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}