package requestDTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import responseDTO.FieldErrorsDTO;

import java.util.Date;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TenantControllerRequestDTO {

    private String tenantName;
    private String tenantId;
    private TenantKeysRequestDTO[] TenantKeys;
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

}
