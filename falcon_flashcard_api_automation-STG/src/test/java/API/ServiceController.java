package API;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Logger;

import base.BaseClass;
import cc.plural.jsonij.JSON;
import io.restassured.response.Response;

import com.pearson.common.framework.api.core.RESTServiceBase;
import org.jetbrains.annotations.NotNull;
import responseDTO.*;
import utils.JsonReader;

public class ServiceController extends BaseClass {

    private String defaultContentType = "JSON";
    private TenantControllerResponseDTO TenantControllerResponseDTO;
    private TenantControllerResponseDTO[] TenantControllerDTO;
    private StoreControllerResponseDTO StoreControllerResponseDTO;
    private StoreControllerResponseDTO[] StoreControllerDTO;
    private DeckControllerResponseDTO DeckControllerResponseDTO;
    private QuestionControllerResponseDTO QuestionControllerResponseDTO;
    private FavouriteControllerResponseDTO FavouriteControllerResponseDTO;
    private GetRecommendationResponseDTO GetRecommendationResponseDTO;
    private PostRecommendedActivitiesResponseDTO PostRecommendedActivitiesResponseDTO;
    private GetActivitiesOfDecksResponseDTO GetActivitiesOfDecksResponseDTO;
    private DeckResponseDTO[] DeckResponseDTO;
    private DeckResponseDTO SearchDeckResponseDTO;
    private DeckResponseDTO DeckUpdateDTO;
    private QuestionDTO QuestionDTO;
    private GetDeckDTO GetDeckDTO;
    private DeckResponseDTO ProvisionedDeckDTO;
    private ExpertQuestionResponseDTO ExpertQuestionResponseDTO;
    private UserDTO UserDTO;
    JsonReader jsonReader = new JsonReader();
    private static final Logger LOGGER = Logger.getLogger(String.valueOf(TenantController.class));
    private static final Logger LOGGER1 = Logger.getLogger(String.valueOf(DeckController.class));


    /*
     *
     *     PI Token Generate Endpoint
     *
     */
    //Create Tenant
    public UserDTO generatePIToken(String payload) throws IOException {
        try{
            Response response = RESTServiceBase.postCallWithJsonBodyParam(payload, loginUrl, defaultContentType);
            // Convert response to String
            String createTenantResponse =response.asString();
            LOGGER.info("Response is: " + response.asString());
            //Map Payload to response DTO
//            Properties a = new Properties();
//            a.setProperty("plus",createTenantResponse);
            UserDTO = mapper.readValue(response.asString(), UserDTO.class);

            StatusCode = response.statusCode();
            //Print the response
            LOGGER.info("Response is: " + response.asString());

            return UserDTO;
        }catch (Exception e){
            throw e;
        }
    }


    /*
     *
     *     Tenant Controller Service
     *
     */
    //Create Tenant
    public TenantControllerResponseDTO createTenant(HashMap<String, String> header, String payload) throws IOException {
        try{
            Response response = RESTServiceBase.postCallWithHeaderAndBodyParam(header, payload, createTenantUrl);
            // Convert response to String
            //String createTenantResponse = String.valueOf(jsonReader.convertResponseToJson(response));
            //Map Payload to response DTO
            TenantControllerResponseDTO = mapper.readValue(response.asString(), TenantControllerResponseDTO.class);

            StatusCode = response.statusCode();
            //Print the response
            LOGGER.info("Response is: " + response.asString());

            return TenantControllerResponseDTO;
        }catch (Exception e){
            throw e;
        }
    }

    //Get Tenant
    public TenantControllerResponseDTO getTenant(HashMap<String, String> header, String TenantId) throws IOException {
        try {
            Response response = RESTServiceBase.getCallWithHeaderParam
                    (header, getTenantUrl.replace("TenantId", TenantId));
            //String getTenantResponse = String.valueOf(jsonReader.convertResponseToJson(response));
            TenantControllerResponseDTO = mapper.readValue(response.asString(), TenantControllerResponseDTO.class);

            StatusCode = response.statusCode();

            LOGGER.info("Response is: " + response.asString());
            return TenantControllerResponseDTO;
        }
        catch (Exception e){
            throw e;
        }
    }

