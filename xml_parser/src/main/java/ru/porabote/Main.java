package ru.porabote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {

        List<Employee> list = parseXML("sources/data.xml");

        String json = listToJson(list);
        writeString("data2.json", json);
    }

    public static List<Employee> parseXML(String filePath) throws IOException {

        List<Employee> list = new ArrayList<Employee>();

        try {
            Document doc = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder().parse(filePath);

            Element rootNode = doc.getDocumentElement();

            NodeList nodeList = rootNode.getElementsByTagName("employee");

            for (int i = 0; i < nodeList.getLength(); i++) {

                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {

                    Element element = (Element) node;
                    NodeList map = node.getChildNodes();

                    Employee employee = new Employee(
                            Long.parseLong(findValueByTagName(element, "id")),
                            findValueByTagName(element, "firstName"),
                            findValueByTagName(element, "lastName"),
                            findValueByTagName(element, "country"),
                            Integer.parseInt(findValueByTagName(element, "age"))
                    );
                    list.add(employee);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    private static <T> T findValueByTagName(Element element, String tagName) {
        return (T) element.getElementsByTagName(tagName).item(0)
                .getFirstChild().getTextContent();

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