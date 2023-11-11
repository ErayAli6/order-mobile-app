package com.example.order;

public class Orders {
    protected String ID, FirstName, LastName, Phone, Email, ShippingAddress, PaymentMethod, Delivered;

    public Orders() {
        ID = "";
        FirstName = "";
        LastName = "";
        Phone = "";
        Email = "";
        ShippingAddress = "";
        PaymentMethod = "";
        Delivered = "";
    }

    public Orders(String ID, String FirstName, String LastName, String Phone, String Email, String ShippingAddress, String PaymentMethod, String Delivered) {
        this.ID = ID;
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.Phone = Phone;
        this.Email = Email;
        this.ShippingAddress = ShippingAddress;
        this.PaymentMethod = PaymentMethod;
        this.Delivered = Delivered;
    }

    public String getID() {
        return ID;
    }

    public String getFirstName() {
        return FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    public String getPhone() {
        return Phone;
    }

    public String getEmail() {
        return Email;
    }

    public String getShippingAddress() {
        return ShippingAddress;
    }

    public String getPaymentMethod() {
        return PaymentMethod;
    }

    public String getDelivered() {
        return Delivered;
    }
}
