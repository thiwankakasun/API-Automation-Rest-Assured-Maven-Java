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

public class TenantController extends ServiceController {

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

    private static final Logger LOGGER = Logger.getLogger(String.valueOf(TenantController.class));

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
            getData();
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

    //verify whether creator Id is displayed  when passing valid tenantId
    @Test(priority = 18) @AlmAnnotation(almTestId = "609187")
    public void GetTenantWithValidateCreatorId() throws Exception {
        try {
            CreateTenantWithFullPayload();

            String TenantId = TenantControllerResponseDTO.getTenantId();
            TenantControllerResponseDTO = getTenant(header, TenantId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            if(Env.equals("PROD"))
                Assert.assertEquals(TenantControllerResponseDTO.getCreatorId(), "ffffffff6155466b50fbc268b6b735b2", "Creator ID");
            else
                Assert.assertEquals(TenantControllerResponseDTO.getCreatorId(), "a4f17ac5f02e494ebe21263069b9ba61", "Creator ID");
            DeleteTenantWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    //verify whether all tenant keys and tenant key names displayed when passing a correct tenant ID
    @Test(priority = 19) @AlmAnnotation(almTestId = "609186")
    public void GetTenantWithValidateTenantKeys() throws Exception {
        try {
            CreateTenantWithFullPayload();

            String TenantId = TenantControllerResponseDTO.getTenantId();
            TenantControllerResponseDTO = getTenant(header, TenantId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(TenantControllerResponseDTO.getTenantKeys()[0].getTenantKey(),TenantControllerRequestDTO.getTenantKeys()[0].getTenantKey(),"Tenant Key must be equal");
            Assert.assertEquals(TenantControllerResponseDTO.getTenantKeys()[0].getTenantKeyName(),TenantControllerRequestDTO.getTenantKeys()[0].getTenantKeyName(),"Tenant Key name must be equal");

            DeleteTenantWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    //retrieve tenant details  by passing wrong tenantId
    @Test(priority = 20) @AlmAnnotation(almTestId = "609181")
    public void GetTenantWithInvalidTenantId() throws Exception {
        try {

//            JSONObject createTenantPayload = jsonReader.getJsonObject(TenantCreatePayload);
//            String payload = String.valueOf(createTenantPayload);
//            TenantControllerResponseDTO = mapper.readValue(payload, TenantControllerResponseDTO.class);
//            TenantControllerResponseDTO.setTenantId("51a6b312-5223-42c6-a103-def1a14a28c7");
//            String TenantId = TenantControllerResponseDTO.getTenantId();

            String TenantId = "51a6b312-5223-42c6-a103";
            TenantControllerResponseDTO = getTenant(header, TenantId);

            Assert.assertEquals(StatusCode, 404, "Status Code");
            Assert.assertEquals(TenantControllerResponseDTO.getDescription(),"Provided Tenant-Id does not exists");

        } catch (Exception e) {
            throw e;
        }
    }

    //retrieve tenant details  by passing invalid tenantId with numbers only and characters only.
    @Test(priority = 21) @AlmAnnotation(almTestId = "609183")
    public void GetTenantWithOnlyStringForTenantId() throws Exception {
        try {
            String TenantId = "aaaa";
            TenantControllerResponseDTO = getTenant(header, TenantId);

            Assert.assertEquals(StatusCode, 404, "Status Code");
            Assert.assertEquals(TenantControllerResponseDTO.getDescription(),"Provided Tenant-Id does not exists");

        } catch (Exception e) {
            throw e;
        }
    }

    //tenant details  by passing deleted tenantId
    @Test(priority = 22) @AlmAnnotation(almTestId = "609182")
    public void GetTenantWithDeletedTenantId() throws Exception {
        try {
            CreateTenantWithFullPayload();
            GetTenantWithFullPayload();
            String TenantId = TenantControllerResponseDTO.getTenantId();
            DeleteTenantWithFullPayload();
            TenantControllerResponseDTO = getTenant(header, TenantId);
            Assert.assertEquals(StatusCode, 404, "Status Code");
            Assert.assertEquals(TenantControllerResponseDTO.getDescription(),"Provided Tenant-Id does not exists");

        } catch (Exception e) {
            throw e;
        }
    }

    //retrieved by passing updated tenantId
    @Test(priority = 23) @AlmAnnotation(almTestId = "609189")
    public void GetTenantWithUpdatedTenantId() throws Exception {
        try {
            CreateTenantWithFullPayload();
            Thread.sleep(10000);
            GetTenantWithFullPayload();
            UpdateTenantWithFullPayload();

            String TenantId = TenantControllerResponseDTO.getTenantId();
            TenantControllerResponseDTO = getTenant(header, TenantId);
            Assert.assertEquals(StatusCode, 200, "Status Code");
            if(Env.equals("PROD"))
                Assert.assertEquals(TenantControllerResponseDTO.getUpdaterId(), "ffffffff6155466b50fbc268b6b735b2", "Creator ID");
            else
                Assert.assertEquals(TenantControllerResponseDTO.getUpdaterId(), "a4f17ac5f02e494ebe21263069b9ba61", "Creator ID");

            DeleteTenantWithFullPayload();

        } catch (Exception e) {
            throw e;
        }
    }

    //verify whether tenant name is displayed as E-text when passing E-text tenantId
    @Test(priority = 24) @AlmAnnotation(almTestId = "609184")
    public void GetTenantWithETextTenantId() throws Exception {
        try {
            String TenantId = null;
            if(Env.equals("DEV")){TenantId= "2b4af3fc-97b4-41a3-af7e-bc9354d35fed";}
            if(Env.equals("QA")){TenantId= "2b4af3fc-97b4-41a3-af7e-bc9354d35fed";}
            if(Env.equals("NFT")){TenantId= "beeea450-02f6-4f88-842e-cec9364453b7";}
            if(Env.equals("STG")){TenantId= "30258aed-46bb-4ec5-909e-ecea2431772b";}
            if(Env.equals("PROD")){TenantId= "beeea450-02f6-4f88-842e-cec9364453b7";}
            TenantControllerResponseDTO = getTenant(header, TenantId);
            Assert.assertEquals(StatusCode, 200, "Status Code");
            if(Env.equals("DEV")){Assert.assertEquals(TenantControllerResponseDTO.getTenantId(),"2b4af3fc-97b4-41a3-af7e-bc9354d35fed");}
            if(Env.equals("QA")){Assert.assertEquals(TenantControllerResponseDTO.getTenantId(),"2b4af3fc-97b4-41a3-af7e-bc9354d35fed");}
            if(Env.equals("STG")){Assert.assertEquals(TenantControllerResponseDTO.getTenantId(),"30258aed-46bb-4ec5-909e-ecea2431772b");}

        } catch (Exception e) {
            throw e;
        }
    }

    //verify whether tenant name is displayed as pearson prep when passing pearson prep tenantId
    @Test(priority = 25) @AlmAnnotation(almTestId = "609185")
    public void GetTenantWithPrepTenantId() throws Exception {
        try {
            String TenantId = null;
            if(Env.equals("DEV")){TenantId= "2b4af3fc-97b4-41a3-af7e-bc9354d35fed";}
            if(Env.equals("QA")){TenantId= "5660deb9-7b3b-44e8-8a6c-4b1fdae17308";}
            if(Env.equals("NFT")){TenantId= "05d8aa57-c407-483b-ad3c-3c8f8d8c116f";}
            if(Env.equals("STG")){TenantId= "05d8aa57-c407-483b-ad3c-3c8f8d8c116f";}
            if(Env.equals("PROD")){TenantId= "ff74661e-8585-4624-b512-be689fc820ce";}
            TenantControllerResponseDTO = getTenant(header, TenantId);
            Assert.assertEquals(StatusCode, 200, "Status Code");
            if(Env.equals("DEV")){Assert.assertEquals(TenantControllerResponseDTO.getTenantId(), "2b4af3fc-97b4-41a3-af7e-bc9354d35fed");}
            if(Env.equals("QA")){Assert.assertEquals(TenantControllerResponseDTO.getTenantId(), "5660deb9-7b3b-44e8-8a6c-4b1fdae17308");}
            if(Env.equals("STG")){Assert.assertEquals(TenantControllerResponseDTO.getTenantId(), "05d8aa57-c407-483b-ad3c-3c8f8d8c116f");}

        } catch (Exception e) {
            throw e;
        }
    }

    //retrieve tenant details  by passing valid tenantId and invalid X-Authorization key
    @Test(priority = 26) @AlmAnnotation(almTestId = "609188")
    public void GetTenantWithUnauthorizedError() throws Exception {
        try {
            String TenantId = "aaaa";
            header.put("X-Authorization","abc");
            TenantControllerResponseDTO = getTenant(header, TenantId);
            Assert.assertEquals(StatusCode, 401, "Status Code");
            Assert.assertEquals(TenantControllerResponseDTO.getError(),"Unauthorized");

        } catch (Exception e) {
            throw e;
        }
    }

    //Verify whether that only superusers can retrieve the tenant details
    @Test(priority = 27) @AlmAnnotation(almTestId = "644990")
    public void GetTenantWith403Error() throws Exception {
        try {
            String TenantId = null;
            if(Env.equals("DEV")){TenantId= "2b4af3fc-97b4-41a3-af7e-bc9354d35fed";}
            if(Env.equals("QA")){TenantId= "2b4af3fc-97b4-41a3-af7e-bc9354d35fed";}
            if(Env.equals("NFT")){TenantId= "beeea450-02f6-4f88-842e-cec9364453b7";}
            if(Env.equals("STG")){TenantId= "30258aed-46bb-4ec5-909e-ecea2431772b";}
            if(Env.equals("PROD")){TenantId= "beeea450-02f6-4f88-842e-cec9364453b7";}
            TenantControllerResponseDTO = getTenant(headerNormal, TenantId);

            Assert.assertEquals(StatusCode, 403, "Status Code");
            Assert.assertEquals(TenantControllerResponseDTO.getDescription(),"You do not have GET_A_TENANT permission to access this method");
        } catch (Exception e) {
            throw e;
        }
    }

    //verify whether all tenants  can be retrieved by passing request to get all tenant.
    @Test(priority = 28) @AlmAnnotation(almTestId = "609308")
    public void GetAllTenantWithFullPayload() throws Exception {
        try {
            getData();
            TenantControllerDTO = getAllTenant(header);

            Assert.assertEquals(StatusCode, 200, "Status Code");
        } catch (Exception e) {
            throw e;
        }
    }

    //verify tenant name, tenants key name and creatorId when retrieve all tenants
    @Test(priority = 29) @AlmAnnotation(almTestId = "609320")
    public void GetAllTenantWithValidatingAllFields() throws Exception {
        try {
            TenantControllerDTO = getAllTenant(header);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertNotNull(TenantControllerDTO[0].getTenantName(), "Tenant Name");
            Assert.assertNotNull(TenantControllerDTO[0].getTenantId(), "Tenant Id");
            Assert.assertNotNull(TenantControllerDTO[0].getCreatorId(),  "CreatorId");
            Assert.assertNotNull(TenantControllerDTO[0].getTenantKeys()[0].getTenantKeyName(),  "Tenant Key Name");
            Assert.assertNotNull(TenantControllerDTO[1].getTenantName(), "Tenant Name");
            Assert.assertNotNull(TenantControllerDTO[1].getTenantId(), "Tenant Id");
            Assert.assertNotNull(TenantControllerDTO[1].getCreatorId(),  "CreatorId");
            Assert.assertNotNull(TenantControllerDTO[1].getTenantKeys()[0].getTenantKeyName(),  "Tenant Key Name");

        } catch (Exception e) {
            throw e;
        }
    }

    //verify whether all tenants  can be retrieved by related to the user.
    @Test(priority = 30) @AlmAnnotation(almTestId = "609315")
    public void GetAllTenantWithValidatingRetrievedDataRelatedtotheUser() throws Exception {
        try {
            TenantControllerDTO = getAllTenant(header);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(TenantControllerDTO[0].getCreatorId(), TenantControllerDTO[1].getCreatorId(),"Tenant Name");
            Assert.assertEquals(TenantControllerDTO[1].getCreatorId(), TenantControllerDTO[2].getCreatorId(),"Tenant Id");
            Assert.assertEquals(TenantControllerDTO[2].getCreatorId(),  TenantControllerDTO[3].getCreatorId(),"CreatorId");

        } catch (Exception e) {
            throw e;
        }
    }

//    @Test(priority = 2) @AlmAnnotation(almTestId = "609315")
//    public void GetAllTenantWithValidatingRetrievalUpdatedTenants() throws Exception {
//        try {
//            CreateTenantWithFullPayload();
//            UpdateTenantWithFullPayload();
//            TenantControllerDTO = getAllTenant(header);
//
//            Assert.assertEquals(StatusCode, 200, "Status Code");
//            for (int i=0; i<500; i++) {
//                if (TenantControllerDTO[i].getTenantId().equals("51a6b312-5223-42c6-a103-def1a14a28c7")){
//                    Assert.assertEquals(TenantControllerDTO[i].getTenantId(), "51a6b312-5223-42c6-a103-def1a14a28c7", "Tenant Id");
//                    Assert.assertEquals(TenantControllerDTO[i].getTenantName(), TenantControllerResponseDTO.getTenantName(), "Tenant Name");
//            }}
//            DeleteTenantWithFullPayload();
////            Assert.assertEquals(TenantControllerResponseDTO.getTenantKeys()[0].getTenantKey(), TenantControllerRequestDTO.getTenantKeys()[0].getTenantKey(), "Tenant Key");
////            Assert.assertEquals(TenantControllerResponseDTO.getMaxDeckLimit(), TenantControllerRequestDTO.getMaxDeckLimit(), "MaxDeckLimit");
////            Assert.assertEquals(TenantControllerResponseDTO.getMaxQuestionLimit(), TenantControllerRequestDTO.getMaxQuestionLimit(), "MaxDeckLimit");
////            Assert.assertTrue(TenantControllerResponseDTO.isValidateNotBlank(), "ValidateNotBlank");
////            Assert.assertTrue(TenantControllerResponseDTO.isValidatePassport(), "ValidatePassport");
////            Assert.assertTrue(TenantControllerResponseDTO.isShouldProvisionExpertDecks(), "ShouldProvisionExpertDecks");
////            Assert.assertTrue(TenantControllerResponseDTO.isShouldRemoveExpertDecksUnpublished(), "ShouldRemoveExpertDecksUnpublished");
////            if(Env.equals("PROD"))
////                Assert.assertEquals(TenantControllerResponseDTO.getCreatorId(), "ffffffff6155466b50fbc268b6b735b2", "Creator ID");
////            else
////                Assert.assertEquals(TenantControllerResponseDTO.getCreatorId(), "a4f17ac5f02e494ebe21263069b9ba61", "Creator ID");
////            Assert.assertNull(TenantControllerResponseDTO.getUpdaterId(), "Updater Id");
////            Assert.assertNotNull(TenantControllerResponseDTO.getCreatedAt(), "Created At");
////            Assert.assertNotNull(TenantControllerResponseDTO.getUpdatedAt(), "Updated At");
//        } catch (Exception e) {
//            throw e;
//        }
//    }

    //retrieve tenants with invalid authorization
    @Test(priority = 31) @AlmAnnotation(almTestId = "609319")
    public void GetAllTenantWith401UnauthorizedError() throws Exception {
        try {
            header.put("X-Authorization","abc");
            TenantControllerResponseDTO = getAllTenantValidate(header);

            Assert.assertEquals(StatusCode, 401, "Status Code");
            Assert.assertEquals(TenantControllerResponseDTO.getError(), "Unauthorized", "Tenant Name");

        } catch (Exception e) {
            throw e;
        }
    }

    //Verify whether that only superusers can retrieve the tenant details
    @Test(priority = 32) @AlmAnnotation(almTestId = "609316")
    public void GetAllTenantWith403Error() throws Exception {
        try {
            TenantControllerResponseDTO = getAllTenantValidate(headerNormal);

            Assert.assertEquals(StatusCode, 403, "Status Code");
            Assert.assertEquals(TenantControllerResponseDTO.getDescription(),"You do not have GET_ALL_TENANTS permission to access this method");

        } catch (Exception e) {
            throw e;
        }
    }

    //verify whether tenant key can be edited by using given tenant ID.
    @Test(priority = 33) @AlmAnnotation(almTestId = "611099")
    public void UpdateTenantKey() throws Exception {
        try {
            getData();
            CreateTenantWithFullPayload();
            JSONObject updateTenantPayload = jsonReader.getJsonObject(TenantUpdatePayload);
            String payload = String.valueOf(updateTenantPayload);
            TenantControllerRequestDTO = mapper.readValue(payload, TenantControllerRequestDTO.class);

            String TenantId = TenantControllerResponseDTO.getTenantId();
            TenantControllerResponseDTO = updateTenant(header, payload, TenantId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(TenantControllerResponseDTO.getTenantKeys()[0].getTenantKey(), TenantControllerRequestDTO.getTenantKeys()[0].getTenantKey(), "Tenant Key");
            DeleteTenantWithFullPayload();

        } catch (Exception e) {
            throw e;
        }
    }

    //verify whether tenant key name can be edited by given tenant ID.
    @Test(priority = 34) @AlmAnnotation(almTestId = "611100")
    public void UpdateTenantKeyName() throws Exception {
        try {
            CreateTenantWithFullPayload();
            JSONObject updateTenantPayload = jsonReader.getJsonObject(TenantUpdatePayload);
            String payload = String.valueOf(updateTenantPayload);
            TenantControllerRequestDTO = mapper.readValue(payload, TenantControllerRequestDTO.class);

            String TenantId = TenantControllerResponseDTO.getTenantId();
            TenantControllerResponseDTO = updateTenant(header, payload, TenantId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(TenantControllerResponseDTO.getTenantKeys()[0].getTenantKeyName(), TenantControllerRequestDTO.getTenantKeys()[0].getTenantKeyName(), "Tenant Key");
            DeleteTenantWithFullPayload();

        } catch (Exception e) {
            throw e;
        }
    }

    //verify whether tenant name can be edited by given tenant ID.
    @Test(priority = 35) @AlmAnnotation(almTestId = "611098")
    public void UpdateTenantName() throws Exception {
        try {

            CreateTenantWithFullPayload();
            JSONObject updateTenantPayload = jsonReader.getJsonObject(TenantUpdatePayload);
            String payload = String.valueOf(updateTenantPayload);
            TenantControllerRequestDTO = mapper.readValue(payload, TenantControllerRequestDTO.class);

            String TenantId = TenantControllerResponseDTO.getTenantId();
            TenantControllerResponseDTO = updateTenant(header, payload, TenantId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            Assert.assertEquals(TenantControllerResponseDTO.getTenantName(), TenantControllerRequestDTO.getTenantName(), "Tenant Key");
            DeleteTenantWithFullPayload();

        } catch (Exception e) {
            throw e;
        }
    }

    //verify whether Creator ID is displayed and should not be changed when edit the tenant.
    @Test(priority = 36) @AlmAnnotation(almTestId = "611104")
    public void UpdateTenantWithSameCreatorId() throws Exception {
        try {

            CreateTenantWithFullPayload();
            JSONObject updateTenantPayload = jsonReader.getJsonObject(TenantUpdatePayload);
            String payload = String.valueOf(updateTenantPayload);
            TenantControllerRequestDTO = mapper.readValue(payload, TenantControllerRequestDTO.class);

            String TenantId = TenantControllerResponseDTO.getTenantId();
            TenantControllerResponseDTO = updateTenant(header, payload, TenantId);

            Assert.assertEquals(StatusCode, 200, "Status Code");
            if(Env.equals("PROD"))
                Assert.assertEquals(TenantControllerResponseDTO.getCreatorId(), "ffffffff6155466b50fbc268b6b735b2", "Creator ID");
            else
                Assert.assertEquals(TenantControllerResponseDTO.getCreatorId(), "a4f17ac5f02e494ebe21263069b9ba61", "Creator ID");

            if(Env.equals("PROD"))
                Assert.assertEquals(TenantControllerResponseDTO.getUpdaterId(), "ffffffff6155466b50fbc268b6b735b2", "Creator ID");
            else
                Assert.assertEquals(TenantControllerResponseDTO.getUpdaterId(), "a4f17ac5f02e494ebe21263069b9ba61", "Creator ID");
            DeleteTenantWithFullPayload();

        } catch (Exception e) {
            throw e;
        }
    }

    //verify whether error response is displayed edit with empty tenantId
    @Test(priority = 37) @AlmAnnotation(almTestId = "611102")
    public void UpdateTenantWithInvalidTenantId() throws Exception {
        try {

            JSONObject updateTenantPayload = jsonReader.getJsonObject(TenantUpdatePayload);
            String payload = String.valueOf(updateTenantPayload);

            String TenantId = "123";
            TenantControllerResponseDTO = updateTenant(header, payload, TenantId);

            Assert.assertEquals(StatusCode, 404, "Status Code");
            Assert.assertEquals(TenantControllerResponseDTO.getDescription(), "Provided Tenant-Id does not exists", "Tenant Key");
            //Assert.assertEquals(TenantControllerResponseDTO.getUpdaterId(),"a4f17ac5f02e494ebe21263069b9ba61","Updater Id");

        } catch (Exception e) {
            throw e;
        }
    }

    //verify whether edit permission have only specific user. Should not be edited any user.
    @Test(priority = 38) @AlmAnnotation(almTestId = "611105")
    public void UpdateTenantWith403Error() throws Exception {
        try {

            JSONObject updateTenantPayload = jsonReader.getJsonObject(TenantUpdatePayload);
            String payload = String.valueOf(updateTenantPayload);

            String TenantId = "51a6b312-5223-42c6-a103-def1a14a28c7";
            TenantControllerResponseDTO = updateTenant(headerNormal, payload, TenantId);

            Assert.assertEquals(StatusCode, 403, "Status Code");
            Assert.assertEquals(TenantControllerResponseDTO.getDescription(), "You do not have UPDATE_TENANT permission to access this method", "Tenant Key");
            //Assert.assertEquals(TenantControllerResponseDTO.getUpdaterId(),"a4f17ac5f02e494ebe21263069b9ba61","Updater Id");

        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority = 39) @AlmAnnotation(almTestId = "611103")
    public void UpdateTenantWithUnauthorizedRequest() throws Exception {
        try {

            JSONObject updateTenantPayload = jsonReader.getJsonObject(TenantUpdatePayload);
            String payload = String.valueOf(updateTenantPayload);

            String TenantId = "51a6b312-5223-42c6-a103-def1a14a28c7";
            header.put("X-Authorization","abc");
            TenantControllerResponseDTO = updateTenant(header, payload, TenantId);

            Assert.assertEquals(StatusCode, 401, "Status Code");
            Assert.assertEquals(TenantControllerResponseDTO.getError(), "Unauthorized", "Tenant Key");
            //Assert.assertEquals(TenantControllerResponseDTO.getUpdaterId(),"a4f17ac5f02e494ebe21263069b9ba61","Updater Id");

        } catch (Exception e) {
            throw e;
        }
    }

    //verify whether tenant can be deleted by given tenant ID
    @Test(priority = 40) @AlmAnnotation(almTestId = "611106")
    public void DeleteTenantWithUpdatedTenantId() throws Exception {
        try {
            CreateTenantWithFullPayload();
            String TenantId = TenantControllerResponseDTO.getTenantId();
            TenantControllerResponseDTO = deleteTenant(header, TenantId);
            Assert.assertEquals(StatusCode, 204, "Status Code");

        } catch (Exception e) {
            throw e;
        }
    }

    //verify whether only specific user can delete the tenant. any users should not be able to delete tenant.
    @Test(priority = 41) @AlmAnnotation(almTestId = "611107")
    public void DeleteTenantWith403Error() throws Exception {
        try {
            String TenantId = "9e4b6729-aef2-438c-90c0-b14126a647f1";
            TenantControllerResponseDTO = deleteTenant(headerNormal, TenantId);
            Assert.assertEquals(StatusCode, 403, "Status Code");
            Assert.assertEquals(TenantControllerResponseDTO.getDescription(),"You do not have DELETE_TENANT permission to access this method");
        } catch (Exception e) {
            throw e;
        }
    }

    //verify whether 400 bad request error display when delete deck with incorrect tenantId    @Test(priority = 32) @AlmAnnotation(almTestId = "611107")
    @Test(priority = 42) @AlmAnnotation(almTestId = "611109")
    public void DeleteTenantWithInvalidTenantId() throws Exception {
        try {
            String TenantId = "123";
            TenantControllerResponseDTO = deleteTenant(header, TenantId);
            Assert.assertEquals(StatusCode, 404, "Status Code");
            Assert.assertEquals(TenantControllerResponseDTO.getDescription(),"Provided Tenant-Id does not exists");
        } catch (Exception e) {
            throw e;
        }
    }

    //verify whether 401  error display when delete tenant with expired or invalid PI token
    @Test(priority = 43) @AlmAnnotation(almTestId = "611110")
    public void DeleteTenantWithUnauthorizedError() throws Exception {
        try {
            String TenantId = "9e4b6729-aef2-438c-90c0-b14126a647f1";
            header.put("X-Authorization","abc");
            TenantControllerResponseDTO = deleteTenant(header, TenantId);
            Assert.assertEquals(StatusCode, 401, "Status Code");
            Assert.assertEquals(TenantControllerResponseDTO.getError(),"Unauthorized");
        } catch (Exception e) {
            throw e;
        }
    }

    //verify whether 404 error displayed when delete a deleted tenant
    @Test(priority = 44) @AlmAnnotation(almTestId = "611110")
    public void DeleteTenantWithDeletedTenantId() throws Exception {

            CreateTenantWithFullPayload();
            String TenantId = TenantControllerResponseDTO.getTenantId();
            DeleteTenantWithFullPayload();
            TenantControllerResponseDTO = deleteTenant(header, TenantId);
            Assert.assertEquals(StatusCode, 404, "Status Code");
            Assert.assertEquals(TenantControllerResponseDTO.getDescription(),"Provided Tenant-Id does not exists");

    }

}
