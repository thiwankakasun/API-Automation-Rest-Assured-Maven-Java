package requestDTO;

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
public class DeckRequestDTO {

    private String id;
    private String title;
    private String userId;
    private String bookId;
    private String productId;
    private String chapterId;
    private String sectionId;
    private String bookOrProductId;
    private int noOfCards;
    Date createdAt;
    Date updatedAt;
    Date userCreatedAt;
    Date userModifiedAt;
    private boolean archived;
    private String deckType;
    private boolean deleted;
    private String parentDeckId;

}
