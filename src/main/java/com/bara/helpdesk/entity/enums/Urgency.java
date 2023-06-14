package com.bara.helpdesk.entity.enums;


public enum Urgency {
    LOW,
    AVERAGE,
    HIGH,
    CRITICAL;

    public static Integer getOrdinal(Urgency urgency) {
        if (urgency != null) {
            return urgency.ordinal();
        } else return null;
    }
}
