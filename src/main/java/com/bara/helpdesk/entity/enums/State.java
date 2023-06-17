package com.bara.helpdesk.entity.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum State {
    DRAFT("Create"),
    NEW("Submit"),
    APPROVED("Approve"),
    DECLINED("Decline"),
    IN_PROGRESS("Assign to Me"),
    DONE("Done"),
    CANCELED("Cancel");

    private final String action;

    public static String getAction(State state){
        return state.action;
    }
}
