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

public class UpdateExpertDeck_StoreController extends ServiceController {

    //private String defaultContentType = "JSON";
    private StoreControllerResponseDTO StoreControllerResponseDTO;
    private StoreControllerRequestDTO StoreControllerRequestDTO;
    private ExpertQuestionResponseDTO ExpertQuestionResponseDTO;
    private static final PiTokenGenaratorAuthor PiTokenGenaratorAuthor = new PiTokenGenaratorAuthor();
    private static final utils.PiTokenGenaratorNormalUser PiTokenGenaratorNormalUser = new PiTokenGenaratorNormalUser();

    private static final HashMap<String, String> headerAuthor = new HashMap<>();
    private static final HashMap<String, String> headerNormal = new HashMap<>();
    JsonReader jsonReader = new JsonReader();

    private static final Logger LOGGER = Logger.getLogger(String.valueOf(UpdateExpertDeck_StoreController.class));

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
    public void UpdateExpertDeckWithAddingMultipleProductIds_MultipleBookIds() throws Exception {
        try {
            CreateExpertDeck();
            JSONObject updateExpertPayload = jsonReader.getJsonObject(ExpertDeckUpdatePayload);
            JSONArray bookIds = new JSONArray();
            if (Env.equals("PROD")){
                bookIds.add("BRNT-2VDGGDVYHJ5");
            }
            else {
                bookIds.add("ABYZ1RY3L9G");
            }
            bookIds.add("123");
            JSONArray productIds = new JSONArray();
            if (Env.equals("PROD")){
                productIds.add("02e18048-3e2d-4869-a646-5f170714dfb2");
            }
            else {
                productIds.add("30b18904-dcc1-4f0f-bf0c-5cc91fa98e8b");
            }
            productIds.add("abc");
            JSONObject BookField = new JSONObject();
            BookField.put("bookTitle","Automation");
            BookField.put("bookIds",bookIds);
            BookField.put("chapter","1");
            BookField.put("bookAuthor","Automation author");
            BookField.put("chapterId","123");
            BookField.put("productIds",productIds);
            updateExpertPayload.put("book",BookField);

            System.out.println(updateExpertPayload);
            String payload = String.valueOf(updateExpertPayload);
            StoreControllerRequestDTO = mapper.readValue(payload, StoreControllerRequestDTO.class);

            String ExpertId = StoreControllerResponseDTO.getId();

            System.out.println("expert id is: " +ExpertId);
            StoreControllerResponseDTO = updateExpertDecks(headerAuthor, payload, ExpertId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(StoreControllerResponseDTO.getId(), ExpertId, "ExpertId");
            Assert.assertEquals(StoreControllerResponseDTO.getBook().getBookIds()[0], StoreControllerRequestDTO.getBook().getBookIds()[0], "ExpertId");
            Assert.assertEquals(StoreControllerResponseDTO.getBook().getBookIds()[1], "123", "ExpertId");
            Assert.assertEquals(StoreControllerResponseDTO.getBook().getProductIds()[0], StoreControllerRequestDTO.getBook().getProductIds()[0], "ExpertId");
            Assert.assertEquals(StoreControllerResponseDTO.getBook().getProductIds()[1], "abc", "ExpertId");

            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 7) @AlmAnnotation(almTestId = "xxxxxx")
    public void UpdateExpertDeckWithAddingMultipleProductIds_SingleBookId() throws Exception {
        try {
            CreateExpertDeck();
            JSONObject updateExpertPayload = jsonReader.getJsonObject(ExpertDeckUpdatePayload);
            JSONArray bookIds = new JSONArray();
            if (Env.equals("PROD")){
                bookIds.add("BRNT-2VDGGDVYHJ5");
            }
            else {
                bookIds.add("ABYZ1RY3L9G");
            }
            JSONArray productIds = new JSONArray();
            if (Env.equals("PROD")){
                productIds.add("02e18048-3e2d-4869-a646-5f170714dfb2");
            }
            else {
                productIds.add("30b18904-dcc1-4f0f-bf0c-5cc91fa98e8b");
            }
            productIds.add("abc");
            JSONObject BookField = new JSONObject();
            BookField.put("bookTitle","Automation");
            BookField.put("bookIds",bookIds);
            BookField.put("chapter","1");
            BookField.put("bookAuthor","Automation author");
            BookField.put("chapterId","123");
            BookField.put("productIds",productIds);
            updateExpertPayload.put("book",BookField);

            System.out.println(updateExpertPayload);
            String payload = String.valueOf(updateExpertPayload);
            StoreControllerRequestDTO = mapper.readValue(payload, StoreControllerRequestDTO.class);

            String ExpertId = StoreControllerResponseDTO.getId();

            System.out.println("expert id is: " +ExpertId);
            StoreControllerResponseDTO = updateExpertDecks(headerAuthor, payload, ExpertId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(StoreControllerResponseDTO.getId(), ExpertId, "ExpertId");
            Assert.assertEquals(StoreControllerResponseDTO.getBook().getBookIds()[0], StoreControllerRequestDTO.getBook().getBookIds()[0], "ExpertId");
            Assert.assertEquals(StoreControllerResponseDTO.getBook().getProductIds()[0], StoreControllerRequestDTO.getBook().getProductIds()[0], "ExpertId");
            Assert.assertEquals(StoreControllerResponseDTO.getBook().getProductIds()[1], "abc", "ExpertId");

            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 8) @AlmAnnotation(almTestId = "xxxxxx")
    public void UpdateExpertDeckWithAddingSingleProductId_MultipleBookIds() throws Exception {
        try {
            CreateExpertDeck();
            JSONObject updateExpertPayload = jsonReader.getJsonObject(ExpertDeckUpdatePayload);
            JSONArray bookIds = new JSONArray();
            if (Env.equals("PROD")){
                bookIds.add("BRNT-2VDGGDVYHJ5");
            }
            else {
                bookIds.add("ABYZ1RY3L9G");
            }
            bookIds.add("123");
            JSONArray productIds = new JSONArray();
            if (Env.equals("PROD")){
                productIds.add("02e18048-3e2d-4869-a646-5f170714dfb2");
            }
            else {
                productIds.add("30b18904-dcc1-4f0f-bf0c-5cc91fa98e8b");
            }
            JSONObject BookField = new JSONObject();
            BookField.put("bookTitle","Automation");
            BookField.put("bookIds",bookIds);
            BookField.put("chapter","1");
            BookField.put("bookAuthor","Automation author");
            BookField.put("chapterId","123");
            BookField.put("productIds",productIds);
            updateExpertPayload.put("book",BookField);

            System.out.println(updateExpertPayload);
            String payload = String.valueOf(updateExpertPayload);
            StoreControllerRequestDTO = mapper.readValue(payload, StoreControllerRequestDTO.class);

            String ExpertId = StoreControllerResponseDTO.getId();

            System.out.println("expert id is: " +ExpertId);
            StoreControllerResponseDTO = updateExpertDecks(headerAuthor, payload, ExpertId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(StoreControllerResponseDTO.getId(), ExpertId, "ExpertId");
            Assert.assertEquals(StoreControllerResponseDTO.getBook().getBookIds()[0], StoreControllerRequestDTO.getBook().getBookIds()[0], "ExpertId");
            Assert.assertEquals(StoreControllerResponseDTO.getBook().getBookIds()[1],  "123","ExpertId");
            Assert.assertEquals(StoreControllerResponseDTO.getBook().getProductIds()[0], StoreControllerRequestDTO.getBook().getProductIds()[0], "ExpertId");

            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }


    @Test(priority = 9) @AlmAnnotation(almTestId = "xxxxxx")
    public void UpdateExpertDeckWithUpdatingStatus_as_Unpublished() throws Exception {
        try {
            CreateExpertDeck();
            JSONObject updateExpertPayload = jsonReader.getJsonObject(ExpertDeckUpdatePayload);
            JSONArray bookIds = new JSONArray();
            if (Env.equals("PROD")){
                bookIds.add("BRNT-2VDGGDVYHJ5");
            }
            else {
                bookIds.add("ABYZ1RY3L9G");
            }
            bookIds.add("123");
            JSONArray productIds = new JSONArray();
            if (Env.equals("PROD")){
                productIds.add("02e18048-3e2d-4869-a646-5f170714dfb2");
            }
            else {
                productIds.add("30b18904-dcc1-4f0f-bf0c-5cc91fa98e8b");
            }
            productIds.add("abc");
            JSONObject BookField = new JSONObject();
            BookField.put("bookTitle","Automation");
            BookField.put("bookIds",bookIds);
            BookField.put("chapter","1");
            BookField.put("bookAuthor","Automation author");
            BookField.put("chapterId","123");
            BookField.put("productIds",productIds);
            updateExpertPayload.put("book",BookField);
            updateExpertPayload.put("status","unpublished");

            System.out.println(updateExpertPayload);
            String payload = String.valueOf(updateExpertPayload);
            StoreControllerRequestDTO = mapper.readValue(payload, StoreControllerRequestDTO.class);

            String ExpertId = StoreControllerResponseDTO.getId();
            StoreControllerResponseDTO = updateExpertDecks(headerAuthor, payload, ExpertId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(StoreControllerResponseDTO.getId(), ExpertId, "ExpertId");
            Assert.assertEquals(StoreControllerResponseDTO.getBook().getBookIds()[0], StoreControllerRequestDTO.getBook().getBookIds()[0], "ExpertId");
            Assert.assertEquals(StoreControllerResponseDTO.getBook().getProductIds()[0], StoreControllerRequestDTO.getBook().getProductIds()[0], "ExpertId");
            Assert.assertEquals(StoreControllerResponseDTO.getStatus(), "unpublished", "ExpertId");

            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 10) @AlmAnnotation(almTestId = "xxxxxx")
    public void UpdateExpertDeckValidateWithUpdatingStatus_as_inactive() throws Exception {
        try {
            CreateExpertDeck();
            JSONObject updateExpertPayload = jsonReader.getJsonObject(ExpertDeckUpdatePayload);
            JSONArray bookIds = new JSONArray();
            bookIds.add("22847");
            bookIds.add("123");
            JSONArray productIds = new JSONArray();
            productIds.add("b45141e1-330a-4d35-ac86-6071e98faa60");
            productIds.add("abc");
            JSONObject BookField = new JSONObject();
            BookField.put("bookTitle","Automation");
            BookField.put("bookIds",bookIds);
            BookField.put("chapter","1");
            BookField.put("bookAuthor","Automation author");
            BookField.put("chapterId","123");
            BookField.put("productIds",productIds);
            updateExpertPayload.put("book",BookField);
            updateExpertPayload.put("status","inactive");

            System.out.println(updateExpertPayload);
            String payload = String.valueOf(updateExpertPayload);
            StoreControllerRequestDTO = mapper.readValue(payload, StoreControllerRequestDTO.class);

            String ExpertId = StoreControllerResponseDTO.getId();
            StoreControllerResponseDTO = updateExpertDecks(headerAuthor, payload, ExpertId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(StoreControllerResponseDTO.getDescription(), "Deck cannot be updated after publishing. Adding new bookIds or ProductIds and update status is allowed.", "ExpertId");
            StoreControllerResponseDTO = deleteExpert(headerAuthor, ExpertId);

            Assert.assertEquals(StatusCode, 204, "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 11) @AlmAnnotation(almTestId = "xxxxxx")
    public void UpdateExpertDeckValidateWithUpdatingAnyFieldExceptStatus_bookIds_productIds() throws Exception {
        try {
            CreateExpertDeck();
            JSONObject updateExpertPayload = jsonReader.getJsonObject(ExpertDeckUpdatePayload);
            JSONArray bookIds = new JSONArray();
            bookIds.add("22847");
            bookIds.add("123");
            JSONArray productIds = new JSONArray();
            productIds.add("b45141e1-330a-4d35-ac86-6071e98faa60");
            productIds.add("abc");
            JSONObject BookField = new JSONObject();
            BookField.put("bookTitle","Automation");
            BookField.put("bookIds",bookIds);
            BookField.put("chapter","1");
            BookField.put("bookAuthor","Automation author");
            BookField.put("chapterId","123");
            BookField.put("productIds",productIds);
            updateExpertPayload.put("book",BookField);
            updateExpertPayload.put("status","active");
            updateExpertPayload.put("title","Automation edited");

            System.out.println(updateExpertPayload);
            String payload = String.valueOf(updateExpertPayload);
            StoreControllerRequestDTO = mapper.readValue(payload, StoreControllerRequestDTO.class);

            String ExpertId = StoreControllerResponseDTO.getId();
            StoreControllerResponseDTO = updateExpertDecks(headerAuthor, payload, ExpertId);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(StoreControllerResponseDTO.getDescription(), "Deck cannot be updated after publishing. Adding new bookIds or ProductIds and update status is allowed.", "ExpertId");
            StoreControllerResponseDTO = deleteExpert(headerAuthor, ExpertId);

            Assert.assertEquals(StatusCode, 204, "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 12) @AlmAnnotation(almTestId = "xxxxxx")
    public void UpdateExpertDeckWithAddingMultipleProductIds_MultipleBookIdsWhenStatus_unpublished() throws Exception {
        try {
            CreateExpertDeck();

            JSONObject updateExpertPayload = jsonReader.getJsonObject(ExpertDeckUpdatePayload);
            updateExpertPayload.put("status","unpublished");
            System.out.println(updateExpertPayload);
            String payload = String.valueOf(updateExpertPayload);
            StoreControllerRequestDTO = mapper.readValue(payload, StoreControllerRequestDTO.class);
            String ExpertId = StoreControllerResponseDTO.getId();
            StoreControllerResponseDTO = updateExpertDecks(headerAuthor, payload, ExpertId);
            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(StoreControllerResponseDTO.getId(), ExpertId, "ExpertId");
            Assert.assertEquals(StoreControllerResponseDTO.getStatus(), "unpublished", "ExpertId");

            JSONObject updateExpert = jsonReader.getJsonObject(ExpertDeckUpdatePayload);
            JSONArray bookIds = new JSONArray();
            if (Env.equals("PROD")){
                bookIds.add("BRNT-2VDGGDVYHJ5");
            }
            else {
                bookIds.add("ABYZ1RY3L9G");
            }
            bookIds.add("123");
            JSONArray productIds = new JSONArray();
            if (Env.equals("PROD")){
                productIds.add("02e18048-3e2d-4869-a646-5f170714dfb2");
            }
            else {
                productIds.add("30b18904-dcc1-4f0f-bf0c-5cc91fa98e8b");
            }
            productIds.add("abc");
            JSONObject BookField = new JSONObject();
            BookField.put("bookTitle","Automation");
            BookField.put("bookIds",bookIds);
            BookField.put("chapter","1");
            BookField.put("bookAuthor","Automation author");
            BookField.put("chapterId","123");
            BookField.put("productIds",productIds);
            updateExpert.put("book",BookField);
            updateExpert.put("status","unpublished");

            System.out.println(updateExpert);
            StoreControllerRequestDTO = mapper.readValue(String.valueOf(updateExpert), StoreControllerRequestDTO.class);

            String payload1 = String.valueOf(updateExpert);
            StoreControllerResponseDTO = updateExpertDecks(headerAuthor, payload1, ExpertId);
            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(StoreControllerResponseDTO.getId(), ExpertId, "ExpertId");
            Assert.assertEquals(StoreControllerResponseDTO.getBook().getBookIds()[0], StoreControllerRequestDTO.getBook().getBookIds()[0], "ExpertId");
            Assert.assertEquals(StoreControllerResponseDTO.getBook().getBookIds()[1], "123", "ExpertId");
            Assert.assertEquals(StoreControllerResponseDTO.getBook().getProductIds()[0], StoreControllerRequestDTO.getBook().getProductIds()[0], "ExpertId");
            Assert.assertEquals(StoreControllerResponseDTO.getBook().getProductIds()[1], "abc", "ExpertId");
            Assert.assertEquals(StoreControllerResponseDTO.getStatus(), "unpublished", "ExpertId");

            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 13) @AlmAnnotation(almTestId = "xxxxxx")
    public void UpdateExpertDeckWithAddingMultipleProductIds_SingleBookIdsWhenStatus_unpublished() throws Exception {
        try {
            CreateExpertDeck();
            JSONObject updateExpertPayload = jsonReader.getJsonObject(ExpertDeckUpdatePayload);
            updateExpertPayload.put("status","unpublished");

            System.out.println(updateExpertPayload);
            String payload = String.valueOf(updateExpertPayload);
            StoreControllerRequestDTO = mapper.readValue(payload, StoreControllerRequestDTO.class);

            String ExpertId = StoreControllerResponseDTO.getId();
            StoreControllerResponseDTO = updateExpertDecks(headerAuthor, payload, ExpertId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(StoreControllerResponseDTO.getId(), ExpertId, "ExpertId");
            Assert.assertEquals(StoreControllerResponseDTO.getStatus(), "unpublished", "ExpertId");

            JSONObject updateExpert = jsonReader.getJsonObject(ExpertDeckUpdatePayload);
            JSONArray bookIds = new JSONArray();
            if (Env.equals("PROD")){
                bookIds.add("BRNT-2VDGGDVYHJ5");
            }
            else {
                bookIds.add("ABYZ1RY3L9G");
            }
            JSONArray productIds = new JSONArray();
            if (Env.equals("PROD")){
                productIds.add("02e18048-3e2d-4869-a646-5f170714dfb2");
            }
            else {
                productIds.add("30b18904-dcc1-4f0f-bf0c-5cc91fa98e8b");
            }
            productIds.add("abc");
            JSONObject BookField = new JSONObject();
            BookField.put("bookTitle","Automation");
            BookField.put("bookIds",bookIds);
            BookField.put("chapter","1");
            BookField.put("bookAuthor","Automation author");
            BookField.put("chapterId","123");
            BookField.put("productIds",productIds);
            updateExpert.put("book",BookField);
            updateExpert.put("status","unpublished");

            System.out.println(updateExpert);
            StoreControllerRequestDTO = mapper.readValue(String.valueOf(updateExpert), StoreControllerRequestDTO.class);

            String payload1 = String.valueOf(updateExpert);
            StoreControllerResponseDTO = updateExpertDecks(headerAuthor, payload1, ExpertId);
            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(StoreControllerResponseDTO.getId(), ExpertId, "ExpertId");
            Assert.assertEquals(StoreControllerResponseDTO.getBook().getBookIds()[0], StoreControllerRequestDTO.getBook().getBookIds()[0], "ExpertId");
            Assert.assertEquals(StoreControllerResponseDTO.getBook().getProductIds()[0], StoreControllerRequestDTO.getBook().getProductIds()[0], "ExpertId");
            Assert.assertEquals(StoreControllerResponseDTO.getBook().getProductIds()[1], "abc", "ExpertId");
            Assert.assertEquals(StoreControllerResponseDTO.getStatus(), "unpublished", "ExpertId");

            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 14) @AlmAnnotation(almTestId = "xxxxxx")
    public void UpdateExpertDeckWithAddingSingleProductIds_MultipleBookIdsWhenStatus_unpublished() throws Exception {
        try {
            CreateExpertDeck();
            JSONObject updateExpertPayload = jsonReader.getJsonObject(ExpertDeckUpdatePayload);
            updateExpertPayload.put("status","unpublished");

            System.out.println(updateExpertPayload);
            String payload = String.valueOf(updateExpertPayload);
            StoreControllerRequestDTO = mapper.readValue(payload, StoreControllerRequestDTO.class);

            String ExpertId = StoreControllerResponseDTO.getId();
            StoreControllerResponseDTO = updateExpertDecks(headerAuthor, payload, ExpertId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(StoreControllerResponseDTO.getId(), ExpertId, "ExpertId");
            Assert.assertEquals(StoreControllerResponseDTO.getStatus(), "unpublished", "ExpertId");

            JSONObject updateExpert = jsonReader.getJsonObject(ExpertDeckUpdatePayload);
            JSONArray bookIds = new JSONArray();
            if (Env.equals("PROD")){
                bookIds.add("BRNT-2VDGGDVYHJ5");
            }
            else {
                bookIds.add("ABYZ1RY3L9G");
            }
            bookIds.add("123");
            JSONArray productIds = new JSONArray();
            if (Env.equals("PROD")){
                productIds.add("02e18048-3e2d-4869-a646-5f170714dfb2");
            }
            else {
                productIds.add("30b18904-dcc1-4f0f-bf0c-5cc91fa98e8b");
            }
            JSONObject BookField = new JSONObject();
            BookField.put("bookTitle","Automation");
            BookField.put("bookIds",bookIds);
            BookField.put("chapter","1");
            BookField.put("bookAuthor","Automation author");
            BookField.put("chapterId","123");
            BookField.put("productIds",productIds);
            updateExpert.put("book",BookField);
            updateExpert.put("status","unpublished");

            System.out.println(updateExpert);
            StoreControllerRequestDTO = mapper.readValue(String.valueOf(updateExpert), StoreControllerRequestDTO.class);

            String payload1 = String.valueOf(updateExpert);
            StoreControllerResponseDTO = updateExpertDecks(headerAuthor, payload1, ExpertId);
            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(StoreControllerResponseDTO.getId(), ExpertId, "ExpertId");
            Assert.assertEquals(StoreControllerResponseDTO.getBook().getBookIds()[0], StoreControllerRequestDTO.getBook().getBookIds()[0], "ExpertId");
            Assert.assertEquals(StoreControllerResponseDTO.getBook().getBookIds()[1], "123", "ExpertId");
            Assert.assertEquals(StoreControllerResponseDTO.getBook().getProductIds()[0], StoreControllerRequestDTO.getBook().getProductIds()[0], "ExpertId");
            Assert.assertEquals(StoreControllerResponseDTO.getStatus(), "unpublished", "ExpertId");

            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 15) @AlmAnnotation(almTestId = "xxxxxx")
    public void UpdateExpertDeckValidateWithUpdatingStatus_from_unpublished_to_inactive() throws Exception {
        try {
            CreateExpertDeck();
            JSONObject updateExpertPayload = jsonReader.getJsonObject(ExpertDeckUpdatePayload);
            updateExpertPayload.put("status","unpublished");

            System.out.println(updateExpertPayload);
            String payload = String.valueOf(updateExpertPayload);
            StoreControllerRequestDTO = mapper.readValue(payload, StoreControllerRequestDTO.class);

            String ExpertId = StoreControllerResponseDTO.getId();
            StoreControllerResponseDTO = updateExpertDecks(headerAuthor, payload, ExpertId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(StoreControllerResponseDTO.getId(), ExpertId, "ExpertId");
            Assert.assertEquals(StoreControllerResponseDTO.getStatus(), "unpublished", "ExpertId");

            JSONObject updateExpert = jsonReader.getJsonObject(ExpertDeckUpdatePayload);
            updateExpert.put("status","inactive");

            System.out.println(updateExpert);
            StoreControllerRequestDTO = mapper.readValue(String.valueOf(updateExpert), StoreControllerRequestDTO.class);

            String payload1 = String.valueOf(updateExpert);
            StoreControllerResponseDTO = updateExpertDecks(headerAuthor, payload1, ExpertId);
            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(StoreControllerResponseDTO.getDescription(), "Deck cannot be updated after publishing. Adding new bookIds or ProductIds and update status is allowed.", "ExpertId");

            StoreControllerResponseDTO = deleteExpert(headerAuthor, ExpertId);

            Assert.assertEquals(StatusCode, 204, "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }


    @Test(priority = 16) @AlmAnnotation(almTestId = "xxxxxx")
    public void UpdateExpertDeckWithUpdatingStatus_from_unpublished_to_active() throws Exception {
        try {
            JSONObject createExpertDeckPayload = jsonReader.getJsonObject(ExpertDeckCreatePayload);
            createExpertDeckPayload.put("status","unpublished");
            System.out.println(createExpertDeckPayload);

            String payload = String.valueOf(createExpertDeckPayload);
            StoreControllerRequestDTO = mapper.readValue(payload, StoreControllerRequestDTO.class);
            StoreControllerResponseDTO = createExpertDeck(headerAuthor, payload);

            Assert.assertEquals(StatusCode, 201, "Status Code");
            LOGGER.info("deck Id:" + StoreControllerResponseDTO.getId());

            JSONObject updateExpertPayload = jsonReader.getJsonObject(ExpertDeckUpdatePayload);
            updateExpertPayload.put("status","active");

            System.out.println(updateExpertPayload);
            String payload1 = String.valueOf(updateExpertPayload);
            StoreControllerRequestDTO = mapper.readValue(payload, StoreControllerRequestDTO.class);

            String ExpertId = StoreControllerResponseDTO.getId();
            StoreControllerResponseDTO = updateExpertDecks(headerAuthor, payload1, ExpertId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(StoreControllerResponseDTO.getId(), ExpertId, "ExpertId");
            Assert.assertEquals(StoreControllerResponseDTO.getStatus(), "active", "ExpertId");

            StoreControllerResponseDTO = deleteExpert(headerAuthor, ExpertId);
            Assert.assertEquals(StatusCode, 204, "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }


    @Test(priority = 17) @AlmAnnotation(almTestId = "xxxxxx")
    public void UpdateExpertDeckWithAddingMultipleProductIds_MultipleBookIdsWhen_created_Status_unpublished() throws Exception {
        try {
            JSONObject createExpertDeckPayload = jsonReader.getJsonObject(ExpertDeckCreatePayload);
            createExpertDeckPayload.put("status","unpublished");
            System.out.println(createExpertDeckPayload);

            String payload = String.valueOf(createExpertDeckPayload);
            StoreControllerRequestDTO = mapper.readValue(payload, StoreControllerRequestDTO.class);
            StoreControllerResponseDTO = createExpertDeck(headerAuthor, payload);

            Assert.assertEquals(StatusCode, 201, "Status Code");
            LOGGER.info("deck Id:" + StoreControllerResponseDTO.getId());

            JSONObject updateExpertPayload = jsonReader.getJsonObject(ExpertDeckUpdatePayload);
            JSONArray bookIds = new JSONArray();
            if (Env.equals("PROD")){
                bookIds.add("BRNT-2VDGGDVYHJ5");
            }
            else {
                bookIds.add("ABYZ1RY3L9G");
            }
            bookIds.add("123");
            JSONArray productIds = new JSONArray();
            if (Env.equals("PROD")){
                productIds.add("02e18048-3e2d-4869-a646-5f170714dfb2");
            }
            else {
                productIds.add("30b18904-dcc1-4f0f-bf0c-5cc91fa98e8b");
            }
            productIds.add("abc");
            JSONObject BookField = new JSONObject();
            BookField.put("bookTitle","Automation");
            BookField.put("bookIds",bookIds);
            BookField.put("chapter","1");
            BookField.put("bookAuthor","Automation author");
            BookField.put("chapterId","123");
            BookField.put("productIds",productIds);
            updateExpertPayload.put("book",BookField);
            updateExpertPayload.put("status","unpublished");

            System.out.println(updateExpertPayload);
            StoreControllerRequestDTO = mapper.readValue(String.valueOf(updateExpertPayload), StoreControllerRequestDTO.class);

            String payload1 = String.valueOf(updateExpertPayload);
            String ExpertId = StoreControllerResponseDTO.getId();
            StoreControllerResponseDTO = updateExpertDecks(headerAuthor, payload1, ExpertId);
            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(StoreControllerResponseDTO.getId(), ExpertId, "ExpertId");
            Assert.assertEquals(StoreControllerResponseDTO.getBook().getBookIds()[0], StoreControllerRequestDTO.getBook().getBookIds()[0], "ExpertId");
            Assert.assertEquals(StoreControllerResponseDTO.getBook().getBookIds()[1], "123", "ExpertId");
            Assert.assertEquals(StoreControllerResponseDTO.getBook().getProductIds()[0], StoreControllerRequestDTO.getBook().getProductIds()[0], "ExpertId");
            Assert.assertEquals(StoreControllerResponseDTO.getBook().getProductIds()[1], "abc", "ExpertId");

            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 18) @AlmAnnotation(almTestId = "xxxxxx")
    public void UpdateExpertDeckWithAddingSingleProductId_MultipleBookIdsWhen_created_Status_unpublished() throws Exception {
        try {
            JSONObject createExpertDeckPayload = jsonReader.getJsonObject(ExpertDeckCreatePayload);
            createExpertDeckPayload.put("status","unpublished");
            System.out.println(createExpertDeckPayload);

            String payload = String.valueOf(createExpertDeckPayload);
            StoreControllerRequestDTO = mapper.readValue(payload, StoreControllerRequestDTO.class);
            StoreControllerResponseDTO = createExpertDeck(headerAuthor, payload);

            Assert.assertEquals(StatusCode, 201, "Status Code");
            LOGGER.info("deck Id:" + StoreControllerResponseDTO.getId());

            JSONObject updateExpertPayload = jsonReader.getJsonObject(ExpertDeckUpdatePayload);
            JSONArray bookIds = new JSONArray();
            if (Env.equals("PROD")){
                bookIds.add("BRNT-2VDGGDVYHJ5");
            }
            else {
                bookIds.add("ABYZ1RY3L9G");
            }
            bookIds.add("123");
            JSONArray productIds = new JSONArray();
            if (Env.equals("PROD")){
                productIds.add("02e18048-3e2d-4869-a646-5f170714dfb2");
            }
            else {
                productIds.add("30b18904-dcc1-4f0f-bf0c-5cc91fa98e8b");
            }
            JSONObject BookField = new JSONObject();
            BookField.put("bookTitle","Automation");
            BookField.put("bookIds",bookIds);
            BookField.put("chapter","1");
            BookField.put("bookAuthor","Automation author");
            BookField.put("chapterId","123");
            BookField.put("productIds",productIds);
            updateExpertPayload.put("book",BookField);
            updateExpertPayload.put("status","unpublished");

            System.out.println(updateExpertPayload);
            StoreControllerRequestDTO = mapper.readValue(String.valueOf(updateExpertPayload), StoreControllerRequestDTO.class);

            String payload1 = String.valueOf(updateExpertPayload);
            String ExpertId = StoreControllerResponseDTO.getId();
            StoreControllerResponseDTO = updateExpertDecks(headerAuthor, payload1, ExpertId);
            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(StoreControllerResponseDTO.getId(), ExpertId, "ExpertId");
            Assert.assertEquals(StoreControllerResponseDTO.getBook().getBookIds()[0], StoreControllerRequestDTO.getBook().getBookIds()[0], "ExpertId");
            Assert.assertEquals(StoreControllerResponseDTO.getBook().getBookIds()[1], "123", "ExpertId");
            Assert.assertEquals(StoreControllerResponseDTO.getBook().getProductIds()[0], StoreControllerRequestDTO.getBook().getProductIds()[0], "ExpertId");

            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }


    @Test(priority = 19) @AlmAnnotation(almTestId = "xxxxxx")
    public void UpdateExpertDeckWithAddingMultipleProductIds_SingleBookIdWhen_created_Status_unpublished() throws Exception {
        try {
            JSONObject createExpertDeckPayload = jsonReader.getJsonObject(ExpertDeckCreatePayload);
            createExpertDeckPayload.put("status","unpublished");
            System.out.println(createExpertDeckPayload);

            String payload = String.valueOf(createExpertDeckPayload);
            StoreControllerRequestDTO = mapper.readValue(payload, StoreControllerRequestDTO.class);
            StoreControllerResponseDTO = createExpertDeck(headerAuthor, payload);

            Assert.assertEquals(StatusCode, 201, "Status Code");
            LOGGER.info("deck Id:" + StoreControllerResponseDTO.getId());


            JSONObject updateExpertPayload = jsonReader.getJsonObject(ExpertDeckUpdatePayload);
            JSONArray bookIds = new JSONArray();
            if (Env.equals("PROD")){
                bookIds.add("BRNT-2VDGGDVYHJ5");
            }
            else {
                bookIds.add("ABYZ1RY3L9G");
            }
            JSONArray productIds = new JSONArray();
            if (Env.equals("PROD")){
                productIds.add("02e18048-3e2d-4869-a646-5f170714dfb2");
            }
            else {
                productIds.add("30b18904-dcc1-4f0f-bf0c-5cc91fa98e8b");
            }
            productIds.add("abc");
            JSONObject BookField = new JSONObject();
            BookField.put("bookTitle","Automation");
            BookField.put("bookIds",bookIds);
            BookField.put("chapter","1");
            BookField.put("bookAuthor","Automation author");
            BookField.put("chapterId","123");
            BookField.put("productIds",productIds);
            updateExpertPayload.put("book",BookField);
            updateExpertPayload.put("status","unpublished");

            System.out.println(updateExpertPayload);
            StoreControllerRequestDTO = mapper.readValue(String.valueOf(updateExpertPayload), StoreControllerRequestDTO.class);

            String payload1 = String.valueOf(updateExpertPayload);
            String ExpertId = StoreControllerResponseDTO.getId();
            StoreControllerResponseDTO = updateExpertDecks(headerAuthor, payload1, ExpertId);
            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(StoreControllerResponseDTO.getId(), ExpertId, "ExpertId");
            Assert.assertEquals(StoreControllerResponseDTO.getBook().getBookIds()[0], StoreControllerRequestDTO.getBook().getBookIds()[0], "ExpertId");
            Assert.assertEquals(StoreControllerResponseDTO.getBook().getProductIds()[0], StoreControllerRequestDTO.getBook().getProductIds()[0], "ExpertId");
            Assert.assertEquals(StoreControllerResponseDTO.getBook().getProductIds()[1], "abc", "ExpertId");

            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 20) @AlmAnnotation(almTestId = "xxxxxx")
    public void UpdateExpertDeckWithAddingMultipleProductIds_SingleBookIdWhen_created_Status_inactive() throws Exception {
        try {
            JSONObject createExpertDeckPayload = jsonReader.getJsonObject(ExpertDeckCreatePayload);
            createExpertDeckPayload.put("status","inactive");
            System.out.println(createExpertDeckPayload);

            String payload = String.valueOf(createExpertDeckPayload);
            StoreControllerRequestDTO = mapper.readValue(payload, StoreControllerRequestDTO.class);
            StoreControllerResponseDTO = createExpertDeck(headerAuthor, payload);

            Assert.assertEquals(StatusCode, 201, "Status Code");
            LOGGER.info("deck Id:" + StoreControllerResponseDTO.getId());

            JSONObject updateExpertPayload = jsonReader.getJsonObject(ExpertDeckUpdatePayload);
            JSONArray bookIds = new JSONArray();
            bookIds.add("22847");
            bookIds.add("123");
            JSONArray productIds = new JSONArray();
            productIds.add("b45141e1-330a-4d35-ac86-6071e98faa60");
            productIds.add("abc");
            JSONObject BookField = new JSONObject();
            BookField.put("bookTitle","Automation edited");
            BookField.put("bookIds",bookIds);
            BookField.put("chapter","1");
            BookField.put("bookAuthor","Automation author edited");
            BookField.put("chapterId","123");
            BookField.put("productIds",productIds);
            updateExpertPayload.put("book",BookField);
            updateExpertPayload.put("status","inactive");
            updateExpertPayload.put("title","kasun - 13446 edited");

            System.out.println(updateExpertPayload);
            StoreControllerRequestDTO = mapper.readValue(String.valueOf(updateExpertPayload), StoreControllerRequestDTO.class);

            String payload1 = String.valueOf(updateExpertPayload);
            String ExpertId = StoreControllerResponseDTO.getId();
            StoreControllerResponseDTO = updateExpertDecks(headerAuthor, payload1, ExpertId);
            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(StoreControllerResponseDTO.getId(), ExpertId, "ExpertId");
            Assert.assertEquals(StoreControllerResponseDTO.getBook().getBookIds()[0], "22847", "ExpertId");
            Assert.assertEquals(StoreControllerResponseDTO.getBook().getBookIds()[1], "123", "ExpertId");
            Assert.assertEquals(StoreControllerResponseDTO.getBook().getProductIds()[0], "b45141e1-330a-4d35-ac86-6071e98faa60", "ExpertId");
            Assert.assertEquals(StoreControllerResponseDTO.getBook().getProductIds()[1], "abc", "ExpertId");

            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 21) @AlmAnnotation(almTestId = "xxxxxx")
    public void UpdateExpertDeckValidateWithUpdatingCreated_Status_from_inactive_to_active_and_back_to_inactive() throws Exception {
        try {
            JSONObject createExpertDeckPayload = jsonReader.getJsonObject(ExpertDeckCreatePayload);
            createExpertDeckPayload.put("status","inactive");
            System.out.println(createExpertDeckPayload);

            String payload = String.valueOf(createExpertDeckPayload);
            StoreControllerRequestDTO = mapper.readValue(payload, StoreControllerRequestDTO.class);
            StoreControllerResponseDTO = createExpertDeck(headerAuthor, payload);

            Assert.assertEquals(StatusCode, 201, "Status Code");
            LOGGER.info("deck Id:" + StoreControllerResponseDTO.getId());


            JSONObject updateExpertPayload = jsonReader.getJsonObject(ExpertDeckUpdatePayload);
            updateExpertPayload.put("status","active");

            System.out.println(updateExpertPayload);
            StoreControllerRequestDTO = mapper.readValue(String.valueOf(updateExpertPayload), StoreControllerRequestDTO.class);

            String payload1 = String.valueOf(updateExpertPayload);
            String ExpertId = StoreControllerResponseDTO.getId();
            StoreControllerResponseDTO = updateExpertDecks(headerAuthor, payload1, ExpertId);
            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(StoreControllerResponseDTO.getId(), ExpertId, "ExpertId");
            Assert.assertEquals(StoreControllerResponseDTO.getStatus(),"active");

            JSONObject updateExpert = jsonReader.getJsonObject(ExpertDeckUpdatePayload);
            updateExpert.put("status","inactive");

            System.out.println(updateExpert);
            StoreControllerRequestDTO = mapper.readValue(String.valueOf(updateExpert), StoreControllerRequestDTO.class);

            String payload2 = String.valueOf(updateExpert);
            StoreControllerResponseDTO = updateExpertDecks(headerAuthor, payload2, ExpertId);
            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(StoreControllerResponseDTO.getDescription(), "Deck cannot be updated after publishing. Adding new bookIds or ProductIds and update status is allowed.", "ExpertId");

            StoreControllerResponseDTO = deleteExpert(headerAuthor, ExpertId);
            Assert.assertEquals(StatusCode, 204, "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 22) @AlmAnnotation(almTestId = "xxxxxx")
    public void UpdateExpertDeckValidateWithUpdatingCreated_Status_from_inactive_to_unpublished_and_back_to_inactive() throws Exception {
        try {
            JSONObject createExpertDeckPayload = jsonReader.getJsonObject(ExpertDeckCreatePayload);
            createExpertDeckPayload.put("status","inactive");
            System.out.println(createExpertDeckPayload);

            String payload = String.valueOf(createExpertDeckPayload);
            StoreControllerRequestDTO = mapper.readValue(payload, StoreControllerRequestDTO.class);
            StoreControllerResponseDTO = createExpertDeck(headerAuthor, payload);

            Assert.assertEquals(StatusCode, 201, "Status Code");
            LOGGER.info("deck Id:" + StoreControllerResponseDTO.getId());


            JSONObject updateExpertPayload = jsonReader.getJsonObject(ExpertDeckUpdatePayload);
            updateExpertPayload.put("status","unpublished");

            System.out.println(updateExpertPayload);
            StoreControllerRequestDTO = mapper.readValue(String.valueOf(updateExpertPayload), StoreControllerRequestDTO.class);

            String payload1 = String.valueOf(updateExpertPayload);
            String ExpertId = StoreControllerResponseDTO.getId();
            StoreControllerResponseDTO = updateExpertDecks(headerAuthor, payload1, ExpertId);
            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(StoreControllerResponseDTO.getId(), ExpertId, "ExpertId");
            Assert.assertEquals(StoreControllerResponseDTO.getStatus(),"unpublished");

            JSONObject updateExpert = jsonReader.getJsonObject(ExpertDeckUpdatePayload);
            updateExpert.put("status","inactive");

            System.out.println(updateExpert);
            StoreControllerRequestDTO = mapper.readValue(String.valueOf(updateExpert), StoreControllerRequestDTO.class);

            String payload2 = String.valueOf(updateExpert);
            StoreControllerResponseDTO = updateExpertDecks(headerAuthor, payload2, ExpertId);
            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(StoreControllerResponseDTO.getDescription(), "Deck cannot be updated after publishing. Adding new bookIds or ProductIds and update status is allowed.", "ExpertId");

            StoreControllerResponseDTO = deleteExpert(headerAuthor, ExpertId);
            Assert.assertEquals(StatusCode, 204, "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 23) @AlmAnnotation(almTestId = "xxxxxx")
    public void UpdateExpertDeckUsingInvalidDeckId() throws Exception {
        try {
            JSONObject updateExpertPayload = jsonReader.getJsonObject(ExpertDeckUpdatePayload);
            System.out.println(updateExpertPayload);
            StoreControllerRequestDTO = mapper.readValue(String.valueOf(updateExpertPayload), StoreControllerRequestDTO.class);

            String payload = String.valueOf(updateExpertPayload);
            String ExpertId = "123";
            StoreControllerResponseDTO = updateExpertDecks(headerAuthor, payload, ExpertId);
            Assert.assertEquals(StatusCode, 404, "Status Code");
            Assert.assertEquals(StoreControllerResponseDTO.getDescription(), "Invalid Deck ID", "ExpertId");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 24) @AlmAnnotation(almTestId = "xxxxxx")
    public void UpdateExpertDeckUsingNullforDeckId() throws Exception {
        try {
            JSONObject updateExpertPayload = jsonReader.getJsonObject(ExpertDeckUpdatePayload);
            System.out.println(updateExpertPayload);
            StoreControllerRequestDTO = mapper.readValue(String.valueOf(updateExpertPayload), StoreControllerRequestDTO.class);

            String payload = String.valueOf(updateExpertPayload);
            String ExpertId = "null";
            StoreControllerResponseDTO = updateExpertDecks(headerAuthor, payload, ExpertId);
            Assert.assertEquals(StatusCode, 404, "Status Code");
            Assert.assertEquals(StoreControllerResponseDTO.getDescription(), "Invalid Deck ID", "ExpertId");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 25) @AlmAnnotation(almTestId = "xxxxxx")
    public void UpdateExpertDeckUsingDeletedDeckId() throws Exception {
        try {
            CreateExpertDeck();
            String ExpertId = StoreControllerResponseDTO.getId();
            DeleteExpertDeck();
            JSONObject updateExpertPayload = jsonReader.getJsonObject(ExpertDeckUpdatePayload);
            System.out.println(updateExpertPayload);
            StoreControllerRequestDTO = mapper.readValue(String.valueOf(updateExpertPayload), StoreControllerRequestDTO.class);

            String payload = String.valueOf(updateExpertPayload);
            StoreControllerResponseDTO = updateExpertDecks(headerAuthor, payload, ExpertId);
            Assert.assertEquals(StatusCode, 404, "Status Code");
            Assert.assertEquals(StoreControllerResponseDTO.getDescription(), "Invalid Deck ID", "ExpertId");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 26) @AlmAnnotation(almTestId = "xxxxxx")
    public void UpdateExpertDeckFullPayloadWithUsingNormalUser() throws Exception {
        try {
            //CreateExpertDeck();
            JSONObject updateExpertPayload = jsonReader.getJsonObject(ExpertDeckUpdatePayload);
            System.out.println(updateExpertPayload);
            String payload = String.valueOf(updateExpertPayload);
            StoreControllerRequestDTO = mapper.readValue(payload, StoreControllerRequestDTO.class);

            String ExpertId = "123";
            StoreControllerResponseDTO = updateExpertDecks(headerNormal, payload, ExpertId);

            Assert.assertEquals(StatusCode, 403, "Status Code");
            Assert.assertEquals(StoreControllerResponseDTO.getDescription(),"You do not have UPDATE_EXPERT_DECK permission to access this method");

//            StoreControllerResponseDTO = deleteExpert(headerAuthor, ExpertId);
//            Assert.assertEquals(StatusCode, 204, "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }
}
