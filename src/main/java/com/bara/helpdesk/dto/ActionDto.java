package com.bara.helpdesk.dto;

import com.bara.helpdesk.entity.enums.State;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ActionDto {
    private String name;

    private String state;

    public ActionDto (State state) {
       this.name = State.getAction(state);
       this.state = state.toString();
    }
}
