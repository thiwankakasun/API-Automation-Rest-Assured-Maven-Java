package utils;

import java.time.Instant;

public class GetCurrentTime {
    public static String getCurrentTimestamp() {
        long epochSeconds = Instant.now().getEpochSecond();
        return "" + epochSeconds;
    }
}
