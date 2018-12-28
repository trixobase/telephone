package cm.trixobase.telephone.domain.fragment;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import cm.trixobase.telephone.MainActivity;
import cm.trixobase.telephone.R;
import cm.trixobase.telephone.Telephone;
import cm.trixobase.telephone.common.AttributeName;
import cm.trixobase.telephone.domain.Favorites;
import cm.trixobase.telephone.domain.ui.UiContact;
import cm.trixobase.telephone.systeme.manager.Manager;
import cm.trixobase.telephone.systeme.manager.PermissionManager;
import cm.trixobase.telephone.systeme.media.DialogCustom;
import cm.trixobase.telephone.systeme.view.ListViewFavoritesAdapter;

public class HomeFavours extends Fragment {

    private static Activity instanceActivity;
    private static Context context;
    public static List<UiContact> favourites;

    private static TextView tv_operator, tv_favours_empty, tv_boy_name, tv_boy_number;
    private static ListView lv_favorites;

    public static HomeFavours newInstance(Activity activity, List<UiContact> contactsFavorites) {
        instanceActivity = activity;
        context = activity;
        favourites = contactsFavorites;
        return new HomeFavours();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_favours, null);

        TextView tv_slogan = view.findViewById(R.id.tv_favours_slogan_id);
        tv_operator = view.findViewById(R.id.tv_operator_id);
        LinearLayout ll_call_credit = view.findViewById(R.id.ll_favours_call_credit_id);
        LinearLayout ll_call_boy = view.findViewById(R.id.ll_favours_id);
        tv_boy_name = view.findViewById(R.id.tv_favours_boy_name_id);
        tv_boy_number = view.findViewById(R.id.tv_favours_boy_number_id);
        LinearLayout ll_setting = view.findViewById(R.id.ll_setting_id);
        Button bt_add_favour = view.findViewById(R.id.bt_favours_add_id);
        lv_favorites = view.findViewById(R.id.lv_favorites_contacts_id);
        tv_favours_empty = view.findViewById(R.id.tv_favours_contact_empty_id);
        ImageView iv_boy = view.findViewById(R.id.iv_boy_id);
        iv_boy.setImageResource(R.drawable.iv_boy);

        String slogan;
        String today = Manager.date.getDate(Calendar.getInstance());
        if (today.equalsIgnoreCase(Manager.getData(context, AttributeName.Key_Slogan_Date, ""))) {
            slogan = Manager.getData(context, AttributeName.Key_Slogan, "");
        } else {
            slogan = Telephone.getRandomSlogan(context.getResources());
            Manager.saveData(context, AttributeName.Key_Slogan_Date, today);
            Manager.saveData(context, AttributeName.Key_Slogan, slogan);
        }
        tv_slogan.setText(slogan);

        ll_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                instanceActivity.showDialog(MainActivity.DIALOG_BOX_SETTING_OPERATOR);
                DialogCustom.refresh();
            }
        });

        ll_call_credit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.contactNumberToCall = Manager.getData(context, AttributeName.Key_Setting_Operator_Code, Telephone.Operator_Camtel_Code);
                launchCall();
            }
        });

        ll_call_boy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.contactNumberToCall = Telephone.boy_number;
                launchCall();
            }
        });

        bt_add_favour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (favourites.size() >= 5)
                    Manager.showToastMessage(context, lv_favorites, String.format(getString(R.string.error_favorite_add), Telephone.koko_name));
                else {
                    Manager.saveData(context, AttributeName.Key_Present_View, "favours");
                    Intent intent = new Intent(context, Favorites.class);
                    intent.putExtra(AttributeName.Key_Favorites_Saved_Size, favourites.size());
                    startActivity(intent);
                }
            }
        });

        refreshView();

        return view;
    }

    public static void refreshView() {
        String operator = Manager.getData(context, AttributeName.Key_Setting_Operator_Code, Telephone.Operator_Camtel_Code);
        switch (operator) {
            case Telephone.Operator_Camtel_Code:
                tv_operator.setText(context.getString(R.string.label_text_operator_camtel));
                break;
            case Telephone.Operator_Mtn_Code:
                tv_operator.setText(context.getString(R.string.label_text_operator_mtn));
                break;
            case Telephone.Operator_Nexttel_Code:
                tv_operator.setText(context.getString(R.string.label_text_operator_nexttel));
                break;
            case Telephone.Operator_Orange_Code:
                tv_operator.setText(context.getString(R.string.label_text_operator_orange));
                break;
        }

        if (favourites.isEmpty())
            tv_favours_empty.setText(context.getString(R.string.label_text_favorites_empty));
        else {
            tv_favours_empty.setText("");
            lv_favorites.setAdapter(new ListViewFavoritesAdapter(instanceActivity, favourites));
        }

        tv_boy_name.setText(Telephone.boy_name);
        tv_boy_number.setText(Telephone.boy_display_number);

    }

    private void launchCall() {
        if(PermissionManager.builder(instanceActivity).withView(tv_operator).checkRequiredPermissions(Manifest.permission.CALL_PHONE))
            MainActivity.call();
    }

}
