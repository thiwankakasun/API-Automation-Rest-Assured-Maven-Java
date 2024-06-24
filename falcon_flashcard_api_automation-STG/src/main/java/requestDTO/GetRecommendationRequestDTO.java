package requestDTO;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import responseDTO.FieldErrorsDTO;

import java.util.Date;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetRecommendationRequestDTO {

    private String deck;
    private String person;
    private String description;
    private String message;
    private FieldErrorsDTO[] fieldErrors;
    private Date timestamp;
    private String status;
    private String error;
    private String path;
}
