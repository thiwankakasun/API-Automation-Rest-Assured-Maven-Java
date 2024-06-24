package responseDTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeckResponseDTO {

    private String id;
    private String title;
    private String userId;
    private String bookId;
    private String productId;
    private String chapterId;
    private String bookOrProductId;
    private String sectionId;
    private int noOfCards;
    private String createdAt;
    private String updatedAt;
    private String userCreatedAt;
    private String userModifiedAt;
    private boolean archived;
    private String deckType;
    private boolean deleted;
    private String parentDeckId;
    private String description;
    private String message;
    private FieldErrorsDTO[] fieldErrors;
    private FieldErrorsDTO[] objectErrors;
    private DeckActivityDTO deckActivity;
    private Date timestamp;
    private String status;
    private String error;
    private String path;

}
