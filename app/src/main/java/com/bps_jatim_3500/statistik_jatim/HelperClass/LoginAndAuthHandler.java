package com.bps_jatim_3500.statistik_jatim.HelperClass;

import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.bps_jatim_3500.statistik_jatim.R;
import com.bps_jatim_3500.statistik_jatim.activity.MainActivity;
import com.bps_jatim_3500.statistik_jatim.fragmen.HomeFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.concurrent.Executor;

import static android.content.ContentValues.TAG;

public class LoginAndAuthHandler extends MainActivity{

    MainActivity mainActivity;
    Dialog dialog;
    GoogleSignInClient mGoogleSignInClient;
    RelativeLayout rvBttLoginSsoGoogle;
    Button signOutButton;
    int RC_SIGN_IN=0;
    HomeFragment homeFragment;
    GoogleSignInAccount account;

    public LoginAndAuthHandler(MainActivity mainActivity, Dialog dialog, HomeFragment homeFragment) {
        this.mainActivity=mainActivity;
        this.dialog=dialog;
        this.homeFragment=homeFragment;

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        account = GoogleSignIn.getLastSignedInAccount(mainActivity);
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this.mainActivity,gso);
    }

    public void loginClick(){
        if(mainActivity.getAcct()==null){
            dialog.setContentView(R.layout.activity_login);
            rvBttLoginSsoGoogle=dialog.findViewById(R.id.rv_btt_login_sso_google);

            rvBttLoginSsoGoogle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.rv_btt_login_sso_google:
                            signIn();
                            break;
                    }
                }
            });
            dialog.show();
        }else{
            dialog.setContentView(R.layout.activity_login_profil);
            signOutButton=dialog.findViewById(R.id.log_out_button);

            signOutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        // ...
                        case R.id.log_out_button:
                            signOut();
                            break;
                        // ...
                    }
                }
            });
            dialog.show();
        }
    }

    public void signOut(){
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        setAcct(null);
                        dialog.dismiss();

                    }
                });
    }

    //SSO Google
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        mainActivity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
            System.out.println("Berhasil Bos");
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            this.account=account;
            // Signed in successfully, show authenticated UI.
            //updateUI(account);
            System.out.println("Berhasil");
            dialog.dismiss();

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
            System.out.println("Gagal");
        }
    }
}
