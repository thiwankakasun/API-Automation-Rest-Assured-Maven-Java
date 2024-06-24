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
//@JsonIgnoreProperties(ignoreUnknown = true)
public class StoreControllerResponseDTO {

    private String id;
    private String title;
    private String subjectId;
    private BookDTO book;
    private purchaseInfoDTO purchaseInfo;
    private notificationSettingsDTO notificationSettings;
    private epochTimeDTO epochTime;
    private String examDate;
    private String userId;
    Date createdAt;
    Date updatedAt;
    private boolean archived;
    private boolean cardPreview;
    private int noOfCards;
    private int progress;
    private int downloads;
    private String status;
    private boolean starred;
    private examReminderDTO examReminder;
    private String language;
    private int mastery;
    private shareDetailsDTO shareDetails;
    private String deckType;
    private boolean viewed;
    private boolean pilot;
    private boolean expert;
    private boolean deleted;
    private String description;
    private String message;
    private FieldErrorsDTO[] fieldErrors;
    private Date timestamp;
    private String error;
    private String path;
    private String[] keywords;
    private String tempDeckId;
    private String categoryId;
    private String thumbnailUrl;
    private String[] tags;
    private String parentDeckId;
    private String deckAuthor;
    private String deckAuthorId;
    private extendedPropertiesDTO extendedProperties;


}
