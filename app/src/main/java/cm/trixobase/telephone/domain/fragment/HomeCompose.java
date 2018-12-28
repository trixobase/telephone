package cm.trixobase.telephone.domain.fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cm.trixobase.telephone.R;
import cm.trixobase.telephone.Telephone;
import cm.trixobase.telephone.domain.ui.UiContact;
import cm.trixobase.telephone.systeme.manager.PermissionManager;
import cm.trixobase.telephone.systeme.media.PhoneProcess;

public class HomeCompose extends Fragment {

    private static List<UiContact> contacts;
    private static String number;
    private static Activity instanceActivity;
    private static EditText et_number;
    private ImageButton delete, one, two, three, four, five, six, seven, height, nine, zero, star, diez, launcher;
    private TextView tv_erase;

    public static HomeCompose newInstance(Activity activity, List<UiContact> contactsList) {
        instanceActivity = activity;
        contacts = new ArrayList<>();
        contacts.addAll(contactsList);
        return new HomeCompose();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_compose, null);
        number = "";
        
        et_number = view.findViewById(R.id.et_number_id);
        tv_erase = view.findViewById(R.id.tv_contact);
        delete = view.findViewById(R.id.ib_delete);

        tv_erase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    number = "";
                    refreshEditText();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    number = number.substring(0, number.length()-1);
                } catch (Exception e) {
                    Log.w(Telephone.Log, "HomeCompose, Error: " + e);
                } finally {
                    refreshEditText();
                }
            }
        });
        one = view.findViewById(R.id.ib_one);
        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addValue("1");
            }
        });
        two = view.findViewById(R.id.ib_two);
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addValue("2");
            }
        });
        three = view.findViewById(R.id.ib_three);
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addValue("3");
            }
        });
        four = view.findViewById(R.id.ib_four);
        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addValue("4");
            }
        });
        five = view.findViewById(R.id.ib_five);
        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addValue("5");
            }
        });
        six = view.findViewById(R.id.ib_six);
        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addValue("6");
            }
        });
        seven = view.findViewById(R.id.ib_seven);
        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addValue("7");
            }
        });
        height = view.findViewById(R.id.ib_height);
        height.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addValue("8");
            }
        });
        nine = view.findViewById(R.id.ib_nine);
        nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addValue("9");
            }
        });
        zero = view.findViewById(R.id.ib_zero);
        zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addValue("0");
            }
        });
        star = view.findViewById(R.id.ib_star);
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addValue("*");
            }
        });
        diez = view.findViewById(R.id.ib_diez);
        diez.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addValue("#");
            }
        });
        launcher = view.findViewById(R.id.ib_call);
        launcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number = et_number.getText().toString();
                checkPermission();
            }
        });

        et_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                String number = et_number.getText().toString().trim();
                if (number.length() > 0) {
                    tv_erase.setVisibility(View.VISIBLE);
                    delete.setVisibility(View.VISIBLE);
                } else {
                    delete.setVisibility(View.INVISIBLE);
                    tv_erase.setVisibility(View.INVISIBLE);
                }
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
    public void onResume() {
        super.onResume();
        number = number.startsWith("#") || number.startsWith("*") ? "" : number;
        refreshEditText();
    }

    private void addValue(String value) {
        number = number.concat(value);
        refreshEditText();
    }

    private void refreshEditText() {
        String numberToShow = number;
        int numberSize = number.length();
        if (numberSize > 13)
            numberToShow = numberToShow.substring(numberSize-13, numberToShow.length());
        et_number.setText(numberToShow);
    }

    private static void checkPermission() {
        if (PermissionManager.builder(instanceActivity).withView(et_number).checkRequiredPermissions(Manifest.permission.CALL_PHONE))
            call();
    }

    private static void call() {
        if (number.length() > 0)
            PhoneProcess.launchCall(instanceActivity, number);
    }

}
