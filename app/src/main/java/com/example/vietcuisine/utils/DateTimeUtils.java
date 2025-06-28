package com.example.vietcuisine.utils;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {
    public static String formatToVietnamTime(String isoTime) {
        try {
            OffsetDateTime utcDateTime = OffsetDateTime.parse(isoTime);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            return utcDateTime
                    .atZoneSameInstant(ZoneId.of("Asia/Ho_Chi_Minh"))
                    .format(formatter);
        } catch (Exception e) {
            e.printStackTrace();
            return "Không rõ thời gian";
        }
    }
}

