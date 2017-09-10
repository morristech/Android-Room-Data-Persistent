package com.burhan.arch.room.adapter;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.burhan.arch.room.R;
import com.burhan.arch.room.dbutils.AppDatabase;
import com.burhan.arch.room.dbutils.RoomDB;
import com.burhan.arch.room.models.User;

import java.util.List;

import static com.burhan.arch.room.dbutils.Utils.getDateOfBirth;


/**
 * Created by Burhanuddin on 9/9/2017.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private List<User> userList;
    private onItemClickListener mOnItemClickListener;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_users, parent, false);
        return new ViewHolder(view);
    }

    public interface onItemClickListener {
        void onClick(View view, int position);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = userList.get(position);
        holder.txtUserName.setText(String.format("%s %s", user.getFirstName(), user.getLastName()));
        holder.txtUserAge.setText(String.format("%d years", user.getAge()));
        holder.txtUserDob.setText(getDateOfBirth(user.getDateOfBirth()));
        if (user.getUserBitmapImage() != null) {
            holder.ivProfilePic.setImageBitmap(user.getUserBitmapImage());
        }
    }

    public void setOnItemClickListener(onItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return userList != null ? userList.size() : 0;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtUserName, txtUserAge, txtUserDob;
        ImageView btnDelete, btnUpdate, ivProfilePic;

        public ViewHolder(final View itemView) {
            super(itemView);
            txtUserName = itemView.findViewById(R.id.txtUserName);
            txtUserAge = itemView.findViewById(R.id.txtUserAge);
            txtUserDob = itemView.findViewById(R.id.txtDOB);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnUpdate = itemView.findViewById(R.id.btnUpdate);
            ivProfilePic = itemView.findViewById(R.id.ivProfilePicture);

            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onClick(view, getLayoutPosition());
                    }
                }
            });
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());
                    alertDialog.setTitle("Delete");
                    alertDialog.setMessage("Are you sure want to delete this user ?");
                    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            AppDatabase appDatabase = RoomDB.getDefaultInstance();
                            appDatabase.userDao().delete(userList.get(getLayoutPosition()));
                            userList.remove(getLayoutPosition());
                            notifyItemRemoved(getLayoutPosition());
                            dialogInterface.dismiss();
                        }
                    });
                    alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    alertDialog.create().show();
                }
            });
        }
    }

    public User getItem(int position) {
        return userList.get(position);
    }
}
