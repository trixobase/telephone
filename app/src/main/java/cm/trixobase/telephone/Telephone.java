package cm.trixobase.telephone;

import android.content.res.Resources;

import java.util.Calendar;

import cm.trixobase.telephone.systeme.manager.Manager;
import cm.trixobase.telephone.systeme.manager.Random;

/**
 * Created by noumianguebissie on 12/19/18.
 */

public class Telephone {

    /*
    Application Variables
     */

    public static final String koko_name = "Anne";
    public static final String koko_name_all = "Anne Noumia";
    public static final String boy_name = "Didier Noumia";
    public static final String boy_display_number = "+1 (438) 884-1053";
    public static final String boy_number = "+14388841053";

    /*
    Application Picture:
        profile (nav bar)           :   iv_koko
                (favorites)         :   iv_boy
        background Menu -> About    :   iv_koko_about
        start button                :   iv_koko_star
        diez button                 :   iv_koko_diez
     */

    public static final String Log = "TELEPHONE WARNING: ";

    public static final int Min_Contact_To_Select = 1;
    public static final int Max_Favorites = 5;

    public static final String Operator_Camtel_Code = "*825*1#";
    public static final String Operator_Mtn_Code = "*155#";
    public static final String Operator_Nexttel_Code = "*801#";
    public static final String Operator_Orange_Code = "#123#";

    public static String getRandomProverb(Resources resources) {
        return new Random<>(
                resources.getString(R.string.app_proverb_1),
                resources.getString(R.string.app_proverb_2),
                resources.getString(R.string.app_proverb_3),
                resources.getString(R.string.app_proverb_4),
                resources.getString(R.string.app_proverb_5),
                resources.getString(R.string.app_proverb_6),
                resources.getString(R.string.app_proverb_7),
                resources.getString(R.string.app_proverb_8),
                resources.getString(R.string.app_proverb_9),
                resources.getString(R.string.app_proverb_10),
                resources.getString(R.string.app_proverb_11),
                resources.getString(R.string.app_proverb_12),
                resources.getString(R.string.app_proverb_13),
                resources.getString(R.string.app_proverb_14),
                resources.getString(R.string.app_proverb_15),
                resources.getString(R.string.app_proverb_16),
                resources.getString(R.string.app_proverb_17),
                resources.getString(R.string.app_proverb_18),
                resources.getString(R.string.app_proverb_19),
                resources.getString(R.string.app_proverb_20)
        ).build();
    }

    public static String getRandomSlogan(Resources resources) {
        return new Random<>(
                String.format(resources.getString(R.string.slogan_best), koko_name),
                String.format(resources.getString(R.string.slogan_good_day), koko_name),
                String.format(resources.getString(R.string.slogan_think), koko_name),
                String.format(resources.getString(R.string.slogan_love_me), koko_name),
                String.format(resources.getString(R.string.slogan_paradis), koko_name),
                String.format(resources.getString(R.string.slogan_sunday), koko_name),
                String.format(resources.getString(R.string.slogan_noel), koko_name),
                String.format(resources.getString(R.string.slogan_name), koko_name),
                String.format(resources.getString(R.string.slogan_tapioca), koko_name),
                String.format(resources.getString(R.string.slogan_place), koko_name),
                getMorning(resources)
        ).build();
    }

    private static String getMorning(Resources resources) {
        int hour = Manager.time.getHour(Manager.time.getTime(Calendar.getInstance()));
        if (hour < 15)
            return resources.getString(R.string.slogan_morning);
        else return resources.getString(R.string.slogan_evening);
    }

}
