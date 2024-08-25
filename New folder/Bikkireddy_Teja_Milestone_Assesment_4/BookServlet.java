package com.wipro;


import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/books")
public class BookServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	List<Book> bookList = new ArrayList<>();
    	try {
        	Class.forName("com.mysql.cj.jdbc.Driver");
        	Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bookstore", "root", "root");
        	String query = "SELECT * FROM books";
        	PreparedStatement statement = conn.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            System.out.println("Database connection established");
            // for getting the details of books
            while (resultSet.next()) {
            	Book book = new Book();
            	book.setId(resultSet.getInt("id"));
            	book.setTitle(resultSet.getString("title"));
                book.setAuthor(resultSet.getString("author"));
                book.setPrice(resultSet.getBigDecimal("price"));
                bookList.add(book);
            }
            conn.close();
        } catch (Exception e) {
        	
            e.printStackTrace();
        }
        // Forward the list of books to the JSP page
        request.setAttribute("books", bookList);
        RequestDispatcher dispatcher = request.getRequestDispatcher("books.jsp");
        dispatcher.forward(request, response);
    }
}
