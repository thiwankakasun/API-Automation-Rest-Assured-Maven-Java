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

public class CreateDecks_DeckController extends ServiceController {

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

    private static final Logger LOGGER = Logger.getLogger(String.valueOf(CreateDecks_DeckController.class));

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

    //Verify user being able to create multiple deck
    @Test(priority = 8) @AlmAnnotation(almTestId = "584139")
    public void createMultipleDeckWithFullPayload() throws Exception {
        JSONObject createDeckPayload = jsonReader.getJsonObject(DeckCreatePayload);

        JSONObject tenantKeyField = new JSONObject();
        tenantKeyField.put("productId","30b18904-dcc1-4f0f-bf0c-5cc91fa98e8b");
        tenantKeyField.put("bookId","aaa");
        tenantKeyField.put("chapterId","123");
        tenantKeyField.put("sectionId","123");
        tenantKeyField.put("title","aaa");

        JSONArray tenantKey = new JSONArray();
        tenantKey.add(tenantKeyField);
        tenantKey.add(tenantKeyField);
        createDeckPayload.put("decks",tenantKey);
        System.out.println(createDeckPayload);
        String payload = String.valueOf(createDeckPayload);
        DeckControllerRequestDTO = mapper.readValue(payload, DeckControllerRequestDTO.class);
        DeckControllerResponseDTO = createDeck(headerNormal,payload);

        Assert.assertEquals(StatusCode, 201, "Status Code");
        Assert.assertNotNull(DeckControllerResponseDTO.getDecks()[0].getId(),"first deck created");
        Assert.assertNotNull(DeckControllerResponseDTO.getDecks()[1].getId(),"second deck created");

        String DeckId = DeckControllerResponseDTO.getDecks()[0].getId();
        DeckControllerResponseDTO = deleteDeck(headerNormal, DeckControllerResponseDTO.getDecks()[1].getId());
        Assert.assertEquals(StatusCode, 204, "Status Code");

        DeckControllerResponseDTO = deleteDeck(headerNormal, DeckId);
        Assert.assertEquals(StatusCode, 204, "Status Code");
    }

    //Verify user being able to create multiple deck with different title
    @Test(priority = 9) @AlmAnnotation(almTestId = "584141")
    public void createMultipleDeckWithDifferentTitles() throws Exception {
        JSONObject createDeckPayload = jsonReader.getJsonObject(DeckCreatePayload);

        JSONObject tenantKeyField = new JSONObject();
        tenantKeyField.put("productId","30b18904-dcc1-4f0f-bf0c-5cc91fa98e8b");
        tenantKeyField.put("bookId","aaa");
        tenantKeyField.put("chapterId","123");
        tenantKeyField.put("sectionId","123");
        tenantKeyField.put("title","aaa");

        JSONObject tenantKeyField1 = new JSONObject();
        tenantKeyField1.put("productId","30b18904-dcc1-4f0f-bf0c-5cc91fa98e8b");
        tenantKeyField1.put("bookId","aaa");
        tenantKeyField1.put("chapterId","123");
        tenantKeyField1.put("sectionId","123");
        tenantKeyField1.put("title","aaa");

        JSONArray tenantKey = new JSONArray();
        tenantKey.add(tenantKeyField);
        tenantKey.add(tenantKeyField1);
        createDeckPayload.put("decks",tenantKey);
        System.out.println(createDeckPayload);
        String payload = String.valueOf(createDeckPayload);
        DeckControllerRequestDTO = mapper.readValue(payload, DeckControllerRequestDTO.class);
        DeckControllerResponseDTO = createDeck(headerNormal,payload);

        Assert.assertEquals(StatusCode, 201, "Status Code");
        Assert.assertNotNull(DeckControllerResponseDTO.getDecks()[0].getId(),"first deck created");
        Assert.assertNotNull(DeckControllerResponseDTO.getDecks()[1].getId(),"second deck created");

        String DeckId = DeckControllerResponseDTO.getDecks()[0].getId();
        DeckControllerResponseDTO = deleteDeck(headerNormal, DeckControllerResponseDTO.getDecks()[1].getId());
        Assert.assertEquals(StatusCode, 204, "Status Code");

        DeckControllerResponseDTO = deleteDeck(headerNormal, DeckId);
        Assert.assertEquals(StatusCode, 204, "Status Code");
    }

    //Verify user not able to create a deck with empty title
    @Test(priority = 10) @AlmAnnotation(almTestId = "584142")
    public void createDeckWithEmptyTitle() throws Exception {
        JSONObject createDeckPayload = jsonReader.getJsonObject(DeckCreatePayload);

        JSONObject tenantKeyField = new JSONObject();
        tenantKeyField.put("productId","30b18904-dcc1-4f0f-bf0c-5cc91fa98e8b");
        tenantKeyField.put("bookId","aaa");
        tenantKeyField.put("chapterId","123");
        tenantKeyField.put("sectionId","123");
        tenantKeyField.put("title",null);

        JSONArray tenantKey = new JSONArray();
        tenantKey.add(tenantKeyField);
        createDeckPayload.put("decks",tenantKey);
        String payload = String.valueOf(createDeckPayload);
        DeckControllerResponseDTO = createDeck(headerNormal,payload);

        Assert.assertEquals(StatusCode, 400, "Status Code");
        Assert.assertEquals(DeckControllerResponseDTO.getFieldErrors()[0].getMessage(),"must not be blank");
    }

