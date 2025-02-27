package ru.practicum.ewm.category;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.category.dto.CategoryInDto;
import ru.practicum.ewm.category.dto.CategoryOutDto;

@UtilityClass
public class CategoryMapper {
    public static CategoryOutDto toCategoryOutDto(Category category) {
        return CategoryOutDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public Category toCategory(CategoryInDto categoryInDto) {
        return new Category(null,
                categoryInDto.getName());
    }
}