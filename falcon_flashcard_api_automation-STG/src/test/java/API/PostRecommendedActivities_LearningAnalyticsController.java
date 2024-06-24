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

public class PostRecommendedActivities_LearningAnalyticsController extends ServiceController {

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

    private static final Logger LOGGER = Logger.getLogger(String.valueOf(PostRecommendedActivities_LearningAnalyticsController.class));
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

    @Test(priority = 10) @AlmAnnotation(almTestId = "609861")
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

    @Test(priority = 11) @AlmAnnotation(almTestId = "609861")
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

    @Test(priority = 12) @AlmAnnotation(almTestId = "609861")
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

    @Test(priority = 13) @AlmAnnotation(almTestId = "609861")
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

    @Test(priority = 14) @AlmAnnotation(almTestId = "609861")
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
    @Test(priority = 15) @AlmAnnotation(almTestId = "609861")
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
    @Test(priority = 16) @AlmAnnotation(almTestId = "609861")
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
    @Test(priority = 17) @AlmAnnotation(almTestId = "609861")
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
    @Test(priority = 18) @AlmAnnotation(almTestId = "609861")
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
    @Test(priority = 19) @AlmAnnotation(almTestId = "609861")
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
    @Test(priority = 20) @AlmAnnotation(almTestId = "609861")
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

    @Test(priority = 21) @AlmAnnotation(almTestId = "609861")
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

    @Test(priority = 22) @AlmAnnotation(almTestId = "609861")
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

    @Test(priority = 23) @AlmAnnotation(almTestId = "609861")
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
}
