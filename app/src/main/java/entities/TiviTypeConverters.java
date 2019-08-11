package entities;

import androidx.room.TypeConverter;

import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.format.DateTimeFormatter;

/**
 * Used to convert between time formats for SQLite, which doesn't support timezones.
 */
public class TiviTypeConverters {
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    @TypeConverter
    public OffsetDateTime toOffsetDateTime(String dateTimeString) {
        if (dateTimeString == null) {
            return null;
        }
        return dateTimeFormatter.parse(dateTimeString, OffsetDateTime.FROM);
    }

    @TypeConverter
    public String fromOffsetDateTime(OffsetDateTime offsetDateTime) {
        if (offsetDateTime == null) {
            return null;
        }
        return offsetDateTime.format(dateTimeFormatter);
    }
}
