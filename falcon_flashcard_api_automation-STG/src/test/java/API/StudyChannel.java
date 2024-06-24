package API;

import com.pearson.common.framework.shared.alm.AlmAnnotation;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import pojos.PiToken;
import responseDTO.*;
import utils.JsonReader;
import utils.PiTokenGenarator;
import utils.PiTokenGenaratorAuthor;
import utils.PiTokenGenaratorNormalUser;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.logging.Logger;

public class StudyChannel extends ServiceController {

    private StoreControllerResponseDTO StoreControllerResponseDTO;
    private DeckResponseDTO ProvisionedDeckDTO;
    private ExpertQuestionResponseDTO ExpertQuestionResponseDTO;
    private GetRecommendationResponseDTO GetRecommendationResponseDTO;
    private PostRecommendedActivitiesResponseDTO PostRecommendedActivitiesResponseDTO;
    private GetActivitiesOfDecksResponseDTO GetActivitiesOfDecksResponseDTO;
    private UserDTO UserDTO;
    private LearningAnalyticsController Learning = new LearningAnalyticsController();
    private StoreController StoreController = new StoreController();
    private static final PiTokenGenarator PiTokenGenarator = new PiTokenGenarator();
    private static final PiTokenGenaratorNormalUser PiTokenGenaratorNormalUser = new PiTokenGenaratorNormalUser();
    private static final PiTokenGenaratorAuthor PiTokenGenaratorAuthor = new PiTokenGenaratorAuthor();
    private static final HashMap<String, String> headerNormal = new HashMap<>();
    private static final HashMap<String, String> header = new HashMap<>();
    private static final HashMap<String, String> headerAuthor = new HashMap<>();
    private static final HashMap<String, String> queryparam = new HashMap<>();
    JsonReader jsonReader = new JsonReader();

    private static final Logger LOGGER = Logger.getLogger(String.valueOf(StudyChannel.class));


