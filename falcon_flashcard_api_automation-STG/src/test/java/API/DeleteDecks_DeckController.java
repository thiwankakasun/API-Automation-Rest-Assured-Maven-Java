package API;

import com.pearson.common.framework.shared.alm.AlmAnnotation;
import org.apache.poi.EncryptedDocumentException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import pojos.PiToken;
import requestDTO.DeckControllerRequestDTO;
import requestDTO.DeckRequestDTO;
import responseDTO.DeckControllerResponseDTO;
import responseDTO.DeckResponseDTO;
import responseDTO.PostRecommendedActivitiesResponseDTO;
import responseDTO.QuestionDTO;
import utils.JsonReader;
import utils.PiTokenGenarator;
import utils.PiTokenGenaratorNormalUser;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.logging.Logger;

public class DeleteDecks_DeckController extends ServiceController {

    private DeckControllerResponseDTO DeckControllerResponseDTO;
    private DeckControllerRequestDTO DeckControllerRequestDTO;
    private responseDTO.QuestionControllerResponseDTO QuestionControllerResponseDTO;
    private DeckResponseDTO[] DeckResponseDTO;
    private DeckResponseDTO DeckUpdateDTO;
    private QuestionDTO QuestionDTO;
    private responseDTO.GetRecommendationResponseDTO GetRecommendationResponseDTO;
    private PostRecommendedActivitiesResponseDTO PostRecommendedActivitiesResponseDTO;
    private DeckRequestDTO DeckRequestDTO;
//    private TenantController PiTokenGenarator1 = new TenantController();
//    private QuestionController PiTokenGenarator2= new QuestionController();
//    private FavouriteController PiTokenGenarator3 = new FavouriteController();
    private static final PiTokenGenarator PiTokenGenarator = new PiTokenGenarator();
    private static final PiTokenGenaratorNormalUser PiTokenGenaratorNormalUser = new PiTokenGenaratorNormalUser();
    private static final HashMap<String, String> headerNormal = new HashMap<>();
    private static final HashMap<String, String> header = new HashMap<>();
    private static final HashMap<String, String> queryparam = new HashMap<>();
    JsonReader jsonReader = new JsonReader();

    private static final Logger LOGGER = Logger.getLogger(String.valueOf(DeleteDecks_DeckController.class));

