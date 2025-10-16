package com.example.demo.service.Impl;

import com.example.demo.dto.LinkYoutobeDto;
import com.example.demo.entity.LinkYoutobe;
import com.example.demo.repository.LinkYoutobeRepository;
import com.example.demo.service.LinkYoutobeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LinkYoutobeServiceImpl implements LinkYoutobeService {

    @Autowired
    private LinkYoutobeRepository repository;

    @Override
    public List<LinkYoutobeDto> getAll() {
        return repository.findAll().stream()
                .map(l -> new LinkYoutobeDto(l.getId(), l.getTitle(), l.getLink()))
                .toList();
    }

    @Override
    public LinkYoutobeDto create(LinkYoutobeDto dto) {
        LinkYoutobe link = new LinkYoutobe();
        link.setTitle(dto.getTitle());
        link.setLink(dto.getLink());
        repository.save(link);
        return new LinkYoutobeDto(link.getId(), link.getTitle(), link.getLink());
    }

    @Override
    public LinkYoutobeDto update(Long id, LinkYoutobeDto dto) {
        LinkYoutobe link = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Link not found"));

        link.setTitle(dto.getTitle());
        link.setLink(dto.getLink());
        repository.save(link);

        return new LinkYoutobeDto(link.getId(), link.getTitle(), link.getLink());
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Link not found");
        }
        repository.deleteById(id);
    }
}
