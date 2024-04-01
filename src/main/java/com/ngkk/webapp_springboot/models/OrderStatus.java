package com.ngkk.webapp_springboot.models;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PUBLIC, makeFinal = true)
public class OrderStatus {
    static String PENDING = "PENDING";
    static String PROCESS = "PROCESS";
    static String SHIPPED = "SHIPPED";
    static String DELIVERED = "DELIVERED";
    static String CANCELLED = "CANCELLED";

}
