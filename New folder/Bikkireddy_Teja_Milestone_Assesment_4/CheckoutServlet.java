package com.wipro;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        List<Book> cart = (List<Book>) session.getAttribute("cart");

        if (cart == null || cart.isEmpty()) {
            response.sendRedirect("books");
            return;
        }

        request.getRequestDispatcher("/checkout.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        List<Book> cart = (List<Book>) session.getAttribute("cart");

        if (cart == null || cart.isEmpty()) {
            response.sendRedirect("books");
            return;
        }

        String shippingAddress = request.getParameter("shippingAddress");
        BigDecimal total = cart.stream().map(Book::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bookstore", "username", "password");
            
            String orderQuery = "INSERT INTO Orders (user_id, total, shipping_address) VALUES (?, ?, ?)";
            PreparedStatement orderSt = connection.prepareStatement(orderQuery, Statement.RETURN_GENERATED_KEYS);
            orderSt.setInt(1, (int) session.getAttribute("userId"));
            orderSt.setBigDecimal(2, total);
            orderSt.setString(3, shippingAddress);
            orderSt.executeUpdate();

            int orderId = 0;
            ResultSet generatedKeys = orderSt.getGeneratedKeys();
            if (generatedKeys.next()) {
                orderId = generatedKeys.getInt(1);
            }

            String itemQuery = "INSERT INTO order_items (order_id, book_id, quantity) VALUES (?, ?, ?)";
            PreparedStatement itemSt = connection.prepareStatement(itemQuery);
            for (Book book : cart) {
                itemSt.setInt(1, orderId);
                itemSt.setInt(2, book.getId());
                itemSt.setInt(3, 1);
                itemSt.executeUpdate();
            }

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        session.removeAttribute("cart");
        session.setAttribute("orderTotal", total);
        session.setAttribute("shippingAddress", shippingAddress);

        response.sendRedirect("confirmation.jsp");
    }
}
