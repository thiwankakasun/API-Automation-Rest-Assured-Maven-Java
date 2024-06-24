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
public class ExpertQuestionResponseDTO {

    private ExpertQuestionDTO question;
    private String kind;
    private String creatorId;
    private String deckId;
    private String creatoredType;
    private String creatorPlatform;
    private String creatoredSource;
    private epochTimeDTO epochTime;
    private String questionType;
    private String userCreatedAt;
    private String userModifiedAt;
    private String createdAt;
    private String updatedAt;
    private AnswersDTO[] answers;
    private int[] correctAnswers;
    private String id;
    private boolean archived;
    private boolean deleted;
    private boolean favourite;
    private String description;
    private String message;
    private FieldErrorsDTO[] fieldErrors;
    private Date timestamp;
    private String error;
    private String path;
    private String tenantId;
    private String tenantKey;
    private String status;

}
