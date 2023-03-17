package ru.practicum.ewm.main.category.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.main.category.model.Category;

import java.util.Optional;

@Repository
public interface CategoryRepository extends PagingAndSortingRepository<Category, Long> {

    Optional<Category> findByCategoryId(Long categoryId);

    void deleteByCategoryId(Long categoryId);

    Boolean existsByCategoryId(Long categoryId);
}
