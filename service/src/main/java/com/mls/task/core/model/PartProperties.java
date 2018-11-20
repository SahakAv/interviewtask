package com.mls.task.core.model;

public enum PartProperties {

    NAME("name"),

    NUMBER("number"),

    VENDOR("vendor"),

    QTY("qty"),

    SHIPPED("shipped"),

    SHIPPED_AFTER("shipped_after"),

    SHIPPED_BEFORE("shipped_before"),

    RECEIVED("received"),

    RECEIVED_AFTER("received_after"),

    RECEIVED_BEFORE("received_before");


    private String columnName;

     PartProperties(String columnName) {
        this.columnName = columnName;
    }


    public String getColumnName() {
        return columnName;
    }
}
