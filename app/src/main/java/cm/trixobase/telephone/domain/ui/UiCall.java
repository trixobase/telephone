package cm.trixobase.telephone.domain.ui;

import android.support.annotation.NonNull;

import cm.trixobase.telephone.UiDomainObject;
import cm.trixobase.telephone.common.AttributeName;
import cm.trixobase.telephone.systeme.manager.Manager;
import cm.trixobase.telephone.systeme.media.PhoneProcess;

/**
 * Created by noumianguebissie on 12/19/18.
 */

public class UiCall extends UiDomainObject implements Comparable<UiCall> {

    @Override
    public int compareTo(@NonNull UiCall newCall) {
        return Manager.date.getDate(newCall.getDate(), newCall.getHour()).compareTo(Manager.date.getDate(getDate(), getHour()));
    }

    public static class Builder extends UiDomainObject.Builder<UiCall> {

        @Override
        protected UiCall newInstance() {
            return new UiCall();
        }
    }

    public static UiCall.Builder builder() {
        return new UiCall.Builder();
    }

    public String getType() {
        return getData().getAsString(AttributeName.Caller_Type);
    }

    public String getName() {
        return getData().getAsString(AttributeName.Caller_Name);
    }

    public String getNumber() {
        return getData().getAsString(AttributeName.Caller_Number);
    }

    public String getDate() {
        return getData().getAsString(AttributeName.Caller_Date);
    }

    public String getHour() {
        return getData().getAsString(AttributeName.Caller_Hour);
    }

    public String getDuration() {
        return getData().getAsString(AttributeName.Caller_Duration);
    }

    public boolean isMissing() {
        return PhoneProcess.Call_Missed.equalsIgnoreCase(getData().getAsString(AttributeName.Caller_Type));
    }

}
