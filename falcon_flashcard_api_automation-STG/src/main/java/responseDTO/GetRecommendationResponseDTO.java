package responseDTO;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetRecommendationResponseDTO {

    private String person;
    private String deck;
    private String mastery;
    private String trackingUUID;
    private String played;
    private SessionDTO session;
    private String context;
    private String contextType;
    private GroupsDTO[] groups;
    private CardsDTO[] cards;
    private String description;
    private String resourceId;
    private String message;
    private FieldErrorsDTO[] fieldErrors;
    private Date timestamp;
    private String status;
    private String error;
    private String path;
}
