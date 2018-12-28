package cm.trixobase.telephone.domain.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cm.trixobase.telephone.R;

public class MenuContact extends Fragment {

    public static MenuContact newInstance() {
        return new MenuContact();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu_contact, container, false);
    }

}