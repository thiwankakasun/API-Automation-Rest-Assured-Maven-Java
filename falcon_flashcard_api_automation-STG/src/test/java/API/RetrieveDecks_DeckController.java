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
import responseDTO.*;
import utils.JsonReader;
import utils.PiTokenGenarator;
import utils.PiTokenGenaratorNormalUser;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.logging.Logger;

public class RetrieveDecks_DeckController extends ServiceController {

    private DeckControllerResponseDTO DeckControllerResponseDTO;
    private DeckControllerRequestDTO DeckControllerRequestDTO;
    private StoreControllerResponseDTO StoreControllerResponseDTO;
    private responseDTO.QuestionControllerResponseDTO QuestionControllerResponseDTO;
    private DeckResponseDTO[] DeckResponseDTO;
    private DeckResponseDTO DeckUpdateDTO;
    private QuestionDTO QuestionDTO;
    private responseDTO.GetRecommendationResponseDTO GetRecommendationResponseDTO;
    private PostRecommendedActivitiesResponseDTO PostRecommendedActivitiesResponseDTO;
    private DeckRequestDTO DeckRequestDTO;
    private StoreController StoreController = new StoreController();

    private static final PiTokenGenarator PiTokenGenarator = new PiTokenGenarator();
    private static final PiTokenGenaratorNormalUser PiTokenGenaratorNormalUser = new PiTokenGenaratorNormalUser();
    private static final HashMap<String, String> headerNormal = new HashMap<>();
    private static final HashMap<String, String> header = new HashMap<>();
    private static final HashMap<String, String> queryparam = new HashMap<>();
    JsonReader jsonReader = new JsonReader();

    private static final Logger LOGGER = Logger.getLogger(String.valueOf(RetrieveDecks_DeckController.class));

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
            String DeckId = DeckControllerResponseDTO.getDecks()[0].getId();
            DeckControllerResponseDTO = deleteDeck(headerNormal, DeckId);

