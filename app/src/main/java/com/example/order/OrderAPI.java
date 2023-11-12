package com.example.order;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface OrderAPI {
    public class message {
        public String status;

        public message(message copy) {
            this.status = copy.status;
        }
    }

    public class orders {
        public long id;
        public String firstName;
        public String lastName;
        public String phone;
        public String email;
        public String shippingAddress;
        public String paymentMethod;
        public String delivered;

        public orders(orders copy) {
            this.id = copy.id;
            this.firstName = copy.firstName;
            this.lastName = copy.lastName;
            this.phone = copy.phone;
            this.email = copy.email;
            this.shippingAddress = copy.shippingAddress;
            this.paymentMethod = copy.paymentMethod;
            this.delivered = copy.delivered;
        }

        public orders(long _ID, String _firstName, String _lastName, String _phone, String _email, String _shippingAddress, String _paymentMethod, String _delivered) {
            id = _ID;
            firstName = _firstName;
            lastName = _lastName;
            phone = _phone;
            email = _email;
            shippingAddress = _shippingAddress;
            paymentMethod = _paymentMethod;
            delivered = _delivered;
        }
    }

    @GET("/api/orders")
    public Call<List<orders>> api_users();

    @GET("/api/orders/{id}")
    public Call<orders> api_get_order(@Path("id") long id);

    @POST("/api/orders/add/{id}")
    public Call<orders> api_add_order(@Path("id") long id, @Body orders c);

    @PUT("/api/orders/update")
    public Call<orders> api_update_order(@Path("id") long id, @Body orders c);

    @DELETE("/api/orders/delete/{id}")
    public Call<message> api_delete_order(@Path("id") long id);
}