    @BeforeClass
    public void getData() throws EncryptedDocumentException, InvalidFormatException, IOException, ParseException {
        try {
            PiToken piToken = PiTokenGenarator.generatePiToken(loginUrl);
            header.put("X-Authorization", piToken.getData());
            PiToken piToken1 = PiTokenGenaratorNormalUser.generatePiToken(loginUrl);
            headerNormal.put("X-Authorization", piToken1.getData());
            PiToken piTokenAuthor = PiTokenGenaratorAuthor.generatePiToken(loginUrl);
            headerAuthor.put("X-Authorization", piTokenAuthor.getData());
            if (Env.equals("DEV")) {
                headerNormal.put("X-TenantId", "5660deb9-7b3b-44e8-8a6c-4b1fdae17308");
                headerNormal.put("X-TenantKey", "b308daa4-2ed4-40a0-ab8c-49fda99994a3");
            }

            if (Env.equals("QA")) {
                headerNormal.put("X-TenantId", "63bc8b14-cb49-466c-8d92-2e2d1358b5ff");
                headerNormal.put("X-TenantKey", "12d4b93c-0ac6-44a7-aba0-6f53d97dee52");
            }

            if (Env.equals("NFT")) {
                headerNormal.put("X-TenantId", "acf59553-f258-4d40-a46d-49a128301043");
                headerNormal.put("X-TenantKey", "3f156508-7e4f-403b-b520-aa444b1fb905");
            }

            if (Env.equals("STG")) {
                headerNormal.put("X-TenantId", "acf59553-f258-4d40-a46d-49a128301043");
                headerNormal.put("X-TenantKey", "3f156508-7e4f-403b-b520-aa444b1fb905");
            }

            if (Env.equals("PROD")) {
                headerNormal.put("X-TenantId", "f00ecc22-af26-4b56-be26-5a40b7e25d23");
                headerNormal.put("X-TenantKey", "27c1c0d2-656a-4d04-a491-06b13ac27261");
            }

        } catch (Exception e) {
            throw e;
        }
    }

//    @BeforeTest
//    public void getData() throws EncryptedDocumentException, IOException {
//        try {
//            PiToken piToken = PiTokenGenarator.generatePiToken(loginUrl);
//            header.put("X-Authorization", piToken.getData());
//            PiToken piToken1 = PiTokenGenaratorNormalUser.generatePiToken(loginUrl);
//            headerNormal.put("X-Authorization", piToken1.getData());
//
//            if (Env.equals("DEV")) {
//                headerNormal.put("X-TenantId", "5660deb9-7b3b-44e8-8a6c-4b1fdae17308");
//                headerNormal.put("X-TenantKey", "b308daa4-2ed4-40a0-ab8c-49fda99994a3");
//                header.put("X-TenantId", "5660deb9-7b3b-44e8-8a6c-4b1fdae17308");
//                header.put("X-TenantKey", "b308daa4-2ed4-40a0-ab8c-49fda99994a3");
//            }
//
//            if (Env.equals("QA")) {
//                headerNormal.put("X-TenantId", "2b4af3fc-97b4-41a3-af7e-bc9354d35fed");
//                headerNormal.put("X-TenantKey", "152be39d-8699-4ae9-9bae-91a50c28b6d0");
//                header.put("X-TenantId", "2b4af3fc-97b4-41a3-af7e-bc9354d35fed");
//                header.put("X-TenantKey", "152be39d-8699-4ae9-9bae-91a50c28b6d0");
//            }
//
//            if (Env.equals("NFT")) {
//                headerNormal.put("X-TenantId", "beeea450-02f6-4f88-842e-cec9364453b7");
//                headerNormal.put("X-TenantKey", "21d77376-46b2-4e26-8781-2d1a59468f8f");
//                header.put("X-TenantId", "beeea450-02f6-4f88-842e-cec9364453b7");
//                header.put("X-TenantKey", "21d77376-46b2-4e26-8781-2d1a59468f8f");
//            }
//
//            if (Env.equals("STG")) {
//                headerNormal.put("X-TenantId", "30258aed-46bb-4ec5-909e-ecea2431772b");
//                headerNormal.put("X-TenantKey", "297a5917-24fa-43fe-a27b-426126bd7cf4");
//                header.put("X-TenantId", "30258aed-46bb-4ec5-909e-ecea2431772b");
//                header.put("X-TenantKey", "297a5917-24fa-43fe-a27b-426126bd7cf4");
//            }
//
//            if (Env.equals("PROD")) {
//                headerNormal.put("X-TenantId", "f00ecc22-af26-4b56-be26-5a40b7e25d23");
//                headerNormal.put("X-TenantKey", "27c1c0d2-656a-4d04-a491-06b13ac27261");
//                header.put("X-TenantId", "f00ecc22-af26-4b56-be26-5a40b7e25d23");
//                header.put("X-TenantKey", "27c1c0d2-656a-4d04-a491-06b13ac27261");
//            }
//        }
//        catch (Exception e){
//            throw  e;
//        }
//
//    }

//    @BeforeClass
//    public void getData() throws Exception {
//            JSONObject GeneratePITokenPayload = jsonReader.getJsonObject(PITokenGeneratePayload);
//            System.out.println(GeneratePITokenPayload);
//
//            String payload = String.valueOf(GeneratePITokenPayload);
//            //storeControllerRequestDTO = mapper.readValue(payload, StoreControllerRequestDTO.class);
//            UserDTO = generatePIToken(payload);
//
//            Assert.assertEquals(StatusCode, 201, "Status Code");
//            LOGGER.info("PIToken"+UserDTO.getData());
//            headerAuthor.put("X-Authorization", UserDTO.getData());
//    }

