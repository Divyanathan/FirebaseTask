package com.example.user.firebaseexample.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.firebaseexample.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    FirebaseAuth mFireBaseAuth;
    EditText mMailEditText, mPassWordEditText;
    GoogleSignInOptions mGoogleSignInoption;
    GoogleApiClient mGoogleClientApi;
    ProgressDialog mProgressDialog;
    private  final  static  int GOOGLE_SIGNIN_REQUEST_CODE=11;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFireBaseAuth = FirebaseAuth.getInstance();
        mMailEditText = (EditText) findViewById(R.id.emailEditText);
        mPassWordEditText = (EditText) findViewById(R.id.passwordEditText);
        mProgressDialog = new ProgressDialog(MainActivity.this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setTitle("Adding the User");

        mGoogleSignInoption = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();



        mGoogleClientApi= new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, mGoogleSignInoption)
                .build();

    }

    public void addUserAuthentication(View pView) {

        mProgressDialog.show();
        String lEmail = mMailEditText.getText().toString().trim();
        String lPassword = mPassWordEditText.getText().toString().trim();
        Log.d(TAG, "addUserAuthentication: " + lEmail + " " + lPassword);

        FirebaseUser lUser = mFireBaseAuth.getCurrentUser();
        if (lUser != null) {
            Log.d(TAG, "addUserAuthentication: " + lUser.getUid() + " " + lUser.getEmail());
//            mProgressDialog.cancel();

            for (UserInfo lUserInfo : lUser.getProviderData()) {
                Log.d(TAG, "addUserAuthentication: " + lUserInfo.getUid());
            }

        }

        mFireBaseAuth.createUserWithEmailAndPassword(lEmail, lPassword)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        mProgressDialog.cancel();

                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: " + "pass");
                            Toast.makeText(MainActivity.this, "added the user Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d(TAG, "onComplete: " + "fail");
                            Toast.makeText(MainActivity.this, "tha email id already exit\n Or enter the valid email address", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void userSignIN(View pView) {

        mProgressDialog.show();
        String lEmail = mMailEditText.getText().toString().trim();
        String lPassword = mPassWordEditText.getText().toString().trim();
        mFireBaseAuth.signInWithEmailAndPassword(lEmail, lPassword).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mProgressDialog.cancel();

                if (task.isSuccessful()) {

                    Toast.makeText(MainActivity.this, "" + FirebaseAuth.getInstance().getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onComplete: " + FirebaseAuth.getInstance().getCurrentUser().getEmail());

                } else {

                    Toast.makeText(MainActivity.this, "sign in failed", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void googleSignIn(View pView){

        Log.d(TAG, "googleSignIn: ");
        Intent lGoogleSignInIntent= Auth.GoogleSignInApi.getSignInIntent(mGoogleClientApi);
        startActivityForResult(lGoogleSignInIntent,GOOGLE_SIGNIN_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GOOGLE_SIGNIN_REQUEST_CODE){

            Log.d(TAG, "onActivityResult: "+"retrived response");
            GoogleSignInResult lResult=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (lResult.isSuccess()){
                GoogleSignInAccount lAccount=lResult.getSignInAccount();
                Log.d(TAG, "onActivityResult: "+"success "+lAccount.getId());
                FirebaseCredentialGoogleSignIn(lAccount);

            }else {
                Log.d(TAG, "onActivityResult: "+"fail");
            }
        }
    }


//    fireBase credential Authentication

    void FirebaseCredentialGoogleSignIn(GoogleSignInAccount pGoogleAccount){

        Log.d(TAG, "FirebaseCredentialGoogleSignIn: "+pGoogleAccount.getIdToken()+" "+pGoogleAccount.getEmail());
        mProgressDialog.show();
        AuthCredential lAutCredential= GoogleAuthProvider.getCredential(pGoogleAccount.getIdToken(),null);
        mFireBaseAuth.signInWithCredential(lAutCredential)
                .addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                mProgressDialog.cancel();
                if(task.isSuccessful()){
                    Log.d(TAG, "onComplete: "+mFireBaseAuth.getCurrentUser().getEmail());
                }
            }
        });

    }


//    this method is nesasary for google client Api
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
