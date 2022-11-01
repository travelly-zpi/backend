package pwr.edu.pl.travelly.core.common.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DateUtils {
    public static boolean isInPresentOrFuture(final LocalDate date){
        return date.isEqual(LocalDate.now()) || date.isAfter(LocalDate.now());
    }

    public static boolean validateIfEndIsAfterBegin(final LocalDate beginDate, final LocalDate endDate){
        return beginDate.isAfter(endDate);
    }

    public static boolean validateIfEndIsAfterBegin(final LocalDateTime beginTime, final LocalDateTime endTime){
        return beginTime.isAfter(endTime);
    }
}
