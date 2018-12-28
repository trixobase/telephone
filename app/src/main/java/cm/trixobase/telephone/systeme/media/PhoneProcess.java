package cm.trixobase.telephone.systeme.media;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cm.trixobase.telephone.R;
import cm.trixobase.telephone.Telephone;
import cm.trixobase.telephone.common.AttributeName;
import cm.trixobase.telephone.core.Contact;
import cm.trixobase.telephone.domain.ui.UiCall;
import cm.trixobase.telephone.domain.ui.UiContact;
import cm.trixobase.telephone.systeme.manager.Manager;

/**
 * Created by noumianguebissie on 12/19/18.
 */

public class PhoneProcess {

    private static Context context;
    public static final String Call_Incoming = "INCOMING";
    public static final String Call_Outgoing = "OUTGOING";
    public static final String Call_Missed = "MISSED";

    public static List<UiContact> getAllContacts(Context context, List<UiContact> favorites) {
        Cursor phones = null;
        ContentResolver contentResolver = context.getContentResolver();
        List<String> allNumbers = new ArrayList<>();
        for (UiContact favorite : favorites) {
            allNumbers.add(favorite.getNumber());
        }
        try {
            phones = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
            while (phones.moveToNext()) {
                String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String number = Manager.phoneNumber.formatNumber(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));

                if (canAdd(allNumbers, number)) {
                    favorites.add(UiContact.builder().build()
                            .setName(name.isEmpty() ? context.getResources().getString(R.string.label_text_unknown) : name)
                            .setSex("H")
                            .setNumber(number)
                            .setOperator(Manager.phoneNumber.getOperatorOf(number))
                            .setEmail("")
                            .setTown("")
                            .setCountry("")
                            .setAsNotFavorite());
                    allNumbers.add(number);
                }
            }
        } catch (Exception e) {
            Log.e(Telephone.Log, "PhoneProcess.getAllContacts - " + e);
        } finally {
            Log.e(Telephone.Log, "all contacts retrieved: " + phones.getCount());
            phones.close();
        }

        return favorites;
    }

    public static void saveContactToSim(Context context, String name, String phoneNumber) {
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();
        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, "com.android.sim")
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, "SIM").build());

        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name).build());
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                .build()); // Asking the Contact provider to create a new contact
        try {
            context.getContentResolver().applyBatch("com.android.contacts", ops);
            Log.e(Telephone.Log, "Contact: " + name + ", saving in SIM with phoneNumber as: " + phoneNumber);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.toString());
            Log.e(Telephone.Log, "Error saving phoneNumber in SIM: " + e);
        }
    }

    @SuppressLint("MissingPermission")
    public static void launchCall(Activity activity, String phoneNumber) {
        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + Uri.encode(phoneNumber)));
        activity.startActivity(callIntent);
    }

    @SuppressLint("MissingPermission")
    public static List<UiCall> getCallDetails(Activity activity) {
        context = activity.getBaseContext();
        List<UiCall> calls = new ArrayList<>();
        Cursor phones = null;
        try {
            phones = activity.getContentResolver().query(CallLog.Calls.CONTENT_URI, null,
                    null, null, null);
            phones.moveToFirst();
            while (!phones.isAfterLast()) {
                calls.add(cursorToCall(phones));
                phones.moveToNext();
            }
        } catch (Exception e) {
            Log.e(Telephone.Log, "PhoneProcess.getCallDetails - " + e);
        } finally {
            Log.e(Telephone.Log, "all calls retrieved: " + phones.getCount());
            if (phones != null)
                phones.close();
        }

        return calls;
    }

    private static boolean canAdd(List<String> allNumbers, String number) {
        if (number.isEmpty()) {
            return false;
        }
        if (allNumbers.contains(number)) {
            return false;
        }
        return true;
    }

    private static UiCall cursorToCall(Cursor cursor) {
        ContentValues data = new ContentValues();
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.DATE))));

            String date = Manager.date.getDate(calendar);
            String hour = Manager.time.getTime(calendar);
            String phoneNumber = Manager.phoneNumber.formatNumber(cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.NUMBER)));

            data.put(AttributeName.Caller_Name, Contact.getNameByNumber(context, phoneNumber));
            data.put(AttributeName.Caller_Number, phoneNumber);
            data.put(AttributeName.Caller_Date, date);
            data.put(AttributeName.Caller_Hour, hour);
            data.put(AttributeName.Caller_Duration, cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.DURATION)));
            data.put(AttributeName.Caller_Type, getCallingType(cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.TYPE))));
        } catch (Exception e) {
            Log.e(Telephone.Log, "Fail to convert cursor to call: " + e);
        }
        return (UiCall) UiCall.builder().withData(data).build();
    }

    private static String getCallingType(String callingType) {
        switch (Integer.parseInt(callingType)) {
            case CallLog.Calls.OUTGOING_TYPE:
                return Call_Outgoing;

            case CallLog.Calls.INCOMING_TYPE:
                return Call_Incoming;

            case CallLog.Calls.MISSED_TYPE:
                return Call_Missed;
            default:
                return "Unknown";
        }
    }

}
