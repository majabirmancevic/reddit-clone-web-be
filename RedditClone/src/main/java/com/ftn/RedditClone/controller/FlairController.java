package com.ftn.RedditClone.controller;

import com.ftn.RedditClone.model.entity.Flair;
import com.ftn.RedditClone.service.FlairService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flairs/")
@AllArgsConstructor
public class FlairController {

    @Autowired
    FlairService flairService;

    @PostMapping
    public ResponseEntity<Flair> createFlair(@RequestBody Flair flair){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(flairService.saveFlair(flair));
    }

    @GetMapping
    public ResponseEntity<List<Flair>> getAllFlairs(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(flairService.getAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Flair> getFlair(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(flairService.getOne(id));
    }


    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteFlair(@PathVariable Long id) {

        Flair flair = flairService.getOne(id);

        if(flair != null){
            flairService.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<Flair> putFlair(@PathVariable Long id, @RequestBody Flair flair) {

        Flair f = flairService.getOne(id);

        if(f != null){
            flairService.update(flair,id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
