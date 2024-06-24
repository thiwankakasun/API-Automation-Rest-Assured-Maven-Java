//package utils;
//
//import tests.BaseClass;
//import com.pearson.common.framework.api.core.RESTServiceBase;
//import io.restassured.response.Response;
//import net.minidev.json.JSONObject;
//import org.codehaus.jackson.JsonParseException;
//import org.codehaus.jackson.map.JsonMappingException;
//import pojos.PiToken;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//public class MsLogin extends BaseClass {
//    private static Map<String, String> header = new HashMap<String, String>();
//    private static Response response;
//    PiToken piToken = new PiToken();
//
//    public PiToken generatePiToken(String url)throws JsonParseException, JsonMappingException, IOException {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("userName", userName);
//        jsonObject.put("password", password);
//        jsonObject.put("clientID", clientID);
//        return getPiToken(jsonObject, url);
//    }
//
//    public PiToken generatePiToken(String url, String username, String password)
//            throws JsonParseException, JsonMappingException, IOException {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("userName", username);
//        jsonObject.put("password", password);
//        return getPiToken(jsonObject, url);
//    }
//
//    private PiToken getPiToken(JSONObject jsonObject, String url)
//            throws JsonParseException, JsonMappingException, IOException {
//        response = RESTServiceBase.postCallWithHeaderAndJsonBodyParam(
//                header,
//                jsonObject.toString(),
//                url);
//        piToken = mapper.readValue(response.asString(), PiToken.class);
//        return piToken;
//    }
//}
