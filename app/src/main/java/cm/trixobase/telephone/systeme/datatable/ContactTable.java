package cm.trixobase.telephone.systeme.datatable;

import android.database.sqlite.SQLiteDatabase;

import cm.trixobase.telephone.common.ColumnName;
import cm.trixobase.telephone.common.TableName;

/**
 * Created by noumianguebissie on 12/19/18.
 */

public class ContactTable {

    private static final String TABLE_CREATE_CONTACT = "create table " + TableName.CONTACT + "("
            + ColumnName.ID + " integer primary key autoincrement, "
            + ColumnName.CONTACT_NAME + " text, "
            + ColumnName.CONTACT_SEX + " text, "
            + ColumnName.CONTACT_NUMBER + " text not null, "
            + ColumnName.CONTACT_OPEARTOR + " text, "
            + ColumnName.CONTACT_EMAIL + " text, "
            + ColumnName.CONTACT_TOWN + " text, "
            + ColumnName.CONTACT_STATUS + " integer not null, "
            + ColumnName.CONTACT_COUNTRY + " text);";

    public static void createIn(SQLiteDatabase database) {
        database.execSQL(TABLE_CREATE_CONTACT);
    }

}