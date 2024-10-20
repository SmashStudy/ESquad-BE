package com.esquad.esquadbe.studypage.repository;

import com.esquad.esquadbe.studypage.entity.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {
}