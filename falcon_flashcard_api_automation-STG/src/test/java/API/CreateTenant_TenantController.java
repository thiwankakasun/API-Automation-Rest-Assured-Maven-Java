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
import requestDTO.TenantControllerRequestDTO;
import responseDTO.TenantControllerResponseDTO;
import utils.JsonReader;
import utils.PiTokenGenarator;
import utils.PiTokenGenaratorNormalUser;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

public class CreateTenant_TenantController extends ServiceController {

    //private String defaultContentType = "JSON";
    private TenantControllerResponseDTO TenantControllerResponseDTO;
    private TenantControllerRequestDTO TenantControllerRequestDTO;
    private TenantControllerResponseDTO[] TenantControllerDTO;
    private static final utils.PiTokenGenarator PiTokenGenarator = new PiTokenGenarator();
    private static final utils.PiTokenGenaratorNormalUser PiTokenGenaratorNormalUser = new PiTokenGenaratorNormalUser();
    private static final HashMap<String, String> header = new HashMap<>();
    private static final HashMap<String, String> headerNormal = new HashMap<>();
    //private Map<String, String> payload = new HashMap<>();
    JsonReader jsonReader = new JsonReader();

    private static final Logger LOGGER = Logger.getLogger(String.valueOf(CreateTenant_TenantController.class));

    @BeforeClass
    public void getData() throws EncryptedDocumentException, InvalidFormatException, IOException, ParseException {
        try {
            PiToken piToken = PiTokenGenarator.generatePiToken(loginUrl);
            header.put("X-Authorization", piToken.getData());
            PiToken piToken1 = PiTokenGenaratorNormalUser.generatePiToken(loginUrl);
            headerNormal.put("X-Authorization", piToken1.getData());
        } catch (Exception e) {
            throw e;
        }
    }

    //Verify that superuser can create tenant
    @Test(priority = 1) @AlmAnnotation(almTestId = "609861")
    public void CreateTenantWithFullPayload() throws Exception {
        try {
            // Read json object from file
            JSONObject createTenantPayload = jsonReader.getJsonObject(TenantCreatePayload);
            System.out.println(createTenantPayload);

            String payload = String.valueOf(createTenantPayload);
            TenantControllerRequestDTO = mapper.readValue(payload, TenantControllerRequestDTO.class);
            TenantControllerResponseDTO = createTenant(header, payload);

            Assert.assertEquals(StatusCode, 201, "Status Code");
            LOGGER.info("Tenant Id:" + TenantControllerResponseDTO.getTenantId());
        } catch (Exception e) {
            throw e;
        }
    }

