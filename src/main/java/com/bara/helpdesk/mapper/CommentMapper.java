package com.bara.helpdesk.mapper;

import com.bara.helpdesk.dto.CommentInputDto;
import com.bara.helpdesk.dto.CommentOutputDto;
import com.bara.helpdesk.entity.Comment;

import java.time.LocalDateTime;


public class CommentMapper {

    public static CommentOutputDto toDto(Comment comment){
        return CommentOutputDto.builder()
                .date(comment.getDate())
                .email(comment.getUser().getEmail())
                .text(comment.getText())
                .id(comment.getId())
                .build();
    }

    public static Comment toEntity(CommentInputDto dto){
       return Comment.builder()
                .date(LocalDateTime.now())
                .text(dto.getText())
                .build();
    }


}