    //Get All Tenant
    public TenantControllerResponseDTO[] getAllTenant(HashMap<String, String> header) throws IOException {
        try {
            Response response = RESTServiceBase.getCallWithHeaderParam
                    (header, getAllTenantUrl);
            //String getTenantResponse = String.valueOf(jsonReader.convertResponseToJson(response));
            TenantControllerDTO = mapper.readValue(response.asString(), TenantControllerResponseDTO[].class);

            StatusCode = response.statusCode();

            LOGGER.info("Response is: " + response.asString());
            return TenantControllerDTO;
        }
        catch (Exception e){
            throw e;
        }
    }

    //Get All Tenant Validate
    public TenantControllerResponseDTO getAllTenantValidate(HashMap<String, String> header) throws IOException {
        try {
            Response response = RESTServiceBase.getCallWithHeaderParam
                    (header, getAllTenantUrl);
            //String getTenantResponse = String.valueOf(jsonReader.convertResponseToJson(response));
            TenantControllerResponseDTO = mapper.readValue(response.asString(), TenantControllerResponseDTO.class);

            StatusCode = response.statusCode();

            LOGGER.info("Response is: " + response.asString());
            return TenantControllerResponseDTO;
        }
        catch (Exception e){
            throw e;
        }
    }

    //Update Tenant
    public TenantControllerResponseDTO updateTenant(HashMap<String, String> header, String payload, String TenantId) throws IOException {
        try {
            Response response = RESTServiceBase.putCallWithHeaderAndBodyParam
                    (header, payload, updateTenantUrl.replace("TenantId", TenantId));
            String updateTenantResponse = String.valueOf(jsonReader.convertResponseToJson(response));
            TenantControllerResponseDTO = mapper.readValue(updateTenantResponse, TenantControllerResponseDTO.class);

            StatusCode = response.statusCode();

            LOGGER.info("Response is: " + updateTenantResponse);
            return TenantControllerResponseDTO;
        }catch (Exception e){
            throw e;
        }
    }

    //Delete Tenant
    public TenantControllerResponseDTO deleteTenant(HashMap<String, String> header, String TenantId) throws IOException {
        try {
            Response response = RESTServiceBase.deleteCallWithHeaderWithoutRequestBody
                    (header, deleteTenantUrl.replace("TenantId", TenantId));

            String createTenantResponse = String.valueOf(jsonReader.convertResponseToJson(response));
            LOGGER.info("Body: "+createTenantResponse);
            if(createTenantResponse!=null) {
                TenantControllerResponseDTO = mapper.readValue(createTenantResponse, TenantControllerResponseDTO.class);
            }
            StatusCode = response.statusCode();
            return TenantControllerResponseDTO;
        }catch (Exception e){
            throw e;
        }
    }




    /*
     *
     *     Deck Controller Service
     *
     */

    //Create Deck Endpoint
    public DeckControllerResponseDTO createDeck(HashMap<String, String> headerNormal, String payload) throws IOException {
        try{
            Response response = RESTServiceBase.postCallWithHeaderAndBodyParam(headerNormal, payload, createDeckUrl);
            LOGGER.info("Response is: " + response.asString());
            DeckControllerResponseDTO = mapper.readValue(response.asString(), DeckControllerResponseDTO.class);

            StatusCode = response.statusCode();
            //Print the response

            return DeckControllerResponseDTO;
        }catch (Exception e){
            throw e;
        }
    }

