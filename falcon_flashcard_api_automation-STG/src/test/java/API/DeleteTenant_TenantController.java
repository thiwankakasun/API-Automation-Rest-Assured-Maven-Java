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

public class DeleteTenant_TenantController extends ServiceController {

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

    private static final Logger LOGGER = Logger.getLogger(String.valueOf(DeleteTenant_TenantController.class));

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
    //verify whether tenant can be deleted by given tenant ID
    @Test(priority = 5) @AlmAnnotation(almTestId = "611106")
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
    @Test(priority = 6) @AlmAnnotation(almTestId = "611107")
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
    @Test(priority = 7) @AlmAnnotation(almTestId = "611109")
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
    @Test(priority = 8) @AlmAnnotation(almTestId = "611110")
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
    @Test(priority = 9) @AlmAnnotation(almTestId = "611110")
    public void DeleteTenantWithDeletedTenantId() throws Exception {

            CreateTenantWithFullPayload();
            String TenantId = TenantControllerResponseDTO.getTenantId();
            DeleteTenantWithFullPayload();
            TenantControllerResponseDTO = deleteTenant(header, TenantId);
            Assert.assertEquals(StatusCode, 404, "Status Code");
            Assert.assertEquals(TenantControllerResponseDTO.getDescription(),"Provided Tenant-Id does not exists");

    }

}