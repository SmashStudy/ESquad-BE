package com.esquad.esquadbe.studypage.repository;

import com.esquad.esquadbe.studypage.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
