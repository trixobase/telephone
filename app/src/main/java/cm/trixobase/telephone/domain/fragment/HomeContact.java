package cm.trixobase.telephone.domain.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cm.trixobase.telephone.R;
import cm.trixobase.telephone.domain.ui.UiContact;
import cm.trixobase.telephone.systeme.view.ListViewContactAdapter;

@SuppressLint("ValidFragment")
public class HomeContact extends Fragment {

    private static Activity instanceActivity;
    private static List<UiContact> contacts;
    private static ListView lv_contact;
    private static TextView tv_contact;

    private EditText et_search;
    private ListViewContactAdapter myAdapter;

    public static HomeContact newInstance(Activity activity, List<UiContact> contactsList) {
        contacts = new ArrayList<>();
        contacts.addAll(contactsList);
        instanceActivity = activity;
        return new HomeContact();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_contact, null);

        lv_contact = view.findViewById(R.id.lv_phone_contact_id);
        tv_contact = view.findViewById(R.id.tv_phone_contact_id);
        et_search = view.findViewById(R.id.et_phone_search_id);

        myAdapter = new ListViewContactAdapter(instanceActivity,  new ArrayList());
        lv_contact.setAdapter(myAdapter);

        if (contacts.isEmpty())
            tv_contact.setText(getResources().getString(R.string.label_text_contact_empty));
        else {
            tv_contact.setText("");
            Collections.sort(contacts);
        }
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                myAdapter.getFilter().filter(arg0);

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {

            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        et_search.setText("");
        new ListViewAsyncTask().execute();
    }

    public class ListViewAsyncTask extends AsyncTask<Void, UiContact, UiContact> {

        private ListViewContactAdapter adapter;
        private List<UiContact> contactList = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            adapter = (ListViewContactAdapter) lv_contact.getAdapter();
            contactList = contacts;
        }

        @Override
        protected UiContact doInBackground(Void... voids) {
            for (UiContact item : contactList) {
                publishProgress(item);
            }
            return null;
        }

        protected void onProgressUpdate(UiContact... item) {
            adapter.add(item[0]);
        }

        @Override
        protected void onPostExecute(UiContact result) {
            super.onPostExecute(result);
        }
    }

}