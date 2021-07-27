package com.news.repository;

import com.news.model.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
// This will be AUTO IMPLEMENTED by Spring into a Bean called NewsRepository
// CRUD refers Create, Read, Update, Delete
public interface NewsRepository extends CrudRepository<News, Long> {
    // будет возвращаться page
    Page<News> findAll(Pageable pageable);

    Page<News> findByTitle(String title, Pageable pageable);

}
