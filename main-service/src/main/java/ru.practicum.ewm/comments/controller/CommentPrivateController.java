package ru.practicum.ewm.comments.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comments.service.CommentService;
import ru.practicum.ewm.comments.dto.CommentInDto;
import ru.practicum.ewm.comments.dto.CommentOutDto;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/comments")
public class CommentPrivateController {
    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentOutDto create(@PathVariable Long userId,
                                @RequestParam Long eventId,
                                @Valid @RequestBody CommentInDto commentInDto) {
        log.info("==> Добавление комментария: {} к событию = {} от пользователя = {}", commentInDto, eventId, userId);
        return commentService.create(userId, eventId, commentInDto);
    }

    @PatchMapping("/{commentId}")
    public CommentOutDto update(@PathVariable Long userId,
                                @PathVariable Long commentId,
                                @Valid @RequestBody CommentInDto updateCommentDto) {
        log.info("==> Обновление пользователем с userId = {} комментария с commentId = {}", userId, commentId);
        return commentService.update(userId, commentId, updateCommentDto);
    }

    @GetMapping
    public List<CommentOutDto> getByUser(@PathVariable Long userId) {
        log.info("==> Получение комментариев пользователя с userId = {}", userId);
        return commentService.getByUser(userId);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long userId, @PathVariable Long commentId) {
        log.info("==> Удаление комментария id = {} пользователем id = {}", commentId, userId);
        commentService.delete(userId, commentId);
    }

    @GetMapping("/{commentId}")
    public CommentOutDto get(@PathVariable Long userId, @PathVariable Long commentId) {
        log.info("==> Получение комментария id = {} пользователем id = {}", commentId, userId);
        return commentService.getByUserAndCommentId(userId, commentId);
    }
}