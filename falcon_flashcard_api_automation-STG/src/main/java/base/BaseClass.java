package base;


import org.codehaus.jackson.map.ObjectMapper;
import com.pearson.common.framework.api.core.EnvParameters;
import com.pearson.common.framework.api.core.RESTServiceBase;
import com.pearson.common.framework.shared.datareader.TestData;

public class BaseClass extends RESTServiceBase {

    public int StatusCode;
    public String Env = EnvParameters.ENV;
    public ObjectMapper mapper = new ObjectMapper();

    public String loginUrl = TestData.getData(Env + "_LoginURL");

    public String createDeckUrl = TestData.getData(Env + "_CreateDeckUrl");
    public String getDeckUrl = TestData.getData(Env + "_GetDeckByUserIdUrl");
    //public String searchDeckUrl = TestData.getData(Env + "_SearchDeckUrl");
    public String updateDeckUrl = TestData.getData(Env + "_UpdateDeckUrl");
    public String deleteDeckUrl = TestData.getData(Env + "_DeleteDeckUrl");

    public String createExpertDeckUrl = TestData.getData(Env + "_CreateExpertUrl");
    public String getExpertDeckUrl = TestData.getData(Env + "_GetExpertUrl");
    public String getAllExpertDeckUrl = TestData.getData(Env + "_GetAllExpertUrl");
    public String updateExpertDeckUrl = TestData.getData(Env + "_UpdateExpertUrl");
    public String deleteExpertDeckUrl = TestData.getData(Env + "_DeleteExpertUrl");

    public String getAllExpertBundleUrl = TestData.getData(Env + "_GetAllExpertBundleUrl");
    public String deleteExpertBundleUrl = TestData.getData(Env + "_DeleteExpertBundleUrl");

    public String getProvisionedDeckUrl = TestData.getData(Env + "_GetProvisionedDeckUrl");

    public String createTenantUrl = TestData.getData(Env + "_CreateTenantUrl");
    public String getTenantUrl = TestData.getData(Env + "_GetTenantUrl");
    public String getAllTenantUrl = TestData.getData(Env + "_GetAllTenantUrl");
    public String updateTenantUrl = TestData.getData(Env + "_UpdateTenantUrl");
    public String deleteTenantUrl = TestData.getData(Env + "_DeleteTenantUrl");

    public String createQuestionUrl = TestData.getData(Env + "_CreateQuestionUrl");
    public String getQuestionUrl = TestData.getData(Env + "_GetQuestionUrl");
    public String getQuestionByDeckIdUrl = TestData.getData(Env + "_GetQuestionByDeckIdUrl");
    public String updateQuestionUrl = TestData.getData(Env + "_UpdateQuestionUrl");
    public String patchQuestionUrl = TestData.getData(Env + "_PatchQuestionUrl");

    public String deleteQuestionUrl = TestData.getData(Env + "_DeleteQuestionUrl");
    public String deleteMultipleQuestionUrl = TestData.getData(Env + "_DeleteMultiQuestionUrl");

    public String createExpertQuestionUrl = TestData.getData(Env + "_CreateExpertQuestionUrl");
    public String getExpertQuestionUrl = TestData.getData(Env + "_GetExpertQuestionUrl");
    public String updateExpertQuestionUrl = TestData.getData(Env + "_UpdateExpertQuestionUrl");
    public String deleteExpertQuestionUrl = TestData.getData(Env + "_DeleteExpertQuestionUrl");

    public String FavouriteQuestionUrl = TestData.getData(Env + "_FavouriteUrl");
    public String getFavouriteQuestionUrl = TestData.getData(Env + "_GetFavouriteUrl");

    public String GetRecommendationUrl = TestData.getData(Env + "_GetRecommendationUrl");
    public String PostRecommendedActivitiesUrl = TestData.getData(Env + "_PostRecommendedActivitiesUrl");
    public String GetActivitiesOfDecksUrl = TestData.getData(Env + "_GetActivitiesOfDecksUrl");

    public String DeckCreatePayload = TestData.getData(Env + "_CreateDeckPayload");
    public String DeckUpdatePayload = TestData.getData(Env + "_UpdateDeckPayload");

    public String ExpertDeckCreatePayload = TestData.getData(Env + "_CreateExpertDeckPayload");
    public String ExpertDeckUpdatePayload = TestData.getData(Env + "_UpdateExpertDeckPayload");

    public String TenantCreatePayload = TestData.getData(Env + "_CreateTenantPayload");
    public String TenantUpdatePayload = TestData.getData(Env + "_UpdateTenantPayload");

    public String QuestionCreatePayload = TestData.getData(Env + "_CreateQuestionPayload");
    public String QuestionUpdatePayload = TestData.getData(Env + "_UpdateQuestionPayload");
    public String QuestionPatchPayload = TestData.getData(Env + "_PatchQuestionPayload");
//ExpertQuestion
    public String ExpertQuestionCreatePayload = TestData.getData(Env + "_CreateExpertQuestionPayload");
    public String ExpertQuestionGetPayload = TestData.getData(Env + "_GetExpertQuestionPayload");
    public String ExpertQuestionUpdatePayload = TestData.getData(Env + "_UpdateExpertQuestionPayload");
    public String ExpertQuestionDeletePayload = TestData.getData(Env + "_DeleteExpertQuestionPayload");

    public String FavouriteQuestionPayload = TestData.getData(Env + "_FavouriteQuestionPayload");

    public String GetRecommendationPayload = TestData.getData(Env + "_GetRecommendationPayload");
    public String PostRecommendedActivitiesPayload = TestData.getData(Env + "_PostRecommendedActivitiesPayload");
    public String GetActivitiesOfDecksPayload = TestData.getData(Env + "_GetActivitiesOfDecksPayload");

    public String PITokenGeneratePayload = TestData.getData(Env + "_PITokenGeneratePayload");

    public String username = TestData.getData(Env + "_UserName");
    public String password = TestData.getData(Env + "_Password");

    public String usernameNormal = TestData.getData(Env + "_UserNameNormalUser");
    public String passwordNormal = TestData.getData(Env + "_PasswordNormalUser");

    public String usernameAuthor = TestData.getData(Env + "_UserNameAuthor");
    public String passwordAuthor = TestData.getData(Env + "_PasswordAuthor");


}
