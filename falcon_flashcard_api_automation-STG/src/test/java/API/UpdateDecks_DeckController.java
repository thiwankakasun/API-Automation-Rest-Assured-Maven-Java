package API;

import com.pearson.common.framework.shared.alm.AlmAnnotation;
import org.apache.pdfbox.cos.COSInteger;
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

public class UpdateDecks_DeckController extends ServiceController {

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

    private static final Logger LOGGER = Logger.getLogger(String.valueOf(UpdateDecks_DeckController.class));

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

    //Verify whether the user is being able to update the title of the created deck
    @Test(priority =8) @AlmAnnotation(almTestId = "583191")
    public void UpdateDeckWithUpdatingOnlyTitle() throws Exception {
        try {

            createSingleDeckWithFullPayload();
            JSONObject updateDeckPayload = jsonReader.getJsonObject(DeckUpdatePayload);

            updateDeckPayload.put("title","Falcon API Automation - updated new");
            System.out.println(updateDeckPayload);
            String payload = String.valueOf(updateDeckPayload);
            DeckRequestDTO = mapper.readValue(payload, DeckRequestDTO.class);

            String DeckId = DeckControllerResponseDTO.getDecks()[0].getId();
            DeckUpdateDTO = updateDeck(headerNormal, payload, DeckId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(DeckUpdateDTO.getTitle(),"Falcon API Automation - updated new","Title can be updated");
            DeleteDeckWithFullPayload();

        } catch (Exception e) {
            throw e;
        }
    }

    //Verify whether the user cannot update a deck name with a empty field
    @Test(priority =9) @AlmAnnotation(almTestId = "583192")
    public void UpdateDeckWithEmptyTitle() throws Exception {
        try {

            createSingleDeckWithFullPayload();
            JSONObject updateDeckPayload = jsonReader.getJsonObject(DeckUpdatePayload);

            updateDeckPayload.put("title","");
            System.out.println(updateDeckPayload);
            String payload = String.valueOf(updateDeckPayload);
            DeckRequestDTO = mapper.readValue(payload, DeckRequestDTO.class);

            String DeckId = DeckControllerResponseDTO.getDecks()[0].getId();
            DeckUpdateDTO = updateDeck(headerNormal, payload, DeckId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(DeckUpdateDTO.getFieldErrors()[0].getMessage(),"size must be between 1 and 1000","Title can not be empty");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    //Verify whether the space is not considered as a character when updating the deck
    @Test(priority =10) @AlmAnnotation(almTestId = "583194")
    public void UpdateDeckWithSpaceForTitle() throws Exception {
        try {

            createSingleDeckWithFullPayload();
            JSONObject updateDeckPayload = jsonReader.getJsonObject(DeckUpdatePayload);

            updateDeckPayload.put("title"," ");
            System.out.println(updateDeckPayload);
            String payload = String.valueOf(updateDeckPayload);
            DeckRequestDTO = mapper.readValue(payload, DeckRequestDTO.class);

            String DeckId = DeckControllerResponseDTO.getDecks()[0].getId();
            DeckUpdateDTO = updateDeck(headerNormal, payload, DeckId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            //title will updated with same result. not replaced by space
            Assert.assertEquals(DeckUpdateDTO.getTitle(),"Falcon API Automation","Title can be updated");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    //Verify whether the maximum characters that can be given to the name is 1000 including the space characters
    @Test(priority =11) @AlmAnnotation(almTestId = "583195")
    public void UpdateDeckWith1000CharForTitle() throws Exception {
        try {

            createSingleDeckWithFullPayload();
            JSONObject updateDeckPayload = jsonReader.getJsonObject(DeckUpdatePayload);

            updateDeckPayload.put("title","zDyaQra04eUjY2lHx02aM6Z9NkkEHHMkuhSDXHCnwFpjwj0stJN0NEjfJCEubk5cK" +
                    "9hcvopMJ80MzkGuTGhfnvEZ7RKSKreWXoJdhFsaeWLxPIYQxeA56tS0cl1tRl78LI6g0M2PeeSLkyf4oCxEj7mjg" +
                    "lLU7xiEunR3MfZhMmqgxYoZtD1vxPWqvSJmqLlE636wJSdU5npLqFx74oFXUgXV5iOC0vYfm2nBxylUOWF7BmtbC" +
                    "jPBY9RwSbDLNtRprH7ckA4kfeHklhQ1b615KIWQKOE3UBwEnBNTgxNXUmEqfKtca1c1cY4BnSchTdY8t1d1FWfQmg" +
                    "9leyhiLH8Vp4fXrWHRweFhoaek6KdBLu4Ri3ZxkesSAFmwhjiydlafrzFfDPcCIVcdnJRmsWECPovO7PaVzFaIx9" +
                    "oFa3PrYkompOzYIKxi1WZIwBTdhYnfagV2NaBWLgkCxs85bXK9vEaKLdbZcIlNULDjUuw8DxopjzLtKTYl5YayA" +
                    "IAFiCw5iE1R4syHV5bnhH3U8V1fOCb7mHTJl3VcgjzZDHlOjQvUaGrvVwA9Uss7ExXti1eiPF5p0uinsQfIGFZ" +
                    "gVduDzUqiKEITemZNxPYz8DH3F6RW4E4osCCNXQ9jiszKrhNHi8p58mg88RCzivD7watGrDpVr0g3AsZgbywVYp7" +
                    "uEotSmxOLntLIkTDv7gvUmVFoetmRuphSIXw8eTjXxy9kxJzHavn8KPpEwLUAj9JW9AM1Vn7SiMCHaKkhTx5QCM8c" +
                    "ytTi0zFmlYHN4gZkqu5KWKUBHMtxMaFAceNME03eRHzK7mwtdckcLzSMpH8i8Q1zSE1V1kHOonwdX40ozEDW9sunu" +
                    "iPbUwt7rgl2Wr8fAEVOmVOe9WdTdXSPIJ77jY9MMtLawhiYHbv4oqMPBAJpv0XA0mlK2WhVf96oGSOQ2B6a4PI76R" +
                    "BD26EAe5nVxZskXffUT5EZ1BMe9vbtMdaRGBmw82ulzvMRtkNsJmyPdg dgdhghdf");
            System.out.println(updateDeckPayload);
            String payload = String.valueOf(updateDeckPayload);
            DeckRequestDTO = mapper.readValue(payload, DeckRequestDTO.class);

            String DeckId = DeckControllerResponseDTO.getDecks()[0].getId();
            DeckUpdateDTO = updateDeck(headerNormal, payload, DeckId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(DeckUpdateDTO.getFieldErrors()[0].getMessage(),"size must be between 1 and 1000","Title can not be empty");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    //Verify whether the minimum characters that can be given to the name is one
    @Test(priority =12) @AlmAnnotation(almTestId = "583196")
    public void UpdateDeckWithSingleCharacterForTitle() throws Exception {
        try {

            createSingleDeckWithFullPayload();
            JSONObject updateDeckPayload = jsonReader.getJsonObject(DeckUpdatePayload);

            updateDeckPayload.put("title","a");
            System.out.println(updateDeckPayload);
            String payload = String.valueOf(updateDeckPayload);
            DeckRequestDTO = mapper.readValue(payload, DeckRequestDTO.class);

            String DeckId = DeckControllerResponseDTO.getDecks()[0].getId();
            DeckUpdateDTO = updateDeck(headerNormal, payload, DeckId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(DeckUpdateDTO.getTitle(),"a","minimum characters that can be given to the Title is one");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    //Verify when an invalid deckId is sent
    @Test(priority =13) @AlmAnnotation(almTestId = "583205")
    public void UpdateDeckUsingInvalidDeckId() throws Exception {
        try {

            JSONObject updateDeckPayload = jsonReader.getJsonObject(DeckUpdatePayload);
            System.out.println(updateDeckPayload);
            String payload = String.valueOf(updateDeckPayload);
            DeckRequestDTO = mapper.readValue(payload, DeckRequestDTO.class);

            String DeckId = "123";
            DeckUpdateDTO = updateDeck(headerNormal, payload, DeckId);

            Assert.assertEquals(StatusCode, 404, "Status Code");
            Assert.assertEquals(DeckUpdateDTO.getDescription(),"Invalid Deck ID","Invalid Deck ID error message");
        } catch (Exception e) {
            throw e;
        }
    }

    //Verify when a deleted deckId is sent
    @Test(priority =14) @AlmAnnotation(almTestId = "583206")
    public void UpdateDeckUsingDeletedDeckId() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            String DeckId = DeckControllerResponseDTO.getDecks()[0].getId();
            DeleteDeckWithFullPayload();
            JSONObject updateDeckPayload = jsonReader.getJsonObject(DeckUpdatePayload);
            System.out.println(updateDeckPayload);
            String payload = String.valueOf(updateDeckPayload);
            DeckRequestDTO = mapper.readValue(payload, DeckRequestDTO.class);

            DeckUpdateDTO = updateDeck(headerNormal, payload, DeckId);

            Assert.assertEquals(StatusCode, 404, "Status Code");
            Assert.assertEquals(DeckUpdateDTO.getDescription(),"Invalid Deck ID","Invalid Deck ID error message");
        } catch (Exception e) {
            throw e;
        }
    }

    //Validate whether the deck belongs to the same user
    @Test(priority =15) @AlmAnnotation(almTestId = "583198")
    public void UpdateDeckWithDifferentUser() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            if (Env.equals("DEV")) {
                header.put("X-TenantId", "2b4af3fc-97b4-41a3-af7e-bc9354d35fed");
                header.put("X-TenantKey", "9acc0488-6a42-4c54-8d7a-edbac1716e73");
            }

            if (Env.equals("QA")) {
                header.put("X-TenantId", "2b4af3fc-97b4-41a3-af7e-bc9354d35fed");
                header.put("X-TenantKey", "152be39d-8699-4ae9-9bae-91a50c28b6d0");
            }

            if (Env.equals("STG")) {
                header.put("X-TenantId", "30258aed-46bb-4ec5-909e-ecea2431772b");
                header.put("X-TenantKey", "297a5917-24fa-43fe-a27b-426126bd7cf4");
            }

            if (Env.equals("PROD")) {
                header.put("X-TenantId", "beeea450-02f6-4f88-842e-cec9364453b7");
                header.put("X-TenantKey", "21d77376-46b2-4e26-8781-2d1a59468f8f");
            }
            JSONObject updateDeckPayload = jsonReader.getJsonObject(DeckUpdatePayload);

            System.out.println(updateDeckPayload);
            String payload = String.valueOf(updateDeckPayload);
            DeckRequestDTO = mapper.readValue(payload, DeckRequestDTO.class);

            String DeckId = DeckControllerResponseDTO.getDecks()[0].getId();
            DeckUpdateDTO = updateDeck(header, payload, DeckId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(DeckUpdateDTO.getDescription(),"User IDs do not match","User IDs do not match error message validation");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    //Validate the tenant ID field
    @Test(priority =16) @AlmAnnotation(almTestId = "583197")
    public void UpdateDeckWithInvalidTenantId() throws Exception {
        try {

            createSingleDeckWithFullPayload();

            headerNormal.put("X-TenantId", "aa");
            JSONObject updateDeckPayload = jsonReader.getJsonObject(DeckUpdatePayload);

            System.out.println(updateDeckPayload);
            String payload = String.valueOf(updateDeckPayload);
            DeckRequestDTO = mapper.readValue(payload, DeckRequestDTO.class);

            String DeckId = DeckControllerResponseDTO.getDecks()[0].getId();
            DeckUpdateDTO = updateDeck(headerNormal, payload, DeckId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(DeckUpdateDTO.getDescription(),"Field 'tenantId' does not have a valid value","User IDs do not match error message validation");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =17) @AlmAnnotation(almTestId = "611101")
    public void UpdateDeckWithInvalidTenantKey() throws Exception {
        try {

            getData();
            createSingleDeckWithFullPayload();

            headerNormal.put("X-TenantKey", "aa");
            JSONObject updateDeckPayload = jsonReader.getJsonObject(DeckUpdatePayload);

            System.out.println(updateDeckPayload);
            String payload = String.valueOf(updateDeckPayload);
            DeckRequestDTO = mapper.readValue(payload, DeckRequestDTO.class);

            String DeckId = DeckControllerResponseDTO.getDecks()[0].getId();
            DeckUpdateDTO = updateDeck(headerNormal, payload, DeckId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(DeckUpdateDTO.getDescription(),"Field 'tenantKey' does not have a valid value","User IDs do not match error message validation");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    //Verify whether the bookId cannot be null
    @Test(priority =18) @AlmAnnotation(almTestId = "583199")
    public void UpdateDeckWithValidating_ProductId_bookId_bookOrProductId_cannot_be_null() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            JSONObject updateDeckPayload = jsonReader.getJsonObject(DeckUpdatePayload);
            System.out.println(updateDeckPayload);
            updateDeckPayload.put("productId",null);
            String payload = String.valueOf(updateDeckPayload);
            DeckRequestDTO = mapper.readValue(payload, DeckRequestDTO.class);

            String DeckId = DeckControllerResponseDTO.getDecks()[0].getId();
            DeckUpdateDTO = updateDeck(headerNormal, payload, DeckId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(DeckUpdateDTO.getObjectErrors()[0].getMessage(),"'bookId', 'productId' or 'bookOrProductId' must be provided","User IDs do not match error message validation");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =19) @AlmAnnotation(almTestId = "611101")
    public void UpdateDeckWithOnlyUsingBookId() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            JSONObject updateDeckPayload = jsonReader.getJsonObject(DeckUpdatePayload);
            System.out.println(updateDeckPayload);
            updateDeckPayload.put("productId",null);
            updateDeckPayload.put("bookId","aaa");
            String payload = String.valueOf(updateDeckPayload);
            DeckRequestDTO = mapper.readValue(payload, DeckRequestDTO.class);

            String DeckId = DeckControllerResponseDTO.getDecks()[0].getId();
            DeckUpdateDTO = updateDeck(headerNormal, payload, DeckId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(DeckUpdateDTO.getTitle(),"Falcon API Automation - updated","User IDs do not match error message validation");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =20) @AlmAnnotation(almTestId = "611101")
    public void UpdateDeckWithOnlyUsingbookOrProductId() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            JSONObject updateDeckPayload = jsonReader.getJsonObject(DeckUpdatePayload);
            System.out.println(updateDeckPayload);
            updateDeckPayload.put("productId",null);
            updateDeckPayload.put("bookId",null);
            updateDeckPayload.put("bookOrProductId","aaa");
            String payload = String.valueOf(updateDeckPayload);
            DeckRequestDTO = mapper.readValue(payload, DeckRequestDTO.class);

            String DeckId = DeckControllerResponseDTO.getDecks()[0].getId();
            DeckUpdateDTO = updateDeck(headerNormal, payload, DeckId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(DeckUpdateDTO.getTitle(),"Falcon API Automation - updated","User IDs do not match error message validation");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =21) @AlmAnnotation(almTestId = "611101")
    public void UpdateDeckWithUsingBookIdAndProductId() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            JSONObject updateDeckPayload = jsonReader.getJsonObject(DeckUpdatePayload);
            System.out.println(updateDeckPayload);
            updateDeckPayload.put("productId","f89df1cb-3a0d-4263-97a8-4c286702a0f3");
            updateDeckPayload.put("bookId","aaa");
            updateDeckPayload.put("bookOrProductId",null);
            String payload = String.valueOf(updateDeckPayload);
            DeckRequestDTO = mapper.readValue(payload, DeckRequestDTO.class);

            String DeckId = DeckControllerResponseDTO.getDecks()[0].getId();
            DeckUpdateDTO = updateDeck(headerNormal, payload, DeckId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(DeckUpdateDTO.getTitle(),"Falcon API Automation - updated","User IDs do not match error message validation");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =22) @AlmAnnotation(almTestId = "611101")
    public void UpdateDeckWithUsingbookOrProductIdAndProductId() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            JSONObject updateDeckPayload = jsonReader.getJsonObject(DeckUpdatePayload);
            System.out.println(updateDeckPayload);
            updateDeckPayload.put("productId","f89df1cb-3a0d-4263-97a8-4c286702a0f3");
            updateDeckPayload.put("bookId",null);
            updateDeckPayload.put("bookOrProductId","aaa");
            String payload = String.valueOf(updateDeckPayload);
            DeckRequestDTO = mapper.readValue(payload, DeckRequestDTO.class);

            String DeckId = DeckControllerResponseDTO.getDecks()[0].getId();
            DeckUpdateDTO = updateDeck(headerNormal, payload, DeckId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(DeckUpdateDTO.getTitle(),"Falcon API Automation - updated","User IDs do not match error message validation");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =23) @AlmAnnotation(almTestId = "611101")
    public void UpdateDeckWithUsingbookOrProductIdAndBookId() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            JSONObject updateDeckPayload = jsonReader.getJsonObject(DeckUpdatePayload);
            System.out.println(updateDeckPayload);
            updateDeckPayload.put("productId",null);
            updateDeckPayload.put("bookId","f89df1cb-3a0d-4263-97a8-4c286702a0f3");
            updateDeckPayload.put("bookOrProductId","aaa");
            String payload = String.valueOf(updateDeckPayload);
            DeckRequestDTO = mapper.readValue(payload, DeckRequestDTO.class);

            String DeckId = DeckControllerResponseDTO.getDecks()[0].getId();
            DeckUpdateDTO = updateDeck(headerNormal, payload, DeckId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(DeckUpdateDTO.getTitle(),"Falcon API Automation - updated","User IDs do not match error message validation");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    //Verify whether the deck can be left-alone without a chapter ID
    @Test(priority =24) @AlmAnnotation(almTestId = "583204")
    public void UpdateDeckWithUsingbookOrProductId_BookIdAndProductId() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            JSONObject updateDeckPayload = jsonReader.getJsonObject(DeckUpdatePayload);
            System.out.println(updateDeckPayload);
            updateDeckPayload.put("productId","aaa");
            updateDeckPayload.put("bookId","f89df1cb-3a0d-4263-97a8-4c286702a0f3");
            updateDeckPayload.put("bookOrProductId","aaa");
            String payload = String.valueOf(updateDeckPayload);
            DeckRequestDTO = mapper.readValue(payload, DeckRequestDTO.class);

            String DeckId = DeckControllerResponseDTO.getDecks()[0].getId();
            DeckUpdateDTO = updateDeck(headerNormal, payload, DeckId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(DeckUpdateDTO.getTitle(),"Falcon API Automation - updated","User IDs do not match error message validation");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =25) @AlmAnnotation(almTestId = "611101")
    public void UpdateDeckWithUsingChapterIdAndProductId() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            JSONObject updateDeckPayload = jsonReader.getJsonObject(DeckUpdatePayload);
            System.out.println(updateDeckPayload);
            updateDeckPayload.put("productId","aaa");
            updateDeckPayload.put("bookId",null);
            updateDeckPayload.put("bookOrProductId",null);
            updateDeckPayload.put("chapterId","edited");
            String payload = String.valueOf(updateDeckPayload);
            DeckRequestDTO = mapper.readValue(payload, DeckRequestDTO.class);

            String DeckId = DeckControllerResponseDTO.getDecks()[0].getId();
            DeckUpdateDTO = updateDeck(headerNormal, payload, DeckId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(DeckUpdateDTO.getTitle(),"Falcon API Automation - updated","User IDs do not match error message validation");
            Assert.assertEquals(DeckUpdateDTO.getChapterId(),"edited","User IDs do not match error message validation");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =26) @AlmAnnotation(almTestId = "611101")
    public void UpdateDeckWithUsingChapterId_sectionIdAndProductId() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            JSONObject updateDeckPayload = jsonReader.getJsonObject(DeckUpdatePayload);
            System.out.println(updateDeckPayload);
            updateDeckPayload.put("productId","aaa");
            updateDeckPayload.put("chapterId","edited");
            updateDeckPayload.put("sectionId","edited");
            String payload = String.valueOf(updateDeckPayload);
            DeckRequestDTO = mapper.readValue(payload, DeckRequestDTO.class);

            String DeckId = DeckControllerResponseDTO.getDecks()[0].getId();
            DeckUpdateDTO = updateDeck(headerNormal, payload, DeckId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(DeckUpdateDTO.getTitle(),"Falcon API Automation - updated","User IDs do not match error message validation");
            Assert.assertEquals(DeckUpdateDTO.getChapterId(),"edited","User IDs do not match error message validation");
            Assert.assertEquals(DeckUpdateDTO.getSectionId(),"edited","User IDs do not match error message validation");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =27) @AlmAnnotation(almTestId = "611101")
    public void UpdateDeckWithUsingChapterId_BookIdAndProductId() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            JSONObject updateDeckPayload = jsonReader.getJsonObject(DeckUpdatePayload);
            System.out.println(updateDeckPayload);
            updateDeckPayload.put("productId","aaa");
            updateDeckPayload.put("bookId","aaa");
            updateDeckPayload.put("chapterId","edited");
            String payload = String.valueOf(updateDeckPayload);
            DeckRequestDTO = mapper.readValue(payload, DeckRequestDTO.class);

            String DeckId = DeckControllerResponseDTO.getDecks()[0].getId();
            DeckUpdateDTO = updateDeck(headerNormal, payload, DeckId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(DeckUpdateDTO.getTitle(),"Falcon API Automation - updated","User IDs do not match error message validation");
            Assert.assertEquals(DeckUpdateDTO.getChapterId(),"edited","User IDs do not match error message validation");
            Assert.assertEquals(DeckUpdateDTO.getBookId(),"aaa","User IDs do not match error message validation");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =28) @AlmAnnotation(almTestId = "611101")
    public void UpdateDeckWithUsingSectionId_ChapterId_BookIdAndProductId() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            JSONObject updateDeckPayload = jsonReader.getJsonObject(DeckUpdatePayload);
            System.out.println(updateDeckPayload);
            updateDeckPayload.put("productId","123");
            updateDeckPayload.put("bookId","aaa");
            updateDeckPayload.put("chapterId","edited");
            updateDeckPayload.put("sectionId","edited");
            String payload = String.valueOf(updateDeckPayload);
            DeckRequestDTO = mapper.readValue(payload, DeckRequestDTO.class);

            String DeckId = DeckControllerResponseDTO.getDecks()[0].getId();
            DeckUpdateDTO = updateDeck(headerNormal, payload, DeckId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(DeckUpdateDTO.getTitle(),"Falcon API Automation - updated","User IDs do not match error message validation");
            Assert.assertEquals(DeckUpdateDTO.getChapterId(),"edited","User IDs do not match error message validation");
            Assert.assertEquals(DeckUpdateDTO.getSectionId(),"edited","User IDs do not match error message validation");
            Assert.assertEquals(DeckUpdateDTO.getBookId(),"aaa","User IDs do not match error message validation");
            Assert.assertEquals(DeckUpdateDTO.getProductId(),"123","User IDs do not match error message validation");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =29) @AlmAnnotation(almTestId = "611101")
    public void UpdateDeckWithUsingbookOrProductId_ChapterId_BookIdAndProductId() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            JSONObject updateDeckPayload = jsonReader.getJsonObject(DeckUpdatePayload);
            System.out.println(updateDeckPayload);
            updateDeckPayload.put("productId","123");
            updateDeckPayload.put("bookId","aaa");
            updateDeckPayload.put("bookOrProductId","aaaa");
            updateDeckPayload.put("chapterId","edited");
            String payload = String.valueOf(updateDeckPayload);
            DeckRequestDTO = mapper.readValue(payload, DeckRequestDTO.class);

            String DeckId = DeckControllerResponseDTO.getDecks()[0].getId();
            DeckUpdateDTO = updateDeck(headerNormal, payload, DeckId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(DeckUpdateDTO.getTitle(),"Falcon API Automation - updated","User IDs do not match error message validation");
            Assert.assertEquals(DeckUpdateDTO.getChapterId(),"edited","User IDs do not match error message validation");
            Assert.assertEquals(DeckUpdateDTO.getBookId(),"aaa","User IDs do not match error message validation");
            Assert.assertEquals(DeckUpdateDTO.getProductId(),"123","User IDs do not match error message validation");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =30) @AlmAnnotation(almTestId = "611101")
    public void UpdateDeckWithUsingSectionId_bookOrProductId_ChapterId_BookIdAndProductId() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            JSONObject updateDeckPayload = jsonReader.getJsonObject(DeckUpdatePayload);
            System.out.println(updateDeckPayload);
            updateDeckPayload.put("productId","123");
            updateDeckPayload.put("bookId","aaa");
            updateDeckPayload.put("bookOrProductId","aaaa");
            updateDeckPayload.put("chapterId","edited");
            updateDeckPayload.put("sectionId","edited");
            String payload = String.valueOf(updateDeckPayload);
            DeckRequestDTO = mapper.readValue(payload, DeckRequestDTO.class);

            String DeckId = DeckControllerResponseDTO.getDecks()[0].getId();
            DeckUpdateDTO = updateDeck(headerNormal, payload, DeckId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(DeckUpdateDTO.getTitle(),"Falcon API Automation - updated","User IDs do not match error message validation");
            Assert.assertEquals(DeckUpdateDTO.getChapterId(),"edited","User IDs do not match error message validation");
            Assert.assertEquals(DeckUpdateDTO.getBookId(),"aaa","User IDs do not match error message validation");
            Assert.assertEquals(DeckUpdateDTO.getSectionId(),"edited","User IDs do not match error message validation");
            Assert.assertEquals(DeckUpdateDTO.getProductId(),"123","User IDs do not match error message validation");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    //Verify whether the deck can be updated with a different chapter ID belonging to the same BookId
    @Test(priority =31) @AlmAnnotation(almTestId = "583202")
    public void UpdateDeckWithUsingChapterId_BookId() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            JSONObject updateDeckPayload = jsonReader.getJsonObject(DeckUpdatePayload);
            System.out.println(updateDeckPayload);
            updateDeckPayload.put("productId",null);
            updateDeckPayload.put("bookId","aaa");
            updateDeckPayload.put("chapterId","edited");
            String payload = String.valueOf(updateDeckPayload);
            DeckRequestDTO = mapper.readValue(payload, DeckRequestDTO.class);

            String DeckId = DeckControllerResponseDTO.getDecks()[0].getId();
            DeckUpdateDTO = updateDeck(headerNormal, payload, DeckId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(DeckUpdateDTO.getTitle(),"Falcon API Automation - updated","User IDs do not match error message validation");
            Assert.assertEquals(DeckUpdateDTO.getChapterId(),"edited","User IDs do not match error message validation");
            Assert.assertEquals(DeckUpdateDTO.getBookId(),"aaa","User IDs do not match error message validation");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =32) @AlmAnnotation(almTestId = "611101")
    public void UpdateDeckWithUsingSectionId_ChapterId_BookId() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            JSONObject updateDeckPayload = jsonReader.getJsonObject(DeckUpdatePayload);
            System.out.println(updateDeckPayload);
            updateDeckPayload.put("productId",null);
            updateDeckPayload.put("bookId","aaa");
            updateDeckPayload.put("chapterId","edited");
            updateDeckPayload.put("sectionId","edited");
            String payload = String.valueOf(updateDeckPayload);
            DeckRequestDTO = mapper.readValue(payload, DeckRequestDTO.class);

            String DeckId = DeckControllerResponseDTO.getDecks()[0].getId();
            DeckUpdateDTO = updateDeck(headerNormal, payload, DeckId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(DeckUpdateDTO.getTitle(),"Falcon API Automation - updated","User IDs do not match error message validation");
            Assert.assertEquals(DeckUpdateDTO.getChapterId(),"edited","User IDs do not match error message validation");
            Assert.assertEquals(DeckUpdateDTO.getBookId(),"aaa","User IDs do not match error message validation");
            Assert.assertEquals(DeckUpdateDTO.getSectionId(),"edited","User IDs do not match error message validation");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =33) @AlmAnnotation(almTestId = "611101")
    public void UpdateDeckWithUsingSectionId_ChapterId_BookId_bookOrProductId() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            JSONObject updateDeckPayload = jsonReader.getJsonObject(DeckUpdatePayload);
            System.out.println(updateDeckPayload);
            updateDeckPayload.put("productId",null);
            updateDeckPayload.put("bookId","aaa");
            updateDeckPayload.put("bookOrProductId","aaaa");
            updateDeckPayload.put("chapterId","edited");
            updateDeckPayload.put("sectionId","edited");
            String payload = String.valueOf(updateDeckPayload);
            DeckRequestDTO = mapper.readValue(payload, DeckRequestDTO.class);

            String DeckId = DeckControllerResponseDTO.getDecks()[0].getId();
            DeckUpdateDTO = updateDeck(headerNormal, payload, DeckId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(DeckUpdateDTO.getTitle(),"Falcon API Automation - updated","User IDs do not match error message validation");
            Assert.assertEquals(DeckUpdateDTO.getChapterId(),"edited","User IDs do not match error message validation");
            Assert.assertEquals(DeckUpdateDTO.getBookId(),"aaa","User IDs do not match error message validation");
            Assert.assertEquals(DeckUpdateDTO.getSectionId(),"edited","User IDs do not match error message validation");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =34) @AlmAnnotation(almTestId = "611101")
    public void UpdateDeckWithUsingChapterId_BookId_bookOrProductId() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            JSONObject updateDeckPayload = jsonReader.getJsonObject(DeckUpdatePayload);
            System.out.println(updateDeckPayload);
            updateDeckPayload.put("productId",null);
            updateDeckPayload.put("bookId","aaa");
            updateDeckPayload.put("bookOrProductId","aaaa");
            updateDeckPayload.put("chapterId","edited");
            String payload = String.valueOf(updateDeckPayload);
            DeckRequestDTO = mapper.readValue(payload, DeckRequestDTO.class);

            String DeckId = DeckControllerResponseDTO.getDecks()[0].getId();
            DeckUpdateDTO = updateDeck(headerNormal, payload, DeckId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(DeckUpdateDTO.getTitle(),"Falcon API Automation - updated","User IDs do not match error message validation");
            Assert.assertEquals(DeckUpdateDTO.getChapterId(),"edited","User IDs do not match error message validation");
            Assert.assertEquals(DeckUpdateDTO.getBookId(),"aaa","User IDs do not match error message validation");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =35) @AlmAnnotation(almTestId = "611101")
    public void UpdateDeckWithUsingChapterId_BookOrProductId() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            JSONObject updateDeckPayload = jsonReader.getJsonObject(DeckUpdatePayload);
            System.out.println(updateDeckPayload);
            updateDeckPayload.put("productId",null);
            updateDeckPayload.put("bookOrProductId","aaaa");
            updateDeckPayload.put("chapterId","edited");
            String payload = String.valueOf(updateDeckPayload);
            DeckRequestDTO = mapper.readValue(payload, DeckRequestDTO.class);

            String DeckId = DeckControllerResponseDTO.getDecks()[0].getId();
            DeckUpdateDTO = updateDeck(headerNormal, payload, DeckId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(DeckUpdateDTO.getTitle(),"Falcon API Automation - updated","User IDs do not match error message validation");
            Assert.assertEquals(DeckUpdateDTO.getChapterId(),"edited","User IDs do not match error message validation");
            Assert.assertEquals(DeckUpdateDTO.getBookId(),"aaaa","User IDs do not match error message validation");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =36) @AlmAnnotation(almTestId = "611101")
    public void UpdateDeckWithUsingSectionId_ChapterId_BookOrProductId() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            JSONObject updateDeckPayload = jsonReader.getJsonObject(DeckUpdatePayload);
            System.out.println(updateDeckPayload);
            updateDeckPayload.put("productId",null);
            updateDeckPayload.put("bookOrProductId","aaaa");
            updateDeckPayload.put("chapterId","edited");
            updateDeckPayload.put("sectionId","edited");
            String payload = String.valueOf(updateDeckPayload);
            DeckRequestDTO = mapper.readValue(payload, DeckRequestDTO.class);

            String DeckId = DeckControllerResponseDTO.getDecks()[0].getId();
            DeckUpdateDTO = updateDeck(headerNormal, payload, DeckId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(DeckUpdateDTO.getTitle(),"Falcon API Automation - updated","User IDs do not match error message validation");
            Assert.assertEquals(DeckUpdateDTO.getChapterId(),"edited","User IDs do not match error message validation");
            Assert.assertEquals(DeckUpdateDTO.getBookId(),"aaaa","User IDs do not match error message validation");
            Assert.assertEquals(DeckUpdateDTO.getSectionId(),"edited","User IDs do not match error message validation");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    //Verify when the chapter ID is available whether the bookId is also should be available
    @Test(priority =37) @AlmAnnotation(almTestId = "583200")
    public void UpdateDeckWithUsingOnlyChapterId() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            JSONObject updateDeckPayload = jsonReader.getJsonObject(DeckUpdatePayload);
            System.out.println(updateDeckPayload);
            updateDeckPayload.put("productId",null);
            updateDeckPayload.put("chapterId","edited");
            String payload = String.valueOf(updateDeckPayload);
            DeckRequestDTO = mapper.readValue(payload, DeckRequestDTO.class);

            String DeckId = DeckControllerResponseDTO.getDecks()[0].getId();
            DeckUpdateDTO = updateDeck(headerNormal, payload, DeckId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            if (DeckUpdateDTO.getObjectErrors()[0].getMessage().equals("'bookId', 'productId' or 'bookOrProductId' must be provided")) {
                Assert.assertEquals(DeckUpdateDTO.getObjectErrors()[0].getMessage(), "'bookId', 'productId' or 'bookOrProductId' must be provided", "User IDs do not match error message validation");
                Assert.assertEquals(DeckUpdateDTO.getObjectErrors()[1].getMessage(), "Book hierarchy is not valid", "User IDs do not match error message validation");
            }
            else{
                Assert.assertEquals(DeckUpdateDTO.getObjectErrors()[1].getMessage(), "'bookId', 'productId' or 'bookOrProductId' must be provided", "User IDs do not match error message validation");
                Assert.assertEquals(DeckUpdateDTO.getObjectErrors()[0 ].getMessage(), "Book hierarchy is not valid", "User IDs do not match error message validation");
            }
            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    //Verify when the sectionId is available both bookId and chapterId are also available
    @Test(priority =38) @AlmAnnotation(almTestId = "583201")
    public void UpdateDeckWithUsingOnlyChapterId_sectionId() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            JSONObject updateDeckPayload = jsonReader.getJsonObject(DeckUpdatePayload);
            System.out.println(updateDeckPayload);
            updateDeckPayload.put("productId",null);
            updateDeckPayload.put("chapterId","edited");
            updateDeckPayload.put("sectionId","edited");
            String payload = String.valueOf(updateDeckPayload);
            DeckRequestDTO = mapper.readValue(payload, DeckRequestDTO.class);

            String DeckId = DeckControllerResponseDTO.getDecks()[0].getId();
            DeckUpdateDTO = updateDeck(headerNormal, payload, DeckId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            if (DeckUpdateDTO.getObjectErrors()[0].getMessage().equals("'bookId', 'productId' or 'bookOrProductId' must be provided")) {
                Assert.assertEquals(DeckUpdateDTO.getObjectErrors()[0].getMessage(), "'bookId', 'productId' or 'bookOrProductId' must be provided", "User IDs do not match error message validation");
                Assert.assertEquals(DeckUpdateDTO.getObjectErrors()[1].getMessage(), "Book hierarchy is not valid", "User IDs do not match error message validation");
            }
            else{
                Assert.assertEquals(DeckUpdateDTO.getObjectErrors()[1].getMessage(), "'bookId', 'productId' or 'bookOrProductId' must be provided", "User IDs do not match error message validation");
                Assert.assertEquals(DeckUpdateDTO.getObjectErrors()[0].getMessage(), "Book hierarchy is not valid", "User IDs do not match error message validation");
            }
            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }
}
