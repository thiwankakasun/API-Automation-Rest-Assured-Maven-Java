package API;

import com.pearson.common.framework.shared.alm.AlmAnnotation;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pojos.PiToken;
import requestDTO.DeckControllerRequestDTO;
import requestDTO.QuestionControllerRequestDTO;
import requestDTO.TenantControllerRequestDTO;
import responseDTO.*;
import utils.JsonReader;
import utils.PiTokenGenarator;
import utils.PiTokenGenaratorNormalUser;

import java.io.IOException;
import java.time.Instant;
import java.util.Calendar;
import java.util.HashMap;
import java.util.logging.Logger;

public class FavouriteController extends ServiceController {

    //private String defaultContentType = "JSON";
    private QuestionControllerResponseDTO QuestionControllerResponseDTO;
    private QuestionControllerRequestDTO QuestionControllerRequestDTO;
    private DeckControllerResponseDTO DeckControllerResponseDTO;
    private DeckControllerRequestDTO DeckControllerRequestDTO;
    private FavouriteControllerResponseDTO FavouriteControllerResponseDTO;
    private QuestionDTO QuestionDTO;
    private static final utils.PiTokenGenarator PiTokenGenarator = new PiTokenGenarator();
    private static final utils.PiTokenGenaratorNormalUser PiTokenGenaratorNormalUser = new PiTokenGenaratorNormalUser();
    private static final HashMap<String, String> header = new HashMap<>();
    private static final HashMap<String, String> headerNormal = new HashMap<>();
    //private Map<String, String> payload = new HashMap<>();
    JsonReader jsonReader = new JsonReader();

    private static final Logger LOGGER = Logger.getLogger(String.valueOf(FavouriteController.class));