    @Test(priority = 1) @AlmAnnotation(almTestId = "xxxxxxxx")
    public void CreateExpertDeck() throws Exception {
        try {
            // Read json object from file
            JSONObject createExpertDeckPayload = jsonReader.getJsonObject(ExpertDeckCreatePayload);
            System.out.println(createExpertDeckPayload);

            String payload = String.valueOf(createExpertDeckPayload);
            //storeControllerRequestDTO = mapper.readValue(payload, StoreControllerRequestDTO.class);
            StoreControllerResponseDTO = createExpertDeck(headerAuthor, payload);

            Assert.assertEquals(StatusCode, 201, "Status Code");
            LOGGER.info("deck Id:" + StoreControllerResponseDTO.getId());
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 2) @AlmAnnotation(almTestId = "xxxxxxxx")
    public void CreateExpertQuestion() throws Exception {
        try {
            // Read json object from file
            JSONObject createExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionCreatePayload);
            System.out.println(createExpertQuestionPayload);
            headerAuthor.put("x-Resource-Id",StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type","expert");

            String payload = String.valueOf(createExpertQuestionPayload);
            //StoreControllerRequestDTO = mapper.readValue(payload, StoreControllerRequestDTO.class);
            ExpertQuestionResponseDTO = createExpertQuestion(headerAuthor, payload);

            Assert.assertEquals(StatusCode, 201, "Status Code");
            //LOGGER.info("deck Id:" + ExpertQuestionResponseDTO.get);
        } catch (Exception e) {
            throw e;
        }
    }

    //Provision an expert deck and get provisioned (user) deck details
    @Test(priority = 3) @AlmAnnotation(almTestId = "XXXXX")
    public void ProvisionExpertDecksOrGetProvisionedExpertDecks() throws Exception {
        try {
            String ExpertDeckId = StoreControllerResponseDTO.getId();
            LOGGER.info("DeckId: " + ExpertDeckId);
            queryparam.put("includeProgress","true");
            queryparam.put("includeRecentProgress","true");
            queryparam.put("includeQuestionCount","true");
            //queryparam.put("productId",DeckControllerResponseDTO.getDecks()[0].getProductId());
            ProvisionedDeckDTO = GetProvisionedDeck(headerNormal, queryparam, ExpertDeckId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertNotNull(ProvisionedDeckDTO.getId(),"Provisioned Deck Id");
            Assert.assertEquals(ProvisionedDeckDTO.getTitle(), StoreControllerResponseDTO.getTitle(),"Provisioned Deck Id");
            if (Env.equals("PROD")){
                Assert.assertEquals(ProvisionedDeckDTO.getUserId(), "bc4e17dc1478465a89af0f84ec5df597","Provisioned Deck Id");
            }
            else{
                Assert.assertEquals(ProvisionedDeckDTO.getUserId(), "0f1d72fbc8a4412ba1ac8b8970eeffd5","Provisioned Deck Id");
            }
            Assert.assertEquals(ProvisionedDeckDTO.getProductId(), StoreControllerResponseDTO.getBook().getProductIds()[0],"Provisioned Deck Id");
            Assert.assertEquals(ProvisionedDeckDTO.getBookId(), StoreControllerResponseDTO.getBook().getBookIds()[0],"Provisioned Deck Id");
            Assert.assertEquals(ProvisionedDeckDTO.getChapterId(), StoreControllerResponseDTO.getBook().getChapterId(),"Provisioned Deck Id");
            Assert.assertEquals(ProvisionedDeckDTO.getNoOfCards(), 1,"Provisioned Deck Id");
            Assert.assertEquals(ProvisionedDeckDTO.getParentDeckId(), StoreControllerResponseDTO.getId(),"Provisioned Deck Id");
            Assert.assertNotNull(ProvisionedDeckDTO.getCreatedAt(), "Provisioned Deck Id");
            Assert.assertNotNull(ProvisionedDeckDTO.getUpdatedAt(),"Provisioned Deck Id");
            Assert.assertNotNull(ProvisionedDeckDTO.getUserCreatedAt(), "Provisioned Deck Id");
            Assert.assertEquals(ProvisionedDeckDTO.isArchived(), false,"Provisioned Deck Id");
            Assert.assertEquals(ProvisionedDeckDTO.getDeckType(), "PURCHASED","Provisioned Deck Id");
            Assert.assertEquals(ProvisionedDeckDTO.isDeleted(), false,"Provisioned Deck Id");
            Assert.assertNotNull(ProvisionedDeckDTO.getDeckActivity(), "deckActivity");
            Assert.assertEquals(ProvisionedDeckDTO.getDeckActivity().getLearnedCards(), 0, "learnedCards");
            Assert.assertEquals(ProvisionedDeckDTO.getDeckActivity().getSeenCards(), 0,"seenCards");
            Assert.assertEquals(ProvisionedDeckDTO.getDeckActivity().getUnseenCards(), 1,"unseenCards");
            Assert.assertEquals(ProvisionedDeckDTO.getDeckActivity().getFavoriteCards(), 0,"favoriteCards");
            Assert.assertEquals(ProvisionedDeckDTO.getDeckActivity().getCorrectCards(), 0,"correctCards");
            Assert.assertEquals(ProvisionedDeckDTO.getDeckActivity().getTotalCards(), 1,"totalCards");
            Assert.assertNotNull(ProvisionedDeckDTO.getDeckActivity().getRecentActivity(), "recentActivity");
            Assert.assertEquals(ProvisionedDeckDTO.getDeckActivity().getRecentActivity().getCorrectCards(), 0,"correctCards");
            Assert.assertEquals(ProvisionedDeckDTO.getDeckActivity().getRecentActivity().getTotalCards(),0, "totalCards");

            LOGGER.info("Tenant Id:" + ProvisionedDeckDTO.getId());
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 4) @AlmAnnotation(almTestId = "XXXXX")
    public void GetRecommendedCards() throws Exception {
        try {
            // Read json object from file
            JSONObject getRecommendationPayload = jsonReader.getJsonObject(GetRecommendationPayload);
            getRecommendationPayload.put("deck",ProvisionedDeckDTO.getId());
            getRecommendationPayload.put("person",ProvisionedDeckDTO.getUserId());

            String payload = String.valueOf(getRecommendationPayload);
            //GetRecommendationResponseDTO = mapper.readValue(payload, GetRecommendationResponseDTO.class);
            GetRecommendationResponseDTO = getRecommendation(headerNormal, payload);

            Assert.assertEquals(StatusCode, 200, "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 5) @AlmAnnotation(almTestId = "XXXX")
    public void PostRecommendedActivities() throws Exception {
        try {
            // Read json object from file
            JSONObject postRecommendedActivitiesPayload = jsonReader.getJsonObject(PostRecommendedActivitiesPayload);
            postRecommendedActivitiesPayload.put("deck",ProvisionedDeckDTO.getId());
            postRecommendedActivitiesPayload.put("person",ProvisionedDeckDTO.getUserId());
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

    @Test(priority = 6) @AlmAnnotation(almTestId = "XXXX")
    public void GetActivitiesOfDecks() throws Exception {
        try {
            // Read json object from file
            JSONObject getActivitiesOfDecksPayload = jsonReader.getJsonObject(GetActivitiesOfDecksPayload);

            JSONArray deckIds = new JSONArray();
            deckIds.add(ProvisionedDeckDTO.getId());
            getActivitiesOfDecksPayload.put("deckIds",deckIds);
            getActivitiesOfDecksPayload.put("studentId",ProvisionedDeckDTO.getUserId());

            String payload = String.valueOf(getActivitiesOfDecksPayload);
            //PostRecommendedActivitiesRequestDTO = mapper.readValue(payload, PostRecommendedActivitiesRequestDTO.class);
            GetActivitiesOfDecksResponseDTO = getActivitiesOfDecks(headerNormal, payload);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(GetActivitiesOfDecksResponseDTO.getDeckActivity()[0].getDeckId(), ProvisionedDeckDTO.getId(), "Status Code");
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

    @Test(priority = 7) @AlmAnnotation(almTestId = "xxxxxx")
    public void DeleteExpertDeck() throws Exception {
        try {
            String ExpertId = StoreControllerResponseDTO.getId();
            StoreControllerResponseDTO = deleteExpert(headerAuthor, ExpertId);

            Assert.assertEquals(StatusCode, 204, "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    //Verify whether that user can provision/get deck successfully by send request with expert deck Id which is free
    @Test(priority = 8) @AlmAnnotation(almTestId = "642352")
    public void ProvisionExpertDecksWhenExpertDeckFree() throws Exception {
        try {
            CreateExpertDeck();
            String ExpertDeckId = StoreControllerResponseDTO.getId();
            LOGGER.info("DeckId: " + ExpertDeckId);
            //queryparam.put("productId",DeckControllerResponseDTO.getDecks()[0].getProductId());
            ProvisionedDeckDTO = GetProvisionedDeck(headerNormal, queryparam, ExpertDeckId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(StoreControllerResponseDTO.getPurchaseInfo().getPrice(), 0.0,"Price");
            Assert.assertEquals(ProvisionedDeckDTO.getParentDeckId(),StoreControllerResponseDTO.getId(),"Expert deck Id");
            LOGGER.info("Provisioned Deck Id:" + ProvisionedDeckDTO.getId());
            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    //Verify whether that user can provision/get deck successfully by send request with expert deck Id which is not free
    @Test(priority = 9) @AlmAnnotation(almTestId = "642350")
    public void ProvisionExpertDecksWhenExpertDeckNotFree() throws Exception {
        try {
            JSONObject createExpertDeckPayload = jsonReader.getJsonObject(ExpertDeckCreatePayload);
            JSONObject purchaseInfo = new JSONObject();
            purchaseInfo.put("price","0.99");
            purchaseInfo.put("purchasedDate","2019-10-14T11:25:50.450Z");
            purchaseInfo.put("sku","com.pearsoned.smartflashcardsmvp.expertdecktier0");
            purchaseInfo.put("status","Available");

            createExpertDeckPayload.put("purchaseInfo",purchaseInfo);

            System.out.println(createExpertDeckPayload);
            String payload = String.valueOf(createExpertDeckPayload);
            //storeControllerRequestDTO = mapper.readValue(payload, StoreControllerRequestDTO.class);
            StoreControllerResponseDTO = createExpertDeck(headerAuthor, payload);

            Assert.assertEquals(StatusCode, 201, "Status Code");

            String ExpertDeckId = StoreControllerResponseDTO.getId();
            LOGGER.info("DeckId: " + ExpertDeckId);
            //queryparam.put("productId",DeckControllerResponseDTO.getDecks()[0].getProductId());
            ProvisionedDeckDTO = GetProvisionedDeck(headerNormal, queryparam, ExpertDeckId);

            Assert.assertEquals(StatusCode, 403, "Status Code");
            Assert.assertEquals(ProvisionedDeckDTO.getDescription(), "You do not have permission to provision the given expert deck. Expert Deck Id: "+ExpertDeckId,"Price");
            //Assert.assertEquals(ProvisionedDeckDTO.getParentDeckId(),StoreControllerResponseDTO.getId(),"Expert deck Id");
            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    //Verify whether that user get 403 Forbidden request error when provisioning pilot expert deck
    @Test(priority = 10) @AlmAnnotation(almTestId = "642355")
    public void ProvisionExpertDecksWhenExpertDeckarePilot() throws Exception {
        try {
            JSONObject createExpertDeckPayload = jsonReader.getJsonObject(ExpertDeckCreatePayload);
            createExpertDeckPayload.put("pilot","true");

            System.out.println(createExpertDeckPayload);
            String payload = String.valueOf(createExpertDeckPayload);
            //storeControllerRequestDTO = mapper.readValue(payload, StoreControllerRequestDTO.class);
            StoreControllerResponseDTO = createExpertDeck(headerAuthor, payload);

            Assert.assertEquals(StatusCode, 201, "Status Code");

            String ExpertDeckId = StoreControllerResponseDTO.getId();
            LOGGER.info("DeckId: " + ExpertDeckId);
            //queryparam.put("productId",DeckControllerResponseDTO.getDecks()[0].getProductId());
            ProvisionedDeckDTO = GetProvisionedDeck(headerNormal, queryparam, ExpertDeckId);

            Assert.assertEquals(StatusCode, 403, "Status Code");
            Assert.assertEquals(ProvisionedDeckDTO.getDescription(), "You do not have permission to provision the given expert deck. Expert Deck Id: "+ExpertDeckId,"Price");
            //Assert.assertEquals(ProvisionedDeckDTO.getParentDeckId(),StoreControllerResponseDTO.getId(),"Expert deck Id");
            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    //Verify whether that user get 403 Forbidden request error when provisioning expert deck which is not in active status
    @Test(priority = 11) @AlmAnnotation(almTestId = "642349")
    public void ProvisionExpertDecksWhenExpertDeckisNotActive() throws Exception {
        try {
            JSONObject createExpertDeckPayload = jsonReader.getJsonObject(ExpertDeckCreatePayload);
            createExpertDeckPayload.put("status","inactive");

            System.out.println(createExpertDeckPayload);
            String payload = String.valueOf(createExpertDeckPayload);
            //storeControllerRequestDTO = mapper.readValue(payload, StoreControllerRequestDTO.class);
            StoreControllerResponseDTO = createExpertDeck(headerAuthor, payload);

            Assert.assertEquals(StatusCode, 201, "Status Code");

            String ExpertDeckId = StoreControllerResponseDTO.getId();
            LOGGER.info("DeckId: " + ExpertDeckId);
            //queryparam.put("productId",DeckControllerResponseDTO.getDecks()[0].getProductId());
            ProvisionedDeckDTO = GetProvisionedDeck(headerNormal, queryparam, ExpertDeckId);

            Assert.assertEquals(StatusCode, 403, "Status Code");
            Assert.assertEquals(ProvisionedDeckDTO.getDescription(), "You do not have permission to provision the given expert deck. Expert Deck Id: "+ExpertDeckId,"Price");
            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    //Verify whether that user get 400 Bad request error when provisioning expert deck with null value for deckId
    @Test(priority = 12) @AlmAnnotation(almTestId = "642353")
    public void ProvisionExpertDecksWhenNullValueforExpertDeckId() throws Exception {
        try {
            String ExpertDeckId = "null";
            //queryparam.put("productId",DeckControllerResponseDTO.getDecks()[0].getProductId());
            ProvisionedDeckDTO = GetProvisionedDeck(headerNormal, queryparam, ExpertDeckId);

            Assert.assertEquals(StatusCode, 404, "Status Code");
            Assert.assertEquals(ProvisionedDeckDTO.getDescription(), "Invalid Deck ID","Price");
        } catch (Exception e) {
            throw e;
        }
    }

    //Verify whether that user get 400 Bad request error when provisioning expert deck with empty value for deckId
    @Test(priority = 13) @AlmAnnotation(almTestId = "XXXX")
    public void ProvisionExpertDecksWhenEmptyValueforExpertDeckId() throws Exception {
        try {
            String ExpertDeckId = " ";
            //queryparam.put("productId",DeckControllerResponseDTO.getDecks()[0].getProductId());
            ProvisionedDeckDTO = GetProvisionedDeck(headerNormal, queryparam, ExpertDeckId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(ProvisionedDeckDTO.getDescription(), "Expert Deck Id Cannot be null or empty","Price");
        } catch (Exception e) {
            throw e;
        }
    }

    //Verify whether that user get 404 invalid deck id error when send request with invalid expert deck Id
    @Test(priority = 14) @AlmAnnotation(almTestId = "642351")
    public void ProvisionExpertDecksWhenInvalidValueforExpertDeckId() throws Exception {
        try {
            String ExpertDeckId = "123";
            //queryparam.put("productId",DeckControllerResponseDTO.getDecks()[0].getProductId());
            ProvisionedDeckDTO = GetProvisionedDeck(headerNormal, queryparam, ExpertDeckId);

            Assert.assertEquals(StatusCode, 404, "Status Code");
            Assert.assertEquals(ProvisionedDeckDTO.getDescription(), "Invalid Deck ID","Price");
        } catch (Exception e) {
            throw e;
        }
    }


    //Verify whether that user can provision,retrieve deck with deckActivity when send request with expert deck Id including  includeProgress as true
    @Test(priority = 15) @AlmAnnotation(almTestId = "642356")
    public void ProvisionExpertDecksWithIncludeProgressParameter() throws Exception {
        try {
            CreateExpertDeck();

            String ExpertDeckId = StoreControllerResponseDTO.getId();
            LOGGER.info("DeckId: " + ExpertDeckId);
            queryparam.put("includeProgress","true");
            //queryparam.put("productId",DeckControllerResponseDTO.getDecks()[0].getProductId());
            ProvisionedDeckDTO = GetProvisionedDeck(headerNormal, queryparam, ExpertDeckId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertNotNull(ProvisionedDeckDTO.getDeckActivity(), "deckActivity");
            Assert.assertNotNull(ProvisionedDeckDTO.getDeckActivity().getLearnedCards(), "learnedCards");
            Assert.assertNotNull(ProvisionedDeckDTO.getDeckActivity().getSeenCards(), "seenCards");
            Assert.assertNotNull(ProvisionedDeckDTO.getDeckActivity().getUnseenCards(), "unseenCards");
            Assert.assertNotNull(ProvisionedDeckDTO.getDeckActivity().getFavoriteCards(), "favoriteCards");
            Assert.assertNotNull(ProvisionedDeckDTO.getDeckActivity().getCorrectCards(), "correctCards");
            Assert.assertNotNull(ProvisionedDeckDTO.getDeckActivity().getTotalCards(), "totalCards");

            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }


    //Verify whether that user can provision,retrieve deck with recentActivity when send request with expert deck Id including  includeRecentProgress as true
    @Test(priority = 16) @AlmAnnotation(almTestId = "642358")
    public void ProvisionExpertDecksWithIncludeRecentProgressParameter() throws Exception {
        try {
            CreateExpertDeck();

            String ExpertDeckId = StoreControllerResponseDTO.getId();
            LOGGER.info("DeckId: " + ExpertDeckId);
            queryparam.put("includeProgress","true");
            queryparam.put("includeRecentProgress","true");
            //queryparam.put("productId",DeckControllerResponseDTO.getDecks()[0].getProductId());
            ProvisionedDeckDTO = GetProvisionedDeck(headerNormal, queryparam, ExpertDeckId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertNotNull(ProvisionedDeckDTO.getDeckActivity(), "deckActivity");
            Assert.assertNotNull(ProvisionedDeckDTO.getDeckActivity().getLearnedCards(), "learnedCards");
            Assert.assertNotNull(ProvisionedDeckDTO.getDeckActivity().getSeenCards(), "seenCards");
            Assert.assertNotNull(ProvisionedDeckDTO.getDeckActivity().getUnseenCards(), "unseenCards");
            Assert.assertNotNull(ProvisionedDeckDTO.getDeckActivity().getFavoriteCards(), "favoriteCards");
            Assert.assertNotNull(ProvisionedDeckDTO.getDeckActivity().getCorrectCards(), "correctCards");
            Assert.assertNotNull(ProvisionedDeckDTO.getDeckActivity().getTotalCards(), "totalCards");
            Assert.assertNotNull(ProvisionedDeckDTO.getDeckActivity().getRecentActivity(), "recentActivity");
            Assert.assertNotNull(ProvisionedDeckDTO.getDeckActivity().getRecentActivity().getCorrectCards(), "correctCards");
            Assert.assertNotNull(ProvisionedDeckDTO.getDeckActivity().getRecentActivity().getTotalCards(), "totalCards");

            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    //Verify whether that user can provision,retrieve deck with recentActivity when send request with expert deck Id including  includeRecentProgress as true
    @Test(priority = 17) @AlmAnnotation(almTestId = "XXXX")
    public void ProvisionExpertDecksWithIncludeQuestionCountParameter() throws Exception {
        try {
            CreateExpertDeck();
            String ExpertDeckId = StoreControllerResponseDTO.getId();
            CreateExpertQuestion();
            LOGGER.info("DeckId: " + ExpertDeckId);
            queryparam.put("includeProgress","true");
            queryparam.put("includeRecentProgress","true");
            queryparam.put("includeQuestionCount","true");
            //queryparam.put("productId",DeckControllerResponseDTO.getDecks()[0].getProductId());
            ProvisionedDeckDTO = GetProvisionedDeck(headerNormal, queryparam, ExpertDeckId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(ProvisionedDeckDTO.getNoOfCards(), 1, "deckActivity");

            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    //Verify whether that update PLA with the provisioned deck id and list of questions for the user
    @Test(priority = 18) @AlmAnnotation(almTestId = "642357")
    public void ProvisionExpertDecksWithValidatePLA() throws Exception {
        try {
            CreateExpertDeck();
            String ExpertDeckId = StoreControllerResponseDTO.getId();
            CreateExpertQuestion();
            LOGGER.info("DeckId: " + ExpertDeckId);
            queryparam.put("includeProgress","true");
            queryparam.put("includeRecentProgress","true");
            queryparam.put("includeQuestionCount","true");
            //queryparam.put("productId",DeckControllerResponseDTO.getDecks()[0].getProductId());
            ProvisionedDeckDTO = GetProvisionedDeck(headerNormal, queryparam, ExpertDeckId);
            Assert.assertEquals(StatusCode, 200, "Status Code");

            GetRecommendedCards();
            PostRecommendedActivities();
            GetActivitiesOfDecks();

            ProvisionedDeckDTO = GetProvisionedDeck(headerNormal, queryparam, ExpertDeckId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(ProvisionedDeckDTO.getNoOfCards(), 1, "deckActivity");
            Assert.assertNotNull(ProvisionedDeckDTO.getDeckActivity(), "deckActivity");
            Assert.assertEquals(ProvisionedDeckDTO.getDeckActivity().getLearnedCards(), 0, "learnedCards");
            Assert.assertEquals(ProvisionedDeckDTO.getDeckActivity().getSeenCards(), 1,"seenCards");
            Assert.assertEquals(ProvisionedDeckDTO.getDeckActivity().getUnseenCards(), 0,"unseenCards");
            Assert.assertEquals(ProvisionedDeckDTO.getDeckActivity().getFavoriteCards(), 0,"favoriteCards");
            Assert.assertEquals(ProvisionedDeckDTO.getDeckActivity().getCorrectCards(), 1,"correctCards");
            Assert.assertEquals(ProvisionedDeckDTO.getDeckActivity().getTotalCards(), 1,"totalCards");
            Assert.assertNotNull(ProvisionedDeckDTO.getDeckActivity().getRecentActivity(), "recentActivity");
            Assert.assertEquals(ProvisionedDeckDTO.getDeckActivity().getRecentActivity().getCorrectCards(), 1,"correctCards");
            Assert.assertEquals(ProvisionedDeckDTO.getDeckActivity().getRecentActivity().getTotalCards(),1, "totalCards");

            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    //Verify whether that user get 400 Bad request error when provisioning expert deck with invalid tenantId
    @Test(priority = 19) @AlmAnnotation(almTestId = "642345")
    public void ProvisionExpertDecksWithInvalidTenantId() throws Exception {

        try {
            headerNormal.put("X-TenantId","a");
            String ExpertDeckId = "a";
            ProvisionedDeckDTO = GetProvisionedDeck(headerNormal, queryparam, ExpertDeckId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(ProvisionedDeckDTO.getDescription(), "Field 'tenantId' does not have a valid value");
        }
        catch (Exception e) {
            throw e;
        }
    }

    //Verify whether that user get 403 Bad request error when provisioning expert deck without any tenantId
    @Test(priority = 20) @AlmAnnotation(almTestId = "642346")
    public void ProvisionExpertDecksWithoutTenantId() throws Exception {

        try {
            headerNormal.put("X-TenantId","");
            String ExpertDeckId = "a";
            ProvisionedDeckDTO = GetProvisionedDeck(headerNormal, queryparam, ExpertDeckId);

            Assert.assertEquals(StatusCode, 403, "Status Code");
            Assert.assertEquals(ProvisionedDeckDTO.getDescription(), "You do not have permission to access this method without a valid tenant ID");
        }
        catch (Exception e) {
            throw e;
        }
    }

    //Verify whether that user get 400 Bad request error when provisioning expert deck with invalid tenantKey
    @Test(priority = 21) @AlmAnnotation(almTestId = "642347")
    public void ProvisionExpertDecksWithInvalidTenantKey() throws Exception {

        try {
            getData();
            headerNormal.put("X-TenantKey","a");
            String ExpertDeckId = "a";
            ProvisionedDeckDTO = GetProvisionedDeck(headerNormal, queryparam, ExpertDeckId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(ProvisionedDeckDTO.getDescription(), "Field 'tenantKey' does not have a valid value");
        }
        catch (Exception e) {
            throw e;
        }
    }

    //Verify whether that user get 403 Bad request error when provisioning expert deck without any tenantKey
    @Test(priority = 22) @AlmAnnotation(almTestId = "642348")
    public void ProvisionExpertDecksWithoutTenantKey() throws Exception {

        try {
            getData();
            headerNormal.put("X-TenantKey","");
            String ExpertDeckId = "a";
            ProvisionedDeckDTO = GetProvisionedDeck(headerNormal, queryparam, ExpertDeckId);

            Assert.assertEquals(StatusCode, 403, "Status Code");
            Assert.assertEquals(ProvisionedDeckDTO.getDescription(), "You do not have permission to access this method without a valid tenant Key");
        } catch (Exception e) {
            throw e;
        }
    }

    //Verify whether that user get 401 Unauthorized request error when send the request with invalid PI token or expired PI token
    @Test(priority = 23) @AlmAnnotation(almTestId = "642354")
    public void ProvisionExpertDecksWithUnauthorizedError() throws Exception{
        try {
            getData();
            headerNormal.put("X-Authorization","a");
            String ExpertDeckId  = "a";
            ProvisionedDeckDTO = GetProvisionedDeck(headerNormal, queryparam, ExpertDeckId);

            Assert.assertEquals(StatusCode, 401, "Status Code");
            Assert.assertNotNull(ProvisionedDeckDTO.getError(), "Unauthorized");
        } catch (Exception e) {
            throw e;
        }
    }


}