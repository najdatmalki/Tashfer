package com.najdat.tashfer.Utility;


import android.content.Context;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import java.util.concurrent.Executor;

public class FingerPrintAuthenticator {

    BiometricPrompt biometricPrompt;
    BiometricPrompt.PromptInfo promptInfo;
    ViewGroup layout;

    Context context;
    AuthenticatorTask authenticatorTask;


    public FingerPrintAuthenticator(Context context,  AuthenticatorTask authenticatorTask,ViewGroup layout) {
        this.layout = layout;
        this.context = context;
        this.authenticatorTask = authenticatorTask;
    }

    public void addAuthenticationa(){
        BiometricManager biometricManager =BiometricManager.from(context);

        if (biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE)
        {
            
        } 
        else if (biometricManager.canAuthenticate()==BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE)
        {
            
        } 
        else if (biometricManager.canAuthenticate()==BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED)
        {
            Toast.makeText(context, "No FingerPrint Assigned", Toast.LENGTH_SHORT).show();
            authenticatorTask.afterValidationSuccess();

        }
        Executor executor = ContextCompat.getMainExecutor(context);
        biometricPrompt = new BiometricPrompt((FragmentActivity) context,executor,new BiometricPrompt.AuthenticationCallback
        (){
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString){
                super.onAuthenticationError(errorCode,errString);
                authenticatorTask.onValidationFailed();
            }
            @Override
            public void  onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result){
                super.onAuthenticationSucceeded(result);
                authenticatorTask.afterValidationSuccess();
            }


            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(context, "Authentication failed", Toast.LENGTH_SHORT).show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("validation").setDescription("Use fingerPrint to " +
                "Login").setDeviceCredentialAllowed(true).build();
        biometricPrompt.authenticate(promptInfo);

    }






}