    //Get Deck Endpoint
    public DeckResponseDTO[] getDeck(HashMap<String, String> header, HashMap<String, String> queryparam, String DeckId) throws IOException, InterruptedException {
        try {
            Response response = RESTServiceBase.getCallWithHeaderAndQueryParam
                    (header, queryparam, getDeckUrl.replace("userId", DeckId), defaultContentType);

            Thread.sleep(10000);
            DeckResponseDTO = mapper.readValue(response.asString(), DeckResponseDTO[].class);

            StatusCode = response.statusCode();

            LOGGER.info("Response is: " + response.asString());
            return DeckResponseDTO;
        }
        catch (Exception e){
            throw e;
        }
    }

//    public DeckResponseDTO searchDeck(HashMap<String, String> header, HashMap<String, String> queryparam, String UserId,String ProductId, String BookId) throws IOException, InterruptedException {
//        try {
//            Response response = RESTServiceBase.getCallWithHeaderAndQueryParam
//                    (header, queryparam, searchDeckUrl.replace("userId", UserId).replace("productId",ProductId).replace("bookId",BookId), defaultContentType);
//
//            Thread.sleep(10000);
//            //SearchDeckResponseDTO = mapper.readValue(response.asString(), DeckResponseDTO.class);
//
//            StatusCode = response.statusCode();
//
//            LOGGER.info("Response is: " + response.asString());
//            return SearchDeckResponseDTO;
//        }
//        catch (Exception e){
//            throw e;
//        }
//    }

    //Get Deck Endpoint
    public DeckControllerResponseDTO getDeckValidate(HashMap<String, String> header, HashMap<String, String> queryparam, String DeckId) throws IOException, InterruptedException {
        try {
            Response response = RESTServiceBase.getCallWithHeaderAndQueryParam
                    (header, queryparam, getDeckUrl.replace("userId", DeckId), defaultContentType);

            Thread.sleep(10000);
            DeckControllerResponseDTO = mapper.readValue(response.asString(), DeckControllerResponseDTO.class);

            StatusCode = response.statusCode();

            LOGGER.info("Response is: " + response.asString());
            return DeckControllerResponseDTO;
        }
        catch (Exception e){
            throw e;
        }
    }

    //Update Deck Endpoint
    public DeckResponseDTO updateDeck(HashMap<String, String> header, String payload, String DeckId) throws IOException {
        try {
            Response response = RESTServiceBase.patchCallWithHeaderAndBodyParam
                    (header, payload, updateDeckUrl.replace("deckId", DeckId));
            String updateTenantResponse = String.valueOf(jsonReader.convertResponseToJson(response));
            DeckUpdateDTO = mapper.readValue(updateTenantResponse, DeckResponseDTO.class);

            StatusCode = response.statusCode();

            LOGGER.info("Response is: " + updateTenantResponse);
            return DeckUpdateDTO;
        }catch (Exception e){
            throw e;
        }
    }

    //Delete Deck Endpoint
    public DeckControllerResponseDTO deleteDeck(HashMap<String, String> header, String DeckId) throws IOException {
        try {
            Response response = RESTServiceBase.deleteCallWithHeaderWithoutRequestBody
                    (header, deleteDeckUrl.replace("deckId", DeckId));

            String createDeckResponse = String.valueOf(jsonReader.convertResponseToJson(response));
            LOGGER.info("Body: "+createDeckResponse);
            if(createDeckResponse!=null) {
                DeckControllerResponseDTO = mapper.readValue(createDeckResponse, DeckControllerResponseDTO.class);
            }
            StatusCode = response.statusCode();
            return DeckControllerResponseDTO;
        }catch (Exception e){
            throw e;
        }
    }

    /*
     *
     *     Question Controller Service
     *
     */

    //Create Question Endpoint
    public QuestionControllerResponseDTO createQuestion(HashMap<String, String> headerNormal, String payload) throws IOException {
        try{
            Response response = RESTServiceBase.postCallWithHeaderAndBodyParam(headerNormal, payload, createQuestionUrl);
            LOGGER.info("Response is: " + response.asString());
            QuestionControllerResponseDTO = mapper.readValue(response.asString(), QuestionControllerResponseDTO.class);

            StatusCode = response.statusCode();

            return QuestionControllerResponseDTO;
        }catch (Exception e){
            throw e;
        }
    }

