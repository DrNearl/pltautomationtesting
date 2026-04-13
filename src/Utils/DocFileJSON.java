package Utils;

import java.io.FileReader;
import java.util.Iterator;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class DocFileJSON {
    
    // Hàm đọc dữ liệu từ file JSON và trả về danh sách các JSONObject
    public static JSONArray docDuLieuJSON(String duongDanFile) {
        JSONParser parser = new JSONParser();
        JSONArray danhSach = null;
        try {
            FileReader reader = new FileReader(duongDanFile);
            Object obj = parser.parse(reader);
            danhSach = (JSONArray) obj;
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return danhSach;
    }
}
