package com.mls.task.core.model.response;

import java.util.Date;
import java.util.Objects;

public class PartResponseModel {

    private String name;

    private String number;

    private String vendor;

    private Integer qty;

    private Date shipped;

    private Date received;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Date getShipped() {
        return shipped;
    }

    public void setShipped(Date shipped) {
        this.shipped = shipped;
    }

    public Date getReceived() {
        return received;
    }

    public void setReceived(Date received) {
        this.received = received;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PartResponseModel that = (PartResponseModel) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(number, that.number) &&
                Objects.equals(vendor, that.vendor) &&
                Objects.equals(qty, that.qty) &&
                Objects.equals(shipped, that.shipped) &&
                Objects.equals(received, that.received);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, number, vendor, qty, shipped, received);
    }
}
