package cm.trixobase.telephone.systeme.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import cm.trixobase.telephone.R;
import cm.trixobase.telephone.common.BaseName;

/**
 * Created by noumianguebissie on 12/19/18.
 */

public class Manager {

    public static class phoneNumber {

        public static String formatNumber(String number) {
            String numberFormat = number
                    .replace("(", "")
                    .replace(")", "")
                    .replace("-", "")
                    .replace("_", "")
                    .replace(" ", "");

            if (numberFormat.startsWith("00"))
                numberFormat = numberFormat.substring(2, numberFormat.length());
            if (numberFormat.startsWith("237") && numberFormat.length() == 12)
                numberFormat = numberFormat.substring(3, numberFormat.length());
            if (numberFormat.startsWith("+237") && numberFormat.length() == 13)
                numberFormat = numberFormat.substring(4, numberFormat.length());
            return numberFormat;
        }

        public static String getOperatorOf(String number) {
            switch (number.length()) {
                case 9:
                    return findOperator(number);
                default:
                    return "";
            }
        }

        private static String findOperator(String number) {
            if (number.startsWith("2"))
                return BaseName.Operator_Camtel;
            if (number.startsWith("3"))
                return BaseName.Operator_Fix;
            if (number.startsWith("6")) {
                String suffix = number.substring(1, 3);
                if (suffix.startsWith("6"))
                    return BaseName.Operator_Nexttel;
                if (suffix.startsWith("7") || suffix.startsWith("8"))
                    return BaseName.Operator_Mtn;
                if (suffix.startsWith("9"))
                    return BaseName.Operator_Orange;
                switch (suffix) {
                    case "50":
                    case "55":
                    case "56":
                    case "57":
                        return BaseName.Operator_Orange;
                    case "51":
                    case "52":
                    case "53":
                    case "54":
                        return BaseName.Operator_Mtn;
                }
            }
            return "";
        }

    }

    public static class date {
        private static Calendar calendar = Calendar.getInstance();
        private static String dayDate = "";
        private static String dayCount = "";
        private static String month = "";
        private static String year = "";

        public static String getDate(Calendar calendar) {
            return calendarToDate(calendar);
        }

        public static Calendar getDate(String date) {
            return dateToCalendar(date);
        }

        public static String getDate(int dateCount, int dateMouth, int dateYear) {
            dayCount = formatNumber(String.valueOf(dateCount));
            month = formatNumber(String.valueOf(dateMouth + 1));
            year = String.valueOf(dateYear);
            return dayCount.concat("/").concat(month).concat("/").concat(year);
        }

        public static Calendar getDate(String date, String hour) {
            return dateToCalendar(date, hour);
        }

        public static Calendar getDateAfterAddMinute(String date, String beginHour, int minuteToAdd) {
            Calendar calendar = dateToCalendar(date);
            calendar.set(Calendar.HOUR_OF_DAY, time.getHour(beginHour));
            calendar.set(Calendar.MINUTE, time.getMinute(beginHour));
            calendar.add(Calendar.MINUTE, minuteToAdd);
            return calendar;
        }

        public static Calendar getDateAfterRemoveMinute(String date, String beginHour, int minuteToRemove) {
            Calendar calendar = dateToCalendar(date);
            calendar.set(Calendar.HOUR_OF_DAY, time.getHour(beginHour));
            calendar.set(Calendar.MINUTE, time.getMinute(beginHour));
            calendar.add(Calendar.MINUTE, -minuteToRemove);
            return calendar;
        }

        public static int computeDateTextColor(String date, String hour) {
            int hourActivity = Integer.valueOf(hour.substring(0, 2));
            int hourToday = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

            int color = 3;
            if (getDate(Calendar.getInstance()).equalsIgnoreCase(date)) {
                if ((hourActivity - hourToday) <= 3)
                    color = 2;
            }
            if (getDate(Calendar.getInstance()).equalsIgnoreCase(date)) {
                if ((hourActivity - hourToday) <= 1)
                    color = 1;
            }
            return color;
        }

        public static String getNextDateOf(String date) {
            calendar = dateToCalendar(date);
            calendar.add(Calendar.DATE, 1);
            return calendarToDate(calendar);
        }

        public static boolean dateIsPassed(String dateToTest, String beginHourToTest) {
            String dateInstance = date.getDate(Calendar.getInstance());
            String timeInstance = time.getTime(Calendar.getInstance());
            int todayYear = date.getYear(dateInstance);
            int todayMonth = date.getMonth(dateInstance);
            int todayDate = date.getDayCount(dateInstance);
            int todayHour = Integer.valueOf(timeInstance.substring(0, 2));
            int todayMinute = Integer.valueOf(timeInstance.substring(3, 5));

            if (todayYear > date.getYear(dateToTest))
                return true;
            if (todayYear == date.getYear(dateToTest)
                    && todayMonth > date.getMonth(dateToTest))
                return true;
            if (todayYear == date.getYear(dateToTest)
                    && todayMonth == date.getMonth(dateToTest)
                    && todayDate > date.getDayCount(dateToTest))
                return true;
            if (todayYear == date.getYear(dateToTest)
                    && todayMonth == date.getMonth(dateToTest)
                    && todayDate == date.getDayCount(dateToTest)
                    && todayHour > time.getHour(beginHourToTest))
                return true;
            return todayYear == date.getYear(dateToTest)
                    && todayMonth == date.getMonth(dateToTest)
                    && todayDate == date.getDayCount(dateToTest)
                    && todayHour == time.getHour(beginHourToTest)
                    && todayMinute > time.getMinute(beginHourToTest);
        }

