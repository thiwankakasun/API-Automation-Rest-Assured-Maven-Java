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
import requestDTO.DeckControllerRequestDTO;
import requestDTO.DeckRequestDTO;
import requestDTO.QuestionControllerRequestDTO;
import responseDTO.*;
import utils.JsonReader;
import utils.PiTokenGenarator;
import utils.PiTokenGenaratorNormalUser;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.logging.Logger;

public class PatchQuestion_QuestionController extends ServiceController {

    private QuestionControllerResponseDTO QuestionControllerResponseDTO;
    private requestDTO.QuestionControllerRequestDTO QuestionControllerRequestDTO;
    private DeckControllerResponseDTO DeckControllerResponseDTO;
    private DeckControllerRequestDTO DeckControllerRequestDTO;
    private QuestionDTO QuestionDTO;
    private static final utils.PiTokenGenarator PiTokenGenarator = new PiTokenGenarator();
    private static final utils.PiTokenGenaratorNormalUser PiTokenGenaratorNormalUser = new PiTokenGenaratorNormalUser();
    private static final HashMap<String, String> header = new HashMap<>();
    private static final HashMap<String, String> headerNormal = new HashMap<>();
    private static final HashMap<String, String> queryparam = new HashMap<>();
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
    @AlmAnnotation(almTestId = "609861")
    public void CreateQuestionWithFullPayload() throws Exception {
        try {
            // Read json object from file
            JSONObject createQuestionPayload = jsonReader.getJsonObject(QuestionCreatePayload);
            System.out.println(createQuestionPayload);
            createQuestionPayload.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());

            String payload = String.valueOf(createQuestionPayload);
            QuestionControllerResponseDTO = createQuestion(headerNormal, payload);

            Assert.assertEquals(StatusCode, 201, "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 3) @AlmAnnotation(almTestId = "611101")
    public void PatchQuestionWithFullPayload() throws Exception {
        try {

            JSONObject updateQuestionPayload = jsonReader.getJsonObject(QuestionPatchPayload);
            String questionId = QuestionControllerResponseDTO.getQuestions()[0].getId();;
            String payload = String.valueOf(updateQuestionPayload);
            long currentTimestamp = System.currentTimeMillis();
            System.out.println(currentTimestamp);
            QuestionDTO = patchQuestion(headerNormal, payload, String.valueOf(currentTimestamp), questionId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 4)
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
    @Test(priority = 5)
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

    @Test(priority = 6) @AlmAnnotation(almTestId = "611101")
    public void PatchQuestionWithValidQuestionId_ValidUpdatedAtValue () throws Exception {
        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            JSONObject updateQuestionPayload = jsonReader.getJsonObject(QuestionPatchPayload);
            updateQuestionPayload.put("deckId",DeckControllerResponseDTO.getDecks()[0].getId());
            updateQuestionPayload.put("id",QuestionControllerResponseDTO.getQuestions()[0].getId());
            String questionId = QuestionControllerResponseDTO.getQuestions()[0].getId();;
            String payload = String.valueOf(updateQuestionPayload);
            long currentTimestamp = System.currentTimeMillis();
            QuestionDTO = patchQuestion(headerNormal, payload, String.valueOf(currentTimestamp), questionId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(QuestionDTO.getId(),questionId);

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 7) @AlmAnnotation(almTestId = "611101")
    public void PatchQuestionWitInvalidQuestionId_ValidUpdatedAtValue () throws Exception {
        try {
            JSONObject updateQuestionPayload = jsonReader.getJsonObject(QuestionPatchPayload);
            String questionId = "invalidId";;
            String payload = String.valueOf(updateQuestionPayload);
            long currentTimestamp = System.currentTimeMillis();
            QuestionDTO = patchQuestion(headerNormal, payload, String.valueOf(currentTimestamp), questionId);

            Assert.assertEquals(StatusCode, 404, "Status Code");
            Assert.assertEquals(QuestionDTO.getDescription(),"Couldn't find question with id : "+questionId);
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 8) @AlmAnnotation(almTestId = "611101")
    public void PatchQuestionWitValidQuestionId_InvalidUpdatedAtValue () throws Exception {
        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            JSONObject updateQuestionPayload = jsonReader.getJsonObject(QuestionPatchPayload);
            updateQuestionPayload.put("deckId",DeckControllerResponseDTO.getDecks()[0].getId());
            updateQuestionPayload.put("id",QuestionControllerResponseDTO.getQuestions()[0].getId());
            String questionId = QuestionControllerResponseDTO.getQuestions()[0].getId();;
            String payload = String.valueOf(updateQuestionPayload);
            String currentTimestamp = "1666343445329";
            QuestionDTO = patchQuestion(headerNormal, payload, currentTimestamp, questionId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionDTO.getDescription(),"Invalid update request. UpdatedAt is less than the question updated time.");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 9) @AlmAnnotation(almTestId = "611101")
    public void PatchQuestionWithInvalidQuestionId_InvalidUpdatedAtValue () throws Exception {
        try {
            JSONObject updateQuestionPayload = jsonReader.getJsonObject(QuestionPatchPayload);
            String questionId = "InvalidId";
            String payload = String.valueOf(updateQuestionPayload);
            String currentTimestamp = "1666343445329";
            QuestionDTO = patchQuestion(headerNormal, payload, currentTimestamp, questionId);

            Assert.assertEquals(StatusCode, 404, "Status Code");
            Assert.assertEquals(QuestionDTO.getDescription(),"Couldn't find question with id : "+questionId);
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 10) @AlmAnnotation(almTestId = "611101")
    public void PatchQuestionWithDeletedQuestionId () throws Exception {
        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();

            JSONObject updateQuestionPayload = jsonReader.getJsonObject(QuestionPatchPayload);
            updateQuestionPayload.put("deckId",DeckControllerResponseDTO.getDecks()[0].getId());
            updateQuestionPayload.put("id",QuestionControllerResponseDTO.getQuestions()[0].getId());
            String questionId = QuestionControllerResponseDTO.getQuestions()[0].getId();
            String payload = String.valueOf(updateQuestionPayload);

            DeleteSingleQuestionWithFullPayload();
            long currentTimestamp = System.currentTimeMillis();
            QuestionDTO = patchQuestion(headerNormal, payload, String.valueOf(currentTimestamp), questionId);

            Assert.assertEquals(StatusCode, 404, "Status Code");
            Assert.assertEquals(QuestionDTO.getDescription(),"Couldn't find question with id : "+questionId);

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 11) @AlmAnnotation(almTestId = "611101")
    public void PatchQuestionWithEmptyRequestBody () throws Exception {
        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();

            String questionId = QuestionControllerResponseDTO.getQuestions()[0].getId();
            String payload = "{}";

            long currentTimestamp = System.currentTimeMillis();
            QuestionDTO = patchQuestion(headerNormal, payload, String.valueOf(currentTimestamp), questionId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(QuestionDTO.getId(),QuestionControllerResponseDTO.getQuestions()[0].getId());

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 12) @AlmAnnotation(almTestId = "611101")
    public void PatchQuestionWithEntireObject () throws Exception {
        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            JSONObject updateQuestionPayload = jsonReader.getJsonObject(QuestionPatchPayload);
            updateQuestionPayload.put("deckId",DeckControllerResponseDTO.getDecks()[0].getId());
            updateQuestionPayload.put("id",QuestionControllerResponseDTO.getQuestions()[0].getId());
            updateQuestionPayload.put("kind","MULTIPLE_CHOICE");
            updateQuestionPayload.put("type","Auto");
            updateQuestionPayload.put("platform","Web");
            updateQuestionPayload.put("source","Clipper");

            String questionId = QuestionControllerResponseDTO.getQuestions()[0].getId();;
            String payload = String.valueOf(updateQuestionPayload);
            long currentTimestamp = System.currentTimeMillis();
            QuestionDTO = patchQuestion(headerNormal, payload, String.valueOf(currentTimestamp), questionId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(QuestionDTO.getId(),questionId);

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    //@Test(priority = 13) @AlmAnnotation(almTestId = "611101")
    public void PatchQuestionWithoutValueforKind () throws Exception {
        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            JSONObject updateQuestionPayload = jsonReader.getJsonObject(QuestionPatchPayload);
            updateQuestionPayload.put("deckId",DeckControllerResponseDTO.getDecks()[0].getId());
            updateQuestionPayload.put("id",QuestionControllerResponseDTO.getQuestions()[0].getId());
            updateQuestionPayload.put("kind","");

            String questionId = QuestionControllerResponseDTO.getQuestions()[0].getId();;
            String payload = String.valueOf(updateQuestionPayload);
            long currentTimestamp = System.currentTimeMillis();
            QuestionDTO = patchQuestion(headerNormal, payload, String.valueOf(currentTimestamp), questionId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionDTO.getFieldErrors()[0].getField(),"kind");
            Assert.assertEquals(QuestionDTO.getFieldErrors()[0].getMessage(),"'kind' Cannot be null or empty");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }
    @Test(priority = 14) @AlmAnnotation(almTestId = "611101")
    public void PatchQuestionWithInvalidValueforKind () throws Exception {
        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            JSONObject updateQuestionPayload = jsonReader.getJsonObject(QuestionPatchPayload);
            updateQuestionPayload.put("deckId",DeckControllerResponseDTO.getDecks()[0].getId());
            updateQuestionPayload.put("id",QuestionControllerResponseDTO.getQuestions()[0].getId());
            updateQuestionPayload.put("kind","InvalidValue");

            String questionId = QuestionControllerResponseDTO.getQuestions()[0].getId();;
            String payload = String.valueOf(updateQuestionPayload);
            long currentTimestamp = System.currentTimeMillis();
            QuestionDTO = patchQuestion(headerNormal, payload, String.valueOf(currentTimestamp), questionId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionDTO.getFieldErrors()[0].getField(),"kind");
            Assert.assertEquals(QuestionDTO.getFieldErrors()[0].getMessage(),"must match \"MULTIPLE_CHOICE|SHORT_ANSWER|ALL\"");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }
    @Test(priority = 15) @AlmAnnotation(almTestId = "611101")
    public void PatchQuestionWithoutValueforType () throws Exception {
        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            JSONObject updateQuestionPayload = jsonReader.getJsonObject(QuestionPatchPayload);
            updateQuestionPayload.put("deckId",DeckControllerResponseDTO.getDecks()[0].getId());
            updateQuestionPayload.put("id",QuestionControllerResponseDTO.getQuestions()[0].getId());
            updateQuestionPayload.put("type","");

            String questionId = QuestionControllerResponseDTO.getQuestions()[0].getId();;
            String payload = String.valueOf(updateQuestionPayload);
            long currentTimestamp = System.currentTimeMillis();
            QuestionDTO = patchQuestion(headerNormal, payload, String.valueOf(currentTimestamp), questionId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionDTO.getFieldErrors()[0].getField(),"type");
            if (QuestionDTO.getFieldErrors()[0].getMessage().equals("'type' Cannot be null or empty")) {
                Assert.assertEquals(QuestionDTO.getFieldErrors()[0].getMessage(), "'type' Cannot be null or empty");
            }
            else {
                Assert.assertEquals(QuestionDTO.getFieldErrors()[0].getMessage(), "must match \"Auto|Manual\"");
            }

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 16) @AlmAnnotation(almTestId = "611101")
    public void PatchQuestionWithInvalidValueforType () throws Exception {
        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            JSONObject updateQuestionPayload = jsonReader.getJsonObject(QuestionPatchPayload);
            updateQuestionPayload.put("deckId",DeckControllerResponseDTO.getDecks()[0].getId());
            updateQuestionPayload.put("id",QuestionControllerResponseDTO.getQuestions()[0].getId());
            updateQuestionPayload.put("type","InvalidValue");

            String questionId = QuestionControllerResponseDTO.getQuestions()[0].getId();;
            String payload = String.valueOf(updateQuestionPayload);
            long currentTimestamp = System.currentTimeMillis();
            QuestionDTO = patchQuestion(headerNormal, payload, String.valueOf(currentTimestamp), questionId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionDTO.getFieldErrors()[0].getField(),"type");
            Assert.assertEquals(QuestionDTO.getFieldErrors()[0].getMessage(),"must match \"Auto|Manual\"");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 17) @AlmAnnotation(almTestId = "611101")
    public void PatchQuestionWithoutValueforPlatform () throws Exception {
        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            JSONObject updateQuestionPayload = jsonReader.getJsonObject(QuestionPatchPayload);
            updateQuestionPayload.put("deckId",DeckControllerResponseDTO.getDecks()[0].getId());
            updateQuestionPayload.put("id",QuestionControllerResponseDTO.getQuestions()[0].getId());
            updateQuestionPayload.put("platform","");

            String questionId = QuestionControllerResponseDTO.getQuestions()[0].getId();;
            String payload = String.valueOf(updateQuestionPayload);
            long currentTimestamp = System.currentTimeMillis();
            QuestionDTO = patchQuestion(headerNormal, payload, String.valueOf(currentTimestamp), questionId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionDTO.getFieldErrors()[0].getField(),"platform");
            if (QuestionDTO.getFieldErrors()[0].getMessage().equals("'platform' Cannot be null or empty")) {
                Assert.assertEquals(QuestionDTO.getFieldErrors()[0].getMessage(), "'platform' Cannot be null or empty");
            }
            else {
                Assert.assertEquals(QuestionDTO.getFieldErrors()[0].getMessage(), "must match \"Mobile|Web|AuthoringTool\"");
            }
            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }
    @Test(priority = 18) @AlmAnnotation(almTestId = "611101")
    public void PatchQuestionWithInvalidValueforPlatform () throws Exception {
        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            JSONObject updateQuestionPayload = jsonReader.getJsonObject(QuestionPatchPayload);
            updateQuestionPayload.put("deckId",DeckControllerResponseDTO.getDecks()[0].getId());
            updateQuestionPayload.put("id",QuestionControllerResponseDTO.getQuestions()[0].getId());
            updateQuestionPayload.put("platform","InvalidValue");

            String questionId = QuestionControllerResponseDTO.getQuestions()[0].getId();;
            String payload = String.valueOf(updateQuestionPayload);
            long currentTimestamp = System.currentTimeMillis();
            QuestionDTO = patchQuestion(headerNormal, payload, String.valueOf(currentTimestamp), questionId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionDTO.getFieldErrors()[0].getField(),"platform");
            Assert.assertEquals(QuestionDTO.getFieldErrors()[0].getMessage(),"must match \"Mobile|Web|AuthoringTool\"");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 19) @AlmAnnotation(almTestId = "611101")
    public void PatchQuestionWithoutValueforSource () throws Exception {
        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            JSONObject updateQuestionPayload = jsonReader.getJsonObject(QuestionPatchPayload);
            updateQuestionPayload.put("deckId",DeckControllerResponseDTO.getDecks()[0].getId());
            updateQuestionPayload.put("id",QuestionControllerResponseDTO.getQuestions()[0].getId());
            updateQuestionPayload.put("source","");

            String questionId = QuestionControllerResponseDTO.getQuestions()[0].getId();;
            String payload = String.valueOf(updateQuestionPayload);
            long currentTimestamp = System.currentTimeMillis();
            QuestionDTO = patchQuestion(headerNormal, payload, String.valueOf(currentTimestamp), questionId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionDTO.getFieldErrors()[0].getField(),"source");
            if (QuestionDTO.getFieldErrors()[0].getMessage().equals("'source' Cannot be null or empty")) {
                Assert.assertEquals(QuestionDTO.getFieldErrors()[0].getMessage(), "'source' Cannot be null or empty");
            }
            else {
                Assert.assertEquals(QuestionDTO.getFieldErrors()[0].getMessage(), "must match \"Clipper|File|App|Gdrive\"");
            }
            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }


    @Test(priority = 20) @AlmAnnotation(almTestId = "611101")
    public void PatchQuestionWithInvalidValueforSource() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();
            JSONObject updateQuestionPayload = jsonReader.getJsonObject(QuestionPatchPayload);
            updateQuestionPayload.put("deckId",DeckControllerResponseDTO.getDecks()[0].getId());
            updateQuestionPayload.put("id",QuestionControllerResponseDTO.getQuestions()[0].getId());
            updateQuestionPayload.put("source","InvalidValue");

            String questionId = QuestionControllerResponseDTO.getQuestions()[0].getId();;
            String payload = String.valueOf(updateQuestionPayload);
            long currentTimestamp = System.currentTimeMillis();
            QuestionDTO = patchQuestion(headerNormal, payload, String.valueOf(currentTimestamp), questionId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionDTO.getFieldErrors()[0].getField(),"source");
            Assert.assertEquals(QuestionDTO.getFieldErrors()[0].getMessage(),"must match \"Clipper|File|App|Gdrive\"");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }



    @Test(priority = 21) @AlmAnnotation(almTestId = "611101")
    public void PatchQuestionWithInvalidTenantId () throws Exception {
        try {
            headerNormal.put("X-TenantId","123");
            JSONObject updateQuestionPayload = jsonReader.getJsonObject(QuestionPatchPayload);
            String questionId = "InvalidId";
            String payload = String.valueOf(updateQuestionPayload);

            long currentTimestamp = System.currentTimeMillis();
            QuestionDTO = patchQuestion(headerNormal, payload, String.valueOf(currentTimestamp), questionId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionDTO.getDescription(),"Field 'tenantId' does not have a valid value");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 22) @AlmAnnotation(almTestId = "611101")
    public void PatchQuestionWithInvalidTenantKey () throws Exception {
        try {
            getData();
            headerNormal.put("X-TenantKey","123");
            JSONObject updateQuestionPayload = jsonReader.getJsonObject(QuestionPatchPayload);
            String questionId = "InvalidId";
            String payload = String.valueOf(updateQuestionPayload);

            long currentTimestamp = System.currentTimeMillis();
            QuestionDTO = patchQuestion(headerNormal, payload, String.valueOf(currentTimestamp), questionId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionDTO.getDescription(),"Field 'tenantKey' does not have a valid value");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 23) @AlmAnnotation(almTestId = "611101")
    public void PatchQuestionWithoutTenantId () throws Exception {
        try {
            headerNormal.put("X-TenantId","");
            JSONObject updateQuestionPayload = jsonReader.getJsonObject(QuestionPatchPayload);
            String questionId = "InvalidId";
            String payload = String.valueOf(updateQuestionPayload);

            long currentTimestamp = System.currentTimeMillis();
            QuestionDTO = patchQuestion(headerNormal, payload, String.valueOf(currentTimestamp), questionId);

            Assert.assertEquals(StatusCode, 403, "Status Code");
            Assert.assertEquals(QuestionDTO.getDescription(),"You do not have permission to access this method without a valid tenant ID");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 24) @AlmAnnotation(almTestId = "611101")
    public void PatchQuestionWithoutTenantKey () throws Exception {
        try {
            getData();
            headerNormal.put("X-TenantKey","");
            JSONObject updateQuestionPayload = jsonReader.getJsonObject(QuestionPatchPayload);
            String questionId = "InvalidId";
            String payload = String.valueOf(updateQuestionPayload);

            long currentTimestamp = System.currentTimeMillis();
            QuestionDTO = patchQuestion(headerNormal, payload, String.valueOf(currentTimestamp), questionId);

            Assert.assertEquals(StatusCode, 403, "Status Code");
            Assert.assertEquals(QuestionDTO.getDescription(),"You do not have permission to access this method without a valid tenant Key");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 25) @AlmAnnotation(almTestId = "611101")
    public void PatchQuestionWithUnauthorizedError() throws Exception {
        try {
            headerNormal.put("X-Authorization",null);
            JSONObject updateQuestionPayload = jsonReader.getJsonObject(QuestionPatchPayload);
            String questionId = "InvalidId";
            String payload = String.valueOf(updateQuestionPayload);

            long currentTimestamp = System.currentTimeMillis();
            QuestionDTO = patchQuestion(headerNormal, payload, String.valueOf(currentTimestamp), questionId);

            Assert.assertEquals(StatusCode, 401, "Status Code");
            Assert.assertEquals(QuestionDTO.getError(),"Unauthorized");
        } catch (Exception e) {
            throw e;
        }
    }


    @Test(priority = 26)
    @AlmAnnotation(almTestId = "669219")
    public void UpdateQuestionbyAddingextendedProperties_PatchUpdate() throws Exception {
        try {
            getData();
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();

            JSONObject updateQuestionPayload = jsonReader.getJsonObject(QuestionUpdatePayload);

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
            DeckField.put("question", "Question");
            DeckField.put("kind", "SHORT_ANSWER");
            DeckField.put("type", "Manual");
            DeckField.put("platform", "Mobile");
            DeckField.put("source", "App");
            DeckField.put("chapterId", "123");
            DeckField.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());
            DeckField.put("extendedProperties", extendedProperties1);

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            updateQuestionPayload.put("questions", decks);

            String payload = String.valueOf(updateQuestionPayload);
            String questionId = QuestionControllerResponseDTO.getQuestions()[0].getId();
            //QuestionControllerRequestDTO = mapper.readValue(payload, QuestionControllerRequestDTO.class);
            long Timestamp = System.currentTimeMillis();
            System.out.println("Time:"+Timestamp);
            QuestionDTO = patchQuestion(headerNormal, payload, String.valueOf(System.currentTimeMillis()), questionId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }


    @Test(priority = 27)
    @AlmAnnotation(almTestId = "672544")
    public void UpdateQuestionWithEmptyextendedProperties_PatchUpdate() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();

            JSONObject updateQuestionPayload = jsonReader.getJsonObject(QuestionUpdatePayload);

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
            DeckField.put("question", "Question");
            DeckField.put("kind", "SHORT_ANSWER");
            DeckField.put("type", "Manual");
            DeckField.put("platform", "Mobile");
            DeckField.put("source", "App");
            DeckField.put("chapterId", "123");
            DeckField.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());
            DeckField.put("extendedProperties", extendedProperties1);

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            updateQuestionPayload.put("questions", decks);

            String payload = String.valueOf(updateQuestionPayload);
            String questionId = QuestionControllerResponseDTO.getQuestions()[0].getId();
            //QuestionControllerRequestDTO = mapper.readValue(payload, QuestionControllerRequestDTO.class);
            long Timestamp = System.currentTimeMillis()+1000;
            System.out.println("Time:"+Timestamp);
            QuestionDTO = patchQuestion(headerNormal, payload, String.valueOf(System.currentTimeMillis()), questionId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 28)
    @AlmAnnotation(almTestId = "672705")
    public void UpdateQuestionWithIntegerForextendedProperties() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();

            JSONObject updateQuestionPayload = jsonReader.getJsonObject(QuestionUpdatePayload);

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
            DeckField.put("question", "Question");
            DeckField.put("kind", "SHORT_ANSWER");
            DeckField.put("type", "Manual");
            DeckField.put("platform", "Mobile");
            DeckField.put("source", "App");
            DeckField.put("chapterId", "123");
            DeckField.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());
            DeckField.put("extendedProperties", extendedProperties1);

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            updateQuestionPayload.put("questions", decks);

            String payload = String.valueOf(updateQuestionPayload);
            String questionId = QuestionControllerResponseDTO.getQuestions()[0].getId();
            //QuestionControllerRequestDTO = mapper.readValue(payload, QuestionControllerRequestDTO.class);
            long Timestamp = System.currentTimeMillis()+1000;
            System.out.println("Time:"+Timestamp);
            QuestionDTO = patchQuestion(headerNormal, payload, String.valueOf(System.currentTimeMillis()), questionId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 29)
    @AlmAnnotation(almTestId = "672707")
    public void UpdateQuestionWithMaximumcharactersForextendedProperties() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();

            JSONObject updateQuestionPayload = jsonReader.getJsonObject(QuestionUpdatePayload);

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
            DeckField.put("question", "Question");
            DeckField.put("kind", "SHORT_ANSWER");
            DeckField.put("type", "Manual");
            DeckField.put("platform", "Mobile");
            DeckField.put("source", "App");
            DeckField.put("chapterId", "123");
            DeckField.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());
            DeckField.put("extendedProperties", extendedProperties1);

            JSONArray decks = new JSONArray();
            decks.add(DeckField);
            updateQuestionPayload.put("questions", decks);

            String payload = String.valueOf(updateQuestionPayload);
            String questionId = QuestionControllerResponseDTO.getQuestions()[0].getId();
            //QuestionControllerRequestDTO = mapper.readValue(payload, QuestionControllerRequestDTO.class);
            long Timestamp = System.currentTimeMillis()+1000;
            System.out.println("Time:"+Timestamp);
            QuestionDTO = patchQuestion(headerNormal, payload, String.valueOf(System.currentTimeMillis()), questionId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 30)
    @AlmAnnotation(almTestId = "672710")
    public void UpdateQuestionWithEmptyextendedPropertiesfield() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();

            JSONObject updateQuestionPayload = jsonReader.getJsonObject(QuestionPatchPayload);

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

            //JSONObject DeckField = new JSONObject();
            updateQuestionPayload.put("answers", answers);
            updateQuestionPayload.put("question", "Question");
            updateQuestionPayload.put("kind", "SHORT_ANSWER");
            updateQuestionPayload.put("type", "Manual");
            updateQuestionPayload.put("platform", "Mobile");
            updateQuestionPayload.put("source", "App");
            updateQuestionPayload.put("chapterId", "123");
            updateQuestionPayload.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());
            updateQuestionPayload.put("extendedProperties", extendedProperties1);

//            JSONArray decks = new JSONArray();
//            decks.add(DeckField);
            //updateQuestionPayload.put("questions", decks);

            String payload = String.valueOf(updateQuestionPayload);
            String questionId = QuestionControllerResponseDTO.getQuestions()[0].getId();
            //QuestionControllerRequestDTO = mapper.readValue(payload, QuestionControllerRequestDTO.class);
            long Timestamp = System.currentTimeMillis()+1000;
            System.out.println("Time:"+Timestamp);
            QuestionDTO = patchQuestion(headerNormal, payload, String.valueOf(System.currentTimeMillis()), questionId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionDTO.getMessage(), "error.validation", "Status Code");
            Assert.assertEquals(QuestionDTO.getDescription(), "Key/Value cannot be null or empty", "Status Code");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 31)
    @AlmAnnotation(almTestId = "672710")
    public void UpdateQuestionWithNullextendedPropertiesfield() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();

            JSONObject updateQuestionPayload = jsonReader.getJsonObject(QuestionPatchPayload);

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

            //JSONObject DeckField = new JSONObject();
            updateQuestionPayload.put("answers", answers);
            // DeckField.put("questionMedia", "TEXT");
            updateQuestionPayload.put("question", "Question");
            updateQuestionPayload.put("kind", "SHORT_ANSWER");
            updateQuestionPayload.put("type", "Manual");
            updateQuestionPayload.put("platform", "Mobile");
            updateQuestionPayload.put("source", "App");
            updateQuestionPayload.put("chapterId", "123");
            updateQuestionPayload.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());
            updateQuestionPayload.put("extendedProperties", extendedProperties1);

//            JSONArray decks = new JSONArray();
//            decks.add(DeckField);
//            updateQuestionPayload.put("questions", decks);

            String payload = String.valueOf(updateQuestionPayload);
            String questionId = QuestionControllerResponseDTO.getQuestions()[0].getId();
            //QuestionControllerRequestDTO = mapper.readValue(payload, QuestionControllerRequestDTO.class);
            long Timestamp = System.currentTimeMillis()+1000;
            System.out.println("Time:"+Timestamp);
            QuestionDTO = patchQuestion(headerNormal, payload, String.valueOf(System.currentTimeMillis()), questionId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionDTO.getMessage(), "error.validation", "Status Code");
            Assert.assertEquals(QuestionDTO.getDescription(), "Key/Value cannot be null or empty", "Status Code");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 32)
    @AlmAnnotation(almTestId = "672714")
    public void UpdateQuestionWith257charactersForextendedPropertiesfield() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();

            JSONObject updateQuestionPayload = jsonReader.getJsonObject(QuestionPatchPayload);

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

            //JSONObject DeckField = new JSONObject();
            updateQuestionPayload.put("answers", answers);
            // DeckField.put("questionMedia", "TEXT");
            updateQuestionPayload.put("question", "Question");
            updateQuestionPayload.put("kind", "SHORT_ANSWER");
            updateQuestionPayload.put("type", "Manual");
            updateQuestionPayload.put("platform", "Mobile");
            updateQuestionPayload.put("source", "App");
            updateQuestionPayload.put("chapterId", "123");
            updateQuestionPayload.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());
            updateQuestionPayload.put("extendedProperties", extendedProperties1);

//            JSONArray decks = new JSONArray();
//            decks.add(DeckField);
//            updateQuestionPayload.put("questions", decks);

            String payload = String.valueOf(updateQuestionPayload);
            String questionId = QuestionControllerResponseDTO.getQuestions()[0].getId();
            //QuestionControllerRequestDTO = mapper.readValue(payload, QuestionControllerRequestDTO.class);
            long Timestamp = System.currentTimeMillis()+1000;
            System.out.println("Time:"+Timestamp);
            QuestionDTO = patchQuestion(headerNormal, payload, String.valueOf(System.currentTimeMillis()), questionId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionDTO.getMessage(), "error.validation", "Status Code");
            Assert.assertEquals(QuestionDTO.getDescription(), "Value("+pageId+") cannot have more than 256 characters", "Status Code");
            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 33)
    @AlmAnnotation(almTestId = "672712")
    public void UpdateQuestionWithsixextendedPropertiesfield() throws Exception {
        try {
            createSingleDeckWithFullPayload();
            CreateQuestionWithFullPayload();

            JSONObject updateQuestionPayload = jsonReader.getJsonObject(QuestionPatchPayload);

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

            //JSONObject DeckField = new JSONObject();
            updateQuestionPayload.put("answers", answers);
            // DeckField.put("questionMedia", "TEXT");
            updateQuestionPayload.put("question", "Question");
            updateQuestionPayload.put("kind", "SHORT_ANSWER");
            updateQuestionPayload.put("type", "Manual");
            updateQuestionPayload.put("platform", "Mobile");
            updateQuestionPayload.put("source", "App");
            updateQuestionPayload.put("chapterId", "123");
            updateQuestionPayload.put("deckId", DeckControllerResponseDTO.getDecks()[0].getId());
            updateQuestionPayload.put("extendedProperties", extendedProperties1);

//            JSONArray decks = new JSONArray();
//            decks.add(DeckField);
//            updateQuestionPayload.put("questions", decks);

            String payload = String.valueOf(updateQuestionPayload);
            String questionId = QuestionControllerResponseDTO.getQuestions()[0].getId();
            //QuestionControllerRequestDTO = mapper.readValue(payload, QuestionControllerRequestDTO.class);
            long Timestamp = System.currentTimeMillis()+1000;
            System.out.println("Time:"+Timestamp);
            QuestionDTO = patchQuestion(headerNormal, payload, String.valueOf(System.currentTimeMillis()), questionId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(QuestionDTO.getMessage(), "error.validation", "Status Code");
            Assert.assertEquals(QuestionDTO.getFieldErrors()[0].getField(), "extendedProperties", "Status Code");
            Assert.assertEquals(QuestionDTO.getFieldErrors()[0].getMessage(), "Can have maximum of 5 extended properties", "Status Code");

            DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }
}
