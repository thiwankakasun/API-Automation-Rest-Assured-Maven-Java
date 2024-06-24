package API;

import com.pearson.common.framework.shared.alm.AlmAnnotation;
import io.cucumber.java.an.E;
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
import requestDTO.StoreControllerRequestDTO;
import responseDTO.*;
import utils.JsonReader;
import utils.PiTokenGenarator;
import utils.PiTokenGenaratorAuthor;
import utils.PiTokenGenaratorNormalUser;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.logging.Logger;

public class ExpertQuestion extends ServiceController {

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

    private static final Logger LOGGER = Logger.getLogger(String.valueOf(ExpertQuestion.class));


    @BeforeClass
    public void getData() throws EncryptedDocumentException, InvalidFormatException, IOException, ParseException {
        try {
          //  PiToken piToken = PiTokenGenarator.generatePiToken(loginUrl);
          //  header.put("X-Authorization", piToken.getData());
            PiToken piToken1 = PiTokenGenaratorNormalUser.generatePiToken(loginUrl);
            headerNormal.put("X-Authorization", piToken1.getData());
            PiToken piTokenAuthor = PiTokenGenaratorAuthor.generatePiToken(loginUrl);
            headerAuthor.put("X-Authorization", piTokenAuthor.getData());
//            if (Env.equals("DEV")) {
//                headerNormal.put("X-TenantId", "5660deb9-7b3b-44e8-8a6c-4b1fdae17308");
//                headerNormal.put("X-TenantKey", "b308daa4-2ed4-40a0-ab8c-49fda99994a3");
//            }
//
//            if (Env.equals("QA")) {
//                headerNormal.put("X-TenantId", "63bc8b14-cb49-466c-8d92-2e2d1358b5ff");
//                headerNormal.put("X-TenantKey", "12d4b93c-0ac6-44a7-aba0-6f53d97dee52");
//            }
//
//            if (Env.equals("NFT")) {
//                headerNormal.put("X-TenantId", "f00ecc22-af26-4b56-be26-5a40b7e25d23");
//                headerNormal.put("X-TenantKey", "27c1c0d2-656a-4d04-a491-06b13ac27261");
//            }
//
//            if (Env.equals("STG")) {
//                headerNormal.put("X-TenantId", "acf59553-f258-4d40-a46d-49a128301043");
//                headerNormal.put("X-TenantKey", "3f156508-7e4f-403b-b520-aa444b1fb905");
//            }
//
//            if (Env.equals("PROD")) {
//                headerNormal.put("X-TenantId", "f00ecc22-af26-4b56-be26-5a40b7e25d23");
//                headerNormal.put("X-TenantKey", "27c1c0d2-656a-4d04-a491-06b13ac27261");
//            }

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

    @Test(priority = 1)
    @AlmAnnotation(almTestId = "")
    public void CreateExpertDeck() throws Exception {
        try {
            JSONObject createExpertDeckPayload = jsonReader.getJsonObject(ExpertDeckCreatePayload);
            System.out.println(createExpertDeckPayload);

            String payload = String.valueOf(createExpertDeckPayload);
            //StoreControllerRequestDTO = mapper.readValue(payload, StoreControllerRequestDTO.class);
            StoreControllerResponseDTO = createExpertDeck(headerAuthor, payload);

            Assert.assertEquals(StatusCode, 201, "Status Code");
            LOGGER.info("deck Id:" + StoreControllerResponseDTO.getId());
        } catch (Exception e) {
            throw e;
        }
    }


    @Test(priority = 2)
    @AlmAnnotation(almTestId = "")
    public void CreateExpertQuestion() throws Exception {
        try {
            // Read json object from file
            JSONObject createExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionCreatePayload);
            System.out.println(createExpertQuestionPayload);
            headerAuthor.put("x-Resource-Id", StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type", "expert");

            String payload = String.valueOf(createExpertQuestionPayload);
            //StoreControllerRequestDTO = mapper.readValue(payload, StoreControllerRequestDTO.class);
            ExpertQuestionResponseDTO = createExpertQuestion(headerAuthor, payload);

            Assert.assertEquals(StatusCode, 201, "Status code");
            //LOGGER.info("deck Id:" + ExpertQuestionResponseDTO.get);
        } catch (Exception e) {
            throw e;
        }
    }


      @Test(priority = 3) @AlmAnnotation(almTestId = "")
    public void GetExpertQuestionUsingQuestionId() throws Exception {
        try {
            // Read json object from file
            // JSONObject GetExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionGetPayload);
            // System.out.println(GetExpertQuestionPayload);
            getData();
            headerAuthor.put("x-Resource-Id", StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type", "expert");

            String ExpertquestionId = ExpertQuestionResponseDTO.getId();
            ExpertQuestionResponseDTO = getExpertQuestion(headerAuthor, ExpertquestionId);

            Assert.assertEquals(StatusCode, 200, "Status code");

        } catch (Exception e) {
            throw e;
        }
    }

     @Test(priority = 4) @AlmAnnotation(almTestId = "")
    public void UpdateExpertQuestionUsingQuestionId() throws Exception {
        try {

            JSONObject updateExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionUpdatePayload);
            System.out.println(updateExpertQuestionPayload);
            headerAuthor.put("x-Resource-Id", StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type", "expert");

            updateExpertQuestionPayload.put("creatorId",StoreControllerResponseDTO.getUserId());
            updateExpertQuestionPayload.put("deckId",StoreControllerResponseDTO.getId());

            String expertQuestionId = ExpertQuestionResponseDTO.getId();
            String payload = String.valueOf(updateExpertQuestionPayload);

            ExpertQuestionResponseDTO = updateExpertQuestion(headerAuthor, payload, expertQuestionId);


            Assert.assertEquals(StatusCode, 200, "Status Code");
        } catch (Exception e) {
            throw e;

        }
    }

    @Test(priority = 5)
    @AlmAnnotation(almTestId = "598683")
    public void DeleteExpertQuestionUsingQuestionId() throws Exception {

        try {
            getData();
            headerAuthor.put("x-Resource-Id", StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type", "expert");

            String ExpertQuestionId = ExpertQuestionResponseDTO.getId();
            ExpertQuestionResponseDTO = deleteExpertQuestion(headerAuthor, ExpertQuestionId);

            Assert.assertEquals(StatusCode, 204, "Status Code");

        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 6) @AlmAnnotation(almTestId = "xxxxxx")
    public void DeleteExpertDeck() throws Exception {
        try {
            String ExpertId = StoreControllerResponseDTO.getId();
            StoreControllerResponseDTO = deleteExpert(headerAuthor, ExpertId);

            Assert.assertEquals(StatusCode, 204, "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 7)
    @AlmAnnotation(almTestId = "609861")
    public void CreateExpertQuestionWithoutDeckIdField() throws Exception {
        try {
            CreateExpertDeck();

            JSONObject createExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionCreatePayload);
            System.out.println(createExpertQuestionPayload);
            headerAuthor.put("x-Resource-Id", StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type", "expert");

            createExpertQuestionPayload.put("creatorId",StoreControllerResponseDTO.getUserId());
            createExpertQuestionPayload.put("deckId",null);

            String payload = String.valueOf(createExpertQuestionPayload);
            ExpertQuestionResponseDTO = createExpertQuestion(headerAuthor, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage(), "'deckId' Cannot be null or empty", "Status Code");

            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 8)
    @AlmAnnotation(almTestId = "609861")
    public void CreateExpertQuestionWithoutValueField() throws Exception {
        try {
            CreateExpertDeck();

            JSONObject createExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionCreatePayload);
            System.out.println(createExpertQuestionPayload);
            headerAuthor.put("x-Resource-Id", StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type", "expert");

            JSONObject answers1 = new JSONObject();
            answers1.put("caseSensitive", false);
            answers1.put("correct", false);
            answers1.put("id", 1);
            answers1.put("type", "TEXT");
            answers1.put("value", null);
            JSONArray answers = new JSONArray();
            answers.add(answers1);

            createExpertQuestionPayload.put("answers", answers);


            createExpertQuestionPayload.put("creatorId",StoreControllerResponseDTO.getUserId());
            createExpertQuestionPayload.put("deckId",StoreControllerResponseDTO.getId());

            String payload = String.valueOf(createExpertQuestionPayload);

            ExpertQuestionResponseDTO = createExpertQuestion(headerAuthor, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage(), "'value' Cannot be null or empty", "Status Code");

            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 9)
    @AlmAnnotation(almTestId = "609861")
    public void CreateExpertQuestionWithEmptyValueForDeckId() throws Exception {
        try {
            CreateExpertDeck();

            JSONObject createExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionCreatePayload);
            System.out.println(createExpertQuestionPayload);
            headerAuthor.put("x-Resource-Id", StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type", "expert");

            createExpertQuestionPayload.put("creatorId",StoreControllerResponseDTO.getUserId());
            createExpertQuestionPayload.put("deckId","");

            String payload = String.valueOf(createExpertQuestionPayload);
            ExpertQuestionResponseDTO = createExpertQuestion(headerAuthor, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            if (ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage().equals("'deckId' Cannot be null or empty")) {
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getField(), "deckId", "Status Code");
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage(), "'deckId' Cannot be null or empty", "Status Code");
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[1].getField(), "deckId", "Status Code");
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[1].getMessage(), "must match \"^[0-9a-fA-F]{24}$\"", "Status Code");
            } else {
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getField(), "deckId", "Status Code");
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage(), "must match \"^[0-9a-fA-F]{24}$\"", "Status Code");
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[1].getMessage(), "'deckId' Cannot be null or empty", "Status Code");
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[1].getField(), "deckId", "Status Code");
            }
            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 10)
    @AlmAnnotation(almTestId = "609861")
    public void CreateExpertQuestionWithInvalidValueForDeckId() throws Exception {
        try {
            CreateExpertDeck();

            JSONObject createExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionCreatePayload);
            System.out.println(createExpertQuestionPayload);
            headerAuthor.put("x-Resource-Id", StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type", "expert");

            createExpertQuestionPayload.put("creatorId",StoreControllerResponseDTO.getUserId());
            createExpertQuestionPayload.put("deckId","1234");

            String payload = String.valueOf(createExpertQuestionPayload);
            ExpertQuestionResponseDTO = createExpertQuestion(headerAuthor, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage(), "must match \"^[0-9a-fA-F]{24}$\"", "Status Code");

            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 11)
    @AlmAnnotation(almTestId = "609861")
    public void CreateExpertQuestionWithDeletedDeckId() throws Exception {
        try {
            CreateExpertDeck();

            JSONObject createExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionCreatePayload);
            System.out.println(createExpertQuestionPayload);
            headerAuthor.put("x-Resource-Id", StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type", "expert");


            createExpertQuestionPayload.put("creatorId",StoreControllerResponseDTO.getUserId());
            createExpertQuestionPayload.put("deckId",StoreControllerResponseDTO.getId());

            DeleteExpertDeck();
            String payload = String.valueOf(createExpertQuestionPayload);
            ExpertQuestionResponseDTO = createExpertQuestion(headerAuthor, payload);

            Assert.assertEquals(StatusCode, 403, "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getDescription(), "Request does not contain valid information to proceed", "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 12)
    @AlmAnnotation(almTestId = "609861")
    public void CreateExpertQuestionWithDifferentUser() throws Exception {
        try {
            CreateExpertDeck();
            JSONObject createExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionCreatePayload);
            System.out.println(createExpertQuestionPayload);

            headerNormal.put("x-Resource-Id", StoreControllerResponseDTO.getId());
            headerNormal.put("x-Resource-Type", "expert");

            createExpertQuestionPayload.put("creatorId",StoreControllerResponseDTO.getUserId());
            createExpertQuestionPayload.put("deckId",StoreControllerResponseDTO.getId());

            String payload = String.valueOf(createExpertQuestionPayload);
            ExpertQuestionResponseDTO = createExpertQuestion(headerNormal, payload);

            Assert.assertEquals(StatusCode, 403, "Status Code"); //"error.dependencyFailed"
            Assert.assertEquals(ExpertQuestionResponseDTO.getDescription(), "You do not have AUTHORING_TOOL_USER permission to access this method", "Status Code");
            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 13)
    @AlmAnnotation(almTestId = "609861")
    public void CreateExpertQuestionWithEmptyValueField() throws Exception {
        try {
            CreateExpertDeck();

            JSONObject createExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionCreatePayload);
            System.out.println(createExpertQuestionPayload);
            headerAuthor.put("x-Resource-Id", StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type", "expert");

            JSONObject answers1 = new JSONObject();
            answers1.put("caseSensitive", false);
            answers1.put("correct", false);
            answers1.put("id", 1);
            answers1.put("type", "TEXT");
            answers1.put("value", "");

            JSONArray answers = new JSONArray();
            answers.add(answers1);

            createExpertQuestionPayload.put("answers", answers);


            createExpertQuestionPayload.put("creatorId",StoreControllerResponseDTO.getUserId());
            createExpertQuestionPayload.put("deckId",StoreControllerResponseDTO.getId());

            String payload = String.valueOf(createExpertQuestionPayload);
            ExpertQuestionResponseDTO = createExpertQuestion(headerAuthor, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage(), "'value' Cannot be null or empty", "Status Code");

            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 14)
    @AlmAnnotation(almTestId = "609861")
    public void CreateExpertQuestionWithEmptyValuefortypeField() throws Exception {
        try {
            CreateExpertDeck();

            JSONObject createExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionCreatePayload);
            System.out.println(createExpertQuestionPayload);
            headerAuthor.put("x-Resource-Id", StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type", "expert");

            JSONObject answers1 = new JSONObject();
            answers1.put("caseSensitive", false);
            answers1.put("correct", false);
            answers1.put("id", 1);
            answers1.put("type", "");
            answers1.put("value", "a");


            JSONArray answers = new JSONArray();
            answers.add(answers1);

            createExpertQuestionPayload.put("answers", answers);

            createExpertQuestionPayload.put("creatorId",StoreControllerResponseDTO.getUserId());
            createExpertQuestionPayload.put("deckId",StoreControllerResponseDTO.getId());

            String payload = String.valueOf(createExpertQuestionPayload);
            ExpertQuestionResponseDTO = createExpertQuestion(headerAuthor, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getField(), "answers[0].type", "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage(), "must match \"TEXT|HTML|IMAGE|VIDEO|AUDIO|MEDIA\"", "Status Code");

            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }


    @Test(priority = 15)
    @AlmAnnotation(almTestId = "609861")
    public void CreateExpertQuestionWithNullValueforAnswersField() throws Exception {
        try {
            CreateExpertDeck();

            JSONObject createExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionCreatePayload);
            System.out.println(createExpertQuestionPayload);
            headerAuthor.put("x-Resource-Id", StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type", "expert");

            createExpertQuestionPayload.put("answers", null);

            createExpertQuestionPayload.put("creatorId",StoreControllerResponseDTO.getUserId());
            createExpertQuestionPayload.put("deckId",StoreControllerResponseDTO.getId());

            String payload = String.valueOf(createExpertQuestionPayload);
            ExpertQuestionResponseDTO = createExpertQuestion(headerAuthor, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");

            if (ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage().equals("answers' Cannot contain null values")) {
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getField(), "answers", "Status Code");
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage(), "answers' Cannot contain null values", "Status Code");
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[1].getField(), "answers", "Status Code");
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[1].getMessage(), "'answers' Cannot be null", "Status Code");
            } else {
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getField(), "answers", "Status Code");
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage(), "'answers' Cannot be null", "Status Code");
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[1].getMessage(), "answers' Cannot contain null values", "Status Code");
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[1].getField(), "answers", "Status Code");
            }
            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 16)
    @AlmAnnotation(almTestId = "609861")
    public void CreateExpertQuestionWithNullValueforTypeField() throws Exception {
        try {
            CreateExpertDeck();

            JSONObject createExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionCreatePayload);
            System.out.println(createExpertQuestionPayload);
            headerAuthor.put("x-Resource-Id", StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type", "expert");

            JSONObject answers1 = new JSONObject();
            answers1.put("caseSensitive", false);
            answers1.put("correct", false);
            answers1.put("id", 1);
            answers1.put("type", null);
            answers1.put("value", "a");


            JSONArray answers = new JSONArray();
            answers.add(answers1);

            createExpertQuestionPayload.put("answers", answers);

            createExpertQuestionPayload.put("creatorId",StoreControllerResponseDTO.getUserId());
            createExpertQuestionPayload.put("deckId",StoreControllerResponseDTO.getId());

            String payload = String.valueOf(createExpertQuestionPayload);
            ExpertQuestionResponseDTO = createExpertQuestion(headerAuthor, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getField(), "answers[0].type", "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage(), "must not be null", "Status Code");

            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 17)
    @AlmAnnotation(almTestId = "609861")
    public void CreateExpertQuestionWithNullValueforcreatorId() throws Exception {
        try {
            CreateExpertDeck();

            JSONObject createExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionCreatePayload);
            System.out.println(createExpertQuestionPayload);
            headerAuthor.put("x-Resource-Id", StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type", "expert");

            createExpertQuestionPayload.put("creatorId",null);
            createExpertQuestionPayload.put("deckId",StoreControllerResponseDTO.getId());

            String payload = String.valueOf(createExpertQuestionPayload);
            ExpertQuestionResponseDTO = createExpertQuestion(headerAuthor, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage(), "'creatorId' Cannot be null or empty", "Status Code");

            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 18)
    @AlmAnnotation(almTestId = "609861")
    public void CreateExpertQuestionWithEmptyValueforcreatorId() throws Exception {
        try {
            CreateExpertDeck();

            JSONObject createExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionCreatePayload);
            System.out.println(createExpertQuestionPayload);
            headerAuthor.put("x-Resource-Id", StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type", "expert");

            createExpertQuestionPayload.put("creatorId","");
            createExpertQuestionPayload.put("deckId",StoreControllerResponseDTO.getId());

            String payload = String.valueOf(createExpertQuestionPayload);
            ExpertQuestionResponseDTO = createExpertQuestion(headerAuthor, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage(), "'creatorId' Cannot be null or empty", "Status Code");

            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 19)
    @AlmAnnotation(almTestId = "609861")
    public void CreateExpertQuestionWithNullidField() throws Exception {
        try {
            CreateExpertDeck();

            JSONObject createExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionCreatePayload);
            System.out.println(createExpertQuestionPayload);
            headerAuthor.put("x-Resource-Id", StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type", "expert");

            JSONObject answers1 = new JSONObject();
            answers1.put("caseSensitive", false);
            answers1.put("correct", false);
            answers1.put("id", null);
            answers1.put("type", "TEXT");
            answers1.put("value", "a");

            JSONArray answers = new JSONArray();
            answers.add(answers1);

            createExpertQuestionPayload.put("answers", answers);

            createExpertQuestionPayload.put("creatorId",StoreControllerResponseDTO.getUserId());
            createExpertQuestionPayload.put("deckId",StoreControllerResponseDTO.getId());

            String payload = String.valueOf(createExpertQuestionPayload);
            ExpertQuestionResponseDTO = createExpertQuestion(headerAuthor, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getField(), "answers[0].id", "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage(), "Answers id should be a positive integer.", "Status Code");

            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 20)
    public void CreateExpertQuestionWithNullValueforcreatorPlatformField() throws Exception {
        try {
            CreateExpertDeck();

            JSONObject createExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionCreatePayload);
            System.out.println(createExpertQuestionPayload);

            headerAuthor.put("x-Resource-Id", StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type", "expert");

            createExpertQuestionPayload.put("creatorId",StoreControllerResponseDTO.getUserId());
            createExpertQuestionPayload.put("creatorPlatform", null);
            createExpertQuestionPayload.put("deckId",StoreControllerResponseDTO.getId());

            String payload = String.valueOf(createExpertQuestionPayload);
            ExpertQuestionResponseDTO = createExpertQuestion(headerAuthor, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getField(), "creatorPlatform", "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage(), "'creatorPlatform' Cannot be null or empty", "Status Code");

            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

   @Test(priority = 21)
    @AlmAnnotation(almTestId = "609861")
    public void CreateExpertQuestionWithEmptyValueforcreatorPlatformField() throws Exception {
        try {
            CreateExpertDeck();

            JSONObject createExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionCreatePayload);
            System.out.println(createExpertQuestionPayload);

            headerAuthor.put("x-Resource-Id", StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type", "expert");

            createExpertQuestionPayload.put("creatorId",StoreControllerResponseDTO.getUserId());
            createExpertQuestionPayload.put("creatorPlatform", "");
            createExpertQuestionPayload.put("deckId",StoreControllerResponseDTO.getId());

            String payload = String.valueOf(createExpertQuestionPayload);
            ExpertQuestionResponseDTO = createExpertQuestion(headerAuthor, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            if (ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage().equals("must match \"Mobile|Web|AuthoringTool\"")) {
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getField(), "creatorPlatform", "Status Code");
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage(), "must match \"Mobile|Web|AuthoringTool\"", "Status Code");
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[1].getField(), "creatorPlatform", "Status Code");
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[1].getMessage(), "'creatorPlatform' Cannot be null or empty", "Status Code");
            } else {
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getField(), "creatorPlatform", "Status Code");
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage(), "'creatorPlatform' Cannot be null or empty", "Status Code");
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[1].getMessage(), "must match \"Mobile|Web|AuthoringTool\"", "Status Code");
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[1].getField(), "creatorPlatform", "Status Code");
            }
            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 22)
    @AlmAnnotation(almTestId = "609861")
    public void CreateExpertQuestionWithNullValueforcreatoredSourceField() throws Exception {
        try {
            CreateExpertDeck();

            JSONObject createExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionCreatePayload);
            System.out.println(createExpertQuestionPayload);

            headerAuthor.put("x-Resource-Id", StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type", "expert");

            createExpertQuestionPayload.put("creatorId",StoreControllerResponseDTO.getUserId());
            createExpertQuestionPayload.put("creatoredSource", null);
            createExpertQuestionPayload.put("deckId",StoreControllerResponseDTO.getId());

            String payload = String.valueOf(createExpertQuestionPayload);
            ExpertQuestionResponseDTO = createExpertQuestion(headerAuthor, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getField(), "creatoredSource", "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage(), "'creatoredSource' Cannot be null or empty", "Status Code");

            DeleteExpertDeck();

             } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 23)
    @AlmAnnotation(almTestId = "609861")
    public void CreateExpertQuestionWithEmptyValueforcreatoredSourceField() throws Exception {
        try {
            CreateExpertDeck();

            JSONObject createExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionCreatePayload);
            System.out.println(createExpertQuestionPayload);

            headerAuthor.put("x-Resource-Id", StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type", "expert");

            createExpertQuestionPayload.put("creatorId",StoreControllerResponseDTO.getUserId());
            createExpertQuestionPayload.put("creatoredSource", "");
            createExpertQuestionPayload.put("deckId",StoreControllerResponseDTO.getId());

            String payload = String.valueOf(createExpertQuestionPayload);
            ExpertQuestionResponseDTO = createExpertQuestion(headerAuthor, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            if (ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage().equals("'creatoredSource' Cannot be null or empty")) {
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getField(), "creatoredSource", "Status Code");
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage(), "'creatoredSource' Cannot be null or empty", "Status Code");
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[1].getField(), "creatoredSource", "Status Code");
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[1].getMessage(), "must match \"Clipper|File|App|Gdrive\"", "Status Code");
            } else {
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getField(), "creatoredSource", "Status Code");
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage(), "must match \"Clipper|File|App|Gdrive\"", "Status Code");
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[1].getMessage(), "'creatoredSource' Cannot be null or empty", "Status Code");
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[1].getField(), "creatoredSource", "Status Code");
            }
            DeleteExpertDeck();

        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 24)
    @AlmAnnotation(almTestId = "609861")
    public void CreateExpertQuestionWithNullValueforcreatoredTypeField() throws Exception {
        try {
            CreateExpertDeck();

            JSONObject createExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionCreatePayload);
            System.out.println(createExpertQuestionPayload);

            headerAuthor.put("x-Resource-Id", StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type", "expert");

            createExpertQuestionPayload.put("creatorId",StoreControllerResponseDTO.getUserId());
            createExpertQuestionPayload.put("creatoredType", null);
            createExpertQuestionPayload.put("deckId",StoreControllerResponseDTO.getId());

            String payload = String.valueOf(createExpertQuestionPayload);
            ExpertQuestionResponseDTO = createExpertQuestion(headerAuthor, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getField(), "creatoredType", "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage(), "'creatoredType' Cannot be null or empty", "Status Code");

            DeleteExpertDeck();

        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 25)
    @AlmAnnotation(almTestId = "609861")
    public void CreateExpertQuestionWithEmptyValueforcreatoredTypeField() throws Exception {
        try {
            CreateExpertDeck();

            JSONObject createExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionCreatePayload);
            System.out.println(createExpertQuestionPayload);

            headerAuthor.put("x-Resource-Id", StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type", "expert");

            createExpertQuestionPayload.put("creatorId",StoreControllerResponseDTO.getUserId());
            createExpertQuestionPayload.put("creatoredType", "");
            createExpertQuestionPayload.put("deckId",StoreControllerResponseDTO.getId());

            String payload = String.valueOf(createExpertQuestionPayload);
            ExpertQuestionResponseDTO = createExpertQuestion(headerAuthor, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            if (ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage().equals("'creatoredType' Cannot be null or empty")) {
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getField(), "creatoredType", "Status Code");
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage(), "'creatoredType' Cannot be null or empty", "Status Code");
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[1].getField(), "creatoredType", "Status Code");
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[1].getMessage(), "must match \"Auto|Manual\"", "Status Code");
            } else {
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getField(), "creatoredType", "Status Code");
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage(), "must match \"Auto|Manual\"", "Status Code");
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[1].getMessage(), "'creatoredType' Cannot be null or empty", "Status Code");
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[1].getField(), "creatoredType", "Status Code");
            }
            DeleteExpertDeck();

        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 26)
    @AlmAnnotation(almTestId = "609861")
    public void CreateExpertQuestionWithNullValueforkindField() throws Exception {
        try {
            CreateExpertDeck();

            JSONObject createExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionCreatePayload);
            System.out.println(createExpertQuestionPayload);

            headerAuthor.put("x-Resource-Id", StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type", "expert");

            createExpertQuestionPayload.put("creatorId",StoreControllerResponseDTO.getUserId());
            createExpertQuestionPayload.put("kind", null);
            createExpertQuestionPayload.put("deckId",StoreControllerResponseDTO.getId());

            String payload = String.valueOf(createExpertQuestionPayload);
            ExpertQuestionResponseDTO = createExpertQuestion(headerAuthor, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getField(), "kind", "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage(), "'kind' Cannot be null or empty", "Status Code");

            DeleteExpertDeck();

        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 27)
    @AlmAnnotation(almTestId = "609861")
    public void CreateExpertQuestionWithEmptyValueforkindField() throws Exception {
        try {
            CreateExpertDeck();

            JSONObject createExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionCreatePayload);
            System.out.println(createExpertQuestionPayload);

            headerAuthor.put("x-Resource-Id", StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type", "expert");

            createExpertQuestionPayload.put("creatorId",StoreControllerResponseDTO.getUserId());
            createExpertQuestionPayload.put("kind", "");
            createExpertQuestionPayload.put("deckId",StoreControllerResponseDTO.getId());

            String payload = String.valueOf(createExpertQuestionPayload);
            ExpertQuestionResponseDTO = createExpertQuestion(headerAuthor, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            if (ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage().equals("'kind' Cannot be null or empty")) {
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getField(), "kind", "Status Code");
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage(), "'kind' Cannot be null or empty", "Status Code");
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[1].getField(), "kind", "Status Code");
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[1].getMessage(), "must match \"MULTIPLE_CHOICE|SHORT_ANSWER|ALL\"", "Status Code");
            } else {
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getField(), "kind", "Status Code");
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage(), "must match \"MULTIPLE_CHOICE|SHORT_ANSWER|ALL\"", "Status Code");
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[1].getMessage(), "'kind' Cannot be null or empty", "Status Code");
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[1].getField(), "kind", "Status Code");
            }
            DeleteExpertDeck();

        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 28)
    @AlmAnnotation(almTestId = "609861")
    public void CreateExpertQuestionWithNullValueforimageUrlField() throws Exception {
        try {
            CreateExpertDeck();

            JSONObject createExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionCreatePayload);
            System.out.println(createExpertQuestionPayload);
            headerAuthor.put("x-Resource-Id", StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type", "expert");

            JSONObject question1 = new JSONObject();
            question1.put("imageUrl", null);
            question1.put("media", "TEXT");
            question1.put("prompt", "a");
            question1.put("promptType", "TEXT");


            createExpertQuestionPayload.put("question", question1);
            createExpertQuestionPayload.put("creatorId",StoreControllerResponseDTO.getUserId());
            createExpertQuestionPayload.put("deckId",StoreControllerResponseDTO.getId());

            String payload = String.valueOf(createExpertQuestionPayload);
            ExpertQuestionResponseDTO = createExpertQuestion(headerAuthor, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getField(), "question.imageUrl", "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage(), "must not be null", "Status Code");

            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 29)
    @AlmAnnotation(almTestId = "609861")
    public void CreateExpertQuestionWithEmptymediaField() throws Exception {
        try {
            CreateExpertDeck();

            JSONObject createExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionCreatePayload);
            System.out.println(createExpertQuestionPayload);
            headerAuthor.put("x-Resource-Id", StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type", "expert");

            JSONObject question1 = new JSONObject();
            question1.put("imageUrl", "http://somehost/someimg.jpg");
            question1.put("media", "");
            question1.put("prompt", "a");
            question1.put("promptType", "TEXT");


            createExpertQuestionPayload.put("question", question1);
            createExpertQuestionPayload.put("creatorId",StoreControllerResponseDTO.getUserId());
            createExpertQuestionPayload.put("deckId",StoreControllerResponseDTO.getId());

            String payload = String.valueOf(createExpertQuestionPayload);
            ExpertQuestionResponseDTO = createExpertQuestion(headerAuthor, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getField(), "question.media", "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage(), "must match \"TEXT|HTML|IMAGE|VIDEO|AUDIO|MEDIA\"", "Status Code");

            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 30)
    @AlmAnnotation(almTestId = "609861")
    public void CreateExpertQuestionWithNullValueForpromptField() throws Exception {
        try {
            CreateExpertDeck();

            JSONObject createExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionCreatePayload);
            System.out.println(createExpertQuestionPayload);
            headerAuthor.put("x-Resource-Id", StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type", "expert");

            JSONObject question1 = new JSONObject();
            question1.put("imageUrl", "http://somehost/someimg.jpg");
            question1.put("media", "TEXT");
            question1.put("prompt", null);
            question1.put("promptType", "TEXT");


            createExpertQuestionPayload.put("question", question1);
            createExpertQuestionPayload.put("creatorId",StoreControllerResponseDTO.getUserId());
            createExpertQuestionPayload.put("deckId",StoreControllerResponseDTO.getId());

            String payload = String.valueOf(createExpertQuestionPayload);
            ExpertQuestionResponseDTO = createExpertQuestion(headerAuthor, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getField(), "question.prompt", "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage(), "'prompt' Cannot be null or empty", "Status Code");

            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 31)
    @AlmAnnotation(almTestId = "609861")
    public void CreateExpertQuestionWithEmptyValueForpromptField() throws Exception {
        try {
            CreateExpertDeck();

            JSONObject createExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionCreatePayload);
            System.out.println(createExpertQuestionPayload);
            headerAuthor.put("x-Resource-Id", StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type", "expert");

            JSONObject question1 = new JSONObject();
            question1.put("imageUrl", "http://somehost/someimg.jpg");
            question1.put("media", "TEXT");
            question1.put("prompt", "");
            question1.put("promptType", "TEXT");


            createExpertQuestionPayload.put("question", question1);
            createExpertQuestionPayload.put("creatorId",StoreControllerResponseDTO.getUserId());
            createExpertQuestionPayload.put("deckId",StoreControllerResponseDTO.getId());

            String payload = String.valueOf(createExpertQuestionPayload);
            ExpertQuestionResponseDTO = createExpertQuestion(headerAuthor, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getField(), "question.prompt", "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage(), "'prompt' Cannot be null or empty", "Status Code");

            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 32)
    @AlmAnnotation(almTestId = "609861")
    public void CreateExpertQuestionWithEmptyValueForpromptTypeField() throws Exception {
        try {
            CreateExpertDeck();

            JSONObject createExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionCreatePayload);
            System.out.println(createExpertQuestionPayload);
            headerAuthor.put("x-Resource-Id", StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type", "expert");

            JSONObject question1 = new JSONObject();
            question1.put("imageUrl", "http://somehost/someimg.jpg");
            question1.put("media", "TEXT");
            question1.put("prompt", "a");
            question1.put("promptType", "");


            createExpertQuestionPayload.put("question", question1);
            createExpertQuestionPayload.put("creatorId",StoreControllerResponseDTO.getUserId());
            createExpertQuestionPayload.put("deckId",StoreControllerResponseDTO.getId());

            String payload = String.valueOf(createExpertQuestionPayload);
            ExpertQuestionResponseDTO = createExpertQuestion(headerAuthor, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getField(), "question.promptType", "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage(), "must match \"HTML|TEXT|IMAGE|MEDIA\"", "Status Code");

            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 33)
    @AlmAnnotation(almTestId = "609861")
    public void UpdateExpertQuestionWithNullValueforTypeField() throws Exception {
        try {
            CreateExpertDeck();
            CreateExpertQuestion();

            JSONObject updateExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionUpdatePayload);
            System.out.println(updateExpertQuestionPayload);
            headerAuthor.put("x-Resource-Id", StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type", "expert");

            JSONObject answers1 = new JSONObject();
            answers1.put("caseSensitive", false);
            answers1.put("correct", false);
            answers1.put("id", 1);
            answers1.put("type", null);
            answers1.put("value", "a");


            JSONArray answers = new JSONArray();
            answers.add(answers1);

            updateExpertQuestionPayload.put("answers", answers);

            updateExpertQuestionPayload.put("creatorId",StoreControllerResponseDTO.getUserId());
            updateExpertQuestionPayload.put("deckId",StoreControllerResponseDTO.getId());

            String ExpertquestionId = ExpertQuestionResponseDTO.getId();
            String payload = String.valueOf(updateExpertQuestionPayload);

            ExpertQuestionResponseDTO = updateExpertQuestion(headerAuthor, payload, ExpertquestionId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getField(), "answers[0].type", "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage(), "must not be null", "Status Code");

            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 34)
    @AlmAnnotation(almTestId = "609861")
    public void UpdateExpertQuestionWithoutTypeField() throws Exception {
        try {
            CreateExpertDeck();
            CreateExpertQuestion();

            JSONObject updateExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionUpdatePayload);
            System.out.println(updateExpertQuestionPayload);
            headerAuthor.put("x-Resource-Id", StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type", "expert");

            JSONObject answers1 = new JSONObject();
            answers1.put("caseSensitive", false);
            answers1.put("correct", false);
            answers1.put("id", 1);
            answers1.put("type", "");
            answers1.put("value", "a");


            JSONArray answers = new JSONArray();
            answers.add(answers1);

            updateExpertQuestionPayload.put("answers", answers);

            updateExpertQuestionPayload.put("creatorId",StoreControllerResponseDTO.getUserId());
            updateExpertQuestionPayload.put("deckId",StoreControllerResponseDTO.getId());

            String ExpertquestionId = ExpertQuestionResponseDTO.getId();
            String payload = String.valueOf(updateExpertQuestionPayload);

            ExpertQuestionResponseDTO = updateExpertQuestion(headerAuthor, payload, ExpertquestionId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
           // Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getField(), "answers[0].type", "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage(), "must match \"TEXT|HTML|IMAGE|VIDEO|AUDIO|MEDIA\"", "Status Code");

            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 35)
    @AlmAnnotation(almTestId = "609861")
    public void UpdateExpertQuestionWithEmptyValueField() throws Exception {
        try {
            CreateExpertDeck();
            CreateExpertQuestion();

            JSONObject updateExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionUpdatePayload);
            System.out.println(updateExpertQuestionPayload);
            headerAuthor.put("x-Resource-Id", StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type", "expert");

            JSONObject answers1 = new JSONObject();
            answers1.put("caseSensitive", false);
            answers1.put("correct", false);
            answers1.put("id", 1);
            answers1.put("type", "TEXT");
            answers1.put("value", "");


            JSONArray answers = new JSONArray();
            answers.add(answers1);

            updateExpertQuestionPayload.put("answers", answers);

            updateExpertQuestionPayload.put("creatorId",StoreControllerResponseDTO.getUserId());
            updateExpertQuestionPayload.put("deckId",StoreControllerResponseDTO.getId());

            String ExpertquestionId = ExpertQuestionResponseDTO.getId();
            String payload = String.valueOf(updateExpertQuestionPayload);

            ExpertQuestionResponseDTO = updateExpertQuestion(headerAuthor, payload, ExpertquestionId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage(), "'value' Cannot be null or empty", "Status Code");

            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 36)
    @AlmAnnotation(almTestId = "609861")
    public void UpdateExpertQuestionWithNullValueField() throws Exception {
        try {
            CreateExpertDeck();
            CreateExpertQuestion();

            JSONObject updateExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionUpdatePayload);
            System.out.println(updateExpertQuestionPayload);
            headerAuthor.put("x-Resource-Id", StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type", "expert");

            JSONObject answers1 = new JSONObject();
            answers1.put("caseSensitive", false);
            answers1.put("correct", false);
            answers1.put("id", 1);
            answers1.put("type", "TEXT");
            answers1.put("value", null);


            JSONArray answers = new JSONArray();
            answers.add(answers1);

            updateExpertQuestionPayload.put("answers", answers);

            updateExpertQuestionPayload.put("creatorId",StoreControllerResponseDTO.getUserId());
            updateExpertQuestionPayload.put("deckId",StoreControllerResponseDTO.getId());

            String ExpertquestionId = ExpertQuestionResponseDTO.getId();
            String payload = String.valueOf(updateExpertQuestionPayload);

            ExpertQuestionResponseDTO = updateExpertQuestion(headerAuthor, payload, ExpertquestionId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage(), "'value' Cannot be null or empty", "Status Code");

            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 37)
    @AlmAnnotation(almTestId = "609861")
    public void UpdateExpertQuestionWithEmptyCreatorId() throws Exception {
        try {
            CreateExpertDeck();
            CreateExpertQuestion();

            JSONObject updateExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionUpdatePayload);
            System.out.println(updateExpertQuestionPayload);
            headerAuthor.put("x-Resource-Id", StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type", "expert");

           /* JSONObject answers1 = new JSONObject();
            answers1.put("caseSensitive", false);
            answers1.put("correct", false);
            answers1.put("id", 1);
            answers1.put("type", "TEXT");
            answers1.put("value", null);


            JSONArray answers = new JSONArray();
            answers.add(answers1);

            updateExpertQuestionPayload.put("answers", answers);*/

            updateExpertQuestionPayload.put("creatorId","");
            updateExpertQuestionPayload.put("deckId",StoreControllerResponseDTO.getId());

            String ExpertquestionId = ExpertQuestionResponseDTO.getId();
            String payload = String.valueOf(updateExpertQuestionPayload);

            ExpertQuestionResponseDTO = updateExpertQuestion(headerAuthor, payload, ExpertquestionId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage(), "'creatorId' Cannot be null or empty", "Status Code");

            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 38)
    @AlmAnnotation(almTestId = "609861")
    public void UpdateExpertQuestionWithNullCreatorId() throws Exception {
        try {
            CreateExpertDeck();
            CreateExpertQuestion();

            JSONObject updateExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionUpdatePayload);
            System.out.println(updateExpertQuestionPayload);
            headerAuthor.put("x-Resource-Id", StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type", "expert");

           /* JSONObject answers1 = new JSONObject();
            answers1.put("caseSensitive", false);
            answers1.put("correct", false);
            answers1.put("id", 1);
            answers1.put("type", "TEXT");
            answers1.put("value", null);


            JSONArray answers = new JSONArray();
            answers.add(answers1);

            updateExpertQuestionPayload.put("answers", answers);*/

            updateExpertQuestionPayload.put("creatorId",null);
            updateExpertQuestionPayload.put("deckId",StoreControllerResponseDTO.getId());

            String ExpertquestionId = ExpertQuestionResponseDTO.getId();
            String payload = String.valueOf(updateExpertQuestionPayload);

            ExpertQuestionResponseDTO = updateExpertQuestion(headerAuthor, payload, ExpertquestionId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage(), "'creatorId' Cannot be null or empty", "Status Code");

            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 39)
    @AlmAnnotation(almTestId = "609861")
    public void UpdateExpertQuestionWithNullValueforcreatorPlatformField() throws Exception {
        try {
            CreateExpertDeck();
            CreateExpertQuestion();

            JSONObject updateExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionUpdatePayload);
            System.out.println(updateExpertQuestionPayload);
            headerAuthor.put("x-Resource-Id", StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type", "expert");

           /* JSONObject answers1 = new JSONObject();
            answers1.put("caseSensitive", false);
            answers1.put("correct", false);
            answers1.put("id", 1);
            answers1.put("type", "TEXT");
            answers1.put("value", null);


            JSONArray answers = new JSONArray();
            answers.add(answers1);

            updateExpertQuestionPayload.put("answers", answers);*/

            updateExpertQuestionPayload.put("creatorId",StoreControllerResponseDTO.getUserId());
            updateExpertQuestionPayload.put("creatorPlatform", null);
            updateExpertQuestionPayload.put("deckId",StoreControllerResponseDTO.getId());

            String ExpertquestionId = ExpertQuestionResponseDTO.getId();
            String payload = String.valueOf(updateExpertQuestionPayload);

            ExpertQuestionResponseDTO = updateExpertQuestion(headerAuthor, payload, ExpertquestionId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getField(), "creatorPlatform", "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage(), "'creatorPlatform' Cannot be null or empty", "Status Code");

            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 40)
    @AlmAnnotation(almTestId = "609861")
    public void UpdateExpertQuestionWithEmptyValueforcreatorPlatformField() throws Exception {
        try {
            CreateExpertDeck();
            CreateExpertQuestion();

            JSONObject updateExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionUpdatePayload);
            System.out.println(updateExpertQuestionPayload);
            headerAuthor.put("x-Resource-Id", StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type", "expert");

           /* JSONObject answers1 = new JSONObject();
            answers1.put("caseSensitive", false);
            answers1.put("correct", false);
            answers1.put("id", 1);
            answers1.put("type", "TEXT");
            answers1.put("value", null);


            JSONArray answers = new JSONArray();
            answers.add(answers1);

            updateExpertQuestionPayload.put("answers", answers);*/

            updateExpertQuestionPayload.put("creatorId",StoreControllerResponseDTO.getUserId());
            updateExpertQuestionPayload.put("creatorPlatform", "");
            updateExpertQuestionPayload.put("deckId",StoreControllerResponseDTO.getId());

            String ExpertquestionId = ExpertQuestionResponseDTO.getId();
            String payload = String.valueOf(updateExpertQuestionPayload);

            ExpertQuestionResponseDTO = updateExpertQuestion(headerAuthor, payload, ExpertquestionId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            if (ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage().equals("must match \"Mobile|Web|AuthoringTool\"")) {
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getField(), "creatorPlatform", "Status Code");
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage(), "must match \"Mobile|Web|AuthoringTool\"", "Status Code");
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[1].getField(), "creatorPlatform", "Status Code");
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[1].getMessage(), "'creatorPlatform' Cannot be null or empty", "Status Code");
            } else {
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getField(), "creatorPlatform", "Status Code");
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage(), "'creatorPlatform' Cannot be null or empty", "Status Code");
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[1].getMessage(), "must match \"Mobile|Web|AuthoringTool\"", "Status Code");
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[1].getField(), "creatorPlatform", "Status Code");
            }

            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 41)
    @AlmAnnotation(almTestId = "609861")
    public void UpdateExpertQuestionWithNullValueforcreatoredSourceField() throws Exception {
        try {
            CreateExpertDeck();
            CreateExpertQuestion();

            JSONObject updateExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionUpdatePayload);
            System.out.println(updateExpertQuestionPayload);
            headerAuthor.put("x-Resource-Id", StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type", "expert");

           /* JSONObject answers1 = new JSONObject();
            answers1.put("caseSensitive", false);
            answers1.put("correct", false);
            answers1.put("id", 1);
            answers1.put("type", "TEXT");
            answers1.put("value", null);


            JSONArray answers = new JSONArray();
            answers.add(answers1);

            updateExpertQuestionPayload.put("answers", answers);*/

            updateExpertQuestionPayload.put("creatorId",StoreControllerResponseDTO.getUserId());
            updateExpertQuestionPayload.put("creatoredSource", null);
            updateExpertQuestionPayload.put("deckId",StoreControllerResponseDTO.getId());

            String ExpertquestionId = ExpertQuestionResponseDTO.getId();
            String payload = String.valueOf(updateExpertQuestionPayload);

            ExpertQuestionResponseDTO = updateExpertQuestion(headerAuthor, payload, ExpertquestionId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getField(), "creatoredSource", "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage(), "'creatoredSource' Cannot be null or empty", "Status Code");

            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 42)
    @AlmAnnotation(almTestId = "609861")
    public void UpdateExpertQuestionWithEmptyValueforcreatoredSourceField() throws Exception {
        try {
            CreateExpertDeck();
            CreateExpertQuestion();

            JSONObject updateExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionUpdatePayload);
            System.out.println(updateExpertQuestionPayload);
            headerAuthor.put("x-Resource-Id", StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type", "expert");

           /* JSONObject answers1 = new JSONObject();
            answers1.put("caseSensitive", false);
            answers1.put("correct", false);
            answers1.put("id", 1);
            answers1.put("type", "TEXT");
            answers1.put("value", null);


            JSONArray answers = new JSONArray();
            answers.add(answers1);

            updateExpertQuestionPayload.put("answers", answers);*/

            updateExpertQuestionPayload.put("creatorId",StoreControllerResponseDTO.getUserId());
            updateExpertQuestionPayload.put("creatoredSource", "");
            updateExpertQuestionPayload.put("deckId",StoreControllerResponseDTO.getId());

            String ExpertquestionId = ExpertQuestionResponseDTO.getId();
            String payload = String.valueOf(updateExpertQuestionPayload);

            ExpertQuestionResponseDTO = updateExpertQuestion(headerAuthor, payload, ExpertquestionId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getField(), "creatoredSource", "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage(), "'creatoredSource' Cannot be null or empty", "Status Code");

            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 43)
    @AlmAnnotation(almTestId = "609861")
    public void UpdateExpertQuestionWithNullDeckId() throws Exception {
        try {
            CreateExpertDeck();
            CreateExpertQuestion();

            JSONObject updateExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionUpdatePayload);
            System.out.println(updateExpertQuestionPayload);
            headerAuthor.put("x-Resource-Id", StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type", "expert");

           /* JSONObject answers1 = new JSONObject();
            answers1.put("caseSensitive", false);
            answers1.put("correct", false);
            answers1.put("id", 1);
            answers1.put("type", "TEXT");
            answers1.put("value", null);


            JSONArray answers = new JSONArray();
            answers.add(answers1);

            updateExpertQuestionPayload.put("answers", answers);*/

            updateExpertQuestionPayload.put("creatorId",StoreControllerResponseDTO.getUserId());
            updateExpertQuestionPayload.put("deckId",null);

            String ExpertquestionId = ExpertQuestionResponseDTO.getId();
            String payload = String.valueOf(updateExpertQuestionPayload);

            ExpertQuestionResponseDTO = updateExpertQuestion(headerAuthor, payload, ExpertquestionId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage(), "'deckId' Cannot be null or empty", "Status Code");


            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 44)
    @AlmAnnotation(almTestId = "609861")
    public void UpdateExpertQuestionWithEmptyDeckId() throws Exception {
        try {
            CreateExpertDeck();
            CreateExpertQuestion();

            JSONObject updateExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionUpdatePayload);
            System.out.println(updateExpertQuestionPayload);
            headerAuthor.put("x-Resource-Id", StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type", "expert");

           /* JSONObject answers1 = new JSONObject();
            answers1.put("caseSensitive", false);
            answers1.put("correct", false);
            answers1.put("id", 1);
            answers1.put("type", "TEXT");
            answers1.put("value", null);


            JSONArray answers = new JSONArray();
            answers.add(answers1);

            updateExpertQuestionPayload.put("answers", answers);*/

            updateExpertQuestionPayload.put("creatorId",StoreControllerResponseDTO.getUserId());
            updateExpertQuestionPayload.put("deckId","");

            String ExpertquestionId = ExpertQuestionResponseDTO.getId();
            String payload = String.valueOf(updateExpertQuestionPayload);

            ExpertQuestionResponseDTO = updateExpertQuestion(headerAuthor, payload, ExpertquestionId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            if (ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage().equals("'deckId' Cannot be null or empty")) {
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getField(), "deckId", "Status Code");
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage(), "'deckId' Cannot be null or empty", "Status Code");
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[1].getField(), "deckId", "Status Code");
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[1].getMessage(), "must match \"^[0-9a-fA-F]{24}$\"", "Status Code");
            } else {
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getField(), "deckId", "Status Code");
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage(), "must match \"^[0-9a-fA-F]{24}$\"", "Status Code");
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[1].getMessage(), "'deckId' Cannot be null or empty", "Status Code");
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[1].getField(), "deckId", "Status Code");
            }
            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 45)
    @AlmAnnotation(almTestId = "609861")
    public void UpdateExpertQuestionWithDeletedDeckId() throws Exception {
        try {
            CreateExpertDeck();
            CreateExpertQuestion();

            JSONObject updateExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionUpdatePayload);
            System.out.println(updateExpertQuestionPayload);
            headerAuthor.put("x-Resource-Id", StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type", "expert");

           /* JSONObject answers1 = new JSONObject();
            answers1.put("caseSensitive", false);
            answers1.put("correct", false);
            answers1.put("id", 1);
            answers1.put("type", "TEXT");
            answers1.put("value", null);


            JSONArray answers = new JSONArray();
            answers.add(answers1);

            updateExpertQuestionPayload.put("answers", answers);*/

            updateExpertQuestionPayload.put("creatorId",StoreControllerResponseDTO.getUserId());
            updateExpertQuestionPayload.put("deckId",StoreControllerResponseDTO.getId());

            DeleteExpertDeck();
            String ExpertquestionId = ExpertQuestionResponseDTO.getId();
            String payload = String.valueOf(updateExpertQuestionPayload);

            ExpertQuestionResponseDTO = updateExpertQuestion(headerAuthor, payload, ExpertquestionId);

            Assert.assertEquals(StatusCode, 403, "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getDescription(), "Request does not contain valid information to proceed", "Status Code");

            //DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 46)
    @AlmAnnotation(almTestId = "609861")
    public void UpdateExpertQuestionWithDifferentUser() throws Exception {
        try {
            CreateExpertDeck();
            CreateExpertQuestion();

            JSONObject updateExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionUpdatePayload);
            System.out.println(updateExpertQuestionPayload);
            headerNormal.put("x-Resource-Id", StoreControllerResponseDTO.getId());
            headerNormal.put("x-Resource-Type", "expert");

           /* JSONObject answers1 = new JSONObject();
            answers1.put("caseSensitive", false);
            answers1.put("correct", false);
            answers1.put("id", 1);
            answers1.put("type", "TEXT");
            answers1.put("value", null);


            JSONArray answers = new JSONArray();
            answers.add(answers1);

            updateExpertQuestionPayload.put("answers", answers);*/

            updateExpertQuestionPayload.put("creatorId",StoreControllerResponseDTO.getUserId());
            updateExpertQuestionPayload.put("deckId",StoreControllerResponseDTO.getId());

            String ExpertquestionId = ExpertQuestionResponseDTO.getId();
            String payload = String.valueOf(updateExpertQuestionPayload);

            ExpertQuestionResponseDTO = updateExpertQuestion(headerNormal, payload, ExpertquestionId);

            Assert.assertEquals(StatusCode, 403, "Status Code"); //"error.dependencyFailed"
            Assert.assertEquals(ExpertQuestionResponseDTO.getDescription(), "You do not have AUTHORING_TOOL_USER permission to access this method", "Status Code");


            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }


    @Test(priority = 47)
    @AlmAnnotation(almTestId = "609861")
    public void UpdateExpertQuestionWithNullValueforimageUrlField() throws Exception {
        try {
            CreateExpertDeck();
            CreateExpertQuestion();

            JSONObject updateExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionUpdatePayload);
            System.out.println(updateExpertQuestionPayload);
            headerAuthor.put("x-Resource-Id", StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type", "expert");

            JSONObject question1 = new JSONObject();
            question1.put("imageUrl", null);
            question1.put("media", "TEXT");
            question1.put("prompt", "a");
            question1.put("promptType", "TEXT");


            updateExpertQuestionPayload.put("question", question1);

            updateExpertQuestionPayload.put("creatorId",StoreControllerResponseDTO.getUserId());
            updateExpertQuestionPayload.put("deckId",StoreControllerResponseDTO.getId());

            String ExpertquestionId = ExpertQuestionResponseDTO.getId();
            String payload = String.valueOf(updateExpertQuestionPayload);

            ExpertQuestionResponseDTO = updateExpertQuestion(headerAuthor, payload, ExpertquestionId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getField(), "question.imageUrl", "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage(), "must not be null", "Status Code");
            DeleteExpertDeck();

        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 48)
    @AlmAnnotation(almTestId = "609861")
    public void UpdateExpertQuestionWithEmptymediaField() throws Exception {
        try {
            CreateExpertDeck();
            CreateExpertQuestion();

            JSONObject updateExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionUpdatePayload);
            System.out.println(updateExpertQuestionPayload);
            headerAuthor.put("x-Resource-Id", StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type", "expert");

            JSONObject question1 = new JSONObject();
            question1.put("imageUrl", "http://somehost/someimg.jpg");
            question1.put("media", "");
            question1.put("prompt", "a");
            question1.put("promptType", "TEXT");

            updateExpertQuestionPayload.put("question", question1);

            updateExpertQuestionPayload.put("creatorId",StoreControllerResponseDTO.getUserId());
            updateExpertQuestionPayload.put("deckId",StoreControllerResponseDTO.getId());

            String ExpertquestionId = ExpertQuestionResponseDTO.getId();
            String payload = String.valueOf(updateExpertQuestionPayload);

            ExpertQuestionResponseDTO = updateExpertQuestion(headerAuthor, payload, ExpertquestionId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getField(), "question.media", "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage(), "must match \"TEXT|HTML|IMAGE|VIDEO|AUDIO|MEDIA\"", "Status Code");

            DeleteExpertDeck();

        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 48)
    @AlmAnnotation(almTestId = "609861")
    public void UpdateExpertQuestionWithNullValueForpromptField() throws Exception {
        try {
            CreateExpertDeck();
            CreateExpertQuestion();

            JSONObject updateExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionUpdatePayload);
            System.out.println(updateExpertQuestionPayload);
            headerAuthor.put("x-Resource-Id", StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type", "expert");

            JSONObject question1 = new JSONObject();
            question1.put("imageUrl", "http://somehost/someimg.jpg");
            question1.put("media", "TEXT");
            question1.put("prompt", null);
            question1.put("promptType", "TEXT");

            updateExpertQuestionPayload.put("question", question1);

            updateExpertQuestionPayload.put("creatorId",StoreControllerResponseDTO.getUserId());
            updateExpertQuestionPayload.put("deckId",StoreControllerResponseDTO.getId());

            String ExpertquestionId = ExpertQuestionResponseDTO.getId();
            String payload = String.valueOf(updateExpertQuestionPayload);

            ExpertQuestionResponseDTO = updateExpertQuestion(headerAuthor, payload, ExpertquestionId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getField(), "question.prompt", "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage(), "'prompt' Cannot be null or empty", "Status Code");
            DeleteExpertDeck();

        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 49)
    @AlmAnnotation(almTestId = "609861")
    public void UpdateExpertQuestionWithEmptyValueForpromptField() throws Exception {
        try {
            CreateExpertDeck();
            CreateExpertQuestion();

            JSONObject updateExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionUpdatePayload);
            System.out.println(updateExpertQuestionPayload);
            headerAuthor.put("x-Resource-Id", StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type", "expert");

            JSONObject question1 = new JSONObject();
            question1.put("imageUrl", "http://somehost/someimg.jpg");
            question1.put("media", "TEXT");
            question1.put("prompt", "");
            question1.put("promptType", "TEXT");

            updateExpertQuestionPayload.put("question", question1);

            updateExpertQuestionPayload.put("creatorId",StoreControllerResponseDTO.getUserId());
            updateExpertQuestionPayload.put("deckId",StoreControllerResponseDTO.getId());

            String ExpertquestionId = ExpertQuestionResponseDTO.getId();
            String payload = String.valueOf(updateExpertQuestionPayload);

            ExpertQuestionResponseDTO = updateExpertQuestion(headerAuthor, payload, ExpertquestionId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getField(), "question.prompt", "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage(), "'prompt' Cannot be null or empty", "Status Code");
            DeleteExpertDeck();

        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 50)
    @AlmAnnotation(almTestId = "609861")
    public void UpdateExpertQuestionWithEmptyValueForpromptTypeField() throws Exception {
        try {
            CreateExpertDeck();
            CreateExpertQuestion();

            JSONObject updateExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionUpdatePayload);
            System.out.println(updateExpertQuestionPayload);
            headerAuthor.put("x-Resource-Id", StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type", "expert");

            JSONObject question1 = new JSONObject();
            question1.put("imageUrl", "http://somehost/someimg.jpg");
            question1.put("media", "TEXT");
            question1.put("prompt", "a");
            question1.put("promptType", "");

            updateExpertQuestionPayload.put("question", question1);

            updateExpertQuestionPayload.put("creatorId",StoreControllerResponseDTO.getUserId());
            updateExpertQuestionPayload.put("deckId",StoreControllerResponseDTO.getId());

            String ExpertquestionId = ExpertQuestionResponseDTO.getId();
            String payload = String.valueOf(updateExpertQuestionPayload);

            ExpertQuestionResponseDTO = updateExpertQuestion(headerAuthor, payload, ExpertquestionId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getField(), "question.promptType", "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage(), "must match \"HTML|TEXT|IMAGE|MEDIA\"", "Status Code");

            DeleteExpertDeck();

        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 51)
    @AlmAnnotation(almTestId = "609861")
    public void UpdateExpertQuestionWithNullidField() throws Exception {
        try {
            CreateExpertDeck();
            CreateExpertQuestion();

            JSONObject updateExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionUpdatePayload);
            System.out.println(updateExpertQuestionPayload);
            headerAuthor.put("x-Resource-Id", StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type", "expert");

            JSONObject answers1 = new JSONObject();
            answers1.put("caseSensitive", false);
            answers1.put("correct", false);
            answers1.put("id", null);
            answers1.put("type", "TEXT");
            answers1.put("value", "a");

            JSONArray answers = new JSONArray();
            answers.add(answers1);

            updateExpertQuestionPayload.put("answers", answers);

            updateExpertQuestionPayload.put("creatorId",StoreControllerResponseDTO.getUserId());
            updateExpertQuestionPayload.put("deckId",StoreControllerResponseDTO.getId());

            String ExpertquestionId = ExpertQuestionResponseDTO.getId();
            String payload = String.valueOf(updateExpertQuestionPayload);

            ExpertQuestionResponseDTO = updateExpertQuestion(headerAuthor, payload, ExpertquestionId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getField(), "answers[].id", "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage(), "Answers id should be a positive integer.", "Status Code");
            DeleteExpertDeck();

        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 52)
    @AlmAnnotation(almTestId = "609861")
    public void UpdateExpertQuestionWithNullValueforkindField() throws Exception {
        try {
            CreateExpertDeck();
            CreateExpertQuestion();

            JSONObject updateExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionUpdatePayload);
            System.out.println(updateExpertQuestionPayload);
            headerAuthor.put("x-Resource-Id", StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type", "expert");

            updateExpertQuestionPayload.put("creatorId",StoreControllerResponseDTO.getUserId());
            updateExpertQuestionPayload.put("kind", null);
            updateExpertQuestionPayload.put("deckId",StoreControllerResponseDTO.getId());

            String ExpertquestionId = ExpertQuestionResponseDTO.getId();
            String payload = String.valueOf(updateExpertQuestionPayload);

            ExpertQuestionResponseDTO = updateExpertQuestion(headerAuthor, payload, ExpertquestionId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getField(), "kind", "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage(), "'kind' Cannot be null or empty", "Status Code");

            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 53)
    @AlmAnnotation(almTestId = "609861")
    public void UpdateExpertQuestionWithEmptyValueforkindField() throws Exception {
        try {
            CreateExpertDeck();
            CreateExpertQuestion();

            JSONObject updateExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionUpdatePayload);
            System.out.println(updateExpertQuestionPayload);
            headerAuthor.put("x-Resource-Id", StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type", "expert");

            updateExpertQuestionPayload.put("creatorId",StoreControllerResponseDTO.getUserId());
            updateExpertQuestionPayload.put("kind", "");
            updateExpertQuestionPayload.put("deckId",StoreControllerResponseDTO.getId());

            String ExpertquestionId = ExpertQuestionResponseDTO.getId();
            String payload = String.valueOf(updateExpertQuestionPayload);

            ExpertQuestionResponseDTO = updateExpertQuestion(headerAuthor, payload, ExpertquestionId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            if (ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage().equals("'kind' Cannot be null or empty")) {
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getField(), "kind", "Status Code");
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage(), "'kind' Cannot be null or empty", "Status Code");
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[1].getField(), "kind", "Status Code");
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[1].getMessage(), "must match \"MULTIPLE_CHOICE|SHORT_ANSWER|ALL\"", "Status Code");
            } else {
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getField(), "kind", "Status Code");
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[0].getMessage(), "must match \"MULTIPLE_CHOICE|SHORT_ANSWER|ALL\"", "Status Code");
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[1].getMessage(), "'kind' Cannot be null or empty", "Status Code");
                Assert.assertEquals(ExpertQuestionResponseDTO.getFieldErrors()[1].getField(), "kind", "Status Code");
            }

            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 54) @AlmAnnotation(almTestId = "")
    public void GetExpertQuestionWithNullAuthorization() throws Exception {
        try {
            getData();
            CreateExpertDeck();
           // CreateExpertQuestion();
            // Read json object from file
            // JSONObject GetExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionGetPayload);
            // System.out.println(GetExpertQuestionPayload);

            headerAuthor.put("X-Authorization", null);
            headerAuthor.put("x-Resource-Id", StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type", "expert");

           // String ExpertquestionId = ExpertQuestionResponseDTO.getId();
            ExpertQuestionResponseDTO = getExpertQuestion(headerAuthor, "635756f61fbbc078c0df098e");

            Assert.assertEquals(StatusCode, 401, "Status code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getError(), "Unauthorized", "Status Code");

            //DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 55) @AlmAnnotation(almTestId = "")
    public void GetExpertQuestionWithInvalidAuthorization() throws Exception {
        try {
            getData();
            CreateExpertDeck();
           // CreateExpertQuestion();
            // Read json object from file
            // JSONObject GetExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionGetPayload);
            // System.out.println(GetExpertQuestionPayload);

            headerAuthor.put("X-Authorization", "abc");
            headerAuthor.put("x-Resource-Id", StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type", "expert");

           // String ExpertquestionId = ExpertQuestionResponseDTO.getId();
            ExpertQuestionResponseDTO = getExpertQuestion(headerAuthor, "635756f61fbbc078c0df098e");

            Assert.assertEquals(StatusCode, 401, "Status code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getError(), "Unauthorized", "Status Code");

           // DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 56) @AlmAnnotation(almTestId = "")
    public void GetExpertQuestionWithNullResourceId() throws Exception {
        try {
            getData();
            CreateExpertDeck();
           // CreateExpertQuestion();
            // Read json object from file
            // JSONObject GetExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionGetPayload);
            // System.out.println(GetExpertQuestionPayload);

            headerAuthor.put("x-Resource-Id", null);
            headerAuthor.put("x-Resource-Type", "expert");

           // String ExpertquestionId = ExpertQuestionResponseDTO.getId();
            ExpertQuestionResponseDTO = getExpertQuestion(headerAuthor, "635756f61fbbc078c0df098e");

            Assert.assertEquals(StatusCode, 403, "Status code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getMessage(), "error.dependencyFailed", "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getDescription(), "Request does not contain valid information to proceed", "Status Code");

            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 57) @AlmAnnotation(almTestId = "")
    public void GetExpertQuestionWithInvalidResourceId() throws Exception {
        try {
            getData();
            CreateExpertDeck();
         //   CreateExpertQuestion();
            // Read json object from file
            // JSONObject GetExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionGetPayload);
            // System.out.println(GetExpertQuestionPayload);

            headerAuthor.put("x-Resource-Id", "abc");
            headerAuthor.put("x-Resource-Type", "expert");

           // String ExpertquestionId = ExpertQuestionResponseDTO.getId();
            ExpertQuestionResponseDTO = getExpertQuestion(headerAuthor, "635756f61fbbc078c0df098e");

            Assert.assertEquals(StatusCode, 403, "Status code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getMessage(), "error.dependencyFailed", "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getDescription(), "Request does not contain valid information to proceed", "Status Code");

            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 58) @AlmAnnotation(almTestId = "")
    public void GetExpertQuestionWithoutQuestionId() throws Exception {
        try {

            getData();
            CreateExpertDeck();
            //CreateExpertQuestion();


            headerAuthor.put("x-Resource-Id",StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type", "expert");

            String ExpertquestionId = "" ;
            ExpertQuestionResponseDTO = getExpertQuestion(headerAuthor, ExpertquestionId);


            Assert.assertEquals(StatusCode, 400, "Status code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getMessage(), "error.validation", "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getDescription(), "Required request parameter 'deck' for method parameter type String is not present", "Status Code");

            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 59) @AlmAnnotation(almTestId = "")
    public void GetExpertQuestionWithInvalidQuestionId() throws Exception {
        try {
            getData();
            CreateExpertDeck();
          //  CreateExpertQuestion();
            // Read json object from file
            // JSONObject GetExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionGetPayload);
            // System.out.println(GetExpertQuestionPayload);

            headerAuthor.put("x-Resource-Id",StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type", "expert");

            String ExpertquestionId ="abc";
            ExpertQuestionResponseDTO = getExpertQuestion(headerAuthor,ExpertquestionId );

            Assert.assertEquals(StatusCode, 404, "Status code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getMessage(), "error.notFound", "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getDescription(), "Couldn't find question with id : " + ExpertquestionId, "Status Code");

            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 60) @AlmAnnotation(almTestId = "")
    public void GetExpertQuestionFromDeletedDeck() throws Exception {
        try {
            getData();
            CreateExpertDeck();
           // CreateExpertQuestion();

            headerAuthor.put("x-Resource-Id", StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type", "expert");

            DeleteExpertDeck();
            // Read json object from file
            // JSONObject GetExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionGetPayload);
            // System.out.println(GetExpertQuestionPayload);



           // String ExpertquestionId = ExpertQuestionResponseDTO.getId();
            ExpertQuestionResponseDTO = getExpertQuestion(headerAuthor, "635756f61fbbc078c0df098e");

            Assert.assertEquals(StatusCode, 403, "Status code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getMessage(), "error.dependencyFailed", "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getDescription(), "Request does not contain valid information to proceed", "Status Code");


        } catch (Exception  e) {
            throw e;
        }
    }

    @Test(priority = 61) @AlmAnnotation(almTestId = "")
    public void DeleteExpertQuestionWithNullAuthorization() throws Exception {
        try {
            getData();
            CreateExpertDeck();
            // CreateExpertQuestion();
            // Read json object from file
            // JSONObject GetExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionGetPayload);
            // System.out.println(GetExpertQuestionPayload);

            headerAuthor.put("X-Authorization", null);
            headerAuthor.put("x-Resource-Id", StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type", "expert");

            // String ExpertquestionId = ExpertQuestionResponseDTO.getId();
            ExpertQuestionResponseDTO = deleteExpertQuestion(headerAuthor, "635756f61fbbc078c0df098e");

            Assert.assertEquals(StatusCode, 401, "Status code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getError(), "Unauthorized", "Status Code");

           // DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 62) @AlmAnnotation(almTestId = "")
    public void DeleteExpertQuestionWithInvalidAuthorization() throws Exception {
        try {
            getData();
            CreateExpertDeck();
            // CreateExpertQuestion();
            // Read json object from file
            // JSONObject GetExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionGetPayload);
            // System.out.println(GetExpertQuestionPayload);

            headerAuthor.put("X-Authorization", "abc");
            headerAuthor.put("x-Resource-Id", StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type", "expert");

            // String ExpertquestionId = ExpertQuestionResponseDTO.getId();
            ExpertQuestionResponseDTO = getExpertQuestion(headerAuthor, "635756f61fbbc078c0df098e");

            Assert.assertEquals(StatusCode, 401, "Status code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getError(), "Unauthorized", "Status Code");

           // DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 63) @AlmAnnotation(almTestId = "")
    public void DeleteExpertQuestionWithoutQuestionId() throws Exception {
        try {
            getData();
            CreateExpertDeck();
            // CreateExpertQuestion();
            // Read json object from file
            // JSONObject GetExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionGetPayload);
            // System.out.println(GetExpertQuestionPayload);

            headerAuthor.put("x-Resource-Id", StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type", "expert");

            //String ExpertquestionId = "";
            ExpertQuestionResponseDTO = deleteExpertQuestion(headerAuthor, "");

            Assert.assertEquals(StatusCode, 405, "Status code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getError(), "Method Not Allowed", "Status Code");

            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 64) @AlmAnnotation(almTestId = "")
    public void DeleteExpertQuestionWithInvalidQuestionId() throws Exception {
        try {
            getData();
            CreateExpertDeck();
            // CreateExpertQuestion();
            // Read json object from file
            // JSONObject GetExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionGetPayload);
            // System.out.println(GetExpertQuestionPayload);

            headerAuthor.put("x-Resource-Id", StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type", "expert");

            String ExpertquestionId = "abc";
            ExpertQuestionResponseDTO = deleteExpertQuestion(headerAuthor, ExpertquestionId);

            Assert.assertEquals(StatusCode, 404, "Status code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getMessage(), "error.notFound", "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getDescription(), "Couldn't find question with id : " + ExpertquestionId, "Status Code");

            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 65) @AlmAnnotation(almTestId = "")
    public void DeleteExpertQuestionFromDeletedDeck() throws Exception {
        try {
            getData();
            CreateExpertDeck();
            // CreateExpertQuestion();
            // Read json object from file
            // JSONObject GetExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionGetPayload);
            // System.out.println(GetExpertQuestionPayload);

            headerAuthor.put("x-Resource-Id", StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type", "expert");

            DeleteExpertDeck();

            String ExpertquestionId = "635756f61fbbc078c0df098e";
            ExpertQuestionResponseDTO = deleteExpertQuestion(headerAuthor, ExpertquestionId);

            Assert.assertEquals(StatusCode, 403, "Status code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getMessage(), "error.dependencyFailed", "Status Code");
            Assert.assertEquals(ExpertQuestionResponseDTO.getDescription(), "Request does not contain valid information to proceed", "Status Code");

        } catch (Exception e) {
            throw e;
        }
    }
}