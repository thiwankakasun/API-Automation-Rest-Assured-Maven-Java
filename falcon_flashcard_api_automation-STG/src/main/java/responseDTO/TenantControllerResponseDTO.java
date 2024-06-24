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
public class TenantControllerResponseDTO {

    private String tenantName;
    private String tenantId;
    private TenantKeysResponseDTO[] TenantKeys;
    private int maxDeckLimit;
    private int maxQuestionLimit;
    private boolean validateNotBlank;
    private boolean validatePassport;
    private boolean shouldProvisionExpertDecks;
    private boolean shouldRemoveExpertDecksUnpublished;
    private String creatorId;
    private String updaterId;
    private String description;
    private String message;
    private FieldErrorsDTO[] fieldErrors;
    Date createdAt;
    Date updatedAt;
    private Date timestamp;
    private String status;
    private String error;
    private String path;

}
