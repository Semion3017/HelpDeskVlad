package com.bara.helpdesk.entity.enums;

public enum State {
    DRAFT,
    NEW,
    APPROVED,
    DECLINED,
    IN_PROGRESS,
    DONE,
    CANCELED;

    public static String getActionName(State state) {
        return switch (state) {
            case DRAFT -> "Create";
            case NEW -> "Submit";
            case APPROVED -> "Approve";
            case DECLINED -> "Decline";
            case CANCELED -> "Cancel";
            case IN_PROGRESS -> "Assign to Me";
            case DONE -> "Done";
        };
    }
}
