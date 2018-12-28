package cm.trixobase.telephone.systeme.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cm.trixobase.telephone.DomainObject;
import cm.trixobase.telephone.Telephone;
import cm.trixobase.telephone.common.AttributeName;
import cm.trixobase.telephone.common.ColumnName;
import cm.trixobase.telephone.common.TableName;
import cm.trixobase.telephone.core.Contact;
import cm.trixobase.telephone.domain.ui.UiContact;

/**
 * Created by noumianguebissie on 12/19/18.
 */

public abstract class RequestHandler extends DomainObject {

    private static Cursor cursor;

    /**
     * To add a Where clause:
     * String selection= COLUMN_NAME + " = ? ";
     * String[] selectionArg=new String[] {"blond"};
     */

    public static boolean contactIsEmpty(Context context) {
        boolean answer = true;
        startTransaction(context);
        try {
            cursor = instanceDatabase.query(TableName.CONTACT, null, null, null, null, null, null, null);
            answer = cursor.getCount() == 0;
        } catch (Exception e) {
            Log.e(Telephone.Log, "contactIsEmpty - " + e);
        } finally {
            stopTransaction();
            Log.e(Telephone.Log, "Contact Present: " + answer + ", all contacts retrieved: " + cursor.getCount());
        }
        return answer;
    }

    protected static List<UiContact> getAllContacts() {
        List<UiContact> allContacts = new ArrayList<>();
        List<String> allNumbers = new ArrayList<>();
        String orderBy = ColumnName.CONTACT_NAME;

        try {
            cursor = instanceDatabase.query(TableName.CONTACT, null, null, null, null, null, orderBy, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                UiContact contact = cursorToContact(cursor);
                if (!allNumbers.contains(contact.getNumber())) {
                    allContacts.add(contact);
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            Log.e(Telephone.Log, "RequestHandler.getAllContacts - " + e);
        } finally {
            Log.e(Telephone.Log, "all contacts retrieved in database: " + cursor.getCount());
            cursor.close();
        }
        return allContacts;
    }

    protected RequestHandler() {
        super();
    }

    private static UiContact cursorToContact(Cursor cursor) {
        ContentValues data = new ContentValues();
        try {
            data.put(AttributeName.Id, cursor.getLong(cursor.getColumnIndexOrThrow(ColumnName.ID)));
            data.put(AttributeName.Contact_Name, cursor.getString(cursor.getColumnIndexOrThrow(ColumnName.CONTACT_NAME)));
            data.put(AttributeName.Contact_Sex, cursor.getString(cursor.getColumnIndexOrThrow(ColumnName.CONTACT_SEX)));
            data.put(AttributeName.Contact_Number, cursor.getString(cursor.getColumnIndexOrThrow(ColumnName.CONTACT_NUMBER)));
            data.put(AttributeName.Contact_Operator, cursor.getString(cursor.getColumnIndexOrThrow(ColumnName.CONTACT_OPEARTOR)));
            data.put(AttributeName.Contact_Email, cursor.getString(cursor.getColumnIndexOrThrow(ColumnName.CONTACT_NAME)));
            data.put(AttributeName.Contact_Town, cursor.getString(cursor.getColumnIndexOrThrow(ColumnName.CONTACT_TOWN)));
            data.put(AttributeName.Contact_Country, cursor.getString(cursor.getColumnIndexOrThrow(ColumnName.CONTACT_COUNTRY)));
            data.put(AttributeName.Contact_Status, cursor.getInt(cursor.getColumnIndexOrThrow(ColumnName.CONTACT_STATUS)));
        } catch (Exception e) {
            Log.e(Telephone.Log, "Fail to convert cursor to contact: " + e);
        }
        return (UiContact) UiContact.builder().withData(data).build();
    }

}
