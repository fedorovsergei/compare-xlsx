package com.fedorov.demo.controller;

import com.fedorov.demo.entity.CorrectPosition;
import com.fedorov.demo.parse.Parse1cFile;
import com.fedorov.demo.parse.ParsePepsiFile;
import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final ParsePepsiFile parsePepsiFile;
    private final Parse1cFile parse1cFile;

    @GetMapping
    @RequestMapping("compare")
    public Set<CorrectPosition> test(@RequestParam("filePepsi") String filePepsi, @RequestParam("file1C") String file1C) {
        Set<CorrectPosition> set1 = new HashSet<>(parsePepsiFile.parse(filePepsi).values());
        Set<CorrectPosition> set2 = new HashSet<>(parse1cFile.parse(file1C).values());
        Set<CorrectPosition> set3 = Sets.difference(set1, set2);
        System.out.println(set1);
        System.out.println(set2);
        System.out.println(set3);
        return set3;
    }
}
