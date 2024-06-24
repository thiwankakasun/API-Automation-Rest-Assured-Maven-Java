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
public class QuestionDTO {

    private String correlationId;
    private String chapterId;
    private String questionMedia;
    private String question;
    private String kind;
    private String creatorId;
    private String deckId;
    private String type;
    private String platform;
    private String source;
    Date userCreatedAt;
    Date createdAt;
    Date updatedAt;
    Date userModifiedAt;
    private AnswersDTO[] answers;
    private boolean favourite;
    private boolean archived;
    private boolean deleted;
    private String id;
    private FieldErrorsDTO[] fieldErrors;
    private String message;
    private String description;
    private Date timestamp;
    private String status;
    private String error;
    private String path;
    private extendedPropertiesDTO extendedProperties;


}
