package com.mls.task.core.model.search;

import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

public class PartSearchModel {

    private final Optional<String> name;

    private final Optional<String> number;

    private final Optional<String> vendor;

    private final Optional<Integer> qty;

    private final Optional<Date> shippedAfter;

    private final Optional<Date> shippedBefore;

    private final Optional<Date> receivedAfter;

    private final Optional<Date> receivedBefore;


    public PartSearchModel(final String name, final String number, final String vendor, final String qty, final String shippedAfter, final String shippedBefore, final String receivedAfter, final String receivedBefore) {
        this.name = ofNullable(name);
        this.number = ofNullable(number);
        this.vendor = ofNullable(vendor);
        this.qty = toInteger(qty);
        this.shippedAfter = toDate(shippedAfter);
        this.shippedBefore = toDate(shippedBefore);
        this.receivedAfter = toDate(receivedAfter);
        this.receivedBefore = toDate(receivedBefore);
    }


    public Optional<String> getName() {
        return name;
    }

    public Optional<String> getNumber() {
        return number;
    }

    public Optional<String> getVendor() {
        return vendor;
    }

    public Optional<Integer> getQty() {
        return qty;
    }

    public Optional<Date> getShippedAfter() {
        return shippedAfter;
    }

    public Optional<Date> getShippedBefore() {
        return shippedBefore;
    }

    public Optional<Date> getReceivedAfter() {
        return receivedAfter;
    }

    public Optional<Date> getReceivedBefore() {
        return receivedBefore;
    }

    public boolean isEmpty() {
        return !(name.isPresent() || number.isPresent() || vendor.isPresent() || qty.isPresent()
                || shippedAfter.isPresent() || shippedBefore.isPresent() || receivedAfter.isPresent() || receivedBefore.isPresent());
    }

    private Optional<Date> toDate(final String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        try {
            return ofNullable(formatter.parse(date));
        } catch (Exception e) {
            //Don't need to handle
            return Optional.empty();
        }

    }

    private Optional<Integer> toInteger(final String str) {
        return str == null || str.equals("") ? Optional.empty() : of(Integer.valueOf(str));
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PartSearchModel.class.getSimpleName() + "[", "]")
                .add("name=" + name)
                .add("number=" + number)
                .add("vendor=" + vendor)
                .add("qty=" + qty)
                .add("shippedAfter=" + shippedAfter)
                .add("shippedBefore=" + shippedBefore)
                .add("receivedAfter=" + receivedAfter)
                .add("receivedBefore=" + receivedBefore)
                .toString();
    }
}
