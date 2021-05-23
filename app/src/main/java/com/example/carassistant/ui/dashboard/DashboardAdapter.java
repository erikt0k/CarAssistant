package com.example.carassistant.ui.dashboard;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carassistant.R;
import com.example.carassistant.ui.Spendings;
import com.example.carassistant.ui.notifications.NotificationsDB;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewDashHolder> {
    ArrayList<Spendings> spendingList;


    public DashboardAdapter(ArrayList<Spendings> spendingList){

        this.spendingList = spendingList;
    }
    @NonNull
    @NotNull
    @Override
    public DashboardAdapter.ViewDashHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new DashboardAdapter.ViewDashHolder(view);
    }
    @Override
    public int getItemViewType(final int position) {
        return R.layout.spending_item;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull DashboardAdapter.ViewDashHolder holder, int position) {
        Spendings spending = spendingList.get(position);
        System.out.println(holder.tvName);
        holder.tvName.setText(spending.getSpendingSum() +" рублей");
        holder.tvDate.setText(spending.getSpendingDate());
        holder.tvType.setText(spending.getSpendingType());

        holder.btDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DashboardDB db = new DashboardDB(v.getContext());
                db.delete(spendingList.get(position).getSpendingId());

                spendingList.remove(position);  // remove the item from list
                notifyItemRemoved(position); // notify the adapter about the removed item
                DashboardFragment.updateList();
                Log.e("pos: ", String.valueOf(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return spendingList.size();
    }

    public void setArrayMyData(ArrayList<Spendings> arrayMyData) {
        this.spendingList = arrayMyData;
    }

    public static class ViewDashHolder extends RecyclerView.ViewHolder{
        TextView tvName, tvDate, tvType, tvSum;
        Button btDelete, btEdit;
        public ViewDashHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.spending_name);
            tvDate = itemView.findViewById(R.id.spending_date);
            tvType = itemView.findViewById(R.id.spending_type);
            tvSum = itemView.findViewById(R.id.edit_spending_money);
            btDelete = itemView.findViewById(R.id.button_spending_delete);
            btEdit = itemView.findViewById(R.id.button_item_edit);
        }
    }
}
