package com.bara.helpdesk.repository.specification.ticket;

import com.bara.helpdesk.entity.Ticket;
import com.bara.helpdesk.entity.Ticket_;
import com.bara.helpdesk.entity.User;
import com.bara.helpdesk.entity.enums.Role;
import com.bara.helpdesk.entity.enums.State;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class TicketSpecifications {

    public static Specification<Ticket> filterAllByUser(User user, Boolean isAll) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (Role.OWNER.equals(user.getRole())) {
                predicates.add(cb.equal(root.get(Ticket_.OWNER), user));
            } else if (Role.MANAGER.equals(user.getRole())) {
                    if (isAll) {
                        predicates.add(cb.equal(root.get(Ticket_.STATE), State.NEW));
                    }
                    predicates.add(cb.equal(root.get(Ticket_.OWNER), user));
                    predicates.add(cb.equal(root.get(Ticket_.APPROVER), user));
            } else if (Role.ENGINEER.equals(user.getRole())) {
                    if (isAll) {
                        predicates.add(cb.equal(root.get(Ticket_.STATE), State.APPROVED));
                    }
                    predicates.add(cb.equal(root.get(Ticket_.ASSIGNEE), user));
            }
            return cb.or(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Ticket> ticketFieldsLikeKeyword(String keyword) {
        String key = "%" + keyword.toLowerCase() + "%";
        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get(Ticket_.NAME).as(String.class)), key),
                cb.like(root.get(Ticket_.DESIRED_RESOLUTION_DATE), key),
                cb.like(cb.lower(root.get(Ticket_.URGENCY).as(String.class)), key),
                cb.like(cb.lower(root.get(Ticket_.STATE).as(String.class)), key)
        );
    }
}
