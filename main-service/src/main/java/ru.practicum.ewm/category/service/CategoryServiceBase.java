package ru.practicum.ewm.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.Category;
import ru.practicum.ewm.category.CategoryMapper;
import ru.practicum.ewm.category.CategoryRepository;
import ru.practicum.ewm.category.dto.CategoryInDto;
import ru.practicum.ewm.category.dto.CategoryOutDto;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.ValidatetionConflict;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceBase implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional(readOnly = true)
    public CategoryOutDto getById(Long id) {
        return CategoryMapper.toCategoryOutDto(findCategory(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryOutDto> get(Integer from, Integer size) {
        PageRequest pageRequest = PageRequest.of(from / size, size);

        return categoryRepository.findAll(pageRequest)
                .stream()
                .map(CategoryMapper::toCategoryOutDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CategoryOutDto create(CategoryInDto categoryInDto) {
        return CategoryMapper.toCategoryOutDto(categoryRepository.save(CategoryMapper.toCategory(categoryInDto)));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (eventRepository.existsById(id)) {
            throw new ValidatetionConflict("Нельзя удалить категорию с id= " + id + " :существуют активные задачи");
        }
        categoryRepository.deleteById(id);
    }

    @Override
    @Transactional
    public CategoryOutDto update(Long id, CategoryInDto categoryInDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Категория с id= " + id + " не найдена"));

        if (categoryInDto.getName() != null && !category.getName().equals(categoryInDto.getName())) {
            category.setName(categoryInDto.getName());

            try {
                category = categoryRepository.save(category);
            } catch (DataIntegrityViolationException e) {
                throw new ValidatetionConflict("Категория с названием " + categoryInDto.getName() + " уже существует");
            }
        }

        return CategoryMapper.toCategoryOutDto(category);
    }

    private Category findCategory(Long id) {
        return categoryRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Категории с id = " + id + " не существует"));
    }
}
