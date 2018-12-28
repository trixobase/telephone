package cm.trixobase.telephone;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cm.trixobase.telephone.common.AttributeName;
import cm.trixobase.telephone.core.Contact;
import cm.trixobase.telephone.domain.fragment.HomeCompose;
import cm.trixobase.telephone.domain.fragment.MenuAbout;
import cm.trixobase.telephone.domain.fragment.MenuContact;
import cm.trixobase.telephone.domain.fragment.MenuTerm;
import cm.trixobase.telephone.domain.ui.UiContact;
import cm.trixobase.telephone.domain.fragment.HomeContact;
import cm.trixobase.telephone.domain.fragment.HomeFavours;
import cm.trixobase.telephone.domain.fragment.HomeHistory;
import cm.trixobase.telephone.systeme.database.Liquibase;
import cm.trixobase.telephone.systeme.manager.Manager;
import cm.trixobase.telephone.systeme.manager.PermissionManager;
import cm.trixobase.telephone.systeme.media.DialogContact;
import cm.trixobase.telephone.systeme.media.DialogCustom;
import cm.trixobase.telephone.systeme.media.DialogDelete;
import cm.trixobase.telephone.systeme.media.PhoneProcess;
import cm.trixobase.telephone.systeme.view.ListViewFavoritesAdapter;

public class MainActivity extends AppCompatActivity {

    public static UiContact contact;
    private static List<UiContact> contacts = new ArrayList<>();
    private static List<UiContact> favorites = new ArrayList<>();
    public static String favoriteNameToDelete = "";
    public static String contactNumberToCall = "";
    private static boolean canClose = true;
    public static String viewToShowAfterBack = "favours";
    public static String presentView = "";
    private static int position = 0;

    private static DrawerLayout drawer;
    private static Context context;
    private static Activity activity;
    private static Toolbar toolbar;
    private static FragmentManager fragmentManager;
    private static BottomNavigationView bottomNavigationView;

    public static final int DIALOG_BOX_SETTING_OPERATOR = 1;
    public static final int DIALOG_BOX_DELETE_FAVORITE = 2;
    public static final int DIALOG_BOX_CONTACT_ADD = 3;
    public static ListViewFavoritesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        context = getBaseContext();
        contacts = new ArrayList<>();
        favorites = new ArrayList<>();

        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        checkPermission();

