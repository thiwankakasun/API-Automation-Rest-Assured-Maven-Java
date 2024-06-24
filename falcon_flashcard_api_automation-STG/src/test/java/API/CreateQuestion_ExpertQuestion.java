package API;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.pearson.common.framework.shared.alm.AlmAnnotation;
import org.apache.poi.EncryptedDocumentException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pojos.PiToken;
import responseDTO.DeckResponseDTO;
import utils.JsonReader;
import requestDTO.StoreControllerRequestDTO;
import responseDTO.*;
import utils.JsonReader;
import utils.PiTokenGenarator;
import utils.PiTokenGenaratorAuthor;
import utils.PiTokenGenaratorNormalUser;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.logging.Logger;


public class CreateQuestion_ExpertQuestion extends ServiceController {

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
//            PiToken piToken = PiTokenGenarator.generatePiToken(loginUrl);
//            header.put("X-Authorization", piToken.getData());
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
}
