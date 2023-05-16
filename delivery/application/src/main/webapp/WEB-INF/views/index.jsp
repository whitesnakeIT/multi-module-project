<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Title</title>
</head>
<body>
<h1>!404 :)</h1>
<c:set var="context" value="/api/v1"/>
<ul>
    <li>
        <a href="<c:url value='${context}/couriers' /> ">
            Couriers
        </a>
    </li>
    <li>
        <a href="<c:url value='${context}/customers' /> ">
            Customers
        </a>
    </li>
    <li>
        <a href="<c:url value='${context}/tracking' /> ">
            Tracking
        </a>
    </li>
    <li>
        <a href="<c:url value='${context}/orders' /> ">
            Orders
        </a>
    </li>
    <li>
        <a href="<c:url value='${context}/deliveries' /> ">
            Deliveries
        </a>
    </li>
</ul>

</body>
</html>
