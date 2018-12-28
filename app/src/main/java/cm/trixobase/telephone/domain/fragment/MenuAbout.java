package cm.trixobase.telephone.domain.fragment;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cm.trixobase.telephone.R;
import cm.trixobase.telephone.Telephone;

public class MenuAbout extends Fragment {


    private static Context context;

    public static MenuAbout newInstance(Context newContext) {
        context = newContext;
        return new MenuAbout();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_about, null);

        final LinearLayout ll_plus = view.findViewById(R.id.ll_about_plus_id);
        final TextView tv_koko_name = view.findViewById(R.id.tv_about_koko_name_all_id);
        final ImageView iv_koko = view.findViewById(R.id.iv_about_koko_about_id);
        final Button bt_plus = view.findViewById(R.id.bt_about_plus_id);
        final ImageView iv_launcher = view.findViewById(R.id.imageView2);
        final TextView tv_trixo1 = view.findViewById(R.id.textView4);
        final TextView tv_trixo2 = view.findViewById(R.id.textView5);

        bt_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ll_plus.getVisibility() == View.INVISIBLE) {
                    ll_plus.setVisibility(View.VISIBLE);
                    bt_plus.setText("ok");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        iv_koko.setBackground(getContext().getResources().getDrawable(R.drawable.iv_koko_about));
                    }
                    iv_launcher.setVisibility(View.INVISIBLE);
                    tv_trixo1.setVisibility(View.INVISIBLE);
                    tv_trixo2.setVisibility(View.INVISIBLE);
                } else {
                    ll_plus.setVisibility(View.INVISIBLE);
                    bt_plus.setText("Plus");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        iv_koko.setBackgroundColor(getContext().getResources().getColor(R.color.colorAccent));
                    }
                    iv_launcher.setVisibility(View.VISIBLE);
                    tv_trixo1.setVisibility(View.VISIBLE);
                    tv_trixo2.setVisibility(View.VISIBLE);
                }

            }
        });
        tv_koko_name.setText(Telephone.koko_name_all);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

}
