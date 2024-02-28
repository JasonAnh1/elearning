package com.jason.elearning.entity.response;

import java.io.Serializable;

public class PaymentResponse implements Serializable {
    private String URL;

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }
}
