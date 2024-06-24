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
import requestDTO.GetRecommendationRequestDTO;
import responseDTO.*;
import utils.JsonReader;
import utils.PiTokenGenarator;
import utils.PiTokenGenaratorNormalUser;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.logging.Logger;

public class LearningAnalyticsController extends ServiceController {

    private DeckControllerResponseDTO DeckControllerResponseDTO;
    private DeckControllerRequestDTO DeckControllerRequestDTO;
    private QuestionControllerResponseDTO QuestionControllerResponseDTO;
    private GetRecommendationResponseDTO GetRecommendationResponseDTO;
    private GetActivitiesOfDecksResponseDTO GetActivitiesOfDecksResponseDTO;
    private GetRecommendationRequestDTO GetRecommendationRequestDTO;
    private PostRecommendedActivitiesResponseDTO PostRecommendedActivitiesResponseDTO;
    private DeckResponseDTO[] DeckResponseDTO;
    private DeckResponseDTO DeckUpdateDTO;
    private DeckRequestDTO DeckRequestDTO;
    private static final utils.PiTokenGenarator PiTokenGenarator = new PiTokenGenarator();
    private static final utils.PiTokenGenaratorNormalUser PiTokenGenaratorNormalUser = new PiTokenGenaratorNormalUser();
    private static final HashMap<String, String> headerNormal = new HashMap<>();
    private static final HashMap<String, String> header = new HashMap<>();
    private static final HashMap<String, String> queryparam = new HashMap<>();
    JsonReader jsonReader = new JsonReader();

    private static final Logger LOGGER = Logger.getLogger(String.valueOf(LearningAnalyticsController.class));
    private FavouriteControllerResponseDTO FavouriteControllerResponseDTO;

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

