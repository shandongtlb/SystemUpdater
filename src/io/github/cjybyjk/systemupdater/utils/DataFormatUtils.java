package io.github.cjybyjk.systemupdater.utils;

import android.content.Context;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import android.util.Base64;

public class DataFormatUtils {

    private static final String TAG = "DataFormatter";

    /**
     * FormatFileSize
     *
     * @param fileS FileSize(bytes)
     * @return String: Formatted FileSize
     */
    public static String FormatFileSize(double fileS) {
        int FILE_SIZE_KB = 1024;
        int FILE_SIZE_MB = 1048576;
        int FILE_SIZE_GB = 1073741824;

        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < FILE_SIZE_KB) {
            fileSizeString = decimalFormat.format(fileS) + "B";
        } else if (fileS < FILE_SIZE_MB) {
            fileSizeString = decimalFormat.format(fileS / FILE_SIZE_KB) + "KB";
        } else if (fileS < FILE_SIZE_GB) {
            fileSizeString = decimalFormat.format(fileS / FILE_SIZE_MB) + "MB";
        } else {
            fileSizeString = decimalFormat.format(fileS / FILE_SIZE_GB) + "GB";
        }
        return fileSizeString;
    }

    public static String base64Decode(String source) {
        return new String(Base64.decode(source.getBytes(), Base64.DEFAULT));
    }

    public static String getTimeLocalized(Context context, long unixTimestamp) {
        DateFormat f = DateFormat.getTimeInstance(DateFormat.SHORT, getCurrentLocale(context));
        Date date = new Date(unixTimestamp * 1000);
        return f.format(date);
    }

    public static String getTimeLocalizedUTC(Context context, long unixTimestamp) {
        DateFormat f = DateFormat.getTimeInstance(DateFormat.SHORT, getCurrentLocale(context));
        f.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = new Date(unixTimestamp * 1000);
        return f.format(date);
    }

    public static String getDateLocalized(Context context, int dateFormat, long unixTimestamp) {
        DateFormat f = DateFormat.getDateInstance(dateFormat, getCurrentLocale(context));
        Date date = new Date(unixTimestamp * 1000);
        return f.format(date);
    }

    public static String getDateLocalizedUTC(Context context, int dateFormat, long unixTimestamp) {
        DateFormat f = DateFormat.getDateInstance(dateFormat, getCurrentLocale(context));
        f.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = new Date(unixTimestamp * 1000);
        return f.format(date);
    }

    public static String getDateTimeLocalized(Context context, long unixTimestamp) {
        DateFormat f = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT,
                getCurrentLocale(context));
        Date date = new Date(unixTimestamp * 1000);
        return f.format(date);
    }

    public static String getDateTimeLocalizedUTC(Context context, long unixTimestamp) {
        DateFormat f = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT,
                getCurrentLocale(context));
        f.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = new Date(unixTimestamp * 1000);
        return f.format(date);
    }

    public static Locale getCurrentLocale(Context context) {
        return context.getResources().getConfiguration().locale;
    }

    public static String getFileNameFromURL(String url) {
        String tFileName = url;
        int tInstr = tFileName.indexOf("?");
        if (tInstr != -1) {
            tFileName = tFileName.substring(0, tInstr);
        }
        tInstr = tFileName.lastIndexOf("/");
        if (tInstr != -1) {
            tFileName = tFileName.substring(tInstr);
        }
        return tFileName;
    }

}
