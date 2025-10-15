package com.example.demo.service;

import com.example.demo.dto.LinkYoutobeDto;

import java.util.List;
public interface LinkYoutobeService {
    List<LinkYoutobeDto> getAll();
    LinkYoutobeDto create(LinkYoutobeDto dto);
    LinkYoutobeDto update(Long id, LinkYoutobeDto dto);
    void delete(Long id);
    List<LinkYoutobeDto> searchByTitle(String keyword);
}
