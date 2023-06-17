package com.bara.helpdesk.service.impl;

import com.bara.helpdesk.entity.Ticket;
import com.bara.helpdesk.entity.enums.Role;
import com.bara.helpdesk.entity.enums.State;
import com.bara.helpdesk.service.MailService;
import com.bara.helpdesk.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MailServiceImpl.class);

    private final TemplateEngine htmlTemplateEngine;
    private final JavaMailSender mailSender;
    private final UserService userService;

    @Override
    @Async
    public void sendTicketStateChangeMessage(Ticket ticket, State oldState) {
        State newState = ticket.getState();
        switch (newState) {
            case NEW -> {
                MessageData messageData = MessageData.builder()
                        .messageSubject("New ticket for approval")
                        .ticket(ticket)
                        .template("TicketForApprovalTemplate")
                        .build();
                userService.getByRole(Role.MANAGER).forEach(manager -> {
                    sendSimpleTicketMessage(messageData, manager.getEmail());
                });
            }
            case APPROVED -> {
                MessageData messageData = MessageData.builder()
                        .messageSubject("New approved ticket")
                        .ticket(ticket)
                        .template("NewApprovedTicketTemplate")
                        .build();
                userService.getByRole(Role.ENGINEER).forEach(engineer -> {
                    sendSimpleTicketMessage(messageData, engineer.getEmail());
                });
                messageData.setTemplate("TicketWasApprovedTemplate");
                messageData.setUsername(ticket.getOwner().getFirstName() + " " + ticket.getOwner().getLastName());
                sendSimpleTicketMessage(messageData, ticket.getOwner().getEmail());
            }
            case DECLINED -> {
                MessageData messageData = MessageData.builder()
                        .messageSubject("Your ticket was declined")
                        .username(ticket.getOwner().getFirstName() + " " + ticket.getOwner().getLastName())
                        .ticket(ticket)
                        .template("TicketWasDeclinedTemplate")
                        .build();
                sendSimpleTicketMessage(messageData, ticket.getOwner().getEmail());
            }
            case CANCELED -> {
                if (State.NEW.equals(oldState)) {
                    MessageData messageData = MessageData.builder()
                            .messageSubject("Your ticket was canceled")
                            .username(ticket.getOwner().getFirstName() + " " + ticket.getOwner().getLastName())
                            .ticket(ticket)
                            .template("TicketWasCanceledByManagerTemplate")
                            .build();
                    sendSimpleTicketMessage(messageData, ticket.getOwner().getEmail());
                }
                if (State.APPROVED.equals(oldState)) {
                    MessageData messageData = MessageData.builder()
                            .messageSubject("Your ticket was canceled")
                            .username(ticket.getOwner().getFirstName() + " " + ticket.getOwner().getLastName())
                            .ticket(ticket)
                            .template("TicketWasCanceledByEngineerTemplate")
                            .build();
                    sendSimpleTicketMessage(messageData, ticket.getOwner().getEmail());
                    messageData.setUsername(ticket.getApprover().getFirstName() + " " + ticket.getApprover().getLastName());
                    messageData.setMessageSubject("Ticket was Canceled");
                    sendSimpleTicketMessage(messageData, ticket.getApprover().getEmail());
                }
            }
            case IN_PROGRESS -> {
                MessageData messageData = MessageData.builder()
                        .messageSubject("Your ticket was taken to processing")
                        .username(ticket.getOwner().getFirstName() + " " + ticket.getOwner().getLastName())
                        .ticket(ticket)
                        .template("TicketWasTakenToProcessingTemplate")
                        .build();
                sendSimpleTicketMessage(messageData, ticket.getOwner().getEmail());
            }
            case DONE -> {
                MessageData messageData = MessageData.builder()
                        .messageSubject("Your ticket was done")
                        .username(ticket.getOwner().getFirstName() + " " + ticket.getOwner().getLastName())
                        .ticket(ticket)
                        .template("TicketWasDoneTemplate")
                        .build();
                sendSimpleTicketMessage(messageData, ticket.getOwner().getEmail());
            }
        }
    }

    private void sendSimpleTicketMessage(MessageData data, String recipient) {
        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper email;
        try {
            email = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            email.setTo(recipient);
            email.setSubject(data.messageSubject);
            email.setFrom(new InternetAddress("helpdeskmailsender@gmail.com"));

            final Context ctx = new Context(LocaleContextHolder.getLocale());
            ctx.setVariable("username", data.username + ",");
            ctx.setVariable("name", data.ticket.getName());
            ctx.setVariable("ticketId", "http://localhost:3000/ticket/" + data.ticket.getId());

            final String htmlContent = this.htmlTemplateEngine.process(data.template, ctx);

            email.setText(htmlContent, true);
        } catch (MessagingException e) {
            LOGGER.error(e.getLocalizedMessage(), e);
        }
        mailSender.send(mimeMessage);
    }

    @Builder
    @Getter
    @Setter
    private static class MessageData {
        String messageSubject;
        String username;
        Ticket ticket;
        String template;
    }
}
