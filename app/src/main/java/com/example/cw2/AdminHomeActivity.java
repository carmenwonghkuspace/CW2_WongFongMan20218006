package com.example.cw2;

import android.app.Dialog;
import android.content.Context;
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
    CustomAdapter arrayAdapter;
    Button btCustomDialog;
    Button btOK;
    Button btC;
    EditText newid, newsurname, newforename;

    List<User> tList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_home);

        list_view = (ListView) findViewById(R.id.list_view);
        btCustomDialog = (Button) findViewById(R.id.btCustomDialog);

        btCustomDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCustomDialog();
            }
        });

        tList = new ArrayList<User>();
        arrayAdapter = new CustomAdapter(tList, (Context) this);
        list_view.setAdapter(arrayAdapter);

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

                for (int i = 0; i < users.size(); i++) {
                    tList.add(users.get(i));
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                // Handle the failure scenario here
            }
        });
    }

    private void setCustomDialog(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AdminHomeActivity.this);
        View v = getLayoutInflater().inflate(R.layout.add_dialog,null);
        alertDialog.setView(v);
        btOK = v.findViewById(R.id.button_ok);
        btC  = v.findViewById(R.id.button_back);
        newid = v.findViewById(R.id.newid);
        newsurname = v.findViewById(R.id.newsurname);
        newforename = v.findViewById(R.id.newforename);


        AlertDialog dialog = alertDialog.create();
        dialog.show();
        btOK.setOnClickListener((v1 -> {
            User user = new User();
            user.id = (int) Integer.valueOf(newid.getText().toString());
            user.surname = newsurname.getText().toString();
            user.forename = newforename.getText().toString();
            updateList(user);
            dialog.dismiss();
        }));

        btC.setOnClickListener((v1 -> {dialog.dismiss();}));
    };

    public void updateList(User newName){
        tList.add(newName);
        arrayAdapter.notifyDataSetChanged();;
    }


}