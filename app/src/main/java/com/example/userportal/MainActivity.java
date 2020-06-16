package com.example.userportal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://10.0.2.2:3000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleLoginDialog();
            }
        });

        findViewById(R.id.signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleSignupDialog();
            }
        });

        findViewById(R.id.editCredentials).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleEditDialog();
            }
        });
    }

    private void handleEditDialog() {
        View view = getLayoutInflater().inflate(R.layout.edit_dialog, null);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setView(view).show();

        Button editBtn = view.findViewById(R.id.editCredentials);
        final EditText emailEdit = view.findViewById(R.id.emailEdit);
        final EditText passwordEdit = view.findViewById(R.id.passwordEdit);
        final EditText ageEdit = view.findViewById(R.id.ageEdit);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> map = new HashMap<>();

                map.put("email", emailEdit.getText().toString());
                map.put("password", passwordEdit.getText().toString());
                map.put("age", ageEdit.getText().toString());

                Call<EditResult> call = retrofitInterface.executeEdit(map);

                call.enqueue(new Callback<EditResult>() {
                    @Override
                    public void onResponse(Call<EditResult> call, Response<EditResult> response) {
                        if(response.code()==200){
                            EditResult result = response.body();

                            AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                            builder1.setTitle("Updated credentials of the user, Name: "+result.getName());
                            //builder1.setMessage(result.getEmail());
                            builder1.setMessage("New Age: "+result.getAge());
                            builder1.show();
                        }else if(response.code()==404){
                            Toast.makeText(MainActivity.this, "Wrong Credentials",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<EditResult> call, Throwable t) {
                        Toast.makeText(MainActivity.this, t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void handleLoginDialog() {
        View view = getLayoutInflater().inflate(R.layout.login_dialog, null);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setView(view).show();

        Button loginBtn = view.findViewById(R.id.login);
        final EditText emailEdit = view.findViewById(R.id.emailEdit);
        final EditText passwordEdit = view.findViewById(R.id.passwordEdit);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> map = new HashMap<>();

                map.put("email", emailEdit.getText().toString());
                map.put("password", passwordEdit.getText().toString());

                Call<LoginResult> call = retrofitInterface.executeLogin(map);

                call.enqueue(new Callback<LoginResult>() {
                    @Override
                    public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                        if(response.code()==200){
                            LoginResult result = response.body();

                            AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                            builder1.setTitle(result.getName());
                            builder1.setMessage("Email: "+result.getEmail()+"\nAge: "+result.getAge());
                            //builder1.setMessage("Age: "+result.getAge());
                            builder1.show();
                        }else if(response.code()==404){
                            Toast.makeText(MainActivity.this, "Wrong Credentials",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResult> call, Throwable t) {
                        Toast.makeText(MainActivity.this, t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void handleSignupDialog() {
        View view = getLayoutInflater().inflate(R.layout.signup_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view).show();

        Button signupBtn = view.findViewById(R.id.signup);
        final EditText nameEdit = view.findViewById(R.id.nameEdit);
        final EditText emailEdit = view.findViewById(R.id.emailEdit);
        final EditText passwordEdit = view.findViewById(R.id.passwordEdit);
        final EditText ageEdit = view.findViewById(R.id.ageEdit);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap<String,String> map = new HashMap<>();

                map.put("name", nameEdit.getText().toString());
                map.put("email", emailEdit.getText().toString());
                map.put("password", passwordEdit.getText().toString());
                map.put("age", ageEdit.getText().toString());

                Call<Void> call = retrofitInterface.executeSignup(map);

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.code()==200){
                            Toast.makeText(MainActivity.this, "Signed up Successfully",
                                    Toast.LENGTH_LONG).show();
                        }else if(response.code()==400){
                            Toast.makeText(MainActivity.this,
                                    "Already Registered", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(MainActivity.this, t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}