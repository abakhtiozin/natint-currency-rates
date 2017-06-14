package com.natint.model.provider.boi.xml.mapping;

public class Currency {
    private String currencycode;
    private String rate;

    public Currency() {
    }

    public Currency(String currencycode, String rate) {
        this.currencycode = currencycode;
        this.rate = rate;
    }

    public String getCurrencycode() {
        return currencycode;
    }

    public void setCurrencycode(String currencycode) {
        this.currencycode = currencycode;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
