package cm.trixobase.telephone.systeme.media;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import cm.trixobase.telephone.MainActivity;
import cm.trixobase.telephone.R;
import cm.trixobase.telephone.Telephone;
import cm.trixobase.telephone.common.AttributeName;
import cm.trixobase.telephone.domain.fragment.HomeFavours;
import cm.trixobase.telephone.systeme.manager.Manager;

/**
 * Created by noumianguebissie on 12/21/18.
 */

public class DialogDelete extends Dialog {

    private static Context instanceContext;
    private static String favorite_to_delete;

    private DialogDelete(@NonNull Context context) {
        super(context);
    }

    public static class Builder {

        private Builder(Context context) {
            instanceContext = context;
        }

        public Builder withFavoriteName(String favoriteNameToDelete) {
            favorite_to_delete = favoriteNameToDelete;
            return this;
        }

        public DialogDelete build() {
            return new DialogDelete(instanceContext);
        }

    }

    public static Builder builder(Context context) {
        return new Builder(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.my_dialog_delete);
        setCancelable(true);

        TextView message = findViewById(R.id.tv_delete_message_id);
        message.setText(String.format(instanceContext.getString(R.string.dialog_message_delete), favorite_to_delete));


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
                MainActivity.refreshFavoriteView();
                dismiss();
            }
        });
    }

}