    //Get Question Endpoint
    public QuestionDTO getQuestionByQuestionId(HashMap<String, String> header, String QuestionId) throws IOException, InterruptedException {
        try {
            Response response = RESTServiceBase.getCallWithHeaderParam(header, getQuestionUrl.replace("questionId", QuestionId),defaultContentType);
            LOGGER.info("Response is: " + response.asString());
            Thread.sleep(10000);
            QuestionDTO = mapper.readValue(response.asString(), QuestionDTO.class);

            StatusCode = response.statusCode();

            LOGGER.info("Response is: " + response.asString());
            return QuestionDTO;
        }
        catch (Exception e){
            throw e;
        }
    }

    //Get Question By Deck Id Endpoint
    public QuestionControllerResponseDTO getQuestionByDeckId(HashMap<String, String> header, HashMap<String, String> queryparam) throws IOException, InterruptedException {
        try {
            Response response = RESTServiceBase.getCallWithHeaderAndQueryParam(header,queryparam, getQuestionByDeckIdUrl,defaultContentType);
            LOGGER.info("Response is: " + response.asString());
            Thread.sleep(10000);
            QuestionControllerResponseDTO = mapper.readValue(response.asString(), QuestionControllerResponseDTO.class);

            StatusCode = response.statusCode();

            LOGGER.info("Response is: " + response.asString());
            return QuestionControllerResponseDTO;
        }
        catch (Exception e){
            throw e;
        }
    }

    //Update Question Endpoint
    public QuestionControllerResponseDTO updateQuestion(HashMap<String, String> header, String payload, long epochTime) throws IOException {
        try {
            Response response = RESTServiceBase.putCallWithHeaderAndBodyParam(header,payload,updateQuestionUrl.replace("epochtime",String.valueOf(epochTime)));
            String updateTenantResponse = String.valueOf(jsonReader.convertResponseToJson(response));
            QuestionControllerResponseDTO = mapper.readValue(updateTenantResponse, QuestionControllerResponseDTO.class);

            StatusCode = response.statusCode();

            LOGGER.info("Response is: " + updateTenantResponse);
            return QuestionControllerResponseDTO;
        }catch (Exception e){
            throw e;
        }
    }

    //Patch Question Endpoint
    public QuestionDTO patchQuestion(HashMap<String, String> header, String payload,String currentTimestamp ,String questionId) throws IOException {
        try {
            Response response = RESTServiceBase.patchCallWithHeaderAndBodyParam(header,payload,patchQuestionUrl.replace("questionId",questionId).replace("epochtime",currentTimestamp));
            String updateTenantResponse = String.valueOf(jsonReader.convertResponseToJson(response));
            QuestionDTO = mapper.readValue(updateTenantResponse, QuestionDTO.class);

            StatusCode = response.statusCode();

            LOGGER.info("Response is: " + updateTenantResponse);
            return QuestionDTO;
        }catch (Exception e){
            throw e;
        }
    }

    //Delete Question Endpoint
    public QuestionControllerResponseDTO deleteQuestion(HashMap<String, String> header, String QuestionId) throws IOException {
        try {
            Response response = RESTServiceBase.deleteCallWithHeaderWithoutRequestBody
                    (header, deleteQuestionUrl.replace("questionId", QuestionId));

            String createDeckResponse = String.valueOf(jsonReader.convertResponseToJson(response));
            LOGGER.info("Body: "+createDeckResponse);
            if(createDeckResponse!=null) {
                QuestionControllerResponseDTO = mapper.readValue(createDeckResponse, QuestionControllerResponseDTO.class);
            }
            StatusCode = response.statusCode();
            return QuestionControllerResponseDTO;
        }catch (Exception e){
            throw e;
        }
    }