    //verify whether tenants details can be retrieved by passing tenantId
    @Test(priority = 2) @AlmAnnotation(almTestId = "609180")
    public void GetTenantWithFullPayload() throws Exception {
        try {
            String TenantId = TenantControllerResponseDTO.getTenantId();
            TenantControllerResponseDTO = getTenant(header, TenantId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(TenantControllerResponseDTO.getTenantName(), TenantControllerRequestDTO.getTenantName(), "Tenant Name");
            Assert.assertEquals(TenantControllerResponseDTO.getTenantId(), TenantControllerRequestDTO.getTenantId(), "Tenant Id");
            Assert.assertEquals(TenantControllerResponseDTO.getTenantKeys()[0].getTenantKeyName(), TenantControllerRequestDTO.getTenantKeys()[0].getTenantKeyName(), "Tenant Key Name");
            Assert.assertEquals(TenantControllerResponseDTO.getTenantKeys()[0].getTenantKey(), TenantControllerRequestDTO.getTenantKeys()[0].getTenantKey(), "Tenant Key");
            Assert.assertEquals(TenantControllerResponseDTO.getMaxDeckLimit(), TenantControllerRequestDTO.getMaxDeckLimit(), "MaxDeckLimit");
            Assert.assertEquals(TenantControllerResponseDTO.getMaxQuestionLimit(), TenantControllerRequestDTO.getMaxQuestionLimit(), "MaxDeckLimit");
            Assert.assertTrue(TenantControllerResponseDTO.isValidateNotBlank(), "ValidateNotBlank");
            Assert.assertTrue(TenantControllerResponseDTO.isValidatePassport(), "ValidatePassport");
            Assert.assertTrue(TenantControllerResponseDTO.isShouldProvisionExpertDecks(), "ShouldProvisionExpertDecks");
            Assert.assertTrue(TenantControllerResponseDTO.isShouldRemoveExpertDecksUnpublished(), "ShouldRemoveExpertDecksUnpublished");
            if(Env.equals("PROD"))
                Assert.assertEquals(TenantControllerResponseDTO.getCreatorId(), "ffffffff6155466b50fbc268b6b735b2", "Creator ID");
            else
                Assert.assertEquals(TenantControllerResponseDTO.getCreatorId(), "a4f17ac5f02e494ebe21263069b9ba61", "Creator ID");
            Assert.assertNull(TenantControllerResponseDTO.getUpdaterId(), "Updater Id");
            //Assert.assertNotNull(TenantControllerResponseDTO.getCreatedAt(), "Created At");
            //Assert.assertNotNull(TenantControllerResponseDTO.getUpdatedAt(), "Updated At");
        } catch (Exception e) {
            throw e;
        }
    }

    //verify whether all edited tenants  can be retrieved by related to the user.
    @Test(priority = 3) @AlmAnnotation(almTestId = "611101")
    public void UpdateTenantWithFullPayload() throws Exception {
        try {

            JSONObject updateTenantPayload = jsonReader.getJsonObject(TenantUpdatePayload);
            System.out.println(updateTenantPayload);
            String payload = String.valueOf(updateTenantPayload);
            TenantControllerRequestDTO = mapper.readValue(payload, TenantControllerRequestDTO.class);

            String TenantId = TenantControllerResponseDTO.getTenantId();
            TenantControllerResponseDTO = updateTenant(header, payload, TenantId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(TenantControllerResponseDTO.getTenantName(), TenantControllerRequestDTO.getTenantName(), "Tenant Name");
            Assert.assertEquals(TenantControllerResponseDTO.getTenantId(), "51a6b312-5223-42c6-a103-def1a14a28c7", "Tenant Id");
            Assert.assertEquals(TenantControllerResponseDTO.getTenantKeys()[0].getTenantKeyName(), TenantControllerRequestDTO.getTenantKeys()[0].getTenantKeyName(), "Tenant Key Name");
            Assert.assertEquals(TenantControllerResponseDTO.getTenantKeys()[0].getTenantKey(), TenantControllerRequestDTO.getTenantKeys()[0].getTenantKey(), "Tenant Key");
            Assert.assertEquals(TenantControllerResponseDTO.getMaxDeckLimit(), TenantControllerRequestDTO.getMaxDeckLimit(), "MaxDeckLimit");
            Assert.assertEquals(TenantControllerResponseDTO.getMaxQuestionLimit(), TenantControllerRequestDTO.getMaxQuestionLimit(), "MaxDeckLimit");
            Assert.assertFalse(TenantControllerResponseDTO.isValidateNotBlank(), "ValidateNotBlank");
            Assert.assertFalse(TenantControllerResponseDTO.isValidatePassport(), "ValidatePassport");
            Assert.assertFalse(TenantControllerResponseDTO.isShouldProvisionExpertDecks(), "ShouldProvisionExpertDecks");
            Assert.assertFalse(TenantControllerResponseDTO.isShouldRemoveExpertDecksUnpublished(), "ShouldRemoveExpertDecksUnpublished");
            if(Env.equals("PROD"))
                Assert.assertEquals(TenantControllerResponseDTO.getCreatorId(), "ffffffff6155466b50fbc268b6b735b2", "Creator ID");
            else
                Assert.assertEquals(TenantControllerResponseDTO.getCreatorId(), "a4f17ac5f02e494ebe21263069b9ba61", "Creator ID");

            if(Env.equals("PROD"))
                Assert.assertEquals(TenantControllerResponseDTO.getUpdaterId(), "ffffffff6155466b50fbc268b6b735b2", "Creator ID");
            else
                Assert.assertEquals(TenantControllerResponseDTO.getUpdaterId(), "a4f17ac5f02e494ebe21263069b9ba61", "Creator ID");
            //Assert.assertNotNull(TenantControllerResponseDTO.getCreatedAt(), "Created At");
            //Assert.assertNotNull(TenantControllerResponseDTO.getUpdatedAt(), "Updated At");


        } catch (Exception e) {
            throw e;
        }
    }

    //verify whether edited tenant can be deleted
    @Test(priority = 4) @AlmAnnotation(almTestId = "611108")
    public void DeleteTenantWithFullPayload() throws Exception {

        try {
            String TenantId = TenantControllerResponseDTO.getTenantId();
            TenantControllerResponseDTO = deleteTenant(header, TenantId);

            Assert.assertEquals(StatusCode, 204, "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    //Verify that when the tenantName is not provided
    @Test(priority = 5) @AlmAnnotation(almTestId = "609949")
    public void CreateTenantWithoutTenantName() throws Exception {
        try {
            // Read json object from file
            JSONObject createTenantPayload = jsonReader.getJsonObject(TenantCreatePayload);

            createTenantPayload.put("tenantName", " ");
            String payload = String.valueOf(createTenantPayload);
            TenantControllerRequestDTO = mapper.readValue(payload, TenantControllerRequestDTO.class);

            TenantControllerResponseDTO = createTenant(header, payload);
            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(TenantControllerResponseDTO.getFieldErrors()[0].getField(),"tenantName");
            Assert.assertEquals(TenantControllerResponseDTO.getFieldErrors()[0].getMessage(), "must not be blank", "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 6) @AlmAnnotation(almTestId = "609946")
    public void CreateTenantWithoutTenantId() throws Exception {
        try {
            // Read json object from file
            JSONObject createTenantPayload = jsonReader.getJsonObject(TenantCreatePayload);

            createTenantPayload.put("tenantId", null);
            String payload = String.valueOf(createTenantPayload);
            TenantControllerRequestDTO = mapper.readValue(payload, TenantControllerRequestDTO.class);

            TenantControllerResponseDTO = createTenant(header, payload);
            Assert.assertEquals(StatusCode, 201, "Status Code");
            Assert.assertNotNull(TenantControllerResponseDTO.getTenantId(), "Tenant Id must be generated");
            LOGGER.info("Tenant Id " + TenantControllerResponseDTO.getTenantId());
            DeleteTenantWithFullPayload();
        } catch(Exception e) {
            throw e;
        }
    }

    //Verify whether system generate two tenant keys should be generated if the tenant keys are not provided for following tenantKeyNames-MOBILE, WEB
    @Test(priority = 7) @AlmAnnotation(almTestId = "609906")
    public void CreateTenantWithoutTenantKeys() throws Exception {
        try {
            // Read json object from file
            JSONObject createTenantPayload = jsonReader.getJsonObject(TenantCreatePayload);

            createTenantPayload.put("tenantKeys", null);
            String payload = String.valueOf(createTenantPayload);
            System.out.println(payload);
            TenantControllerRequestDTO = mapper.readValue(payload, TenantControllerRequestDTO.class);

            TenantControllerResponseDTO = createTenant(header, payload);
            Assert.assertEquals(StatusCode, 201, "Status Code");
            Assert.assertEquals(TenantControllerResponseDTO.getTenantKeys()[0].getTenantKeyName(), "MOBILE", "Tenant Key must be generated");
            Assert.assertNotNull(TenantControllerResponseDTO.getTenantKeys()[0], "Tenant Key must be generated");
            Assert.assertEquals(TenantControllerResponseDTO.getTenantKeys()[1].getTenantKeyName(), "WEB", "Tenant Key must be generated");
            Assert.assertNotNull(TenantControllerResponseDTO.getTenantKeys()[1], "Tenant Key must be generated");
            //LOGGER.info("Tenant Id "+TenantControllerResponseDTO.getTenantKeys()[1]);
            DeleteTenantWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 8) @AlmAnnotation(almTestId = "609906")
    public void CreateTenantWithoutTenantKeyAndTenantKeyName() throws Exception {
        try {
            // Read json object from file
            JSONObject createTenantPayload = jsonReader.getJsonObject(TenantCreatePayload);

            JSONObject tenantKeyField = new JSONObject();
            tenantKeyField.put("tenantKey",null);
            tenantKeyField.put("tenantKeyName",null);

            JSONArray tenantKey = new JSONArray();
            tenantKey.add(tenantKeyField);
            createTenantPayload.put("tenantKeys",tenantKey);
            String payload = String.valueOf(createTenantPayload);
            System.out.println(payload);
            TenantControllerRequestDTO = mapper.readValue(payload, TenantControllerRequestDTO.class);

            TenantControllerResponseDTO = createTenant(header, payload);
            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(TenantControllerResponseDTO.getFieldErrors()[0].getMessage(),"must not be blank", "Tenant Key must be generated");
            Assert.assertEquals(TenantControllerResponseDTO.getFieldErrors()[1].getMessage(),"must not be blank", "Tenant Key must be generated");
        } catch (Exception e) {
            throw e;
        }
    }

    //Verify that Provided Tenant-Id already exists error message using already exist tenant Id
    @Test(priority = 9) @AlmAnnotation(almTestId = "609885")
    public void CreateTenantWithExistingTenantId() throws Exception {
        try {
            // Read json object from file
            JSONObject createTenantPayload = jsonReader.getJsonObject(TenantCreatePayload);
            String payload = String.valueOf(createTenantPayload);
            TenantControllerRequestDTO = mapper.readValue(payload, TenantControllerRequestDTO.class);

            TenantControllerResponseDTO = createTenant(header, payload);
            Assert.assertEquals(StatusCode, 201, "Status Code");
            LOGGER.info("Tenant Id:" + TenantControllerResponseDTO.getTenantId());
            String TenantId = TenantControllerResponseDTO.getTenantId();

            TenantControllerResponseDTO = createTenant(header, payload);
            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(TenantControllerResponseDTO.getDescription(), "Provided Tenant-Id already exists", "Status Code");

            TenantControllerResponseDTO = deleteTenant(header, TenantId);
            Assert.assertEquals(StatusCode, 204, "Status Code");

        }catch (Exception e){
            throw e;
        }
    }

    //Verify whether send POST request with incorrect userId
    @Test(priority = 10) @AlmAnnotation(almTestId = "609931")
    public void CreateTenantWithInvalidUserId() throws Exception {
        try {
            // Read json object from file
            JSONObject createTenantPayload = jsonReader.getJsonObject(TenantCreatePayload);

            createTenantPayload.put("creatorId", "1234");
            String payload = String.valueOf(createTenantPayload);
            System.out.println(payload);
            TenantControllerRequestDTO = mapper.readValue(payload, TenantControllerRequestDTO.class);

            TenantControllerResponseDTO = createTenant(header, payload);
            Assert.assertEquals(StatusCode, 201, "Status Code");

            DeleteTenantWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    //Verify whether default values for maxDeckLimit, maxQuestionLimit, validateNotBlank , shouldProvisionExpertDecks
    @Test(priority = 11) @AlmAnnotation(almTestId = "610317")
    public void CreateTenantWithoutBooleanFields() throws Exception {
        try {
            // Read json object from file
            JSONObject createTenantPayload = jsonReader.getJsonObject(TenantCreatePayload);
            createTenantPayload.put("maxDeckLimit", null);
            createTenantPayload.put("maxQuestionLimit", null);
            createTenantPayload.put("validateNotBlank", null);
            createTenantPayload.put("validatePassport", null);
            createTenantPayload.put("shouldProvisionExpertDecks", null);
            createTenantPayload.put("shouldRemoveExpertDecksUnpublished", null);
            String payload = String.valueOf(createTenantPayload);
            System.out.println(payload);
            TenantControllerRequestDTO = mapper.readValue(payload, TenantControllerRequestDTO.class);

            TenantControllerResponseDTO = createTenant(header, payload);
            Assert.assertEquals(StatusCode, 201, "Status Code");
            Assert.assertEquals(TenantControllerResponseDTO.getMaxDeckLimit(),0);
            Assert.assertEquals(TenantControllerResponseDTO.getMaxQuestionLimit(),0);
            Assert.assertFalse(TenantControllerResponseDTO.isValidateNotBlank(), "ValidateNotBlank");
            Assert.assertFalse(TenantControllerResponseDTO.isValidatePassport(), "ValidatePassport");
            Assert.assertFalse(TenantControllerResponseDTO.isShouldProvisionExpertDecks(), "ShouldProvisionExpertDecks");
            Assert.assertFalse(TenantControllerResponseDTO.isShouldRemoveExpertDecksUnpublished(), "ShouldRemoveExpertDecksUnpublished");

            DeleteTenantWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    //Verify that whether all fields are included and correct in the response body
    @Test(priority = 12) @AlmAnnotation(almTestId = "609921")
    public void CreateTenantWithValidateAllFields() throws Exception {
        try {
            getData();
            // Read json object from file
            JSONObject createTenantPayload = jsonReader.getJsonObject(TenantCreatePayload);

            String payload = String.valueOf(createTenantPayload);
            System.out.println(payload);
            TenantControllerRequestDTO = mapper.readValue(payload, TenantControllerRequestDTO.class);

            TenantControllerResponseDTO = createTenant(header, payload);
            Assert.assertEquals(StatusCode, 201, "Status Code");
            Assert.assertEquals(TenantControllerResponseDTO.getTenantName(), TenantControllerRequestDTO.getTenantName(), "Tenant Name");
            Assert.assertEquals(TenantControllerResponseDTO.getTenantId(), TenantControllerRequestDTO.getTenantId(), "Tenant Id");
            Assert.assertEquals(TenantControllerResponseDTO.getTenantKeys()[0].getTenantKeyName(), TenantControllerRequestDTO.getTenantKeys()[0].getTenantKeyName(), "Tenant Key Name");
            Assert.assertEquals(TenantControllerResponseDTO.getTenantKeys()[0].getTenantKey(), TenantControllerRequestDTO.getTenantKeys()[0].getTenantKey(), "Tenant Key");
            Assert.assertEquals(TenantControllerResponseDTO.getMaxDeckLimit(), TenantControllerRequestDTO.getMaxDeckLimit(), "MaxDeckLimit");
            Assert.assertEquals(TenantControllerResponseDTO.getMaxQuestionLimit(), TenantControllerRequestDTO.getMaxQuestionLimit(), "MaxDeckLimit");
            Assert.assertTrue(TenantControllerResponseDTO.isValidateNotBlank(), "ValidateNotBlank");
            Assert.assertTrue(TenantControllerResponseDTO.isValidatePassport(), "ValidatePassport");
            Assert.assertTrue(TenantControllerResponseDTO.isShouldProvisionExpertDecks(), "ShouldProvisionExpertDecks");
            Assert.assertTrue(TenantControllerResponseDTO.isShouldRemoveExpertDecksUnpublished(), "ShouldRemoveExpertDecksUnpublished");
            if(Env.equals("PROD"))
                Assert.assertEquals(TenantControllerResponseDTO.getCreatorId(), "ffffffff6155466b50fbc268b6b735b2", "Creator ID");
            else
                Assert.assertEquals(TenantControllerResponseDTO.getCreatorId(), "a4f17ac5f02e494ebe21263069b9ba61", "Creator ID");
            Assert.assertNull(TenantControllerResponseDTO.getUpdaterId(), "Updater Id");
            //Assert.assertNotNull(TenantControllerResponseDTO.getCreatedAt(), "Created At");
            //Assert.assertNotNull(TenantControllerResponseDTO.getUpdatedAt(), "Updated At");

            DeleteTenantWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    //Verify that When tenantId is not a valid UUID
    @Test(priority = 13) @AlmAnnotation(almTestId = "609948")
    public void CreateTenantWithInvalidFormatofTenantId() throws Exception {
        try {
            // Read json object from file
            JSONObject createTenantPayload = jsonReader.getJsonObject(TenantCreatePayload);

            createTenantPayload.put("tenantId", "2b4af3fc-97b4-41a3-af7e-bc9354d35fe");
            String payload = String.valueOf(createTenantPayload);
            System.out.println(payload);
            TenantControllerRequestDTO = mapper.readValue(payload, TenantControllerRequestDTO.class);

            TenantControllerResponseDTO = createTenant(header, payload);
            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(TenantControllerResponseDTO.getFieldErrors()[0].getField(),"tenantId");
            Assert.assertEquals(TenantControllerResponseDTO.getFieldErrors()[0].getMessage(), "Provided UUID is not valid");
        } catch (Exception e) {
            throw e;
        }
    }

    //Verify that When tenantKey is not a valid UUID
    @Test(priority = 14) @AlmAnnotation(almTestId = "609952")
    public void CreateTenantWithInvalidFormatofTenantKey() throws Exception {
        try {
            // Read json object from file
            JSONObject createTenantPayload = jsonReader.getJsonObject(TenantCreatePayload);

            JSONObject tenantKeyField = new JSONObject();
            tenantKeyField.put("tenantKey","2b4af3fc-97b4-41a3-af7e-bc9354d35fe");
            tenantKeyField.put("tenantKeyName","ET2_WEB");

            JSONArray tenantKey = new JSONArray();
            tenantKey.add(tenantKeyField);
            createTenantPayload.put("tenantKeys",tenantKey);
            String payload = String.valueOf(createTenantPayload);
            System.out.println(payload);
            TenantControllerRequestDTO = mapper.readValue(payload, TenantControllerRequestDTO.class);

            TenantControllerResponseDTO = createTenant(header, payload);
            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(TenantControllerResponseDTO.getFieldErrors()[0].getField(),"tenantKeys[0].tenantKey");
            Assert.assertEquals(TenantControllerResponseDTO.getFieldErrors()[0].getMessage(), "Provided UUID is not valid");
        } catch (Exception e) {
            throw e;
        }
    }

    //Verify that when tenantKeyName is provided, but the tenantKey is not provided
    @Test(priority = 15) @AlmAnnotation(almTestId = "609946")
    public void CreateTenantWithoutTenantKeyandWithTenantKeyName() throws Exception {
        try {
            // Read json object from file
            JSONObject createTenantPayload = jsonReader.getJsonObject(TenantCreatePayload);

            JSONObject tenantKeyField = new JSONObject();
            tenantKeyField.put("tenantKey",null);
            tenantKeyField.put("tenantKeyName","ET2_WEB");

            JSONArray tenantKey = new JSONArray();
            tenantKey.add(tenantKeyField);
            createTenantPayload.put("tenantKeys",tenantKey);
            String payload = String.valueOf(createTenantPayload);
            System.out.println(payload);
            TenantControllerRequestDTO = mapper.readValue(payload, TenantControllerRequestDTO.class);

            TenantControllerResponseDTO = createTenant(header, payload);
            Assert.assertEquals(StatusCode, 400, "Status Code");
            Assert.assertEquals(TenantControllerResponseDTO.getFieldErrors()[0].getField(),"tenantKeys[0].tenantKey");
            Assert.assertEquals(TenantControllerResponseDTO.getFieldErrors()[0].getMessage(), "must not be blank");
        } catch (Exception e) {
            throw e;
        }
    }

    //Verify that Unauthorized Request error message using invalid PI token
    @Test(priority = 16) @AlmAnnotation(almTestId = "609862")
    public void CreateTenantWithUnauthorizedError() throws Exception {
        try {
            // Read json object from file
            JSONObject createTenantPayload = jsonReader.getJsonObject(TenantCreatePayload);

            String payload = String.valueOf(createTenantPayload);
            System.out.println(payload);
            TenantControllerRequestDTO = mapper.readValue(payload, TenantControllerRequestDTO.class);

            header.put("X-Authorization","abc");
            TenantControllerResponseDTO = createTenant(header, payload);
            Assert.assertEquals(StatusCode, 401, "Status Code");
            Assert.assertEquals(TenantControllerResponseDTO.getError(), "Unauthorized");
        } catch (Exception e) {
            throw e;
        }
    }

    //Verify that 403 Forbidden error message using access denied PI token
    @Test(priority = 17) @AlmAnnotation(almTestId = "609926")
    public void CreateTenantWith403Error() throws Exception {
        try {
            // Read json object from file
            JSONObject createTenantPayload = jsonReader.getJsonObject(TenantCreatePayload);

            String payload = String.valueOf(createTenantPayload);
            System.out.println(payload);
            TenantControllerRequestDTO = mapper.readValue(payload, TenantControllerRequestDTO.class);

            TenantControllerResponseDTO = createTenant(headerNormal, payload);
            Assert.assertEquals(StatusCode, 403, "Status Code");
            Assert.assertEquals(TenantControllerResponseDTO.getDescription(), "You do not have CREATE_TENANT permission to access this method");
        } catch (Exception e) {
            throw e;
        }
    }
}
