package com.example.order;


import android.widget.EditText;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public interface OrderServerCommunication {
    interface ShowMessage {
        void Message(String message);
    }

    default void APICallDelete(long ID, ShowMessage m) {
        Thread t = new Thread(() -> {
            try {
                OkHttpClient client =
                        new OkHttpClient.Builder()
                                .build();
                Retrofit retrofit
                        = new Retrofit.Builder()
                        .baseUrl("http://192.168.0.156:8080")
                        .addConverterFactory(
                                GsonConverterFactory.create()
                        )
                        .client(client)
                        .build();
                OrderAPI api = retrofit.create(OrderAPI.class);
                Call<OrderAPI.message> deletedOrder =
                        api.api_delete_order(ID);
                Response<OrderAPI.message>
                        r = deletedOrder.execute();
                if (r.isSuccessful()) {
                    m.Message("DELETED RECORD IN SERVER WITH ID = " + ID);
                } else {
                    throw new RuntimeException("Server Error: "
                            + r.errorBody().string()
                    );
                }
            } catch (Exception e) {
                m.Message("Exception: " + e.getLocalizedMessage());
            }
        });
        t.start();
    }

    default void APICallUpdate(long ID,
                               EditText editFirstName, EditText editLastName, EditText editPhone, EditText editEmail,
                               EditText editShippingAddress, EditText editPaymentMethod, EditText editDelivered,
                               ShowMessage m

    ) {
        Thread t = new Thread(() -> {
            try {
                OkHttpClient client =
                        new OkHttpClient.Builder()
                                .build();
                Retrofit retrofit
                        = new Retrofit.Builder()
                        .baseUrl("http://192.168.0.156:8080")
                        .addConverterFactory(
                                GsonConverterFactory.create()
                        )
                        .client(client)
                        .build();
                OrderAPI api = retrofit.create(OrderAPI.class);
                Call<OrderAPI.orders> updatedOrder =
                        api.api_update_order(ID, new OrderAPI.orders(
                                ID,
                                editFirstName.getText().toString(),
                                editLastName.getText().toString(),
                                editPhone.getText().toString(),
                                editEmail.getText().toString(),
                                editShippingAddress.getText().toString(),
                                editPaymentMethod.getText().toString(),
                                editDelivered.getText().toString()
                        ));
                Response<OrderAPI.orders>
                        r = updatedOrder.execute();
                if (r.isSuccessful()) {
                    OrderAPI.orders resp = r.body();
                    m.Message("updated record in server with ID = " + resp.id);
                } else {
                    throw new RuntimeException("Server Error: "
                            + r.errorBody().string()
                    );
                }
            } catch (Exception e) {
                m.Message("Exception: " + e.getLocalizedMessage());
            }
        });
        t.start();
    }
}
