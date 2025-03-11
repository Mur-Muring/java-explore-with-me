package ru.practicum.ewm.comments;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.comments.dto.CountCommentsByEventDto;
import ru.practicum.ewm.event.model.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByEvent_Id(Long eventId, Pageable pageable);

    List<Comment> findByAuthor_Id(Long userId);

    Optional<Comment> findByAuthor_IdAndId(Long userId, Long id);

    @Query("SELECT new ru.practicum.ewm.comments.dto.CountCommentsByEventDto(c.event.id, COUNT(c)) " +
            "FROM comments c WHERE c.event.id IN ?1 " +
            "GROUP BY c.event.id")
    List<CountCommentsByEventDto> countCommentByEvent(List<Long> eventIds);

    @Query("SELECT c FROM comments c WHERE LOWER(c.text) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<Comment> search(String text, Pageable pageable);

    @Query("SELECT new ru.practicum.ewm.comments.dto.CountCommentsByEventDto(c.event.id, COUNT(c)) " +
            "FROM comments c " +
            "WHERE c.event.id IN ?1 AND c.created BETWEEN ?2 AND ?3 " +
            "GROUP BY c.event.id")
    List<CountCommentsByEventDto> countCommentsByEventAndDateRange(List<Long> eventIds, LocalDateTime start, LocalDateTime end);

    // Метод для подсчета комментариев для события в указанном временном интервале
    long countByEventAndCreatedBetween(Event event, LocalDateTime start, LocalDateTime end);

}