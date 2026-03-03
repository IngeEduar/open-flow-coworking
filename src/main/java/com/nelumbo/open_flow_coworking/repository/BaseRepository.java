package com.nelumbo.open_flow_coworking.repository;

import com.nelumbo.open_flow_coworking.entity.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import java.util.Optional;
import java.util.UUID;

@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity> extends JpaRepository<T, UUID> {
    Optional<T> findByIdAndRecycleFalse(UUID id);
    Page<T> findAllByRecycleFalse(Pageable pageable);
}