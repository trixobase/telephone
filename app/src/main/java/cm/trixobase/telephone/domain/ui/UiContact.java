package cm.trixobase.telephone.domain.ui;

/**
 * Created by noumianguebissie on 12/19/18.
 */

import android.support.annotation.NonNull;

import cm.trixobase.telephone.common.AttributeName;
import cm.trixobase.telephone.UiDomainObject;
import cm.trixobase.telephone.systeme.manager.Manager;

public class UiContact extends UiDomainObject implements Comparable<UiContact> {

    public boolean isChecked;

    @Override
    public int compareTo(@NonNull UiContact contact) {
        return this.getName().compareTo(contact.getName());
    }

    public static class Builder extends UiDomainObject.Builder<UiContact> {

        @Override
        protected UiContact newInstance() {
            return new UiContact();
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getName() {
        return getData().getAsString(AttributeName.Contact_Name);
    }

    public String getSex() {
        return getData().getAsString(AttributeName.Contact_Sex);
    }

    public String getDisplayName() {
        return Manager.compute.contactName(getName());
    }

    public String getNumber() {
        return getData().getAsString(AttributeName.Contact_Number);
    }

    public String getOperator() {
        if (getData().containsKey(AttributeName.Contact_Operator)) {
            String operator = getData().getAsString(AttributeName.Contact_Operator);
            if (operator == null || operator.isEmpty())
                return "";
            else return operator;
        }
        return getData().getAsString(AttributeName.Contact_Operator);
    }

    public String getEmail() {
        return getData().getAsString(AttributeName.Contact_Email);
    }

    public String getTown() {
        return getData().getAsString(AttributeName.Contact_Town);
    }

    public String getCountry() {
        return getData().getAsString(AttributeName.Contact_Country);
    }

    public int getStatus() {
        return getData().getAsInteger(AttributeName.Contact_Status);
    }

    public UiContact setName(String name) {
        getData().put(AttributeName.Contact_Name, name);
        return this;
    }

    public UiContact setSex(String sex) {
        getData().put(AttributeName.Contact_Sex, sex);
        return this;
    }

    public UiContact setNumber(String number) {
        getData().put(AttributeName.Contact_Number, number);
        return this;
    }

    public UiContact setEmail(String email) {
        getData().put(AttributeName.Contact_Email, email);
        return this;
    }

    public UiContact setOperator(String operator) {
        getData().put(AttributeName.Contact_Operator, operator);
        return this;
    }

    public UiContact setTown(String town) {
        getData().put(AttributeName.Contact_Town, town);
        return this;
    }

    public UiContact setCountry(String country) {
        getData().put(AttributeName.Contact_Country, country);
        return this;
    }

    public UiContact setAsFavorite() {
        getData().put(AttributeName.Contact_Status, 1);
        return this;
    }

    public UiContact setAsNotFavorite() {
        getData().put(AttributeName.Contact_Status, 0);
        return this;
    }

    public boolean isFavorite() {
        return getData().getAsInteger(AttributeName.Contact_Status) == 1;
    }

    public void setChecked(boolean state) {
        isChecked = state;
    }

    public boolean isChecked() {
        return isChecked;
    }


}