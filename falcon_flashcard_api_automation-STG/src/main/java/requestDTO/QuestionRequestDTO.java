package requestDTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import responseDTO.extendedPropertiesDTO;

import java.util.Date;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuestionRequestDTO {

    private String correlationId;
    private String questionMedia;
    private String question;
    private String kind;
    private String creatorId;
    private String deckId;
    private String type;
    private String platform;
    private String source;
    Date userCreatedAt;
    private AnswersRequestDTO[] answers;
    private boolean favourite;
    private boolean archived;
    private boolean deleted;
    private extendedPropertiesDTO extendedProperties;

}
