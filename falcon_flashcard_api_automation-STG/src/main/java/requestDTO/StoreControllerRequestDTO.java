package requestDTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import responseDTO.*;

import java.util.Date;
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class StoreControllerRequestDTO {

    private String id;
    private String title;
    private String subjectId;
    private BookRequestDTO book;
    private purchaseInfoRequestDTO purchaseInfo;
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
    private extendedPropertiesDTO extendedProperties;


}
