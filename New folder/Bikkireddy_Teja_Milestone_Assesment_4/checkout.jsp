<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.wipro.Book" %>
<html>
<head>
    <title>Checkout</title>
</head>
<body>
    <h2>Your Shopping Cart</h2>
    <table border="1">
        <tr>
            <th>Title</th>
            <th>Author</th>
            <th>Price</th>
        </tr>
        <%
            List<Book> cart = (List<Book>) session.getAttribute("cart");
            if (cart != null) {
                double tot = 0;
                for (Book book : cart) {
                    tot += book.getPrice().doubleValue();
        %>
        <tr>
            <td><%= book.getTitle() %></td>
            <td><%= book.getAuthor() %></td>
            <td><%= book.getPrice() %></td>
        </tr>
        <%
                }
        %>
    </table>
    <h3>Total: <%= tot %></h3>
        <%
            }
        %>
    
    <h2>Shipping Details</h2>
    <form action="checkout" method="post">
        <label for="shippingAddress">Address:</label>
        <input type="text" id="shippingAddress" name="shippingAddress" required><br><br>
        <input type="submit" value="Place Order">
    </form>
</body>
</html>
