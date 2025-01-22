package com.example.cw2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends ArrayAdapter<User> implements View.OnClickListener{

    private List<User> dataSet;
    Context mContext;

    private static class ViewHolder {
        ImageButton btndel, btnedit;
        TextView name;
    }

    public CustomAdapter(List<User> data, Context context){
        super(context, R.layout.listview_employee, data);
        this.dataSet = data;
        this.mContext=context;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup){
        ViewHolder viewHolder = new ViewHolder();;

        if (view == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.listview_employee, viewGroup, false);
        }

        viewHolder.btndel = (ImageButton) view.findViewById(R.id.btndel);
        viewHolder.btnedit = (ImageButton) view.findViewById(R.id.btnedit);
        viewHolder.name = (TextView) view.findViewById(R.id.name);

        viewHolder.name.setText(combineName(dataSet.get(position)));
        viewHolder.btndel.setTag(position);
        viewHolder.btnedit.setTag(position);

        viewHolder.btndel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = (Integer) v.getTag();
                dataSet.remove(i);
                notifyDataSetChanged();
            }
        });

        viewHolder.btnedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = (Integer) v.getTag();
                User oldUser = dataSet.get(i);
                setCustomDialog(oldUser, i);
            }
        });

        return view;
    }

    public String combineName(User user){
        String id = String.valueOf(user.id);
        String combine = id.concat(": ").concat(user.surname).concat(" ").concat(user.forename);
        return combine;
    }

    private void setCustomDialog(User oldUser, int position){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.add_dialog,null);
        alertDialog.setView(v);
        Button btOK = v.findViewById(R.id.button_ok);
        Button btC  = v.findViewById(R.id.button_back);
        EditText newid = v.findViewById(R.id.newid);
        EditText newsurname = v.findViewById(R.id.newsurname);
        EditText newforename = v.findViewById(R.id.newforename);

        newid.setText(String.valueOf(oldUser.id));
        newsurname.setText(oldUser.surname);
        newforename.setText(oldUser.forename);

        AlertDialog dialog = alertDialog.create();
        dialog.show();
        btOK.setOnClickListener((v1 -> {
            User user = new User();
            user.id = (int) Integer.valueOf(newid.getText().toString());
            user.surname = newsurname.getText().toString();
            user.forename = newforename.getText().toString();
            updateList(user, position);
            dialog.dismiss();
        }));

        btC.setOnClickListener((v1 -> {dialog.dismiss();}));
    };

    public void updateList(User newUser, int position){
        dataSet.set(position, newUser);
        notifyDataSetChanged();;
    }
}
