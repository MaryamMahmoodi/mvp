package com.project.mvp.model.service.contract;

import com.project.mvp.model.entity.Url;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UrlMappingRepository extends JpaRepository<Url, Long>
{
    Optional<Url> findByShortCode(String shortCode);
}
