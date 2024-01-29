package com.najdat.tashfer.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.najdat.tashfer.Datebase.RoomDB;
import com.najdat.tashfer.Model.Message;
import com.najdat.tashfer.R;
import com.najdat.tashfer.Utility.BDUtility;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.myViewHolder> {

    List<Message> messageList;
    LayoutInflater inflater;

    RoomDB database;
    public MessageAdapter(List<Message> messageList,Context context,RoomDB database){
    this.messageList=messageList;
    this.inflater=LayoutInflater.from(context);
    this.database = database;

    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_message,parent,false);
        return  new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
    holder.txtPlain.setText(messageList.get(position).getPtext());
    holder.txtEncrypted.setText(messageList.get(position).getEtext());
    holder.txtDate.setText(messageList.get(position).getCreationtime().toString());

    holder.btnCopyToClipboard.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            BDUtility.setClipboard(inflater.getContext(), messageList.get(position).getEtext());
            Toast.makeText(inflater.getContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show();

        }
    });
    holder.btnDelete.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            new AlertDialog.Builder(inflater.getContext())
                    .setTitle("Delete !!")
                    .setMessage("are you sure?")
                    .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            database.mainDao().delete(messageList.get(position));
                            messageList.remove(messageList.get(position));
                            notifyDataSetChanged();

                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).setIcon(R.drawable.ic_delete)
                    .show();
        }
    });

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{

            TextView txtPlain,txtEncrypted,txtDate;
            ImageButton btnCopyToClipboard,btnDelete;
        public myViewHolder(@NonNull View itemView){
            super(itemView);
            txtPlain = itemView.findViewById(R.id.txtPlainText);
            txtEncrypted = itemView.findViewById(R.id.txtEncryptedText);
            txtDate = itemView.findViewById(R.id.txtCreationTime);
            btnCopyToClipboard=itemView.findViewById(R.id.btnCopyToClipboard);
            btnDelete=itemView.findViewById(R.id.btnDelete);

        }
    }


}
