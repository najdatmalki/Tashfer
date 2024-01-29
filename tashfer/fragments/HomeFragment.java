package com.najdat.tashfer.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.najdat.tashfer.Datebase.RoomDB;
import com.najdat.tashfer.EncryptDecrypt;
import com.najdat.tashfer.Model.Key;
import com.najdat.tashfer.Model.Message;
import com.najdat.tashfer.R;
import com.najdat.tashfer.Utility.BDUtility;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Date;
import java.util.List;

import javax.crypto.BadPaddingException;


public class HomeFragment extends Fragment {

    private MaterialButton btnEncrypt,btnDecrypt;
    private TextInputEditText txtEncrypt, txtDecrypt;
    RoomDB database;

    String key;
    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_home,container,false);
    database = RoomDB.getInstance(getActivity());
    txtEncrypt = view.findViewById(R.id.txtEncrypt);
    txtDecrypt = view.findViewById(R.id.txtDecrypt);
    btnEncrypt = view.findViewById(R.id.btnEncrypt);
    btnDecrypt = view.findViewById(R.id.btnDecrypt);
    btnEncrypt.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String value = txtEncrypt.getText().toString();
            if (value!=null && !value.isEmpty()){
                key = database.keyDao().getAllKey().get(0).getKey();
                String encryptedText = EncryptDecrypt.encrypt(txtEncrypt.getText().toString(), key);
                txtDecrypt.setText(encryptedText);

                List<Key> keyList = database.keyDao().getAllKey();
                Boolean saveMessages = keyList.get(0).getMessagebackup();

                if (saveMessages){
                    saveEncryptedMessage(encryptedText, txtEncrypt.getText().toString());
                }
                BDUtility.setClipboard(getContext(), txtDecrypt.getText().toString());

                txtEncrypt.getText().clear();
                Toast.makeText(getContext(), "Encrypted data copied to clipboard", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Field is empty", Toast.LENGTH_SHORT).show();
            }
        }
    });

    btnDecrypt.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                String value = txtDecrypt.getText().toString();
                if (value!=null && !value.isEmpty()){
                    key = database.keyDao().getAllKey().get(0).getKey();
                    String decryptedText = EncryptDecrypt.decrypt(value, key);
                    txtEncrypt.setText(decryptedText);
                    txtDecrypt.getText().clear();
                    BDUtility.setClipboard(getContext(), txtEncrypt.getText().toString());
                    Toast.makeText(getContext(), "Decrypted text copied to clipboard", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(), "Field Empty", Toast.LENGTH_SHORT).show();
                }
            } catch (BadPaddingException ex){
                ex.printStackTrace();
                Toast.makeText(getContext(), "key changed or invalid encrypted data", Toast.LENGTH_SHORT).show();
            }
            catch (Exception ex){
                ex.printStackTrace();
                Toast.makeText(getContext(), "Invalid encrypted data", Toast.LENGTH_SHORT).show();
            }
        }
    });

    return view;
    }
    private  void saveEncryptedMessage(String eText, String pText){
        Message message = new Message();
        message.setEtext(eText);
        message.setPtext(pText);
        message.setCreationtime(new Date());
        database.mainDao().saveItems(message);
    }
}