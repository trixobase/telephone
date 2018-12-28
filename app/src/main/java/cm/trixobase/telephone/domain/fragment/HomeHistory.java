package cm.trixobase.telephone.domain.fragment;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cm.trixobase.telephone.R;
import cm.trixobase.telephone.domain.ui.UiCall;
import cm.trixobase.telephone.systeme.manager.PermissionManager;
import cm.trixobase.telephone.systeme.media.PhoneProcess;
import cm.trixobase.telephone.systeme.view.ListViewCallAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeHistory extends Fragment {

    private static Activity instanceActivity;
    private static Context instanceContext;
    private static List<UiCall> calls;
    private static boolean showAllCalls = true;

    private static Toolbar toolbar;
    private static Button allCalls, missedCalls;
    private static ListView lv_calls;
    private static TextView tv_call_empty;

    public static HomeHistory newInstance(Activity activity, Toolbar tool) {
        instanceActivity = activity;
        instanceContext = activity.getBaseContext();
        toolbar = tool;
        return new HomeHistory();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contain = inflater.inflate(R.layout.fragment_home_history, null);

        allCalls = contain.findViewById(R.id.bt_history_call_all_id);
        missedCalls = contain.findViewById(R.id.bt_history_call_missing_id);
        lv_calls = contain.findViewById(R.id.lv_history_id);
        tv_call_empty = contain.findViewById(R.id.tv_history_empty_id);

        allCalls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    showAllCalls = true;
                    refreshView();
            }
        });
        missedCalls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    showAllCalls = false;
                    refreshView();
                }
            }
        });

        return contain;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshView();
    }

    public static void refreshView() {
        String text;
        if (showAllCalls) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                allCalls.setBackground(instanceContext.getResources().getDrawable(R.drawable.sc_call_all_active));
                missedCalls.setBackground(instanceContext.getResources().getDrawable(R.drawable.sc_call_missing_not_active));
                allCalls.setTextColor(instanceContext.getResources().getColor(R.color.colorPrimaryDark));
                missedCalls.setTextColor(instanceContext.getResources().getColor(R.color.colorAccent));
            }
            calls = getAllCalls();
            text = instanceContext.getString(R.string.label_text_all_call_empty);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                allCalls.setBackground(instanceContext.getResources().getDrawable(R.drawable.sc_call_all_not_active));
                missedCalls.setBackground(instanceContext.getResources().getDrawable(R.drawable.sc_call_missing_active));
                allCalls.setTextColor(instanceContext.getResources().getColor(R.color.colorAccent));
                missedCalls.setTextColor(instanceContext.getResources().getColor(R.color.colorPrimaryDark));
            }
            calls = getMissingCalls();
            text = instanceContext.getString(R.string.label_text_missing_call_empty);
        }

        if (calls.isEmpty())
            tv_call_empty.setText(text);
         else {
            tv_call_empty.setText("");
            Collections.sort(calls);
            calls = reverse(calls);
        }

        lv_calls.setAdapter(new ListViewCallAdapter(instanceActivity, calls));
    }

    private static List<UiCall> getAllCalls() {
        List<UiCall> allCalls = new ArrayList<>();
        if (PermissionManager.builder(instanceActivity).withView(toolbar).checkRequiredPermissions(Manifest.permission.WRITE_CALL_LOG)
                && PermissionManager.builder(instanceActivity).withView(toolbar).checkRequiredPermissions(Manifest.permission.WRITE_CONTACTS))
            allCalls = PhoneProcess.getCallDetails(instanceActivity);
        return allCalls;
    }

    private static List<UiCall> getMissingCalls() {
        List<UiCall> missingCalls = new ArrayList<>();
        for (UiCall call : getAllCalls()) {
            if (call.isMissing())
                missingCalls.add(call);
        }
        return missingCalls;
    }

    private static List<UiCall> reverse(List<UiCall> list) {
        List<UiCall> result = new ArrayList<>();
        for (int i = list.size() - 1; i >= 0; i--)
            result.add(list.get(i));
        return result;
    }

}

