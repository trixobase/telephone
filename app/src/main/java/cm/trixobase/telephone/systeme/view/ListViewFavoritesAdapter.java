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

import cm.trixobase.telephone.MainActivity;
import cm.trixobase.telephone.R;
import cm.trixobase.telephone.common.BaseName;
import cm.trixobase.telephone.core.Contact;
import cm.trixobase.telephone.domain.ui.UiContact;
import cm.trixobase.telephone.systeme.manager.Manager;
import cm.trixobase.telephone.systeme.manager.PermissionManager;

/**
 * Created by noumianguebissie on 12/21/18.
 */

public class ListViewFavoritesAdapter extends BaseAdapter {

    private static int instancePosition;

    public class ViewHolder {
        ImageView iv_operator;
        TextView tv_name, tv_number;
        LinearLayout ll_favorite_call;
        LinearLayout ll_favorite_delete;
    }

    private final Activity instanceActivity;
    private final Context context;
    private static List<UiContact> favorites;
    private ListViewFavoritesAdapter instance;
    private LayoutInflater inflater;

    public ListViewFavoritesAdapter(Activity activity, List<UiContact> contactsList) {
        instance = this;
        instanceActivity = activity;
        this.context = activity;
        this.favorites = contactsList;
    }

    @Override
    public int getCount() {
        return favorites.size();
    }

    @Override
    public UiContact getItem(int position) {
        return favorites.get(position);
    }

    @Override
    public long getItemId(int position) {
        return favorites.indexOf(getItem(position));
    }

    public View getView(final int position, View itemView, ViewGroup parent) {
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ViewHolder holder;
        if (itemView == null) {
            holder = new ViewHolder();
            itemView = inflater.inflate(R.layout.listview_favorites_item, null);

            holder.iv_operator = itemView.findViewById(R.id.iv_operator_id);
            holder.tv_name = itemView.findViewById(R.id.tv_favorite_name_id);
            holder.tv_number = itemView.findViewById(R.id.tv_favorite_number_id);
            holder.ll_favorite_call = itemView.findViewById(R.id.ll_favorite_call_id);
            holder.ll_favorite_delete = itemView.findViewById(R.id.ll_delete_id);
            itemView.setTag(holder);
        } else holder = (ViewHolder) itemView.getTag();

        final UiContact favoriteToDisplay = favorites.get(position);

        holder.tv_name.setText(Manager.compute.contactName(favoriteToDisplay.getName()));
        holder.tv_number.setText(favoriteToDisplay.getNumber());
        holder.ll_favorite_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.contactNumberToCall = favoriteToDisplay.getNumber();
                if(PermissionManager.builder(instanceActivity).withView(view).checkRequiredPermissions(Manifest.permission.CALL_PHONE))
                    MainActivity.call();
            }
        });
        holder.ll_favorite_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.adapter = instance;
                MainActivity.favoriteNameToDelete = favorites.get(position).getDisplayName();
                instancePosition = position;
                instanceActivity.showDialog(MainActivity.DIALOG_BOX_DELETE_FAVORITE);
            }
        });

        switch (favoriteToDisplay.getOperator()) {
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

        return itemView;
    }

    public void refreshView() {
        UiContact contactToRemove = favorites.get(instancePosition);
        contactToRemove.setAsNotFavorite();
        Contact.builder().withData(contactToRemove.getData()).build().save(context);
        favorites.remove(instancePosition);
        notifyDataSetChanged();
    }

}