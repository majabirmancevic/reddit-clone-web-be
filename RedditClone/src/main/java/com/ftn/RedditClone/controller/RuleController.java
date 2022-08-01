package com.ftn.RedditClone.controller;

import com.ftn.RedditClone.model.entity.Rule;
import com.ftn.RedditClone.model.entity.dto.RuleDto;
import com.ftn.RedditClone.service.RuleService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rules/")
@AllArgsConstructor
public class RuleController {

    @Autowired
    RuleService ruleService;

    @GetMapping("{id}")
    public ResponseEntity<RuleDto> getRule(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ruleService.findOne(id));
    }

    @GetMapping("byCommunity/{communityId}")
    public ResponseEntity<List<Rule>> getAllRules(@PathVariable Long communityId){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ruleService.findAllByCommunity(communityId));
    }

    @PostMapping
    public ResponseEntity<Rule> createRule(@Validated @RequestBody RuleDto rule){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ruleService.save(rule));
    }

    @PutMapping("{id}")
    public ResponseEntity<Rule> updateRule(@PathVariable Long id, @RequestBody RuleDto ruleDto) {

        RuleDto r = ruleService.findOne(id);

        if(r != null){
            ruleService.update(ruleDto,id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteRule(@PathVariable Long id) {

        Rule rule = ruleService.findRule(id);

        if(rule != null){
            ruleService.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
