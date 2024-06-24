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
import responseDTO.*;
import utils.JsonReader;
import utils.PiTokenGenarator;
import utils.PiTokenGenaratorAuthor;
import utils.PiTokenGenaratorNormalUser;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

public class UpdateQuestion_ExpertQuestion extends ServiceController{

    private responseDTO.StoreControllerResponseDTO StoreControllerResponseDTO;
    private DeckResponseDTO ProvisionedDeckDTO;
    private responseDTO.ExpertQuestionResponseDTO ExpertQuestionResponseDTO;
    private responseDTO.GetRecommendationResponseDTO GetRecommendationResponseDTO;
    private responseDTO.PostRecommendedActivitiesResponseDTO PostRecommendedActivitiesResponseDTO;
    private GetActivitiesOfDecksResponseDTO GetActivitiesOfDecksResponseDTO;
    private UserDTO UserDTO;
    private LearningAnalyticsController Learning = new LearningAnalyticsController();
    private StoreController StoreController = new StoreController();
    private static final utils.PiTokenGenarator PiTokenGenarator = new PiTokenGenarator();
    private static final utils.PiTokenGenaratorNormalUser PiTokenGenaratorNormalUser = new PiTokenGenaratorNormalUser();
    private static final utils.PiTokenGenaratorAuthor PiTokenGenaratorAuthor = new PiTokenGenaratorAuthor();
    private static final HashMap<String, String> headerNormal = new HashMap<>();
    private static final HashMap<String, String> header = new HashMap<>();
    private static final HashMap<String, String> headerAuthor = new HashMap<>();
    private static final HashMap<String, String> queryparam = new HashMap<>();
    JsonReader jsonReader = new JsonReader();

    private static final Logger LOGGER = Logger.getLogger(String.valueOf(ExpertQuestion.class));


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
                headerNormal.put("X-TenantId", "f00ecc22-af26-4b56-be26-5a40b7e25d23");
                headerNormal.put("X-TenantKey", "27c1c0d2-656a-4d04-a491-06b13ac27261");
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


   /* @Test(priority = 3) @AlmAnnotation(almTestId = "")
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
    }*/

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


}
