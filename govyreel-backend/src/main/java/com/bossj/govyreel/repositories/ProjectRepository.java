package com.bossj.govyreel.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bossj.govyreel.entities.Project;

public interface ProjectRepository extends JpaRepository<Project, UUID> {
    List<Project> findAllByUserId(UUID userId);
    List<Project> findAllByUserIdOrderByLastModifiedDateDesc(UUID userId);
    Optional<Project> findByIdAndUserId(UUID projectId, UUID userId);
}