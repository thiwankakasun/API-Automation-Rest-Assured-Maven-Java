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
import requestDTO.StoreControllerRequestDTO;
import responseDTO.ExpertQuestionResponseDTO;
import responseDTO.StoreControllerResponseDTO;
import utils.JsonReader;
import utils.PiTokenGenaratorAuthor;
import utils.PiTokenGenaratorNormalUser;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

public class DeleteExpertDeck_StoreController extends ServiceController {

    //private String defaultContentType = "JSON";
    private StoreControllerResponseDTO StoreControllerResponseDTO;
    private StoreControllerRequestDTO StoreControllerRequestDTO;
    private ExpertQuestionResponseDTO ExpertQuestionResponseDTO;
    private static final PiTokenGenaratorAuthor PiTokenGenaratorAuthor = new PiTokenGenaratorAuthor();
    private static final utils.PiTokenGenaratorNormalUser PiTokenGenaratorNormalUser = new PiTokenGenaratorNormalUser();

    private static final HashMap<String, String> headerAuthor = new HashMap<>();
    private static final HashMap<String, String> headerNormal = new HashMap<>();
    JsonReader jsonReader = new JsonReader();

    private static final Logger LOGGER = Logger.getLogger(String.valueOf(DeleteExpertDeck_StoreController.class));

    @BeforeClass
    public void getData() throws EncryptedDocumentException, InvalidFormatException, IOException, ParseException {
        try {
            PiToken piTokenAuthor = PiTokenGenaratorAuthor.generatePiToken(loginUrl);
            headerAuthor.put("X-Authorization", piTokenAuthor.getData());

            PiToken piToken1 = PiTokenGenaratorNormalUser.generatePiToken(loginUrl);
            headerNormal.put("X-Authorization", piToken1.getData());

        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 1) @AlmAnnotation(almTestId = "xxxxxxxx")
    public void CreateExpertDeck() throws Exception {
        try {
            // Read json object from file
            JSONObject createExpertDeckPayload = jsonReader.getJsonObject(ExpertDeckCreatePayload);
            System.out.println(createExpertDeckPayload);

            String payload = String.valueOf(createExpertDeckPayload);
            StoreControllerRequestDTO = mapper.readValue(payload, StoreControllerRequestDTO.class);
            StoreControllerResponseDTO = createExpertDeck(headerAuthor, payload);

            Assert.assertEquals(StatusCode, 201, "Status Code");
            LOGGER.info("deck Id:" + StoreControllerResponseDTO.getId());
        } catch (Exception e) {
            throw e;
        }
    }


    @Test(priority = 2) @AlmAnnotation(almTestId = "xxxxx")
    public void GetExpertDeckWithFullPayload() throws Exception {
        try {
            String ExpertDeckId = StoreControllerResponseDTO.getId();
            StoreControllerResponseDTO = getExpertDeck(headerAuthor, ExpertDeckId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(StoreControllerResponseDTO.getId(), ExpertDeckId, "Expert deck Id");

        } catch (Exception e) {
            throw e;
        }
    }


    @Test(priority = 3) @AlmAnnotation(almTestId = "xxxxxx")
    public void UpdateExpertDeckFullPayload() throws Exception {
        try {
            JSONObject updateExpertPayload = jsonReader.getJsonObject(ExpertDeckUpdatePayload);
            System.out.println(updateExpertPayload);
            String payload = String.valueOf(updateExpertPayload);
            StoreControllerRequestDTO = mapper.readValue(payload, StoreControllerRequestDTO.class);

            String ExpertId = StoreControllerResponseDTO.getId();

            System.out.println("expert id is: " +ExpertId);
            StoreControllerResponseDTO = updateExpertDecks(headerAuthor, payload, ExpertId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(StoreControllerResponseDTO.getId(), ExpertId, "ExpertId");
            } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 4) @AlmAnnotation(almTestId = "xxxxxxxx")
    public void CreateExpertQuestion() throws Exception {
        try {
            // Read json object from file
            JSONObject createExpertQuestionPayload = jsonReader.getJsonObject(ExpertQuestionCreatePayload);
            System.out.println(createExpertQuestionPayload);
            headerAuthor.put("x-Resource-Id",StoreControllerResponseDTO.getId());
            headerAuthor.put("x-Resource-Type","expert");

            String payload = String.valueOf(createExpertQuestionPayload);
            ExpertQuestionResponseDTO = createExpertQuestion(headerAuthor, payload);

            Assert.assertEquals(StatusCode, 201, "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 5) @AlmAnnotation(almTestId = "xxxxxx")
    public void DeleteExpertDeck() throws Exception {
        try {
            String ExpertId = StoreControllerResponseDTO.getId();
            StoreControllerResponseDTO = deleteExpert(headerAuthor, ExpertId);

            Assert.assertEquals(StatusCode, 204, "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }
    @Test(priority = 6) @AlmAnnotation(almTestId = "xxxxxx")
    public void DeleteExpertDeckWithUsingUpdatedDeckId() throws Exception {
        try {
            CreateExpertDeck();
            UpdateExpertDeckFullPayload();
            String ExpertId = StoreControllerResponseDTO.getId();
            StoreControllerResponseDTO = deleteExpert(headerAuthor, ExpertId);

            Assert.assertEquals(StatusCode, 204, "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 7) @AlmAnnotation(almTestId = "xxxxxx")
    public void DeleteExpertDeckWithUsingDeletedDeckId() throws Exception {
        try {
            CreateExpertDeck();
            String ExpertId = StoreControllerResponseDTO.getId();
            StoreControllerResponseDTO = deleteExpert(headerAuthor, ExpertId);

            Assert.assertEquals(StatusCode, 204, "Status Code");
            StoreControllerResponseDTO = deleteExpert(headerAuthor, ExpertId);

            Assert.assertEquals(StatusCode, 404, "Status Code");
            Assert.assertEquals(StoreControllerResponseDTO.getMessage(),"error.notFound");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 8) @AlmAnnotation(almTestId = "xxxxxx")
    public void DeleteExpertDeckWithUsingInvalidDeckId() throws Exception {
        try {
            String ExpertId = "123";
            StoreControllerResponseDTO = deleteExpert(headerAuthor, ExpertId);

            Assert.assertEquals(StatusCode, 404, "Status Code");
            Assert.assertEquals(StoreControllerResponseDTO.getMessage(),"error.notFound");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 9) @AlmAnnotation(almTestId = "xxxxxx")
    public void DeleteExpertDeckWithUsingNormalUser() throws Exception {
        try {
            String ExpertId = "123";
            StoreControllerResponseDTO = deleteExpert(headerNormal, ExpertId);
            Assert.assertEquals(StatusCode, 403, "Status Code");
            Assert.assertEquals(StoreControllerResponseDTO.getDescription(),"You do not have DELETE_EXPERT_DECK permission to access this method");

        } catch (Exception e) {
            throw e;
        }
    }

}
