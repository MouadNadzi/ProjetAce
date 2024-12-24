package com.codegen.repository;

import com.codegen.model.entity.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TemplateRepository extends JpaRepository<Pattern, Long> {
    Optional<Pattern> findByPlatformAndName(String platform, String name);
}