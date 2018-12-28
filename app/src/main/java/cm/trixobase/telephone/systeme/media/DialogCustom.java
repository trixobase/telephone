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

import cm.trixobase.telephone.R;
import cm.trixobase.telephone.Telephone;
import cm.trixobase.telephone.common.AttributeName;
import cm.trixobase.telephone.domain.fragment.HomeFavours;
import cm.trixobase.telephone.systeme.manager.Manager;

/**
 * Created by noumianguebissie on 9/29/18.
 */

public class DialogCustom extends Dialog {

    private static Context instanceContext;
    private static String operatorCodeSelected;

    private static RadioGroup rg_operator;

    private DialogCustom(@NonNull Context context) {
        super(context);
    }

    public static class Builder {

        private static Context context;

        private Builder(Context context) {
            this.context = context;
        }

        public DialogCustom build() {
            return new DialogCustom(context);
        }

    }

    public static Builder builder(Context context) {
        instanceContext = context;
        return new Builder(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.my_dialog_custom);
        setCancelable(true);

        rg_operator = findViewById(R.id.rg_operator_id);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.r_camtel_id:
                        operatorCodeSelected = Telephone.Operator_Camtel_Code;
                        break;
                    case R.id.r_mtn_id:
                        operatorCodeSelected = Telephone.Operator_Mtn_Code;
                        break;
                    case R.id.r_nexttel_id:
                        operatorCodeSelected = Telephone.Operator_Nexttel_Code;
                        break;
                    case R.id.r_orange_id:
                        operatorCodeSelected = Telephone.Operator_Orange_Code;
                        break;
                }
            }
        };
        for (int i = 0; i < rg_operator.getChildCount(); i++) {
            RadioButton radio = (RadioButton) rg_operator.getChildAt(i);
            radio.setOnClickListener(listener);
        }

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
                Manager.saveData(instanceContext, AttributeName.Key_Setting_Operator_Code, operatorCodeSelected);
                HomeFavours.refreshView();
                dismiss();
            }
        });

        refresh();

    }

    public static void refresh() {
        operatorCodeSelected = Manager.getData(instanceContext, AttributeName.Key_Setting_Operator_Code, Telephone.Operator_Camtel_Code);
        switch (operatorCodeSelected) {
            case Telephone.Operator_Mtn_Code:
                rg_operator.findViewById(R.id.r_mtn_id).setSelected(true);
                break;
            case Telephone.Operator_Orange_Code:
                rg_operator.findViewById(R.id.r_orange_id).setSelected(true);
                break;
            case Telephone.Operator_Nexttel_Code:
                rg_operator.findViewById(R.id.r_nexttel_id).setSelected(true);
                break;
            case Telephone.Operator_Camtel_Code:
                rg_operator.findViewById(R.id.r_camtel_id).setSelected(true);
                break;
        }
    }

}
