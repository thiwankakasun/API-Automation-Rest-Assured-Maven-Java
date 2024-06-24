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

public class CreateQuestion_QuestionController extends ServiceController {

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

    private static final Logger LOGGER = Logger.getLogger(String.valueOf(CreateQuestion_QuestionController.class));

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

    //Verifying whether the cards cannot be saved without question prompt
    @Test(priority = 9)
    @AlmAnnotation(almTestId = "609861")
    public void CreateQuestionWithoutQuestionField() throws Exception {
        try {
            createSingleDeckWithFullPayload();

            JSONObject createQuestionPayload = jsonReader.getJsonObject(QuestionCreatePayload);
            System.out.println(createQuestionPayload);

            createQuestionPayload.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());

            JSONObject answers1 = new JSONObject();
            answers1.put("correct", true);
            answers1.put("id", 1);
            answers1.put("value", "string");
            JSONArray answers = new JSONArray();
            answers.add(answers1);

            JSONObject DeckField = new JSONObject();
            DeckField.put("answers", answers);
            DeckField.put("question", null);
            DeckField.put("kind", "ALL");
            DeckField.put("platform", "Mobile");
            DeckField.put("source", "App");
            DeckField.put("type", "Manual");

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            createQuestionPayload.put("questions", decks);

            String payload = String.valueOf(createQuestionPayload);
            QuestionControllerResponseDTO = createQuestion(headerNormal, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getFieldErrors()[0].getMessage(), "'question' Cannot be null or empty", "Status Code");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    //Verifying whether the cards cannot be saved without answer prompt
    @Test(priority = 10)
    @AlmAnnotation(almTestId = "609861")
    public void CreateQuestionWithoutValueField() throws Exception {
        try {
            createSingleDeckWithFullPayload();

            JSONObject createQuestionPayload = jsonReader.getJsonObject(QuestionCreatePayload);
            System.out.println(createQuestionPayload);

            createQuestionPayload.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());

            JSONObject answers1 = new JSONObject();
            answers1.put("correct", true);
            answers1.put("id", 1);
            answers1.put("value", null);
            JSONArray answers = new JSONArray();
            answers.add(answers1);

            JSONObject DeckField = new JSONObject();
            DeckField.put("answers", answers);
            DeckField.put("question", "string");
            DeckField.put("kind", "ALL");
            DeckField.put("platform", "Mobile");
            DeckField.put("source", "App");
            DeckField.put("type", "Manual");

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            createQuestionPayload.put("questions", decks);

            String payload = String.valueOf(createQuestionPayload);
            QuestionControllerResponseDTO = createQuestion(headerNormal, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getFieldErrors()[0].getMessage(), "'value' Cannot be null or empty", "Status Code");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 11)
    @AlmAnnotation(almTestId = "609861")
    public void CreateQuestionWithEmptyValueForDeckId() throws Exception {
        try {
            JSONObject createQuestionPayload = jsonReader.getJsonObject(QuestionCreatePayload);
            System.out.println(createQuestionPayload);
            createQuestionPayload.put("deckId", "");

            String payload = String.valueOf(createQuestionPayload);
            QuestionControllerResponseDTO = createQuestion(headerNormal, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getFieldErrors()[0].getMessage(), "deckId Cannot be null or empty", "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 12)
    @AlmAnnotation(almTestId = "609861")
    public void CreateQuestionWithInvalidValueForDeckId() throws Exception {
        try {
            JSONObject createQuestionPayload = jsonReader.getJsonObject(QuestionCreatePayload);
            System.out.println(createQuestionPayload);
            createQuestionPayload.put("deckId", "1234");

            String payload = String.valueOf(createQuestionPayload);
            QuestionControllerResponseDTO = createQuestion(headerNormal, payload);

            Assert.assertEquals(StatusCode, 404, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getDescription(), "Invalid Deck ID", "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 13)
    @AlmAnnotation(almTestId = "609861")
    public void CreateQuestionWithDeletedDeckId() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            JSONObject createQuestionPayload = jsonReader.getJsonObject(QuestionCreatePayload);
            System.out.println(createQuestionPayload);
            createQuestionPayload.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());
            DeleteDeckWithFullPayload();

            String payload = String.valueOf(createQuestionPayload);
            QuestionControllerResponseDTO = createQuestion(headerNormal, payload);

            Assert.assertEquals(StatusCode, 404, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getDescription(), "Invalid Deck ID", "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 14)
    @AlmAnnotation(almTestId = "609861")
    public void CreateQuestionWithDifferentUser() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            JSONObject createQuestionPayload = jsonReader.getJsonObject(QuestionCreatePayload);
            System.out.println(createQuestionPayload);
            createQuestionPayload.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());

            String payload = String.valueOf(createQuestionPayload);
            QuestionControllerResponseDTO = createQuestion(header, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getDescription(), "User IDs do not match", "Status Code");
            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 15)
    @AlmAnnotation(almTestId = "609861")
    public void CreateQuestionWithNullValueforCorrectField() throws Exception {
        try {
            createSingleDeckWithFullPayload();

            JSONObject createQuestionPayload = jsonReader.getJsonObject(QuestionCreatePayload);
            System.out.println(createQuestionPayload);

            createQuestionPayload.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());

            JSONObject answers1 = new JSONObject();
            answers1.put("correct", null);
            answers1.put("id", 1);
            answers1.put("value", "string");
            JSONArray answers = new JSONArray();
            answers.add(answers1);

            JSONObject DeckField = new JSONObject();
            DeckField.put("answers", answers);
            DeckField.put("question", "string");
            DeckField.put("kind", "ALL");
            DeckField.put("platform", "Mobile");
            DeckField.put("source", "App");
            DeckField.put("type", "Manual");

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            createQuestionPayload.put("questions", decks);

            String payload = String.valueOf(createQuestionPayload);
            QuestionControllerResponseDTO = createQuestion(headerNormal, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            if (QuestionControllerResponseDTO.getFieldErrors()[0].getMessage().equals("'correctAnswers' should be present")) {
                Assert.assertEquals(QuestionControllerResponseDTO.getFieldErrors()[0].getMessage(), "'correctAnswers' should be present", "Status Code");
                Assert.assertEquals(QuestionControllerResponseDTO.getFieldErrors()[1].getMessage(), "must not be null", "Status Code");
            } else {
                Assert.assertEquals(QuestionControllerResponseDTO.getFieldErrors()[1].getMessage(), "'correctAnswers' should be present", "Status Code");
                Assert.assertEquals(QuestionControllerResponseDTO.getFieldErrors()[0].getMessage(), "must not be null", "Status Code");
            }
            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 16)
    @AlmAnnotation(almTestId = "609861")
    public void CreateQuestionWithNullValueforIdField() throws Exception {
        try {
            createSingleDeckWithFullPayload();

            JSONObject createQuestionPayload = jsonReader.getJsonObject(QuestionCreatePayload);
            System.out.println(createQuestionPayload);

            createQuestionPayload.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());

            JSONObject answers1 = new JSONObject();
            answers1.put("correct", true);
            answers1.put("id", null);
            answers1.put("value", "string");
            JSONArray answers = new JSONArray();
            answers.add(answers1);

            JSONObject DeckField = new JSONObject();
            DeckField.put("answers", answers);
            DeckField.put("question", "string");
            DeckField.put("kind", "ALL");
            DeckField.put("platform", "Mobile");
            DeckField.put("source", "App");
            DeckField.put("type", "Manual");

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            createQuestionPayload.put("questions", decks);

            String payload = String.valueOf(createQuestionPayload);
            QuestionControllerResponseDTO = createQuestion(headerNormal, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getFieldErrors()[0].getMessage(), "Answers id should be a positive integer.", "Status Code");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 17)
    @AlmAnnotation(almTestId = "609861")
    public void CreateQuestionWithNullValueforAnswerTypeField() throws Exception {
        try {
            createSingleDeckWithFullPayload();

            JSONObject createQuestionPayload = jsonReader.getJsonObject(QuestionCreatePayload);
            System.out.println(createQuestionPayload);

            createQuestionPayload.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());

            JSONObject answers1 = new JSONObject();
            answers1.put("correct", true);
            answers1.put("id", 1);
            answers1.put("value", "string");
            JSONArray answers = new JSONArray();
            answers.add(answers1);

            JSONObject DeckField = new JSONObject();
            DeckField.put("answers", answers);
            DeckField.put("question", "string");
            DeckField.put("kind", "ALL");
            DeckField.put("platform", "Mobile");
            DeckField.put("source", "App");
            DeckField.put("type", "Manual");

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            createQuestionPayload.put("questions", decks);

            String payload = String.valueOf(createQuestionPayload);
            QuestionControllerResponseDTO = createQuestion(headerNormal, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getFieldErrors()[0].getField(), "questions[0].answers[0].type", "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getFieldErrors()[0].getMessage(), "must not be null", "Status Code");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 18)
    @AlmAnnotation(almTestId = "609861")
    public void CreateQuestionWithNullValueforAnswersField() throws Exception {
        try {
            createSingleDeckWithFullPayload();

            JSONObject createQuestionPayload = jsonReader.getJsonObject(QuestionCreatePayload);
            System.out.println(createQuestionPayload);

            createQuestionPayload.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());

            JSONObject DeckField = new JSONObject();
            DeckField.put("answers", null);
            DeckField.put("question", "string");
            DeckField.put("kind", "ALL");
            DeckField.put("platform", "Mobile");
            DeckField.put("source", "App");
            DeckField.put("type", "Manual");

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            createQuestionPayload.put("questions", decks);

            String payload = String.valueOf(createQuestionPayload);
            QuestionControllerResponseDTO = createQuestion(headerNormal, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            if (QuestionControllerResponseDTO.getFieldErrors()[0].getMessage().equals("answers' Cannot contain null values")) {
                Assert.assertEquals(QuestionControllerResponseDTO.getFieldErrors()[0].getField(), "questions[0].answers", "Status Code");
                Assert.assertEquals(QuestionControllerResponseDTO.getFieldErrors()[0].getMessage(), "answers' Cannot contain null values", "Status Code");
                Assert.assertEquals(QuestionControllerResponseDTO.getFieldErrors()[1].getField(), "questions[0].answers", "Status Code");
                Assert.assertEquals(QuestionControllerResponseDTO.getFieldErrors()[1].getMessage(), "'answers' Cannot be null", "Status Code");
            } else {
                Assert.assertEquals(QuestionControllerResponseDTO.getFieldErrors()[0].getField(), "questions[0].answers", "Status Code");
                Assert.assertEquals(QuestionControllerResponseDTO.getFieldErrors()[0].getMessage(), "'answers' Cannot be null", "Status Code");
                Assert.assertEquals(QuestionControllerResponseDTO.getFieldErrors()[1].getMessage(), "answers' Cannot contain null values", "Status Code");
                Assert.assertEquals(QuestionControllerResponseDTO.getFieldErrors()[1].getField(), "questions[0].answers", "Status Code");
            }
            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 19)
    @AlmAnnotation(almTestId = "609861")
    public void CreateQuestionWithNullValueforSourceField() throws Exception {
        try {
            createSingleDeckWithFullPayload();

            JSONObject createQuestionPayload = jsonReader.getJsonObject(QuestionCreatePayload);
            System.out.println(createQuestionPayload);

            createQuestionPayload.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());

            JSONObject answers1 = new JSONObject();
            answers1.put("correct", true);
            answers1.put("id", 1);
            answers1.put("type", "TEXT");
            answers1.put("value", "string");
            JSONArray answers = new JSONArray();
            answers.add(answers1);

            JSONObject DeckField = new JSONObject();
            DeckField.put("answers", answers);
            DeckField.put("question", "string");
            DeckField.put("kind", "ALL");
            DeckField.put("platform", "Mobile");
            DeckField.put("source", null);
            DeckField.put("type", "Manual");

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            createQuestionPayload.put("questions", decks);

            String payload = String.valueOf(createQuestionPayload);
            QuestionControllerResponseDTO = createQuestion(headerNormal, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getFieldErrors()[0].getField(), "questions[0].source", "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getFieldErrors()[0].getMessage(), "'source' Cannot be null or empty", "Status Code");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 20)
    @AlmAnnotation(almTestId = "609861")
    public void CreateQuestionWithNullValueforTypeField() throws Exception {
        try {
            createSingleDeckWithFullPayload();

            JSONObject createQuestionPayload = jsonReader.getJsonObject(QuestionCreatePayload);
            System.out.println(createQuestionPayload);

            createQuestionPayload.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());

            JSONObject answers1 = new JSONObject();
            answers1.put("correct", true);
            answers1.put("id", 1);
            answers1.put("type", "TEXT");
            answers1.put("value", "string");
            JSONArray answers = new JSONArray();
            answers.add(answers1);

            JSONObject DeckField = new JSONObject();
            DeckField.put("answers", answers);
            DeckField.put("question", "string");
            DeckField.put("kind", "ALL");
            DeckField.put("platform", "Mobile");
            DeckField.put("source", "App");
            DeckField.put("type", null);

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            createQuestionPayload.put("questions", decks);

            String payload = String.valueOf(createQuestionPayload);
            QuestionControllerResponseDTO = createQuestion(headerNormal, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getFieldErrors()[0].getField(), "questions[0].type", "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getFieldErrors()[0].getMessage(), "'type' Cannot be null or empty", "Status Code");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 21)
    @AlmAnnotation(almTestId = "609861")
    public void CreateQuestionWithNullValueforPlatformField() throws Exception {
        try {
            createSingleDeckWithFullPayload();

            JSONObject createQuestionPayload = jsonReader.getJsonObject(QuestionCreatePayload);
            System.out.println(createQuestionPayload);

            createQuestionPayload.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());

            JSONObject answers1 = new JSONObject();
            answers1.put("correct", true);
            answers1.put("id", 1);
            answers1.put("type", "TEXT");
            answers1.put("value", "string");
            JSONArray answers = new JSONArray();
            answers.add(answers1);

            JSONObject DeckField = new JSONObject();
            DeckField.put("answers", answers);
            DeckField.put("question", "string");
            DeckField.put("kind", "ALL");
            DeckField.put("platform", null);
            DeckField.put("source", "App");
            DeckField.put("type", "Manual");

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            createQuestionPayload.put("questions", decks);

            String payload = String.valueOf(createQuestionPayload);
            QuestionControllerResponseDTO = createQuestion(headerNormal, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getFieldErrors()[0].getField(), "questions[0].platform", "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getFieldErrors()[0].getMessage(), "'platform' Cannot be null or empty", "Status Code");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 22)
    @AlmAnnotation(almTestId = "609861")
    public void CreateQuestionWithNullValueforKindField() throws Exception {
        try {
            createSingleDeckWithFullPayload();

            JSONObject createQuestionPayload = jsonReader.getJsonObject(QuestionCreatePayload);
            System.out.println(createQuestionPayload);

            createQuestionPayload.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());

            JSONObject answers1 = new JSONObject();
            answers1.put("correct", true);
            answers1.put("id", 1);
            answers1.put("type", "TEXT");
            answers1.put("value", "string");
            JSONArray answers = new JSONArray();
            answers.add(answers1);

            JSONObject DeckField = new JSONObject();
            DeckField.put("answers", answers);
            DeckField.put("question", "string");
            DeckField.put("kind", null);
            DeckField.put("platform", "Mobile");
            DeckField.put("source", "App");
            DeckField.put("type", "Manual");

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            createQuestionPayload.put("questions", decks);

            String payload = String.valueOf(createQuestionPayload);
            QuestionControllerResponseDTO = createQuestion(headerNormal, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getFieldErrors()[0].getField(), "questions[0].kind", "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getFieldErrors()[0].getMessage(), "'kind' Cannot be null or empty", "Status Code");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 23)
    @AlmAnnotation(almTestId = "609861")
    public void CreateQuestionWithoutArchivedandFavouriteField() throws Exception {
        try {
            createSingleDeckWithFullPayload();

            JSONObject createQuestionPayload = jsonReader.getJsonObject(QuestionCreatePayload);
            System.out.println(createQuestionPayload);

            createQuestionPayload.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());

            JSONObject answers1 = new JSONObject();
            answers1.put("correct", true);
            answers1.put("id", 1);
            answers1.put("type", "TEXT");
            answers1.put("value", "string");
            JSONArray answers = new JSONArray();
            answers.add(answers1);

            JSONObject DeckField = new JSONObject();
            DeckField.put("answers", answers);
            DeckField.put("question", "string");
            DeckField.put("kind", "ALL");
            DeckField.put("platform", "Mobile");
            DeckField.put("source", "App");
            DeckField.put("type", "Manual");

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            createQuestionPayload.put("questions", decks);

            String payload = String.valueOf(createQuestionPayload);
            QuestionControllerResponseDTO = createQuestion(headerNormal, payload);

            Assert.assertEquals(StatusCode, 201, "Status Code");
            Assert.assertFalse(QuestionControllerResponseDTO.getQuestions()[0].isArchived(), "Status Code");
            Assert.assertFalse(QuestionControllerResponseDTO.getQuestions()[0].isFavourite(), "Status Code");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 24)
    @AlmAnnotation(almTestId = "609861")
    public void CreateQuestionWithInvalidTenantId() throws Exception {
        try {
            headerNormal.put("X-TenantId", "a");
            JSONObject createQuestionPayload = jsonReader.getJsonObject(QuestionCreatePayload);
            System.out.println(createQuestionPayload);
            createQuestionPayload.put("deckId", "a");

            String payload = String.valueOf(createQuestionPayload);
            QuestionControllerResponseDTO = createQuestion(headerNormal, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getDescription(), "Field 'tenantId' does not have a valid value", "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 25)
    @AlmAnnotation(almTestId = "609861")
    public void CreateQuestionWithInvalidTenantKey() throws Exception {
        try {
            getData();
            headerNormal.put("X-TenantKey", "a");
            JSONObject createQuestionPayload = jsonReader.getJsonObject(QuestionCreatePayload);
            System.out.println(createQuestionPayload);
            createQuestionPayload.put("deckId", "a");

            String payload = String.valueOf(createQuestionPayload);
            QuestionControllerResponseDTO = createQuestion(headerNormal, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getDescription(), "Field 'tenantKey' does not have a valid value", "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 26)
    @AlmAnnotation(almTestId = "609861")
    public void CreateQuestionWithUnauthorizedError() throws Exception {
        try {
            headerNormal.put("X-Authorization", "a");
            JSONObject createQuestionPayload = jsonReader.getJsonObject(QuestionCreatePayload);
            System.out.println(createQuestionPayload);
            createQuestionPayload.put("deckId", "a");

            String payload = String.valueOf(createQuestionPayload);
            QuestionControllerResponseDTO = createQuestion(headerNormal, payload);

            Assert.assertEquals(StatusCode, 401, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getError(), "Unauthorized", "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }


}

