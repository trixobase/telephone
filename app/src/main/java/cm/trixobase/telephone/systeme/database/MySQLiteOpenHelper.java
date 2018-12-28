package cm.trixobase.telephone.systeme.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import cm.trixobase.telephone.Telephone;
import cm.trixobase.telephone.common.TableName;
import cm.trixobase.telephone.systeme.datatable.ContactTable;

/**
 * Created by noumianguebissie on 12/19/18.
 */

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "telephone.db";
    private static final int DATABASE_VERSION = 1;
    private static MySQLiteOpenHelper instance;
    protected static SQLiteDatabase instanceDatabase;
    protected static Context instanceContext;

    protected MySQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    protected static void startTransaction(Context context) {
        if (instance == null) {
            instance = new MySQLiteOpenHelper(context);
            instanceContext = context;
        }
        try {
            if (instanceDatabase == null || !instanceDatabase.isOpen()) {
                instanceDatabase = instance.getReadableDatabase();
                instanceDatabase.beginTransaction();
            }
        } catch (Exception e) {
            Log.e(Telephone.Log, " Erreur lors de l'ouverture de la BD: " + e);
        }
    }

    protected static void stopTransaction() {
        try {
            if (instanceDatabase.inTransaction()) {
                instanceDatabase.setTransactionSuccessful();
                instanceDatabase.endTransaction();
            }
        } catch (Exception e) {
            Log.e(Telephone.Log, "Erreur en fin de transaction de la BD: " + e);
        }
        try {
            if (instanceDatabase != null && instanceDatabase.isOpen())
                instanceDatabase.close();
        } catch (Exception e) {
            Log.e(Telephone.Log, "Erreur lors de la fermeture de la BD: " + e);
        }
    }

    protected static void forceStopTransaction() {
        try {
            if (instanceDatabase != null && instanceDatabase.isOpen()) {
                instanceDatabase.close();
                Log.e(Telephone.Log, "Fermeture forcee de la BD avec succes");
            }
        } catch (Exception e) {
            Log.e(Telephone.Log, "Erreur lors de la fermeture forcee de la BD: " + e);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        ContactTable.createIn(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.w(MySQLiteOpenHelper.class.getName(),
                "Upgrading instanceDatabase from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        instanceDatabase.execSQL("DROP TABLE IF EXISTS " + TableName.CONTACT);
        onCreate(instanceDatabase);
    }

}

