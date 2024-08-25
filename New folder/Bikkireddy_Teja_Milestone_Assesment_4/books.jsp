<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.wipro.Book" %>
<html>
<head>
    <title>Available Books</title>
</head>
<body>
    <h1>Available Books</h1>
    <table border="1.5">
        <tr>
            <th>Title</th>
            <th>Author</th>
            <th>Price</th>
            <th>Action</th>
        </tr>
        <%
            List<Book> books = (List<Book>) request.getAttribute("books");
            for (Book book : books) {
        %>
            <tr>
                <td><%= book.getTitle() %></td>
                <td><%= book.getAuthor() %></td>
                <td><%= book.getPrice() %></td>
                <td><a href="addToCart?bookId=<%= book.getId() %>">Add to Cart</a></td>
            </tr>
        <%
            }
        %>
    </table>
</body>
</html>
