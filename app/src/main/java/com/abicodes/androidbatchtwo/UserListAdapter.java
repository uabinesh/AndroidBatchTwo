package com.abicodes.androidbatchtwo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ListViewHolder> {

    Context context;
    List<ModelUser> mData;
    private ListViewHolder.RecycleViewClickListener clickListener;

    public UserListAdapter(Context context, List<ModelUser> mData) {
        this.context = context;
        this.mData = mData;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout;
        layout = LayoutInflater.from(context).inflate(R.layout.list_layout,parent,false);
        return new ListViewHolder(layout,context,mData,clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        String name = mData.get(position).getName().toString();
        String email = mData.get(position).getEmail().toString();

        holder.tv_name.setText(name);
        holder.tv_email.setText(email);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tv_name,tv_email;

        public ListViewHolder(@NonNull View itemView, Context context,List<ModelUser> mData,RecycleViewClickListener clickListener) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_email = itemView.findViewById(R.id.tv_email);
        }

        @Override
        public void onClick(View v) {

        }
        public interface RecycleViewClickListener{

        }
    }

}
