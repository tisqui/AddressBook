package com.squirrel.addressbook;

/**
 * Created by squirrel on 12/2/15.
 */
public class Contact  {
    private int _id;
    private String _name;
    private String _email;
    private String _phone;
    private String _address;

    public Contact(int _id, String _name, String _email, String _phone, String _address) {
        this._id = _id;
        this._name = _name;
        this._email = _email;
        this._phone = _phone;
        this._address = _address;
    }

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public String getName() {
        return _name;
    }

    public void setName(String _name) {
        this._name = _name;
    }

    public String getEmail() {
        return _email;
    }

    public void setEmail(String _email) {
        this._email = _email;
    }

    public String getPhone() {
        return _phone;
    }

    public void setPhone(String _phone) {
        this._phone = _phone;
    }

    public String getAddress() {
        return _address;
    }

    public void setAddress(String _address) {
        this._address = _address;
    }
}