        fragmentManager = getSupportFragmentManager();
        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.ic_favors_id:
                                setFragmentFavours();
                                break;
                            case R.id.ic_recents_id:
                                setFragmentHistory();
                                break;
                            case R.id.ic_keyboard_id:
                                setFragmentCompose();
                                break;
                            case R.id.ic_contacts_id:
                                checkPermission();
                                setFragmentContacts();
                                break;
                        }
                        return true;
                    }
                });

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_contact:
                        setFragmentTrixo(presentView);
                        break;
                    case R.id.nav_utility:
                        setFragmentTerm(presentView);
                        break;
                    case R.id.nav_koko:
                        setFragmentAbout(presentView);
                        break;
                }

                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (presentView.isEmpty())
            presentView = Manager.getData(context, AttributeName.Key_Present_View, "favours");
        switch (presentView) {
            case "favours":
                setFragmentFavours();
                break;
            case "recents":
                setFragmentHistory();
                break;
            case "keyboard":
                setFragmentCompose();
                break;
            case "contacts":
                setFragmentContacts();
                break;
            case "trixo":
                setFragmentTrixo(presentView);
                break;
            case "terms":
                setFragmentTerm(presentView);
                break;
            case "about":
                setFragmentAbout(presentView);
                break;
        }
    }

    private static void setFragment(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.commit();
        toolbar.setSubtitle("");
    }

    private static void setFragmentFavours() {
        setFragment(HomeFavours.newInstance(activity, favorites));
        toolbar.setTitle(context.getString(R.string.label_title_favours));
        bottomNavigationView.getMenu().getItem(0).setChecked(true);
        presentView = "favours";
        canClose = true;
        viewToShowAfterBack = "";
        position = 0;
    }

    private static void setFragmentHistory() {
        setFragment(HomeHistory.newInstance(activity, toolbar));
        toolbar.setTitle(context.getString(R.string.label_title_recent));
        bottomNavigationView.getMenu().getItem(1).setChecked(true);
        presentView = "recents";
        canClose = true;
        viewToShowAfterBack = "";
        position = 1;
    }

    public static void setFragmentCompose() {
        setFragment(HomeCompose.newInstance(activity, contacts));
        toolbar.setTitle(context.getString(R.string.label_title_keyboard));
        bottomNavigationView.getMenu().getItem(2).setChecked(true);
        presentView = "keyboard";
        canClose = true;
        viewToShowAfterBack = "";
        position = 2;
    }

    public static void setFragmentContacts() {
        setFragment(HomeContact.newInstance(activity, contacts));
        toolbar.setTitle(context.getString(R.string.label_title_contact));
        bottomNavigationView.getMenu().getItem(3).setChecked(true);
        presentView = "contacts";
        canClose = true;
        viewToShowAfterBack = "";
        position = 3;
    }

    public static void setFragmentTrixo(String view) {
        setFragment(MenuContact.newInstance());
        toolbar.setTitle(context.getString(R.string.label_text_Menu));
        toolbar.setSubtitle(context.getString(R.string.action_contact));
        drawer.closeDrawer(GravityCompat.START);
        canClose = false;
        presentView = view;
        viewToShowAfterBack = view;
    }

    public static void setFragmentTerm(String view) {
        setFragment(MenuTerm.newInstance(activity));
        toolbar.setTitle(context.getString(R.string.label_text_Menu));
        toolbar.setSubtitle(context.getString(R.string.action_utility));
        drawer.closeDrawer(GravityCompat.START);
        presentView = view;
        canClose = false;
        viewToShowAfterBack = view;
    }

    public static void setFragmentAbout(String view) {
        setFragment(MenuAbout.newInstance(activity));
        toolbar.setTitle(context.getString(R.string.label_text_Menu));
        toolbar.setSubtitle(context.getString(R.string.action_about));
        drawer.closeDrawer(GravityCompat.START);
        presentView = view;
        canClose = false;
        viewToShowAfterBack = view;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PermissionManager.PERMISSIONS_REQUEST_CALL_PHONE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    call();
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && shouldShowRequestPermissionRationale(permissions[0]) == false) {
                    displayOptions();
                } else {
                    PermissionManager.explainPermission(activity, toolbar, Manifest.permission.CALL_PHONE);
                }
                break;
            case PermissionManager.PERMISSIONS_REQUEST_WRITE_CONTACTS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadData();
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && shouldShowRequestPermissionRationale(permissions[0]) == false) {
                    displayOptions();
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home_top, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_contact_id:
                showDialog(DIALOG_BOX_CONTACT_ADD);
                return true;
            case R.id.ic_menu_id:
                drawer.openDrawer(Gravity.START);
                TextView tv_kokoName = findViewById(R.id.nom_koko);
                tv_kokoName.setText(Telephone.koko_name);
                TextView tv_proverb = findViewById(R.id.proverbe);
                tv_proverb.setText(Telephone.getRandomProverb(getResources()));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Dialog onCreateDialog(int idBox) {
        switch (idBox) {
            case DIALOG_BOX_SETTING_OPERATOR:
                return DialogCustom.builder(this).build();
            case DIALOG_BOX_DELETE_FAVORITE:
                return DialogDelete.builder(this).withFavoriteName(favoriteNameToDelete).build();
            case DIALOG_BOX_CONTACT_ADD:
                return DialogContact.builder(this).build();
            default:
                return null;
        }
    }

    @Override
    public void onPrepareDialog(int id, Dialog box) {
        if (id == DIALOG_BOX_DELETE_FAVORITE) {
            TextView message = box.findViewById(R.id.tv_delete_message_id);
            message.setText(String.format(context.getString(R.string.dialog_message_delete), favoriteNameToDelete));
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(Gravity.START)) {
            drawer.closeDrawer(Gravity.START);
        } else if (!canClose) {
            switch (viewToShowAfterBack) {
                case "favours":
                    setFragmentFavours();
                    break;
                case "recents":
                    setFragmentHistory();
                    break;
                case "keyboard":
                    setFragmentCompose();
                    break;
                case "contacts":
                    setFragmentContacts();
                    break;
                default:

                    break;
            }
        } else {
            Manager.saveData(context, AttributeName.Key_Present_View, presentView);
            super.onBackPressed();
        }
    }

    private static void checkPermission() {
        if (PermissionManager.builder(activity).withView(toolbar).checkRequiredPermissions(Manifest.permission.WRITE_CONTACTS))
            loadData();
    }

    private static void loadData() {
        Liquibase.insertData(context);

        if (contacts.isEmpty()) {
            contacts = Contact.getAll(context);
            Collections.sort(contacts);

            favorites.clear();
            for (UiContact contact : contacts) {
                if (contact.isFavorite())
                    favorites.add(contact);
            }
        }
    }

    private void displayOptions() {
        Snackbar.make(toolbar, context.getString(R.string.error_permission_desactivate), Snackbar.LENGTH_INDEFINITE).setAction(context.getString(R.string.label_text_setting), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.fromParts("package", activity.getPackageName(), null));
                startActivity(intent);
            }
        }).show();
    }

    public static void doFinish() {
        if (activity != null)
            activity.finish();
    }

    public static void refreshFavoriteView() {
        adapter.refreshView();
    }

    public static void refreshView() {
        activity.recreate();
    }

    @SuppressLint("MissingPermission")
    public static void call() {
        PhoneProcess.launchCall(activity, contactNumberToCall);
    }

}