    @BeforeClass
    public void getData() throws EncryptedDocumentException, InvalidFormatException, IOException, ParseException {
        try {
            PiToken piToken = PiTokenGenarator.generatePiToken(loginUrl);
            header.put("X-Authorization", piToken.getData());
            PiToken piToken1 = PiTokenGenaratorNormalUser.generatePiToken(loginUrl);
            headerNormal.put("X-Authorization", piToken1.getData());
            if (Env.equals("DEV")) {
                headerNormal.put("X-TenantId", "2b4af3fc-97b4-41a3-af7e-bc9354d35fed");
                headerNormal.put("X-TenantKey", "9acc0488-6a42-4c54-8d7a-edbac1716e73");
                header.put("X-TenantId", "2b4af3fc-97b4-41a3-af7e-bc9354d35fed");
                header.put("X-TenantKey", "9acc0488-6a42-4c54-8d7a-edbac1716e73");
            }

            if (Env.equals("QA")) {
                headerNormal.put("X-TenantId", "2b4af3fc-97b4-41a3-af7e-bc9354d35fed");
                headerNormal.put("X-TenantKey", "152be39d-8699-4ae9-9bae-91a50c28b6d0");
                header.put("X-TenantId", "2b4af3fc-97b4-41a3-af7e-bc9354d35fed");
                header.put("X-TenantKey", "152be39d-8699-4ae9-9bae-91a50c28b6d0");
            }

            if (Env.equals("NFT")) {
                headerNormal.put("X-TenantId", "beeea450-02f6-4f88-842e-cec9364453b7");
                headerNormal.put("X-TenantKey", "21d77376-46b2-4e26-8781-2d1a59468f8f");
                header.put("X-TenantId", "beeea450-02f6-4f88-842e-cec9364453b7");
                header.put("X-TenantKey", "21d77376-46b2-4e26-8781-2d1a59468f8f");
            }

            if (Env.equals("STG")) {
                headerNormal.put("X-TenantId", "30258aed-46bb-4ec5-909e-ecea2431772b");
                headerNormal.put("X-TenantKey", "297a5917-24fa-43fe-a27b-426126bd7cf4");
                header.put("X-TenantId", "30258aed-46bb-4ec5-909e-ecea2431772b");
                header.put("X-TenantKey", "297a5917-24fa-43fe-a27b-426126bd7cf4");
            }

            if (Env.equals("PROD")) {
                headerNormal.put("X-TenantId", "beeea450-02f6-4f88-842e-cec9364453b7");
                headerNormal.put("X-TenantKey", "21d77376-46b2-4e26-8781-2d1a59468f8f");
                header.put("X-TenantId", "beeea450-02f6-4f88-842e-cec9364453b7");
                header.put("X-TenantKey", "21d77376-46b2-4e26-8781-2d1a59468f8f");
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 1)
    public void createSingleDeckWithFullPayload() throws Exception {
        JSONObject createDeckPayload = jsonReader.getJsonObject(DeckCreatePayload);
        System.out.println(createDeckPayload);

        String payload = String.valueOf(createDeckPayload);
        DeckControllerRequestDTO = mapper.readValue(payload, DeckControllerRequestDTO.class);
        DeckControllerResponseDTO = createDeck(headerNormal,payload);

        Assert.assertEquals(StatusCode, 201, "Status Code");
        LOGGER.info("Tenant Id:" + DeckControllerResponseDTO.getDecks()[0].getId());
    }

    @Test(priority = 2) @AlmAnnotation(almTestId = "609861")
    public void CreateQuestionWithFullPayload() throws Exception {
        try {
            // Read json object from file
            JSONObject createQuestionPayload = jsonReader.getJsonObject(QuestionCreatePayload);
            System.out.println(createQuestionPayload);
            createQuestionPayload.put("deckId",DeckControllerResponseDTO.getDecks()[0].getId());

            String payload = String.valueOf(createQuestionPayload);
            //QuestionControllerRequestDTO = mapper.readValue(payload, QuestionControllerRequestDTO.class);
            QuestionControllerResponseDTO = createQuestion(headerNormal, payload);

            Assert.assertEquals(StatusCode, 201, "Status Code");
            //LOGGER.info("Tenant Id:" + QuestionControllerResponseDTO.getQuestions()[0].getI);
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 3) @AlmAnnotation(almTestId = "609180")
    public void FavouriteQuestion() throws Exception {
        try {
            String QuestionId = QuestionControllerResponseDTO.getQuestions()[0].getId();
            JSONObject FavouritePayload = jsonReader.getJsonObject(FavouriteQuestionPayload);

            JSONObject favourites = new JSONObject();
            favourites.put("favourite",true);
            favourites.put("questionId",QuestionId);
            favourites.put("updatedAt", Instant.now().toString());

            JSONArray favouritesKey = new JSONArray();
            favouritesKey.add(favourites);
            FavouritePayload.put("favourites",favouritesKey);
            FavouritePayload.put("userDeckId",DeckControllerResponseDTO.getDecks()[0].getId());
            FavouritePayload.put("userId",DeckControllerResponseDTO.getDecks()[0].getUserId());

            String payload = String.valueOf(FavouritePayload);
            System.out.println(payload);
            //DeckControllerRequestDTO = mapper.readValue(payload, DeckControllerRequestDTO.class);
            FavouriteControllerResponseDTO = favouriteQuestion(headerNormal, payload);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(FavouriteControllerResponseDTO.getFavourites()[0].getQuestionId(), QuestionId, "Status Code");
            Assert.assertEquals(FavouriteControllerResponseDTO.getFavourites()[0].isFavourite(), true, "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 4) @AlmAnnotation(almTestId = "609180")
    public void GetFavouriteQuestion() throws Exception {
        try {
            String QuestionId = QuestionControllerResponseDTO.getQuestions()[0].getId();
            String deckId = DeckControllerResponseDTO.getDecks()[0].getId();
            //DeckControllerRequestDTO = mapper.readValue(payload, DeckControllerRequestDTO.class);
            FavouriteControllerResponseDTO = getfavouriteQuestion(headerNormal, deckId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(FavouriteControllerResponseDTO.getFavourites()[0].getQuestionId(), QuestionId, "Status Code");
            Assert.assertEquals(FavouriteControllerResponseDTO.getFavourites()[0].isFavourite(), true, "Status Code");
            Assert.assertEquals(FavouriteControllerResponseDTO.getUserDeckId(), deckId, "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 5) @AlmAnnotation(almTestId = "611108")
    public void DeleteSingleQuestionWithFullPayload() throws Exception {

        try {
            String QuestionId = QuestionControllerResponseDTO.getQuestions()[0].getId();
            QuestionControllerResponseDTO = deleteQuestion(headerNormal, QuestionId);

            Assert.assertEquals(StatusCode, 204, "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }
    @Test(priority = 6) @AlmAnnotation(almTestId = "611108")
    public void DeleteDeckWithFullPayload() throws Exception {

        try {
            String DeckId = DeckControllerResponseDTO.getDecks()[0].getId();
            DeckControllerResponseDTO = deleteDeck(headerNormal, DeckId);

            Assert.assertEquals(StatusCode, 204, "Status Code");

        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 7) @AlmAnnotation(almTestId = "609180")
    public void FavouriteQuestionwithoutQuestionId() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            JSONObject FavouritePayload = jsonReader.getJsonObject(FavouriteQuestionPayload);

            JSONObject favourites = new JSONObject();
            favourites.put("favourite",true);
            favourites.put("updatedAt", Instant.now().toString());

            JSONArray favouritesKey = new JSONArray();
            favouritesKey.add(favourites);
            FavouritePayload.put("favourites",favouritesKey);
            FavouritePayload.put("userDeckId",DeckControllerResponseDTO.getDecks()[0].getId());

            String payload = String.valueOf(FavouritePayload);
            System.out.println(payload);
            //DeckControllerRequestDTO = mapper.readValue(payload, DeckControllerRequestDTO.class);
            FavouriteControllerResponseDTO = favouriteQuestion(headerNormal, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            LOGGER.info("No any content returned in Response");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 8) @AlmAnnotation(almTestId = "609180")
    public void FavouriteQuestionwithoutfavouriteField() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            String QuestionId = QuestionControllerResponseDTO.getQuestions()[0].getId();
            JSONObject FavouritePayload = jsonReader.getJsonObject(FavouriteQuestionPayload);

            JSONObject favourites = new JSONObject();
            favourites.put("questionId",QuestionId);
            favourites.put("updatedAt", Instant.now().toString());

            JSONArray favouritesKey = new JSONArray();
            favouritesKey.add(favourites);
            FavouritePayload.put("favourites",favouritesKey);
            FavouritePayload.put("userDeckId",DeckControllerResponseDTO.getDecks()[0].getId());
            FavouritePayload.put("userId",DeckControllerResponseDTO.getDecks()[0].getUserId());

            String payload = String.valueOf(FavouritePayload);
            System.out.println(payload);
            //DeckControllerRequestDTO = mapper.readValue(payload, DeckControllerRequestDTO.class);
            FavouriteControllerResponseDTO = favouriteQuestion(headerNormal, payload);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(FavouriteControllerResponseDTO.getFavourites()[0].getQuestionId(),  QuestionId,"Status Code");
            Assert.assertFalse(FavouriteControllerResponseDTO.getFavourites()[0].isFavourite(),  "Status Code");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 9) @AlmAnnotation(almTestId = "609180")
    public void FavouriteQuestionwithoutuserDeckIdField() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            String QuestionId = QuestionControllerResponseDTO.getQuestions()[0].getId();
            JSONObject FavouritePayload = jsonReader.getJsonObject(FavouriteQuestionPayload);

            JSONObject favourites = new JSONObject();
            favourites.put("questionId",QuestionId);
            favourites.put("updatedAt", Instant.now().toString());

            JSONArray favouritesKey = new JSONArray();
            favouritesKey.add(favourites);
            FavouritePayload.put("favourites",favouritesKey);
            FavouritePayload.put("userDeckId",null);

            String payload = String.valueOf(FavouritePayload);
            System.out.println(payload);
            //DeckControllerRequestDTO = mapper.readValue(payload, DeckControllerRequestDTO.class);
            FavouriteControllerResponseDTO = favouriteQuestion(headerNormal, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(FavouriteControllerResponseDTO.getFieldErrors()[0].getField(),  "userDeckId","Status Code");
            Assert.assertEquals(FavouriteControllerResponseDTO.getFieldErrors()[0].getMessage(), "Cannot be null or empty" ,"Status Code");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    //@Test(priority = 9) @AlmAnnotation(almTestId = "609180")
    public void FavouriteQuestionwithInvaliduserDeckIdField() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            String QuestionId = QuestionControllerResponseDTO.getQuestions()[0].getId();
            JSONObject FavouritePayload = jsonReader.getJsonObject(FavouriteQuestionPayload);

            JSONObject favourites = new JSONObject();
            favourites.put("questionId",QuestionId);
            favourites.put("updatedAt", Instant.now().toString());

            JSONArray favouritesKey = new JSONArray();
            favouritesKey.add(favourites);
            FavouritePayload.put("favourites",favouritesKey);
            FavouritePayload.put("userDeckId","123");

            String payload = String.valueOf(FavouritePayload);
            System.out.println(payload);
            //DeckControllerRequestDTO = mapper.readValue(payload, DeckControllerRequestDTO.class);
            FavouriteControllerResponseDTO = favouriteQuestion(headerNormal, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(FavouriteControllerResponseDTO.getFieldErrors()[0].getField(),  "userDeckId","Status Code");
            Assert.assertEquals(FavouriteControllerResponseDTO.getFieldErrors()[0].getMessage(), "Cannot be null or empty" ,"Status Code");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 10) @AlmAnnotation(almTestId = "609180")
    public void FavouriteQuestionwithoutuserIdField() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            String QuestionId = QuestionControllerResponseDTO.getQuestions()[0].getId();
            JSONObject FavouritePayload = jsonReader.getJsonObject(FavouriteQuestionPayload);

            JSONObject favourites = new JSONObject();
            favourites.put("questionId",QuestionId);
            favourites.put("updatedAt", Instant.now().toString());

            JSONArray favouritesKey = new JSONArray();
            favouritesKey.add(favourites);
            FavouritePayload.put("favourites",favouritesKey);
            FavouritePayload.put("userDeckId",DeckControllerResponseDTO.getDecks()[0].getId());
            FavouritePayload.put("userId",null);

            String payload = String.valueOf(FavouritePayload);
            System.out.println(payload);
            //DeckControllerRequestDTO = mapper.readValue(payload, DeckControllerRequestDTO.class);
            FavouriteControllerResponseDTO = favouriteQuestion(headerNormal, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(FavouriteControllerResponseDTO.getFieldErrors()[0].getField(),  "userId","Status Code");
            Assert.assertEquals(FavouriteControllerResponseDTO.getFieldErrors()[0].getMessage(), "Cannot be null or empty" ,"Status Code");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }


    @Test(priority =11) @AlmAnnotation(almTestId = "609180")
    public void FavouriteQuestionwithNullforquestionIdField() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            String QuestionId = QuestionControllerResponseDTO.getQuestions()[0].getId();
            JSONObject FavouritePayload = jsonReader.getJsonObject(FavouriteQuestionPayload);

            JSONObject favourites = new JSONObject();
            favourites.put("questionId"," ");
            favourites.put("updatedAt", Instant.now().toString());

            JSONArray favouritesKey = new JSONArray();
            favouritesKey.add(favourites);
            FavouritePayload.put("favourites",favouritesKey);
            FavouritePayload.put("userDeckId",DeckControllerResponseDTO.getDecks()[0].getId());

            String payload = String.valueOf(FavouritePayload);
            System.out.println(payload);
            //DeckControllerRequestDTO = mapper.readValue(payload, DeckControllerRequestDTO.class);
            FavouriteControllerResponseDTO = favouriteQuestion(headerNormal, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(FavouriteControllerResponseDTO.getFieldErrors()[0].getField(),  "favourites[].questionId","Status Code");
            Assert.assertEquals(FavouriteControllerResponseDTO.getFieldErrors()[0].getMessage(),  "'questionId' Cannot be null or empty","Status Code");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =12) @AlmAnnotation(almTestId = "609180")
    public void FavouriteQuestionwithInvalidDeckId() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            String QuestionId = QuestionControllerResponseDTO.getQuestions()[0].getId();
            JSONObject FavouritePayload = jsonReader.getJsonObject(FavouriteQuestionPayload);

            JSONObject favourites = new JSONObject();
            favourites.put("questionId",QuestionId);
            favourites.put("updatedAt", Instant.now().toString());

            JSONArray favouritesKey = new JSONArray();
            favouritesKey.add(favourites);
            FavouritePayload.put("favourites",favouritesKey);
            FavouritePayload.put("userDeckId","123");
            FavouritePayload.put("userId",DeckControllerResponseDTO.getDecks()[0].getUserId());

            String payload = String.valueOf(FavouritePayload);
            System.out.println(payload);
            FavouriteControllerResponseDTO = favouriteQuestion(headerNormal, payload);

            Assert.assertEquals(StatusCode, 404, "Status Code");
            Assert.assertEquals(FavouriteControllerResponseDTO.getDescription(),  "Invalid Deck ID","Status Code");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =13) @AlmAnnotation(almTestId = "609180")
    public void FavouriteQuestionwithInvalidTenantId() throws Exception {
        try {
            headerNormal.put("X-TenantId", "aaa");
            String QuestionId = "123";
            JSONObject FavouritePayload = jsonReader.getJsonObject(FavouriteQuestionPayload);

            JSONObject favourites = new JSONObject();
            favourites.put("questionId",QuestionId);
            favourites.put("updatedAt", Instant.now().toString());

            JSONArray favouritesKey = new JSONArray();
            favouritesKey.add(favourites);
            FavouritePayload.put("favourites",favouritesKey);
            FavouritePayload.put("userDeckId","123");

            String payload = String.valueOf(FavouritePayload);
            System.out.println(payload);
            FavouriteControllerResponseDTO = favouriteQuestion(headerNormal, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(FavouriteControllerResponseDTO.getDescription(),  "Field 'tenantId' does not have a valid value","Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =14) @AlmAnnotation(almTestId = "609180")
    public void FavouriteQuestionwithoutTenantId() throws Exception {
        try {
            headerNormal.put("X-TenantId", "");
            String QuestionId = "123";
            JSONObject FavouritePayload = jsonReader.getJsonObject(FavouriteQuestionPayload);

            JSONObject favourites = new JSONObject();
            favourites.put("questionId",QuestionId);
            favourites.put("updatedAt", Instant.now().toString());

            JSONArray favouritesKey = new JSONArray();
            favouritesKey.add(favourites);
            FavouritePayload.put("favourites",favouritesKey);
            FavouritePayload.put("userDeckId","123");

            String payload = String.valueOf(FavouritePayload);
            System.out.println(payload);
            FavouriteControllerResponseDTO = favouriteQuestion(headerNormal, payload);

            Assert.assertEquals(StatusCode, 403, "Status Code");
            Assert.assertEquals(FavouriteControllerResponseDTO.getDescription(),  "You do not have permission to access this method without a valid tenant ID","Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =15) @AlmAnnotation(almTestId = "609180")
    public void FavouriteQuestionwithInvalidTenantKey() throws Exception {
        try {
            getData();
            headerNormal.put("X-TenantKey", "aaa");
            String QuestionId = "123";
            JSONObject FavouritePayload = jsonReader.getJsonObject(FavouriteQuestionPayload);

            JSONObject favourites = new JSONObject();
            favourites.put("questionId",QuestionId);
            favourites.put("updatedAt", Instant.now().toString());

            JSONArray favouritesKey = new JSONArray();
            favouritesKey.add(favourites);
            FavouritePayload.put("favourites",favouritesKey);
            FavouritePayload.put("userDeckId","123");

            String payload = String.valueOf(FavouritePayload);
            System.out.println(payload);
            FavouriteControllerResponseDTO = favouriteQuestion(headerNormal, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(FavouriteControllerResponseDTO.getDescription(),  "Field 'tenantKey' does not have a valid value","Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =16) @AlmAnnotation(almTestId = "609180")
    public void FavouriteQuestionwithoutTenantKey() throws Exception {
        try {
            headerNormal.put("X-TenantKey", "");
            String QuestionId = "123";
            JSONObject FavouritePayload = jsonReader.getJsonObject(FavouriteQuestionPayload);

            JSONObject favourites = new JSONObject();
            favourites.put("questionId",QuestionId);
            favourites.put("updatedAt", Instant.now().toString());

            JSONArray favouritesKey = new JSONArray();
            favouritesKey.add(favourites);
            FavouritePayload.put("favourites",favouritesKey);
            FavouritePayload.put("userDeckId","123");

            String payload = String.valueOf(FavouritePayload);
            System.out.println(payload);
            FavouriteControllerResponseDTO = favouriteQuestion(headerNormal, payload);

            Assert.assertEquals(StatusCode, 403, "Status Code");
            Assert.assertEquals(FavouriteControllerResponseDTO.getDescription(),  "You do not have permission to access this method without a valid tenant Key","Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =17) @AlmAnnotation(almTestId = "609180")
    public void FavouriteQuestionUnauthorizedError() throws Exception {
        try {
            headerNormal.put("X-Authorization", "");
            String QuestionId = "123";
            JSONObject FavouritePayload = jsonReader.getJsonObject(FavouriteQuestionPayload);

            JSONObject favourites = new JSONObject();
            favourites.put("questionId",QuestionId);
            favourites.put("updatedAt", Instant.now().toString());

            JSONArray favouritesKey = new JSONArray();
            favouritesKey.add(favourites);
            FavouritePayload.put("favourites",favouritesKey);
            FavouritePayload.put("userDeckId","123");

            String payload = String.valueOf(FavouritePayload);
            System.out.println(payload);
            FavouriteControllerResponseDTO = favouriteQuestion(headerNormal, payload);

            Assert.assertEquals(StatusCode, 401, "Status Code");
            Assert.assertEquals(FavouriteControllerResponseDTO.getError(),  "Unauthorized","Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 18) @AlmAnnotation(almTestId = "609180")
    public void GetFavouriteQuestionwithInvalidDeckId() throws Exception {
        try {
            getData();
            String deckId = "123";
            FavouriteControllerResponseDTO = getfavouriteQuestion(headerNormal, deckId);

            Assert.assertEquals(StatusCode, 404, "Status Code");
            Assert.assertEquals(FavouriteControllerResponseDTO.getDescription(), "Invalid Deck ID", "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 19) @AlmAnnotation(almTestId = "609180")
    public void GetFavouriteQuestionwithDeletedDeckId() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            FavouriteQuestion();

            String QuestionId = QuestionControllerResponseDTO.getQuestions()[0].getId();
            String deckId = DeckControllerResponseDTO.getDecks()[0].getId();

            DeleteDeckWithFullPayload();
            FavouriteControllerResponseDTO = getfavouriteQuestion(headerNormal, deckId);

            Assert.assertEquals(StatusCode, 404, "Status Code");
            Assert.assertEquals(FavouriteControllerResponseDTO.getDescription(), "Invalid Deck ID", "Status Code");

        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 20) @AlmAnnotation(almTestId = "609180")
    public void GetFavouriteQuestionwithoutTenantId() throws Exception {
        try {
            headerNormal.put("X-TenantId", "");
            String deckId = "123";

            FavouriteControllerResponseDTO = getfavouriteQuestion(headerNormal, deckId);

            Assert.assertEquals(StatusCode, 403, "Status Code");
            Assert.assertEquals(FavouriteControllerResponseDTO.getDescription(), "You do not have permission to access this method without a valid tenant ID", "Status Code");

        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 21) @AlmAnnotation(almTestId = "609180")
    public void GetFavouriteQuestionwithInvalidTenantId() throws Exception {
        try {
            headerNormal.put("X-TenantId", "aaa");
            String deckId = "123";

            FavouriteControllerResponseDTO = getfavouriteQuestion(headerNormal, deckId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(FavouriteControllerResponseDTO.getDescription(), "Field 'tenantId' does not have a valid value", "Status Code");

        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 22) @AlmAnnotation(almTestId = "609180")
    public void GetFavouriteQuestionwithoutTenantKey() throws Exception {
        try {
            getData();
            headerNormal.put("X-TenantKey", "");
            String deckId = "123";

            FavouriteControllerResponseDTO = getfavouriteQuestion(headerNormal, deckId);

            Assert.assertEquals(StatusCode, 403, "Status Code");
            Assert.assertEquals(FavouriteControllerResponseDTO.getDescription(), "You do not have permission to access this method without a valid tenant Key", "Status Code");

        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 23) @AlmAnnotation(almTestId = "609180")
    public void GetFavouriteQuestionwithInvalidTenantKey() throws Exception {
        try {
            headerNormal.put("X-TenantKey", "aaa");
            String deckId = "123";

            FavouriteControllerResponseDTO = getfavouriteQuestion(headerNormal, deckId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(FavouriteControllerResponseDTO.getDescription(), "Field 'tenantKey' does not have a valid value", "Status Code");

        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 24) @AlmAnnotation(almTestId = "609180")
    public void GetFavouriteQuestionwithUnauthorizedError() throws Exception {
        try {
            getData();
            headerNormal.put("X-Authorization", "aaa");
            String deckId = "123";

            FavouriteControllerResponseDTO = getfavouriteQuestion(headerNormal, deckId);

            Assert.assertEquals(StatusCode, 401, "Status Code");
            Assert.assertEquals(FavouriteControllerResponseDTO.getError(), "Unauthorized", "Status Code");

        } catch (Exception e) {
            throw e;
        }
    }






    //@Test (priority = 4)
//    public void publishMediaAssignments() throws Exception{
//        //JSONObject publishMSAssignmentPayload = jsonReader.getJsonObject(publishMediaAssignmentPayload);
//        System.out.println("YT: " + DataStore.getInstance().getMediaIDYouTube());
//        System.out.println("Web: " + DataStore.getInstance().getMediaIDWebLink());
//       // publishMSAssignmentPayload.put("courseId",courseID);
//       // publishMSAssignmentPayload.put("sectionId",courseID);
//
//        JSONArray media = new JSONArray();
//        media.add(DataStore.getInstance().getMediaIDYouTube());
//        media.add(DataStore.getInstance().getMediaIDWebLink());

    // publishMSAssignmentPayload.put("assignmentId", DataStore.getInstance().getMultimediaAssignmentYouTube() );
    // publishMSAssignmentPayload.put("title", DataStore.getInstance().getMultimediaAssignmentNameYouTube() );
    //  publishMSAssignmentPayload.put("media", media);
    // System.out.println("publishMSAssignmentPayload: " + publishMSAssignmentPayload);
    //  Response response = RESTServiceBase.putCallWithHeaderAndBodyParam(header,String.valueOf(publishMSAssignmentPayload),publishMediaAssignmentUrl + "mediaAssignments/" + DataStore.getInstance().getMultimediaAssignmentYouTube());
//        LoggerUtil.log(response.asString());
//        Assert.assertEquals(response.statusCode(), 200);
//        System.out.println(response);
//        System.out.println("Response Code is: " + response.statusCode());
//       String titleName = "MMA with You Tube N Web Link "+ GetCurrentTime.getCurrentTimestamp();
//        System.out.println("Media Assignment is:" + titleName);
//        DataStore.getInstance().setMultimediaAssignmentNameYouTube(titleName);
}
