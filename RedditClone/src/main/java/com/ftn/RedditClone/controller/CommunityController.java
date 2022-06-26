package com.ftn.RedditClone.controller;

import com.ftn.RedditClone.mapper.CommunityMapper;
import com.ftn.RedditClone.model.entity.Community;
import com.ftn.RedditClone.model.entity.dto.CommunityDto;
import com.ftn.RedditClone.service.CommunityService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/community/")
@AllArgsConstructor
public class CommunityController {

    private final CommunityService communityService;
    private final CommunityMapper communityMapper;


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

    @GetMapping("{id}")
    public ResponseEntity<CommunityDto> getCommunity(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(communityService.getCommunity(id));
    }



    @DeleteMapping(value = "{id}")
    public ResponseEntity<Void> deleteCommunity(@PathVariable Long id){

        CommunityDto community = communityService.getCommunity(id);

        if(community != null){
            communityService.removeCommunity(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping(value = "edit/{id}")
    public ResponseEntity<CommunityDto> updateCommunity(@RequestBody CommunityDto communityDto, @PathVariable Long id){

        Community community = communityService.findCommunity(id);

        if (community == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if(communityDto.getDescription() != community.getDescription() && communityDto.getDescription() != "" ) {
            community.setDescription(communityDto.getDescription());
        }
        if(communityDto.getName() != community.getName() && communityDto.getName() != "") {
            community.setName(communityDto.getName());
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(communityService.save(communityMapper.mapSubredditToDto(community)));
    }

}
