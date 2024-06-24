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
public class GetActivitiesOfDecksResponseDTO {

    private DeckActivityDTO[] deckActivity;
    private String message;
    private String description;
    private String resourceId;
    private FieldErrorsDTO[] fieldErrors;
    private Date timestamp;
    private String status;
    private String error;
    private String path;
}
