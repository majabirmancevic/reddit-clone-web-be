package com.ftn.RedditClone.controller;

import com.ftn.RedditClone.mapper.CommunityMapper;
import com.ftn.RedditClone.model.entity.Community;
import com.ftn.RedditClone.model.entity.dto.CommunityDto;
import com.ftn.RedditClone.repository.CommunityRepository;
import com.ftn.RedditClone.service.CommunityService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/community/")
@AllArgsConstructor
public class CommunityController {

    @Autowired
    CommunityService communityService;
    @Autowired
    CommunityMapper communityMapper;
    @Autowired
    CommunityRepository communityRepository;


    @PostMapping
    public ResponseEntity<CommunityDto> createCommunity(@Valid @RequestBody CommunityDto communityDto){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(communityService.save(communityDto));
    }

    @GetMapping
    public ResponseEntity<List<CommunityDto>> getAllCommunities(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(communityService.getAll());
    }

    @GetMapping(value="all")
    public ResponseEntity<List<CommunityDto>> getAllNotDeletedCommunities(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(communityService.getAllNotDeleted());
    }

    @GetMapping("{id}")
    public ResponseEntity<CommunityDto> getCommunity(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(communityService.getCommunity(id));
    }

    @GetMapping("byName/{name}")
    public ResponseEntity<CommunityDto> getCommunityByName(@PathVariable String name) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(communityService.getCommunityByName(name));
    }


    @PostMapping(value = "{id}")
    public ResponseEntity<Community> deleteCommunity(@PathVariable Long id, @RequestBody String suspendedReason){

        Community community = communityService.findCommunity(id);

        if(community != null){
            return ResponseEntity.status(HttpStatus.OK)
                    .body(communityService.removeCommunity(id, suspendedReason));
        } else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping(value = "edit/{id}")
    public ResponseEntity<CommunityDto> updateCommunity(@Valid @RequestBody CommunityDto communityDto, @PathVariable Long id){

        Community community = communityService.findCommunity(id);

        if (community == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else{
            communityService.editCommunity(communityDto,id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(communityMapper.mapSubredditToDto(community));
        }

    }



}
