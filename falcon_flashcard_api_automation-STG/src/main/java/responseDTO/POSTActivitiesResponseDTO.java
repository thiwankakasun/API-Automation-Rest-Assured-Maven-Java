package responseDTO;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class POSTActivitiesResponseDTO {

    private String mastery;
    private String message;
    private String recommendation;
    private GetRecommendationResponseDTO description;
    private FieldErrorsDTO[] fieldErrors;
    private Date timestamp;
    private String status;
    private String error;
    private String path;
}