    @Test(priority = 1)
    public void CreateSingleDeckWithFullPayload() throws Exception {
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

    @Test(priority = 5) @AlmAnnotation(almTestId = "609861")
    public void GetActivitiesOfDecks() throws Exception {
        try {
            // Read json object from file
            JSONObject getActivitiesOfDecksPayload = jsonReader.getJsonObject(GetActivitiesOfDecksPayload);

            JSONArray deckIds = new JSONArray();
            deckIds.add(DeckControllerResponseDTO.getDecks()[0].getId());
            getActivitiesOfDecksPayload.put("deckIds",deckIds);
            getActivitiesOfDecksPayload.put("studentId",DeckControllerResponseDTO.getDecks()[0].getUserId());

            String payload = String.valueOf(getActivitiesOfDecksPayload);
            //PostRecommendedActivitiesRequestDTO = mapper.readValue(payload, PostRecommendedActivitiesRequestDTO.class);
            GetActivitiesOfDecksResponseDTO = getActivitiesOfDecks(headerNormal, payload);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getDeckId(), DeckControllerResponseDTO.getDecks()[0].getId(), "Status Code");
            Assert.assertNotNull(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getLearnedCards(),  "Status Code");
            Assert.assertNotNull(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getSeenCards(), "Status Code");
            Assert.assertNotNull(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getUnseenCards(),"Status Code");
            Assert.assertNotNull(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getFavoriteCards(),  "Status Code");
            Assert.assertNotNull(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getCorrectCards(), "Status Code");
            Assert.assertNotNull(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getTotalCards(), "Status Code");
            Assert.assertNotNull(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getRecentActivity().getCorrectCards(),  "Status Code");
            Assert.assertNotNull(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getRecentActivity().getTotalCards(),  "Status Code");

        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 6) @AlmAnnotation(almTestId = "609180")
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

    @Test(priority = 7) @AlmAnnotation(almTestId = "611108")
    public void DeleteSingleQuestionWithFullPayload() throws Exception {

        try {
            String QuestionId = QuestionControllerResponseDTO.getQuestions()[0].getId();
            QuestionControllerResponseDTO = deleteQuestion(headerNormal, QuestionId);

            Assert.assertEquals(StatusCode, 204, "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }
    @Test(priority = 8) @AlmAnnotation(almTestId = "611108")
    public void DeleteDeckWithFullPayload() throws Exception {

        try {
            String DeckId = DeckControllerResponseDTO.getDecks()[0].getId();
            DeckControllerResponseDTO = deleteDeck(headerNormal, DeckId);

            Assert.assertEquals(StatusCode, 204, "Status Code");

        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 9) @AlmAnnotation(almTestId = "609861")
    public void GetRecommendedCardsWithoutPersonIdField() throws Exception {
        try {
            CreateSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            // Read json object from file
            JSONObject getRecommendationPayload = jsonReader.getJsonObject(GetRecommendationPayload);
            //getRecommendationPayload.put("deck",DeckControllerResponseDTO.getDecks()[0].getId());
            getRecommendationPayload.put("person",null);

            String payload = String.valueOf(getRecommendationPayload);
            //GetRecommendationResponseDTO = mapper.readValue(payload, GetRecommendationResponseDTO.class);
            GetRecommendationResponseDTO = getRecommendation(headerNormal, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(GetRecommendationResponseDTO.getFieldErrors()[0].getField(),"person","Status Code");
            Assert.assertEquals(GetRecommendationResponseDTO.getFieldErrors()[0].getMessage(),"username is required","Status Code");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 10) @AlmAnnotation(almTestId = "609861")
    public void GetRecommendedCardsWithInvalidPersonIdField() throws Exception {
        try {
            CreateSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            // Read json object from file
            JSONObject getRecommendationPayload = jsonReader.getJsonObject(GetRecommendationPayload);
            //getRecommendationPayload.put("deck",DeckControllerResponseDTO.getDecks()[0].getId());
            getRecommendationPayload.put("person","ffffffff605d9c3f41e1746864b37b32");

            String payload = String.valueOf(getRecommendationPayload);
            //GetRecommendationResponseDTO = mapper.readValue(payload, GetRecommendationResponseDTO.class);
            GetRecommendationResponseDTO = getRecommendation(headerNormal, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(GetRecommendationResponseDTO.getDescription(),"User IDs do not match","Status Code");
            //Assert.assertEquals(GetRecommendationResponseDTO.getFieldErrors()[0].getMessage(),"username is required","Status Code");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 11) @AlmAnnotation(almTestId = "609861")
    public void GetRecommendedCardsWithoutDeckIdField() throws Exception {
        try {
//            CreateSingleDeckWithFullPayload();
//            CreateQuestionWithFullPayload();
            // Read json object from file
            JSONObject getRecommendationPayload = jsonReader.getJsonObject(GetRecommendationPayload);
            //getRecommendationPayload.put("deck",DeckControllerResponseDTO.getDecks()[0].getId());
            getRecommendationPayload.put("deck"," ");

            String payload = String.valueOf(getRecommendationPayload);
            //GetRecommendationResponseDTO = mapper.readValue(payload, GetRecommendationResponseDTO.class);
            GetRecommendationResponseDTO = getRecommendation(headerNormal, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(GetRecommendationResponseDTO.getFieldErrors()[0].getMessage(),"deck id is required","Status Code");
            //Assert.assertEquals(GetRecommendationResponseDTO.getFieldErrors()[0].getMessage(),"username is required","Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 12) @AlmAnnotation(almTestId = "609861")
    public void GetRecommendedCardsWithInvalidDeckIdField() throws Exception {
        try {
            CreateSingleDeckWithFullPayload();
//            CreateQuestionWithFullPayload();
            // Read json object from file
            JSONObject getRecommendationPayload = jsonReader.getJsonObject(GetRecommendationPayload);
            //getRecommendationPayload.put("deck",DeckControllerResponseDTO.getDecks()[0].getId());
            getRecommendationPayload.put("deck","jehfwohfoeij");
            getRecommendationPayload.put("person",DeckControllerResponseDTO.getDecks()[0].getUserId());

            String payload = String.valueOf(getRecommendationPayload);
            //GetRecommendationResponseDTO = mapper.readValue(payload, GetRecommendationResponseDTO.class);
            GetRecommendationResponseDTO = getRecommendation(headerNormal, payload);

            Assert.assertEquals(StatusCode, 404, "Status Code");
            Assert.assertEquals(GetRecommendationResponseDTO.getDescription(),"Invalid Deck ID","Status Code");
            //Assert.assertEquals(GetRecommendationResponseDTO.getFieldErrors()[0].getMessage(),"username is required","Status Code");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 13) @AlmAnnotation(almTestId = "609861")
    public void GetRecommendedCardsWithNullforDeckIdField() throws Exception {
        try {
//            CreateSingleDeckWithFullPayload();
//            CreateQuestionWithFullPayload();
            // Read json object from file
            JSONObject getRecommendationPayload = jsonReader.getJsonObject(GetRecommendationPayload);
            //getRecommendationPayload.put("deck",DeckControllerResponseDTO.getDecks()[0].getId());
            getRecommendationPayload.put("deck",null);

            String payload = String.valueOf(getRecommendationPayload);
            //GetRecommendationResponseDTO = mapper.readValue(payload, GetRecommendationResponseDTO.class);
            GetRecommendationResponseDTO = getRecommendation(headerNormal, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(GetRecommendationResponseDTO.getFieldErrors()[0].getMessage(),"deck id is required","Status Code");
            //Assert.assertEquals(GetRecommendationResponseDTO.getFieldErrors()[0].getMessage(),"username is required","Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 14) @AlmAnnotation(almTestId = "609861")
    public void GetRecommendedCardsWithoutAnyCards() throws Exception {
        try {
            CreateSingleDeckWithFullPayload();
//            CreateQuestionWithFullPayload();
            // Read json object from file
            JSONObject getRecommendationPayload = jsonReader.getJsonObject(GetRecommendationPayload);
            getRecommendationPayload.put("deck",DeckControllerResponseDTO.getDecks()[0].getId());
            getRecommendationPayload.put("person",DeckControllerResponseDTO.getDecks()[0].getUserId());

            String payload = String.valueOf(getRecommendationPayload);
            GetRecommendationRequestDTO = mapper.readValue(payload, GetRecommendationRequestDTO.class);
            GetRecommendationResponseDTO = getRecommendation(headerNormal, payload);

            Assert.assertEquals(StatusCode, 404, "Status Code");
            Assert.assertEquals(GetRecommendationResponseDTO.getDescription(),"Questions are not available in the given deck","Status Code");
            Assert.assertEquals(GetRecommendationResponseDTO.getResourceId(),DeckControllerResponseDTO.getDecks()[0].getId(),"Status Code");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 15) @AlmAnnotation(almTestId = "609861")
    public void GetRecommendedCardsWithValidatingMaximumCardsPerSession() throws Exception {
        try {
            CreateSingleDeckWithFullPayload();
            for (int i=0; i<=20; i++)
                CreateQuestionWithFullPayload();
            // Read json object from file
            JSONObject getRecommendationPayload = jsonReader.getJsonObject(GetRecommendationPayload);
            getRecommendationPayload.put("deck",DeckControllerResponseDTO.getDecks()[0].getId());
            getRecommendationPayload.put("person",DeckControllerResponseDTO.getDecks()[0].getUserId());

            String payload = String.valueOf(getRecommendationPayload);
            GetRecommendationRequestDTO = mapper.readValue(payload, GetRecommendationRequestDTO.class);
            GetRecommendationResponseDTO = getRecommendation(headerNormal, payload);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            for (int i=0; i<20; i++)
                Assert.assertNotNull(GetRecommendationResponseDTO.getSession().getCards()[i],"Status Code");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 16) @AlmAnnotation(almTestId = "609861")
    public void GetRecommendationForFavoriteCards() throws Exception {
        try {
            CreateSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            CreateQuestionWithFullPayload();
            FavouriteQuestion();

            // Read json object from file
            JSONObject getRecommendationPayload = jsonReader.getJsonObject(GetRecommendationPayload);
            getRecommendationPayload.put("deck",DeckControllerResponseDTO.getDecks()[0].getId());
            getRecommendationPayload.put("person",DeckControllerResponseDTO.getDecks()[0].getUserId());
            getRecommendationPayload.put("context","FAVORITE");
            getRecommendationPayload.put("contextType","PREFERENCE");

            String payload = String.valueOf(getRecommendationPayload);
            //GetRecommendationRequestDTO = mapper.readValue(payload, GetRecommendationRequestDTO.class);
            GetRecommendationResponseDTO = getRecommendation(headerNormal, payload);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(GetRecommendationResponseDTO.getSession().getCards()[0], FavouriteControllerResponseDTO.getFavourites()[0].getQuestionId(), "Status Code");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    //Check that the deleted cards are not return to the recommendation
    @Test(priority = 17) @AlmAnnotation(almTestId = "609861")
    public void GetRecommendationWithVerify_deleted_cards_are_not_return_in_the_recommendation() throws Exception {
        try {
            CreateSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            DeleteSingleQuestionWithFullPayload();

            // Read json object from file
            JSONObject getRecommendationPayload = jsonReader.getJsonObject(GetRecommendationPayload);
            getRecommendationPayload.put("deck",DeckControllerResponseDTO.getDecks()[0].getId());
            getRecommendationPayload.put("person",DeckControllerResponseDTO.getDecks()[0].getUserId());

            String payload = String.valueOf(getRecommendationPayload);
            //GetRecommendationRequestDTO = mapper.readValue(payload, GetRecommendationRequestDTO.class);
            GetRecommendationResponseDTO = getRecommendation(headerNormal, payload);

            Assert.assertEquals(StatusCode, 404, "Status Code");
            Assert.assertEquals(GetRecommendationResponseDTO.getDescription(), "Questions are not available in the given deck", "Status Code");
            Assert.assertEquals(GetRecommendationResponseDTO.getResourceId(), DeckControllerResponseDTO.getDecks()[0].getId(), "Status Code");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 18) @AlmAnnotation(almTestId = "609861")
    public void GetRecommendationWithDeletedDeckId() throws Exception {
        try {
            CreateSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();

            // Read json object from file
            JSONObject getRecommendationPayload = jsonReader.getJsonObject(GetRecommendationPayload);
            getRecommendationPayload.put("deck",DeckControllerResponseDTO.getDecks()[0].getId());
            getRecommendationPayload.put("person",DeckControllerResponseDTO.getDecks()[0].getUserId());

            DeleteDeckWithFullPayload();
            String payload = String.valueOf(getRecommendationPayload);
            //GetRecommendationRequestDTO = mapper.readValue(payload, GetRecommendationRequestDTO.class);
            GetRecommendationResponseDTO = getRecommendation(headerNormal, payload);

            Assert.assertEquals(StatusCode, 404, "Status Code");
            Assert.assertEquals(GetRecommendationResponseDTO.getDescription(), "Invalid Deck ID", "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 19) @AlmAnnotation(almTestId = "609861")
    public void GetRecommendationWithVerifyingMasteryValueisZeroinBegining() throws Exception {
        try {
            CreateSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();

            // Read json object from file
            JSONObject getRecommendationPayload = jsonReader.getJsonObject(GetRecommendationPayload);
            getRecommendationPayload.put("deck",DeckControllerResponseDTO.getDecks()[0].getId());
            getRecommendationPayload.put("person",DeckControllerResponseDTO.getDecks()[0].getUserId());

            String payload = String.valueOf(getRecommendationPayload);
            //GetRecommendationRequestDTO = mapper.readValue(payload, GetRecommendationRequestDTO.class);
            GetRecommendationResponseDTO = getRecommendation(headerNormal, payload);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(GetRecommendationResponseDTO.getMastery(), "0.0", "Status Code");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 20) @AlmAnnotation(almTestId = "609861")
    public void GetRecommendationWithPrepTenantId() throws Exception {
        try {
            CreateSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();

            if(Env.equals("QA") || Env.equals("DEV")){
            headerNormal.put("X-TenantId","5660deb9-7b3b-44e8-8a6c-4b1fdae17308");
            headerNormal.put("X-TenantKey","b308daa4-2ed4-40a0-ab8c-49fda99994a3");}
            else if(Env.equals("STG" ) || Env.equals("NFT")){
                headerNormal.put("X-TenantId","05d8aa57-c407-483b-ad3c-3c8f8d8c116f");
                headerNormal.put("X-TenantKey","1ef03454-0021-4cf6-8fbb-516ec4570173");}
            else{
                headerNormal.put("X-TenantId","ff74661e-8585-4624-b512-be689fc820ce");
                headerNormal.put("X-TenantKey","70a05a9c-fea1-4c1f-9f44-1acb2662d3c4");
            }
            // Read json object from file
            JSONObject getRecommendationPayload = jsonReader.getJsonObject(GetRecommendationPayload);
            getRecommendationPayload.put("deck",DeckControllerResponseDTO.getDecks()[0].getId());
            getRecommendationPayload.put("person",DeckControllerResponseDTO.getDecks()[0].getUserId());

            String payload = String.valueOf(getRecommendationPayload);
            //GetRecommendationRequestDTO = mapper.readValue(payload, GetRecommendationRequestDTO.class);
            GetRecommendationResponseDTO = getRecommendation(headerNormal, payload);

            Assert.assertEquals(StatusCode, 404, "Status Code");
            Assert.assertEquals(GetRecommendationResponseDTO.getDescription(), "Invalid Deck ID", "Status Code");

            getData();
            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 21) @AlmAnnotation(almTestId = "609861")
    public void GetRecommendationWithInvalidTenantId() throws Exception {
        try {
            headerNormal.put("X-TenantId","a");
            // Read json object from file
            JSONObject getRecommendationPayload = jsonReader.getJsonObject(GetRecommendationPayload);

            String payload = String.valueOf(getRecommendationPayload);
            GetRecommendationResponseDTO = getRecommendation(headerNormal, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(GetRecommendationResponseDTO.getDescription(), "Field 'tenantId' does not have a valid value", "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 22) @AlmAnnotation(almTestId = "609861")
    public void GetRecommendationWithInvalidTenantKey() throws Exception {
        try {
            getData();
            headerNormal.put("X-TenantKey","a");
            // Read json object from file
            JSONObject getRecommendationPayload = jsonReader.getJsonObject(GetRecommendationPayload);

            String payload = String.valueOf(getRecommendationPayload);
            GetRecommendationResponseDTO = getRecommendation(headerNormal, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(GetRecommendationResponseDTO.getDescription(), "Field 'tenantKey' does not have a valid value", "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 23) @AlmAnnotation(almTestId = "609861")
    public void GetRecommendationWithUnauthorizedError() throws Exception {
        try {
            headerNormal.put("X-Authorization","a");

            // Read json object from file
            JSONObject getRecommendationPayload = jsonReader.getJsonObject(GetRecommendationPayload);

            String payload = String.valueOf(getRecommendationPayload);
            GetRecommendationResponseDTO = getRecommendation(headerNormal, payload);

            Assert.assertEquals(StatusCode, 401, "Status Code");
            Assert.assertEquals(GetRecommendationResponseDTO.getError(), "Unauthorized", "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }


    @Test(priority = 24) @AlmAnnotation(almTestId = "609861")
    public void PostRecommendedActivitiesWithoutUserIdinRequestPayload() throws Exception {
        try {
            getData();
            CreateSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            GetRecommendedCards();
            // Read json object from file
            JSONObject postRecommendedActivitiesPayload = jsonReader.getJsonObject(PostRecommendedActivitiesPayload);
            postRecommendedActivitiesPayload.put("deck",DeckControllerResponseDTO.getDecks()[0].getId());
            postRecommendedActivitiesPayload.put("person","");

            String payload = String.valueOf(postRecommendedActivitiesPayload);
            PostRecommendedActivitiesResponseDTO = postRecommendedActivities(headerNormal, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(PostRecommendedActivitiesResponseDTO.getDescription(),"User IDs do not match");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 25) @AlmAnnotation(almTestId = "609861")
    public void PostRecommendedActivitiesWithoutDeckIdinRequestPayload() throws Exception {
        try {
            getData();
            CreateSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            GetRecommendedCards();
            // Read json object from file
            JSONObject postRecommendedActivitiesPayload = jsonReader.getJsonObject(PostRecommendedActivitiesPayload);
            postRecommendedActivitiesPayload.put("deck","123");
            postRecommendedActivitiesPayload.put("person",DeckControllerResponseDTO.getDecks()[0].getUserId());

            String payload = String.valueOf(postRecommendedActivitiesPayload);
            //PostRecommendedActivitiesRequestDTO = mapper.readValue(payload, PostRecommendedActivitiesRequestDTO.class);
            PostRecommendedActivitiesResponseDTO = postRecommendedActivities(headerNormal, payload);

            Assert.assertEquals(StatusCode, 404, "Status Code");
            Assert.assertEquals(PostRecommendedActivitiesResponseDTO.getDescription(),"Invalid Deck ID");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 26) @AlmAnnotation(almTestId = "609861")
    public void PostRecommendedActivitiesWithoutrefreshDeckFieldinRequestPayload() throws Exception {
        try {
            CreateSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            GetRecommendedCards();
            // Read json object from file
            JSONObject postRecommendedActivitiesPayload = jsonReader.getJsonObject(PostRecommendedActivitiesPayload);
            postRecommendedActivitiesPayload.put("deck",DeckControllerResponseDTO.getDecks()[0].getId());
            postRecommendedActivitiesPayload.put("person",DeckControllerResponseDTO.getDecks()[0].getUserId());
            postRecommendedActivitiesPayload.put("refreshDeck",false);

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
            Assert.assertNotNull(PostRecommendedActivitiesResponseDTO.getMastery(),"Mastery not be null");
            Assert.assertEquals(PostRecommendedActivitiesResponseDTO.getMessage(),"Number of activities processed 1");
            Assert.assertNull(PostRecommendedActivitiesResponseDTO.getRecommendation(),"Number of activities processed 1");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 27) @AlmAnnotation(almTestId = "609861")
    public void PostRecommendedActivitiesWithNullforActivitiesFieldinRequestPayload() throws Exception {
        try {
            CreateSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            GetRecommendedCards();
            // Read json object from file
            JSONObject postRecommendedActivitiesPayload = jsonReader.getJsonObject(PostRecommendedActivitiesPayload);
            postRecommendedActivitiesPayload.put("deck",DeckControllerResponseDTO.getDecks()[0].getId());
            postRecommendedActivitiesPayload.put("person",DeckControllerResponseDTO.getDecks()[0].getUserId());
            postRecommendedActivitiesPayload.put("refreshDeck",true);

            postRecommendedActivitiesPayload.put("activities",null);

            String payload = String.valueOf(postRecommendedActivitiesPayload);
            //PostRecommendedActivitiesRequestDTO = mapper.readValue(payload, PostRecommendedActivitiesRequestDTO.class);
            PostRecommendedActivitiesResponseDTO = postRecommendedActivities(headerNormal, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(PostRecommendedActivitiesResponseDTO.getMessage(),"error.badRequest");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 28) @AlmAnnotation(almTestId = "609861")
    public void PostRecommendedActivitiesWithNullforActivityIdFieldinRequestPayload() throws Exception {
        try {
            CreateSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            GetRecommendedCards();
            // Read json object from file
            JSONObject postRecommendedActivitiesPayload = jsonReader.getJsonObject(PostRecommendedActivitiesPayload);
            postRecommendedActivitiesPayload.put("deck",DeckControllerResponseDTO.getDecks()[0].getId());
            postRecommendedActivitiesPayload.put("person",DeckControllerResponseDTO.getDecks()[0].getUserId());
            postRecommendedActivitiesPayload.put("refreshDeck",true);

            JSONObject event = new JSONObject();
            event.put("eventType", "ANSWERED");
            event.put("eventTime", Instant.now().toString());
            event.put("score",  1);
            JSONArray events = new JSONArray();
            events.add(event);

            JSONObject activity = new JSONObject();
            activity.put("activityId", null);
            activity.put("cardId", GetRecommendationResponseDTO.getSession().getCards()[0]);
            activity.put("cardType", "flashCard");
            activity.put("startTime", Instant.now().toString());
            activity.put("endTime", Instant.now().toString());
            activity.put("sessionId", GetRecommendationResponseDTO.getSession().getSessionId());
            activity.put("events", events);
            JSONArray activities = new JSONArray();
            activities.add(activity);
            postRecommendedActivitiesPayload.put("activities",null);

            String payload = String.valueOf(postRecommendedActivitiesPayload);
            //PostRecommendedActivitiesRequestDTO = mapper.readValue(payload, PostRecommendedActivitiesRequestDTO.class);
            PostRecommendedActivitiesResponseDTO = postRecommendedActivities(headerNormal, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(PostRecommendedActivitiesResponseDTO.getMessage(),"error.badRequest");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 29) @AlmAnnotation(almTestId = "609861")
    public void PostRecommendedActivitiesWithNullforCardIdFieldinRequestPayload() throws Exception {
        try {
            CreateSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            GetRecommendedCards();
            // Read json object from file
            JSONObject postRecommendedActivitiesPayload = jsonReader.getJsonObject(PostRecommendedActivitiesPayload);
            postRecommendedActivitiesPayload.put("deck",DeckControllerResponseDTO.getDecks()[0].getId());
            postRecommendedActivitiesPayload.put("person",DeckControllerResponseDTO.getDecks()[0].getUserId());
            postRecommendedActivitiesPayload.put("refreshDeck",true);

            JSONObject event = new JSONObject();
            event.put("eventType", "ANSWERED");
            event.put("eventTime", Instant.now().toString());
            event.put("score",  1);
            JSONArray events = new JSONArray();
            events.add(event);

            JSONObject activity = new JSONObject();
            activity.put("activityId", "activityId1");
            activity.put("cardId", null);
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

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(PostRecommendedActivitiesResponseDTO.getMessage(),"error.badRequest");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 30) @AlmAnnotation(almTestId = "609861")
    public void PostRecommendedActivitiesWithNullforCardTypeFieldinRequestPayload() throws Exception {
        try {
            CreateSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            GetRecommendedCards();
            // Read json object from file
            JSONObject postRecommendedActivitiesPayload = jsonReader.getJsonObject(PostRecommendedActivitiesPayload);
            postRecommendedActivitiesPayload.put("deck",DeckControllerResponseDTO.getDecks()[0].getId());
            postRecommendedActivitiesPayload.put("person",DeckControllerResponseDTO.getDecks()[0].getUserId());
            postRecommendedActivitiesPayload.put("refreshDeck",true);

            JSONObject event = new JSONObject();
            event.put("eventType", "ANSWERED");
            event.put("eventTime", Instant.now().toString());
            event.put("score",  1);
            JSONArray events = new JSONArray();
            events.add(event);

            JSONObject activity = new JSONObject();
            activity.put("activityId", "activityId1");
            activity.put("cardId", GetRecommendationResponseDTO.getSession().getCards()[0]);
            activity.put("cardType", null);
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

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(PostRecommendedActivitiesResponseDTO.getMessage(),"error.badRequest");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 31) @AlmAnnotation(almTestId = "609861")
    public void PostRecommendedActivitiesWithNullforStartTimeFieldinRequestPayload() throws Exception {
        try {
            CreateSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            GetRecommendedCards();
            // Read json object from file
            JSONObject postRecommendedActivitiesPayload = jsonReader.getJsonObject(PostRecommendedActivitiesPayload);
            postRecommendedActivitiesPayload.put("deck",DeckControllerResponseDTO.getDecks()[0].getId());
            postRecommendedActivitiesPayload.put("person",DeckControllerResponseDTO.getDecks()[0].getUserId());
            postRecommendedActivitiesPayload.put("refreshDeck",true);

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
            activity.put("startTime", null);
            activity.put("endTime", Instant.now().toString());
            activity.put("sessionId", GetRecommendationResponseDTO.getSession().getSessionId());
            activity.put("events", events);
            JSONArray activities = new JSONArray();
            activities.add(activity);
            postRecommendedActivitiesPayload.put("activities",activities);

            String payload = String.valueOf(postRecommendedActivitiesPayload);
            //PostRecommendedActivitiesRequestDTO = mapper.readValue(payload, PostRecommendedActivitiesRequestDTO.class);
            PostRecommendedActivitiesResponseDTO = postRecommendedActivities(headerNormal, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(PostRecommendedActivitiesResponseDTO.getMessage(),"error.badRequest");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 32) @AlmAnnotation(almTestId = "609861")
    public void PostRecommendedActivitiesWithNullforEndTimeFieldinRequestPayload() throws Exception {
        try {
            CreateSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            GetRecommendedCards();
            // Read json object from file
            JSONObject postRecommendedActivitiesPayload = jsonReader.getJsonObject(PostRecommendedActivitiesPayload);
            postRecommendedActivitiesPayload.put("deck",DeckControllerResponseDTO.getDecks()[0].getId());
            postRecommendedActivitiesPayload.put("person",DeckControllerResponseDTO.getDecks()[0].getUserId());
            postRecommendedActivitiesPayload.put("refreshDeck",true);

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
            activity.put("endTime", null);
            activity.put("sessionId", GetRecommendationResponseDTO.getSession().getSessionId());
            activity.put("events", events);
            JSONArray activities = new JSONArray();
            activities.add(activity);
            postRecommendedActivitiesPayload.put("activities",activities);

            String payload = String.valueOf(postRecommendedActivitiesPayload);
            //PostRecommendedActivitiesRequestDTO = mapper.readValue(payload, PostRecommendedActivitiesRequestDTO.class);
            PostRecommendedActivitiesResponseDTO = postRecommendedActivities(headerNormal, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(PostRecommendedActivitiesResponseDTO.getMessage(),"error.badRequest");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 33) @AlmAnnotation(almTestId = "609861")
    public void PostRecommendedActivitiesWithNullforSessionIdFieldinRequestPayload() throws Exception {
        try {
            CreateSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            GetRecommendedCards();
            // Read json object from file
            JSONObject postRecommendedActivitiesPayload = jsonReader.getJsonObject(PostRecommendedActivitiesPayload);
            postRecommendedActivitiesPayload.put("deck",DeckControllerResponseDTO.getDecks()[0].getId());
            postRecommendedActivitiesPayload.put("person",DeckControllerResponseDTO.getDecks()[0].getUserId());
            postRecommendedActivitiesPayload.put("refreshDeck",true);

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
            activity.put("sessionId", null);
            activity.put("events", events);
            JSONArray activities = new JSONArray();
            activities.add(activity);
            postRecommendedActivitiesPayload.put("activities",activities);

            String payload = String.valueOf(postRecommendedActivitiesPayload);
            //PostRecommendedActivitiesRequestDTO = mapper.readValue(payload, PostRecommendedActivitiesRequestDTO.class);
            PostRecommendedActivitiesResponseDTO = postRecommendedActivities(headerNormal, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(PostRecommendedActivitiesResponseDTO.getMessage(),"error.badRequest");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 34) @AlmAnnotation(almTestId = "609861")
    public void PostRecommendedActivitiesWithNullforEventsFieldinRequestPayload() throws Exception {
        try {
            CreateSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            GetRecommendedCards();
            // Read json object from file
            JSONObject postRecommendedActivitiesPayload = jsonReader.getJsonObject(PostRecommendedActivitiesPayload);
            postRecommendedActivitiesPayload.put("deck",DeckControllerResponseDTO.getDecks()[0].getId());
            postRecommendedActivitiesPayload.put("person",DeckControllerResponseDTO.getDecks()[0].getUserId());
            postRecommendedActivitiesPayload.put("refreshDeck",true);

//            JSONObject event = new JSONObject();
//            event.put("eventType", "ANSWERED");
//            event.put("eventTime", Instant.now().toString());
//            event.put("score",  1);
//            JSONArray events = new JSONArray();
//            events.add(event);

            JSONObject activity = new JSONObject();
            activity.put("activityId", "activityId1");
            activity.put("cardId", GetRecommendationResponseDTO.getSession().getCards()[0]);
            activity.put("cardType", "flashCard");
            activity.put("startTime", Instant.now().toString());
            activity.put("endTime", Instant.now().toString());
            activity.put("sessionId", GetRecommendationResponseDTO.getSession().getSessionId());
            activity.put("events", null);
            JSONArray activities = new JSONArray();
            activities.add(activity);
            postRecommendedActivitiesPayload.put("activities",activities);

            String payload = String.valueOf(postRecommendedActivitiesPayload);
            //PostRecommendedActivitiesRequestDTO = mapper.readValue(payload, PostRecommendedActivitiesRequestDTO.class);
            PostRecommendedActivitiesResponseDTO = postRecommendedActivities(headerNormal, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(PostRecommendedActivitiesResponseDTO.getMessage(),"error.badRequest");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 35) @AlmAnnotation(almTestId = "609861")
    public void PostRecommendedActivitiesWithNullforEventTypeFieldinRequestPayload() throws Exception {
        try {
            CreateSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            GetRecommendedCards();
            // Read json object from file
            JSONObject postRecommendedActivitiesPayload = jsonReader.getJsonObject(PostRecommendedActivitiesPayload);
            postRecommendedActivitiesPayload.put("deck",DeckControllerResponseDTO.getDecks()[0].getId());
            postRecommendedActivitiesPayload.put("person",DeckControllerResponseDTO.getDecks()[0].getUserId());
            postRecommendedActivitiesPayload.put("refreshDeck",true);

            JSONObject event = new JSONObject();
            event.put("eventType", null);
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

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(PostRecommendedActivitiesResponseDTO.getMessage(),"error.badRequest");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 36) @AlmAnnotation(almTestId = "609861")
    public void PostRecommendedActivitiesWithNullforEventTimeFieldinRequestPayload() throws Exception {
        try {
            CreateSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            GetRecommendedCards();
            // Read json object from file
            JSONObject postRecommendedActivitiesPayload = jsonReader.getJsonObject(PostRecommendedActivitiesPayload);
            postRecommendedActivitiesPayload.put("deck",DeckControllerResponseDTO.getDecks()[0].getId());
            postRecommendedActivitiesPayload.put("person",DeckControllerResponseDTO.getDecks()[0].getUserId());
            postRecommendedActivitiesPayload.put("refreshDeck",true);

            JSONObject event = new JSONObject();
            event.put("eventType", "ANSWERED");
            event.put("eventTime", null);
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

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(PostRecommendedActivitiesResponseDTO.getMessage(),"error.badRequest");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 37) @AlmAnnotation(almTestId = "609861")
    public void PostRecommendedActivitiesWithNullforScoreFieldinRequestPayload() throws Exception {
        try {
            CreateSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            GetRecommendedCards();
            // Read json object from file
            JSONObject postRecommendedActivitiesPayload = jsonReader.getJsonObject(PostRecommendedActivitiesPayload);
            postRecommendedActivitiesPayload.put("deck",DeckControllerResponseDTO.getDecks()[0].getId());
            postRecommendedActivitiesPayload.put("person",DeckControllerResponseDTO.getDecks()[0].getUserId());
            postRecommendedActivitiesPayload.put("refreshDeck",true);

            JSONObject event = new JSONObject();
            event.put("eventType", "ANSWERED");
            event.put("eventTime", Instant.now().toString());
            event.put("score",  null);
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
            Assert.assertEquals(PostRecommendedActivitiesResponseDTO.getMastery(),0.0);

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 38) @AlmAnnotation(almTestId = "609861")
    public void PostRecommendedActivitiesWithZeroforScoreFieldinRequestPayload() throws Exception {
        try {
            CreateSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            GetRecommendedCards();
            // Read json object from file
            JSONObject postRecommendedActivitiesPayload = jsonReader.getJsonObject(PostRecommendedActivitiesPayload);
            postRecommendedActivitiesPayload.put("deck",DeckControllerResponseDTO.getDecks()[0].getId());
            postRecommendedActivitiesPayload.put("person",DeckControllerResponseDTO.getDecks()[0].getUserId());
            postRecommendedActivitiesPayload.put("refreshDeck",true);

            JSONObject event = new JSONObject();
            event.put("eventType", "ANSWERED");
            event.put("eventTime", Instant.now().toString());
            event.put("score",  0);
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
            Assert.assertEquals(PostRecommendedActivitiesResponseDTO.getMastery(),0.0);

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 39) @AlmAnnotation(almTestId = "609861")
    public void PostRecommendedActivitiesWithValidateOneisHighestValueforMastery() throws Exception {
        try {
            CreateSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            GetRecommendedCards();
            // Read json object from file
            PostRecommendedActivities();

            while (PostRecommendedActivitiesResponseDTO.getMastery()<1){
                PostRecommendedActivities();
            }
            PostRecommendedActivities();
            Assert.assertEquals(PostRecommendedActivitiesResponseDTO.getMastery(),1);

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 40) @AlmAnnotation(almTestId = "609861")
    public void PostRecommendedActivitiesWithMultipleQuestions() throws Exception {
        try {
            CreateSingleDeckWithFullPayload();
            for(int i=0; i<=5; i++)
                CreateQuestionWithFullPayload();
            GetRecommendedCards();
            // Read json object from file
            JSONObject postRecommendedActivitiesPayload = jsonReader.getJsonObject(PostRecommendedActivitiesPayload);
            postRecommendedActivitiesPayload.put("deck",DeckControllerResponseDTO.getDecks()[0].getId());
            postRecommendedActivitiesPayload.put("person",DeckControllerResponseDTO.getDecks()[0].getUserId());
            postRecommendedActivitiesPayload.put("refreshDeck",true);

            JSONObject event = new JSONObject();
            event.put("eventType", "ANSWERED");
            event.put("eventTime", Instant.now().toString());
            event.put("score",  1);
            JSONArray events = new JSONArray();
            events.add(event);

            JSONArray activities = new JSONArray();
            //Payload for Question1
            for(int i=0; i<=5; i++) {
                JSONObject activity = new JSONObject();
                activity.put("activityId", "activityId1");
                activity.put("cardId", GetRecommendationResponseDTO.getSession().getCards()[i]);
                activity.put("cardType", "flashCard");
                activity.put("startTime", Instant.now().toString());
                activity.put("endTime", Instant.now().toString());
                activity.put("sessionId", GetRecommendationResponseDTO.getSession().getSessionId());
                activity.put("events", events);
                activities.add(activity);
            }
            postRecommendedActivitiesPayload.put("activities", activities);

            String payload = String.valueOf(postRecommendedActivitiesPayload);
            PostRecommendedActivitiesResponseDTO = postRecommendedActivities(headerNormal, payload);
            //PostRecommendedActivitiesRequestDTO = mapper.readValue(payload, PostRecommendedActivitiesRequestDTO.class);
            while (PostRecommendedActivitiesResponseDTO.getMastery()<1){
                PostRecommendedActivitiesResponseDTO = postRecommendedActivities(headerNormal, payload);
            }

            Assert.assertEquals(StatusCode, 201, "Status Code");
            Assert.assertEquals(PostRecommendedActivitiesResponseDTO.getMessage(), "Number of activities processed "+PostRecommendedActivitiesResponseDTO.getRecommendation().getSession().getCards().length, "Status Code");
            Assert.assertEquals(PostRecommendedActivitiesResponseDTO.getMastery(),1.0);

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 41) @AlmAnnotation(almTestId = "609861")
    public void PostRecommendedActivitiesWithInvalidValueforCardIdFieldinRequestPayload() throws Exception {
        try {
            CreateSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            GetRecommendedCards();
            DeleteSingleQuestionWithFullPayload();
            // Read json object from file
            JSONObject postRecommendedActivitiesPayload = jsonReader.getJsonObject(PostRecommendedActivitiesPayload);
            postRecommendedActivitiesPayload.put("deck",DeckControllerResponseDTO.getDecks()[0].getId());
            postRecommendedActivitiesPayload.put("person",DeckControllerResponseDTO.getDecks()[0].getUserId());
            postRecommendedActivitiesPayload.put("refreshDeck",true);

            JSONObject event = new JSONObject();
            event.put("eventType", "ANSWERED");
            event.put("eventTime", Instant.now().toString());
            event.put("score",  1);
            JSONArray events = new JSONArray();
            events.add(event);

            JSONObject activity = new JSONObject();
            activity.put("activityId", "activityId1");
            activity.put("cardId", "123");
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

            Assert.assertEquals(StatusCode, 404, "Status Code");
            Assert.assertEquals(PostRecommendedActivitiesResponseDTO.getMessage(),"error.notFound");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 42) @AlmAnnotation(almTestId = "609861")
    public void PostRecommendedActivitiesWithInvalidTenantId() throws Exception {
        try {
            headerNormal.put("X-TenantId","a");
            JSONObject postRecommendedActivitiesPayload = jsonReader.getJsonObject(PostRecommendedActivitiesPayload);

            String payload = String.valueOf(postRecommendedActivitiesPayload);
            //PostRecommendedActivitiesRequestDTO = mapper.readValue(payload, PostRecommendedActivitiesRequestDTO.class);
            PostRecommendedActivitiesResponseDTO = postRecommendedActivities(headerNormal, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(PostRecommendedActivitiesResponseDTO.getDescription(), "Field 'tenantId' does not have a valid value", "Status Code");
        } catch (Exception e) {
            throw e;
        }
        }

        @Test(priority = 43) @AlmAnnotation(almTestId = "609861")
        public void PostRecommendedActivitiesWithInvalidTenantKey() throws Exception {
            try {
                getData();
                headerNormal.put("X-TenantKey","a");
                // Read json object from file
                JSONObject postRecommendedActivitiesPayload = jsonReader.getJsonObject(PostRecommendedActivitiesPayload);

                String payload = String.valueOf(postRecommendedActivitiesPayload);
                PostRecommendedActivitiesResponseDTO = postRecommendedActivities(headerNormal, payload);

                Assert.assertEquals(StatusCode, 400, "Status Code");
                Assert.assertEquals(PostRecommendedActivitiesResponseDTO.getDescription(), "Field 'tenantKey' does not have a valid value", "Status Code");
            } catch (Exception e) {
                throw e;
            }
        }

        @Test(priority = 44) @AlmAnnotation(almTestId = "609861")
        public void PostRecommendedActivitiesWithUnauthorizedError() throws Exception {
            try {
                headerNormal.put("X-Authorization","a");

                JSONObject postRecommendedActivitiesPayload = jsonReader.getJsonObject(PostRecommendedActivitiesPayload);

                String payload = String.valueOf(postRecommendedActivitiesPayload);
                PostRecommendedActivitiesResponseDTO = postRecommendedActivities(headerNormal, payload);

                Assert.assertEquals(StatusCode, 401, "Status Code");
                Assert.assertEquals(PostRecommendedActivitiesResponseDTO.getError(), "Unauthorized", "Status Code");
            } catch (Exception e) {
                throw e;
            }
        }

    @Test(priority = 45) @AlmAnnotation(almTestId = "609861")
    public void GetActivitiesOfDecksWithNullValueforDeckIdinResponse() throws Exception {
        try {
            getData();
            // Read json object from file
            JSONObject getActivitiesOfDecksPayload = jsonReader.getJsonObject(GetActivitiesOfDecksPayload);

            getActivitiesOfDecksPayload.put("deckIds",null);
            //getActivitiesOfDecksPayload.put("studentId",DeckControllerResponseDTO.getDecks()[0].getUserId());

            String payload = String.valueOf(getActivitiesOfDecksPayload);
            //PostRecommendedActivitiesRequestDTO = mapper.readValue(payload, PostRecommendedActivitiesRequestDTO.class);
            GetActivitiesOfDecksResponseDTO = getActivitiesOfDecks(headerNormal, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getFieldErrors()[0].getField(),"deckIds", "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getFieldErrors()[0].getMessage(),"must not be null", "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 46) @AlmAnnotation(almTestId = "609861")
    public void GetActivitiesOfDecksWithNullValueforIncludeRecentFieldinResponse() throws Exception {
        try {
            CreateSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            GetRecommendedCards();
            PostRecommendedActivities();
            // Read json object from file
            JSONObject getActivitiesOfDecksPayload = jsonReader.getJsonObject(GetActivitiesOfDecksPayload);

            JSONArray deckIds = new JSONArray();
            deckIds.add(DeckControllerResponseDTO.getDecks()[0].getId());
            getActivitiesOfDecksPayload.put("deckIds",deckIds);
            getActivitiesOfDecksPayload.put("includeRecent",null);
            getActivitiesOfDecksPayload.put("studentId",DeckControllerResponseDTO.getDecks()[0].getUserId());

            String payload = String.valueOf(getActivitiesOfDecksPayload);
            //PostRecommendedActivitiesRequestDTO = mapper.readValue(payload, PostRecommendedActivitiesRequestDTO.class);
            GetActivitiesOfDecksResponseDTO = getActivitiesOfDecks(headerNormal, payload);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getDeckId(), DeckControllerResponseDTO.getDecks()[0].getId(), "Status Code");
            Assert.assertNull(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getRecentActivity());
            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 47) @AlmAnnotation(almTestId = "609861")
    public void GetActivitiesOfDecksWithFalseValueforIncludeRecentFieldinResponse() throws Exception {
        try {
            CreateSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            GetRecommendedCards();
            PostRecommendedActivities();
            // Read json object from file
            JSONObject getActivitiesOfDecksPayload = jsonReader.getJsonObject(GetActivitiesOfDecksPayload);

            JSONArray deckIds = new JSONArray();
            deckIds.add(DeckControllerResponseDTO.getDecks()[0].getId());
            getActivitiesOfDecksPayload.put("deckIds",deckIds);
            getActivitiesOfDecksPayload.put("includeRecent",false);
            getActivitiesOfDecksPayload.put("studentId",DeckControllerResponseDTO.getDecks()[0].getUserId());

            String payload = String.valueOf(getActivitiesOfDecksPayload);
            //PostRecommendedActivitiesRequestDTO = mapper.readValue(payload, PostRecommendedActivitiesRequestDTO.class);
            GetActivitiesOfDecksResponseDTO = getActivitiesOfDecks(headerNormal, payload);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getDeckId(), DeckControllerResponseDTO.getDecks()[0].getId(), "Status Code");
            Assert.assertNull(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getRecentActivity());
            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 48) @AlmAnnotation(almTestId = "609861")
    public void GetActivitiesOfDecksWithNullValueforstudentIdFieldinResponse() throws Exception {
        try {
            // Read json object from file
            JSONObject getActivitiesOfDecksPayload = jsonReader.getJsonObject(GetActivitiesOfDecksPayload);

            JSONArray deckIds = new JSONArray();
            deckIds.add("DeckId");
            getActivitiesOfDecksPayload.put("deckIds",deckIds);
            getActivitiesOfDecksPayload.put("studentId",null);

            String payload = String.valueOf(getActivitiesOfDecksPayload);
            //PostRecommendedActivitiesRequestDTO = mapper.readValue(payload, PostRecommendedActivitiesRequestDTO.class);
            GetActivitiesOfDecksResponseDTO = getActivitiesOfDecks(headerNormal, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getFieldErrors()[0].getField(),"studentId", "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getFieldErrors()[0].getMessage(),"must not be blank", "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 49) @AlmAnnotation(almTestId = "609861")
    public void GetActivitiesOfDecksWithInvalidValueforstudentIdFieldinResponse() throws Exception {
        try {
            // Read json object from file
            JSONObject getActivitiesOfDecksPayload = jsonReader.getJsonObject(GetActivitiesOfDecksPayload);

            JSONArray deckIds = new JSONArray();
            deckIds.add("DeckId");
            getActivitiesOfDecksPayload.put("deckIds",deckIds);
            getActivitiesOfDecksPayload.put("studentId","123");

            String payload = String.valueOf(getActivitiesOfDecksPayload);
            //PostRecommendedActivitiesRequestDTO = mapper.readValue(payload, PostRecommendedActivitiesRequestDTO.class);
            GetActivitiesOfDecksResponseDTO = getActivitiesOfDecks(headerNormal, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDescription(),"User IDs do not match", "Status Code");
            //DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 50) @AlmAnnotation(almTestId = "609861")
    public void GetActivitiesOfDecksWithValidateProgressofCard() throws Exception {
        try {
            CreateSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();

            GetRecommendedCards();
            GetActivitiesOfDecks();
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getDeckId(), DeckControllerResponseDTO.getDecks()[0].getId(), "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getLearnedCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getSeenCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getUnseenCards(), 1, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getFavoriteCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getCorrectCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getTotalCards(), 1, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getRecentActivity().getCorrectCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getRecentActivity().getTotalCards(), 0, "Status Code");

            PostRecommendedActivities();
            GetActivitiesOfDecks();
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getDeckId(), DeckControllerResponseDTO.getDecks()[0].getId(), "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getLearnedCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getSeenCards(), 1, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getUnseenCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getFavoriteCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getCorrectCards(), 1, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getTotalCards(), 1, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getRecentActivity().getCorrectCards(), 1, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getRecentActivity().getTotalCards(), 1, "Status Code");

            PostRecommendedActivities();
            GetActivitiesOfDecks();
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getDeckId(), DeckControllerResponseDTO.getDecks()[0].getId(), "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getLearnedCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getSeenCards(), 1, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getUnseenCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getFavoriteCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getCorrectCards(), 1, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getTotalCards(), 1, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getRecentActivity().getCorrectCards(), 1, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getRecentActivity().getTotalCards(), 1, "Status Code");

            PostRecommendedActivities();
            GetActivitiesOfDecks();
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getDeckId(), DeckControllerResponseDTO.getDecks()[0].getId(), "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getLearnedCards(), 1, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getSeenCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getUnseenCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getFavoriteCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getCorrectCards(), 1, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getTotalCards(), 1, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getRecentActivity().getCorrectCards(), 1, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getRecentActivity().getTotalCards(), 1, "Status Code");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }


    //Favourite a card and verify the number of favourite cards
    @Test(priority = 51) @AlmAnnotation(almTestId = "609861")
    public void GetActivitiesOfDecksWithValidateFavoriteCardCount() throws Exception {
        try {
            CreateSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            FavouriteQuestion();
            GetRecommendedCards();
            GetActivitiesOfDecks();
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getDeckId(), DeckControllerResponseDTO.getDecks()[0].getId(), "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getLearnedCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getSeenCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getUnseenCards(), 1, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getFavoriteCards(), 1, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getCorrectCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getTotalCards(), 1, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getRecentActivity().getCorrectCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getRecentActivity().getTotalCards(), 1, "Status Code");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

//Un favourite a card and verify the number of favourite cards
    @Test(priority = 52) @AlmAnnotation(almTestId = "609861")
    public void GetActivitiesOfDecksWithValidateFavoriteCardCountWhenUnfavoriteCard() throws Exception {
        try {
            CreateSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            FavouriteQuestion();
            GetRecommendedCards();
            GetActivitiesOfDecks();
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getDeckId(), DeckControllerResponseDTO.getDecks()[0].getId(), "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getLearnedCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getSeenCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getUnseenCards(), 1, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getFavoriteCards(), 1, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getCorrectCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getTotalCards(), 1, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getRecentActivity().getCorrectCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getRecentActivity().getTotalCards(), 1, "Status Code");

            //String QuestionId = QuestionControllerResponseDTO.getQuestions()[0].getId();
            JSONObject FavouritePayload = jsonReader.getJsonObject(FavouriteQuestionPayload);

            JSONObject favourites = new JSONObject();
            favourites.put("favourite",false);
            favourites.put("questionId",FavouriteControllerResponseDTO.getFavourites()[0].getQuestionId());
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

            GetActivitiesOfDecks();
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getDeckId(), DeckControllerResponseDTO.getDecks()[0].getId(), "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getLearnedCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getSeenCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getUnseenCards(), 1, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getFavoriteCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getCorrectCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getTotalCards(), 1, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getRecentActivity().getCorrectCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getRecentActivity().getTotalCards(), 1, "Status Code");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    //Delete a card and verify whether the total card count is updated
    @Test(priority = 53) @AlmAnnotation(almTestId = "609861")
    public void GetActivitiesOfDecksWithValidateTotalCardCountWhenDeleteTheCard() throws Exception {
        try {
            CreateSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            GetActivitiesOfDecks();
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getDeckId(), DeckControllerResponseDTO.getDecks()[0].getId(), "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getLearnedCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getSeenCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getUnseenCards(), 1, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getFavoriteCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getCorrectCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getTotalCards(), 1, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getRecentActivity().getCorrectCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getRecentActivity().getTotalCards(), 0, "Status Code");

            DeleteSingleQuestionWithFullPayload();
            GetActivitiesOfDecks();
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getDeckId(), DeckControllerResponseDTO.getDecks()[0].getId(), "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getLearnedCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getSeenCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getUnseenCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getFavoriteCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getCorrectCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getTotalCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getRecentActivity().getCorrectCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getRecentActivity().getTotalCards(), 0, "Status Code");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    //Add a card and verify whether the total card count is updated
    @Test(priority = 54) @AlmAnnotation(almTestId = "609861")
    public void GetActivitiesOfDecksWithValidateTotalCardCountWhenAddTheCard() throws Exception {
        try {
            CreateSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            GetActivitiesOfDecks();
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getDeckId(), DeckControllerResponseDTO.getDecks()[0].getId(), "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getLearnedCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getSeenCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getUnseenCards(), 1, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getFavoriteCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getCorrectCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getTotalCards(), 1, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getRecentActivity().getCorrectCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getRecentActivity().getTotalCards(), 0, "Status Code");

            CreateQuestionWithFullPayload();
            GetActivitiesOfDecks();
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getDeckId(), DeckControllerResponseDTO.getDecks()[0].getId(), "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getLearnedCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getSeenCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getUnseenCards(), 2, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getFavoriteCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getCorrectCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getTotalCards(), 2, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getRecentActivity().getCorrectCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getRecentActivity().getTotalCards(), 0, "Status Code");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    //Verify passing a deck with no cards
    @Test(priority = 55) @AlmAnnotation(almTestId = "609861")
    public void GetActivitiesOfDecksWithValidateTotalCardCountWithoutAnyCard() throws Exception {
        try {
            CreateSingleDeckWithFullPayload();
            GetActivitiesOfDecks();
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getDeckId(), DeckControllerResponseDTO.getDecks()[0].getId(), "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getLearnedCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getSeenCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getUnseenCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getFavoriteCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getCorrectCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getTotalCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getRecentActivity().getCorrectCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getRecentActivity().getTotalCards(), 0, "Status Code");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    //Verify passing a deleted deck
    @Test(priority = 56) @AlmAnnotation(almTestId = "609861")
    public void GetActivitiesOfDecksWithValidateTotalCardCountWithoutAnyCar() throws Exception {
        try {
            CreateSingleDeckWithFullPayload();
            String DeckId= DeckControllerResponseDTO.getDecks()[0].getId();
            String UserId= DeckControllerResponseDTO.getDecks()[0].getUserId();
            DeleteDeckWithFullPayload();
            // Read json object from file
            JSONObject getActivitiesOfDecksPayload = jsonReader.getJsonObject(GetActivitiesOfDecksPayload);

            JSONArray deckIds = new JSONArray();
            deckIds.add(DeckId);
            getActivitiesOfDecksPayload.put("deckIds",deckIds);
            getActivitiesOfDecksPayload.put("studentId",UserId);

            String payload = String.valueOf(getActivitiesOfDecksPayload);
            //PostRecommendedActivitiesRequestDTO = mapper.readValue(payload, PostRecommendedActivitiesRequestDTO.class);
            GetActivitiesOfDecksResponseDTO = getActivitiesOfDecks(headerNormal, payload);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getDeckId(), DeckId, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getLearnedCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getSeenCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getUnseenCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getFavoriteCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getCorrectCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getTotalCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getRecentActivity().getCorrectCards(), 0, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getRecentActivity().getTotalCards(), 0, "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 57) @AlmAnnotation(almTestId = "609861")
    public void GetActivitiesOfDecksWithInvalidTenantId() throws Exception {
        try {
            headerNormal.put("X-TenantId","a");
            // Read json object from file
            JSONObject getActivitiesOfDecksPayload = jsonReader.getJsonObject(GetActivitiesOfDecksPayload);

            String payload = String.valueOf(getActivitiesOfDecksPayload);
            //PostRecommendedActivitiesRequestDTO = mapper.readValue(payload, PostRecommendedActivitiesRequestDTO.class);
            GetActivitiesOfDecksResponseDTO = getActivitiesOfDecks(headerNormal, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDescription(), "Field 'tenantId' does not have a valid value", "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 58) @AlmAnnotation(almTestId = "609861")
    public void GetActivitiesOfDecksWithInvalidTenantKey() throws Exception {
        try {
            getData();
            headerNormal.put("X-TenantKey","a");
            // Read json object from file
            JSONObject getActivitiesOfDecksPayload = jsonReader.getJsonObject(GetActivitiesOfDecksPayload);

            String payload = String.valueOf(getActivitiesOfDecksPayload);
            //PostRecommendedActivitiesRequestDTO = mapper.readValue(payload, PostRecommendedActivitiesRequestDTO.class);
            GetActivitiesOfDecksResponseDTO = getActivitiesOfDecks(headerNormal, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDescription(), "Field 'tenantKey' does not have a valid value", "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 59) @AlmAnnotation(almTestId = "609861")
    public void GetActivitiesOfDecksWithUnauthorizedError() throws Exception {
        try {
            headerNormal.put("X-Authorization","a");
            // Read json object from file
            JSONObject getActivitiesOfDecksPayload = jsonReader.getJsonObject(GetActivitiesOfDecksPayload);

            String payload = String.valueOf(getActivitiesOfDecksPayload);
            //PostRecommendedActivitiesRequestDTO = mapper.readValue(payload, PostRecommendedActivitiesRequestDTO.class);
            GetActivitiesOfDecksResponseDTO = getActivitiesOfDecks(headerNormal, payload);

            Assert.assertEquals(StatusCode, 401, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getError(), "Unauthorized", "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }


//    @Test(priority =75) @AlmAnnotation(almTestId = "611101")
//    public void DeleteDeckWithInvalidTenant() throws Exception {
//        try {
//            PiTokenGenarator1.CreateTenantWithFullPayload();
//            PiTokenGenarator1.GetTenantWithFullPayload();
//            PiTokenGenarator1.UpdateTenantWithFullPayload();
//            PiTokenGenarator1.DeleteTenantWithFullPayload();
//        } catch (Exception e) {
//            throw e;
//        }
//    }




//    //@Test (priority = 5)
//    public void publishMediaAssignments() throws Exception{
//        JSONObject publishMSAssignmentPayload = jsonReader.getJsonObject(publishMediaAssignmentPayload);
//        System.out.println("Doc: " + DataStore.getInstance().getMediaIDDoc());
//        System.out.println("Video: " + DataStore.getInstance().getMediaIDVideo());
//
//        JSONArray document = new JSONArray();
//        JSONArray media = new JSONArray();
//        publishMSAssignmentPayload.put("courseId",courseID);
//        publishMSAssignmentPayload.put("sectionId",courseID);
//        document.add(DataStore.getInstance().getMediaIDDoc());
//        document.add(DataStore.getInstance().getMediaIDImage());
//        media.add(DataStore.getInstance().getMediaIDVideo());
//
//        publishMSAssignmentPayload.put("assignmentId", DataStore.getInstance().getMultimediaAssignmentDoc() );
//        publishMSAssignmentPayload.put("title", DataStore.getInstance().getMultimediaAssignmentNameDoc() );
//        publishMSAssignmentPayload.put("document", document);
//        publishMSAssignmentPayload.put("media", media);
//        System.out.println("publishMSAssignmentPayload: " + publishMSAssignmentPayload);
//        Response response = RESTServiceBase.putCallWithHeaderAndBodyParam(header,String.valueOf(publishMSAssignmentPayload),publishMediaAssignmentUrl + "mediaAssignments/" + DataStore.getInstance().getMultimediaAssignmentDoc());
//        LoggerUtil.log(response.asString());
//        Assert.assertEquals(response.statusCode(), 200);
//        System.out.println(response);
//        System.out.println("Response Code is: " + response.statusCode());
//    }
}
