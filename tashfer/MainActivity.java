package com.najdat.tashfer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.najdat.tashfer.R;
import com.najdat.tashfer.Datebase.RoomDB;
import com.najdat.tashfer.Model.Key;
import com.najdat.tashfer.Utility.AuthenticatorTask;
import com.najdat.tashfer.Utility.FingerPrintAuthenticator;
import com.najdat.tashfer.fragments.HomeFragment;
import com.najdat.tashfer.fragments.MessageFragment;
import com.najdat.tashfer.fragments.SecretChangerFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    RelativeLayout relativeLayout;
    RoomDB database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        persisyKey();

        bottomNavigationView = findViewById(R.id.bnView);
        relativeLayout = findViewById(R.id.layout_main);

        try {
            AuthenticatorTask authenticatorTask = new AuthenticatorTask() {
                @Override
                public void afterValidationSuccess() {
                    relativeLayout.setVisibility(View.VISIBLE);
                }

                @Override
                public void onValidationFailed() {
                    KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
                    if (keyguardManager.isKeyguardSecure()) {
                        ((Activity) MainActivity.this).finish();
                        System.exit(0);
                    } else {
                        Toast.makeText(MainActivity.this, "Phone doesn't contain password", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            };

            List<Key> keyList = database.keyDao().getAllKey();
            Boolean security = keyList.get(0).getSecurity();
            if (security) {
                FingerPrintAuthenticator fingerPrintAuthenticator = new FingerPrintAuthenticator(MainActivity.this, authenticatorTask, relativeLayout);
                fingerPrintAuthenticator.addAuthenticationa();

            } else {
                relativeLayout.setVisibility(View.VISIBLE);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    loadFragment(new HomeFragment(), false);
                    return true;
                } else if (item.getItemId() == R.id.nav_massage) {
                    loadFragment(new MessageFragment(), false);
                    return true;
                } else if (item.getItemId() == R.id.nav_changekey) {
                    loadFragment(new SecretChangerFragment(), false);
                    return true;
                } else {
                    Toast.makeText(MainActivity.this, "Default clicked", Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.nav_home);
    }

    private void loadFragment(Fragment fragment, boolean flag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (flag) {
            fragmentTransaction.add(R.id.container, fragment);
        } else {
            fragmentTransaction.replace(R.id.container, fragment);
        }
        fragmentTransaction.commit();
    }

    private void persisyKey() {
        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = prefs.edit();

            database = RoomDB.getInstance(this);
            if (!prefs.getBoolean("firstTime", false)) {
                Key key = new Key();
                key.setKey("KJKLJDLKFLK90*()ksdfjlsd");
                key.setMessagebackup(true);
                key.setSecurity(true);
                database.keyDao().saveItem(key);
                editor.putBoolean("firstTime", true);
                editor.commit();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}