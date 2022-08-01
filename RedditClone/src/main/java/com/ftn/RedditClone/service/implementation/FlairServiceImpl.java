package com.ftn.RedditClone.service.implementation;

import com.ftn.RedditClone.exceptions.SpringRedditException;
import com.ftn.RedditClone.model.entity.Flair;
import com.ftn.RedditClone.repository.FlairRepository;
import com.ftn.RedditClone.service.FlairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlairServiceImpl implements FlairService {

    @Autowired
    FlairRepository flairRepository;

    @Override
    public Flair saveFlair(Flair flair) {
        return flairRepository.save(flair);
    }

    @Override
    public void delete(Long id) {
        Flair f = flairRepository.findById(id).orElseThrow(() -> new SpringRedditException("No community found with ID - " + id));
        if (f != null){
            flairRepository.deleteById(id);
        }
    }

    @Override
    public Flair update(Flair flair, Long id) {

        Flair f = flairRepository.findById(id).orElseThrow(() -> new SpringRedditException("No flair found with ID - " + id));
        if (f != null){
            return flairRepository.save(flair);
        }
        return null;

    }

    @Override
    public List<Flair> getAll() {
        return flairRepository.findAll();
    }

    @Override
    public Flair getOne(Long id) {
        return flairRepository.findById(id).orElseThrow(() -> new SpringRedditException("No flair found with ID - " + id));
    }
}
