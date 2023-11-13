package com.example.order.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.order.interfaces.OrderAPI;
import com.example.order.model.Orders;
import com.example.order.R;
import com.example.order.interfaces.Validation;
import com.example.order.db.DatabaseHelper;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements Validation {

    public class CustomArrayAdapter extends ArrayAdapter<Orders> {
        private Context context;
        private int resource;


        public CustomArrayAdapter(@NonNull Context context, int resource, @NonNull List<Orders> objects) {
            super(context, resource, objects);
            this.context = context;
            this.resource = resource;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Orders orders = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(resource,
                        parent, false);
            }
            TextView viewID = convertView.findViewById(R.id.listViewID);
            viewID.setText(orders.getID());
            TextView viewFirstName = convertView.findViewById(R.id.listViewFirstName);
            viewFirstName.setText(orders.getFirstName());
            TextView viewLastName = convertView.findViewById(R.id.listViewLastName);
            viewLastName.setText(orders.getLastName());
            TextView viewPhone = convertView.findViewById(R.id.listViewPhone);
            viewPhone.setText(orders.getPhone());
            TextView viewDelivered = convertView.findViewById(R.id.listViewDelivered);
            viewDelivered.setText(orders.getDelivered());
            TextView viewEmail = convertView.findViewById(R.id.listViewEmail);
            viewEmail.setText(orders.getEmail());
            TextView viewShippingAddress = convertView.findViewById(R.id.listViewShippingAddress);
            viewShippingAddress.setText(orders.getShippingAddress());
            TextView viewPaymentMethod = convertView.findViewById(R.id.listViewPaymentMethod);
            viewPaymentMethod.setText(orders.getPaymentMethod());
            return convertView;
        }
    }

    protected EditText editFirstName, editLastName, editPhone, editEmail, editShippingAddress, editPaymentMethod, editDelivered;
    protected Button btnInsert;
    protected ListView listView;

    protected DatabaseHelper db;

    protected void FillListView() {
        db = new DatabaseHelper(getApplicationContext());
        List<Orders> orders = db.select();
        CustomArrayAdapter customArrayAdapter =
                new CustomArrayAdapter(this,
                        R.layout.activity_list_view,
                        orders);
        listView.clearChoices();
        listView.setAdapter(customArrayAdapter);
        db.close();
        db = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            FillListView();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    e.getLocalizedMessage(),
                    Toast.LENGTH_LONG
            ).show();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editFirstName = findViewById(R.id.editFirstName);
        editLastName = findViewById(R.id.editLastName);
        editPhone = findViewById(R.id.editPhone);
        editEmail = findViewById(R.id.editEmail);
        editShippingAddress = findViewById(R.id.editShippingAddress);
        editPaymentMethod = findViewById(R.id.editPaymentMethod);
        editDelivered = findViewById(R.id.editDelivered);
        btnInsert = findViewById(R.id.btnInsert);
        listView = findViewById(R.id.listView);
        try {
            FillListView();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    e.getLocalizedMessage(),
                    Toast.LENGTH_LONG
            ).show();
        }
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            String ID = ((TextView) view.findViewById(R.id.listViewID)).getText().toString();
            String FirstName = ((TextView) view.findViewById(R.id.listViewFirstName)).getText().toString();
            String LastName = ((TextView) view.findViewById(R.id.listViewLastName)).getText().toString();
            String Phone = ((TextView) view.findViewById(R.id.listViewPhone)).getText().toString();
            String Email = ((TextView) view.findViewById(R.id.listViewEmail)).getText().toString();
            String ShippingAddress = ((TextView) view.findViewById(R.id.listViewShippingAddress)).getText().toString();
            String PaymentMethod = ((TextView) view.findViewById(R.id.listViewPaymentMethod)).getText().toString();
            String Delivered = ((TextView) view.findViewById(R.id.listViewDelivered)).getText().toString();

            Intent intent = new Intent(MainActivity.this, UpdateDelete.class);
            Bundle b = new Bundle();
            b.putString("ID", ID);
            b.putString("FirstName", FirstName);
            b.putString("LastName", LastName);
            b.putString("Phone", Phone);
            b.putString("Email", Email);
            b.putString("ShippingAddress", ShippingAddress);
            b.putString("PaymentMethod", PaymentMethod);
            b.putString("Delivered", Delivered);
            intent.putExtras(b);
            startActivityForResult(intent, 200, b);
        });
        btnInsert.setOnClickListener(view -> {
            try {
                db = new DatabaseHelper(getApplicationContext());
                if (!Validation.Validate(editEmail.getText().toString(),
                        "(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})"
                )) {
                    throw new Exception("Invalid Email Address");
                }
                if (!Validation.Validate(editPhone.getText().toString(),
                        "(\\+\\d{1,3}( )?)?(\\d{2,4} ?)(\\d{2,4} ?)+\\d{2,4}")) {
                    throw new Exception("Invalid Phone Format");
                }
                String editDeliveredText = editDelivered.getText().toString();
                if (!editDeliveredText.equals("Pending") && !editDeliveredText.equals("In progress") && !editDeliveredText.equals("Delivered")) {
                    throw new Exception("Invalid Status: " + editDeliveredText);
                }
                long insertedId = db.insert(editFirstName.getText().toString(),
                        editLastName.getText().toString(),
                        editPhone.getText().toString(),
                        editEmail.getText().toString(),
                        editShippingAddress.getText().toString(),
                        editPaymentMethod.getText().toString(),
                        editDelivered.getText().toString()
                );
                Thread t = new Thread(() -> {
                    try {
                        OkHttpClient client = new OkHttpClient.Builder().build();
                        Retrofit retrofit =
                                new Retrofit.Builder()
                                        .baseUrl("http://192.168.0.156:8080")
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .client(client)
                                        .build();
                        OrderAPI api = retrofit.create(OrderAPI.class);
                        @SuppressLint("CutPasteId") Call<OrderAPI.orders> insertedOrder =
                                api.api_add_order(insertedId, new OrderAPI.orders(insertedId,
                                        editFirstName.getText().toString(),
                                        editLastName.getText().toString(),
                                        editPhone.getText().toString(),
                                        editEmail.getText().toString(),
                                        editShippingAddress.getText().toString(),
                                        editPaymentMethod.getText().toString(),
                                        editDelivered.getText().toString()
                                ));
                        Response<OrderAPI.orders> r = insertedOrder.execute();
                        if (r.isSuccessful()) {
                            OrderAPI.orders resp = r.body();
                            runOnUiThread(() -> {
                                Toast.makeText(getApplicationContext(),
                                                "INSERTED IN SERVER WITH ID = " + resp.id,
                                                Toast.LENGTH_LONG)
                                        .show();
                            });
                        } else {
                            throw new RuntimeException("exception in server: "
                                    + r.errorBody().string()
                            );
                        }
                    } catch (IllegalArgumentException | IOException e) {
                        runOnUiThread(() -> {
                            Toast.makeText(getApplicationContext(),
                                    "Exception: " + e.getLocalizedMessage(),
                                    Toast.LENGTH_LONG
                            ).show();

                        });
                    }
                });
                t.start();

                Toast.makeText(getApplicationContext(),
                                "INSERT OK", Toast.LENGTH_LONG)
                        .show();


            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),
                        e.getLocalizedMessage(),
                        Toast.LENGTH_LONG
                ).show();
            } finally {
                if (db != null) {
                    db.close();
                    db = null;
                }
            }
            try {
                FillListView();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),
                        e.getLocalizedMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }
}
