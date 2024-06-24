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

public class RetrieveTenant_TenantController extends ServiceController {

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

    private static final Logger LOGGER = Logger.getLogger(String.valueOf(RetrieveTenant_TenantController.class));

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

    //verify whether creator Id is displayed  when passing valid tenantId
    @Test(priority = 5) @AlmAnnotation(almTestId = "609187")
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
    @Test(priority = 6) @AlmAnnotation(almTestId = "609186")
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
    @Test(priority = 7) @AlmAnnotation(almTestId = "609181")
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
    @Test(priority = 8) @AlmAnnotation(almTestId = "609183")
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
    @Test(priority = 9) @AlmAnnotation(almTestId = "609182")
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
    @Test(priority = 10) @AlmAnnotation(almTestId = "609189")
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
    @Test(priority = 11) @AlmAnnotation(almTestId = "609184")
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
    @Test(priority = 12) @AlmAnnotation(almTestId = "609185")
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
    @Test(priority = 13) @AlmAnnotation(almTestId = "609188")
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
    @Test(priority = 14) @AlmAnnotation(almTestId = "644990")
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
    @Test(priority = 15) @AlmAnnotation(almTestId = "609308")
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
    @Test(priority = 16) @AlmAnnotation(almTestId = "609320")
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
    @Test(priority = 17) @AlmAnnotation(almTestId = "609315")
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
    @Test(priority = 18) @AlmAnnotation(almTestId = "609319")
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
    @Test(priority = 19) @AlmAnnotation(almTestId = "609316")
    public void GetAllTenantWith403Error() throws Exception {
        try {
            TenantControllerResponseDTO = getAllTenantValidate(headerNormal);

            Assert.assertEquals(StatusCode, 403, "Status Code");
            Assert.assertEquals(TenantControllerResponseDTO.getDescription(),"You do not have GET_ALL_TENANTS permission to access this method");

        } catch (Exception e) {
            throw e;
        }
    }

}
