package com.socialnetwork.app.repository;

import com.socialnetwork.app.domain.Entity;
import com.socialnetwork.app.exceptions.RepoException;

public interface UpdatableRepository<E extends Entity<Integer>> {
    void update(E obj) throws RepoException;

}
