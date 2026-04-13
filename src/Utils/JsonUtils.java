package Utils;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonUtils {

    public static JSONArray readJsonArray(String path) {
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader(path)) {
            Object obj = parser.parse(reader);
            return (JSONArray) obj;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONObject readJsonObject(String path) {
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader(path)) {
            Object obj = parser.parse(reader);
            return (JSONObject) obj;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Map<String, String>> readChuongData(String path) {
        List<Map<String, String>> list = new ArrayList<>();
        JSONArray arr = readJsonArray(path);
        if (arr != null) {
            for (Object obj : arr) {
                JSONObject json = (JSONObject) obj;
                Map<String, String> map = new HashMap<>();
                Object title = json.get("Title");
                Object body = json.get("Body");
                map.put("Title", title != null ? title.toString() : "");
                map.put("Body", body != null ? body.toString() : "");
                list.add(map);
            }
        }
        return list;
    }

    public static Object[][] getTestData(String path) {
        JSONArray jsonArray = readJsonArray(path);
        if (jsonArray == null) {
            return new Object[0][0]; 
        }
        Object[][] data = new Object[jsonArray.size()][1];
        for (int i = 0; i < jsonArray.size(); i++) {
            data[i][0] = jsonArray.get(i); 
        }
        return data;
    }
}