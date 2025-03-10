package ru.practicum.ewm.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.ValidatetionConflict;
import ru.practicum.ewm.compilation.Compilation;
import ru.practicum.ewm.compilation.CompilationMapper;
import ru.practicum.ewm.compilation.CompilationRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationDto;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompilationServiceBase implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Transactional
    @Override
    public CompilationDto create(NewCompilationDto compilationDto) {
        Compilation compilation = CompilationMapper.toCompilation(compilationDto);
        compilation.setPinned(Optional.ofNullable(compilation.getPinned()).orElse(false));

        Set<Long> eventIds = (compilationDto.getEvents() != null) ? compilationDto.getEvents() : Collections.emptySet();
        Set<Event> events = new HashSet<>(eventRepository.findAllByIdIn(new ArrayList<>(eventIds)));
        compilation.setEvents(events);

        return CompilationMapper.toDto(compilationRepository.save(compilation));
    }

    @Transactional
    @Override
    public CompilationDto update(Long compId, UpdateCompilationDto update) {
        Compilation compilation = getCompilation(compId);

        if (update.getEvents() != null) {
            Set<Event> eventSet = new HashSet<>(eventRepository.findAllByIdIn(new ArrayList<>(update.getEvents())));
            compilation.setEvents(eventSet);
        }

        compilation.setPinned(Optional.ofNullable(update.getPinned()).orElse(compilation.getPinned()));

        if (update.getTitle() != null) {
            if (update.getTitle().isBlank()) {
                throw new ValidatetionConflict("Title не может состоять из пробелов");
            }
            compilation.setTitle(update.getTitle());
        }

        return CompilationMapper.toDto(compilation);
    }

    @Transactional
    @Override
    public void delete(Long compId) {
        if (!compilationRepository.existsById(compId)) {
            throw new NotFoundException("Compilation с id = " + compId + " не найден");
        }
        compilationRepository.deleteById(compId);
    }

    @Override
    public List<CompilationDto> get(Boolean pinned, Integer from, Integer size) {
        PageRequest pageRequest = PageRequest.of(from, size);
        List<Compilation> compilations = (pinned == null)
                ? compilationRepository.findAll(pageRequest).getContent()
                : compilationRepository.findAllByPinned(pinned, pageRequest);

        return compilations.stream()
                .map(CompilationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto findById(Long compId) {
        return CompilationMapper.toDto(getCompilation(compId));
    }

    private Compilation getCompilation(Long compId) {
        return compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation с id = " + compId + " не найден"));
    }
}