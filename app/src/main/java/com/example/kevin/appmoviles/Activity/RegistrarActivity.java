package com.example.kevin.appmoviles.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.kevin.appmoviles.R;
import com.example.kevin.appmoviles.api.ApiClient;
import com.example.kevin.appmoviles.api.request.LoginRequest;
import com.example.kevin.appmoviles.api.request.UsuarioRequest;
import com.example.kevin.appmoviles.api.response.LoginResponse;
import com.example.kevin.appmoviles.api.response.UsuarioResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrarActivity extends AppCompatActivity {
    ImageView ivAtras;
    EditText etNombre,etApellido,etEdad,etUsuario,etPassword;
    public ProgressBar pbLoading;
    private Button btnRegistrar;
    String user,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
        setUi();




    }

    void setUi(){

        ivAtras = findViewById(R.id.ivAtras);
        etNombre = findViewById(R.id.etNombre);
        etApellido = findViewById(R.id.etApellido);
        etEdad = findViewById(R.id.etEdad);
        etUsuario = findViewById(R.id.etUsuario);
        etPassword = findViewById(R.id.etPassword);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        pbLoading = findViewById(R.id.pbLoading);
        pbLoading.setVisibility(View.GONE);

        ivAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation();
            }
        });
    }
    private void doRegister() {

        UsuarioRequest DataRequest = new UsuarioRequest();
        DataRequest.setNombre(etNombre.getText().toString());
        DataRequest.setApellido(etApellido.getText().toString());
        DataRequest.setEdad(Integer.valueOf(etEdad.getText().toString()));
        DataRequest.setUsuario(user);
        DataRequest.setPassword(password);


        Call<UsuarioResponse> responseCall = ApiClient.getApiClient().doRegister(DataRequest);


        responseCall.enqueue(new Callback<UsuarioResponse>() {
            @Override
            public void onResponse(Call<UsuarioResponse> call, Response<UsuarioResponse> response) {

                if (response.isSuccessful()) {
                    UsuarioResponse dataResponse = response.body();
                    if (dataResponse.isSuccess()) {

                        Intent iLogin = new Intent(RegistrarActivity.this, LoginActivity.class);
                        startActivity(iLogin);
                        Toast.makeText(RegistrarActivity.this, "Registro exitoso.", Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<UsuarioResponse> call, Throwable t) {
                Log.i("LOGIN", "onFailure");
            }
        });


    }

    private void validation(){
        if(validate()){
            new SignUp().execute();
        }
        else return;

    }

    public boolean validate() {
        user= etUsuario.getText().toString();
        password= etPassword.getText().toString();
        boolean valid = true;
        //if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        if(user.isEmpty()){
            etUsuario.setError("Por favor ingrese un usuario.");
            valid = false;
        } else {
            etUsuario.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 15 ) {
            etPassword.setError("La contraseña debe tener entre 6 y 15 caracteres alfanuméricos.");
            valid = false;
        } else {
            etPassword.setError(null);
        }

        return valid;
    }
    public class SignUp extends AsyncTask<Void,Integer,Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {

            doRegister();
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

}
