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

public class CreateExpertDeck_StoreController extends ServiceController {

    //private String defaultContentType = "JSON";
    private StoreControllerResponseDTO StoreControllerResponseDTO;
    private StoreControllerRequestDTO StoreControllerRequestDTO;
    private ExpertQuestionResponseDTO ExpertQuestionResponseDTO;
    private static final PiTokenGenaratorAuthor PiTokenGenaratorAuthor = new PiTokenGenaratorAuthor();
    private static final utils.PiTokenGenaratorNormalUser PiTokenGenaratorNormalUser = new PiTokenGenaratorNormalUser();

    private static final HashMap<String, String> headerAuthor = new HashMap<>();
    private static final HashMap<String, String> headerNormal = new HashMap<>();
    JsonReader jsonReader = new JsonReader();

    private static final Logger LOGGER = Logger.getLogger(String.valueOf(CreateExpertDeck_StoreController.class));

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


    //create an expert deck with only book id
    @Test(priority = 6) @AlmAnnotation(almTestId = "xxxxxxxx")
    public void CreateExpertDeckWithOnlyBookID() throws Exception {
        try {
            // Read json object from file
            JSONObject createExpertDeckPayload = jsonReader.getJsonObject(ExpertDeckCreatePayload);
            JSONArray bookIds = new JSONArray();
            bookIds.add("22847");
            JSONObject BookField = new JSONObject();
            BookField.put("bookTitle","Automation");
            BookField.put("bookIds",bookIds);
            BookField.put("chapter","1");
            BookField.put("bookAuthor","Automation author");
            BookField.put("chapterId","123");
            createExpertDeckPayload.put("book",BookField);
            System.out.println(createExpertDeckPayload);

            String payload = String.valueOf(createExpertDeckPayload);
            StoreControllerRequestDTO = mapper.readValue(payload, StoreControllerRequestDTO.class);
            StoreControllerResponseDTO = createExpertDeck(headerAuthor, payload);

            Assert.assertEquals(StatusCode, 201, "Status Code");
            LOGGER.info("deck Id:" + StoreControllerResponseDTO.getId());
            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }


    //create an expert deck with only Product id
    @Test(priority = 7) @AlmAnnotation(almTestId = "xxxxxxxx")
    public void CreateExpertDeckWithOnlyProductID() throws Exception {
        try {
            // Read json object from file
            JSONObject createExpertDeckPayload = jsonReader.getJsonObject(ExpertDeckCreatePayload);
            JSONArray productIds = new JSONArray();
            productIds.add("b45141e1-330a-4d35-ac86-6071e98faa60");
            JSONObject BookField = new JSONObject();
            BookField.put("bookTitle","Automation");
            BookField.put("chapter","1");
            BookField.put("bookAuthor","Automation author");
            BookField.put("chapterId","123");
            BookField.put("productIds",productIds);
            createExpertDeckPayload.put("book",BookField);
            System.out.println(createExpertDeckPayload);

            String payload = String.valueOf(createExpertDeckPayload);
            StoreControllerRequestDTO = mapper.readValue(payload, StoreControllerRequestDTO.class);
            StoreControllerResponseDTO = createExpertDeck(headerAuthor, payload);

            Assert.assertEquals(StatusCode, 201, "Status Code");
            LOGGER.info("deck Id:" + StoreControllerResponseDTO.getId());
            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    //verify whether expert deck can be created with multiple productIds and multiple bookIds
    @Test(priority = 8) @AlmAnnotation(almTestId = "xxxxxxxx")
    public void CreateExpertDeckWithMultipleBookIDs_ProductIDs() throws Exception {
        try {
            // Read json object from file
            JSONObject createExpertDeckPayload = jsonReader.getJsonObject(ExpertDeckCreatePayload);
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
            createExpertDeckPayload.put("book",BookField);
            System.out.println(createExpertDeckPayload);

            String payload = String.valueOf(createExpertDeckPayload);
            StoreControllerRequestDTO = mapper.readValue(payload, StoreControllerRequestDTO.class);
            StoreControllerResponseDTO = createExpertDeck(headerAuthor, payload);

            Assert.assertEquals(StatusCode, 201, "Status Code");
            LOGGER.info("deck Id:" + StoreControllerResponseDTO.getId());
            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    //verify whether expert deck can be created with Only multiple bookIds
    @Test(priority = 9) @AlmAnnotation(almTestId = "xxxxxxxx")
    public void CreateExpertDeckWithOnlyMultipleBookIDs() throws Exception {
        try {
            // Read json object from file
            JSONObject createExpertDeckPayload = jsonReader.getJsonObject(ExpertDeckCreatePayload);
            JSONArray bookIds = new JSONArray();
            bookIds.add("22847");
            bookIds.add("123");
            JSONObject BookField = new JSONObject();
            BookField.put("bookTitle","Automation");
            BookField.put("bookIds",bookIds);
            BookField.put("chapter","1");
            BookField.put("bookAuthor","Automation author");
            BookField.put("chapterId","123");
            createExpertDeckPayload.put("book",BookField);
            System.out.println(createExpertDeckPayload);

            String payload = String.valueOf(createExpertDeckPayload);
            StoreControllerRequestDTO = mapper.readValue(payload, StoreControllerRequestDTO.class);
            StoreControllerResponseDTO = createExpertDeck(headerAuthor, payload);

            Assert.assertEquals(StatusCode, 201, "Status Code");
            LOGGER.info("deck Id:" + StoreControllerResponseDTO.getId());
            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    //verify whether expert deck can be created with Only multiple productIds
    @Test(priority = 10) @AlmAnnotation(almTestId = "xxxxxxxx")
    public void CreateExpertDeckWithOnlyMultipleProductIds() throws Exception {
        try {
            // Read json object from file
            JSONObject createExpertDeckPayload = jsonReader.getJsonObject(ExpertDeckCreatePayload);
            JSONArray productIds = new JSONArray();
            productIds.add("b45141e1-330a-4d35-ac86-6071e98faa60");
            productIds.add("abc");
            JSONObject BookField = new JSONObject();
            BookField.put("bookTitle","Automation");
            BookField.put("chapter","1");
            BookField.put("bookAuthor","Automation author");
            BookField.put("chapterId","123");
            BookField.put("productIds",productIds);
            createExpertDeckPayload.put("book",BookField);
            System.out.println(createExpertDeckPayload);

            String payload = String.valueOf(createExpertDeckPayload);
            StoreControllerRequestDTO = mapper.readValue(payload, StoreControllerRequestDTO.class);
            StoreControllerResponseDTO = createExpertDeck(headerAuthor, payload);

            Assert.assertEquals(StatusCode, 201, "Status Code");
            LOGGER.info("deck Id:" + StoreControllerResponseDTO.getId());
            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    //verify whether expert deck can be created with multiple productIds and single bookId
    @Test(priority = 11) @AlmAnnotation(almTestId = "xxxxxxxx")
    public void CreateExpertDeckWithOnlyMultipleProductIds_SingleBookId() throws Exception {
        try {
            // Read json object from file
            JSONObject createExpertDeckPayload = jsonReader.getJsonObject(ExpertDeckCreatePayload);
            JSONArray bookIds = new JSONArray();
            bookIds.add("22847");
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
            createExpertDeckPayload.put("book",BookField);
            System.out.println(createExpertDeckPayload);

            String payload = String.valueOf(createExpertDeckPayload);
            StoreControllerRequestDTO = mapper.readValue(payload, StoreControllerRequestDTO.class);
            StoreControllerResponseDTO = createExpertDeck(headerAuthor, payload);

            Assert.assertEquals(StatusCode, 201, "Status Code");
            LOGGER.info("deck Id:" + StoreControllerResponseDTO.getId());
            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }


    //verify whether expert deck can be created with multiple productIds and single bookId
    @Test(priority = 12) @AlmAnnotation(almTestId = "xxxxxxxx")
    public void CreateExpertDeckWithOnlyMultipleBookIds_SingleProductId() throws Exception {
        try {
            // Read json object from file
            JSONObject createExpertDeckPayload = jsonReader.getJsonObject(ExpertDeckCreatePayload);
            JSONArray bookIds = new JSONArray();
            bookIds.add("22847");
            bookIds.add("123");
            JSONArray productIds = new JSONArray();
            productIds.add("b45141e1-330a-4d35-ac86-6071e98faa60");
            JSONObject BookField = new JSONObject();
            BookField.put("bookTitle","Automation");
            BookField.put("bookIds",bookIds);
            BookField.put("chapter","1");
            BookField.put("bookAuthor","Automation author");
            BookField.put("chapterId","123");
            BookField.put("productIds",productIds);
            createExpertDeckPayload.put("book",BookField);
            System.out.println(createExpertDeckPayload);

            String payload = String.valueOf(createExpertDeckPayload);
            StoreControllerRequestDTO = mapper.readValue(payload, StoreControllerRequestDTO.class);
            StoreControllerResponseDTO = createExpertDeck(headerAuthor, payload);

            Assert.assertEquals(StatusCode, 201, "Status Code");
            LOGGER.info("deck Id:" + StoreControllerResponseDTO.getId());
            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 13) @AlmAnnotation(almTestId = "xxxxxx")
    public void CreateExpertDeckWithMultipleBookIds_MultipleProductIds_when_Status_unpublished() throws Exception {
        try {
            JSONObject createExpertDeckPayload = jsonReader.getJsonObject(ExpertDeckCreatePayload);
            createExpertDeckPayload.put("status","unpublished");
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
            createExpertDeckPayload.put("book",BookField);
            createExpertDeckPayload.put("status","unpublished");
            System.out.println(createExpertDeckPayload);

            String payload = String.valueOf(createExpertDeckPayload);
            StoreControllerRequestDTO = mapper.readValue(payload, StoreControllerRequestDTO.class);
            StoreControllerResponseDTO = createExpertDeck(headerAuthor, payload);

            Assert.assertEquals(StatusCode, 201, "Status Code");
            LOGGER.info("deck Id:" + StoreControllerResponseDTO.getId());
            Assert.assertEquals(StoreControllerResponseDTO.getBook().getBookIds()[0], "22847", "ExpertId");
            Assert.assertEquals(StoreControllerResponseDTO.getBook().getBookIds()[1], "123", "ExpertId");
            Assert.assertEquals(StoreControllerResponseDTO.getBook().getProductIds()[0], "b45141e1-330a-4d35-ac86-6071e98faa60", "ExpertId");
            Assert.assertEquals(StoreControllerResponseDTO.getBook().getProductIds()[1], "abc", "ExpertId");

            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 14) @AlmAnnotation(almTestId = "xxxxxx")
    public void CreateExpertDeckWithOnlySingleBookId_MultipleProductIds_when_Status_unpublished() throws Exception {
        try {
            JSONObject createExpertDeckPayload = jsonReader.getJsonObject(ExpertDeckCreatePayload);
            createExpertDeckPayload.put("status","unpublished");
            JSONArray bookIds = new JSONArray();
            bookIds.add("22847");
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
            createExpertDeckPayload.put("book",BookField);
            createExpertDeckPayload.put("status","unpublished");
            System.out.println(createExpertDeckPayload);

            String payload = String.valueOf(createExpertDeckPayload);
            StoreControllerRequestDTO = mapper.readValue(payload, StoreControllerRequestDTO.class);
            StoreControllerResponseDTO = createExpertDeck(headerAuthor, payload);

            Assert.assertEquals(StatusCode, 201, "Status Code");
            LOGGER.info("deck Id:" + StoreControllerResponseDTO.getId());
            Assert.assertEquals(StoreControllerResponseDTO.getBook().getBookIds()[0], "22847", "ExpertId");
            Assert.assertEquals(StoreControllerResponseDTO.getBook().getProductIds()[0], "b45141e1-330a-4d35-ac86-6071e98faa60", "ExpertId");
            Assert.assertEquals(StoreControllerResponseDTO.getBook().getProductIds()[1], "abc", "ExpertId");

            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 15) @AlmAnnotation(almTestId = "xxxxxx")
    public void CreateExpertDeckWithOnlyMultipleBookIds_SingleProductId_when_Status_unpublished() throws Exception {
        try {
            JSONObject createExpertDeckPayload = jsonReader.getJsonObject(ExpertDeckCreatePayload);
            createExpertDeckPayload.put("status","unpublished");
            JSONArray bookIds = new JSONArray();
            bookIds.add("22847");
            bookIds.add("123");
            JSONArray productIds = new JSONArray();
            productIds.add("b45141e1-330a-4d35-ac86-6071e98faa60");
            JSONObject BookField = new JSONObject();
            BookField.put("bookTitle","Automation");
            BookField.put("bookIds",bookIds);
            BookField.put("chapter","1");
            BookField.put("bookAuthor","Automation author");
            BookField.put("chapterId","123");
            BookField.put("productIds",productIds);
            createExpertDeckPayload.put("book",BookField);
            createExpertDeckPayload.put("status","unpublished");
            System.out.println(createExpertDeckPayload);

            String payload = String.valueOf(createExpertDeckPayload);
            StoreControllerRequestDTO = mapper.readValue(payload, StoreControllerRequestDTO.class);
            StoreControllerResponseDTO = createExpertDeck(headerAuthor, payload);

            Assert.assertEquals(StatusCode, 201, "Status Code");
            LOGGER.info("deck Id:" + StoreControllerResponseDTO.getId());
            Assert.assertEquals(StoreControllerResponseDTO.getBook().getBookIds()[0], "22847", "ExpertId");
            Assert.assertEquals(StoreControllerResponseDTO.getBook().getBookIds()[1], "123", "ExpertId");
            Assert.assertEquals(StoreControllerResponseDTO.getBook().getProductIds()[0], "b45141e1-330a-4d35-ac86-6071e98faa60", "ExpertId");

            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 16) @AlmAnnotation(almTestId = "xxxxxx")
    public void CreateExpertDeckWithMultipleBookIds_MultipleProductIds_when_Status_inactive() throws Exception {
        try {
            JSONObject createExpertDeckPayload = jsonReader.getJsonObject(ExpertDeckCreatePayload);
            createExpertDeckPayload.put("status","inactive");
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
            createExpertDeckPayload.put("book",BookField);
            createExpertDeckPayload.put("status","inactive");
            System.out.println(createExpertDeckPayload);

            String payload = String.valueOf(createExpertDeckPayload);
            StoreControllerRequestDTO = mapper.readValue(payload, StoreControllerRequestDTO.class);
            StoreControllerResponseDTO = createExpertDeck(headerAuthor, payload);

            Assert.assertEquals(StatusCode, 201, "Status Code");
            LOGGER.info("deck Id:" + StoreControllerResponseDTO.getId());
            Assert.assertEquals(StoreControllerResponseDTO.getBook().getBookIds()[0], "22847", "ExpertId");
            Assert.assertEquals(StoreControllerResponseDTO.getBook().getBookIds()[1], "123", "ExpertId");
            Assert.assertEquals(StoreControllerResponseDTO.getBook().getProductIds()[0], "b45141e1-330a-4d35-ac86-6071e98faa60", "ExpertId");
            Assert.assertEquals(StoreControllerResponseDTO.getBook().getProductIds()[1], "abc", "ExpertId");

            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 17) @AlmAnnotation(almTestId = "xxxxxx")
    public void CreateExpertDeckWithOnlySingleBookId_MultipleProductIds_when_Status_inactive() throws Exception {
        try {
            JSONObject createExpertDeckPayload = jsonReader.getJsonObject(ExpertDeckCreatePayload);
            createExpertDeckPayload.put("status","inactive");
            JSONArray bookIds = new JSONArray();
            bookIds.add("22847");
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
            createExpertDeckPayload.put("book",BookField);
            createExpertDeckPayload.put("status","inactive");
            System.out.println(createExpertDeckPayload);

            String payload = String.valueOf(createExpertDeckPayload);
            StoreControllerRequestDTO = mapper.readValue(payload, StoreControllerRequestDTO.class);
            StoreControllerResponseDTO = createExpertDeck(headerAuthor, payload);

            Assert.assertEquals(StatusCode, 201, "Status Code");
            LOGGER.info("deck Id:" + StoreControllerResponseDTO.getId());
            Assert.assertEquals(StoreControllerResponseDTO.getBook().getBookIds()[0], "22847", "ExpertId");
            Assert.assertEquals(StoreControllerResponseDTO.getBook().getProductIds()[0], "b45141e1-330a-4d35-ac86-6071e98faa60", "ExpertId");
            Assert.assertEquals(StoreControllerResponseDTO.getBook().getProductIds()[1], "abc", "ExpertId");

            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 18) @AlmAnnotation(almTestId = "xxxxxx")
    public void CreateExpertDeckWithOnlyMultipleBookIds_SingleProductId_when_Status_inactive() throws Exception {
        try {
            JSONObject createExpertDeckPayload = jsonReader.getJsonObject(ExpertDeckCreatePayload);
            createExpertDeckPayload.put("status","inactive");
            JSONArray bookIds = new JSONArray();
            bookIds.add("22847");
            bookIds.add("123");
            JSONArray productIds = new JSONArray();
            productIds.add("b45141e1-330a-4d35-ac86-6071e98faa60");
            JSONObject BookField = new JSONObject();
            BookField.put("bookTitle","Automation");
            BookField.put("bookIds",bookIds);
            BookField.put("chapter","1");
            BookField.put("bookAuthor","Automation author");
            BookField.put("chapterId","123");
            BookField.put("productIds",productIds);
            createExpertDeckPayload.put("book",BookField);
            createExpertDeckPayload.put("status","inactive");
            System.out.println(createExpertDeckPayload);

            String payload = String.valueOf(createExpertDeckPayload);
            StoreControllerRequestDTO = mapper.readValue(payload, StoreControllerRequestDTO.class);
            StoreControllerResponseDTO = createExpertDeck(headerAuthor, payload);

            Assert.assertEquals(StatusCode, 201, "Status Code");
            LOGGER.info("deck Id:" + StoreControllerResponseDTO.getId());
            Assert.assertEquals(StoreControllerResponseDTO.getBook().getBookIds()[0], "22847", "ExpertId");
            Assert.assertEquals(StoreControllerResponseDTO.getBook().getBookIds()[1], "123", "ExpertId");
            Assert.assertEquals(StoreControllerResponseDTO.getBook().getProductIds()[0], "b45141e1-330a-4d35-ac86-6071e98faa60", "ExpertId");

            DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    //verify whether an error message displayed when create a expert deck without book chapter
    @Test(priority = 19) @AlmAnnotation(almTestId = "xxxxxxxx")
    public void CreateExpertDeckWithoutBookChapterField() throws Exception {
        try {
            // Read json object from file
            JSONObject createExpertDeckPayload = jsonReader.getJsonObject(ExpertDeckCreatePayload);
            JSONArray bookIds = new JSONArray();
            bookIds.add("22847");
            bookIds.add("123");
            JSONArray productIds = new JSONArray();
            productIds.add("b45141e1-330a-4d35-ac86-6071e98faa60");
            JSONObject BookField = new JSONObject();
            BookField.put("bookTitle","Automation");
            BookField.put("bookIds",bookIds);
            BookField.put("bookAuthor","Automation author");
            BookField.put("chapterId","123");
            BookField.put("productIds",productIds);
            createExpertDeckPayload.put("book",BookField);
            System.out.println(createExpertDeckPayload);

            String payload = String.valueOf(createExpertDeckPayload);
            StoreControllerRequestDTO = mapper.readValue(payload, StoreControllerRequestDTO.class);
            StoreControllerResponseDTO = createExpertDeck(headerAuthor, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(StoreControllerResponseDTO.getFieldErrors()[0].getMessage(),"Book chapter should be present.");
            LOGGER.info("deck Id:" + StoreControllerResponseDTO.getId());

        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 20) @AlmAnnotation(almTestId = "xxxxxxxx")
    public void CreateExpertDeckWithoutBookIds_ProductIds() throws Exception {
        try {
            // Read json object from file
            JSONObject createExpertDeckPayload = jsonReader.getJsonObject(ExpertDeckCreatePayload);
            JSONArray bookIds = new JSONArray();
            bookIds.add("22847");
            bookIds.add("123");
            JSONArray productIds = new JSONArray();
            productIds.add("b45141e1-330a-4d35-ac86-6071e98faa60");
            JSONObject BookField = new JSONObject();
            BookField.put("bookTitle","Automation");
            BookField.put("chapter","1");
            BookField.put("bookAuthor","Automation author");
            BookField.put("chapterId","123");
            createExpertDeckPayload.put("book",BookField);
            System.out.println(createExpertDeckPayload);

            String payload = String.valueOf(createExpertDeckPayload);
            StoreControllerRequestDTO = mapper.readValue(payload, StoreControllerRequestDTO.class);
            StoreControllerResponseDTO = createExpertDeck(headerAuthor, payload);

            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(StoreControllerResponseDTO.getFieldErrors()[0].getMessage(),"'productId' or 'bookId' should be present.");
            LOGGER.info("deck Id:" + StoreControllerResponseDTO.getId());

        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 21) @AlmAnnotation(almTestId = "xxxxxxxx")
    public void CreateExpertDeckWithUsingNormalUser() throws Exception {
        try {
            // Read json object from file
            JSONObject createExpertDeckPayload = jsonReader.getJsonObject(ExpertDeckCreatePayload);
            System.out.println(createExpertDeckPayload);

            String payload = String.valueOf(createExpertDeckPayload);
            StoreControllerRequestDTO = mapper.readValue(payload, StoreControllerRequestDTO.class);
            StoreControllerResponseDTO = createExpertDeck(headerNormal, payload);

            Assert.assertEquals(StatusCode, 403, "Status Code");
            Assert.assertEquals(StoreControllerResponseDTO.getDescription(),"You do not have CREATE_EXPERT_DECK permission to access this method");

        } catch (Exception e) {
            throw e;
        }
    }
}
