package ru.practicum.service.category.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.service.category.model.Category;

@Repository
public interface CategoryRepository extends PagingAndSortingRepository<Category, Long> {
}
