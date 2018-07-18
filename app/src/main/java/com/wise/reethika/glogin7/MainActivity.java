package com.wise.reethika.glogin7;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    private static final int REQ_CODE = 9001;
    public static GoogleApiClient googleApiClient;
    private LinearLayout prof_section;
    private Button Signout;
    private SignInButton Signin;
    private TextView Name, Email;
    private ImageView t_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prof_section = findViewById(R.id.prof_section);
        Signout = findViewById(R.id.logout);
        Signin = findViewById(R.id.signin);
        Name = findViewById(R.id.name);
        Email = findViewById(R.id.email);
        t_image = findViewById(R.id.g_image);


        Signin.setOnClickListener(this);
        Signout.setOnClickListener(this);
        prof_section.setVisibility(View.GONE);
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).build();


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.signin:
                signin();
                break;
            case R.id.logout:
                signout();
                break;

        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private void signin() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, REQ_CODE);
        //startActivity(new Intent(getApplicationContext(), Main2Activity.class));
    }

    public void signout() {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                Toast.makeText(getApplicationContext(), "Logging Out", Toast.LENGTH_SHORT).show();
                updateUI(false);
            }
        });

    }

    private void handleresult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            String name = account.getDisplayName();
            String email = account.getEmail();
            //String img_url = account.getPhotoUrl().toString();
            // Picasso.with(this).load(url).resize(500,500).into(g_image);
            //Glide.with(this).load(img_url).into(g_image);
            Name.setText(name);
            // (this.getApplicationContext()).setemail(email);
            Email.setText(email);
            updateUI(true);
        } else {
            updateUI(false);
        }

    }

    public void updateUI(boolean isLogin) {
        if (isLogin) {
            prof_section.setVisibility(View.VISIBLE);
            Signin.setVisibility(View.GONE);
            t_image.setVisibility(View.GONE);

        } else {
            prof_section.setVisibility(View.GONE);
            Signin.setVisibility(View.VISIBLE);
            t_image.setVisibility(View.VISIBLE);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE) {
            if (resultCode == RESULT_OK) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                int statusCode = result.getStatus().getStatusCode();
                handleresult(result);
            }
        }

           /* GoogleSignInResult result= Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleresult(result);*/

    }
}
