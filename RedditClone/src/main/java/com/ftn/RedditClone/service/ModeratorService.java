package com.ftn.RedditClone.service;

import com.ftn.RedditClone.model.entity.Moderator;
import com.ftn.RedditClone.model.entity.User;

public interface ModeratorService {
    Moderator findModeratorByUser(User user);
    void save (Moderator moderator);
}
