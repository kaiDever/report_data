package xinmei.report.data.utils;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class DateUtil {

    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    public static Comparator<List<Object>> comparator = (list1, list2) -> {
        try {
            Date date1 = sdf.parse(list1.get(0).toString().replace("/", "-"));
            Date date2 = sdf.parse(list2.get(0).toString().replace("/", "-"));
            return date1.compareTo(date2);
        } catch (Exception e) {
            throw new RuntimeException("日期转换失败，String->Date");
        }
    };

    public static String coverTimeZone(String date, Boolean utcToUtc8Flag) {
        SimpleDateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH");
        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        SimpleDateFormat utc8Format = new SimpleDateFormat("yyyy-MM-dd'T'HH");
        utc8Format.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        try {
            if (utcToUtc8Flag) {
                Date utcDate = utcFormat.parse(date);
                return utc8Format.format(utcDate);
            } else {
                Date utc8Date = utc8Format.parse(date);
                return utcFormat.format(utc8Date);
            }
        } catch (Exception e) {
            throw new RuntimeException("时区日期转换失败");
        }
    }
}
