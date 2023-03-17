package ru.practicum.ewm.main.user.repository;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.main.user.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long>, QuerydslPredicateExecutor<User> {

    Optional<User> findByUserId(Long userId);

    Boolean existsByUserId(Long userId);

    void deleteByUserId(Long userId);
}
