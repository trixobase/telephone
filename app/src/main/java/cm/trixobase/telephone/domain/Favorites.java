package cm.trixobase.telephone.domain;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cm.trixobase.telephone.MainActivity;
import cm.trixobase.telephone.R;
import cm.trixobase.telephone.Telephone;
import cm.trixobase.telephone.common.AttributeName;
import cm.trixobase.telephone.core.Contact;
import cm.trixobase.telephone.domain.ui.UiContact;
import cm.trixobase.telephone.systeme.manager.Manager;
import cm.trixobase.telephone.systeme.manager.PermissionManager;
import cm.trixobase.telephone.systeme.view.ListViewContactFavoritesAdapter;

public class Favorites extends AppCompatActivity {

    private static Activity activity;
    private static Context context;
    public static ArrayList<String> favoritesNumbers;
    private static int favoritesSaved;
    public static List<UiContact> contacts;

    private static Toolbar toolbar;
    private static ListView listview;
    private  static ListViewContactFavoritesAdapter myAdapter;
    private static TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        activity = this;
        context = getBaseContext();
        contacts = new ArrayList<>();
        favoritesNumbers = new ArrayList<>();
        favoritesSaved = getIntent().getIntExtra(AttributeName.Key_Favorites_Saved_Size, 0);
        myAdapter  = new ListViewContactFavoritesAdapter(this, new ArrayList<UiContact>());

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.label_title_favours));
        getSupportActionBar().setSubtitle(getString(R.string.label_text_add));

        textView = findViewById(R.id.tv_phone_contact_id);
        listview = findViewById(R.id.lv_contact_favorite_id);
        listview.setAdapter(myAdapter);

        checkPermission();

        EditText et_search = findViewById(R.id.et_favorite_search_id);
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                myAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private static void checkPermission() {
        if (PermissionManager.builder(activity).withView(toolbar).checkRequiredPermissions(Manifest.permission.WRITE_CONTACTS)) {
            contacts = Contact.getAllNotFavorites(context);
            Collections.sort(contacts);

            myAdapter = new ListViewContactFavoritesAdapter(context, contacts);
            listview.setAdapter(myAdapter);
        }
        if (contacts.isEmpty())
            textView.setText(context.getString(R.string.label_text_favorite_empty));
        else textView.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ic_action_save_id:
                doSave();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PermissionManager.PERMISSIONS_REQUEST_WRITE_CONTACTS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    recreate();
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && shouldShowRequestPermissionRationale(permissions[0]) == false) {
                    displayOptions();
                } else {
                    PermissionManager.explainPermission(activity, toolbar, Manifest.permission.CALL_PHONE);
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void displayOptions() {
        Snackbar.make(toolbar, context.getString(R.string.error_permission_desactivate), Snackbar.LENGTH_LONG).setAction(context.getString(R.string.label_text_setting), new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                final Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                final Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        }).show();
    }

    private void doSave() {
        if (canSave()) {
            if (save() == -1) {
                showMessage(getResources().getString(R.string.error_favorite_save));
            } else {
                MainActivity.doFinish();
                Intent i =  new Intent(getBaseContext(), MainActivity.class);
                startActivity(i);
                finish();
                if (favoritesNumbers.size() == 1)
                    showMessage(getResources().getString(R.string.success_favorite_sigle_added));
                else showMessage(getResources().getString(R.string.success_favorite_several_added));
            }
        }
    }

    private boolean canSave() {
        if (favoritesNumbers.size() < Telephone.Min_Contact_To_Select) {
            showMessage(String.format(getResources().getString(R.string.error_favorite_save_min), Telephone.koko_name));
            return false;
        }
        if (favoritesNumbers.size() + favoritesSaved > Telephone.Max_Favorites) {
            showMessage(getResources().getString(R.string.error_favorite_save_max) + " " + Telephone.Max_Favorites + ", " + getResources().getString(R.string.label_text_actual) + ": " + (favoritesNumbers.size() + favoritesSaved));
            return false;
        }
        return true;
    }

    private long save() {
        List<ContentValues> datas = new ArrayList<>();
        for (UiContact contact : contacts) {
            for (String number : favoritesNumbers) {
                if (number.equalsIgnoreCase(contact.getNumber())) {
                    contact.setAsFavorite();
                    datas.add(contact.getData());
                }
            }
        }
        return Contact.saveFavorites(context, datas);
    }

    private void showMessage(String message) {
        Manager.showToastMessage(getBaseContext(), toolbar, message);
    }

}