    //Verify the maximum characters can be given to the title is 1000 including the space
    @Test(priority =11) @AlmAnnotation(almTestId = "584143")
    public void createDeckWith1000CharForTitle() throws Exception {
        try {
            JSONObject createDeckPayload = jsonReader.getJsonObject(DeckCreatePayload);

            JSONObject tenantKeyField = new JSONObject();
            tenantKeyField.put("productId", "30b18904-dcc1-4f0f-bf0c-5cc91fa98e8b");
            tenantKeyField.put("bookId", "aaa");
            tenantKeyField.put("chapterId", "123");
            tenantKeyField.put("sectionId", "123");
            tenantKeyField.put("title", "zDyaQra04eUjY2lHx02aM6Z9NkkEHHMkuhSDXHCnwFpjwj0stJN0NEjfJCEubk5cK9hcvopM" +
                    "J80MzkGuTGhfnvEZ7RKSKreWXoJdhFsaeWLxPIYQxeA56tS0cl1tRl78LI6g0M2PeeSLkyf4oCxEj7mjglLU7xiEunR3" +
                    "MfZhMmqgxYoZtD1vxPWqvSJmqLlE636wJSdU5npLqFx74oFXUgXV5iOC0vYfm2nBxylUOWF7BmtbCjPBY9RwSbDLNt" +
                    "RprH7ckA4kfeHklhQ1b615KIWQKOE3UBwEnBNTgxNXUmEqfKtca1c1cY4BnSchTdY8t1d1FWfQmg9leyhiLH8Vp4fXrW" +
                    "HRweFhoaek6KdBLu4Ri3ZxkesSAFmwhjiydlafrzFfDPcCIVcdnJRmsWECPovO7PaVzFaIx9oFa3PrYkompOzYIKxi1WZ" +
                    "wIwBTdhYnfagV2NaBWLgkCxs85bXK9vEaKLdbZcIlNULDjUuw8DxopjzLtKTYl5YayAIAFiCw5iE1R4syHV5bnhH3U8V1f" +
                    "OCb7mHTJl3VcgjzZDHlOjQvUaGrvVwA9Uss7ExXti1eiPF5p0uinsQfIGFZgVduDzUqiKEITemZNxPYz8DH3F6RW4E4o" +
                    "sCCNXQ9jiszKrhNHi8p58mg88RCzivD7watGrDpVr0g3AsZgbywVYp7uEotSmxOLntLIkTDv7gvUmVFoetmRuphSIXw8e" +
                    "TjXxy9kxJzHavn8KPpEwLUAj9JW9AM1Vn7SiMCHaKkhTx5QCM8cytTi0zFmlYHN4gZkqu5KWKUBHMtxMaFAceNME03e" +
                    "RHzK7mwtdckcLzSMpH8i8Q1zSE1V1kHOonwdX40ozEDW9sunuiPbUwt7rgl2Wr8fAEVOmVOe9WdTdXSPIJ77jY9MMtL" +
                    "awhiYHbv4oqMPBAJpv0XA0mlK2WhVf96oGSOQ2B6a4PI76RBD26EAe5nVxZskXffUT5EZ1BMe9vbtMdaRGBmw82ulzvM" +
                    "RtkNsJmyP");

            JSONArray tenantKey = new JSONArray();
            tenantKey.add(tenantKeyField);
            createDeckPayload.put("decks", tenantKey);
            String payload = String.valueOf(createDeckPayload);
            DeckControllerResponseDTO = createDeck(headerNormal, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(DeckControllerResponseDTO.getFieldErrors()[0].getMessage(), "size must be between 1 and 1000");
        }
    catch (Exception e){
            throw e;
    }}

    //Verify whether the minimum characters that can be given to the title is one
    @Test(priority = 12) @AlmAnnotation(almTestId = "584144")
    public void createDeckWithMinimumCharis1ForTitle() throws Exception {
        try {
            JSONObject createDeckPayload = jsonReader.getJsonObject(DeckCreatePayload);

            JSONObject tenantKeyField = new JSONObject();
            tenantKeyField.put("productId", "30b18904-dcc1-4f0f-bf0c-5cc91fa98e8b");
            tenantKeyField.put("bookId", "aaa");
            tenantKeyField.put("chapterId", "123");
            tenantKeyField.put("sectionId", "123");
            tenantKeyField.put("title", "a");

            JSONArray tenantKey = new JSONArray();
            tenantKey.add(tenantKeyField);
            createDeckPayload.put("decks", tenantKey);
            String payload = String.valueOf(createDeckPayload);
            DeckControllerResponseDTO = createDeck(headerNormal, payload);

            Assert.assertEquals(StatusCode, 201, "Status Code");

            DeleteDeckWithFullPayload();
        }
        catch (Exception e){
            throw e;
        }}

    //Validate the tenant id field
    @Test(priority = 13) @AlmAnnotation(almTestId = "584145")
    public void createDeckWithInvalidTenantId() throws Exception {
        try {
            JSONObject createDeckPayload = jsonReader.getJsonObject(DeckCreatePayload);
            System.out.println(createDeckPayload);

            String payload = String.valueOf(createDeckPayload);
            DeckControllerRequestDTO = mapper.readValue(payload, DeckControllerRequestDTO.class);
            headerNormal.put("X-TenantId", "a");
            DeckControllerResponseDTO = createDeck(headerNormal,payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(DeckControllerResponseDTO.getDescription(),"Field 'tenantId' does not have a valid value");

        }
        catch (Exception e){
            throw e;
        }}

    @Test(priority = 14) @AlmAnnotation(almTestId = "xxxx")
    public void createDeckWithoutBookIdProductId() throws Exception {
        try {
            getData();
            JSONObject createDeckPayload = jsonReader.getJsonObject(DeckCreatePayload);

            JSONObject tenantKeyField = new JSONObject();
            tenantKeyField.put("productId", null);
            tenantKeyField.put("bookId", null);
            tenantKeyField.put("chapterId", null);
            tenantKeyField.put("sectionId", null);
            tenantKeyField.put("title", "Falcon API Automation");

            JSONArray tenantKey = new JSONArray();
            tenantKey.add(tenantKeyField);
            createDeckPayload.put("decks", tenantKey);
            String payload = String.valueOf(createDeckPayload);
//            if (Env.equals("QA")) {
//                headerNormal.put("X-TenantId", "2b4af3fc-97b4-41a3-af7e-bc9354d35fed");
//                headerNormal.put("X-TenantKey", "152be39d-8699-4ae9-9bae-91a50c28b6d0");
//            }
//
//            if (Env.equals("STG")) {
//                headerNormal.put("X-TenantId", "2b4af3fc-97b4-41a3-af7e-bc9354d35fed");
//                headerNormal.put("X-TenantKey", "152be39d-8699-4ae9-9bae-91a50c28b6d0");
//            }
            DeckControllerResponseDTO = createDeck(headerNormal, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(DeckControllerResponseDTO.getFieldErrors()[0].getMessage(),"'bookId', 'productId' or 'bookOrProductId' must be provided");

        }
        catch (Exception e){
            throw e;
        }}

    @Test(priority = 15) @AlmAnnotation(almTestId = "xxxx")
    public void createDeckWithOnlyBookId() throws Exception {
        try {
            JSONObject createDeckPayload = jsonReader.getJsonObject(DeckCreatePayload);

            JSONObject tenantKeyField = new JSONObject();
            tenantKeyField.put("bookId", "30b18904-dcc1-4f0f-bf0c-5cc91fa98e8b");
            tenantKeyField.put("title", "Falcon API Automation");

            JSONArray tenantKey = new JSONArray();
            tenantKey.add(tenantKeyField);
            createDeckPayload.put("decks", tenantKey);
            String payload = String.valueOf(createDeckPayload);
            DeckControllerResponseDTO = createDeck(headerNormal, payload);

            Assert.assertEquals(StatusCode, 201, "Status Code");

            DeleteDeckWithFullPayload();
        }
        catch (Exception e){
            throw e;
        }}

    @Test(priority = 16) @AlmAnnotation(almTestId = "xxxx")
    public void createDeckWithOnlyProductId() throws Exception {
        try {
            JSONObject createDeckPayload = jsonReader.getJsonObject(DeckCreatePayload);

            JSONObject tenantKeyField = new JSONObject();
            tenantKeyField.put("productId", "30b18904-dcc1-4f0f-bf0c-5cc91fa98e8b");
            tenantKeyField.put("title", "Falcon API Automation");

            JSONArray tenantKey = new JSONArray();
            tenantKey.add(tenantKeyField);
            createDeckPayload.put("decks", tenantKey);
            String payload = String.valueOf(createDeckPayload);
            DeckControllerResponseDTO = createDeck(headerNormal, payload);

            Assert.assertEquals(StatusCode, 201, "Status Code");

            DeleteDeckWithFullPayload();
        }
        catch (Exception e){
            throw e;
        }}

    @Test(priority = 17) @AlmAnnotation(almTestId = "xxxx")
    public void createDeckWithOnlybookOrProductId() throws Exception {
        try {
            JSONObject createDeckPayload = jsonReader.getJsonObject(DeckCreatePayload);

            JSONObject DeckField = new JSONObject();
            DeckField.put("bookOrProductId", "30b18904-dcc1-4f0f-bf0c-5cc91fa98e8b");
            DeckField.put("title", "Falcon API Automation");

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            createDeckPayload.put("decks", decks);
            String payload = String.valueOf(createDeckPayload);
            DeckControllerRequestDTO = mapper.readValue(payload, DeckControllerRequestDTO.class);
            DeckControllerResponseDTO = createDeck(headerNormal, payload);

            Assert.assertEquals(StatusCode, 201, "Status Code");
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getProductId(),DeckControllerRequestDTO.getDecks()[0].getBookOrProductId());
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getDeckType(),"USER");
            DeleteDeckWithFullPayload();
        }
        catch (Exception e){
            throw e;
        }}

    @Test(priority = 18) @AlmAnnotation(almTestId = "xxxx")
    public void createDeckWithOnlyBookIdAndProductId() throws Exception {
        try {
            JSONObject createDeckPayload = jsonReader.getJsonObject(DeckCreatePayload);

            JSONObject DeckField = new JSONObject();
            DeckField.put("bookId", "Falcon API Automation");
            DeckField.put("productId", "30b18904-dcc1-4f0f-bf0c-5cc91fa98e8b");
            DeckField.put("title", "Falcon API Automation");

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            createDeckPayload.put("decks", decks);
            String payload = String.valueOf(createDeckPayload);
            DeckControllerRequestDTO = mapper.readValue(payload, DeckControllerRequestDTO.class);
            DeckControllerResponseDTO = createDeck(headerNormal, payload);

            Assert.assertEquals(StatusCode, 201, "Status Code");
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getProductId(),DeckControllerRequestDTO.getDecks()[0].getProductId());
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getBookId(),DeckControllerRequestDTO.getDecks()[0].getBookId());
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getDeckType(),"USER");
            DeleteDeckWithFullPayload();
        }
        catch (Exception e){
            throw e;
        }}

    @Test(priority = 19) @AlmAnnotation(almTestId = "xxxx")
    public void createDeckWithOnlyBookIdAndbookOrProductId() throws Exception {
        try {
            JSONObject createDeckPayload = jsonReader.getJsonObject(DeckCreatePayload);

            JSONObject DeckField = new JSONObject();
            DeckField.put("bookId", "Falcon API Automation");
            DeckField.put("bookOrProductId", "30b18904-dcc1-4f0f-bf0c-5cc91fa98e8b");
            DeckField.put("title", "Falcon API Automation");

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            createDeckPayload.put("decks", decks);
            String payload = String.valueOf(createDeckPayload);
            DeckControllerRequestDTO = mapper.readValue(payload, DeckControllerRequestDTO.class);
            DeckControllerResponseDTO = createDeck(headerNormal, payload);

            Assert.assertEquals(StatusCode, 201, "Status Code");
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getBookId(),DeckControllerRequestDTO.getDecks()[0].getBookId()," Book Id");
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getDeckType(),"USER","Deck Type");
            DeleteDeckWithFullPayload();
        }
        catch (Exception e){
            throw e;
        }}

    @Test(priority = 20) @AlmAnnotation(almTestId = "xxxx")
    public void createDeckWithOnlyProductIdAndbookOrProductId() throws Exception {
        try {
            JSONObject createDeckPayload = jsonReader.getJsonObject(DeckCreatePayload);

            JSONObject DeckField = new JSONObject();
            DeckField.put("productId", "Falcon API Automation");
            DeckField.put("bookOrProductId", "30b18904-dcc1-4f0f-bf0c-5cc91fa98e8b");
            DeckField.put("title", "Falcon API Automation");

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            createDeckPayload.put("decks", decks);
            String payload = String.valueOf(createDeckPayload);
            DeckControllerRequestDTO = mapper.readValue(payload, DeckControllerRequestDTO.class);
            DeckControllerResponseDTO = createDeck(headerNormal, payload);

            Assert.assertEquals(StatusCode, 201, "Status Code");
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getProductId(),DeckControllerRequestDTO.getDecks()[0].getProductId()," Product Id");
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getDeckType(),"USER","Deck Type");
            DeleteDeckWithFullPayload();
        }
        catch (Exception e){
            throw e;
        }}

    @Test(priority = 21) @AlmAnnotation(almTestId = "xxxx")
    public void createDeckWithOnlyProductIdBookIdAndBookOrProductId() throws Exception {
        try {
            JSONObject createDeckPayload = jsonReader.getJsonObject(DeckCreatePayload);

            JSONObject DeckField = new JSONObject();
            DeckField.put("productId", "Falcon API Automation");
            DeckField.put("bookId", "30b18904-dcc1-4f0f-bf0c-5cc91fa98e8b");
            DeckField.put("bookOrProductId", "aaaa");
            DeckField.put("title", "Falcon API Automation");

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            createDeckPayload.put("decks", decks);
            String payload = String.valueOf(createDeckPayload);
            DeckControllerRequestDTO = mapper.readValue(payload, DeckControllerRequestDTO.class);
            DeckControllerResponseDTO = createDeck(headerNormal, payload);

            Assert.assertEquals(StatusCode, 201, "Status Code");
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getProductId(),DeckControllerRequestDTO.getDecks()[0].getProductId()," Product Id");
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getBookId(),DeckControllerRequestDTO.getDecks()[0].getBookId()," Product Id");
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getDeckType(),"USER","Deck Type");
            DeleteDeckWithFullPayload();
        }
        catch (Exception e){
            throw e;
        }}

    //Verify whether the decks can be created giving only bookId and chapterId when the tenent is etext
    @Test(priority = 22) @AlmAnnotation(almTestId = "598686")
    public void createDeckWithOnlyBookIdAndChapterId() throws Exception {
        try {
            JSONObject createDeckPayload = jsonReader.getJsonObject(DeckCreatePayload);

            JSONObject DeckField = new JSONObject();
            DeckField.put("bookId", "30b18904-dcc1-4f0f-bf0c-5cc91fa98e8b");
            DeckField.put("chapterId", "123");
            DeckField.put("title", "Falcon API Automation");

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            createDeckPayload.put("decks", decks);
            String payload = String.valueOf(createDeckPayload);
            DeckControllerRequestDTO = mapper.readValue(payload, DeckControllerRequestDTO.class);
            DeckControllerResponseDTO = createDeck(headerNormal, payload);

            Assert.assertEquals(StatusCode, 201, "Status Code");
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getBookId(),"30b18904-dcc1-4f0f-bf0c-5cc91fa98e8b"," Book Id");
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getChapterId(),"123"," Chapter Id");
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getDeckType(),"USER","Deck Type");
            DeleteDeckWithFullPayload();
        }
        catch (Exception e){
            throw e;
        }}

    //Verify whether the decks can be created giving only bookId and chapterId and sectionId when the tenent is etext
    @Test(priority = 23) @AlmAnnotation(almTestId = "598687")
    public void createDeckWithOnlyBookIdSectionIdAndChapterId() throws Exception {
        try {
            JSONObject createDeckPayload = jsonReader.getJsonObject(DeckCreatePayload);

            JSONObject DeckField = new JSONObject();
            //DeckField.put("productId", "Falcon API Automation");
            DeckField.put("bookId", "30b18904-dcc1-4f0f-bf0c-5cc91fa98e8b");
            DeckField.put("chapterId", "123");
            DeckField.put("sectionId", "123");
            DeckField.put("title", "Falcon API Automation");

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            createDeckPayload.put("decks", decks);
            String payload = String.valueOf(createDeckPayload);
            DeckControllerRequestDTO = mapper.readValue(payload, DeckControllerRequestDTO.class);
            DeckControllerResponseDTO = createDeck(headerNormal, payload);

            Assert.assertEquals(StatusCode, 201, "Status Code");
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getBookId(),"30b18904-dcc1-4f0f-bf0c-5cc91fa98e8b"," Book Id");
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getChapterId(),"123"," Chapter Id");
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getSectionId(),"123"," Section Id");
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getDeckType(),"USER","Deck Type");
            DeleteDeckWithFullPayload();
        }
        catch (Exception e){
            throw e;
        }}

    @Test(priority = 24) @AlmAnnotation(almTestId = "xxxx")
    public void createDeckWithOnlyProductIdBookIdAndChapterId() throws Exception {
        try {
            JSONObject createDeckPayload = jsonReader.getJsonObject(DeckCreatePayload);

            JSONObject DeckField = new JSONObject();
            DeckField.put("productId", "Falcon API Automation");
            DeckField.put("bookId", "30b18904-dcc1-4f0f-bf0c-5cc91fa98e8b");
            DeckField.put("chapterId", "123");
            DeckField.put("title", "Falcon API Automation");

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            createDeckPayload.put("decks", decks);
            String payload = String.valueOf(createDeckPayload);
            DeckControllerRequestDTO = mapper.readValue(payload, DeckControllerRequestDTO.class);
            DeckControllerResponseDTO = createDeck(headerNormal, payload);

            Assert.assertEquals(StatusCode, 201, "Status Code");
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getBookId(),"30b18904-dcc1-4f0f-bf0c-5cc91fa98e8b"," Book Id");
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getChapterId(),"123"," Chapter Id");
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getProductId(),"Falcon API Automation"," Section Id");
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getDeckType(),"USER","Deck Type");
            DeleteDeckWithFullPayload();
        }
        catch (Exception e){
            throw e;
        }}

    //Validate book hierarchy when the tenant is e-text
    @Test(priority = 25) @AlmAnnotation(almTestId = "584146")
    public void createDeckWithOnlyProductIdBookIdChapterIdAndSectionId() throws Exception {
        try {
            JSONObject createDeckPayload = jsonReader.getJsonObject(DeckCreatePayload);

            JSONObject DeckField = new JSONObject();
            DeckField.put("productId", "Falcon API Automation");
            DeckField.put("bookId", "30b18904-dcc1-4f0f-bf0c-5cc91fa98e8b");
            DeckField.put("chapterId", "123");
            DeckField.put("sectionId", "123");
            DeckField.put("title", "Falcon API Automation");

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            createDeckPayload.put("decks", decks);
            String payload = String.valueOf(createDeckPayload);
            DeckControllerRequestDTO = mapper.readValue(payload, DeckControllerRequestDTO.class);
            DeckControllerResponseDTO = createDeck(headerNormal, payload);

            Assert.assertEquals(StatusCode, 201, "Status Code");
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getBookId(),"30b18904-dcc1-4f0f-bf0c-5cc91fa98e8b"," Book Id");
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getChapterId(),"123"," Chapter Id");
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getSectionId(),"123"," Chapter Id");
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getProductId(),"Falcon API Automation"," Section Id");
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getDeckType(),"USER","Deck Type");
            DeleteDeckWithFullPayload();
        }
        catch (Exception e){
            throw e;
        }}

    //Verify whether the decks can be created giving only productId, bookId bookOrProductId and chapterId when the tenent is etext
    @Test(priority = 26) @AlmAnnotation(almTestId = "598684")
    public void createDeckWithOnlyProductIdBookIdbookOrProductIdAndChapterId() throws Exception {
        try {
            JSONObject createDeckPayload = jsonReader.getJsonObject(DeckCreatePayload);

            JSONObject DeckField = new JSONObject();
            DeckField.put("productId", "Falcon API Automation");
            DeckField.put("bookId", "30b18904-dcc1-4f0f-bf0c-5cc91fa98e8b");
            DeckField.put("bookOrProductId", "22847");
            DeckField.put("chapterId", "123");
            //DeckField.put("sectionId", "123");
            DeckField.put("title", "Falcon API Automation");

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            createDeckPayload.put("decks", decks);
            String payload = String.valueOf(createDeckPayload);
            DeckControllerRequestDTO = mapper.readValue(payload, DeckControllerRequestDTO.class);
            DeckControllerResponseDTO = createDeck(headerNormal, payload);

            Assert.assertEquals(StatusCode, 201, "Status Code");
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getBookId(),"30b18904-dcc1-4f0f-bf0c-5cc91fa98e8b"," Book Id");
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getChapterId(),"123"," Chapter Id");
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getProductId(),"Falcon API Automation"," Product Id");
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getDeckType(),"USER","Deck Type");
            DeleteDeckWithFullPayload();
        }
        catch (Exception e){
            throw e;
        }}

    //Verify whether the decks can be created giving only productId, bookId bookOrProductId and chapterId and sectionId when the tenent is etext
    @Test(priority = 27) @AlmAnnotation(almTestId = "598685")
    public void createDeckWithOnlyProductIdBookIdbookOrProductIdChapterIdAndSectionId() throws Exception {
        try {
            JSONObject createDeckPayload = jsonReader.getJsonObject(DeckCreatePayload);

            JSONObject DeckField = new JSONObject();
            DeckField.put("productId", "Falcon API Automation");
            DeckField.put("bookId", "30b18904-dcc1-4f0f-bf0c-5cc91fa98e8b");
            DeckField.put("bookOrProductId", "22847");
            DeckField.put("chapterId", "123");
            DeckField.put("sectionId", "123");
            DeckField.put("title", "Falcon API Automation");

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            createDeckPayload.put("decks", decks);
            String payload = String.valueOf(createDeckPayload);
            DeckControllerRequestDTO = mapper.readValue(payload, DeckControllerRequestDTO.class);
            DeckControllerResponseDTO = createDeck(headerNormal, payload);

            Assert.assertEquals(StatusCode, 201, "Status Code");
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getBookId(),"30b18904-dcc1-4f0f-bf0c-5cc91fa98e8b"," Book Id");
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getChapterId(),"123"," Chapter Id");
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getSectionId(),"123"," Section Id");
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getProductId(),"Falcon API Automation"," Product Id");
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getDeckType(),"USER","Deck Type");
            DeleteDeckWithFullPayload();
        }
        catch (Exception e){
            throw e;
        }}

    @Test(priority = 28) @AlmAnnotation(almTestId = "xxxx")
    public void createDeckWithOnlyProductIdAndChapterId() throws Exception {
        try {
            JSONObject createDeckPayload = jsonReader.getJsonObject(DeckCreatePayload);

            JSONObject DeckField = new JSONObject();
            DeckField.put("productId", "Falcon API Automation");
            DeckField.put("chapterId", "123");
            DeckField.put("title", "Falcon API Automation");

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            createDeckPayload.put("decks", decks);
            String payload = String.valueOf(createDeckPayload);
            DeckControllerRequestDTO = mapper.readValue(payload, DeckControllerRequestDTO.class);
            DeckControllerResponseDTO = createDeck(headerNormal, payload);

            Assert.assertEquals(StatusCode, 201, "Status Code");
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getChapterId(),"123"," Chapter Id");
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getProductId(),"Falcon API Automation"," Product Id");
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getDeckType(),"USER","Deck Type");
            DeleteDeckWithFullPayload();
        }
        catch (Exception e){
            throw e;
        }}

    @Test(priority = 29) @AlmAnnotation(almTestId = "xxxx")
    public void createDeckWithOnlyProductIdChapterIdAndSectionId() throws Exception {
        try {
            JSONObject createDeckPayload = jsonReader.getJsonObject(DeckCreatePayload);

            JSONObject DeckField = new JSONObject();
            DeckField.put("productId", "Falcon API Automation");
            DeckField.put("chapterId", "123");
            DeckField.put("sectionId", "123");
            DeckField.put("title", "Falcon API Automation");

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            createDeckPayload.put("decks", decks);
            String payload = String.valueOf(createDeckPayload);
            DeckControllerRequestDTO = mapper.readValue(payload, DeckControllerRequestDTO.class);
            DeckControllerResponseDTO = createDeck(headerNormal, payload);

            Assert.assertEquals(StatusCode, 201, "Status Code");
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getChapterId(),"123"," Chapter Id");
             Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getSectionId(),"123"," Section Id");
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getProductId(),"Falcon API Automation"," Product Id");
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getDeckType(),"USER","Deck Type");
            DeleteDeckWithFullPayload();
        }
        catch (Exception e){
            throw e;
        }}

    //Verify whether the decks can be created giving only bookId and bookIdOrProductId and chapterId and sectionId when the tenant is e-text
    @Test(priority = 30) @AlmAnnotation(almTestId = "598688")
    public void createDeckWithOnlyProductIdBookOrProductIdChapterIdAndSectionId() throws Exception {
        try {
            JSONObject createDeckPayload = jsonReader.getJsonObject(DeckCreatePayload);

            JSONObject DeckField = new JSONObject();
            DeckField.put("productId", "Falcon API Automation");
            DeckField.put("bookOrProductId", "22847");
            DeckField.put("chapterId", "123");
            DeckField.put("sectionId", "123");
            DeckField.put("title", "Falcon API Automation");

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            createDeckPayload.put("decks", decks);
            String payload = String.valueOf(createDeckPayload);
            DeckControllerRequestDTO = mapper.readValue(payload, DeckControllerRequestDTO.class);
            DeckControllerResponseDTO = createDeck(headerNormal, payload);

            Assert.assertEquals(StatusCode, 201, "Status Code");
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getChapterId(),"123"," Chapter Id");
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getSectionId(),"123"," Section Id");
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getProductId(),"Falcon API Automation"," Product Id");
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getDeckType(),"USER","Deck Type");
            DeleteDeckWithFullPayload();
        }
        catch (Exception e){
            throw e;
        }}

    //Verify whether the decks can be created giving only bookId and bookIdOrProductId and chapterId when the tenent is etext
    @Test(priority = 31) @AlmAnnotation(almTestId = "598689")
    public void createDeckWithOnlyProductIdbookOrProductIdAndChapterId() throws Exception {
        try {
            JSONObject createDeckPayload = jsonReader.getJsonObject(DeckCreatePayload);

            JSONObject DeckField = new JSONObject();
            DeckField.put("productId", "Falcon API Automation");
            DeckField.put("bookOrProductId", "22847");
            DeckField.put("chapterId", "123");
            DeckField.put("title", "Falcon API Automation");

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            createDeckPayload.put("decks", decks);
            String payload = String.valueOf(createDeckPayload);
            DeckControllerRequestDTO = mapper.readValue(payload, DeckControllerRequestDTO.class);
            DeckControllerResponseDTO = createDeck(headerNormal, payload);

            Assert.assertEquals(StatusCode, 201, "Status Code");
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getChapterId(),"123"," Chapter Id");
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getProductId(),"Falcon API Automation"," Product Id");
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getDeckType(),"USER","Deck Type");
            DeleteDeckWithFullPayload();
        }
        catch (Exception e){
            throw e;
        }}

    @Test(priority = 32) @AlmAnnotation(almTestId = "xxx")
    public void createDeckWithOnlybookOrProductIdAndChapterId() throws Exception {
        try {
            JSONObject createDeckPayload = jsonReader.getJsonObject(DeckCreatePayload);

            JSONObject DeckField = new JSONObject();
            DeckField.put("bookOrProductId", "30b18904-dcc1-4f0f-bf0c-5cc91fa98e8b");
            DeckField.put("chapterId", "123");
            DeckField.put("title", "Falcon API Automation");

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            createDeckPayload.put("decks", decks);
            String payload = String.valueOf(createDeckPayload);
            DeckControllerRequestDTO = mapper.readValue(payload, DeckControllerRequestDTO.class);
            DeckControllerResponseDTO = createDeck(headerNormal, payload);

            Assert.assertEquals(StatusCode, 201, "Status Code");
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getChapterId(),"123"," Chapter Id");
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getProductId(),"30b18904-dcc1-4f0f-bf0c-5cc91fa98e8b"," Product Id");
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getDeckType(),"USER","Deck Type");
            DeleteDeckWithFullPayload();
        }
        catch (Exception e){
            throw e;
        }}

    @Test(priority = 33) @AlmAnnotation(almTestId = "xxxx")
    public void createDeckWithOnlybookOrProductIdChapterIdAndSectionId() throws Exception {
        try {
            JSONObject createDeckPayload = jsonReader.getJsonObject(DeckCreatePayload);

            JSONObject DeckField = new JSONObject();
            DeckField.put("bookOrProductId", "30b18904-dcc1-4f0f-bf0c-5cc91fa98e8b");
            DeckField.put("chapterId", "123");
            DeckField.put("sectionId", "123");
            DeckField.put("title", "Falcon API Automation");

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            createDeckPayload.put("decks", decks);
            String payload = String.valueOf(createDeckPayload);
            DeckControllerRequestDTO = mapper.readValue(payload, DeckControllerRequestDTO.class);
            DeckControllerResponseDTO = createDeck(headerNormal, payload);

            Assert.assertEquals(StatusCode, 201, "Status Code");
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getChapterId(),"123"," Chapter Id");
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getSectionId(),"123"," Section Id");
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getProductId(),"30b18904-dcc1-4f0f-bf0c-5cc91fa98e8b"," Product Id");
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getDeckType(),"USER","Deck Type");
            DeleteDeckWithFullPayload();
        }
        catch (Exception e){
            throw e;
        }}

    //Verify whether an error is thrown when ONLY sectionId is sent
    @Test(priority = 34) @AlmAnnotation(almTestId = "598690")
    public void createDeckWithOnlySectionId() throws Exception {
        try {
            JSONObject createDeckPayload = jsonReader.getJsonObject(DeckCreatePayload);

            JSONObject DeckField = new JSONObject();
            DeckField.put("sectionId", "123");
            DeckField.put("title", "Falcon API Automation");

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            createDeckPayload.put("decks", decks);
            String payload = String.valueOf(createDeckPayload);
            DeckControllerRequestDTO = mapper.readValue(payload, DeckControllerRequestDTO.class);
            DeckControllerResponseDTO = createDeck(headerNormal, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            if(DeckControllerResponseDTO.getFieldErrors()[0].getMessage().equals("'bookId', 'productId' or 'bookOrProductId' must be provided")){
                Assert.assertEquals(DeckControllerResponseDTO.getFieldErrors()[0].getMessage(), "'bookId', 'productId' or 'bookOrProductId' must be provided", " error message");
                Assert.assertEquals(DeckControllerResponseDTO.getFieldErrors()[1].getMessage(), "Book hierarchy is not valid", " error message");
            }
            else{
                Assert.assertEquals(DeckControllerResponseDTO.getFieldErrors()[1].getMessage(), "'bookId', 'productId' or 'bookOrProductId' must be provided", " error message");
                Assert.assertEquals(DeckControllerResponseDTO.getFieldErrors()[0].getMessage(), "Book hierarchy is not valid", " error message");
            }
        }
        catch (Exception e){
            throw e;
        }}

    //Verify whether an error is thrown when ONLY chapterId and sectionId are sent
    @Test(priority = 35) @AlmAnnotation(almTestId = "598691")
    public void createDeckWithOnlyChapterIdAndSectionId() throws Exception {
        try {
            JSONObject createDeckPayload = jsonReader.getJsonObject(DeckCreatePayload);

            JSONObject DeckField = new JSONObject();
            DeckField.put("chapterId", "123");
            DeckField.put("sectionId", "123");
            DeckField.put("title", "Falcon API Automation");

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            createDeckPayload.put("decks", decks);
            String payload = String.valueOf(createDeckPayload);
            DeckControllerRequestDTO = mapper.readValue(payload, DeckControllerRequestDTO.class);
            DeckControllerResponseDTO = createDeck(headerNormal, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            if(DeckControllerResponseDTO.getFieldErrors()[0].getMessage().equals("'bookId', 'productId' or 'bookOrProductId' must be provided")){
                Assert.assertEquals(DeckControllerResponseDTO.getFieldErrors()[1].getMessage(),"Book hierarchy is not valid"," error message");
                Assert.assertEquals(DeckControllerResponseDTO.getFieldErrors()[0].getMessage(),"'bookId', 'productId' or 'bookOrProductId' must be provided"," error message");
            }
            else {
                Assert.assertEquals(DeckControllerResponseDTO.getFieldErrors()[0].getMessage(), "Book hierarchy is not valid", " error message");
                Assert.assertEquals(DeckControllerResponseDTO.getFieldErrors()[1].getMessage(), "'bookId', 'productId' or 'bookOrProductId' must be provided", " error message");
            }

        }
        catch (Exception e){
            throw e;
        }}

    //Verify whether the deckType is returned when a deck is created
    @Test(priority = 36) @AlmAnnotation(almTestId = "598682")
    public void createDeckWithValidatingDeckTypeField() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            Assert.assertEquals(StatusCode, 201, "Status Code");
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getDeckType(),"USER"," Deck Type");
            DeleteDeckWithFullPayload();
        }
        catch (Exception e){
            throw e;
        }}

    //Verify epoch times updated accurately
    @Test(priority = 37) @AlmAnnotation(almTestId = "584148")
    public void createDeckWithCreatedTimeAndUpdatedTime() throws Exception {
        try {
            JSONObject createDeckPayload = jsonReader.getJsonObject(DeckCreatePayload);

            JSONObject DeckField = new JSONObject();
            DeckField.put("chapterId", "123");
            DeckField.put("sectionId", "123");
            DeckField.put("title", "Falcon API Automation");
            DeckField.put("productId", "30b18904-dcc1-4f0f-bf0c-5cc91fa98e8b");
            DeckField.put("bookId", "aaa");
            DeckField.put("userCreatedAt", "2021-06-03T07:25:07.074Z");
            DeckField.put("userModifiedAt", "2021-06-03T07:25:07.074Z");

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            createDeckPayload.put("decks", decks);
            String payload = String.valueOf(createDeckPayload);
            DeckControllerRequestDTO = mapper.readValue(payload, DeckControllerRequestDTO.class);
            DeckControllerResponseDTO = createDeck(headerNormal, payload);

            Assert.assertEquals(StatusCode, 201, "Status Code");
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getChapterId(),"123"," Chapter Id");
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getSectionId(),"123"," Section Id");
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getProductId(),"30b18904-dcc1-4f0f-bf0c-5cc91fa98e8b"," Product Id");
            Assert.assertEquals(DeckControllerResponseDTO.getDecks()[0].getDeckType(),"USER","Deck Type");
            DeleteDeckWithFullPayload();

        }
        catch (Exception e){
            throw e;
        }}


}
