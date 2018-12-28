package cm.trixobase.telephone.systeme.media;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import cm.trixobase.telephone.MainActivity;
import cm.trixobase.telephone.R;
import cm.trixobase.telephone.common.AttributeName;
import cm.trixobase.telephone.common.DictionaryStructure;
import cm.trixobase.telephone.core.Contact;
import cm.trixobase.telephone.systeme.manager.Manager;

/**
 * Created by noumianguebissie on 12/22/18.
 */

public class DialogContact extends Dialog {

    private static Context instanceContext;

    private static EditText et_number;

    private DialogContact(@NonNull Context context) {
        super(context);
    }

    public static class Builder {

        private Builder(Context context) {
            instanceContext = context;
        }

        public DialogContact build() {
            return new DialogContact(instanceContext);
        }

    }

    public static Builder builder(Context context) {
        return new Builder(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.my_dialog_contact);

        setCancelable(true);

        final EditText et_name, et_surname, et_email, et_town, et_country;
        et_name = findViewById(R.id.et_form_name_id);
        et_surname = findViewById(R.id.et_form_surname_id);
        et_number = findViewById(R.id.et_form_number_id);
        et_email = findViewById(R.id.et_form_email_id);
        et_town = findViewById(R.id.et_form_town_id);
        et_country = findViewById(R.id.et_form_country_id);

        Button bt_cancel = findViewById(R.id.bt_dialog_no_id);
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        Button bt_validate = findViewById(R.id.bt_dialog_yes_id);
        bt_validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_number.getText().toString().isEmpty())
                    showMessage(instanceContext.getString(R.string.error_contact_save_number));
                else {
                    String name = et_name.getText().toString() + " " + et_surname.getText().toString();
                    String number = et_number.getText().toString();
                    long result = Contact.builder().withData(DictionaryStructure.builder()
                            .withEntry(AttributeName.Contact_Name, name)
                            .withEntry(AttributeName.Contact_Number, number)
                            .withEntry(AttributeName.Contact_Email, et_email.getText().toString())
                            .withEntry(AttributeName.Contact_Town, et_town.getText().toString())
                            .withEntry(AttributeName.Contact_Country, et_country.getText().toString())
                            .withEntry(AttributeName.Contact_Status, 0)
                            .withEntry(AttributeName.Contact_Operator, Manager.phoneNumber.getOperatorOf(number))
                            .build()).build().save(instanceContext);
                    if (result == -1)
                        showMessage(instanceContext.getString(R.string.error_contact_save));
                    else {
                        PhoneProcess.saveContactToSim(instanceContext, name, number);
                        showMessage(instanceContext.getString(R.string.success_contact_saved));
                        MainActivity.refreshView();
                    }
                    dismiss();
                }
            }
        });
    }

    private static void showMessage(String message) {
        Manager.showToastMessage(instanceContext, et_number, message);
    }

}
