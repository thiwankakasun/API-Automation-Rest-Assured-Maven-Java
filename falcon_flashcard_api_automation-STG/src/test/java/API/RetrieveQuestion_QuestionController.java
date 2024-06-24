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
import requestDTO.DeckControllerRequestDTO;
import requestDTO.QuestionControllerRequestDTO;
import responseDTO.DeckControllerResponseDTO;
import responseDTO.QuestionControllerResponseDTO;
import responseDTO.QuestionDTO;
import utils.JsonReader;
import utils.PiTokenGenarator;
import utils.PiTokenGenaratorNormalUser;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

public class RetrieveQuestion_QuestionController extends ServiceController {

    //private String defaultContentType = "JSON";
    private QuestionControllerResponseDTO QuestionControllerResponseDTO;
    private QuestionControllerRequestDTO QuestionControllerRequestDTO;
    private DeckControllerResponseDTO DeckControllerResponseDTO;
    private DeckControllerRequestDTO DeckControllerRequestDTO;
    private QuestionDTO QuestionDTO;
    private static final utils.PiTokenGenarator PiTokenGenarator = new PiTokenGenarator();
    private static final utils.PiTokenGenaratorNormalUser PiTokenGenaratorNormalUser = new PiTokenGenaratorNormalUser();
    private static final HashMap<String, String> header = new HashMap<>();
    private static final HashMap<String, String> headerNormal = new HashMap<>();
    private static final HashMap<String, String> queryparam = new HashMap<>();
    //private Map<String, String> payload = new HashMap<>();
    JsonReader jsonReader = new JsonReader();

    private static final Logger LOGGER = Logger.getLogger(String.valueOf(RetrieveQuestion_QuestionController.class));

    @BeforeClass
    public void getData() throws EncryptedDocumentException, InvalidFormatException, IOException, ParseException {
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
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 1)
    public void createSingleDeckWithFullPayload() throws Exception {
        JSONObject createDeckPayload = jsonReader.getJsonObject(DeckCreatePayload);
        System.out.println(createDeckPayload);

        String payload = String.valueOf(createDeckPayload);
        DeckControllerRequestDTO = mapper.readValue(payload, DeckControllerRequestDTO.class);
        DeckControllerResponseDTO = createDeck(headerNormal, payload);

        Assert.assertEquals(StatusCode, 201, "Status Code");
        LOGGER.info("Deck Id:" + DeckControllerResponseDTO.getDecks()[0].getId());
    }

