package ru.practicum.ewm.comments.service;

import ru.practicum.ewm.comments.dto.CommentInDto;
import ru.practicum.ewm.comments.dto.CommentOutDto;

import java.util.List;

public interface CommentService {
    CommentOutDto create(Long userId, Long eventId, CommentInDto commentDto);

    CommentOutDto update(Long userId, Long commentId, CommentInDto updateCommentDto);

    List<CommentOutDto> getByUser(Long userId);

    CommentOutDto getByUserAndCommentId(Long userId, Long commentId);

    List<CommentOutDto> getByEvent(Long eventId, Integer from, Integer size);

    void delete(Long userId, Long commentId);

    void deleteAdmin(Long commentId);

    List<CommentOutDto> search(String text, Integer from, Integer size);
}