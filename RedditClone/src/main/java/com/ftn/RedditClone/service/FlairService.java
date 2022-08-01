package com.ftn.RedditClone.service;

import com.ftn.RedditClone.model.entity.Flair;

import java.util.List;

public interface FlairService {

    Flair saveFlair(Flair flair);
    void delete(Long id);
    Flair update(Flair flair, Long id);
    List<Flair> getAll();
    Flair getOne (Long id);
}
