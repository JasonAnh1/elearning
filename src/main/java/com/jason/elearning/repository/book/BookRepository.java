package com.jason.elearning.repository.book;

import com.jason.elearning.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BookRepository extends JpaRepository<Book,Long> , BookRepositoryCustom{
    List<Book> findAllByIdIn(List<Long> bookIds);

}
