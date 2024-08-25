package com.wipro;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/addToCart")
public class CartServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        List<Book> cart = (List<Book>) session.getAttribute("cart");

        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }

        String bookId = request.getParameter("bookId");
        if (bookId != null && !bookId.isEmpty()) {
            Book book = getBookById(Integer.parseInt(bookId));
            if (book != null) {
                cart.add(book);
            }
        }

        response.sendRedirect("checkout");
    }

    private Book getBookById(int bookId) {
        Book book = null;
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bookstore", "root", "root");
            String query = "SELECT * FROM books WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, bookId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                book = new Book();
                book.setId(resultSet.getInt("id"));
                book.setTitle(resultSet.getString("title"));
                book.setAuthor(resultSet.getString("author"));
                book.setPrice(resultSet.getBigDecimal("price"));
            }

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return book;
    }
}