    @BeforeTest
    public void getData() throws EncryptedDocumentException, IOException {
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
        }
        catch (Exception e){
            throw  e;
        }

    }

    //Verify user being able to create single deck using tenent id etext
    @Test(priority = 1) @AlmAnnotation(almTestId = "584138")
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

    @Test(priority = 3) @AlmAnnotation(almTestId = "609861")
    public void GetRecommendedCards() throws Exception {
        try {
            // Read json object from file
            JSONObject getRecommendationPayload = jsonReader.getJsonObject(GetRecommendationPayload);
            getRecommendationPayload.put("deck",DeckControllerResponseDTO.getDecks()[0].getId());
            getRecommendationPayload.put("person",DeckControllerResponseDTO.getDecks()[0].getUserId());

            String payload = String.valueOf(getRecommendationPayload);
            //GetRecommendationResponseDTO = mapper.readValue(payload, GetRecommendationResponseDTO.class);
            GetRecommendationResponseDTO = getRecommendation(headerNormal, payload);

            Assert.assertEquals(StatusCode, 200, "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 4) @AlmAnnotation(almTestId = "609861")
    public void PostRecommendedActivities() throws Exception {
        try {
            // Read json object from file
            JSONObject postRecommendedActivitiesPayload = jsonReader.getJsonObject(PostRecommendedActivitiesPayload);
            postRecommendedActivitiesPayload.put("deck",DeckControllerResponseDTO.getDecks()[0].getId());
            postRecommendedActivitiesPayload.put("person",DeckControllerResponseDTO.getDecks()[0].getUserId());
            JSONObject event = new JSONObject();
            event.put("eventType", "ANSWERED");
            event.put("eventTime", Instant.now().toString());
            event.put("score",  1);
            JSONArray events = new JSONArray();
            events.add(event);

            JSONObject activity = new JSONObject();
            activity.put("activityId", "activityId1");
            activity.put("cardId", GetRecommendationResponseDTO.getSession().getCards()[0]);
            activity.put("cardType", "flashCard");
            activity.put("startTime", Instant.now().toString());
            activity.put("endTime", Instant.now().toString());
            activity.put("sessionId", GetRecommendationResponseDTO.getSession().getSessionId());
            activity.put("events", events);
            JSONArray activities = new JSONArray();
            activities.add(activity);
            postRecommendedActivitiesPayload.put("activities",activities);
            String payload = String.valueOf(postRecommendedActivitiesPayload);
            //PostRecommendedActivitiesRequestDTO = mapper.readValue(payload, PostRecommendedActivitiesRequestDTO.class);
            PostRecommendedActivitiesResponseDTO = postRecommendedActivities(headerNormal, payload);

            Assert.assertEquals(StatusCode, 201, "Status Code");
            Assert.assertEquals(PostRecommendedActivitiesResponseDTO.getMessage(), "Number of activities processed "+PostRecommendedActivitiesResponseDTO.getRecommendation().getSession().getCards().length, "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 5) @AlmAnnotation(almTestId = "609180")
    public void GetDeckUsingProductId() throws Exception {
        try {
            String UserId = DeckControllerResponseDTO.getDecks()[0].getUserId();
            LOGGER.info("DeckId: " + UserId);
            queryparam.put("productId",DeckControllerResponseDTO.getDecks()[0].getProductId());
            DeckResponseDTO = getDeck(headerNormal, queryparam, UserId);

            Assert.assertEquals(StatusCode, 200, "Status Code");

        } catch (Exception e) {
            throw e;
        }
    }

    //Verify whether the epoch times are updated accurately
    @Test(priority = 6) @AlmAnnotation(almTestId = "583208")
    public void UpdateDeckWithFullPayload() throws Exception {
        try {
            JSONObject updateDeckPayload = jsonReader.getJsonObject(DeckUpdatePayload);
            System.out.println(updateDeckPayload);
            String payload = String.valueOf(updateDeckPayload);
            DeckRequestDTO = mapper.readValue(payload, DeckRequestDTO.class);

            String DeckId = DeckControllerResponseDTO.getDecks()[0].getId();
            DeckUpdateDTO = updateDeck(headerNormal, payload, DeckId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertNotNull(DeckControllerResponseDTO.getDecks()[0].getUpdatedAt());

        } catch (Exception e) {
            throw e;
        }
    }

    //Delete the created deck
    @Test(priority = 7) @AlmAnnotation(almTestId = "598683")
    public void DeleteDeckWithFullPayload() throws Exception {

        try {
            getData();
            String DeckId = DeckControllerResponseDTO.getDecks()[0].getId();
            DeckControllerResponseDTO = deleteDeck(headerNormal, DeckId);

            Assert.assertEquals(StatusCode, 204, "Status Code");

        } catch (Exception e) {
            throw e;
        }
    }

    //Verify whether the user is not able to delete decks which were not created by him
    @Test(priority =8) @AlmAnnotation(almTestId = "583211")
    public void DeleteDeckWithUsingAnyUser_which_was_not_created_by_him() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            String DeckId = DeckControllerResponseDTO.getDecks()[0].getId();
            DeckControllerResponseDTO = deleteDeck(header, DeckId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(DeckControllerResponseDTO.getDescription(), "User IDs do not match", "Status Code");

            DeckControllerResponseDTO = deleteDeck(headerNormal, DeckId);
            Assert.assertEquals(StatusCode, 204, "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    //Verify deleting questions belonging to a deleted deck
    @Test(priority =9) @AlmAnnotation(almTestId = "583216")
    public void DeleteDeckWith_Verify_deleting_questions_belonging_to_a_deleted_deck() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            DeleteDeckWithFullPayload();
            Thread.sleep(15000);
            String QuestionId = QuestionControllerResponseDTO.getQuestions()[0].getId();
            QuestionDTO = getQuestionByQuestionId(headerNormal, QuestionId);

            Assert.assertEquals(StatusCode, 404, "Status Code");
            Assert.assertEquals(QuestionDTO.getDescription(), "Couldn't find question with id : "+QuestionId, "Status Code");

        } catch (Exception e) {
            throw e;
        }
    }

    //Verify whether an error is thrown when the user tries to delete an deleted deck
    @Test(priority =10) @AlmAnnotation(almTestId = "583215")
    public void DeleteDeckWithDeletedDeckId() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            String DeckId = DeckControllerResponseDTO.getDecks()[0].getId();
            DeckControllerResponseDTO = deleteDeck(headerNormal, DeckId);
            Assert.assertEquals(StatusCode, 204, "Status Code");

            DeckControllerResponseDTO = deleteDeck(headerNormal, DeckId);
            Assert.assertEquals(StatusCode, 404, "Status Code");
            Assert.assertEquals(DeckControllerResponseDTO.getDescription(), "Invalid Deck ID", "Status Code");

        } catch (Exception e) {
            throw e;
        }
    }

    //Verify whether an error is thrown when the user tries to delete an deck using invalid deck Id
    @Test(priority =11) @AlmAnnotation(almTestId = "583217")
    public void DeleteDeckWithInvalidDeckId() throws Exception {
        try {
            String DeckId = "1234";

            DeckControllerResponseDTO = deleteDeck(headerNormal, DeckId);
            Assert.assertEquals(StatusCode, 404, "Status Code");
            Assert.assertEquals(DeckControllerResponseDTO.getDescription(), "Invalid Deck ID", "Status Code");


        } catch (Exception e) {
            throw e;
        }
    }

    //Verify the tenantId field when an invalid value is sent
    @Test(priority =12) @AlmAnnotation(almTestId = "583212")
    public void DeleteDeckWithInvalidTenantId() throws Exception {
        try {
            String DeckId = "a";
            headerNormal.put("X-TenantId","a");
            DeckControllerResponseDTO = deleteDeck(headerNormal, DeckId);
            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(DeckControllerResponseDTO.getDescription(), "Field 'tenantId' does not have a valid value", "Status Code");

        } catch (Exception e) {
            throw e;
        }
    }

    //Verify the tenantId field when an empty value is sent
    @Test(priority =13) @AlmAnnotation(almTestId = "583213")
    public void DeleteDeckWithoutTenantId() throws Exception {
        try {
            String DeckId = "a";
            headerNormal.put("X-TenantId","");
            DeckControllerResponseDTO = deleteDeck(headerNormal, DeckId);
            Assert.assertEquals(StatusCode, 403, "Status Code");
            Assert.assertEquals(DeckControllerResponseDTO.getDescription(), "You do not have permission to access this method without a valid tenant ID", "Status Code");

        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =14) @AlmAnnotation(almTestId = "611101")
    public void DeleteDeckWithInvalidTenantKey() throws Exception {
        try {
            getData();
            String DeckId = "a";
            headerNormal.put("X-TenantKey","a");
            DeckControllerResponseDTO = deleteDeck(headerNormal, DeckId);
            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(DeckControllerResponseDTO.getDescription(), "Field 'tenantKey' does not have a valid value", "Status Code");

        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =15) @AlmAnnotation(almTestId = "611101")
    public void DeleteDeckWithUnauthorizedError() throws Exception {
        try {
            String DeckId = "a";
            headerNormal.put("X-Authorization","a");
            DeckControllerResponseDTO = deleteDeck(headerNormal, DeckId);
            Assert.assertEquals(StatusCode, 401, "Status Code");
            Assert.assertEquals(DeckControllerResponseDTO.getError(), "Unauthorized", "Status Code");

        } catch (Exception e) {
            throw e;
        }
    }

}
