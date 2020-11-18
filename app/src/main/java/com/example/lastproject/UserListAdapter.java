package com.example.lastproject;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserListViewHolder> {

    ArrayList<UserObject> userList;

    public UserListAdapter(ArrayList<UserObject> userList){
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);

        UserListViewHolder rcv = new UserListViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull final UserListViewHolder holder, final int position) {
        holder.mName.setText(userList.get(position).getName());
        holder.mPhone.setText(userList.get(position).getPhone());
        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createChat(holder.getAdapterPosition());
            }
        });
        /*
        holder.mAdd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                userList.get(holder.getAdapterPosition()).setSelected(isChecked);
            }
        });

         */
    }

    private void createChat(int position)
    {
        String key = FirebaseDatabase.getInstance().getReference().child("user").child("chat").push().getKey();

        HashMap newChatMap = new HashMap();
        newChatMap.put("id", key);
        newChatMap.put("users/"+ FirebaseAuth.getInstance().getUid(), true);
        newChatMap.put("users/"+userList.get(position).getUid(), true);

        DatabaseReference chatInfoDb = FirebaseDatabase.getInstance().getReference()
                .child("user")
                .child("chat")
                .child(key)
                .child("info");
        chatInfoDb.updateChildren(newChatMap);



        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("user");
        ref.child(FirebaseAuth.getInstance().getUid()).child("chat").child(key).setValue(true);
        ref.child(userList.get(position).getUid()).child("chat").child(key).setValue(true);


    }

    @Override
    public int getItemCount() {
        return userList.size();
    }



    class UserListViewHolder extends RecyclerView.ViewHolder{
        TextView mName, mPhone;
        LinearLayout mLayout;
        CheckBox mAdd;
        UserListViewHolder(View view){
            super(view);
            mName = view.findViewById(R.id.name);
            mPhone = view.findViewById(R.id.phone);
            mAdd = view.findViewById(R.id.add);
            mLayout = view.findViewById(R.id.layout);
        }
    }
}