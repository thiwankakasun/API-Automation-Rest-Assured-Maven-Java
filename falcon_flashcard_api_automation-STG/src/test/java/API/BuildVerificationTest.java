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
import utils.PiTokenGenarator;
import utils.PiTokenGenaratorAuthor;
import utils.PiTokenGenaratorNormalUser;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

public class BuildVerificationTest extends ServiceController {

    //private String defaultContentType = "JSON";
    private StoreControllerResponseDTO StoreControllerResponseDTO;
    private StoreControllerResponseDTO[] StoreControllerDTO;
    private StoreControllerRequestDTO StoreControllerRequestDTO;
    private ExpertQuestionResponseDTO ExpertQuestionResponseDTO;

    private TenantController TenantController = new TenantController();
    private DeckController DeckController = new DeckController();
    private StoreController StoreController = new StoreController();
    private QuestionController QuestionController= new QuestionController();
    private FavouriteController FavouriteController = new FavouriteController();
    private LearningAnalyticsController LearningAnalyticsController = new LearningAnalyticsController();
    private StudyChannel StudyChannel = new StudyChannel();

    JsonReader jsonReader = new JsonReader();

    private static final Logger LOGGER = Logger.getLogger(String.valueOf(BuildVerificationTest.class));

    @Test(priority =1) @AlmAnnotation(almTestId = "611101")
    public void TenantControllerBVT1() throws Exception {
        try {
            TenantController.getData();
            TenantController.CreateTenantWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =2) @AlmAnnotation(almTestId = "611101")
    public void TenantControllerBVT2() throws Exception {
        try {
            TenantController.GetTenantWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =3) @AlmAnnotation(almTestId = "611101")
    public void TenantControllerBVT3() throws Exception {
        try {
            TenantController.UpdateTenantWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =4) @AlmAnnotation(almTestId = "611101")
    public void TenantControllerBVT4() throws Exception {
        try {
            TenantController.DeleteTenantWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =5) @AlmAnnotation(almTestId = "611101")
    public void TenantControllerBVT5() throws Exception {
        try {
            TenantController.GetTenantWithETextTenantId();

        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =6) @AlmAnnotation(almTestId = "611101")
    public void TenantControllerBVT6() throws Exception {
        try {
            TenantController.GetTenantWithPrepTenantId();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =7) @AlmAnnotation(almTestId = "611101")
    public void TenantControllerBVT7() throws Exception {
        try {
            TenantController.GetAllTenantWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =8) @AlmAnnotation(almTestId = "611101")
    public void TenantControllerBVT8() throws Exception {
        try {
            TenantController.GetTenantWith403Error();
        } catch (Exception e) {
            throw e;
        }
    }
    @Test(priority =9) @AlmAnnotation(almTestId = "611101")
    public void TenantControllerBVT9() throws Exception {
        try {
            TenantController.GetAllTenantWith403Error();
        } catch (Exception e) {
            throw e;
        }
    }


    @Test(priority =10) @AlmAnnotation(almTestId = "611101")
    public void DeckControllerBVT1() throws Exception {
        try {
            DeckController.getData();
            DeckController.createSingleDeckWithFullPayload();

        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =11) @AlmAnnotation(almTestId = "611101")
    public void DeckControllerBVT2() throws Exception {
        try {
            DeckController.CreateQuestionWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =12) @AlmAnnotation(almTestId = "611101")
    public void DeckControllerBVT3() throws Exception {
        try {
            DeckController.GetRecommendedCards();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =13) @AlmAnnotation(almTestId = "611101")
    public void DeckControllerBVT4() throws Exception {
        try {
            DeckController.PostRecommendedActivities();

        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =14) @AlmAnnotation(almTestId = "611101")
    public void DeckControllerBVT5() throws Exception {
        try {
            DeckController.GetDeckUsingProductId();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =15) @AlmAnnotation(almTestId = "611101")
    public void DeckControllerBVT6() throws Exception {
        try {
            DeckController.UpdateDeckWithFullPayload();

        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =16) @AlmAnnotation(almTestId = "611101")
    public void DeckControllerBVT7() throws Exception {
        try {
            DeckController.DeleteDeckWithFullPayload();

        } catch (Exception e) {
            throw e;
        }
    }
    @Test(priority =17) @AlmAnnotation(almTestId = "611101")
    public void DeckControllerBVT8() throws Exception {
        try {
            DeckController.createMultipleDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =18) @AlmAnnotation(almTestId = "611101")
    public void DeckControllerBVT9() throws Exception {
        try {
            DeckController.GetDeckWithValidatingCardCount();

        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =19) @AlmAnnotation(almTestId = "611101")
    public void DeckControllerBVT10() throws Exception {
        try {
            DeckController.GetDeckWithValidatingIncludeProgress();

        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =20) @AlmAnnotation(almTestId = "611101")
    public void DeckControllerBVT11() throws Exception {
        try {
            DeckController.GetDeckWithValidatingIncludeProgressandIncludeRecentProgress();

        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =21) @AlmAnnotation(almTestId = "611101")
    public void DeckControllerBVT12() throws Exception {
        try {
            DeckController.GetDeckWithValidatingResponseBody();

        } catch (Exception e) {
            throw e;
        }
    }


    @Test(priority =22) @AlmAnnotation(almTestId = "611101")
    public void QuestionControllerBVT1() throws Exception {
        try {
            QuestionController.getData();
            QuestionController.createSingleDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =23) @AlmAnnotation(almTestId = "611101")
    public void QuestionControllerBVT2() throws Exception {
        try {
            QuestionController.CreateQuestionWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =24) @AlmAnnotation(almTestId = "611101")
    public void QuestionControllerBVT3() throws Exception {
        try {
            QuestionController.GetQuestionByQuestionIdWithFullPayload();

        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =25) @AlmAnnotation(almTestId = "611101")

    public void QuestionControllerBVT4() throws Exception {
        try {
            QuestionController.UpdateQuestionWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =26) @AlmAnnotation(almTestId = "611101")
    public void QuestionControllerBVT5() throws Exception {
        try {
            QuestionController.DeleteSingleQuestionWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =27) @AlmAnnotation(almTestId = "611101")
    public void QuestionControllerBVT6() throws Exception {
        try {
            QuestionController.DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =28) @AlmAnnotation(almTestId = "611101")
    public void QuestionControllerBVT7() throws Exception {
        try {
            QuestionController.GetQuestionByQuestionIdWithValidatingFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =29) @AlmAnnotation(almTestId = "611101")
    public void QuestionControllerBVT8() throws Exception {
        try {
            QuestionController.GetQuestionByDeckIdWithValidatingFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =30) @AlmAnnotation(almTestId = "611101")
    public void QuestionControllerBVT9() throws Exception {
        try {

            QuestionController.DeleteMultipleQuestionWithBulkQuestions();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =31) @AlmAnnotation(almTestId = "611101")
    public void QuestionControllerBVT10() throws Exception {
        try {
            QuestionController.DeleteMultipleQuestionWithOnlySingleQuestion();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =32) @AlmAnnotation(almTestId = "611101")
    public void StoreControllerBVT1() throws Exception {
        try {
            StoreController.getData();
            StoreController.CreateExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =33) @AlmAnnotation(almTestId = "611101")
    public void StoreControllerBVT2() throws Exception {
        try {
            StoreController.GetExpertDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =34) @AlmAnnotation(almTestId = "611101")
    public void StoreControllerBVT3() throws Exception {
        try {
            StoreController.UpdateExpertDeckFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =35) @AlmAnnotation(almTestId = "611101")
    public void StoreControllerBVT4() throws Exception {
        try {
            StoreController.CreateExpertQuestion();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =36) @AlmAnnotation(almTestId = "611101")
    public void StoreControllerBVT5() throws Exception {
        try {
            StoreController.DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =37) @AlmAnnotation(almTestId = "611101")
    public void StoreControllerBVT6() throws Exception {
        try {
            StoreController.CreateExpertDeckWithMultipleBookIDs_ProductIDs();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =38) @AlmAnnotation(almTestId = "611101")
    public void StoreControllerBVT7() throws Exception {
        try {
            StoreController.UpdateExpertDeckWithAddingMultipleProductIds_MultipleBookIds();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =39) @AlmAnnotation(almTestId = "611101")
    public void FavouriteControllerBVT1() throws Exception {
        try {
            FavouriteController.getData();
            FavouriteController.createSingleDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =40) @AlmAnnotation(almTestId = "611101")
    public void FavouriteControllerBVT2() throws Exception {
        try {
            FavouriteController.CreateQuestionWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =41) @AlmAnnotation(almTestId = "611101")
    public void FavouriteControllerBVT3() throws Exception {
        try {
            FavouriteController.FavouriteQuestion();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =42) @AlmAnnotation(almTestId = "611101")
    public void FavouriteControllerBVT4() throws Exception {
        try {
            FavouriteController.GetFavouriteQuestion();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =43) @AlmAnnotation(almTestId = "611101")
    public void FavouriteControllerBVT5() throws Exception {
        try {
            FavouriteController.DeleteSingleQuestionWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =44) @AlmAnnotation(almTestId = "611101")
    public void FavouriteControllerBVT6() throws Exception {
        try {
            FavouriteController.DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =45) @AlmAnnotation(almTestId = "611101")
    public void LearningAnalyticsControllerBVT1() throws Exception {
        try {
            LearningAnalyticsController.getData();
            LearningAnalyticsController.CreateSingleDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =46) @AlmAnnotation(almTestId = "611101")
    public void LearningAnalyticsControllerBVT2() throws Exception {
        try {
            LearningAnalyticsController.CreateQuestionWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =47) @AlmAnnotation(almTestId = "611101")
    public void LearningAnalyticsControllerBVT3() throws Exception {
        try {
            LearningAnalyticsController.GetRecommendedCards();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =48) @AlmAnnotation(almTestId = "611101")
    public void LearningAnalyticsControllerBVT4() throws Exception {
        try {
            LearningAnalyticsController.PostRecommendedActivities();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =49) @AlmAnnotation(almTestId = "611101")
    public void LearningAnalyticsControllerBVT5() throws Exception {
        try {
            LearningAnalyticsController.GetActivitiesOfDecks();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =50) @AlmAnnotation(almTestId = "611101")
    public void LearningAnalyticsControllerBVT6() throws Exception {
        try {
            LearningAnalyticsController.FavouriteQuestion();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =51) @AlmAnnotation(almTestId = "611101")
    public void LearningAnalyticsControllerBVT7() throws Exception {
        try {
            LearningAnalyticsController.DeleteSingleQuestionWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =52) @AlmAnnotation(almTestId = "611101")
    public void LearningAnalyticsControllerBVT8() throws Exception {
        try {
            LearningAnalyticsController.DeleteDeckWithFullPayload();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =53) @AlmAnnotation(almTestId = "611101")
    public void StudyChannelBVT1() throws Exception {
        try {
            StudyChannel.getData();
            StudyChannel.CreateExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =54) @AlmAnnotation(almTestId = "611101")
    public void StudyChannelBVT2() throws Exception {
        try {
            StudyChannel.CreateExpertQuestion();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =55) @AlmAnnotation(almTestId = "611101")
    public void StudyChannelBVT3() throws Exception {
        try {
            StudyChannel.ProvisionExpertDecksOrGetProvisionedExpertDecks();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =56) @AlmAnnotation(almTestId = "611101")
    public void StudyChannelBVT4() throws Exception {
        try {
            StudyChannel.GetRecommendedCards();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =57) @AlmAnnotation(almTestId = "611101")
    public void StudyChannelBVT5() throws Exception {
        try {
            StudyChannel.PostRecommendedActivities();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =58) @AlmAnnotation(almTestId = "611101")
    public void StudyChannelBVT6() throws Exception {
        try {
            StudyChannel.GetActivitiesOfDecks();
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(priority =59) @AlmAnnotation(almTestId = "611101")
    public void StudyChannelBVT7() throws Exception {
        try {
            StudyChannel.DeleteExpertDeck();
        } catch (Exception e) {
            throw e;
        }
    }

//    @Test(priority = 1) @AlmAnnotation(almTestId = "xxxxx")
//    public void GetExpertDeckWithFullPayload() throws Exception {
//        try {
//            StoreControllerDTO = getallExpertDeck(headerAuthor);
//            Assert.assertEquals(StatusCode, 200, "Status Code");
//
//            for (int i=0; i<1000; i++) {
//                String ExpertId = StoreControllerDTO[i].getId();
//                StoreControllerResponseDTO = deleteExpert(headerAuthor, ExpertId);
//                if (StatusCode == 204) {
//                    Assert.assertEquals(StatusCode, 204, "Status Code");
//                    LOGGER.info(ExpertId+ " Expert deck deleted");
//                    //i=i+1;
//                } else {
//                    LOGGER.info(ExpertId+ " Expert deck not deleted");
//                    //i = i + 1;
//                }
//            }
//
//        } catch (Exception e) {
//            throw e;
//        }
//    }


//    @Test(priority = 2) @AlmAnnotation(almTestId = "xxxxxx")
//    public void DeleteExpertDeck() throws Exception {
//        try {
//            String ExpertId = StoreControllerDTO[0].getId();
//            StoreControllerResponseDTO = deleteExpert(headerAuthor, ExpertId);
//
//            Assert.assertEquals(StatusCode, 204, "Status Code");
//        } catch (Exception e) {
//            throw e;
//        }
//    }

}
