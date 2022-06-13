package com.ftn.RedditClone.controller;

import com.ftn.RedditClone.model.entity.dto.CommunityDto;
import com.ftn.RedditClone.service.CommunityService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/community")
@AllArgsConstructor
public class CommunityController {

    private final CommunityService communityService;

    @PostMapping
    public ResponseEntity<CommunityDto> createCommunity(@RequestBody CommunityDto communityDto){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(communityService.save(communityDto));
    }

    @GetMapping
    public ResponseEntity<List<CommunityDto>> getAllCommunities(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(communityService.getAll());
    }
}
