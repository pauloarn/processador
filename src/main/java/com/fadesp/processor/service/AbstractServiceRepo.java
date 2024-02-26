package com.fadesp.processor.service;

import com.fadesp.processor.repository.GenericRepository;

import java.io.Serializable;

public abstract class AbstractServiceRepo<R extends GenericRepository<T, I>, T, I extends Serializable> extends AbstractService {
  protected R repository;

  public AbstractServiceRepo(R repository) {
    this.repository = repository;
  }
}
