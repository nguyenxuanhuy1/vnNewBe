package com.example.demo.controller;

import com.example.demo.dto.LinkYoutobeDto;
import com.example.demo.service.LinkYoutobeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/link-youtube")
public class LinkYoutobeController {

    @Autowired
    private LinkYoutobeService service;

    @PostMapping("/list")
    public List<LinkYoutobeDto> getAll() {
        return service.getAll();
    }

    @PostMapping("/create")
    public LinkYoutobeDto create(@RequestBody LinkYoutobeDto dto) {
        return service.create(dto);
    }

    @PostMapping("/update/{id}")
    public LinkYoutobeDto update(@PathVariable Long id, @RequestBody LinkYoutobeDto dto) {
        return service.update(id, dto);
    }

    @PostMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @PostMapping("/detail")
    public List<LinkYoutobeDto> search(@RequestParam String keyword) {
        return service.searchByTitle(keyword);
    }
}
