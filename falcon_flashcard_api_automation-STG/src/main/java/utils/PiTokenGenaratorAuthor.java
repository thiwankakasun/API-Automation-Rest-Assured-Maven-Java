package utils;

import base.BaseClass;
import com.pearson.common.framework.api.core.RESTServiceBase;
import io.restassured.response.Response;
import net.minidev.json.JSONObject;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import pojos.PiToken;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PiTokenGenaratorAuthor extends BaseClass {

    private static Map<String, String> header = new HashMap<String, String>();
    private static Response response;
    PiToken piToken = new PiToken();

    public PiToken generatePiToken(String url) throws JsonParseException, JsonMappingException, IOException {
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("userName", usernameAuthor);
        jsonObject1.put("password", passwordAuthor);
        return getPiToken(jsonObject1, url);
    }

    private PiToken getPiToken(JSONObject jsonObject, String url)
            throws JsonParseException, JsonMappingException, IOException {
        response = RESTServiceBase.postCallWithHeaderAndJsonBodyParam(
                header,
                jsonObject.toString(),
                url);

        piToken = mapper.readValue(response.asString(), PiToken.class);
        return piToken;
    }
}
