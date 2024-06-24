package API;

import com.ibm.icu.text.SimpleDateFormat;
import com.pearson.common.framework.shared.alm.AlmAnnotation;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pojos.PiToken;
import requestDTO.DeckControllerRequestDTO;
import requestDTO.QuestionControllerRequestDTO;
import requestDTO.TenantControllerRequestDTO;
import responseDTO.DeckControllerResponseDTO;
import responseDTO.QuestionControllerResponseDTO;
import responseDTO.QuestionDTO;
import responseDTO.TenantControllerResponseDTO;
import utils.JsonReader;
import utils.PiTokenGenarator;
import utils.PiTokenGenaratorNormalUser;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Logger;

public class QuestionController extends ServiceController {

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

    private static final Logger LOGGER = Logger.getLogger(String.valueOf(QuestionController.class));

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
    @AlmAnnotation(almTestId = "609861-669199")
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


    @Test(priority = 27)
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
//                Assert.assertEquals(QuestionDTO.getCreatorId(), DeckControllerResponseDTO.getDecks()[0].getUserId(), "Creator ID");
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

    @Test(priority = 28)
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

    @Test(priority = 29)
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
    @Test(priority = 30)
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
    @Test(priority = 31)
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

    @Test(priority = 32)
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

    @Test(priority = 33)
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

    @Test(priority = 34)
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
    @Test(priority = 35)
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

    @Test(priority = 36)
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

    @Test(priority = 37)
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
    //@Test(priority = 38) @AlmAnnotation(almTestId = "609180")
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
    @Test(priority = 39)
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

    @Test(priority = 40)
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

    @Test(priority = 41)
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
    @Test(priority = 42)
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

    @Test(priority = 43)
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

    @Test(priority = 44)
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

    @Test(priority = 45)
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

    //Verify creatorId and createdTime are returned in the card edit response
    @Test(priority = 46) @AlmAnnotation(almTestId = "611101")
    public void UpdateQuestionWithVerifingCreatorIdandCreatedTimeinPayload() throws Exception {
        try {
            getData();
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            long currentTimestamp = System.currentTimeMillis();
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

            QuestionControllerResponseDTO = updateQuestion(headerNormal, payload, currentTimestamp);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            if (Env.equals("PROD"))
                Assert.assertEquals(QuestionControllerResponseDTO.getQuestions()[0].getCreatorId(), "ffffffff5bc5a665e4b0868373897e34", "Creator ID");
            else
                Assert.assertEquals(QuestionControllerResponseDTO.getQuestions()[0].getCreatorId(), "0f1d72fbc8a4412ba1ac8b8970eeffd5", "Creator ID");
            Assert.assertNotNull(QuestionControllerResponseDTO.getQuestions()[0].getCreatedAt(), "0f1d72fbc8a4412ba1ac8b8970eeffd5");
            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    //Verify when an old value is sent as updateAfter value whether the request fails
    @Test(priority = 47)
    @AlmAnnotation(almTestId = "611101")
    public void VerifyUpdateQuestionWithSendingRequestWithPastEpochTime() throws Exception {
        try {
            getData();
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            long currentTimestamp = 1644900913;
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

            QuestionControllerResponseDTO = updateQuestion(headerNormal, payload, currentTimestamp);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getDescription(), "Invalid update request. UpdatedAt is less than the question updated time.");
            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 48) @AlmAnnotation(almTestId = "611101")
    public void UpdateQuestionWithUsingDeletedQuestionId() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
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

            String Id = QuestionControllerResponseDTO.getQuestions()[0].getId();
            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            updateQuestionPayload.put("questions", decks);

            DeleteSingleQuestionWithFullPayload();
            String payload = String.valueOf(updateQuestionPayload);
            //QuestionControllerRequestDTO = mapper.readValue(payload, QuestionControllerRequestDTO.class);
            long currentTimestamp = System.currentTimeMillis();
            QuestionControllerResponseDTO = updateQuestion(headerNormal, payload, currentTimestamp);

            Assert.assertEquals(StatusCode, 404, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getDescription(), "Couldn't find question with id : " + Id);
            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 49)
    @AlmAnnotation(almTestId = "611101")
    public void UpdateQuestionWithUsingDeletedDeckId() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
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

            DeleteDeckWithFullPayload();
            String payload = String.valueOf(updateQuestionPayload);
            //QuestionControllerRequestDTO = mapper.readValue(payload, QuestionControllerRequestDTO.class);
            long currentTimestamp = System.currentTimeMillis();
            QuestionControllerResponseDTO = updateQuestion(headerNormal, payload, currentTimestamp);

            Assert.assertEquals(StatusCode, 404, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getDescription(), "Invalid Deck ID");

        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 50)
    @AlmAnnotation(almTestId = "611101")
    public void UpdateQuestionWithUsingNullValueforQuestionId() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
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
            DeckField.put("id", null);

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            updateQuestionPayload.put("questions", decks);

            String payload = String.valueOf(updateQuestionPayload);
            //QuestionControllerRequestDTO = mapper.readValue(payload, QuestionControllerRequestDTO.class);
            long currentTimestamp = System.currentTimeMillis();
            QuestionControllerResponseDTO = updateQuestion(headerNormal, payload, currentTimestamp);

            Assert.assertEquals(StatusCode, 404, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getDescription(), "Couldn't find question with id : null");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 51)
    @AlmAnnotation(almTestId = "611101")
    public void UpdateQuestionWithUsingNullValueforAnswers() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            JSONObject updateQuestionPayload = jsonReader.getJsonObject(QuestionUpdatePayload);
            updateQuestionPayload.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());

            JSONObject DeckField = new JSONObject();
            DeckField.put("answers", null);
            DeckField.put("question", "Question");
            DeckField.put("kind", "SHORT_ANSWER");
            DeckField.put("platform", "Mobile");
            DeckField.put("source", "App");
            DeckField.put("type", "Manual");
            DeckField.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());
            DeckField.put("id", null);

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            updateQuestionPayload.put("questions", decks);

            String payload = String.valueOf(updateQuestionPayload);
            //QuestionControllerRequestDTO = mapper.readValue(payload, QuestionControllerRequestDTO.class);
            long currentTimestamp = System.currentTimeMillis();
            QuestionControllerResponseDTO = updateQuestion(headerNormal, payload, currentTimestamp);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            if (QuestionControllerResponseDTO.getFieldErrors()[0].getMessage().equals("answers' Cannot contain null values")) {
                Assert.assertEquals(QuestionControllerResponseDTO.getFieldErrors()[0].getMessage(), "answers' Cannot contain null values");
                Assert.assertEquals(QuestionControllerResponseDTO.getFieldErrors()[1].getMessage(), "'answers' Cannot be null");
            } else {
                Assert.assertEquals(QuestionControllerResponseDTO.getFieldErrors()[1].getMessage(), "answers' Cannot contain null values");
                Assert.assertEquals(QuestionControllerResponseDTO.getFieldErrors()[0].getMessage(), "'answers' Cannot be null");
            }

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 52)
    @AlmAnnotation(almTestId = "611101")
    public void UpdateQuestionWithUsingNullValueforAnswers_id() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            JSONObject updateQuestionPayload = jsonReader.getJsonObject(QuestionUpdatePayload);
            updateQuestionPayload.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());
            JSONObject answers1 = new JSONObject();
            answers1.put("type", "TEXT");
            answers1.put("id", null);
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
            DeckField.put("id", null);

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            updateQuestionPayload.put("questions", decks);

            String payload = String.valueOf(updateQuestionPayload);
            //QuestionControllerRequestDTO = mapper.readValue(payload, QuestionControllerRequestDTO.class);
            long currentTimestamp = System.currentTimeMillis();
            QuestionControllerResponseDTO = updateQuestion(headerNormal, payload, currentTimestamp);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getFieldErrors()[0].getMessage(), "Answers id should be a positive integer.");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 53)
    @AlmAnnotation(almTestId = "611101")
    public void UpdateQuestionWithUsingNullValueforValueField() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            JSONObject updateQuestionPayload = jsonReader.getJsonObject(QuestionUpdatePayload);
            updateQuestionPayload.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());
            JSONObject answers1 = new JSONObject();
            answers1.put("type", "TEXT");
            answers1.put("id", 1);
            answers1.put("value", null);
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
            DeckField.put("id", null);

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            updateQuestionPayload.put("questions", decks);

            String payload = String.valueOf(updateQuestionPayload);
            //QuestionControllerRequestDTO = mapper.readValue(payload, QuestionControllerRequestDTO.class);
            long currentTimestamp = System.currentTimeMillis();
            QuestionControllerResponseDTO = updateQuestion(headerNormal, payload, currentTimestamp);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getFieldErrors()[0].getMessage(), "'value' Cannot be null or empty");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 54)
    @AlmAnnotation(almTestId = "611101")
    public void UpdateQuestionWithUsingNullValueforAnswer_TypeField() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            JSONObject updateQuestionPayload = jsonReader.getJsonObject(QuestionUpdatePayload);
            updateQuestionPayload.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());
            JSONObject answers1 = new JSONObject();
            answers1.put("type", null);
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
            DeckField.put("id", null);

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            updateQuestionPayload.put("questions", decks);

            String payload = String.valueOf(updateQuestionPayload);
            //QuestionControllerRequestDTO = mapper.readValue(payload, QuestionControllerRequestDTO.class);
            long currentTimestamp = System.currentTimeMillis();
            QuestionControllerResponseDTO = updateQuestion(headerNormal, payload, currentTimestamp);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getFieldErrors()[0].getField(), "questions[0].answers[0].type");
            Assert.assertEquals(QuestionControllerResponseDTO.getFieldErrors()[0].getMessage(), "must not be null");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 55)
    @AlmAnnotation(almTestId = "611101")
    public void UpdateQuestionWithUsingNullValueforSourceField() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
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
            DeckField.put("source", null);
            DeckField.put("type", "Manual");
            DeckField.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());
            DeckField.put("id", null);

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            updateQuestionPayload.put("questions", decks);

            String payload = String.valueOf(updateQuestionPayload);
            //QuestionControllerRequestDTO = mapper.readValue(payload, QuestionControllerRequestDTO.class);
            long currentTimestamp = System.currentTimeMillis();
            QuestionControllerResponseDTO = updateQuestion(headerNormal, payload, currentTimestamp);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getFieldErrors()[0].getField(), "questions[0].source");
            Assert.assertEquals(QuestionControllerResponseDTO.getFieldErrors()[0].getMessage(), "'source' Cannot be null or empty");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 56)
    @AlmAnnotation(almTestId = "611101")
    public void UpdateQuestionWithUsingNullValueforPlatformField() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
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
            DeckField.put("platform", null);
            DeckField.put("source", "App");
            DeckField.put("type", "Manual");
            DeckField.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());
            DeckField.put("id", null);

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            updateQuestionPayload.put("questions", decks);

            String payload = String.valueOf(updateQuestionPayload);
            //QuestionControllerRequestDTO = mapper.readValue(payload, QuestionControllerRequestDTO.class);
            long currentTimestamp = System.currentTimeMillis();
            QuestionControllerResponseDTO = updateQuestion(headerNormal, payload, currentTimestamp);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getFieldErrors()[0].getField(), "questions[0].platform");
            Assert.assertEquals(QuestionControllerResponseDTO.getFieldErrors()[0].getMessage(), "'platform' Cannot be null or empty");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 57)
    @AlmAnnotation(almTestId = "611101")
    public void UpdateQuestionWithUsingNullValueforTypeField() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
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
            DeckField.put("type", null);
            DeckField.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());
            DeckField.put("id", null);

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            updateQuestionPayload.put("questions", decks);

            String payload = String.valueOf(updateQuestionPayload);
            //QuestionControllerRequestDTO = mapper.readValue(payload, QuestionControllerRequestDTO.class);
            long currentTimestamp = System.currentTimeMillis();
            QuestionControllerResponseDTO = updateQuestion(headerNormal, payload, currentTimestamp);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getFieldErrors()[0].getField(), "questions[0].type");
            Assert.assertEquals(QuestionControllerResponseDTO.getFieldErrors()[0].getMessage(), "'type' Cannot be null or empty");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 58)
    @AlmAnnotation(almTestId = "611101")
    public void UpdateQuestionWithUsingNullValueforKindField() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
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
            DeckField.put("kind", null);
            DeckField.put("platform", "Mobile");
            DeckField.put("source", "App");
            DeckField.put("type", "Manual");
            DeckField.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());
            DeckField.put("id", null);

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            updateQuestionPayload.put("questions", decks);

            String payload = String.valueOf(updateQuestionPayload);
            //QuestionControllerRequestDTO = mapper.readValue(payload, QuestionControllerRequestDTO.class);
            long currentTimestamp = System.currentTimeMillis();
            QuestionControllerResponseDTO = updateQuestion(headerNormal, payload, currentTimestamp);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getFieldErrors()[0].getField(), "questions[0].kind");
            Assert.assertEquals(QuestionControllerResponseDTO.getFieldErrors()[0].getMessage(), "'kind' Cannot be null or empty");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 59)
    @AlmAnnotation(almTestId = "611101")
    public void UpdateQuestionWithUsingNullValueforQuestionField() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
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
            DeckField.put("question", null);
            DeckField.put("kind", "SHORT_ANSWER");
            DeckField.put("platform", "Mobile");
            DeckField.put("source", "App");
            DeckField.put("type", "Manual");
            DeckField.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());
            DeckField.put("id", null);

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            updateQuestionPayload.put("questions", decks);

            String payload = String.valueOf(updateQuestionPayload);
            //QuestionControllerRequestDTO = mapper.readValue(payload, QuestionControllerRequestDTO.class);
            long currentTimestamp = System.currentTimeMillis();
            QuestionControllerResponseDTO = updateQuestion(headerNormal, payload, currentTimestamp);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getFieldErrors()[0].getField(), "questions[0].question");
            Assert.assertEquals(QuestionControllerResponseDTO.getFieldErrors()[0].getMessage(), "'question' Cannot be null or empty");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 60)
    @AlmAnnotation(almTestId = "611101")
    public void UpdateQuestionWithUsingNullValueforQuestionsArray() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            JSONObject updateQuestionPayload = jsonReader.getJsonObject(QuestionUpdatePayload);
            updateQuestionPayload.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());
            updateQuestionPayload.put("questions", null);

            String payload = String.valueOf(updateQuestionPayload);
            //QuestionControllerRequestDTO = mapper.readValue(payload, QuestionControllerRequestDTO.class);
            long currentTimestamp = System.currentTimeMillis();
            QuestionControllerResponseDTO = updateQuestion(headerNormal, payload, currentTimestamp);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getFieldErrors()[0].getField(), "questions");
