package utils;

import base.BaseClass;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

public class JsonReader extends BaseClass {

    public JSONObject getJsonObject(String url) throws Exception {
        JSONParser parser = new JSONParser();
        Reader reader = new FileReader(url);
        JSONObject jsonPayload = (JSONObject) parser.parse(reader);

        return jsonPayload;
    }

    public JSONObject convertResponseToJson(Response response) {
        Object obj = JSONValue.parse(response.asString());
        JSONObject object = (JSONObject) obj;

        return object;
    }
}
