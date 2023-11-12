package com.example.order;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UpdateDelete extends AppCompatActivity implements OrderServerCommunication {
    protected String ID;
    protected EditText editFirstName, editLastName, editPhone, editEmail, editShippingAddress, editPaymentMethod, editDelivered;
    protected Button btnUpdate, btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete);
        editFirstName = findViewById(R.id.editFirstName);
        editLastName = findViewById(R.id.editLastName);
        editPhone = findViewById(R.id.editPhone);
        editEmail = findViewById(R.id.editEmail);
        editShippingAddress = findViewById(R.id.editShippingAddress);
        editPaymentMethod = findViewById(R.id.editPaymentMethod);
        editDelivered = findViewById(R.id.editDelivered);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        Bundle b = getIntent().getExtras();

        Orders c = new Orders(
                b.getString("ID"),
                b.getString("FirstName"),
                b.getString("LastName"),
                b.getString("Phone"),
                b.getString("Email"),
                b.getString("ShippingAddress"),
                b.getString("PaymentMethod"),
                b.getString("Delivered")
        );
        if (c == null) {
            Toast.makeText(getApplicationContext(),
                    "Data not found",
                    Toast.LENGTH_LONG
            ).show();
        }
        ID = c.getID();
        editFirstName.setText(c.getFirstName());
        editLastName.setText(c.getLastName());
        editPhone.setText(c.getPhone());
        editEmail.setText(c.getEmail());
        editShippingAddress.setText(c.getShippingAddress());
        editPaymentMethod.setText(c.getPaymentMethod());
        editDelivered.setText(c.getDelivered());

        btnUpdate.setOnClickListener(view -> {
            DatabaseHelper db = null;
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
                db.update(
                        new Orders(
                                ID,
                                editFirstName.getText().toString(),
                                editLastName.getText().toString(),
                                editPhone.getText().toString(),
                                editEmail.getText().toString(),
                                editShippingAddress.getText().toString(),
                                editPaymentMethod.getText().toString(),
                                editDelivered.getText().toString()
                        )
                );
                APICallUpdate(Long.parseLong(ID), editFirstName, editLastName, editPhone, editEmail, editShippingAddress, editPaymentMethod, editDelivered,
                        message -> runOnUiThread(
                                () -> Toast.makeText(getApplicationContext(),
                                        message, Toast.LENGTH_LONG
                                ).show()
                        )
                );
                Toast.makeText(getApplicationContext(),
                                "UPDATE OK", Toast.LENGTH_LONG)
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
            finishActivity(301);
            Intent i = new Intent(UpdateDelete.this, MainActivity.class);
            startActivity(i);
        });

        btnDelete.setOnClickListener(view -> {
            DatabaseHelper db = null;
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
                db.delete(
                        new Orders(
                                ID,
                                editFirstName.getText().toString(),
                                editLastName.getText().toString(),
                                editPhone.getText().toString(),
                                editEmail.getText().toString(),
                                editShippingAddress.getText().toString(),
                                editPaymentMethod.getText().toString(),
                                editDelivered.getText().toString()
                        )
                );
                APICallDelete(Long.parseLong(ID),
                        message -> runOnUiThread(
                                () -> Toast.makeText(getApplicationContext(),
                                        message, Toast.LENGTH_LONG
                                ).show()
                        )
                );
                Toast.makeText(getApplicationContext(),
                                "DELETE OK", Toast.LENGTH_LONG)
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
            finishActivity(301);
            Intent i = new Intent(UpdateDelete.this, MainActivity.class);
            startActivity(i);
        });
    }
}
