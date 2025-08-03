package com.ssm_project.controller;

import com.ssm_project.pojo.Books;
import com.ssm_project.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/book")
public class BookController {

    @Autowired
    @Qualifier("BookServiceImpl")
    private BookService bookService;

    @RequestMapping("/allBook")
    public ModelAndView list(Model model) {
        List<Books> list = bookService.queryAllBook();
       ModelAndView modelAndView = new ModelAndView();
       modelAndView.addObject("list",list);
       modelAndView.setViewName("allbooks");
       return modelAndView;
    }

    @RequestMapping("/toAddBook")
    public String toAddPaper() {
        return "addBook";
    }

    @RequestMapping("/addBook")
    public String addPaper(Books books) {
        System.out.println(books);
        bookService.addBook(books);
        return "redirect:/book/allBook";
    }

    @RequestMapping("/toUpdateBook")
    public String toUpdateBook(Model model, String id) {
        Books books = bookService.queryBookById(id);
        System.out.println(books);
        model.addAttribute("book",books );
        return "updateBook";
    }

    @RequestMapping("/updateBook")
    public String updateBook(Model model, Books book) {
        System.out.println(book);
        bookService.updateBook(book);
        Books books = bookService.queryBookById(book.getBookID());
        model.addAttribute("books", books);
        return "redirect:/book/allBook";
    }

    @RequestMapping("/del/{bookId}")
    public String deleteBook(@PathVariable("bookId") String id) {
        bookService.deleteBookById(id);
        return "redirect:/book/allBook";
    }
    @RequestMapping("/queryBookById")
    public ModelAndView queryBookById(@RequestParam("ID")String id){
        Books books=  bookService.queryBookById(id);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("book",books);
        modelAndView.setViewName("queryBookByID");
        return modelAndView;
    }
}
