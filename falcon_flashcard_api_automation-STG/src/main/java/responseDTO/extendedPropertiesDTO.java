package responseDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class extendedPropertiesDTO {
    private String pageId1;
    private String pageId2;
    private String pageId3;
    private String pageId4;
    private String pageId5;
}
