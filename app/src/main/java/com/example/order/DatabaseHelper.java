package com.example.order;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "orders.db";
    public static final int DBVERSION = 1;

    private final SQLiteDatabase _myDB;

    public static String DBCREATE = "" +
            "CREATE TABLE orders( " +
            "ID integer PRIMARY KEY AUTOINCREMENT, " +
            "FirstName text not null, " +
            "LastName text not null, " +
            "Phone text not null, " +
            "Email text not null, " +
            "ShippingAddress text not null, " +
            "PaymentMethod text not null, " +
            "Delivered text not null, " +
            "UNIQUE(ID) " +
            ");" +
            "";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DBNAME, null, DBVERSION);
        _myDB = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DBCREATE);

    }

    public long insert(String FirstName, String LastName, String Phone, String Email,
                       String ShippingAddress, String PaymentMethod, String Delivered) {
        ContentValues values = new ContentValues();
        values.put("FirstName", FirstName);
        values.put("LastName", LastName);
        values.put("Phone", Phone);
        values.put("Email", Email);
        values.put("ShippingAddress", ShippingAddress);
        values.put("PaymentMethod", PaymentMethod);
        values.put("Delivered", Delivered);
        return _myDB.insert("orders", null, values);
    }

    public void update(Orders c) {
        String updateQ = "UPDATE orders SET " +
                "FirstName = ?, LastName = ?, Phone = ?, Email = ?, ShippingAddress = ?, PaymentMethod = ?, Delivered = ?" +
                " WHERE  ID = ? ";
        _myDB.execSQL(updateQ, new Object[]{
                c.getFirstName(), c.getLastName(), c.getPhone(), c.getEmail(), c.getShippingAddress(), c.getPaymentMethod(), c.getDelivered(), c.getID()
        });
    }

    public void delete(Orders c) {
        String deleteQ = "DELETE FROM orders WHERE ID = ?; ";
        _myDB.execSQL(deleteQ, new Object[]{c.getID()});
    }

    public List<Orders> select() {
        String selectQ = "SELECT * FROM orders ORDER BY Delivered; ";
        Cursor c = _myDB.rawQuery(selectQ, null);
        List<Orders> l = new ArrayList<>();
        while (c.moveToNext()) {
            @SuppressLint("Range") Orders orders = new Orders(
                    c.getString(c.getColumnIndex("ID")),
                    c.getString(c.getColumnIndex("FirstName")),
                    c.getString(c.getColumnIndex("LastName")),
                    c.getString(c.getColumnIndex("Phone")),
                    c.getString(c.getColumnIndex("Email")),
                    c.getString(c.getColumnIndex("ShippingAddress")),
                    c.getString(c.getColumnIndex("PaymentMethod")),
                    c.getString(c.getColumnIndex("Delivered"))
            );
            l.add(orders);
        }
        return l;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        if (i1 > i) {
            sqLiteDatabase.execSQL("DROP TABLE orders; ");
            sqLiteDatabase.execSQL(DBCREATE);
        }
    }
}
