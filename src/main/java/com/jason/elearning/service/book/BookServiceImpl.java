package com.jason.elearning.service.book;

import com.jason.elearning.entity.Book;
import com.jason.elearning.entity.constants.BookStatus;
import com.jason.elearning.repository.book.BookRepository;
import com.jason.elearning.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl extends BaseService implements BookService{
    @Autowired
    private BookRepository bookRepository;
    @Override
    public void addBook(Book book) {
        bookRepository.save(book);
    }

    @Override
    public void updateBook(Book request) throws Exception {
        Book book = bookRepository.findById(request.getId()).orElseThrow(()-> new Exception("can not find book"));
        if(request.getStatus() != null){
            book.setStatus(request.getStatus());
        }
        if(request.getDescription() != null){
            book.setDescription(request.getDescription());
        }
        if(request.getTitle() != null){
            book.setTitle(request.getTitle());
        }
        if(request.getPrice() != 0){
            book.setPrice(request.getPrice());
        }
        if(request.getPriceSale() !=0){
            book.setPriceSale(request.getPriceSale());
        }
        if(request.getMediaId() != null){
            book.setMediaId(request.getMediaId());
        }
        bookRepository.save(book);
    }

    @Override
    public List<Book> listBook(int page, Long categoryId, String title, Long authorId, String authorName, BookStatus status, Long startPrice, Long endPrice) throws Exception {
        return bookRepository.getBook(page,title,categoryId,authorId,authorName,status,startPrice,endPrice,authorId);
    }

    @Override
    public Book getBookById(long bookId) throws Exception {
        return bookRepository.findById(bookId).orElseThrow(()-> new Exception("can not find book"));
    }

    @Override
    public List<Book> listCourseForUserReading() throws Exception {
        return null;
    }
}
