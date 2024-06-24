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
public class DeckControllerResponseDTO {

    private DeckResponseDTO[] decks;
    private String description;
    private String message;
    private FieldErrorsDTO[] fieldErrors;
    private Date timestamp;
    private String status;
    private String error;
    private String path;

}
