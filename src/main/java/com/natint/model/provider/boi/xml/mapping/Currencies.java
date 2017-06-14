package com.natint.model.provider.boi.xml.mapping;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class Currencies {
    private String last_update;
    private List<Currency> currencies;

    public Currencies() {
    }

    public Currencies(String last_update, List<Currency> currencies) {
        super();
        this.last_update = last_update;
        this.currencies = currencies;
    }

    @XmlElement
    public String getLast_update() {
        return last_update;
    }

    public void setLast_update(String last_update) {
        this.last_update = last_update;
    }

    @XmlElement(name = "currency")
    public List<Currency> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(List<Currency> currencies) {
        this.currencies = currencies;
    }
}
