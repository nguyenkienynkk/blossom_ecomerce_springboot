package com.ngkk.webapp_springboot.models;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

public class OrderStatus {
    public static final String PENDING = "PENDING";
     public static final String PROCESS = "PROCESS";
     public static final String SHIPPED = "SHIPPED";
     public static final String DELIVERED = "DELIVERED";
     public static final String CANCELLED = "CANCELLED";

}
