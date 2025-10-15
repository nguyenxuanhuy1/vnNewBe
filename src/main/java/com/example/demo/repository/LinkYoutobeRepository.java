package com.example.demo.repository;

import com.example.demo.entity.LinkYoutobe;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LinkYoutobeRepository extends JpaRepository<LinkYoutobe, Long> {
    List<LinkYoutobe> findByTitleContainingIgnoreCase(String title);
}
