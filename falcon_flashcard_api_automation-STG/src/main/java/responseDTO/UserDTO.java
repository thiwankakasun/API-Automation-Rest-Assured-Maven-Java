package responseDTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.USE_DEFAULTS)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO {

    private String status;
    private String data;

}
