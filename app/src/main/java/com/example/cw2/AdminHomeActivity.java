package com.example.cw2;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdminHomeActivity extends AppCompatActivity {

    ListView list_view;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_home);

        list_view = (ListView) findViewById(R.id.list_view);
        arrayAdapter = new ArrayAdapter<>(this, R.layout.listview_employee, R.id.name);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://web.socem.plymouth.ac.uk/COMP2000/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        Call<List<User>> call = apiService.getUsers();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (!response.isSuccessful()) {
                    // Handle the error scenario here
                    return;
                }

                List<User> users = response.body();

                List<String> tList = new ArrayList<String>();
                for (int i = 0; i < users.size(); i++) {
                    String name = users.get(i).surname.concat(" ").concat(users.get(i).forename);
                    tList.add(name);
                }

                arrayAdapter.addAll(tList);
                list_view.setAdapter(arrayAdapter);
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                // Handle the failure scenario here
            }
        });
    }
}