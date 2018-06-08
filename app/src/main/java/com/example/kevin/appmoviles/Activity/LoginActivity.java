package com.example.kevin.appmoviles.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kevin.appmoviles.R;
import com.example.kevin.appmoviles.api.ApiClient;
import com.example.kevin.appmoviles.api.request.LoginRequest;
import com.example.kevin.appmoviles.api.response.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etLoginID;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvSignup;
    public ProgressBar pbLoading;
    String user,password;

    public static String mNombreUsuario ="Admin";
    public static String mApellidoUsuario ="Admin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setStatusBarTranslucent(true);
        initView();
        getPermissionGps();
    }

    private void initView(){
        etLoginID = findViewById(R.id.etLoginID);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignup =  findViewById(R.id.tvSignup);
        pbLoading = findViewById(R.id.pbLoading);
        pbLoading.setVisibility(View.GONE);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
            }
        });
        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iRegistrar= new Intent(LoginActivity.this,RegistrarActivity.class);
                startActivity(iRegistrar);

            }
        });
    }
    private void validation(){
        if(validate()){
            new SignIn().execute();
        }
        else return;

    }

    public boolean validate() {
        user= etLoginID.getText().toString();
        password= etPassword.getText().toString();
        boolean valid = true;
        //if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        if(user.isEmpty()){
            etLoginID.setError("Por favor ingrese un usuario.");
            valid = false;
        } else {
            etLoginID.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 15 ) {
            etPassword.setError("La contraseña debe tener entre 6 y 15 caracteres alfanuméricos.");
            valid = false;
        } else {
            etPassword.setError(null);
        }

        return valid;
    }


    private void doLogin() {




        LoginRequest DataRequest = new LoginRequest();
        DataRequest.setUsuario(user);
        DataRequest.setPassword(password);


        Call<LoginResponse> responseCall = ApiClient.getApiClient().doLogin(DataRequest);


        responseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (response.isSuccessful()) {
                    LoginResponse dataResponse = response.body();
                    if (dataResponse.isSuccess()) {

                        Intent iUnitActivity = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(iUnitActivity);
                        LoginActivity.this.finish();
                        mNombreUsuario = dataResponse.getNombre();
                        mApellidoUsuario = dataResponse.getApellido();

                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.i("LOGIN", "onFailure");
            }
        });


    }
    public class SignIn extends AsyncTask<Void,Integer,Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {

            doLogin();
            return true;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pbLoading.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            pbLoading.setVisibility(View.GONE);

        }
    }








    protected void setStatusBarTranslucent(boolean makeTranslucent) {
        if (makeTranslucent) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private void getPermissionGps() {
        if (ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        , 2);
            }
            return;
        }
        //your code
        //Toast.makeText(VerificationActivity.this, "GPS is enable.", Toast.LENGTH_SHORT).show();

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case 1:{
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getPermissionGps();

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(LoginActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;

                // other 'case' lines to check for other
                // permissions this app might request

            }
            default:
                break;
        }
    }
}
