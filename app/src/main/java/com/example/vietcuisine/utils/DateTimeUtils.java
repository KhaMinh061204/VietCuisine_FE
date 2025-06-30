package com.example.vietcuisine.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {
    public static String formatToVietnamTime(String isoOrMillis) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                    .withZone(ZoneId.of("Asia/Ho_Chi_Minh"));

            // Nếu là timestamp dạng số
            if (isoOrMillis.matches("\\d+")) {
                long millis = Long.parseLong(isoOrMillis);
                Instant instant = Instant.ofEpochMilli(millis);
                return formatter.format(instant);
            }

            // Nếu là chuỗi ISO
            OffsetDateTime utcDateTime = OffsetDateTime.parse(isoOrMillis);
            ZonedDateTime vietnamTime = utcDateTime.atZoneSameInstant(ZoneId.of("Asia/Ho_Chi_Minh"));
            return vietnamTime.format(formatter);

        } catch (Exception e) {
            e.printStackTrace();
            return "Không rõ thời gian";
        }
    }
}
