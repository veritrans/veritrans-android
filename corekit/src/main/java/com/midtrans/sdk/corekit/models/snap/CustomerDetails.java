package com.midtrans.sdk.corekit.models.snap;

/**
 * @author rakawm
 */
public class CustomerDetails {
    private String name;
    private String email;
    private String phone;
    private String address;

    public CustomerDetails() {

    }

    public CustomerDetails(String name, String phone, String email, String address) {
        setName(name);
        setPhone(phone);
        setEmail(email);
        setAddress(address);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