//            Assert.assertEquals(QuestionControllerResponseDTO.getFieldErrors()[0].getMessage(),"'questions' cannot contain null values");
//            Assert.assertEquals(QuestionControllerResponseDTO.getFieldErrors()[1].getMessage(),"Cannot be empty");
//            Assert.assertEquals(QuestionControllerResponseDTO.getFieldErrors()[2].getMessage(),"'questions' cannot be null");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 61)
    @AlmAnnotation(almTestId = "611101")
    public void UpdateQuestionWithUsingNullValuefordeckIdField() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            JSONObject updateQuestionPayload = jsonReader.getJsonObject(QuestionUpdatePayload);
            updateQuestionPayload.put("deckId", null);
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

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getFieldErrors()[0].getField(), "deckId");
            Assert.assertEquals(QuestionControllerResponseDTO.getFieldErrors()[0].getMessage(), "deckId Cannot be null or empty");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 62)
    @AlmAnnotation(almTestId = "611101")
    public void UpdateQuestionWithUsingNullValueforQuestions_DeckIdField() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
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
            DeckField.put("deckId", null);
            DeckField.put("id", QuestionControllerResponseDTO.getQuestions()[0].getId());

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            updateQuestionPayload.put("questions", decks);

            String payload = String.valueOf(updateQuestionPayload);
            //QuestionControllerRequestDTO = mapper.readValue(payload, QuestionControllerRequestDTO.class);
            long currentTimestamp = System.currentTimeMillis();
            QuestionControllerResponseDTO = updateQuestion(headerNormal, payload, currentTimestamp);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getDescription(), "All the questions should have the same 'deckId'");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 63)
    @AlmAnnotation(almTestId = "609861")
    public void UpdateQuestionWithInvalidTenantId() throws Exception {
        try {
            headerNormal.put("X-TenantId", "a");
            JSONObject updateQuestionPayload = jsonReader.getJsonObject(QuestionUpdatePayload);
            System.out.println(updateQuestionPayload);
            long currentTimestamp = System.currentTimeMillis();
            String payload = String.valueOf(updateQuestionPayload);
            QuestionControllerResponseDTO = updateQuestion(headerNormal, payload, currentTimestamp);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getDescription(), "Field 'tenantId' does not have a valid value", "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 64)
    @AlmAnnotation(almTestId = "609861")
    public void UpdateQuestionWithInvalidTenantKey() throws Exception {
        try {
            getData();
            headerNormal.put("X-TenantKey", "a");
            JSONObject updateQuestionPayload = jsonReader.getJsonObject(QuestionUpdatePayload);
            System.out.println(updateQuestionPayload);
            long currentTimestamp = System.currentTimeMillis();
            String payload = String.valueOf(updateQuestionPayload);
            QuestionControllerResponseDTO = updateQuestion(headerNormal, payload, currentTimestamp);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getDescription(), "Field 'tenantKey' does not have a valid value", "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 65)
    @AlmAnnotation(almTestId = "609861")
    public void UpdateQuestionWithUnauthorizedError() throws Exception {
        try {
            headerNormal.put("X-Authorization", "a");
            JSONObject updateQuestionPayload = jsonReader.getJsonObject(QuestionUpdatePayload);
            System.out.println(updateQuestionPayload);
            long currentTimestamp = System.currentTimeMillis();
            String payload = String.valueOf(updateQuestionPayload);
            QuestionControllerResponseDTO = updateQuestion(headerNormal, payload, currentTimestamp);

            Assert.assertEquals(StatusCode, 401, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getError(), "Unauthorized", "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 66)
    @AlmAnnotation(almTestId = "611108")
    public void DeleteSingleQuestionWithInvalidQuestionId() throws Exception {
        try {
            getData();
            String QuestionId = "6062af3c15a01f10b775a3cb";
            QuestionControllerResponseDTO = deleteQuestion(headerNormal, QuestionId);

            Assert.assertEquals(StatusCode, 404, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getDescription(), "Couldn't find question with id : 6062af3c15a01f10b775a3cb", "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 67)
    @AlmAnnotation(almTestId = "611108")
    public void DeleteSingleQuestionWithDeletedQuestionId() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            String QuestionId = QuestionControllerResponseDTO.getQuestions()[0].getId();
            DeleteSingleQuestionWithFullPayload();
            QuestionControllerResponseDTO = deleteQuestion(headerNormal, QuestionId);

            Assert.assertEquals(StatusCode, 404, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getDescription(), "Couldn't find question with id : " + QuestionId, "Status Code");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 68)
    @AlmAnnotation(almTestId = "611108")
    public void DeleteSingleQuestionWhenDeckIsDeleted() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            String QuestionId = QuestionControllerResponseDTO.getQuestions()[0].getId();
            DeleteDeckWithFullPayload();
            Thread.sleep(5000);
            QuestionControllerResponseDTO = deleteQuestion(headerNormal, QuestionId);

            Assert.assertEquals(StatusCode, 404, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getDescription(), "Couldn't find question with id : " + QuestionId, "Status Code");

        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 69)
    @AlmAnnotation(almTestId = "611108")
    public void DeleteSingleQuestionUsingDifferentUser() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            String QuestionId = QuestionControllerResponseDTO.getQuestions()[0].getId();
            QuestionControllerResponseDTO = deleteQuestion(header, QuestionId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getDescription(), "User IDs do not match", "Status Code");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 70)
    @AlmAnnotation(almTestId = "611108")
    public void DeleteSingleQuestionWithInvalidTenantId() throws Exception {
        try {
            headerNormal.put("X-TenantId", "a");
            String QuestionId = "a";
            QuestionControllerResponseDTO = deleteQuestion(headerNormal, QuestionId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getDescription(), "Field 'tenantId' does not have a valid value", "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 71)
    @AlmAnnotation(almTestId = "611108")
    public void DeleteSingleQuestionWithInvalidTenantKey() throws Exception {
        try {
            getData();
            headerNormal.put("X-TenantKey", "a");
            String QuestionId = "a";
            QuestionControllerResponseDTO = deleteQuestion(headerNormal, QuestionId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getDescription(), "Field 'tenantKey' does not have a valid value", "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 72)
    @AlmAnnotation(almTestId = "611108")
    public void DeleteSingleQuestionWithUnauthorizedError() throws Exception {
        try {
            getData();
            headerNormal.put("X-Authorization", "a");
            String QuestionId = "a";
            QuestionControllerResponseDTO = deleteQuestion(headerNormal, QuestionId);

            Assert.assertEquals(StatusCode, 401, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getError(), "Unauthorized", "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 73)
    @AlmAnnotation(almTestId = "611108")
    public void DeleteMultipleQuestionWithUnavailableQuestionId() throws Exception {

        try {
            getData();
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            String QuestionId1 = QuestionControllerResponseDTO.getQuestions()[0].getId();
            String QuestionId2 = "61c1ff23b3f8e23c9a2c34f";
            String QuestionId = QuestionId1 + "," + QuestionId2;
            QuestionControllerResponseDTO = deleteMultipleQuestion(headerNormal, QuestionId);

            Assert.assertEquals(StatusCode, 404, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getDescription(), "Couldn't find question with id : 61c1ff23b3f8e23c9a2c34f", "Status Code");
            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 74)
    @AlmAnnotation(almTestId = "611108")
    public void DeleteMultipleQuestionWithDuplicateQuestionId() throws Exception {

        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            String QuestionId1 = QuestionControllerResponseDTO.getQuestions()[0].getId();
            String QuestionId = QuestionId1 + "," + QuestionId1;
            QuestionControllerResponseDTO = deleteMultipleQuestion(headerNormal, QuestionId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getFieldErrors()[0].getMessage(), "Cannot contain duplicate values", "Status Code");
            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    //Check that an error is displaying when the qustion ids are not belongs to the requesting user
    @Test(priority = 75)
    @AlmAnnotation(almTestId = "611108")
    public void DeleteMultipleQuestionWithDifferentUser() throws Exception {

        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            String QuestionId = QuestionControllerResponseDTO.getQuestions()[0].getId();
            QuestionControllerResponseDTO = deleteMultipleQuestion(header, QuestionId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getDescription(), "All the questions should have the same 'creatorId'", "Status Code");
            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 76)
    @AlmAnnotation(almTestId = "611108")
    public void DeleteMultipleQuestionWithDifferentDeck() throws Exception {

        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            String QuestionId1 = QuestionControllerResponseDTO.getQuestions()[0].getId();
            String Id = DeckControllerResponseDTO.getDecks()[0].getId();

            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            String QuestionId2 = QuestionControllerResponseDTO.getQuestions()[0].getId();
            String QuestionId = QuestionId1 + "," + QuestionId2;
            QuestionControllerResponseDTO = deleteMultipleQuestion(headerNormal, QuestionId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getDescription(), "All the questions should have the same 'deckId'", "Status Code");
            DeleteDeckWithFullPayload();
            DeckControllerResponseDTO = deleteDeck(headerNormal, Id);

            Assert.assertEquals(StatusCode, 204, "Status Code");

        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 77)
    @AlmAnnotation(almTestId = "611108")
    public void DeleteMultipleQuestionWithOnlySingleQuestion() throws Exception {

        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            String QuestionId = QuestionControllerResponseDTO.getQuestions()[0].getId();

            QuestionControllerResponseDTO = deleteMultipleQuestion(headerNormal, QuestionId);

            Assert.assertEquals(StatusCode, 204, "Status Code");
            //Assert.assertEquals(QuestionControllerResponseDTO.getDescription(), "All the questions should have the same 'deckId'", "Status Code");
            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 78)
    @AlmAnnotation(almTestId = "611108")
    public void DeleteMultipleQuestionWithBulkQuestions() throws Exception {

        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            String QuestionId1 = QuestionControllerResponseDTO.getQuestions()[0].getId();
            CreateQuestionWithFullPayload();
            String QuestionId2 = QuestionControllerResponseDTO.getQuestions()[0].getId();
            CreateQuestionWithFullPayload();
            String QuestionId3 = QuestionControllerResponseDTO.getQuestions()[0].getId();
            CreateQuestionWithFullPayload();
            String QuestionId4 = QuestionControllerResponseDTO.getQuestions()[0].getId();
            CreateQuestionWithFullPayload();
            String QuestionId5 = QuestionControllerResponseDTO.getQuestions()[0].getId();
            CreateQuestionWithFullPayload();
            String QuestionId6 = QuestionControllerResponseDTO.getQuestions()[0].getId();
            CreateQuestionWithFullPayload();
            String QuestionId7 = QuestionControllerResponseDTO.getQuestions()[0].getId();
            CreateQuestionWithFullPayload();
            String QuestionId8 = QuestionControllerResponseDTO.getQuestions()[0].getId();
            CreateQuestionWithFullPayload();
            String QuestionId9 = QuestionControllerResponseDTO.getQuestions()[0].getId();
            CreateQuestionWithFullPayload();
            String QuestionId10 = QuestionControllerResponseDTO.getQuestions()[0].getId();
            CreateQuestionWithFullPayload();
            String QuestionId11 = QuestionControllerResponseDTO.getQuestions()[0].getId();
            CreateQuestionWithFullPayload();
            String QuestionId12 = QuestionControllerResponseDTO.getQuestions()[0].getId();
            CreateQuestionWithFullPayload();
            String QuestionId13 = QuestionControllerResponseDTO.getQuestions()[0].getId();
            CreateQuestionWithFullPayload();
            String QuestionId14 = QuestionControllerResponseDTO.getQuestions()[0].getId();
            CreateQuestionWithFullPayload();
            String QuestionId15 = QuestionControllerResponseDTO.getQuestions()[0].getId();
            CreateQuestionWithFullPayload();
            String QuestionId16 = QuestionControllerResponseDTO.getQuestions()[0].getId();
            CreateQuestionWithFullPayload();
            String QuestionId17 = QuestionControllerResponseDTO.getQuestions()[0].getId();
            CreateQuestionWithFullPayload();
            String QuestionId18 = QuestionControllerResponseDTO.getQuestions()[0].getId();
            CreateQuestionWithFullPayload();
            String QuestionId19 = QuestionControllerResponseDTO.getQuestions()[0].getId();
            CreateQuestionWithFullPayload();
            String QuestionId20 = QuestionControllerResponseDTO.getQuestions()[0].getId();

            String QuestionId = QuestionId1 + "," + QuestionId2 + "," + QuestionId3 + "," + QuestionId4 + "," + QuestionId5 + "," + QuestionId6 + "," + QuestionId7 + "," + QuestionId8 + "," + QuestionId9 + "," + QuestionId10
                    + "," + QuestionId11 + "," + QuestionId12 + "," + QuestionId13 + "," + QuestionId14 + "," + QuestionId15 + "," + QuestionId16 + "," + QuestionId17 + "," + QuestionId18 + "," + QuestionId19 + "," + QuestionId20;

            QuestionControllerResponseDTO = deleteMultipleQuestion(headerNormal, QuestionId);

            Assert.assertEquals(StatusCode, 204, "Status Code");
            //Assert.assertEquals(QuestionControllerResponseDTO.getDescription(), "All the questions should have the same 'deckId'", "Status Code");
            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 79)
    @AlmAnnotation(almTestId = "611108")
    public void DeleteMultipleQuestionWithInvalidQuestionId() throws Exception {

        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            String QuestionId = "aa";

            QuestionControllerResponseDTO = deleteMultipleQuestion(headerNormal, QuestionId);

            Assert.assertEquals(StatusCode, 404, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getDescription(), "Resource Not Found", "Status Code");
            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 80)
    @AlmAnnotation(almTestId = "611108")
    public void DeleteMultipleQuestionWithNullValueforQuestionId() throws Exception {

        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            String QuestionId = "";

            QuestionControllerResponseDTO = deleteMultipleQuestion(headerNormal, QuestionId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getFieldErrors()[0].getMessage(), "Cannot be empty", "Status Code");
            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 81)
    @AlmAnnotation(almTestId = "611108")
    public void DeleteMultipleQuestionWithInvalidTenantId() throws Exception {

        try {
            headerNormal.put("X-TenantId", "a");
            String QuestionId = "a";
            QuestionControllerResponseDTO = deleteMultipleQuestion(headerNormal, QuestionId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getDescription(), "Field 'tenantId' does not have a valid value", "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 82)
    @AlmAnnotation(almTestId = "611108")
    public void DeleteMultipleQuestionWithInvalidTenantKey() throws Exception {

        try {
            getData();
            headerNormal.put("X-TenantKey", "a");
            String QuestionId = "a";
            QuestionControllerResponseDTO = deleteMultipleQuestion(headerNormal, QuestionId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getDescription(), "Field 'tenantKey' does not have a valid value", "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 83)
    @AlmAnnotation(almTestId = "611108")
    public void DeleteMultipleQuestionWithUnauthorizedError() throws Exception {
        try {
            getData();
            headerNormal.put("X-Authorization", "a");
            String QuestionId = "a";
            QuestionControllerResponseDTO = deleteMultipleQuestion(headerNormal, QuestionId);

            Assert.assertEquals(StatusCode, 401, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getError(), "Unauthorized", "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }


    //@Test (priority = 4)
//    public void publishMediaAssignments() throws Exception{
//        //JSONObject publishMSAssignmentPayload = jsonReader.getJsonObject(publishMediaAssignmentPayload);
//        System.out.println("YT: " + DataStore.getInstance().getMediaIDYouTube());
//        System.out.println("Web: " + DataStore.getInstance().getMediaIDWebLink());
//       // publishMSAssignmentPayload.put("courseId",courseID);
//       // publishMSAssignmentPayload.put("sectionId",courseID);
//
//        JSONArray media = new JSONArray();
//        media.add(DataStore.getInstance().getMediaIDYouTube());
//        media.add(DataStore.getInstance().getMediaIDWebLink());

    // publishMSAssignmentPayload.put("assignmentId", DataStore.getInstance().getMultimediaAssignmentYouTube() );
    // publishMSAssignmentPayload.put("title", DataStore.getInstance().getMultimediaAssignmentNameYouTube() );
    //  publishMSAssignmentPayload.put("media", media);
    // System.out.println("publishMSAssignmentPayload: " + publishMSAssignmentPayload);
    //  Response response = RESTServiceBase.putCallWithHeaderAndBodyParam(header,String.valueOf(publishMSAssignmentPayload),publishMediaAssignmentUrl + "mediaAssignments/" + DataStore.getInstance().getMultimediaAssignmentYouTube());
//        LoggerUtil.log(response.asString());
//        Assert.assertEquals(response.statusCode(), 200);
//        System.out.println(response);
//        System.out.println("Response Code is: " + response.statusCode());
//       String titleName = "MMA with You Tube N Web Link "+ GetCurrentTime.getCurrentTimestamp();
//        System.out.println("Media Assignment is:" + titleName);
//        DataStore.getInstance().setMultimediaAssignmentNameYouTube(titleName);


    //   ---------------------------------------------
// SFC-15449 - Add “chapter id” to question object
    //@Test(priority = 2)
    @AlmAnnotation(almTestId = "609861")
    public void CreateQuestionWithchapterId() throws Exception {
        try {
            createSingleDeckWithFullPayload();

            // Read json object from file
            JSONObject createQuestionPayload = jsonReader.getJsonObject(QuestionCreatePayload);
            System.out.println(createQuestionPayload);
            createQuestionPayload.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());
            createQuestionPayload.put("chapterId", "123");

            String payload = String.valueOf(createQuestionPayload);
            //QuestionControllerRequestDTO = mapper.readValue(payload, QuestionControllerRequestDTO.class);
            QuestionControllerResponseDTO = createQuestion(headerNormal, payload);

            Assert.assertEquals(StatusCode, 201, "Status Code");
            //LOGGER.info("Tenant Id:" + QuestionControllerResponseDTO.getQuestions()[0].getI);

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    //Verifying whether the cards cannot be saved without question prompt
    //@Test(priority = 9)
    @AlmAnnotation(almTestId = "609861")
    public void CreateQuestionWithoutQuestionFieldWithchapterId() throws Exception {
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
            DeckField.put("chapterId", "123");

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
    //@Test(priority = 10)
    @AlmAnnotation(almTestId = "609861")
    public void CreateQuestionWithoutValueFieldWithchapterId() throws Exception {
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
            DeckField.put("chapterId", "123");

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

    //@Test(priority = 11)
    @AlmAnnotation(almTestId = "609861")
    public void CreateQuestionWithEmptyValueForDeckIdWithchapterId() throws Exception {
        try {
            JSONObject createQuestionPayload = jsonReader.getJsonObject(QuestionCreatePayload);
            System.out.println(createQuestionPayload);
            createQuestionPayload.put("deckId", "");
            createQuestionPayload.put("chapterId", "123");

            String payload = String.valueOf(createQuestionPayload);
            QuestionControllerResponseDTO = createQuestion(headerNormal, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getFieldErrors()[0].getMessage(), "deckId Cannot be null or empty", "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    //@Test(priority = 12)
    @AlmAnnotation(almTestId = "609861")
    public void CreateQuestionWithInvalidValueForDeckIdWithchapterId() throws Exception {
        try {
            JSONObject createQuestionPayload = jsonReader.getJsonObject(QuestionCreatePayload);
            System.out.println(createQuestionPayload);
            createQuestionPayload.put("deckId", "1234");
            createQuestionPayload.put("chapterId", "123");

            String payload = String.valueOf(createQuestionPayload);
            QuestionControllerResponseDTO = createQuestion(headerNormal, payload);

            Assert.assertEquals(StatusCode, 404, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getDescription(), "Invalid Deck ID", "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    //@Test(priority = 13)
    @AlmAnnotation(almTestId = "609861")
    public void CreateQuestionWithDeletedDeckIdWithchapterId() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            JSONObject createQuestionPayload = jsonReader.getJsonObject(QuestionCreatePayload);
            System.out.println(createQuestionPayload);
            createQuestionPayload.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());
            createQuestionPayload.put("chapterId", "123");
            DeleteDeckWithFullPayload();

            String payload = String.valueOf(createQuestionPayload);
            QuestionControllerResponseDTO = createQuestion(headerNormal, payload);

            Assert.assertEquals(StatusCode, 404, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getDescription(), "Invalid Deck ID", "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    //@Test(priority = 14)
    @AlmAnnotation(almTestId = "609861")
    public void CreateQuestionWithDifferentUserWithchapterId() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            JSONObject createQuestionPayload = jsonReader.getJsonObject(QuestionCreatePayload);
            System.out.println(createQuestionPayload);
            createQuestionPayload.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());
            createQuestionPayload.put("chapterId", "123");

            String payload = String.valueOf(createQuestionPayload);
            QuestionControllerResponseDTO = createQuestion(header, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getDescription(), "User IDs do not match", "Status Code");
            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    //@Test(priority = 15)
    @AlmAnnotation(almTestId = "609861")
    public void CreateQuestionWithNullValueforCorrectFieldWithchapterId() throws Exception {
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
            DeckField.put("chapterId", "123");

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

    //@Test(priority = 16)
    @AlmAnnotation(almTestId = "609861")
    public void CreateQuestionWithNullValueforIdFieldWithchapterId() throws Exception {
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
            DeckField.put("chapterId", "123");

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

    //@Test(priority = 17)
    @AlmAnnotation(almTestId = "609861")
    public void CreateQuestionWithNullValueforAnswerTypeFieldWithchapterId() throws Exception {
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
            DeckField.put("chapterId", "123");

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

    //@Test(priority = 18)
    @AlmAnnotation(almTestId = "609861")
    public void CreateQuestionWithNullValueforAnswersFieldWithchapterId() throws Exception {
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
            DeckField.put("chapterId", "123");

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

    //@Test(priority = 19)
    @AlmAnnotation(almTestId = "609861")
    public void CreateQuestionWithNullValueforSourceFieldWithchapterId() throws Exception {
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
            DeckField.put("chapterId", "123");

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

    //@Test(priority = 20)
    @AlmAnnotation(almTestId = "609861")
    public void CreateQuestionWithNullValueforTypeFieldWithchapterId() throws Exception {
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
            DeckField.put("chapterId", "123");

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

    //@Test(priority = 21)
    @AlmAnnotation(almTestId = "609861")
    public void CreateQuestionWithNullValueforPlatformFieldWithchapterId() throws Exception {
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
            DeckField.put("chapterId", "123");

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

    //@Test(priority = 22)
    @AlmAnnotation(almTestId = "609861")
    public void CreateQuestionWithNullValueforKindFieldWithchapterId() throws Exception {
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
            DeckField.put("chapterId", "123");

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

    //@Test(priority = 23)
    @AlmAnnotation(almTestId = "609861")
    public void CreateQuestionWithoutArchivedandFavouriteFieldWithchapterId() throws Exception {
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
            DeckField.put("chapterId", "123");

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

    //@Test(priority = 24)
    @AlmAnnotation(almTestId = "609861")
    public void CreateQuestionWithInvalidTenantIdWithchapterId() throws Exception {
        try {
            headerNormal.put("X-TenantId", "a");
            JSONObject createQuestionPayload = jsonReader.getJsonObject(QuestionCreatePayload);
            System.out.println(createQuestionPayload);
            createQuestionPayload.put("deckId", "a");
            createQuestionPayload.put("chapterId", "123");

            String payload = String.valueOf(createQuestionPayload);
            QuestionControllerResponseDTO = createQuestion(headerNormal, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getDescription(), "Field 'tenantId' does not have a valid value", "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    //@Test(priority = 25)
    @AlmAnnotation(almTestId = "609861")
    public void CreateQuestionWithInvalidTenantKeyWithchapterId() throws Exception {
        try {
            getData();
            headerNormal.put("X-TenantKey", "a");
            JSONObject createQuestionPayload = jsonReader.getJsonObject(QuestionCreatePayload);
            System.out.println(createQuestionPayload);
            createQuestionPayload.put("deckId", "a");
            createQuestionPayload.put("chapterId", "123");

            String payload = String.valueOf(createQuestionPayload);
            QuestionControllerResponseDTO = createQuestion(headerNormal, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getDescription(), "Field 'tenantKey' does not have a valid value", "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    //-----------
    //@Test(priority = 3)
    @AlmAnnotation(almTestId = "609180")
    public void GetQuestionByQuestionIdWithchapterId() throws Exception {

        try {
            createSingleDeckWithFullPayload();

            // Read json object from file
            JSONObject createQuestionPayload = jsonReader.getJsonObject(QuestionCreatePayload);
            System.out.println(createQuestionPayload);
            createQuestionPayload.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());
            createQuestionPayload.put("chapterId", "123");

            String payload = String.valueOf(createQuestionPayload);
            //QuestionControllerRequestDTO = mapper.readValue(payload, QuestionControllerRequestDTO.class);
            QuestionControllerResponseDTO = createQuestion(headerNormal, payload);

            // Assert.assertEquals(StatusCode, 201, "Status Code");
            //LOGGER.info("Tenant Id:" + QuestionControllerResponseDTO.getQuestions()[0].getI);


            String QuestionId = QuestionControllerResponseDTO.getQuestions()[0].getId();
            QuestionDTO = getQuestionByQuestionId(headerNormal, QuestionId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            DeleteDeckWithFullPayload();

        } catch (Exception e) {
            throw e;
        }
    }

    //@Test(priority = 4)
    @AlmAnnotation(almTestId = "609180")
    public void GetQuestionByDeckIdWithchapterId() throws Exception {


        try {
            createSingleDeckWithFullPayload();

            // Read json object from file
            JSONObject createQuestionPayload = jsonReader.getJsonObject(QuestionCreatePayload);
            System.out.println(createQuestionPayload);
            createQuestionPayload.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());
            createQuestionPayload.put("chapterId", "123");

            String payload = String.valueOf(createQuestionPayload);
            //QuestionControllerRequestDTO = mapper.readValue(payload, QuestionControllerRequestDTO.class);
            QuestionControllerResponseDTO = createQuestion(headerNormal, payload);

            // Assert.assertEquals(StatusCode, 201, "Status Code");
            //LOGGER.info("Tenant Id:" + QuestionControllerResponseDTO.getQuestions()[0].getI);

            queryparam.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());
            QuestionControllerResponseDTO = getQuestionByDeckId(headerNormal, queryparam);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            DeleteDeckWithFullPayload();

        } catch (Exception e) {
            throw e;
        }
    }

    //@Test(priority = 5) @AlmAnnotation(almTestId = "611101")
    public void UpdateQuestionWithchapterId() throws Exception {


        try {

            createSingleDeckWithFullPayload();

            // Read json object from file
            JSONObject createQuestionPayload = jsonReader.getJsonObject(QuestionCreatePayload);
            System.out.println(createQuestionPayload);
            createQuestionPayload.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());
            createQuestionPayload.put("chapterId", "123");

            String payload = String.valueOf(createQuestionPayload);
            //QuestionControllerRequestDTO = mapper.readValue(payload, QuestionControllerRequestDTO.class);
            QuestionControllerResponseDTO = createQuestion(headerNormal, payload);

            // Assert.assertEquals(StatusCode, 201, "Status Code");
            //LOGGER.info("Tenant Id:" + QuestionControllerResponseDTO.getQuestions()[0].getI);

            long currentTimestamp = System.currentTimeMillis();
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
            DeckField.put("chapterId", "123");
            DeckField.put("kind", "SHORT_ANSWER");
            DeckField.put("platform", "Mobile");
            DeckField.put("source", "App");
            DeckField.put("type", "Manual");
            DeckField.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());
            DeckField.put("id", QuestionControllerResponseDTO.getQuestions()[0].getId());

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            updateQuestionPayload.put("questions", decks);

            String payload1 = String.valueOf(updateQuestionPayload);
            //QuestionControllerRequestDTO = mapper.readValue(payload, QuestionControllerRequestDTO.class);

            QuestionControllerResponseDTO = updateQuestion(headerNormal, payload1, currentTimestamp);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            DeleteDeckWithFullPayload();

        } catch (Exception e) {
            throw e;
        }
    }

    //@Test(priority = 6)
    @AlmAnnotation(almTestId = "611108")
    public void DeleteSingleQuestionWithchapterId() throws Exception {

        try {
            createSingleDeckWithFullPayload();

            // Read json object from file
            JSONObject createQuestionPayload = jsonReader.getJsonObject(QuestionCreatePayload);
            System.out.println(createQuestionPayload);
            createQuestionPayload.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());
            createQuestionPayload.put("chapterId", "123");

            String payload = String.valueOf(createQuestionPayload);
            //QuestionControllerRequestDTO = mapper.readValue(payload, QuestionControllerRequestDTO.class);
            QuestionControllerResponseDTO = createQuestion(headerNormal, payload);

            // Assert.assertEquals(StatusCode, 201, "Status Code");
            //LOGGER.info("Tenant Id:" + QuestionControllerResponseDTO.getQuestions()[0].getI);

            String QuestionId = QuestionControllerResponseDTO.getQuestions()[0].getId();
            QuestionControllerResponseDTO = deleteQuestion(headerNormal, QuestionId);

            Assert.assertEquals(StatusCode, 204, "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    //@Test(priority = 7)
    @AlmAnnotation(almTestId = "611108")
    public void DeleteMultipleQuestionWithchapterId() throws Exception {

        try {
            createSingleDeckWithFullPayload();

            // Read json object from file
            JSONObject createQuestionPayload = jsonReader.getJsonObject(QuestionCreatePayload);
            System.out.println(createQuestionPayload);
            createQuestionPayload.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());
            createQuestionPayload.put("chapterId", "123");

            String payload = String.valueOf(createQuestionPayload);
            //QuestionControllerRequestDTO = mapper.readValue(payload, QuestionControllerRequestDTO.class);
            QuestionControllerResponseDTO = createQuestion(headerNormal, payload);

            // Assert.assertEquals(StatusCode, 201, "Status Code");
            //LOGGER.info("Tenant Id:" + QuestionControllerResponseDTO.getQuestions()[0].getI);

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

    //
//    //   ---------------------------------------------
//// SFC-16721 - Add “extendedProperrties” to question object
    @Test(priority = 84)
    @AlmAnnotation(almTestId = "669191")
    public void CreateQuestionWithextendedProperties() throws Exception {
        try {
            getData();
            createSingleDeckWithFullPayload();

            // Read json object from file
            JSONObject createQuestionPayload = jsonReader.getJsonObject(QuestionCreatePayload);
            System.out.println(createQuestionPayload);
            createQuestionPayload.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());

            JSONObject extendedProperties1 = new JSONObject();
            extendedProperties1.put("pageId1", "123");
            extendedProperties1.put("pageId2", "TEXT");
            extendedProperties1.put("pageId3", "a");
            extendedProperties1.put("pageId4", "asd");

            JSONObject answers1 = new JSONObject();
            answers1.put("type", "TEXT");
            answers1.put("id", 1);
            answers1.put("value", "This is the answer");
            answers1.put("correct", true);
            JSONArray answers = new JSONArray();
            answers.add(answers1);

            JSONObject DeckField = new JSONObject();
            DeckField.put("answers", answers);
            DeckField.put("archived", true);
            DeckField.put("creatorId", "0f1d72fbc8a4412ba1ac8b8970eeffd5");
            DeckField.put("favourite", false);
            DeckField.put("kind", "ALL");
            DeckField.put("platform", "Mobile");
            DeckField.put("question", "string");
            DeckField.put("questionMedia", "TEXT");
            DeckField.put("source", "App");
            DeckField.put("type", "Manual");
            DeckField.put("chapterId", "123");
            DeckField.put("extendedProperties", extendedProperties1);

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            createQuestionPayload.put("questions", decks);

            String payload = String.valueOf(createQuestionPayload);
            //QuestionControllerRequestDTO = mapper.readValue(payload, QuestionControllerRequestDTO.class);
            QuestionControllerResponseDTO = createQuestion(headerNormal, payload);

            Assert.assertEquals(StatusCode, 201, "Status Code");
            //LOGGER.info("Tenant Id:" + QuestionControllerResponseDTO.getQuestions()[0].getI);

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 85)
    @AlmAnnotation(almTestId = "669205")
    public void CreateQuestionWithEmptyextendedProperties() throws Exception {
        try {
            createSingleDeckWithFullPayload();

            // Read json object from file
            JSONObject createQuestionPayload = jsonReader.getJsonObject(QuestionCreatePayload);
            System.out.println(createQuestionPayload);
            createQuestionPayload.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());

            JSONObject extendedProperties1 = new JSONObject();
         /*   extendedProperties1.put("pageId1", "123");
            extendedProperties1.put("pageId2", "TEXT");
            extendedProperties1.put("pageId3", "a");
            extendedProperties1.put("pageId4", "asd");*/

            JSONObject answers1 = new JSONObject();
            answers1.put("type", "TEXT");
            answers1.put("id", 1);
            answers1.put("value", "This is the answer");
            answers1.put("correct", true);
            JSONArray answers = new JSONArray();
            answers.add(answers1);

            JSONObject DeckField = new JSONObject();
            DeckField.put("answers", answers);
            DeckField.put("archived", true);
            DeckField.put("creatorId", "0f1d72fbc8a4412ba1ac8b8970eeffd5");
            DeckField.put("favourite", false);
            DeckField.put("kind", "ALL");
            DeckField.put("platform", "Mobile");
            DeckField.put("question", "string");
            DeckField.put("questionMedia", "TEXT");
            DeckField.put("source", "App");
            DeckField.put("type", "Manual");
            DeckField.put("chapterId", "123");
            DeckField.put("extendedProperties", extendedProperties1);

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            createQuestionPayload.put("questions", decks);

            String payload = String.valueOf(createQuestionPayload);
            //QuestionControllerRequestDTO = mapper.readValue(payload, QuestionControllerRequestDTO.class);
            QuestionControllerResponseDTO = createQuestion(headerNormal, payload);

            Assert.assertEquals(StatusCode, 201, "Status Code");
            //LOGGER.info("Tenant Id:" + QuestionControllerResponseDTO.getQuestions()[0].getI);

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 86)
    @AlmAnnotation(almTestId = "672362")
    public void CreateQuestionWithIntegerForextendedPropertiesfield() throws Exception {
        try {
            createSingleDeckWithFullPayload();

            // Read json object from file
            JSONObject createQuestionPayload = jsonReader.getJsonObject(QuestionCreatePayload);
            System.out.println(createQuestionPayload);
            createQuestionPayload.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());

            JSONObject extendedProperties1 = new JSONObject();
            extendedProperties1.put("pageId1", 123);
            extendedProperties1.put("pageId2", "TEXT");
            extendedProperties1.put("pageId3", "a");
            extendedProperties1.put("pageId4", "asd");

            JSONObject answers1 = new JSONObject();
            answers1.put("type", "TEXT");
            answers1.put("id", 1);
            answers1.put("value", "This is the answer");
            answers1.put("correct", true);
            JSONArray answers = new JSONArray();
            answers.add(answers1);

            JSONObject DeckField = new JSONObject();
            DeckField.put("answers", answers);
            DeckField.put("archived", true);
            DeckField.put("creatorId", "0f1d72fbc8a4412ba1ac8b8970eeffd5");
            DeckField.put("favourite", false);
            DeckField.put("kind", "ALL");
            DeckField.put("platform", "Mobile");
            DeckField.put("question", "string");
            DeckField.put("questionMedia", "TEXT");
            DeckField.put("source", "App");
            DeckField.put("type", "Manual");
            DeckField.put("chapterId", "123");
            DeckField.put("extendedProperties", extendedProperties1);

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            createQuestionPayload.put("questions", decks);

            String payload = String.valueOf(createQuestionPayload);
            //QuestionControllerRequestDTO = mapper.readValue(payload, QuestionControllerRequestDTO.class);
            QuestionControllerResponseDTO = createQuestion(headerNormal, payload);

            Assert.assertEquals(StatusCode, 201, "Status Code");
            //LOGGER.info("Tenant Id:" + QuestionControllerResponseDTO.getQuestions()[0].getI);

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 87)
    @AlmAnnotation(almTestId = "672363")
    public void CreateQuestionWithMaximumcharactersForextendedPropertiesfield() throws Exception {
        try {
            createSingleDeckWithFullPayload();

            // Read json object from file
            JSONObject createQuestionPayload = jsonReader.getJsonObject(QuestionCreatePayload);
            System.out.println(createQuestionPayload);
            createQuestionPayload.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());

            JSONObject extendedProperties1 = new JSONObject();
            extendedProperties1.put("pageId1", "dsWVVyxhW3Z73yQF1sadGLioCL2yAn7057YzrTqnMxPqTkPNQ3q9WLQpqLPmBpO7gNSZWAcGFVzXDIwPykzbGZSbYRgn4l2U3J1dJXq5hcseMZtGa4ePf7BRvkA8C16of7G0QG7l9l9UFUc7VKinWNbxAMrH1x1ojamXk3lkabv7dK7wG644H8upg31JZzoV6DoFa93uAlc7iUdjm6kU4vVV4cKF4ziEQDmnJGouYjpjGItfP3V49sVBj4V7ynev");
            extendedProperties1.put("pageId2", "TEXT");
            extendedProperties1.put("pageId3", "a");
            extendedProperties1.put("pageId4", "asd");

            JSONObject answers1 = new JSONObject();
            answers1.put("type", "TEXT");
            answers1.put("id", 1);
            answers1.put("value", "This is the answer");
            answers1.put("correct", true);
            JSONArray answers = new JSONArray();
            answers.add(answers1);

            JSONObject DeckField = new JSONObject();
            DeckField.put("answers", answers);
            DeckField.put("archived", true);
            DeckField.put("creatorId", "0f1d72fbc8a4412ba1ac8b8970eeffd5");
            DeckField.put("favourite", false);
            DeckField.put("kind", "ALL");
            DeckField.put("platform", "Mobile");
            DeckField.put("question", "string");
            DeckField.put("questionMedia", "TEXT");
            DeckField.put("source", "App");
            DeckField.put("type", "Manual");
            DeckField.put("chapterId", "123");
            DeckField.put("extendedProperties", extendedProperties1);

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            createQuestionPayload.put("questions", decks);

            String payload = String.valueOf(createQuestionPayload);
            //QuestionControllerRequestDTO = mapper.readValue(payload, QuestionControllerRequestDTO.class);
            QuestionControllerResponseDTO = createQuestion(headerNormal, payload);

            Assert.assertEquals(StatusCode, 201, "Status Code");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 88)
    @AlmAnnotation(almTestId = "672373")
    public void CreateQuestionWithEmptyextendedPropertiesfield() throws Exception {
        try {
            createSingleDeckWithFullPayload();

            // Read json object from file
            JSONObject createQuestionPayload = jsonReader.getJsonObject(QuestionCreatePayload);
            System.out.println(createQuestionPayload);
            createQuestionPayload.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());

            JSONObject extendedProperties1 = new JSONObject();
            extendedProperties1.put("pageId1", "");
            extendedProperties1.put("pageId2", "TEXT");
            extendedProperties1.put("pageId3", "a");
            extendedProperties1.put("pageId4", "asd");

            JSONObject answers1 = new JSONObject();
            answers1.put("type", "TEXT");
            answers1.put("id", 1);
            answers1.put("value", "This is the answer");
            answers1.put("correct", true);
            JSONArray answers = new JSONArray();
            answers.add(answers1);

            JSONObject DeckField = new JSONObject();
            DeckField.put("answers", answers);
            DeckField.put("archived", true);
            DeckField.put("creatorId", "0f1d72fbc8a4412ba1ac8b8970eeffd5");
            DeckField.put("favourite", false);
            DeckField.put("kind", "ALL");
            DeckField.put("platform", "Mobile");
            DeckField.put("question", "string");
            DeckField.put("questionMedia", "TEXT");
            DeckField.put("source", "App");
            DeckField.put("type", "Manual");
            DeckField.put("chapterId", "123");
            DeckField.put("extendedProperties", extendedProperties1);

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            createQuestionPayload.put("questions", decks);

            String payload = String.valueOf(createQuestionPayload);
            //QuestionControllerRequestDTO = mapper.readValue(payload, QuestionControllerRequestDTO.class);
            QuestionControllerResponseDTO = createQuestion(headerNormal, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getMessage(), "error.validation", "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getDescription(), "Key/Value cannot be null or empty", "Status Code");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 89)
    @AlmAnnotation(almTestId = "672380")
    public void CreateQuestionWithNullForextendedPropertiesfield() throws Exception {
        try {
            createSingleDeckWithFullPayload();

            // Read json object from file
            JSONObject createQuestionPayload = jsonReader.getJsonObject(QuestionCreatePayload);
            System.out.println(createQuestionPayload);
            createQuestionPayload.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());

            JSONObject extendedProperties1 = new JSONObject();
            extendedProperties1.put("pageId1", null);
            extendedProperties1.put("pageId2", "TEXT");
            extendedProperties1.put("pageId3", "a");
            extendedProperties1.put("pageId4", "asd");

            JSONObject answers1 = new JSONObject();
            answers1.put("type", "TEXT");
            answers1.put("id", 1);
            answers1.put("value", "This is the answer");
            answers1.put("correct", true);
            JSONArray answers = new JSONArray();
            answers.add(answers1);

            JSONObject DeckField = new JSONObject();
            DeckField.put("answers", answers);
            DeckField.put("archived", true);
            DeckField.put("creatorId", "0f1d72fbc8a4412ba1ac8b8970eeffd5");
            DeckField.put("favourite", false);
            DeckField.put("kind", "ALL");
            DeckField.put("platform", "Mobile");
            DeckField.put("question", "string");
            DeckField.put("questionMedia", "TEXT");
            DeckField.put("source", "App");
            DeckField.put("type", "Manual");
            DeckField.put("chapterId", "123");
            DeckField.put("extendedProperties", extendedProperties1);

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            createQuestionPayload.put("questions", decks);

            String payload = String.valueOf(createQuestionPayload);
            //QuestionControllerRequestDTO = mapper.readValue(payload, QuestionControllerRequestDTO.class);
            QuestionControllerResponseDTO = createQuestion(headerNormal, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getMessage(), "error.validation", "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getDescription(), "Key/Value cannot be null or empty", "Status Code");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 90)
    @AlmAnnotation(almTestId = "672381")
    public void CreateQuestionWit257charactersForextendedPropertiesfield() throws Exception {
        try {
            createSingleDeckWithFullPayload();

            // Read json object from file
            JSONObject createQuestionPayload = jsonReader.getJsonObject(QuestionCreatePayload);
            System.out.println(createQuestionPayload);
            createQuestionPayload.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());

            String pageId = "dsWVVyxhW3Z73yQF1sadGLioCL2yAn7057YzrTqnMxPqTkPNQ3q9WLQpqLPmBpO7gNSZWAcGFVzXDIwPykzbGZSbYRgn4l2U3J1dJXq5hcseMZtGa4ePf7BRvkA8C16of7G0QG7l9l9UFUc7VKinWNbxAMrH1x1ojamXk3lkabv7dK7wG644H8upg31JZzoV6DoFa93uAlc7iUdjm6kU4vVV4cKF4ziEQDmnJGouYjpjGItfP3V49sVBj4V7ynev1";
            JSONObject extendedProperties1 = new JSONObject();
            extendedProperties1.put("pageId1", pageId);
            extendedProperties1.put("pageId2", "TEXT");
            extendedProperties1.put("pageId3", "a");
            extendedProperties1.put("pageId4", "asd");

            JSONObject answers1 = new JSONObject();
            answers1.put("type", "TEXT");
            answers1.put("id", 1);
            answers1.put("value", "This is the answer");
            answers1.put("correct", true);
            JSONArray answers = new JSONArray();
            answers.add(answers1);

            JSONObject DeckField = new JSONObject();
            DeckField.put("answers", answers);
            DeckField.put("archived", true);
            DeckField.put("creatorId", "0f1d72fbc8a4412ba1ac8b8970eeffd5");
            DeckField.put("favourite", false);
            DeckField.put("kind", "ALL");
            DeckField.put("platform", "Mobile");
            DeckField.put("question", "string");
            DeckField.put("questionMedia", "TEXT");
            DeckField.put("source", "App");
            DeckField.put("type", "Manual");
            DeckField.put("chapterId", "123");
            DeckField.put("extendedProperties", extendedProperties1);

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            createQuestionPayload.put("questions", decks);

            String payload = String.valueOf(createQuestionPayload);
            //QuestionControllerRequestDTO = mapper.readValue(payload, QuestionControllerRequestDTO.class);
            QuestionControllerResponseDTO = createQuestion(headerNormal, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getMessage(), "error.validation", "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getDescription(), "Value("+pageId+") cannot have more than 256 characters", "Status Code");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 91)
    @AlmAnnotation(almTestId = "672383")
    public void CreateQuestionWith_Six_extendedPropertiesfield() throws Exception {
        try {
            createSingleDeckWithFullPayload();

            // Read json object from file
            JSONObject createQuestionPayload = jsonReader.getJsonObject(QuestionCreatePayload);
            System.out.println(createQuestionPayload);
            createQuestionPayload.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());

            JSONObject extendedProperties1 = new JSONObject();
            extendedProperties1.put("pageId1", "123");
            extendedProperties1.put("pageId2", "TEXT");
            extendedProperties1.put("pageId3", "a");
            extendedProperties1.put("pageId4", "asd");
            extendedProperties1.put("pageId5", "TEXT");
            extendedProperties1.put("pageId6", "a");

            JSONObject answers1 = new JSONObject();
            answers1.put("type", "TEXT");
            answers1.put("id", 1);
            answers1.put("value", "This is the answer");
            answers1.put("correct", true);
            JSONArray answers = new JSONArray();
            answers.add(answers1);

            JSONObject DeckField = new JSONObject();
            DeckField.put("answers", answers);
            DeckField.put("archived", true);
            DeckField.put("creatorId", "0f1d72fbc8a4412ba1ac8b8970eeffd5");
            DeckField.put("favourite", false);
            DeckField.put("kind", "ALL");
            DeckField.put("platform", "Mobile");
            DeckField.put("question", "string");
            DeckField.put("questionMedia", "TEXT");
            DeckField.put("source", "App");
            DeckField.put("type", "Manual");
            DeckField.put("chapterId", "123");
            DeckField.put("extendedProperties", extendedProperties1);

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            createQuestionPayload.put("questions", decks);

            String payload = String.valueOf(createQuestionPayload);
            //QuestionControllerRequestDTO = mapper.readValue(payload, QuestionControllerRequestDTO.class);
            QuestionControllerResponseDTO = createQuestion(headerNormal, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getMessage(), "error.validation", "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getFieldErrors()[0].getField(), "questions[0].extendedProperties", "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getFieldErrors()[0].getMessage(), "Can have maximum of 5 extended properties", "Status Code");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 92)
    @AlmAnnotation(almTestId = "669218")
    public void UpdateQuestionbyAddingextendedProperties() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();

            JSONObject updateQuestionPayload = jsonReader.getJsonObject(QuestionUpdatePayload);
            updateQuestionPayload.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());

            JSONObject extendedProperties1 = new JSONObject();
            extendedProperties1.put("pageId1", "123");
            extendedProperties1.put("pageId2", "TEXT");
            extendedProperties1.put("pageId3", "a");
            extendedProperties1.put("pageId4", "asd");
            extendedProperties1.put("pageId5", "TEXT");


            JSONObject answers1 = new JSONObject();
            answers1.put("type", "TEXT");
            answers1.put("id", 1);
            answers1.put("value", "This is the answer");
            answers1.put("correct", true);
            JSONArray answers = new JSONArray();
            answers.add(answers1);

            JSONObject DeckField = new JSONObject();
            DeckField.put("answers", answers);
           // DeckField.put("questionMedia", "TEXT");
            DeckField.put("question", "Question");
            DeckField.put("kind", "SHORT_ANSWER");
            DeckField.put("type", "Manual");
            DeckField.put("platform", "Mobile");
            DeckField.put("source", "App");
            DeckField.put("chapterId", "123");
            DeckField.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());
            DeckField.put("id", QuestionControllerResponseDTO.getQuestions()[0].getId());
            DeckField.put("extendedProperties", extendedProperties1);

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            updateQuestionPayload.put("questions", decks);

            String payload = String.valueOf(updateQuestionPayload);
            //QuestionControllerRequestDTO = mapper.readValue(payload, QuestionControllerRequestDTO.class);
            long Timestamp = System.currentTimeMillis();
            System.out.println("Time:"+Timestamp);
            QuestionControllerResponseDTO = updateQuestion(headerNormal, payload, System.currentTimeMillis());

            Assert.assertEquals(StatusCode, 200, "Status Code");
            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 93)
    @AlmAnnotation(almTestId = "672541")
    public void UpdateQuestionWithEmptyextendedProperties() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();

            JSONObject updateQuestionPayload = jsonReader.getJsonObject(QuestionUpdatePayload);
            updateQuestionPayload.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());

            JSONObject extendedProperties1 = new JSONObject();
           /* extendedProperties1.put("pageId1", "123");
            extendedProperties1.put("pageId2", "TEXT");
            extendedProperties1.put("pageId3", "a");
            extendedProperties1.put("pageId4", "asd");
            extendedProperties1.put("pageId5", "TEXT");*/


            JSONObject answers1 = new JSONObject();
            answers1.put("type", "TEXT");
            answers1.put("id", 1);
            answers1.put("value", "This is the answer");
            answers1.put("correct", true);
            JSONArray answers = new JSONArray();
            answers.add(answers1);

            JSONObject DeckField = new JSONObject();
            DeckField.put("answers", answers);
            // DeckField.put("questionMedia", "TEXT");
            DeckField.put("question", "Question");
            DeckField.put("kind", "SHORT_ANSWER");
            DeckField.put("type", "Manual");
            DeckField.put("platform", "Mobile");
            DeckField.put("source", "App");
            DeckField.put("chapterId", "123");
            DeckField.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());
            DeckField.put("id", QuestionControllerResponseDTO.getQuestions()[0].getId());
            DeckField.put("extendedProperties", extendedProperties1);

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            updateQuestionPayload.put("questions", decks);

            String payload = String.valueOf(updateQuestionPayload);
            //QuestionControllerRequestDTO = mapper.readValue(payload, QuestionControllerRequestDTO.class);
            long Timestamp = System.currentTimeMillis();
            System.out.println("Time:"+Timestamp);
            QuestionControllerResponseDTO = updateQuestion(headerNormal, payload, System.currentTimeMillis());

            Assert.assertEquals(StatusCode, 200, "Status Code");
            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 94)
    @AlmAnnotation(almTestId = "672542")
    public void UpdateQuestionWithIntegerForextendedProperties() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();

            JSONObject updateQuestionPayload = jsonReader.getJsonObject(QuestionUpdatePayload);
            updateQuestionPayload.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());

            JSONObject extendedProperties1 = new JSONObject();
            extendedProperties1.put("pageId1", 123);
            extendedProperties1.put("pageId2", "TEXT");
            extendedProperties1.put("pageId3", "a");
            extendedProperties1.put("pageId4", "asd");
            extendedProperties1.put("pageId5", "TEXT");


            JSONObject answers1 = new JSONObject();
            answers1.put("type", "TEXT");
            answers1.put("id", 1);
            answers1.put("value", "This is the answer");
            answers1.put("correct", true);
            JSONArray answers = new JSONArray();
            answers.add(answers1);

            JSONObject DeckField = new JSONObject();
            DeckField.put("answers", answers);
            // DeckField.put("questionMedia", "TEXT");
            DeckField.put("question", "Question");
            DeckField.put("kind", "SHORT_ANSWER");
            DeckField.put("type", "Manual");
            DeckField.put("platform", "Mobile");
            DeckField.put("source", "App");
            DeckField.put("chapterId", "123");
            DeckField.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());
            DeckField.put("id", QuestionControllerResponseDTO.getQuestions()[0].getId());
            DeckField.put("extendedProperties", extendedProperties1);

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            updateQuestionPayload.put("questions", decks);

            String payload = String.valueOf(updateQuestionPayload);
            //QuestionControllerRequestDTO = mapper.readValue(payload, QuestionControllerRequestDTO.class);
            long Timestamp = System.currentTimeMillis();
            System.out.println("Time:"+Timestamp);
            QuestionControllerResponseDTO = updateQuestion(headerNormal, payload, System.currentTimeMillis());

            Assert.assertEquals(StatusCode, 200, "Status Code");
            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 95)
    @AlmAnnotation(almTestId = "672543")
    public void UpdateQuestionWithMaximumcharactersForextendedProperties() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();

            JSONObject updateQuestionPayload = jsonReader.getJsonObject(QuestionUpdatePayload);
            updateQuestionPayload.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());

            String pageId = "dsWVVyxhW3Z73yQF1sadGLioCL2yAn7057YzrTqnMxPqTkPNQ3q9WLQpqLPmBpO7gNSZWAcGFVzXDIwPykzbGZSbYRgn4l2U3J1dJXq5hcseMZtGa4ePf7BRvkA8C16of7G0QG7l9l9UFUc7VKinWNbxAMrH1x1ojamXk3lkabv7dK7wG644H8upg31JZzoV6DoFa93uAlc7iUdjm6kU4vVV4cKF4ziEQDmnJGouYjpjGItfP3V49sVBj4V7ynev";
            JSONObject extendedProperties1 = new JSONObject();
            extendedProperties1.put("pageId1", pageId);
            extendedProperties1.put("pageId2", "TEXT");
            extendedProperties1.put("pageId3", "a");
            extendedProperties1.put("pageId4", "asd");
            extendedProperties1.put("pageId5", "TEXT");


            JSONObject answers1 = new JSONObject();
            answers1.put("type", "TEXT");
            answers1.put("id", 1);
            answers1.put("value", "This is the answer");
            answers1.put("correct", true);
            JSONArray answers = new JSONArray();
            answers.add(answers1);

            JSONObject DeckField = new JSONObject();
            DeckField.put("answers", answers);
            // DeckField.put("questionMedia", "TEXT");
            DeckField.put("question", "Question");
            DeckField.put("kind", "SHORT_ANSWER");
            DeckField.put("type", "Manual");
            DeckField.put("platform", "Mobile");
            DeckField.put("source", "App");
            DeckField.put("chapterId", "123");
            DeckField.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());
            DeckField.put("id", QuestionControllerResponseDTO.getQuestions()[0].getId());
            DeckField.put("extendedProperties", extendedProperties1);

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            updateQuestionPayload.put("questions", decks);

            String payload = String.valueOf(updateQuestionPayload);
            //QuestionControllerRequestDTO = mapper.readValue(payload, QuestionControllerRequestDTO.class);
            long Timestamp = System.currentTimeMillis();
            System.out.println("Time:"+Timestamp);
            QuestionControllerResponseDTO = updateQuestion(headerNormal, payload, System.currentTimeMillis());

            Assert.assertEquals(StatusCode, 200, "Status Code");
            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 96)
    @AlmAnnotation(almTestId = "669212")
    public void UpdateQuestionWithEmptyextendedPropertiesfield() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();

            JSONObject updateQuestionPayload = jsonReader.getJsonObject(QuestionUpdatePayload);
            updateQuestionPayload.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());

            JSONObject extendedProperties1 = new JSONObject();
            extendedProperties1.put("pageId1", "");
            extendedProperties1.put("pageId2", "TEXT");
            extendedProperties1.put("pageId3", "a");
            extendedProperties1.put("pageId4", "asd");
            extendedProperties1.put("pageId5", "TEXT");


            JSONObject answers1 = new JSONObject();
            answers1.put("type", "TEXT");
            answers1.put("id", 1);
            answers1.put("value", "This is the answer");
            answers1.put("correct", true);
            JSONArray answers = new JSONArray();
            answers.add(answers1);

            JSONObject DeckField = new JSONObject();
            DeckField.put("answers", answers);
            // DeckField.put("questionMedia", "TEXT");
            DeckField.put("question", "Question");
            DeckField.put("kind", "SHORT_ANSWER");
            DeckField.put("type", "Manual");
            DeckField.put("platform", "Mobile");
            DeckField.put("source", "App");
            DeckField.put("chapterId", "123");
            DeckField.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());
            DeckField.put("id", QuestionControllerResponseDTO.getQuestions()[0].getId());
            DeckField.put("extendedProperties", extendedProperties1);

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            updateQuestionPayload.put("questions", decks);

            String payload = String.valueOf(updateQuestionPayload);
            //QuestionControllerRequestDTO = mapper.readValue(payload, QuestionControllerRequestDTO.class);
            long Timestamp = System.currentTimeMillis();
            System.out.println("Time:"+Timestamp);
            QuestionControllerResponseDTO = updateQuestion(headerNormal, payload, System.currentTimeMillis());

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getMessage(), "error.validation", "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getDescription(), "Key/Value cannot be null or empty", "Status Code");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    //Verify whether that user get 400  Bad response error when send the request with null or empty field in extendedProperties
    @Test(priority = 97)
    @AlmAnnotation(almTestId = "669212")
    public void UpdateQuestionWithNullextendedPropertiesfield() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();

            JSONObject updateQuestionPayload = jsonReader.getJsonObject(QuestionUpdatePayload);
            updateQuestionPayload.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());

            JSONObject extendedProperties1 = new JSONObject();
            extendedProperties1.put("pageId1", null);
            extendedProperties1.put("pageId2", "TEXT");
            extendedProperties1.put("pageId3", "a");
            extendedProperties1.put("pageId4", "asd");
            extendedProperties1.put("pageId5", "TEXT");


            JSONObject answers1 = new JSONObject();
            answers1.put("type", "TEXT");
            answers1.put("id", 1);
            answers1.put("value", "This is the answer");
            answers1.put("correct", true);
            JSONArray answers = new JSONArray();
            answers.add(answers1);

            JSONObject DeckField = new JSONObject();
            DeckField.put("answers", answers);
            // DeckField.put("questionMedia", "TEXT");
            DeckField.put("question", "Question");
            DeckField.put("kind", "SHORT_ANSWER");
            DeckField.put("type", "Manual");
            DeckField.put("platform", "Mobile");
            DeckField.put("source", "App");
            DeckField.put("chapterId", "123");
            DeckField.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());
            DeckField.put("id", QuestionControllerResponseDTO.getQuestions()[0].getId());
            DeckField.put("extendedProperties", extendedProperties1);

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            updateQuestionPayload.put("questions", decks);

            String payload = String.valueOf(updateQuestionPayload);
            //QuestionControllerRequestDTO = mapper.readValue(payload, QuestionControllerRequestDTO.class);
            long Timestamp = System.currentTimeMillis();
            System.out.println("Time:"+Timestamp);
            QuestionControllerResponseDTO = updateQuestion(headerNormal, payload, System.currentTimeMillis());

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getMessage(), "error.validation", "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getDescription(), "Key/Value cannot be null or empty", "Status Code");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    //Verify whether that user get 400  Bad response error when update with more than 256 characters on field in extendedProperties
    @Test(priority = 98)
    @AlmAnnotation(almTestId = "669216")
    public void UpdateQuestionWith257charactersForextendedPropertiesfield() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();

            JSONObject updateQuestionPayload = jsonReader.getJsonObject(QuestionUpdatePayload);
            updateQuestionPayload.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());

            String pageId = "AdsWVVyxhW3Z73yQF1sadGLioCL2yAn7057YzrTqnMxPqTkPNQ3q9WLQpqLPmBpO7gNSZWAcGFVzXDIwPykzbGZSbYRgn4l2U3J1dJXq5hcseMZtGa4ePf7BRvkA8C16of7G0QG7l9l9UFUc7VKinWNbxAMrH1x1ojamXk3lkabv7dK7wG644H8upg31JZzoV6DoFa93uAlc7iUdjm6kU4vVV4cKF4ziEQDmnJGouYjpjGItfP3V49sVBj4V7ynev";
            JSONObject extendedProperties1 = new JSONObject();
            extendedProperties1.put("pageId1", pageId);
            extendedProperties1.put("pageId2", "TEXT");
            extendedProperties1.put("pageId3", "a");
            extendedProperties1.put("pageId4", "asd");
            extendedProperties1.put("pageId5", "TEXT");


            JSONObject answers1 = new JSONObject();
            answers1.put("type", "TEXT");
            answers1.put("id", 1);
            answers1.put("value", "This is the answer");
            answers1.put("correct", true);
            JSONArray answers = new JSONArray();
            answers.add(answers1);

            JSONObject DeckField = new JSONObject();
            DeckField.put("answers", answers);
            // DeckField.put("questionMedia", "TEXT");
            DeckField.put("question", "Question");
            DeckField.put("kind", "SHORT_ANSWER");
            DeckField.put("type", "Manual");
            DeckField.put("platform", "Mobile");
            DeckField.put("source", "App");
            DeckField.put("chapterId", "123");
            DeckField.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());
            DeckField.put("id", QuestionControllerResponseDTO.getQuestions()[0].getId());
            DeckField.put("extendedProperties", extendedProperties1);

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            updateQuestionPayload.put("questions", decks);

            String payload = String.valueOf(updateQuestionPayload);
            //QuestionControllerRequestDTO = mapper.readValue(payload, QuestionControllerRequestDTO.class);
            long Timestamp = System.currentTimeMillis();
            System.out.println("Time:"+Timestamp);
            QuestionControllerResponseDTO = updateQuestion(headerNormal, payload, System.currentTimeMillis());

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getMessage(), "error.validation", "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getDescription(), "Value("+pageId+") cannot have more than 256 characters", "Status Code");
            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    //Verify whether that user get 400  Bad response error when update with more than 5 fields in extendedProperties
    @Test(priority = 99)
    @AlmAnnotation(almTestId = "669217")
    public void UpdateQuestionWithsixextendedPropertiesfield() throws Exception {
        try {

            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();

            JSONObject updateQuestionPayload = jsonReader.getJsonObject(QuestionUpdatePayload);
            updateQuestionPayload.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());

            JSONObject extendedProperties1 = new JSONObject();
            extendedProperties1.put("pageId1", "123");
            extendedProperties1.put("pageId2", "TEXT");
            extendedProperties1.put("pageId3", "a");
            extendedProperties1.put("pageId4", "asd");
            extendedProperties1.put("pageId5", "TEXT");
            extendedProperties1.put("pageId5", "a");

            JSONObject answers1 = new JSONObject();
            answers1.put("type", "TEXT");
            answers1.put("id", 1);
            answers1.put("value", "This is the answer");
            answers1.put("correct", true);
            JSONArray answers = new JSONArray();
            answers.add(answers1);

            JSONObject DeckField = new JSONObject();
            DeckField.put("answers", answers);
            // DeckField.put("questionMedia", "TEXT");
            DeckField.put("question", "Question");
            DeckField.put("kind", "SHORT_ANSWER");
            DeckField.put("type", "Manual");
            DeckField.put("platform", "Mobile");
            DeckField.put("source", "App");
            DeckField.put("chapterId", "123");
            DeckField.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());
            DeckField.put("id", QuestionControllerResponseDTO.getQuestions()[0].getId());
            DeckField.put("extendedProperties", extendedProperties1);

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            updateQuestionPayload.put("questions", decks);

            String payload = String.valueOf(updateQuestionPayload);
            //QuestionControllerRequestDTO = mapper.readValue(payload, QuestionControllerRequestDTO.class);
            long Timestamp = System.currentTimeMillis();
            System.out.println("Time:"+Timestamp);
            QuestionControllerResponseDTO = updateQuestion(headerNormal, payload, System.currentTimeMillis());

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getMessage(), "error.validation", "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getFieldErrors()[0].getField(), "questions[0].extendedProperties", "Status Code");
            Assert.assertEquals(QuestionControllerResponseDTO.getFieldErrors()[0].getMessage(), "Can have maximum of 5 extended properties", "Status Code");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    //Verify whether user get 200 get response when retriving question with extendedProperties json updated get question id endpoint
    @Test(priority = 100)
    @AlmAnnotation(almTestId = "669220")
    public void GetQuestionByQuestionIdWithextendedPropertiesfield() throws Exception {
        try {
            createSingleDeckWithFullPayload();

            // Read json object from file
            JSONObject createQuestionPayload = jsonReader.getJsonObject(QuestionCreatePayload);
            System.out.println(createQuestionPayload);
            createQuestionPayload.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());

            JSONObject extendedProperties1 = new JSONObject();
            extendedProperties1.put("pageId1", "123");
            extendedProperties1.put("pageId2", "TEXT");
            extendedProperties1.put("pageId3", "a");
            extendedProperties1.put("pageId4", "asd");

            JSONObject answers1 = new JSONObject();
            answers1.put("type", "TEXT");
            answers1.put("id", 1);
            answers1.put("value", "This is the answer");
            answers1.put("correct", true);
            JSONArray answers = new JSONArray();
            answers.add(answers1);

            JSONObject DeckField = new JSONObject();
            DeckField.put("answers", answers);
            DeckField.put("archived", true);
            DeckField.put("creatorId", "0f1d72fbc8a4412ba1ac8b8970eeffd5");
            DeckField.put("favourite", false);
            DeckField.put("kind", "ALL");
            DeckField.put("platform", "Mobile");
            DeckField.put("question", "string");
            DeckField.put("questionMedia", "TEXT");
            DeckField.put("source", "App");
            DeckField.put("type", "Manual");
            DeckField.put("chapterId", "123");
            DeckField.put("extendedProperties", extendedProperties1);

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            createQuestionPayload.put("questions", decks);

            String payload = String.valueOf(createQuestionPayload);
            //QuestionControllerRequestDTO = mapper.readValue(payload, QuestionControllerRequestDTO.class);
            QuestionControllerResponseDTO = createQuestion(headerNormal, payload);

//
            String QuestionId = QuestionControllerResponseDTO.getQuestions()[0].getId();
            QuestionDTO = getQuestionByQuestionId(headerNormal, QuestionId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            DeleteDeckWithFullPayload();

        } catch (Exception e) {
            throw e;
        }
    }

    //Verify whether user get 200 get response when retriving question with extendedProperties json updated get deck id endpoint
    @Test(priority = 101)
    @AlmAnnotation(almTestId = "669221")
    public void GetQuestionByDeckIdWithextendedPropertiesfield() throws Exception {
        try {
            createSingleDeckWithFullPayload();

            // Read json object from file
            JSONObject createQuestionPayload = jsonReader.getJsonObject(QuestionCreatePayload);
            System.out.println(createQuestionPayload);
            createQuestionPayload.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());

            JSONObject extendedProperties1 = new JSONObject();
            extendedProperties1.put("pageId1", "123");
            extendedProperties1.put("pageId2", "TEXT");
            extendedProperties1.put("pageId3", "a");
            extendedProperties1.put("pageId4", "asd");

            JSONObject answers1 = new JSONObject();
            answers1.put("type", "TEXT");
            answers1.put("id", 1);
            answers1.put("value", "This is the answer");
            answers1.put("correct", true);
            JSONArray answers = new JSONArray();
            answers.add(answers1);

            JSONObject DeckField = new JSONObject();
            DeckField.put("answers", answers);
            DeckField.put("archived", true);
            DeckField.put("creatorId", "0f1d72fbc8a4412ba1ac8b8970eeffd5");
            DeckField.put("favourite", false);
            DeckField.put("kind", "ALL");
            DeckField.put("platform", "Mobile");
            DeckField.put("question", "string");
            DeckField.put("questionMedia", "TEXT");
            DeckField.put("source", "App");
            DeckField.put("type", "Manual");
            DeckField.put("chapterId", "123");
            DeckField.put("extendedProperties", extendedProperties1);

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            createQuestionPayload.put("questions", decks);

            String payload = String.valueOf(createQuestionPayload);
            //QuestionControllerRequestDTO = mapper.readValue(payload, QuestionControllerRequestDTO.class);
            QuestionControllerResponseDTO = createQuestion(headerNormal, payload);

//
            queryparam.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());
            QuestionControllerResponseDTO = getQuestionByDeckId(headerNormal, queryparam);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            DeleteDeckWithFullPayload();

        } catch (Exception e) {
            throw e;
        }
    }

    //Verify whether user get 204 delete response when deleting question with extendedProperties json
    @Test(priority = 102)
    @AlmAnnotation(almTestId = "669222")
    public void DeleteSingleQuestionWithextendedPropertiesfield() throws Exception {

        try {
            createSingleDeckWithFullPayload();

            // Read json object from file
            JSONObject createQuestionPayload = jsonReader.getJsonObject(QuestionCreatePayload);
            System.out.println(createQuestionPayload);
            createQuestionPayload.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());

            JSONObject extendedProperties1 = new JSONObject();
            extendedProperties1.put("pageId1", "123");
            extendedProperties1.put("pageId2", "TEXT");
            extendedProperties1.put("pageId3", "a");
            extendedProperties1.put("pageId4", "asd");

            JSONObject answers1 = new JSONObject();
            answers1.put("type", "TEXT");
            answers1.put("id", 1);
            answers1.put("value", "This is the answer");
            answers1.put("correct", true);
            JSONArray answers = new JSONArray();
            answers.add(answers1);

            JSONObject DeckField = new JSONObject();
            DeckField.put("answers", answers);
            DeckField.put("archived", true);
            DeckField.put("creatorId", "0f1d72fbc8a4412ba1ac8b8970eeffd5");
            DeckField.put("favourite", false);
            DeckField.put("kind", "ALL");
            DeckField.put("platform", "Mobile");
            DeckField.put("question", "string");
            DeckField.put("questionMedia", "TEXT");
            DeckField.put("source", "App");
            DeckField.put("type", "Manual");
            DeckField.put("chapterId", "123");
            DeckField.put("extendedProperties", extendedProperties1);

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            createQuestionPayload.put("questions", decks);

            String payload = String.valueOf(createQuestionPayload);
            //QuestionControllerRequestDTO = mapper.readValue(payload, QuestionControllerRequestDTO.class);
            QuestionControllerResponseDTO = createQuestion(headerNormal, payload);

            String QuestionId = QuestionControllerResponseDTO.getQuestions()[0].getId();
            QuestionControllerResponseDTO = deleteQuestion(headerNormal, QuestionId);

            Assert.assertEquals(StatusCode, 204, "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    //Verify whether user get 204 delete response when deleting multiple questions with extendedProperties json
    @Test(priority = 103)
    @AlmAnnotation(almTestId = "672539")
    public void DeleteMultipleQuestionWithextendedPropertiesfield() throws Exception {

        try {
            createSingleDeckWithFullPayload();

            // Read json object from file
            JSONObject createQuestionPayload = jsonReader.getJsonObject(QuestionCreatePayload);
            System.out.println(createQuestionPayload);
            createQuestionPayload.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());

            JSONObject extendedProperties1 = new JSONObject();
            extendedProperties1.put("pageId1", "123");
            extendedProperties1.put("pageId2", "TEXT");
            extendedProperties1.put("pageId3", "a");
            extendedProperties1.put("pageId4", "asd");

            JSONObject answers1 = new JSONObject();
            answers1.put("type", "TEXT");
            answers1.put("id", 1);
            answers1.put("value", "This is the answer");
            answers1.put("correct", true);
            JSONArray answers = new JSONArray();
            answers.add(answers1);

            JSONObject DeckField = new JSONObject();
            DeckField.put("answers", answers);
            DeckField.put("archived", true);
            DeckField.put("creatorId", "0f1d72fbc8a4412ba1ac8b8970eeffd5");
            DeckField.put("favourite", false);
            DeckField.put("kind", "ALL");
            DeckField.put("platform", "Mobile");
            DeckField.put("question", "string");
            DeckField.put("questionMedia", "TEXT");
            DeckField.put("source", "App");
            DeckField.put("type", "Manual");
            DeckField.put("chapterId", "123");
            DeckField.put("extendedProperties", extendedProperties1);

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            createQuestionPayload.put("questions", decks);

            String payload = String.valueOf(createQuestionPayload);
            //QuestionControllerRequestDTO = mapper.readValue(payload, QuestionControllerRequestDTO.class);
            QuestionControllerResponseDTO = createQuestion(headerNormal, payload);

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
}


