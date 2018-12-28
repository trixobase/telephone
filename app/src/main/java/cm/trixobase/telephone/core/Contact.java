package cm.trixobase.telephone.core;

import android.content.ContentValues;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import cm.trixobase.telephone.DomainObject;
import cm.trixobase.telephone.R;
import cm.trixobase.telephone.common.AttributeName;
import cm.trixobase.telephone.common.ColumnName;
import cm.trixobase.telephone.common.TableName;
import cm.trixobase.telephone.domain.ui.UiContact;
import cm.trixobase.telephone.systeme.database.RequestHandler;
import cm.trixobase.telephone.systeme.media.PhoneProcess;

/**
 * Created by noumianguebissie on 12/19/18.
 */

public class Contact extends RequestHandler {

    @Override
    protected String getTableName() {
        return TableName.CONTACT;
    }

    public static class Builder extends DomainObject.Builder<Contact> {

        private Builder() {
            super();
        }

        @Override
        protected Contact newInstance() {
            return new Contact();
        }

        @Override
        public Builder withData(ContentValues newData) {
            if (newData.containsKey(AttributeName.Id))
                instance.id = newData.getAsLong(AttributeName.Id);
            instance.setName(newData.getAsString(AttributeName.Contact_Name))
                    .setSex(newData.getAsString(AttributeName.Contact_Sex))
                    .setNumber(newData.getAsString(AttributeName.Contact_Number))
                    .setOperator(newData.getAsString(AttributeName.Contact_Operator))
                    .setEmail(newData.getAsString(AttributeName.Contact_Email))
                    .setTown(newData.getAsString(AttributeName.Contact_Town))
                    .setCountry(newData.getAsString(AttributeName.Contact_Country))
                    .setStatus(newData.getAsInteger(AttributeName.Contact_Status));
            return this;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private Contact setName(String name) {
        this.name = name;
        return this;
    }

    private Contact setSex(String sex) {
        this.sex = sex;
        return this;
    }

    private Contact setNumber(String number) {
        this.number = number;
        return this;
    }

    private Contact setOperator(String operator) {
        this.operator = operator;
        return this;
    }

    private Contact setEmail(String email) {
        this.email = email;
        return this;
    }

    private Contact setTown(String town) {
        this.town = town;
        return this;
    }

    private Contact setCountry(String country) {
        this.country = country;
        return this;
    }

    private Contact setStatus(int state) {
        this.status = state;
        return this;
    }

    public static List<UiContact> getAll(Context context) {
        startTransaction(context);
        List<UiContact> contacts = PhoneProcess.getAllContacts(context, getAllContacts());
        stopTransaction();
        return contacts;
    }

    public static List<UiContact> getAllNotFavorites(Context context) {
        List<UiContact> contacts = getAll(context);
        List<UiContact> contactsToBeSelected = new ArrayList<>();
        for (UiContact contact : contacts) {
            if (!contact.isFavorite())
                contactsToBeSelected.add(contact);
        }
        return contactsToBeSelected;
    }

    public static String getNameByNumber(Context context, String phoneNumber) {
        String name = context.getString(R.string.label_text_unknown);
        for (UiContact contact : getAll(context)) {
            if (phoneNumber.equalsIgnoreCase(contact.getNumber())) {
                name = contact.getName();
                break;
            }
        }
        return name;
    }

    private String name;
    private String sex;
    private String number;
    private String operator;
    private String email;
    private String town;
    private String country;
    private int status;

    public long save(Context context) {
        getData().put(ColumnName.CONTACT_NAME, name);
        getData().put(ColumnName.CONTACT_SEX, sex);
        getData().put(ColumnName.CONTACT_NUMBER, number);
        getData().put(ColumnName.CONTACT_OPEARTOR, operator);
        getData().put(ColumnName.CONTACT_EMAIL, email);
        getData().put(ColumnName.CONTACT_TOWN, town);
        getData().put(ColumnName.CONTACT_COUNTRY, country);
        getData().put(ColumnName.CONTACT_STATUS, status);

        startTransaction(context);
        long result = -1;
        result = super.save();
        stopTransaction();

        return result;
    }

    public static long saveFavorites(Context context, List<ContentValues> datas) {
        long result = -1;
        for (ContentValues data : datas) {
            result = builder().withData(data).build().save(context);
            if (result == -1) {
                forceStopTransaction();
                break;
            }
        }
        return result;
    }

}

