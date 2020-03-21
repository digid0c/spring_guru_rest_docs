package guru.samples.rest.docs.web.mapper;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.OffsetDateTime;

import static java.sql.Timestamp.valueOf;
import static java.time.ZoneOffset.UTC;
import static java.util.Optional.ofNullable;

@Component
public class DateMapper {

    public OffsetDateTime asOffsetDateTime(Timestamp timestamp) {
        return ofNullable(timestamp)
                .map(Timestamp::toLocalDateTime)
                .map(dateTime -> OffsetDateTime.of(
                        dateTime.getYear(),
                        dateTime.getMonthValue(),
                        dateTime.getDayOfMonth(),
                        dateTime.getHour(),
                        dateTime.getMinute(),
                        dateTime.getSecond(),
                        dateTime.getNano(),
                        UTC
                ))
                .orElse(null);
    }

    public Timestamp asTimestamp(OffsetDateTime offsetDateTime) {
        return ofNullable(offsetDateTime)
                .map(dateTime -> valueOf(dateTime.atZoneSameInstant(UTC).toLocalDateTime()))
                .orElse(null);
    }
}