    @Test(priority = 2)
    @AlmAnnotation(almTestId = "609861")
    public void CreateQuestionWithFullPayload() throws Exception {
        try {
            // Read json object from file
            JSONObject createQuestionPayload = jsonReader.getJsonObject(QuestionCreatePayload);
            System.out.println(createQuestionPayload);
            createQuestionPayload.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());

            String payload = String.valueOf(createQuestionPayload);
            //QuestionControllerRequestDTO = mapper.readValue(payload, QuestionControllerRequestDTO.class);
            QuestionControllerResponseDTO = createQuestion(headerNormal, payload);

            Assert.assertEquals(StatusCode, 201, "Status Code");
            //LOGGER.info("Tenant Id:" + QuestionControllerResponseDTO.getQuestions()[0].getI);
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 3)
    @AlmAnnotation(almTestId = "609180")
    public void GetQuestionByQuestionIdWithFullPayload() throws Exception {
        try {
            String QuestionId = QuestionControllerResponseDTO.getQuestions()[0].getId();
            QuestionDTO = getQuestionByQuestionId(headerNormal, QuestionId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 4)
    @AlmAnnotation(almTestId = "609180")
    public void GetQuestionByDeckIdWithFullPayload() throws Exception {
        try {
            queryparam.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());
            QuestionControllerResponseDTO = getQuestionByDeckId(headerNormal, queryparam);

            Assert.assertEquals(StatusCode, 200, "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 5) @AlmAnnotation(almTestId = "611101")
    public void UpdateQuestionWithFullPayload() throws Exception {
        try {

            JSONObject updateQuestionPayload = jsonReader.getJsonObject(QuestionUpdatePayload);
            updateQuestionPayload.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());
            JSONObject answers1 = new JSONObject();
            answers1.put("type", "TEXT");
            answers1.put("id", 1);
            answers1.put("value", "This is the answer");
            JSONArray answers = new JSONArray();
            answers.add(answers1);

            JSONObject DeckField = new JSONObject();
            DeckField.put("answers", answers);
            DeckField.put("question", "Question");
            DeckField.put("kind", "SHORT_ANSWER");
            DeckField.put("platform", "Mobile");
            DeckField.put("source", "App");
            DeckField.put("type", "Manual");
            DeckField.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());
            DeckField.put("id", QuestionControllerResponseDTO.getQuestions()[0].getId());

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            updateQuestionPayload.put("questions", decks);

            String payload = String.valueOf(updateQuestionPayload);
            //QuestionControllerRequestDTO = mapper.readValue(payload, QuestionControllerRequestDTO.class);
            long currentTimestamp = System.currentTimeMillis();
            QuestionControllerResponseDTO = updateQuestion(headerNormal, payload, currentTimestamp);

            Assert.assertEquals(StatusCode, 200, "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 6)
    @AlmAnnotation(almTestId = "611108")
    public void DeleteSingleQuestionWithFullPayload() throws Exception {

        try {
            String QuestionId = QuestionControllerResponseDTO.getQuestions()[0].getId();
            QuestionControllerResponseDTO = deleteQuestion(headerNormal, QuestionId);

            Assert.assertEquals(StatusCode, 204, "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 7)
    @AlmAnnotation(almTestId = "611108")
    public void DeleteMultipleQuestionWithFullPayload() throws Exception {

        try {
            CreateQuestionWithFullPayload();
            String QuestionId1 = QuestionControllerResponseDTO.getQuestions()[0].getId();
            CreateQuestionWithFullPayload();
            String QuestionId2 = QuestionControllerResponseDTO.getQuestions()[0].getId();
            String QuestionId = QuestionId1 + "," + QuestionId2;
            QuestionControllerResponseDTO = deleteMultipleQuestion(headerNormal, QuestionId);

            Assert.assertEquals(StatusCode, 204, "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 8)
    @AlmAnnotation(almTestId = "611108")
    public void DeleteDeckWithFullPayload() throws Exception {

        try {
            String DeckId = DeckControllerResponseDTO.getDecks()[0].getId();
            DeckControllerResponseDTO = deleteDeck(headerNormal, DeckId);

            Assert.assertEquals(StatusCode, 204, "Status Code");

        } catch (Exception e) {
            throw e;
        }
    }
    @Test(priority = 9)
    @AlmAnnotation(almTestId = "609180")
    public void GetQuestionByQuestionIdWithValidatingFullPayload() throws Exception {
        try {
            getData();
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            String QuestionId = QuestionControllerResponseDTO.getQuestions()[0].getId();
            QuestionDTO = getQuestionByQuestionId(headerNormal, QuestionId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertNotNull(QuestionDTO.getCorrelationId());
            Assert.assertEquals(QuestionDTO.getQuestion(), "string", "Status Code");
            Assert.assertEquals(QuestionDTO.getKind(), "ALL", "Status Code");
//            if (Env.equals("PROD"))
//                Assert.assertEquals(QuestionDTO.getCreatorId(), "ffffffff5bc5a665e4b0868373897e34", "Creator ID");
//            else
            Assert.assertEquals(QuestionDTO.getCreatorId(), DeckControllerResponseDTO.getDecks()[0].getUserId(), "Creator ID");
            Assert.assertEquals(QuestionDTO.getDeckId(), DeckControllerResponseDTO.getDecks()[0].getId(), "Status Code");
            Assert.assertEquals(QuestionDTO.getType(), "Manual", "Status Code");
            Assert.assertEquals(QuestionDTO.getPlatform(), "Mobile", "Status Code");
            Assert.assertEquals(QuestionDTO.getSource(), "App", "Status Code");
            Assert.assertNotNull(QuestionDTO.getUserCreatedAt(), "Status Code");
            Assert.assertNotNull(QuestionDTO.getId(), "Status Code");
            Assert.assertFalse(QuestionDTO.isDeleted(), "Status Code");
            Assert.assertFalse(QuestionDTO.isArchived(), "Status Code");
            Assert.assertFalse(QuestionDTO.isFavourite(), "Status Code");
            Assert.assertEquals(QuestionDTO.getAnswers()[0].getId(), 1, "Status Code");
            Assert.assertEquals(QuestionDTO.getAnswers()[0].getType(), "TEXT", "Status Code");
            Assert.assertEquals(QuestionDTO.getAnswers()[0].getValue(), "string", "Status Code");
            Assert.assertTrue(QuestionDTO.getAnswers()[0].isCorrect(), "Status Code");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 10)
    @AlmAnnotation(almTestId = "609180")
    public void GetQuestionByQuestionIdWithInvalidQuestionId() throws Exception {
        try {

            String QuestionId = "1234";
            QuestionDTO = getQuestionByQuestionId(headerNormal, QuestionId);

            Assert.assertEquals(StatusCode, 404, "Status Code");
            Assert.assertEquals(QuestionDTO.getDescription(), "Couldn't find question with id : 1234", "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 11)
    @AlmAnnotation(almTestId = "609180")
    public void GetQuestionByQuestionIdWithoutQuestionId() throws Exception {
        try {

            String QuestionId = "";
            QuestionDTO = getQuestionByQuestionId(headerNormal, QuestionId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionDTO.getFieldErrors()[0].getMessage(), "Query parameter deckId Cannot be null or empty", "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    //Verify whether an error is thrown when retrieving a question with already deleted questionID.
    @Test(priority = 12)
    @AlmAnnotation(almTestId = "609180")
    public void GetQuestionByQuestionIdWithDeletedQuestionId() throws Exception {
        try {

            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            String QuestionId = QuestionControllerResponseDTO.getQuestions()[0].getId();
            DeleteSingleQuestionWithFullPayload();
            QuestionDTO = getQuestionByQuestionId(headerNormal, QuestionId);

            Assert.assertEquals(StatusCode, 404, "Status Code");
            Assert.assertEquals(QuestionDTO.getDescription(), "Couldn't find question with id : " + QuestionId, "Status Code");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    //Verify whether an error is thrown when retrieving a question in deleted deck.
    @Test(priority = 13)
    @AlmAnnotation(almTestId = "609180")
    public void GetQuestionByQuestionIdWithDeletedDeck() throws Exception {
        try {

            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            String QuestionId = QuestionControllerResponseDTO.getQuestions()[0].getId();
            DeleteDeckWithFullPayload();
            Thread.sleep(5000);
            QuestionDTO = getQuestionByQuestionId(headerNormal, QuestionId);

            Assert.assertEquals(StatusCode, 404, "Status Code");
            Assert.assertEquals(QuestionDTO.getDescription(), "Couldn't find question with id : " + QuestionId, "Status Code");


        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 14)
    @AlmAnnotation(almTestId = "609180")
    public void GetQuestionByQuestionIdWithInvalidTenantId() throws Exception {
        try {

            headerNormal.put("X-TenantId", "a");
            String QuestionId = "a";
            QuestionDTO = getQuestionByQuestionId(headerNormal, QuestionId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionDTO.getDescription(), "Field 'tenantId' does not have a valid value", "Status Code");


        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 15)
    @AlmAnnotation(almTestId = "609180")
    public void GetQuestionByQuestionIdWithInvalidTenantKey() throws Exception {
        try {

            getData();
            headerNormal.put("X-TenantKey", "a");
            String QuestionId = "a";
            QuestionDTO = getQuestionByQuestionId(headerNormal, QuestionId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionDTO.getDescription(), "Field 'tenantKey' does not have a valid value", "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 16)
    @AlmAnnotation(almTestId = "609180")
    public void GetQuestionByQuestionIdWithUnauthorizedError() throws Exception {
        try {
            getData();
            headerNormal.put("X-Authorization", "a");
            String QuestionId = "a";
            QuestionDTO = getQuestionByQuestionId(headerNormal, QuestionId);

            Assert.assertEquals(StatusCode, 401, "Status Code");
            Assert.assertEquals(QuestionDTO.getError(), "Unauthorized", "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    //GetQuestionByDeckId
    //Verify Fields in Payload
    @Test(priority = 17)
    @AlmAnnotation(almTestId = "609180")
    public void GetQuestionByDeckIdWithValidatingFullPayload() throws Exception {
        try {
            getData();
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            queryparam.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());
            QuestionControllerResponseDTO = getQuestionByDeckId(headerNormal, queryparam);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertNotNull(QuestionControllerResponseDTO.getQuestions()[0].getCorrelationId());
            Assert.assertEquals(QuestionControllerResponseDTO.getQuestions()[0].getQuestion(), "string", "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getQuestions()[0].getKind(), "ALL", "Status Code");
//            if (Env.equals("PROD"))
//                Assert.assertEquals(QuestionControllerResponseDTO.getQuestions()[0].getCreatorId(), "ffffffff5bc5a665e4b0868373897e34", "Creator ID");
//            else
            Assert.assertEquals(QuestionControllerResponseDTO.getQuestions()[0].getCreatorId(), DeckControllerResponseDTO.getDecks()[0].getUserId(), "Creator ID");
            Assert.assertEquals(QuestionControllerResponseDTO.getQuestions()[0].getDeckId(), DeckControllerResponseDTO.getDecks()[0].getId(), "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getQuestions()[0].getType(), "Manual", "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getQuestions()[0].getPlatform(), "Mobile", "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getQuestions()[0].getSource(), "App", "Status Code");
            Assert.assertNotNull(QuestionControllerResponseDTO.getQuestions()[0].getUserCreatedAt(), "Status Code");
            Assert.assertNotNull(QuestionControllerResponseDTO.getQuestions()[0].getId(), "Status Code");
            Assert.assertFalse(QuestionControllerResponseDTO.getQuestions()[0].isDeleted(), "Status Code");
            Assert.assertFalse(QuestionControllerResponseDTO.getQuestions()[0].isArchived(), "Status Code");
            Assert.assertFalse(QuestionControllerResponseDTO.getQuestions()[0].isFavourite(), "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getQuestions()[0].getAnswers()[0].getId(), 1, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getQuestions()[0].getAnswers()[0].getType(), "TEXT", "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getQuestions()[0].getAnswers()[0].getValue(), "string", "Status Code");
            Assert.assertTrue(QuestionControllerResponseDTO.getQuestions()[0].getAnswers()[0].isCorrect(), "Status Code");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 18)
    @AlmAnnotation(almTestId = "609180")
    public void GetQuestionByDeckIdWithoutDeckId() throws Exception {
        try {
            queryparam.put("deckId", "");
            QuestionControllerResponseDTO = getQuestionByDeckId(headerNormal, queryparam);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getFieldErrors()[0].getMessage(), "Query parameter deckId Cannot be null or empty", "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 19)
    @AlmAnnotation(almTestId = "609180")
    public void GetQuestionByDeckIdWithDeletedDeckId() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            queryparam.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());
            DeleteDeckWithFullPayload();
            QuestionControllerResponseDTO = getQuestionByDeckId(headerNormal, queryparam);

            Assert.assertEquals(StatusCode, 404, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getDescription(), "Invalid Deck ID", "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    //Validate retriving cards giving updatedAfter value
    //@Test(priority = 20) @AlmAnnotation(almTestId = "609180")
    public void GetQuestionByDeckIdWithupdatedAfterField() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            queryparam.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());
            long currentTimestamp = System.currentTimeMillis();
            queryparam.put("updatedAfter", String.valueOf(currentTimestamp));
            UpdateQuestionWithFullPayload();
            QuestionControllerResponseDTO = getQuestionByDeckId(headerNormal, queryparam);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getQuestions()[0].getDeckId(), DeckControllerResponseDTO.getDecks()[0].getId(), "Status Code");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    //User will not able to retrive questions when deck id sent not belong to that user
    @Test(priority = 21)
    @AlmAnnotation(almTestId = "609180")
    public void GetQuestionByDeckIdWithDifferentUser() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            queryparam.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());
            //long currentTimestamp = System.currentTimeMillis();
            //queryparam.put("updatedAfter",String.valueOf(currentTimestamp));
            //UpdateQuestionWithFullPayload();
            QuestionControllerResponseDTO = getQuestionByDeckId(header, queryparam);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getDescription(), "User IDs do not match", "Status Code");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 22)
    @AlmAnnotation(almTestId = "609180")
    public void GetQuestionByDeckIdWithDifferentUse() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            queryparam.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());
//            long currentTimestamp = System.currentTimeMillis();
//            queryparam.put("updatedAfter",String.valueOf(currentTimestamp));
//            UpdateQuestionWithFullPayload();
            QuestionControllerResponseDTO = getQuestionByDeckId(header, queryparam);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getDescription(), "User IDs do not match", "Status Code");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 23)
    @AlmAnnotation(almTestId = "609180")
    public void GetQuestionByDeckIdWithInvalidDeckId() throws Exception {
        try {
            queryparam.put("deckId", "1234");
            QuestionControllerResponseDTO = getQuestionByDeckId(headerNormal, queryparam);

            Assert.assertEquals(StatusCode, 404, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getDescription(), "Invalid Deck ID", "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    //Verify retrieving cards from an empty deck
    @Test(priority = 24)
    @AlmAnnotation(almTestId = "609180")
    public void GetQuestionByDeckIdWithEmptyDeck() throws Exception {
        try {
            createSingleDeckWithFullPayload();

            queryparam.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());
            QuestionControllerResponseDTO = getQuestionByDeckId(headerNormal, queryparam);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 25)
    @AlmAnnotation(almTestId = "609180")
    public void GetQuestionByDeckIdWithInvalidTenantId() throws Exception {
        try {
            headerNormal.put("X-TenantId", "a");
            queryparam.put("deckId", "a");
            QuestionControllerResponseDTO = getQuestionByDeckId(headerNormal, queryparam);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getDescription(), "Field 'tenantId' does not have a valid value");

        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 26)
    @AlmAnnotation(almTestId = "609180")
    public void GetQuestionByDeckIdWithInvalidTenantKey() throws Exception {
        try {
            getData();
            headerNormal.put("X-TenantKey", "a");
            queryparam.put("deckId", "a");
            QuestionControllerResponseDTO = getQuestionByDeckId(headerNormal, queryparam);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getDescription(), "Field 'tenantKey' does not have a valid value");

        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 27)
    @AlmAnnotation(almTestId = "609180")
    public void GetQuestionByDeckIdWithAuthorizedError() throws Exception {
        try {
            getData();
            headerNormal.put("X-Authorization", "a");
            queryparam.put("deckId", "a");
            QuestionControllerResponseDTO = getQuestionByDeckId(headerNormal, queryparam);

            Assert.assertEquals(StatusCode, 401, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getError(), "Unauthorized", "Status Code");

        } catch (Exception e) {
            throw e;
        }
    }

}

