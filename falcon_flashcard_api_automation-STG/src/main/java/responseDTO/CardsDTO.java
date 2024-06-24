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
public class CardsDTO {

    private String cardId;
    private String cardType;
    private int group;
    private String cardTypes[];
    Date lastPlayed;
    private String preferences[];


}