    //Delete Multiple Question Endpoint
    public QuestionControllerResponseDTO deleteMultipleQuestion(HashMap<String, String> header, String questionId) throws IOException, InterruptedException {
        try {
            Response response = RESTServiceBase.deleteCallWithHeaderWithoutRequestBody(header, deleteMultipleQuestionUrl.replace("card",questionId));

            String createDeckResponse = String.valueOf(jsonReader.convertResponseToJson(response));
            LOGGER.info("Body: "+createDeckResponse);
            if(createDeckResponse!=null) {
                QuestionControllerResponseDTO = mapper.readValue(createDeckResponse, QuestionControllerResponseDTO.class);
            }
            StatusCode = response.statusCode();
            return QuestionControllerResponseDTO;
        }catch (Exception e){
            throw e;
        }
    }

    //Favourite Question Endpoint
    public FavouriteControllerResponseDTO favouriteQuestion(HashMap<String, String> headerNormal, String payload) throws IOException {
        try{
            Response response = RESTServiceBase.postCallWithHeaderAndBodyParam(headerNormal, payload, FavouriteQuestionUrl);
            LOGGER.info("Response is: " + response.asString());
            FavouriteControllerResponseDTO = mapper.readValue(response.asString(), FavouriteControllerResponseDTO.class);

            StatusCode = response.statusCode();

            return FavouriteControllerResponseDTO;
        }catch (Exception e){
            throw e;
        }
    }

    //Get Question Endpoint
    public FavouriteControllerResponseDTO getfavouriteQuestion(HashMap<String, String> header, String deckId) throws IOException, InterruptedException {
        try {
            Response response = RESTServiceBase.getCallWithHeaderParam(header, getFavouriteQuestionUrl.replace("deckId", deckId),defaultContentType);
            LOGGER.info("Response is: " + response.asString());
            Thread.sleep(10000);
            FavouriteControllerResponseDTO = mapper.readValue(response.asString(), FavouriteControllerResponseDTO.class);

            StatusCode = response.statusCode();

            LOGGER.info("Response is: " + response.asString());
            return FavouriteControllerResponseDTO;
        }
        catch (Exception e){
            throw e;
        }
    }

    //Get Recommended Cards Endpoint
    public GetRecommendationResponseDTO getRecommendation(HashMap<String, String> headerNormal, String payload) throws IOException {
        try{
            Response response = RESTServiceBase.postCallWithHeaderAndBodyParam(headerNormal, payload, GetRecommendationUrl);
            LOGGER.info("Response is: " + response.asString());
            GetRecommendationResponseDTO = mapper.readValue(response.asString(), GetRecommendationResponseDTO.class);

            StatusCode = response.statusCode();

            return GetRecommendationResponseDTO;
        }catch (Exception e){
            throw e;
        }
    }

    //Post Recommended Activities Endpoint
    public PostRecommendedActivitiesResponseDTO postRecommendedActivities(HashMap<String, String> headerNormal, String payload) throws IOException {
        try{
            Response response = RESTServiceBase.postCallWithHeaderAndBodyParam(headerNormal, payload, PostRecommendedActivitiesUrl);
            LOGGER.info("Response is: " + response.asString());
            PostRecommendedActivitiesResponseDTO = mapper.readValue(response.asString(), PostRecommendedActivitiesResponseDTO.class);

            StatusCode = response.statusCode();

            return PostRecommendedActivitiesResponseDTO;
        }catch (Exception e){
            throw e;
        }
    }

    //Post Recommended Activities Endpoint
    public GetActivitiesOfDecksResponseDTO getActivitiesOfDecks(HashMap<String, String> headerNormal, String payload) throws IOException {
        try{
            Response response = RESTServiceBase.postCallWithHeaderAndBodyParam(headerNormal, payload, GetActivitiesOfDecksUrl);
            LOGGER.info("Response is: " + response.asString());
            GetActivitiesOfDecksResponseDTO = mapper.readValue(response.asString(), GetActivitiesOfDecksResponseDTO.class);

            StatusCode = response.statusCode();

            return GetActivitiesOfDecksResponseDTO;
        }catch (Exception e){
            throw e;
        }
    }

