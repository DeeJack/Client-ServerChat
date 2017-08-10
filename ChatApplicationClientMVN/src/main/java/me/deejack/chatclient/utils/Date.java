package me.deejack.chatclient.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author DeeJack
 */
public class Date {
    private ZonedDateTime dateTime;

    public Date() {
        dateTime = ZonedDateTime.now();
    }

    public Date(String date) {
        try {
            dateTime = ZonedDateTime.ofInstant(new SimpleDateFormat("d/MM/yy HH:mm").parse(date).toInstant(), ZoneId.systemDefault());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public int getYear() {
        return dateTime.getYear();
    }

    public Month getMonth() {
        return dateTime.getMonth();
    }

    public int dayOfYear() {
        return dateTime.getDayOfYear();
    }

    @Override
    public String toString() {
        return dateTime.format(DateTimeFormatter.ofPattern("d/MM/yy HH:mm"));
    }
}
