package ru.practicum.ewm.main.category.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.main.category.model.Category;

@Repository
public interface CategoryRepository extends PagingAndSortingRepository<Category, Long> {
}