    /*
     *
     *     Store Controller Service
     *
     */

    //create expert deck

    public StoreControllerResponseDTO createExpertDeck(HashMap<String, String> header, String payload) throws IOException {
        try {
            //Create deck
            Response response = RESTServiceBase.postCallWithHeaderAndBodyParam(header, payload, createExpertDeckUrl);
            // Convert response to String
            //String createTenantResponse = String.valueOf(jsonReader.convertResponseToJson(response));
            //Map Payload to response DTO
            StoreControllerResponseDTO = mapper.readValue(response.asString(), StoreControllerResponseDTO.class);

            StatusCode = response.statusCode();
            //Print the response
            LOGGER.info("Response is: " + response.asString());

            return StoreControllerResponseDTO;
        } catch (Exception e) {
            throw e;
        }
    }

    //retrieve expert decks

    public StoreControllerResponseDTO getExpertDeck(HashMap<String, String> header, String ExpertDeckId) throws IOException {
        //Get deck
        Response response = RESTServiceBase.getCallWithHeaderParam(header, getExpertDeckUrl.replace("ExpertId", ExpertDeckId));
        //String getTenantResponse = String.valueOf(jsonReader.convertResponseToJson(response));
        StoreControllerResponseDTO = mapper.readValue(response.asString(), StoreControllerResponseDTO.class);

        StatusCode = response.statusCode();

        LOGGER.info("Response is: " + response.asString());
        return StoreControllerResponseDTO;
    }

    //retrieve all expert decks

    public StoreControllerResponseDTO[] getallExpertDeck(HashMap<String, String> header) throws IOException {
        //Get deck
        Response response = RESTServiceBase.getCallWithHeaderParam(header, getAllExpertDeckUrl);
        //String getTenantResponse = String.valueOf(jsonReader.convertResponseToJson(response));
        StoreControllerDTO = mapper.readValue(response.asString(), StoreControllerResponseDTO[].class);

        StatusCode = response.statusCode();

        LOGGER.info("Response is: " + response.asString());
        return StoreControllerDTO;
    }

    //Update Expert deck
    public StoreControllerResponseDTO updateExpertDecks(HashMap<String, String> header, String payload, String ExpertdeckId) throws IOException {
        try {
            Response response = RESTServiceBase.putCallWithHeaderAndBodyParam
                    (header, payload, updateExpertDeckUrl.replace("ExpertId", ExpertdeckId));
            //String updateExpertResponse = String.valueOf(jsonReader.convertResponseToJson(response));
            StoreControllerResponseDTO = mapper.readValue(response.asString(), StoreControllerResponseDTO.class);

            StatusCode = response.statusCode();

            LOGGER.info("Response is: " + response.asString());
            return StoreControllerResponseDTO;
        } catch (Exception e) {
            throw e;
        }
    }

    //Delete Expert Deck
    public StoreControllerResponseDTO deleteExpert(HashMap<String, String> header, String ExperttId) throws IOException {
        try {
            Response response = RESTServiceBase.deleteCallWithHeaderWithoutRequestBody
                    (header, deleteExpertDeckUrl.replace("ExpertId", ExperttId));

            String deleteExpertResponse = String.valueOf(jsonReader.convertResponseToJson(response));
            LOGGER.info("Body: " + deleteExpertResponse);
            if (deleteExpertResponse != null) {
                StoreControllerResponseDTO = mapper.readValue(deleteExpertResponse, StoreControllerResponseDTO.class);
            }
            StatusCode = response.statusCode();
            return StoreControllerResponseDTO;
        } catch (Exception e) {
            throw e;
        }
    }


