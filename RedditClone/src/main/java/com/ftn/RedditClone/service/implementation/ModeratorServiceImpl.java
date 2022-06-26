package com.ftn.RedditClone.service.implementation;

import com.ftn.RedditClone.model.entity.Moderator;
import com.ftn.RedditClone.model.entity.User;
import com.ftn.RedditClone.repository.ModeratorRepository;
import com.ftn.RedditClone.service.ModeratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModeratorServiceImpl implements ModeratorService {
    @Autowired
    ModeratorRepository moderatorRepository;

    @Override
    public Moderator findModeratorByUser(User user) {
        return moderatorRepository.findModeratorByUser(user);
    }
}
