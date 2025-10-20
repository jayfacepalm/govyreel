package com.bossj.govyreel.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bossj.govyreel.entities.RoleToUserMapping;

public interface RoleToUserMappingRepository extends JpaRepository<RoleToUserMapping, UUID> {
}
