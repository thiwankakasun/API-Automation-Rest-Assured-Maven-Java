package API;

import com.pearson.common.framework.shared.alm.AlmAnnotation;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
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

public class DeleteQuestion_ExpertQuestion extends ServiceController{

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