        private static String calendarToDate(Calendar calendar) {
            String dayCount = formatNumber(String.valueOf(calendar.get(Calendar.DATE)));
            String month = formatNumber(String.valueOf(calendar.get(Calendar.MONTH) + 1));
            String year = formatNumber(String.valueOf(calendar.get(Calendar.YEAR)));
            return dayCount.concat("/").concat(month).concat("/").concat(year);
        }

        private static Calendar dateToCalendar(String date) {
            calendar.set(getYear(date), getMonth(date) - 1, getDayCount(date));
            return calendar;
        }

        private static Calendar dateToCalendar(String date, String hour) {
            calendar.set(getYear(date), getMonth(date) - 1, getDayCount(date), time.getHour(hour), time.getMinute(hour));
            return calendar;
        }

        private static String getDayName(String date, Resources Ress) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Integer.valueOf(date.substring(6, 10)),
                    Integer.valueOf(date.substring(3, 5)) - 1,
                    Integer.valueOf(date.substring(0, 2)));
            int newDate = calendar.get(Calendar.DAY_OF_WEEK);
            if (newDate == 1) {
                newDate = 8;
            }
            dayDate = formatNumber(String.valueOf(newDate - 1));
            switch (dayDate) {
                case "01":
                    return Ress.getString(R.string.label_day_lundi);
                case "02":
                    return Ress.getString(R.string.label_day_mardi);
                case "03":
                    return Ress.getString(R.string.label_day_mercredi);
                case "04":
                    return Ress.getString(R.string.label_day_jeudi);
                case "05":
                    return Ress.getString(R.string.label_day_vendredi);
                case "06":
                    return Ress.getString(R.string.label_day_samedi);
                case "07":
                    return Ress.getString(R.string.label_day_dimanche);
                default:
                    return "Error";
            }
        }

        public static int getDayCount(String date) {
            return Integer.valueOf(date.substring(0, 2));
        }

        private static String getMonthName(Resources Ress, String date) {
            switch (date.substring(3, 5)) {
                case "01":
                    return Ress.getString(R.string.label_month_jan);
                case "02":
                    return Ress.getString(R.string.label_month_fev);
                case "03":
                    return Ress.getString(R.string.label_month_mar);
                case "04":
                    return Ress.getString(R.string.label_month_avr);
                case "05":
                    return Ress.getString(R.string.label_month_may);
                case "06":
                    return Ress.getString(R.string.label_month_jun);
                case "07":
                    return Ress.getString(R.string.label_month_juil);
                case "08":
                    return Ress.getString(R.string.label_month_aou);
                case "09":
                    return Ress.getString(R.string.label_month_sep);
                case "10":
                    return Ress.getString(R.string.label_month_oct);
                case "11":
                    return Ress.getString(R.string.label_month_nov);
                case "12":
                    return Ress.getString(R.string.label_month_dec);
                default:
                    return "Error";
            }
        }

        public static int getMonth(String date) {
            return Integer.valueOf(date.substring(3, 5));
        }

        public static int getYear(String date) {
            return Integer.valueOf(date.substring(6, 10));
        }

    }

    public static class time {

        public static String getTime(int Hour, int Minute) {
            return formatNumber(String.valueOf(Hour)).concat(":").concat(formatNumber(String.valueOf(Minute)));
        }

        public static String getTime(Calendar calendar) {
            return formatNumber(String.valueOf(calendar.get(Calendar.HOUR_OF_DAY))).concat(":").concat(formatNumber(String.valueOf(calendar.get(Calendar.MINUTE))));
        }

        public static int getHour(String time) {
            return Integer.valueOf(time.substring(0, 2));
        }

        public static int getMinute(String time) {
            return Integer.valueOf(time.substring(3, 5));
        }

        public static String calendarToTime(Calendar calendar) {
            return formatNumber(String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)))
                    .concat(":").concat(formatNumber(String.valueOf(calendar.get(Calendar.MINUTE))));
        }

    }

    public static class compute {

        public static String contactName(String name) {
            String[] names = name.split(" ");
            switch (names.length) {
                case 0:
                case 1:
                    return name.trim();
                default:
                    return names[0].concat(" ").concat(names[1]).trim();
            }
        }

        public static String titleActivity(String name, String parenthesis) {
            if (name.contains(" "))
                name = name.substring(0, name.indexOf(" "));
            if (name.length() > 12)
                name = name.substring(0, 12).concat("..");
            return name.concat(" (").concat(parenthesis).concat(")");
        }

    }

    public static String formatName(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1, name.length());
    }

    public static String formatNumber(String value) {
        return (value.length() == 1) ? 0 + value : value;
    }

    public static void showToastMessage(Context context, View view, String message) {
        View layout = view.inflate(context, R.layout.my_toast, null);
        TextView text = layout.findViewById(R.id.text);
        text.setText(message);
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    public static void saveData(Context context, String key, String value) {
        SharedPreferences.Editor prefs = PreferenceManager.getDefaultSharedPreferences(context).edit();
        prefs.putString(key, value);
        prefs.apply();
    }

    public static void saveData(Context context, String key, int value) {
        SharedPreferences.Editor prefs = PreferenceManager.getDefaultSharedPreferences(context).edit();
        prefs.putInt(key, value);
        prefs.apply();
    }

    public static String getData(Context context, String key, String defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(key, defaultValue);
    }

    public static int getData(Context context, String key, int defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(key, defaultValue);
    }

}