package com.jason.elearning.service.book;

import com.jason.elearning.configuration.Translator;
import com.jason.elearning.entity.Book;
import com.jason.elearning.entity.Read;
import com.jason.elearning.entity.User;
import com.jason.elearning.entity.constants.BookStatus;
import com.jason.elearning.entity.constants.RoleName;
import com.jason.elearning.repository.book.BookRepository;
import com.jason.elearning.repository.book.ReadRepository;
import com.jason.elearning.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl extends BaseService implements BookService{
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private ReadRepository readRepository;
    @Override
    public List<Book> getLearnerBooks() throws Exception {
        User user = getUser();
        if (user == null ) {
            throw new Exception(Translator.toLocale("access_denied"));
        }
        List<Long> bookIds = readRepository.findAllByUserId(user.getId())
                                .stream().map(Read::getBookId).collect(Collectors.toList());

        List<Book> bookList = bookRepository.findAllByIdIn(bookIds);


        return bookList;
    }

    @Override
    public void addBook(Book book) throws Exception {
        User user = getUser();
        if (user == null || user.getRoles().get(0).getName() == RoleName.ROLE_LEARNER) {
            throw new Exception(Translator.toLocale("access_denied"));
        }
        book.setAuthorId(user.getId());
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

    @Override
    public List<Book> getBooks(String title,BookStatus status, Long categoryId,Long authorId,String authorName,Long startPrice,Long endPrice,int page) throws Exception {
        User user = getUser();

        return  bookRepository.getBook(page,title,categoryId,authorId,authorName,status,startPrice,endPrice, user == null ? -1 : user.getId());
    }

    @Override
    public String updateChapter(int chapter,long bookId ) throws Exception {
        User user = getUser();
        if (user == null) {
            throw new Exception(Translator.toLocale("access_denied"));
        }
        Read read = readRepository.findFirstByUserIdAndBookId(user.getId(), bookId);
        if(read == null){
            throw new Exception(Translator.toLocale("some thing went wrong"));
        }
        read.setCurrentChapter(chapter);
        readRepository.save(read);
        return "success";
    }

    @Override
    public int currentChapter(long bookId) throws Exception {
        User user = getUser();
        if (user == null) {
            throw new Exception(Translator.toLocale("access_denied"));
        }
        Read read = readRepository.findFirstByUserIdAndBookId(user.getId(), bookId);
        if(read == null){
            throw new Exception(Translator.toLocale("some thing went wrong"));
        }


        return read.getCurrentChapter();
    }

    @Override
    public Boolean isBookPaid(long bookId) throws Exception {
        User user = getUser();
        if (user == null) {
            return false;
        }
        Read read = readRepository.findFirstByUserIdAndBookId(user.getId(), bookId);
        if(read == null){
           return false;
        }
        return true;
    }
}
