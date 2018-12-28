package cm.trixobase.telephone.systeme.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cm.trixobase.telephone.R;
import cm.trixobase.telephone.common.AttributeName;
import cm.trixobase.telephone.domain.ui.UiCall;
import cm.trixobase.telephone.systeme.manager.PermissionManager;
import cm.trixobase.telephone.systeme.media.PhoneProcess;

/**
 * Created by noumianguebissie on 12/25/18.
 */

public class ListViewCallAdapter extends BaseAdapter {

    private static class ViewHolder {
        ImageView ic_callType;
        TextView tv_name, tv_number, tv_dateDay, tv_dateHour;
        LinearLayout layout_call;
    }

    private static Activity instanceActivity;
    private final Context context;
    private List<UiCall> callsList;
    private LayoutInflater inflater;

    public ListViewCallAdapter(Activity activity, List<UiCall> calls) {
        instanceActivity = activity;
        this.context = activity.getBaseContext();
        this.callsList = calls;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return callsList.size();
    }

    @Override
    public UiCall getItem(int position) {
        return callsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return callsList.indexOf(getItem(position));
    }

    public View getView(int position, View itemView, ViewGroup parent) {
        ViewHolder holder;
        if (itemView == null) {
            holder = new ViewHolder();
            itemView = inflater.inflate(R.layout.listview_call_item, null);

            holder.ic_callType = itemView.findViewById(R.id.iv_call_type_id);
            holder.tv_name = itemView.findViewById(R.id.tv_call_name_id);
            holder.tv_number = itemView.findViewById(R.id.tv_call_number_id);
            holder.tv_dateDay = itemView.findViewById(R.id.tv_call_date_day_id);
            holder.tv_dateHour = itemView.findViewById(R.id.tv_call_date_hour_id);
            holder.layout_call = itemView.findViewById(R.id.ll_contact_call_id);
            itemView.setTag(holder);
        } else holder = (ViewHolder) itemView.getTag();

        final UiCall callToDisplay = callsList.get(position);

        switch (callToDisplay.getType()) {
            case PhoneProcess.Call_Outgoing:
                holder.ic_callType.setImageResource(R.drawable.ic_call_outgoing);
                break;
            case PhoneProcess.Call_Incoming:
                holder.ic_callType.setImageResource(R.drawable.ic_call_incomming);
                break;
            case PhoneProcess.Call_Missed:
                holder.ic_callType.setImageResource(R.drawable.ic_call_missing);
                break;
        }

        holder.tv_name.setText(callToDisplay.getName());
        holder.tv_number.setText(callToDisplay.getNumber());
        holder.tv_dateDay.setText(callToDisplay.getDate());
        holder.tv_dateHour.setText(callToDisplay.getHour());

        if (callToDisplay.isMissing()) {
            holder.tv_name.setTextColor(context.getResources().getColor(R.color.colorPinkDark));
            holder.tv_number.setTextColor(context.getResources().getColor(R.color.colorPinkDark));
        } else {
            holder.tv_name.setTextColor(context.getResources().getColor(R.color.colorBlack));
            holder.tv_number.setTextColor(context.getResources().getColor(R.color.colorGrayDark));
        }

        View.OnClickListener detailsListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        };

        holder.ic_callType.setOnClickListener(detailsListener);
        holder.tv_name.setOnClickListener(detailsListener);
        holder.tv_number.setOnClickListener(detailsListener);

        holder.layout_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PermissionManager.builder(instanceActivity).withView(view).checkRequiredPermissions(Manifest.permission.CALL_PHONE))
                    PhoneProcess.launchCall(instanceActivity, callToDisplay.getNumber());
            }
        });

        return itemView;
    }

}