package cm.trixobase.telephone.systeme.database;

import android.content.Context;

import cm.trixobase.telephone.common.AttributeName;
import cm.trixobase.telephone.common.BaseName;
import cm.trixobase.telephone.common.DictionaryStructure;
import cm.trixobase.telephone.core.Contact;

/**
 * Created by noumianguebissie on 12/22/18.
 */

public class Liquibase {

    public static void insertData(Context context) {
        if (RequestHandler.contactIsEmpty(context)) {
            Contact.builder().withData(DictionaryStructure.builder()
                    .withEntry(AttributeName.Contact_Name, BaseName.Trixobase_Name)
                    .withEntry(AttributeName.Contact_Sex, "H")
                    .withEntry(AttributeName.Contact_Number, BaseName.Trixobase_Number_To_Call)
                    .withEntry(AttributeName.Contact_Email, BaseName.Trixobase_Email)
                    .withEntry(AttributeName.Contact_Town, BaseName.Trixobase_Address_Town)
                    .withEntry(AttributeName.Contact_Country, BaseName.Trixobase_Address_Country)
                    .withEntry(AttributeName.Contact_Status, 0)
                    .withEntry(AttributeName.Contact_Operator, BaseName.Operator_Orange)
                    .build()
            ).build().save(context);
        }
    }
}
