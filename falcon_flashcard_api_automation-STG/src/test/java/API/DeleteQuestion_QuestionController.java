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

public class DeleteQuestion_QuestionController extends ServiceController {

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

    private static final Logger LOGGER = Logger.getLogger(String.valueOf(DeleteQuestion_QuestionController.class));

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

    @Test(priority = 10)
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

    @Test(priority = 11)
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

    @Test(priority = 12)
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

    @Test(priority = 13)
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

    @Test(priority = 14)
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

    @Test(priority = 15)
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

    @Test(priority = 16)
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

    @Test(priority = 17)
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
    @Test(priority = 18)
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

    @Test(priority = 19)
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

    @Test(priority = 20)
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

    @Test(priority = 21)
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

    @Test(priority = 22)
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

    @Test(priority = 23)
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

    @Test(priority = 24)
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

    @Test(priority = 25)
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

    @Test(priority = 26)
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
}

