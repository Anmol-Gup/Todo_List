package com.example.coursera;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView list;
    ArrayList<String> array;
    ArrayAdapter<String> adapter;
    EditText e;
    Button b;
    int flag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list=(ListView)findViewById(R.id.list);
        e=(EditText)findViewById(R.id.item);
        b=(Button)findViewById(R.id.addItemName);

        array=new ArrayList<>();
        adapter=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_expandable_list_item_1,array);
        list.setAdapter(adapter);

        // fetching the values from shared preferences
        SharedPreferences shared=getSharedPreferences("items",MODE_PRIVATE);
        String value=shared.getString("str","");

        if(value!=""){
            String []words=value.split(",");
            for(String i:words)
            {
                if(array.contains(i)==false) {
                    array.add(i);
                    list.setAdapter(adapter);
                }
            }
        }

        // pop-up on item click
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item=adapter.getItem(i);
                Toast.makeText(getApplicationContext(),"You have clicked on "+item,Toast.LENGTH_SHORT).show();
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                SharedPreferences sp=getSharedPreferences("items",MODE_PRIVATE);
                final SharedPreferences.Editor edit=sp.edit();
                final String item=adapter.getItem(i);
                final int position=i;

                // alert box
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Confirm Delete!!!");
                builder.setMessage("Are you sure want to Delete?");
                builder.setCancelable(true);

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        edit.remove("str");
                        array.remove(position);
                        list.setAdapter(adapter);

                        StringBuilder string =new StringBuilder();
                        for(String s:array){
                            string.append(s);
                            string.append(",");
                        }
                        edit.putString("str",string.toString());
                        edit.apply();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                builder.show();
                return false;
            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String itemName = e.getText().toString().trim();
                if (itemName.length() == 0 || itemName == null)
                    Toast.makeText(getApplicationContext(), "Field Required!", Toast.LENGTH_SHORT).show();
                else{
                    array.add(itemName);
                    list.setAdapter(adapter);

                    StringBuilder string=new StringBuilder();
                    for(String s:array)
                    {
                        string.append(s);
                        string.append(",");
                    }
                    SharedPreferences sp=getSharedPreferences("items",MODE_PRIVATE);
                    SharedPreferences.Editor edit=sp.edit();

                    edit.putString("str",string.toString());
                    edit.apply();
                    e.setText("");
                    Toast.makeText(getApplicationContext(), itemName + " added!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}