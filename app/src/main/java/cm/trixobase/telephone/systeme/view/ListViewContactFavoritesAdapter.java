package cm.trixobase.telephone.systeme.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cm.trixobase.telephone.R;
import cm.trixobase.telephone.common.BaseName;
import cm.trixobase.telephone.domain.Favorites;
import cm.trixobase.telephone.domain.ui.UiContact;
import cm.trixobase.telephone.systeme.manager.Manager;

/**
 * Created by noumianguebissie on 12/22/18.
 */

public class ListViewContactFavoritesAdapter extends BaseAdapter implements Filterable {

    public static class ViewHolder {
        ImageView iv_contact;
        TextView tv_name, tv_number;
        CheckBox cb_item;
    }

    private List<UiContact> contactsList;
    private List<UiContact> contactsFilter;
    private ValueFilter valueFilter;
    private LayoutInflater inflater;

    public ListViewContactFavoritesAdapter(Context context, List<UiContact> contacts) {
        this.contactsList = new ArrayList<>();
        this.contactsFilter = new ArrayList<>();

        this.contactsList.addAll(contacts);
        this.contactsFilter.addAll(contacts);
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

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_contact_favorite_item, null);
            holder = new ViewHolder();

            holder.iv_contact = convertView.getRootView().findViewById(R.id.iv_contact_operator_id);
            holder.tv_name = convertView.getRootView().findViewById(R.id.tv_contact_name_id);
            holder.tv_number = convertView.getRootView().findViewById(R.id.tv_contact_number_id);
            holder.cb_item = convertView.findViewById(R.id.cb_contact_id);

            convertView.setTag(holder);

            holder.cb_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CheckBox c = (CheckBox) view;
                    UiContact contact = (UiContact) c.getTag();
                    String number = contact.getNumber();
                    if (c.isChecked()) {
                        if (!Favorites.favoritesNumbers.contains(number)) {
                            Favorites.favoritesNumbers.add(number);
                            contact.setChecked(true);
                        }
                    } else {
                        if (Favorites.favoritesNumbers.contains(number)) {
                            Favorites.favoritesNumbers.remove(number);
                            contact.setChecked(false);
                        }

                    }
                }
            });
        } else holder = (ViewHolder) convertView.getTag();

        UiContact contactToDisplay = contactsList.get(position);

        switch (contactToDisplay.getOperator()) {
            case BaseName.Operator_Camtel:
                holder.iv_contact.setImageResource(R.drawable.lg_camtel);
                break;
            case BaseName.Operator_Nexttel:
                holder.iv_contact.setImageResource(R.drawable.lg_nexttel);
                break;
            case BaseName.Operator_Orange:
                holder.iv_contact.setImageResource(R.drawable.lg_orange);
                break;
            case BaseName.Operator_Mtn:
                holder.iv_contact.setImageResource(R.drawable.lg_mtn);
                break;
            default:
                holder.iv_contact.setImageResource(R.drawable.ic_action_phone);
                break;
        }

        holder.tv_name.setText(Manager.compute.contactName(contactToDisplay.getName()));
        holder.tv_number.setText(contactToDisplay.getNumber());

        holder.cb_item.setChecked(contactToDisplay.isChecked());
        holder.cb_item.setTag(contactToDisplay);


        return convertView;
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


        //Invoked in the UI thread to publish the filtering results in the user interface.
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            contactsList = (List<UiContact>) results.values;
            notifyDataSetChanged();
        }
    }

}