    public StoreControllerResponseDTO[] getallExpertBundle(HashMap<String, String> header, String UserId, String BundleId) throws IOException {
        //Get deck
        Response response = RESTServiceBase.getCallWithHeaderParam(header, getAllExpertBundleUrl.replace("UserId",UserId).replace("BundleId",BundleId));
        //String getTenantResponse = String.valueOf(jsonReader.convertResponseToJson(response));
        StoreControllerDTO = mapper.readValue(response.asString(), StoreControllerResponseDTO[].class);

        StatusCode = response.statusCode();

        LOGGER.info("Response is: " + response.asString());
        return StoreControllerDTO;
    }

    public StoreControllerResponseDTO deleteExpertBundle(HashMap<String, String> header, String ExperttId) throws IOException {
        try {
            Response response = RESTServiceBase.deleteCallWithHeaderWithoutRequestBody
                    (header, deleteExpertBundleUrl.replace("BundleId", ExperttId));

            String deleteExpertResponse = String.valueOf(jsonReader.convertResponseToJson(response));
            LOGGER.info("Body: " + deleteExpertResponse);
            if (deleteExpertResponse != null) {
                StoreControllerResponseDTO = mapper.readValue(deleteExpertResponse, StoreControllerResponseDTO.class);
            }
            StatusCode = response.statusCode();
            return StoreControllerResponseDTO;
        } catch (Exception e) {
            throw e;
        }
    }


    /*
     *
     *     Expert Question Service
     *
     */

    //create expert question

    public ExpertQuestionResponseDTO createExpertQuestion(HashMap<String, String> header, String payload) throws IOException {
        try {
            //Create deck
            Response response = RESTServiceBase.postCallWithHeaderAndBodyParam(header, payload, createExpertQuestionUrl);
            // Convert response to String
            //String createTenantResponse = String.valueOf(jsonReader.convertResponseToJson(response));
            //Map Payload to response DTO
            ExpertQuestionResponseDTO = mapper.readValue(response.asString(), ExpertQuestionResponseDTO.class);

            StatusCode = response.statusCode();
            //Print the response
            LOGGER.info("Response is: " + response.asString());

            return ExpertQuestionResponseDTO;
        } catch (Exception e) {
            throw e;
        }
    }

    //get expert question

    public ExpertQuestionResponseDTO getExpertQuestion(HashMap<String, String> header, String ExpertquestionId) throws IOException {
        try {
            Response response = RESTServiceBase.getCallWithHeaderParam(header, getExpertQuestionUrl.replace("ExpertquestionId",ExpertquestionId));

            ExpertQuestionResponseDTO = mapper.readValue(response.asString(), ExpertQuestionResponseDTO.class);

            StatusCode = response.statusCode();
            LOGGER.info("Response is: " + response.asString());

            return ExpertQuestionResponseDTO;
        }
        catch (Exception e) {
            throw e;
        }
    }

    //Update Expert Question
    public ExpertQuestionResponseDTO updateExpertQuestion(HashMap<String, String> header,String payload, String ExpertquestionId) throws IOException {
        try {
            Response response = RESTServiceBase.putCallWithHeaderAndBodyParam(header, payload, updateExpertQuestionUrl.replace("ExpertquestionId", ExpertquestionId));
            //String updateExpertResponse = String.valueOf(jsonReader.convertResponseToJson(response));
            ExpertQuestionResponseDTO = mapper.readValue(response.asString(), ExpertQuestionResponseDTO.class);

            StatusCode = response.statusCode();

            LOGGER.info("Response is: " + response.asString());
            return ExpertQuestionResponseDTO;
        }catch (Exception e){
            throw e;
        }
    }
//delete expert question

