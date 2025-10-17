package com.example.demo.controller;

import com.example.demo.dto.LinkYoutobeDto;
import com.example.demo.dto.PageResponse;
import com.example.demo.service.LinkYoutobeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/link-youtube")
public class LinkYoutobeController {

    @Autowired
    private LinkYoutobeService service;

    @GetMapping("/list")
    public PageResponse<LinkYoutobeDto> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return service.getAll(page, size);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public LinkYoutobeDto create(@RequestBody LinkYoutobeDto dto) {
        return service.create(dto);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/update/{id}")
    public LinkYoutobeDto update(@PathVariable Long id, @RequestBody LinkYoutobeDto dto) {
        return service.update(id, dto);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

}
