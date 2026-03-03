package com.nelumbo.open_flow_coworking.repository;

import com.nelumbo.open_flow_coworking.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<User> {
    @Query("SELECT u FROM User u WHERE " +
            "(LOWER(u.name) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            " LOWER(u.email) LIKE LOWER(CONCAT('%', :q, '%'))) AND " +
            "u.recycle = false")
    Page<User> searchByQuery(@Param("q") String q, Pageable pageable);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
