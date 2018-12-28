package cm.trixobase.telephone.domain.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cm.trixobase.telephone.R;

public class MenuTerm extends Fragment {


    private static Context context;

    public static MenuTerm newInstance(Context newContext) {
        context = newContext;
        return new MenuTerm();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_term, null);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

}
