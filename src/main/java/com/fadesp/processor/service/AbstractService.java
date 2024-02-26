package com.fadesp.processor.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Log4j2
public abstract class AbstractService {

  @Autowired
  public ApplicationEventPublisher publisher;

  @PersistenceContext
  public EntityManager em;

  public Pageable generatePageable(Integer page, Integer size) {
    if (page == null || size == null)
      return null;
    return generatePageable(page, size, Sort.unsorted());
  }

  public Pageable generatePageable(Integer page, Integer size, Sort sort) {
    if (page == null || size == null)
      return null;
    if (page - 1 > -1) page = page - 1;
    return PageRequest.of(page, size, sort);
  }
}