    public ExpertQuestionResponseDTO deleteExpertQuestion(HashMap<String, String> header, String ExpertquestionId) throws IOException {
        try {

            Response response = RESTServiceBase.deleteCallWithHeaderWithoutRequestBody(header, deleteExpertQuestionUrl.replace("ExpertquestionId", ExpertquestionId));
            // Convert response to String
           // String deleteExpertResponse = String.valueOf(jsonReader.convertResponseToJson(response));
            //Map Payload to response DTO

            String deleteExpertResponse = String.valueOf(jsonReader.convertResponseToJson(response));
            LOGGER.info("Body: " + deleteExpertResponse);
            if (deleteExpertResponse != null) {
                ExpertQuestionResponseDTO = mapper.readValue(deleteExpertResponse, ExpertQuestionResponseDTO.class);
            }
          //  ExpertQuestionResponseDTO = mapper.readValue(response.asString(), ExpertQuestionResponseDTO.class);

            StatusCode = response.statusCode();
            //Print the response
           // LOGGER.info("Response is: " + response.asString());

            return ExpertQuestionResponseDTO;
        } catch (Exception e) {
            throw e;
        }
    }

    /*
     *
     *     Study Channel Service
     *
     */
    //Provision an expert deck and get provisioned (user) deck details Endpoint
    public DeckResponseDTO GetProvisionedDeck(HashMap<String, String> header, HashMap<String, String> queryparam, String ExpertDeckId) throws IOException, InterruptedException {
        try {
            Response response = RESTServiceBase.getCallWithHeaderAndQueryParam
                    (header, queryparam, getProvisionedDeckUrl.replace("ExpertId", ExpertDeckId), defaultContentType);

            Thread.sleep(10000);
            ProvisionedDeckDTO = mapper.readValue(response.asString(), DeckResponseDTO.class);

            StatusCode = response.statusCode();

            LOGGER.info("Response is: " + response.asString());
            return ProvisionedDeckDTO;
        }
        catch (Exception e){
            throw e;
        }
    }



//    @Test
//    public void piLoginTest() throws Exception {
//
//        // Read JSON file and create a JSONObject
//        JSONParser parser = new JSONParser();
//        Reader reader = new FileReader(".\\testdata\\api-payload\\login-payload.json");
//        JSONObject jsonObject1 = (JSONObject) parser.parse(reader);
//
//
//        jsonObject1.put("userName", "samc2");
//        jsonObject1.put("password", "Password1");
//
//        System.out.println(jsonObject1);
//
////        JSONObject jsonObject = new JSONObject();
////        jsonObject.put("userName", username);
////        jsonObject.put("password", password);
////
//        Response response = RESTServiceBase.postCallWithJsonBodyParam(jsonObject1.toString(), loginUrl);
//        LoggerUtil.log(response.asString());
//        PiValidAuthResponse res = mapper.readValue(response.asString(), PiValidAuthResponse.class);
//        Assert.assertEquals(res.getStatus(), "success");
//        Assert.assertNotNull(res.getData());
//    }


//    @Test
//    public void piLoginTest1() throws Exception {
//
//        HashMap<String, Header> authHeader = new HashMap<String, Header>();
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("userName", username);
//        jsonObject.put("password", password);
//        Responses responses = RESTWrapper.doRequest(authHeader, ContentType.JSON, url,
//                jsonObject.toString(), Method.POST);
//        System.out.println(responses.getBody().toString());
//        PiValidAuthResponse res =
//                mapper.readValue(responses.getBody().getBodyString(), PiValidAuthResponse.class);
//        Assert.assertEquals(res.getStatus(), "success");
//        Assert.assertNotNull(res.getData());
//    }

//    @Test(description = "validating error response for a get request")
//    public void sampleGetTest() throws Exception {
//
//        //Normally client would be located in some sort of base class
//        final Client client = new Client();
//
//        //Actual GET is implemented within Client
//        final ErrorResponse response = client.getToken();
//
//        //Build expected results
//        final ErrorResponse expected = buildErrorResponse("error",
//                "invalid username or password",
//                "401-UNAUTHORIZED",
//                "",
//                "piui.authFailure");
//
//        //Validation of actual response
//        ErrorAsserts.assertEquals(response, expected);
//    }
}