            Assert.assertEquals(StatusCode, 204, "Status Code");

        } catch (Exception e) {
            throw e;
        }
    }

    //Verify that the number of cards in the deck is correct
    @Test(priority =8) @AlmAnnotation(almTestId = "584120")
    public void GetDeckWithValidatingCardCount() throws Exception {
        try {
            CreateSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            String UserId = DeckControllerResponseDTO.getDecks()[0].getUserId();
//            if (Env.equals("PROD")){
//                UserId = "ffffffff5bc5a665e4b0868373897e34";
//            }
//            else{
//                UserId = "0f1d72fbc8a4412ba1ac8b8970eeffd5";
//            }
            LOGGER.info("UserId: " + UserId);
            queryparam.put("productId",DeckControllerResponseDTO.getDecks()[0].getProductId());
            DeckResponseDTO = getDeck(headerNormal, queryparam, UserId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(DeckResponseDTO[0].getId(),DeckControllerResponseDTO.getDecks()[0].getId(),"Deck Id");
            Assert.assertEquals(DeckResponseDTO[0].getNoOfCards(),1,"noOfCards");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    //Verify that the progress proficiency  is correct.
    @Test(priority =9) @AlmAnnotation(almTestId = "584121")
    public void GetDeckWithValidatingIncludeProgress() throws Exception {
        try {
            CreateSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            GetRecommendedCards();
            PostRecommendedActivities();
            String UserId = DeckControllerResponseDTO.getDecks()[0].getUserId();
//            if (Env.equals("PROD")){
//                UserId = "ffffffff5bc5a665e4b0868373897e34";
//            }
//            else{
//                UserId = "0f1d72fbc8a4412ba1ac8b8970eeffd5";
//            }
            LOGGER.info("UserId: " + UserId);
            queryparam.put("productId",DeckControllerResponseDTO.getDecks()[0].getProductId());
            queryparam.put("includeProgress","true");
            DeckResponseDTO = getDeck(headerNormal, queryparam, UserId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(DeckResponseDTO[0].getId(),DeckControllerResponseDTO.getDecks()[0].getId(),"Deck Id");
            Assert.assertEquals(DeckResponseDTO[0].getNoOfCards(),1,"noOfCards");
            Assert.assertEquals(DeckResponseDTO[0].getDeckActivity().getLearnedCards(),0,"noOfCards");
            Assert.assertEquals(DeckResponseDTO[0].getDeckActivity().getSeenCards(),1,"noOfCards");
            Assert.assertEquals(DeckResponseDTO[0].getDeckActivity().getUnseenCards(),0,"noOfCards");
            Assert.assertEquals(DeckResponseDTO[0].getDeckActivity().getFavoriteCards(),0,"noOfCards");
            Assert.assertEquals(DeckResponseDTO[0].getDeckActivity().getCorrectCards(),1,"noOfCards");
            Assert.assertEquals(DeckResponseDTO[0].getDeckActivity().getTotalCards(),1,"noOfCards");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =10) @AlmAnnotation(almTestId = "609180")
    public void GetDeckWithValidatingIncludeProgressandIncludeRecentProgress() throws Exception {
        try {
            CreateSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            GetRecommendedCards();
            PostRecommendedActivities();
            String UserId = DeckControllerResponseDTO.getDecks()[0].getUserId();
//            if (Env.equals("PROD")){
//                UserId = "ffffffff5bc5a665e4b0868373897e34";
//            }
//            else{
//                UserId = "0f1d72fbc8a4412ba1ac8b8970eeffd5";
//            }
            LOGGER.info("UserId: " + UserId);
            queryparam.put("productId",DeckControllerResponseDTO.getDecks()[0].getProductId());
            queryparam.put("includeProgress","true");
            queryparam.put("includeRecentProgress","true");
            DeckResponseDTO = getDeck(headerNormal, queryparam, UserId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(DeckResponseDTO[0].getId(),DeckControllerResponseDTO.getDecks()[0].getId(),"Deck Id");
            Assert.assertEquals(DeckResponseDTO[0].getNoOfCards(),1,"noOfCards");
            Assert.assertEquals(DeckResponseDTO[0].getDeckActivity().getLearnedCards(),0,"noOfCards");
            Assert.assertEquals(DeckResponseDTO[0].getDeckActivity().getSeenCards(),1,"noOfCards");
            Assert.assertEquals(DeckResponseDTO[0].getDeckActivity().getUnseenCards(),0,"noOfCards");
            Assert.assertEquals(DeckResponseDTO[0].getDeckActivity().getFavoriteCards(),0,"noOfCards");
            Assert.assertEquals(DeckResponseDTO[0].getDeckActivity().getCorrectCards(),1,"noOfCards");
            Assert.assertEquals(DeckResponseDTO[0].getDeckActivity().getTotalCards(),1,"noOfCards");
            Assert.assertEquals(DeckResponseDTO[0].getDeckActivity().getRecentActivity().getCorrectCards(),1,"noOfCards");
            Assert.assertEquals(DeckResponseDTO[0].getDeckActivity().getRecentActivity().getTotalCards(),1,"noOfCards");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    //Verify the content of the response body.
    @Test(priority =11) @AlmAnnotation(almTestId = "584119")
    public void GetDeckWithValidatingResponseBody() throws Exception {
        try {
            //Thread.sleep(10000);
            getData();
            CreateSingleDeckWithFullPayload();
            String UserId = DeckControllerResponseDTO.getDecks()[0].getUserId();
//            if (Env.equals("PROD")){
//                UserId = "ffffffff5bc5a665e4b0868373897e34";
//            }
//            else{
//                UserId = "0f1d72fbc8a4412ba1ac8b8970eeffd5";
//            }
            LOGGER.info("UserId: " + UserId);
            queryparam.put("productId",DeckControllerResponseDTO.getDecks()[0].getProductId());
            queryparam.put("includeProgress","false");
            queryparam.put("includeRecentProgress","false");
            DeckResponseDTO = getDeck(headerNormal, queryparam, UserId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(DeckResponseDTO[0].getId(),DeckControllerResponseDTO.getDecks()[0].getId(),"Deck Id");
            Assert.assertEquals(DeckResponseDTO[0].getTitle(),"Falcon API Automation","Title");
//            if(Env.equals("PROD"))
//                Assert.assertEquals(DeckResponseDTO[0].getUserId(), "ffffffff5bc5a665e4b0868373897e34", "Creator ID");
//            else
            Assert.assertEquals(DeckResponseDTO[0].getUserId(), DeckControllerResponseDTO.getDecks()[0].getUserId(), "Creator ID");
            Assert.assertEquals(DeckResponseDTO[0].getBookId(),DeckControllerResponseDTO.getDecks()[0].getBookId(),"Book Id");
            Assert.assertEquals(DeckResponseDTO[0].getChapterId(),DeckControllerResponseDTO.getDecks()[0].getChapterId(),"chapterId");
            Assert.assertEquals(DeckResponseDTO[0].getSectionId(),DeckControllerResponseDTO.getDecks()[0].getSectionId(),"sectionId");
            Assert.assertEquals(DeckResponseDTO[0].getNoOfCards(),DeckControllerResponseDTO.getDecks()[0].getNoOfCards(),"noOfCards");
            Assert.assertEquals(DeckResponseDTO[0].getCreatedAt(),DeckControllerResponseDTO.getDecks()[0].getCreatedAt(),"createdAt");
            Assert.assertEquals(DeckResponseDTO[0].getUpdatedAt(),DeckControllerResponseDTO.getDecks()[0].getUpdatedAt(),"updatedAt");
            Assert.assertEquals(DeckResponseDTO[0].getUserCreatedAt(),DeckControllerResponseDTO.getDecks()[0].getUserCreatedAt(),"updatedAt");
            Assert.assertEquals(DeckResponseDTO[0].getDeckType(),DeckControllerResponseDTO.getDecks()[0].getDeckType(),"deckType");
            Assert.assertFalse(DeckResponseDTO[0].isArchived(),"archived");
            Assert.assertFalse(DeckResponseDTO[0].isDeleted(),"deleted");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    //Verify that an error displays for invalid tenant id.
    @Test(priority =12) @AlmAnnotation(almTestId = "584111")
    public void GetDeckUsingInvalidTenantId() throws Exception {
        try {
            headerNormal.put("X-TenantId", "a");
            String UserId = "0f1d72fbc8a4412ba1ac8b8970eeffd5";
            LOGGER.info("UserId: " + UserId);
            queryparam.put("productId","30b18904-dcc1-4f0f-bf0c-5cc91fa98e8b");
            DeckControllerResponseDTO = getDeckValidate(headerNormal, queryparam, UserId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(DeckControllerResponseDTO.getDescription(), "Field 'tenantId' does not have a valid value", "Error message");
        } catch (Exception e) {
            throw e;
        }
    }

    //Verify that an error displays for missing tenant id.
    @Test(priority =13) @AlmAnnotation(almTestId = "584110")
    public void GetDeckUsingMissingTenantId() throws Exception {
        try {
            getData();
            headerNormal.put("X-TenantId", "");
            String UserId = "0f1d72fbc8a4412ba1ac8b8970eeffd5";
            LOGGER.info("UserId: " + UserId);
            queryparam.put("productId","30b18904-dcc1-4f0f-bf0c-5cc91fa98e8b");
            DeckControllerResponseDTO = getDeckValidate(headerNormal, queryparam, UserId);

            Assert.assertEquals(StatusCode, 403, "Status Code");
            Assert.assertEquals(DeckControllerResponseDTO.getDescription(), "You do not have permission to access this method without a valid tenant ID", "Error message");
        } catch (Exception e) {
            throw e;
        }
    }

    //Verify that an error displays if request is sent with an invalid user id.
    @Test(priority =14) @AlmAnnotation(almTestId = "584114")
    public void GetDeckUsingInvalidUserId() throws Exception {
        try {
            getData();
            String UserId = "fffffff5af55f14e4b07b5c2f171f63";
            LOGGER.info("UserId: " + UserId);
            queryparam.put("productId","30b18904-dcc1-4f0f-bf0c-5cc91fa98e8b");
            DeckControllerResponseDTO = getDeckValidate(headerNormal, queryparam, UserId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(DeckControllerResponseDTO.getDescription(), "User IDs do not match", "Error message");
        } catch (Exception e) {
            throw e;
        }
    }

    //Verify that an error displays if request is sent without the user id.
    @Test(priority =15) @AlmAnnotation(almTestId = "584113")
    public void GetDeckUsingMissingUserId() throws Exception {
        try {
            String UserId = "";
            LOGGER.info("UserId: " + UserId);
            queryparam.put("productId","30b18904-dcc1-4f0f-bf0c-5cc91fa98e8b");
            DeckControllerResponseDTO = getDeckValidate(headerNormal, queryparam, UserId);

            Assert.assertEquals(StatusCode, 405, "Status Code");
            Assert.assertEquals(DeckControllerResponseDTO.getError(), "Method Not Allowed", "Error message");
        } catch (Exception e) {
            throw e;
        }
    }

    //Verify that request cannot send with only the user id.
    @Test(priority =16) @AlmAnnotation(almTestId = "584118")
    public void GetDeckWithoutUsingProductIdBookId() throws Exception {
        try {
            getData();
            String UserId= null;
            if (Env.equals("PROD")){
                UserId = "bc4e17dc1478465a89af0f84ec5df597";
            }
            else{
                UserId = "0f1d72fbc8a4412ba1ac8b8970eeffd5";
            }
            LOGGER.info("UserId: " + UserId);
            queryparam.put("productId","");
            DeckControllerResponseDTO = getDeckValidate(headerNormal, queryparam, UserId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(DeckControllerResponseDTO.getDescription(), "'bookId', 'productId' or 'bookOrProductId' must be provided.", "Error message");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =17) @AlmAnnotation(almTestId = "609180")
    public void GetExpertDeckUsingBookIdSectionIdandChapterId() throws Exception {
        try {
            getData();
            String UserId = null;
            if (Env.equals("PROD")){
                UserId = "bc4e17dc1478465a89af0f84ec5df597";
            }
            else{
                UserId = "0f1d72fbc8a4412ba1ac8b8970eeffd5";
            }
            LOGGER.info("UserId: " + UserId);
            queryparam.put("productId","");
            DeckControllerResponseDTO = getDeckValidate(headerNormal, queryparam, UserId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(DeckControllerResponseDTO.getDescription(), "'bookId', 'productId' or 'bookOrProductId' must be provided.", "Error message");
        } catch (Exception e) {
            throw e;
        }
    }

    /* SFC-16559  Changes to get decks via Book or Product ID */

    //Verify whether that user can send the request to retrieve decks by adding multiple values to bookOrProductIds
    @Test(priority = 18) @AlmAnnotation(almTestId = "609180")
    public void GetDeckUsingMultipleValues_for_bookorProductIds() throws Exception {
        try {
            JSONObject createDeckPayload = jsonReader.getJsonObject(DeckCreatePayload);

            JSONObject tenantKeyField = new JSONObject();
            tenantKeyField.put("productId","30b18904-dcc1-4f0f-bf0c-5cc91fa98e8b");
            tenantKeyField.put("bookId","ABYZ1RY3L9G");
            tenantKeyField.put("chapterId","123");
            tenantKeyField.put("sectionId","123");
            tenantKeyField.put("title","aaa");

            JSONObject tenantKeyField1 = new JSONObject();
            tenantKeyField1.put("productId","ed887a47-862d-4a79-aff7-539091203aa5");
            tenantKeyField1.put("bookId","EA67MKTA2LV");
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

            String UserId = DeckControllerResponseDTO.getDecks()[0].getUserId();
            LOGGER.info("DeckId: " + UserId);
            String bookOrProductIds= (DeckControllerResponseDTO.getDecks()[0].getProductId()+","+DeckControllerResponseDTO.getDecks()[1].getProductId());
            queryparam.put("bookOrProductIds",bookOrProductIds);
            DeckResponseDTO = getDeck(headerNormal, queryparam, UserId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(DeckResponseDTO[0].getId(),DeckControllerResponseDTO.getDecks()[1].getId());
            Assert.assertEquals(DeckResponseDTO[1].getId(),DeckControllerResponseDTO.getDecks()[0].getId());
            Assert.assertEquals(DeckResponseDTO[0].getProductId(), DeckControllerResponseDTO.getDecks()[1].getProductId());
            Assert.assertEquals(DeckResponseDTO[1].getProductId(), DeckControllerResponseDTO.getDecks()[0].getProductId());

            DeleteDeckWithFullPayload();

        } catch (Exception e) {
            throw e;
        }
    }


    //Verify whether that user can successfully retrieve decks that contain bookId and productId by sending the request with bookOrProductIds
    @Test(priority = 19) @AlmAnnotation(almTestId = "609180")
    public void GetDeckUsingBookId_and_ProductId_for_bookorProductIds() throws Exception {
        try {
            JSONObject createDeckPayload = jsonReader.getJsonObject(DeckCreatePayload);

            JSONObject tenantKeyField = new JSONObject();
            tenantKeyField.put("productId","30b18904-dcc1-4f0f-bf0c-5cc91fa98e8b");
            tenantKeyField.put("bookId","aaa");
            tenantKeyField.put("chapterId","123");
            tenantKeyField.put("sectionId","123");
            tenantKeyField.put("title","aaa");

            JSONObject tenantKeyField1 = new JSONObject();
            tenantKeyField1.put("productId","aaa");
            tenantKeyField1.put("bookId","EA67MKTA2LV");
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

            String UserId = DeckControllerResponseDTO.getDecks()[0].getUserId();
            LOGGER.info("DeckId: " + UserId);
            String bookOrProductIds= (DeckControllerResponseDTO.getDecks()[0].getProductId()+","+DeckControllerResponseDTO.getDecks()[1].getBookId());
            queryparam.put("bookOrProductIds",bookOrProductIds);
            DeckResponseDTO = getDeck(headerNormal, queryparam, UserId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(DeckResponseDTO[0].getId(),DeckControllerResponseDTO.getDecks()[1].getId());
            Assert.assertEquals(DeckResponseDTO[1].getId(),DeckControllerResponseDTO.getDecks()[0].getId());
            Assert.assertEquals(DeckResponseDTO[0].getBookId(), DeckControllerResponseDTO.getDecks()[1].getBookId());
            Assert.assertEquals(DeckResponseDTO[1].getProductId(), DeckControllerResponseDTO.getDecks()[0].getProductId());

            DeleteDeckWithFullPayload();

        } catch (Exception e) {
            throw e;
        }
    }

    //Verify whether that user can successfully retrieve decks that contain only productId by sending the request with bookOrProductIds
    @Test(priority = 20) @AlmAnnotation(almTestId = "609180")
    public void GetDeckUsingProductId_for_bookorProductIds() throws Exception {
        try {
            CreateSingleDeckWithFullPayload();
            String UserId = DeckControllerResponseDTO.getDecks()[0].getUserId();
            LOGGER.info("DeckId: " + UserId);
            String bookOrProductIds= (DeckControllerResponseDTO.getDecks()[0].getProductId());
            queryparam.put("bookOrProductIds",bookOrProductIds);
            DeckResponseDTO = getDeck(headerNormal, queryparam, UserId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(DeckResponseDTO[0].getId(),DeckControllerResponseDTO.getDecks()[0].getId());
            Assert.assertEquals(DeckResponseDTO[0].getProductId(), bookOrProductIds);

            DeleteDeckWithFullPayload();

        } catch (Exception e) {
            throw e;
        }
    }

    //Verify whether that user can successfully retrieve decks that contain only bookId by sending the request with bookOrProductIds
    @Test(priority = 21) @AlmAnnotation(almTestId = "609180")
    public void GetDeckUsingBookId_for_bookorProductIds() throws Exception {
        try {
            JSONObject createDeckPayload = jsonReader.getJsonObject(DeckCreatePayload);

            JSONObject tenantKeyField = new JSONObject();
            //tenantKeyField.put("productId","30b18904-dcc1-4f0f-bf0c-5cc91fa98e8b");
            tenantKeyField.put("bookId","9cffba6b-7ca1-4f0f-b559-7a359f787df3");
            tenantKeyField.put("chapterId","123");
            tenantKeyField.put("sectionId","123");
            tenantKeyField.put("title","aaa");

            JSONArray tenantKey = new JSONArray();
            tenantKey.add(tenantKeyField);
            createDeckPayload.put("decks",tenantKey);
            System.out.println(createDeckPayload);
            String payload = String.valueOf(createDeckPayload);
            DeckControllerRequestDTO = mapper.readValue(payload, DeckControllerRequestDTO.class);
            DeckControllerResponseDTO = createDeck(headerNormal,payload);

            Assert.assertEquals(StatusCode, 201, "Status Code");

            String UserId = DeckControllerResponseDTO.getDecks()[0].getUserId();
            LOGGER.info("DeckId: " + UserId);
            String bookOrProductIds= (DeckControllerResponseDTO.getDecks()[0].getBookId());
            queryparam.put("bookOrProductIds",bookOrProductIds);
            DeckResponseDTO = getDeck(headerNormal, queryparam, UserId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(DeckResponseDTO[0].getId(),DeckControllerResponseDTO.getDecks()[0].getId());
            Assert.assertEquals(DeckResponseDTO[0].getBookId(), bookOrProductIds);

            DeleteDeckWithFullPayload();

        } catch (Exception e) {
            throw e;
        }
    }

    //Verify whether that user can successfully retrieve PURCHASED decks by sending the request with bookOrProductIds
    //@Test(priority = 22) @AlmAnnotation(almTestId = "609180")
    public void GetDeckUsing_bookorProductIds_with_purchased_decks() throws Exception {
        try {
            CreateSingleDeckWithFullPayload();
            String UserId = DeckControllerResponseDTO.getDecks()[0].getUserId();
            LOGGER.info("DeckId: " + UserId);
            String bookOrProductIds= (DeckControllerResponseDTO.getDecks()[0].getProductId());
            queryparam.put("bookOrProductIds",bookOrProductIds);
            queryparam.put("deckType","PURCHASED");
            DeckResponseDTO = getDeck(headerNormal, queryparam, UserId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertNotEquals(DeckResponseDTO[0].getId(), DeckControllerResponseDTO.getDecks()[0].getId());
            if(DeckResponseDTO[0].getProductId()==null)
                Assert.assertEquals(DeckResponseDTO[0].getBookId(), bookOrProductIds);
            else
                Assert.assertEquals(DeckResponseDTO[0].getProductId(), bookOrProductIds);
            Assert.assertEquals(DeckResponseDTO[0].getDeckType(),"PURCHASED");

            DeleteDeckWithFullPayload();

        } catch (Exception e) {
            throw e;
        }
    }

    //Verify whether that user can successfully retrieve USER decks by sending the request with bookOrProductIds
    @Test(priority = 23) @AlmAnnotation(almTestId = "609180")
    public void GetDeckUsing_bookorProductIds_with_user_decks() throws Exception {
        try {
            StoreController.getData();
            StoreController.CreateExpertDeck();
            String UserId=null;
            if (Env.equals("PROD")){
                UserId = "bc4e17dc1478465a89af0f84ec5df597";
            }
            else{
                UserId = "0f1d72fbc8a4412ba1ac8b8970eeffd5";
            }
            LOGGER.info("DeckId: " + UserId);
            String bookOrProductIds= null;
            if (Env.equals("PROD")){
                bookOrProductIds= ("02e18048-3e2d-4869-a646-5f170714dfb2");
            }
            else{
                bookOrProductIds= ("30b18904-dcc1-4f0f-bf0c-5cc91fa98e8b");
            }

            queryparam.put("bookOrProductIds",bookOrProductIds);
            queryparam.put("deckType","");
            DeckResponseDTO = getDeck(headerNormal, queryparam, UserId);
            Assert.assertEquals(StatusCode, 202, "Status Code");

            DeckResponseDTO = getDeck(headerNormal, queryparam, UserId);
            Assert.assertEquals(StatusCode, 200, "Status Code");

            Assert.assertEquals(DeckResponseDTO[0].getProductId(), bookOrProductIds);
            Assert.assertEquals(DeckResponseDTO[0].getDeckType(),"PURCHASED");
            String DeckId = DeckResponseDTO[0].getId();

            queryparam.put("deckType","USER");
            DeckResponseDTO = getDeck(headerNormal, queryparam, UserId);
            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertNotEquals(DeckResponseDTO[0].getId(),DeckId);
            Assert.assertEquals(DeckResponseDTO[0].getDeckType(),"USER");

            StoreController.DeleteExpertDeck();

        } catch (Exception e) {
            throw e;
        }
    }

    //Verify whether that user can not send the request to retrieve decks by adding multiple null or empty values to bookOrProductIds
    @Test(priority = 24) @AlmAnnotation(almTestId = "609180")
    public void GetDeckUsing_multiple_null_values_for_bookorProductIds() throws Exception {
        try {

            String UserId=null;
            if (Env.equals("PROD")){
                UserId = "bc4e17dc1478465a89af0f84ec5df597";
            }
            else{
                UserId = "0f1d72fbc8a4412ba1ac8b8970eeffd5";
            }
            LOGGER.info("DeckId: " + UserId);
            String bookOrProductIds= (""+","+"");
            queryparam.put("bookOrProductIds",bookOrProductIds);
            DeckControllerResponseDTO = getDeckValidate(headerNormal, queryparam, UserId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(DeckControllerResponseDTO.getFieldErrors()[0].getField(),"bookOrProductIds");
            if (DeckControllerResponseDTO.getFieldErrors()[0].getMessage().equals("Cannot contain empty values"))
                Assert.assertEquals(DeckControllerResponseDTO.getFieldErrors()[0].getMessage(), "Cannot contain empty values");

            else
                Assert.assertEquals(DeckControllerResponseDTO.getFieldErrors()[0].getMessage(), "Cannot contain duplicate values");

            //DeleteDeckWithFullPayload();

        } catch (Exception e) {
            throw e;
        }
    }

    //Verify whether that user can not send the request to retrieve decks by adding the first value as valid and the second value as null or empty to bookOrProductIds
    @Test(priority = 25) @AlmAnnotation(almTestId = "609180")
    public void GetDeckUsing_second_value_as_null_for_bookorProductIds() throws Exception {
        try {

            String UserId=null;
            if (Env.equals("PROD")){
                UserId = "bc4e17dc1478465a89af0f84ec5df597";
            }
            else{
                UserId = "0f1d72fbc8a4412ba1ac8b8970eeffd5";
            }
            LOGGER.info("DeckId: " + UserId);
            String bookOrProductIds= ("30b18904-dcc1-4f0f-bf0c-5cc91fa98e8b"+","+"");
            queryparam.put("bookOrProductIds",bookOrProductIds);
            DeckControllerResponseDTO = getDeckValidate(headerNormal, queryparam, UserId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(DeckControllerResponseDTO.getFieldErrors()[0].getField(),"bookOrProductIds");
            Assert.assertEquals(DeckControllerResponseDTO.getFieldErrors()[0].getMessage(), "Cannot contain empty values");


            //DeleteDeckWithFullPayload();

        } catch (Exception e) {
            throw e;
        }
    }

    //Verify whether that user can not send the request to retrieve decks by adding the first value as null or empty and the second value as valid to bookOrProductIds
    @Test(priority = 26) @AlmAnnotation(almTestId = "609180")
    public void GetDeckUsing_first_value_as_null_for_bookorProductIds() throws Exception {
        try {

            String UserId=null;
            if (Env.equals("PROD")){
                UserId = "bc4e17dc1478465a89af0f84ec5df597";
            }
            else{
                UserId = "0f1d72fbc8a4412ba1ac8b8970eeffd5";
            }
            LOGGER.info("DeckId: " + UserId);
            String bookOrProductIds= (""+","+"30b18904-dcc1-4f0f-bf0c-5cc91fa98e8b");
            queryparam.put("bookOrProductIds",bookOrProductIds);
            DeckControllerResponseDTO = getDeckValidate(headerNormal, queryparam, UserId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(DeckControllerResponseDTO.getFieldErrors()[0].getField(),"bookOrProductIds");
            Assert.assertEquals(DeckControllerResponseDTO.getFieldErrors()[0].getMessage(), "Cannot contain empty values");


            //DeleteDeckWithFullPayload();

        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 27) @AlmAnnotation(almTestId = "609180")
    public void GetDeckUsing_multiple_empty_values_for_bookorProductIds() throws Exception {
        try {

            String UserId=null;
            if (Env.equals("PROD")){
                UserId = "bc4e17dc1478465a89af0f84ec5df597";
            }
            else{
                UserId = "0f1d72fbc8a4412ba1ac8b8970eeffd5";
            }
            LOGGER.info("DeckId: " + UserId);
            String bookOrProductIds= (" "+","+" ");
            queryparam.put("bookOrProductIds",bookOrProductIds);
            DeckControllerResponseDTO = getDeckValidate(headerNormal, queryparam, UserId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(DeckControllerResponseDTO.getFieldErrors()[0].getField(),"bookOrProductIds");
            if (DeckControllerResponseDTO.getFieldErrors()[0].getMessage().equals("Cannot contain empty values"))
                Assert.assertEquals(DeckControllerResponseDTO.getFieldErrors()[0].getMessage(), "Cannot contain empty values");

            else
                Assert.assertEquals(DeckControllerResponseDTO.getFieldErrors()[0].getMessage(), "Cannot contain duplicate values");

            //DeleteDeckWithFullPayload();

        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 28) @AlmAnnotation(almTestId = "609180")
    public void GetDeckUsing_second_value_as_empty_for_bookorProductIds() throws Exception {
        try {

            String UserId=null;
            if (Env.equals("PROD")){
                UserId = "bc4e17dc1478465a89af0f84ec5df597";
            }
            else{
                UserId = "0f1d72fbc8a4412ba1ac8b8970eeffd5";
            }
            LOGGER.info("DeckId: " + UserId);
            String bookOrProductIds= ("30b18904-dcc1-4f0f-bf0c-5cc91fa98e8b"+","+" ");
            queryparam.put("bookOrProductIds",bookOrProductIds);
            DeckControllerResponseDTO = getDeckValidate(headerNormal, queryparam, UserId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(DeckControllerResponseDTO.getFieldErrors()[0].getField(),"bookOrProductIds");
            Assert.assertEquals(DeckControllerResponseDTO.getFieldErrors()[0].getMessage(), "Cannot contain empty values");


            //DeleteDeckWithFullPayload();

        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 29) @AlmAnnotation(almTestId = "609180")
    public void GetDeckUsing_first_value_as_empty_for_bookorProductIds() throws Exception {
        try {

            String UserId=null;
            if (Env.equals("PROD")){
                UserId = "bc4e17dc1478465a89af0f84ec5df597";
            }
            else{
                UserId = "0f1d72fbc8a4412ba1ac8b8970eeffd5";
            }
            LOGGER.info("DeckId: " + UserId);
            String bookOrProductIds= (" "+","+"30b18904-dcc1-4f0f-bf0c-5cc91fa98e8b");
            queryparam.put("bookOrProductIds",bookOrProductIds);
            DeckControllerResponseDTO = getDeckValidate(headerNormal, queryparam, UserId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(DeckControllerResponseDTO.getFieldErrors()[0].getField(),"bookOrProductIds");
            Assert.assertEquals(DeckControllerResponseDTO.getFieldErrors()[0].getMessage(), "Cannot contain empty values");


            //DeleteDeckWithFullPayload();

        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 30) @AlmAnnotation(almTestId = "609180")
    public void GetDeckUsing_duplicate_values_for_bookorProductIds() throws Exception {
        try {

            String UserId=null;
            if (Env.equals("PROD")){
                UserId = "bc4e17dc1478465a89af0f84ec5df597";
            }
            else{
                UserId = "0f1d72fbc8a4412ba1ac8b8970eeffd5";
            }
            LOGGER.info("DeckId: " + UserId);
            String bookOrProductIds= ("30b18904-dcc1-4f0f-bf0c-5cc91fa98e8b"+","+"30b18904-dcc1-4f0f-bf0c-5cc91fa98e8b");
            queryparam.put("bookOrProductIds",bookOrProductIds);
            DeckControllerResponseDTO = getDeckValidate(headerNormal, queryparam, UserId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(DeckControllerResponseDTO.getFieldErrors()[0].getField(),"bookOrProductIds");
            Assert.assertEquals(DeckControllerResponseDTO.getFieldErrors()[0].getMessage(), "Cannot contain duplicate values");


            //DeleteDeckWithFullPayload();

        } catch (Exception e) {
            throw e;
        }
    }

    //Verify whether that user can retrieve decks with including deckActivity when sending the request with bookOrProductIds and includeProgress
    @Test(priority = 31) @AlmAnnotation(almTestId = "609180")
    public void GetDeckUsing_bookorProductIds_with_includeProgress() throws Exception {
        try {
            CreateSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            GetRecommendedCards();
            PostRecommendedActivities();

            String UserId = DeckControllerResponseDTO.getDecks()[0].getUserId();
            LOGGER.info("UserId: " + UserId);
            String bookOrProductIds= (DeckControllerResponseDTO.getDecks()[0].getProductId());
            queryparam.put("bookOrProductIds",bookOrProductIds);
            queryparam.put("deckType","");
            queryparam.put("includeProgress","true");
            DeckResponseDTO = getDeck(headerNormal, queryparam, UserId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(DeckResponseDTO[0].getId(),DeckControllerResponseDTO.getDecks()[0].getId(),"Deck Id");
            Assert.assertEquals(DeckResponseDTO[0].getProductId(), bookOrProductIds);
            Assert.assertEquals(DeckResponseDTO[0].getNoOfCards(),1,"noOfCards");
            Assert.assertNotNull(DeckResponseDTO[0].getDeckActivity());
            Assert.assertEquals(DeckResponseDTO[0].getDeckActivity().getLearnedCards(),0,"noOfCards");
            Assert.assertEquals(DeckResponseDTO[0].getDeckActivity().getSeenCards(),1,"noOfCards");
            Assert.assertEquals(DeckResponseDTO[0].getDeckActivity().getUnseenCards(),0,"noOfCards");
            Assert.assertEquals(DeckResponseDTO[0].getDeckActivity().getFavoriteCards(),0,"noOfCards");
            Assert.assertEquals(DeckResponseDTO[0].getDeckActivity().getCorrectCards(),1,"noOfCards");
            Assert.assertEquals(DeckResponseDTO[0].getDeckActivity().getTotalCards(),1,"noOfCards");

            DeleteDeckWithFullPayload();

        } catch (Exception e) {
            throw e;
        }
    }

    //Verify whether that user can retrieve decks with including recentActivity when sending the request with bookOrProductIds, includeProgress and includeRecentProgress
    @Test(priority = 32) @AlmAnnotation(almTestId = "609180")
    public void GetDeckUsing_bookorProductIds_with_includeRecentProgress() throws Exception {
        try {
            CreateSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            GetRecommendedCards();
            PostRecommendedActivities();

            String UserId = DeckControllerResponseDTO.getDecks()[0].getUserId();
            LOGGER.info("UserId: " + UserId);
            String bookOrProductIds= (DeckControllerResponseDTO.getDecks()[0].getProductId());
            queryparam.put("bookOrProductIds",bookOrProductIds);
            queryparam.put("deckType","");
            queryparam.put("includeProgress","true");
            queryparam.put("includeRecentProgress","true");
            DeckResponseDTO = getDeck(headerNormal, queryparam, UserId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(DeckResponseDTO[0].getId(),DeckControllerResponseDTO.getDecks()[0].getId(),"Deck Id");
            Assert.assertEquals(DeckResponseDTO[0].getProductId(), bookOrProductIds);
            Assert.assertNotNull(DeckResponseDTO[0].getDeckActivity());
            Assert.assertEquals(DeckResponseDTO[0].getNoOfCards(),1,"noOfCards");
            Assert.assertEquals(DeckResponseDTO[0].getDeckActivity().getLearnedCards(),0,"noOfCards");
            Assert.assertEquals(DeckResponseDTO[0].getDeckActivity().getSeenCards(),1,"noOfCards");
            Assert.assertEquals(DeckResponseDTO[0].getDeckActivity().getUnseenCards(),0,"noOfCards");
            Assert.assertEquals(DeckResponseDTO[0].getDeckActivity().getFavoriteCards(),0,"noOfCards");
            Assert.assertEquals(DeckResponseDTO[0].getDeckActivity().getCorrectCards(),1,"noOfCards");
            Assert.assertEquals(DeckResponseDTO[0].getDeckActivity().getTotalCards(),1,"noOfCards");
            Assert.assertNotNull(DeckResponseDTO[0].getDeckActivity().getRecentActivity());
            Assert.assertEquals(DeckResponseDTO[0].getDeckActivity().getRecentActivity().getCorrectCards(),1,"noOfCards");
            Assert.assertEquals(DeckResponseDTO[0].getDeckActivity().getRecentActivity().getTotalCards(),1,"noOfCards");

            DeleteDeckWithFullPayload();

        } catch (Exception e) {
            throw e;
        }
    }

    //Verify whether that user can retrieve the deck details with the deleted decks when sending the request with bookOrProductIds and includeDeleted
    @Test(priority = 33) @AlmAnnotation(almTestId = "609180")
    public void GetDeckUsing_bookorProductIds_with_deleted_decks() throws Exception {
        try {
            String UserId = null;
            if (Env.equals("PROD")){
                UserId = "bc4e17dc1478465a89af0f84ec5df597";
            }
            else{
                UserId = "0f1d72fbc8a4412ba1ac8b8970eeffd5";
            }
            LOGGER.info("UserId: " + UserId);
            String bookOrProductIds= null;
            if (Env.equals("PROD")){
                bookOrProductIds= ("02e18048-3e2d-4869-a646-5f170714dfb2");
            }
            else{
                bookOrProductIds= ("c5c21fcb-d3a7-47a0-bfbc-d77109ed6508");
            }
            queryparam.put("bookOrProductIds",bookOrProductIds);
            queryparam.put("deckType","USER");
            queryparam.put("includeDeleted","true");
            queryparam.put("sort","deleted,desc");
            DeckResponseDTO = getDeck(headerNormal, queryparam, UserId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertTrue(DeckResponseDTO[0].isDeleted());

        } catch (Exception e) {
            throw e;
        }
    }

    //Verify whether that user can retrieve the deck details with the archived decks when sending the request with bookOrProductIds and includeArchived
    @Test(priority = 34) @AlmAnnotation(almTestId = "609180")
    public void GetDeckUsing_bookorProductIds_with_archived_decks() throws Exception {
        try {
            String UserId = null;
            if (Env.equals("PROD")){
                UserId = "bc4e17dc1478465a89af0f84ec5df597";
            }
            else{
                UserId = "0f1d72fbc8a4412ba1ac8b8970eeffd5";
            }
            LOGGER.info("UserId: " + UserId);
            String bookOrProductIds= null;
            if (Env.equals("PROD")){
                bookOrProductIds= ("02e18048-3e2d-4869-a646-5f170714dfb2");
            }
            else{
                bookOrProductIds= ("30b18904-dcc1-4f0f-bf0c-5cc91fa98e8b");
            }
            queryparam.put("bookOrProductIds",bookOrProductIds);
            queryparam.put("includeArchived","true");
            queryparam.put("includeDeleted","");
            queryparam.put("deckType","");
            queryparam.put("sort","archived,desc");
            DeckResponseDTO = getDeck(headerNormal, queryparam, UserId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(DeckResponseDTO[0].getProductId(), bookOrProductIds);
            Assert.assertTrue(DeckResponseDTO[0].isArchived());

        } catch (Exception e) {
            throw e;
        }
    }

    //Verify whether that user can retrieve the deck details with sorting when sending the request with bookOrProductIds and sortBy
    @Test(priority = 35) @AlmAnnotation(almTestId = "609180")
    public void GetDeckUsing_bookorProductIds_with_sortby() throws Exception {
        try {
            CreateSingleDeckWithFullPayload();
            String UserId = DeckControllerResponseDTO.getDecks()[0].getUserId();
            LOGGER.info("UserId: " + UserId);
            String bookOrProductIds= (DeckControllerResponseDTO.getDecks()[0].getProductId());
            queryparam.put("bookOrProductIds",bookOrProductIds);
            queryparam.put("includeArchived","");
            queryparam.put("includeDeleted","");
            queryparam.put("sort","createdAt,desc");
            DeckResponseDTO = getDeck(headerNormal, queryparam, UserId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(DeckResponseDTO[0].getProductId(), bookOrProductIds);
            Assert.assertEquals(DeckResponseDTO[0].getId(),DeckControllerResponseDTO.getDecks()[0].getId());

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    //Verify whether that user can provision the decks when sending the request with bookOrProductIds
    @Test(priority = 36) @AlmAnnotation(almTestId = "609180")
    public void GetDeckUsing_bookorProductIds_with_provisioning_expert_decks() throws Exception {
        try {
            StoreController.getData();
            StoreController.CreateExpertDeck();
            String UserId=null;
            if (Env.equals("PROD")){
                UserId = "bc4e17dc1478465a89af0f84ec5df597";
            }
            else{
                UserId = "0f1d72fbc8a4412ba1ac8b8970eeffd5";
            }
            LOGGER.info("DeckId: " + UserId);
            String bookOrProductIds= null;
            if (Env.equals("PROD")){
                bookOrProductIds= ("02e18048-3e2d-4869-a646-5f170714dfb2");
            }
            else{
                bookOrProductIds= ("30b18904-dcc1-4f0f-bf0c-5cc91fa98e8b");
            }
            queryparam.put("bookOrProductIds",bookOrProductIds);
            queryparam.put("includeArchived","");
            queryparam.put("includeDeleted","");
            queryparam.put("sort","");
            DeckResponseDTO = getDeck(headerNormal, queryparam, UserId);
            Assert.assertEquals(StatusCode, 202, "Status Code");

            DeckResponseDTO = getDeck(headerNormal, queryparam, UserId);
            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(DeckResponseDTO[0].getProductId(), bookOrProductIds);
            Assert.assertEquals(DeckResponseDTO[0].getDeckType(),"PURCHASED");

            StoreController.DeleteExpertDeck();

        } catch (Exception e) {
            throw e;
        }
    }

}
