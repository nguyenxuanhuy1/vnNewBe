package com.example.demo.service;

import com.example.demo.dto.LinkYoutobeDto;
import com.example.demo.dto.PageResponse;

public interface LinkYoutobeService {
    PageResponse<LinkYoutobeDto> getAll(int page, int size);
    LinkYoutobeDto create(LinkYoutobeDto dto);
    LinkYoutobeDto update(Long id, LinkYoutobeDto dto);
    void delete(Long id);
}
