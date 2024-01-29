package com.najdat.tashfer.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.najdat.tashfer.Datebase.RoomDB;
import com.najdat.tashfer.Model.Key;
import com.najdat.tashfer.R;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SecretChangerFragment extends Fragment {

    private Button  btnSaveKey,btnEnableDisableMessage,btnDeleteAllMessage,btnEnableLock;
    private EditText txtKey;

    private TextView txtWarning;
    RoomDB database;

    public SecretChangerFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_secret_changer,container,false);
        database =RoomDB.getInstance(getActivity());
        txtKey = view.findViewById(R.id.txtKey);
        btnSaveKey=view.findViewById(R.id.btnSaveKey);
        txtWarning=view.findViewById(R.id.txtWarning);
        btnEnableDisableMessage=view.findViewById(R.id.btnEnableDisableMessage);
        btnDeleteAllMessage=view.findViewById(R.id.btnDeletAllMessage);
        btnEnableLock=view.findViewById(R.id.btnEnableLock);


        btnSaveKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String keyText = txtKey.getText().toString();
                if (isValidPassword(keyText)){
                    saveKey(keyText);
                    txtWarning.setVisibility(View.GONE);
                    txtKey.getText().clear();
                    Toast.makeText(getContext(), "Encryption key changed", Toast.LENGTH_SHORT).show();
                }else {
                    txtWarning.setVisibility(View.VISIBLE);
                }

            }
        });
        btnEnableDisableMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message,dialogMessage,dialogTitle;
                List<Key> keyList =database.keyDao().getAllKey();
                Key key = keyList.get(0);
                Boolean status;
                if (key.getMessagebackup()){
                    status = false;
                    message = "Message backup disabled.";
                    dialogMessage="Are you sure want to disable (Message history creation) ?\n No message history will be made further. ";
                    dialogTitle="Disable";

                }else {
                    status = true;
                    message = "Message backup enabled.";
                    dialogMessage="Are you sure you want to enable (Message history creation) ?\n By enabling you will have history of messages you encrypt.";
                    dialogTitle ="Enable";
                }
                new AlertDialog.Builder(inflater.getContext())
                        .setTitle(dialogTitle +"Confirmation||")
                        .setMessage(dialogMessage)
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                database.keyDao().enableDisable(key.getID(),status);
                                Toast.makeText(getContext(),message, Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("Cancel ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {

                            }
                        }).setIcon(status ? R.drawable.ic_enable:R.drawable.ic_disable)
                        .show();
            }
        });

        btnDeleteAllMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(inflater.getContext())
                        .setTitle("Delet !!")
                        .setMessage("Are you sure ?")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                              database.mainDao().deleteAllMessages();
                              Toast.makeText(getContext(),"All messaged deleted",Toast.LENGTH_SHORT).show();

                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {

                            }
                        }).setIcon(R.drawable.ic_delete).show();
            }
        });

        btnEnableLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String message,dialogMessage,dialogTitle;
               List<Key> keyList = database.keyDao().getAllKey();
               Key key = keyList.get(0);
               Boolean status;
               if (key.getSecurity()){
                   status = false;
                   message = "Screen lock disabled.";
                   dialogMessage ="Are you sure you want to disable (Screen lock) ?";
                   dialogTitle = "Disable";
               }else {
                   status = true;
                   message = "Screem lock enabled";
                   dialogMessage = "Are you sure you want to enable (Screem Lock)?\n By enabling you have to use screen lock to open";
                   dialogTitle = "Enable";

               }
                new AlertDialog.Builder(inflater.getContext())
                        .setTitle(dialogTitle="Lock!!")
                        .setMessage(dialogMessage)
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                database.keyDao().enableDisableSecurity(key.getID(),status);
                                Toast.makeText(getContext(),message, Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {

                            }
                        }).setIcon(status?R.drawable.ic_enable: R.drawable.ic_disable).show();




            }
        });






    return view;
    }

    public boolean isValidPassword(final String password)
    {
        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN ="^(?=.*[0-9])(?=.*[a-z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";;
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();

    }



    private void saveKey(String keyToSave){

        List<Key> keyList = database.keyDao().getAllKey();
        Key myKey = keyList.get(0);
        database.keyDao().changeKey(myKey.getID(),keyToSave);


    }
}