package cm.trixobase.telephone.systeme.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cm.trixobase.telephone.MainActivity;
import cm.trixobase.telephone.R;
import cm.trixobase.telephone.Telephone;
import cm.trixobase.telephone.common.BaseName;
import cm.trixobase.telephone.domain.ui.UiContact;
import cm.trixobase.telephone.systeme.manager.PermissionManager;
import cm.trixobase.telephone.systeme.media.ImageProcess;

/**
 * Created by noumianguebissie on 12/19/18.
 */

public class ListViewContactAdapter extends BaseAdapter implements Filterable {

    private static class ViewHolder {
        ImageView iv_operator;
        TextView tv_name, tv_number;
        LinearLayout layout_call;
    }

    private static Activity instanceActivity;
    private final Context context;
    private List<UiContact> contactsList;
    private List<UiContact> contactsFilter;
    private LayoutInflater inflater;
    private ValueFilter valueFilter;

    public ListViewContactAdapter(Activity activity, List<UiContact> contacts) {
        instanceActivity = activity;
        this.context = activity.getBaseContext();
        this.contactsList = contacts;
        this.contactsFilter = contacts;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        getFilter();
    }

    @Override
    public int getCount() {
        return contactsList.size();
    }

    @Override
    public UiContact getItem(int position) {
        return contactsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return contactsList.indexOf(getItem(position));
    }

    public View getView(int position, View itemView, ViewGroup parent) {
        ViewHolder holder;
        if (itemView == null) {
            holder = new ListViewContactAdapter.ViewHolder();
            itemView = inflater.inflate(R.layout.listview_contact_item, null);
            holder.iv_operator = itemView.findViewById(R.id.iv_contact_operator_id);
            holder.tv_name = itemView.findViewById(R.id.tv_contact_name_id);
            holder.tv_number = itemView.findViewById(R.id.tv_contact_number_id);
            holder.layout_call = itemView.findViewById(R.id.ll_contact_call_id);
            itemView.setTag(holder);
        } else holder = (ViewHolder) itemView.getTag();

        final UiContact contactToDisplay = contactsList.get(position);

        switch (contactToDisplay.getOperator()) {
            case BaseName.Operator_Camtel:
                holder.iv_operator.setImageResource(R.drawable.lg_camtel);
                break;
            case BaseName.Operator_Nexttel:
                holder.iv_operator.setImageResource(R.drawable.lg_nexttel);
                break;
            case BaseName.Operator_Orange:
                holder.iv_operator.setImageResource(R.drawable.lg_orange);
                break;
            case BaseName.Operator_Mtn:
                holder.iv_operator.setImageResource(R.drawable.lg_mtn);
                break;
            default:
                holder.iv_operator.setImageResource(R.drawable.ic_action_phone);
                break;
        }

        if (contactToDisplay.getNumber().contains(BaseName.Trixobase_Number_To_Call))
            holder.iv_operator.setImageResource(R.drawable.trixobase);

        holder.tv_name.setText(contactToDisplay.getDisplayName());
        holder.tv_number.setText(contactToDisplay.getNumber());

        View.OnClickListener editListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(context, ContactEditActivity.class);
//            i.putExtra(AttributeName.Contact_Id, contactToDisplay.getIdentification());
//            i.putExtra(AttributeName.Interface_Page_To_show, PhoneActivity.PAGE_CONTACT);
//            context.startActivity(i);
            }
        };

        View.OnClickListener callListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.contactNumberToCall = contactToDisplay.getNumber();
                if (PermissionManager.builder(instanceActivity).withView(view).checkRequiredPermissions(Manifest.permission.CALL_PHONE))
                    MainActivity.call();
            }
        };

        holder.iv_operator.setOnClickListener(editListener);
        holder.tv_name.setOnClickListener(editListener);
        holder.tv_number.setOnClickListener(editListener);
        holder.layout_call.setOnClickListener(callListener);

        return itemView;
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null)
            valueFilter = new ValueFilter();
        return valueFilter;
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                List<UiContact> filterList = new ArrayList<>();
                for (int i = 0; i < contactsFilter.size(); i++) {
                    if ((contactsFilter.get(i).getName().toUpperCase())
                            .contains(constraint.toString().toUpperCase())
                            || contactsFilter.get(i).getNumber()
                            .contains(constraint.toString())) {
                        filterList.add(contactsFilter.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = contactsFilter.size();
                results.values = contactsFilter;
            }
            return results;
        }


        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            contactsList = (List<UiContact>) results.values;
            notifyDataSetChanged();
        }
    }

    public void add(UiContact contact) {
        contactsList.add(contact);
        notifyDataSetChanged();
    }

}