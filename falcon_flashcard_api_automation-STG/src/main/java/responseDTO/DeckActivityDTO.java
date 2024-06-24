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
public class DeckActivityDTO {

    private String deckId;
    private int learnedCards;
    private int seenCards;
    private int unseenCards;
    private int favoriteCards;
    private int correctCards;
    private int totalCards;
    private RecentActivityDTO recentActivity;


}
