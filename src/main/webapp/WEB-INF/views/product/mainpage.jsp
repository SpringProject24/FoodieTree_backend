<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Foodie Tree</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/mainpage.css">
</head>
<body>
    <header>
        <nav>
            <button class="menu-button">☰</button>
            <span class="title">FOODIE TREE</span>
            <input type="text" class="search-bar" placeholder="주소 or 음식 검색">
            <button class="profile-button">프사</button>
            <button class="sign-out-button">sign out</button>
        </nav>
    </header>
    <main>
        <section class="category">
            <h2>음식</h2>
            <div class="card-container">
                <c:forEach var="item" items="${categoryByFood}">
                    <div class="card">
                        <div class="photo"></div>
                        <div class="info">
                            <p>가게이름 : ${item.storeName}</p>
                            <p>픽업시간 : ${item.pickupTime}</p>
                            <p>평점 / 거리</p>
                            <p>가격 : ${item.price}</p>
                            <p>수량 : </p>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </section>
        <section class="category">
            <h2>지역</h2>
            <div class="card-container">
                <c:forEach var="item" items="${categoryByArea}">
                    <div class="card">
                        <div class="photo"></div>
                        <div class="info">
                            <p>가게이름 : ${item.storeName}</p>
                            <p>픽업시간 : ${item.pickupTime}</p>
                            <p>평점 / 거리</p>
                            <p>가격 : ${item.price}</p>
                            <p>수량 : </p>
                        </div>
                    </div>
                </c:forEach>
            </div>
            <div class="pagination">
                <button class="prev-button">◀</button>
                <button class="next-button">▶</button>
            </div>
        </section>
    </main>
    <!-- <footer>
        <div class="footer-content">
            <p>FOODIE TREE</p>
            <div class="store-links">
                <img src="${pageContext.request.contextPath}/appstore.png" alt="App Store">
                <img src="${pageContext.request.contextPath}/googleplay.png" alt="Google Play">
            </div>
            <div class="footer-links">
                <a href="#">Get Help</a>
                <a href="#">Buy gift cards</a>
                <a href="#">Add your restaurant</a>
                <a href="#">Sign up to deliver</a>
                <a href="#">Create a business account</a>
                <a href="#">Promotions</a>
                <a href="#">Restaurants near me</a>
                <a href="#">View all cities</a>
                <a href="#">View all countries</a>
                <a href="#">Pickup near me</a>
                <a href="#">About Uber Eats</a>
                <a href="#">Shop groceries</a>
            </div>
            <div class="footer-legal">
                <a href="#">Privacy Policy</a>
                <a href="#">Terms</a>
                <a href="#">Pricing</a>
                <a href="#">Do not sell or share my personal information</a>
            </div>
            <p>© 2024 Uber Technologies Inc.</p>
        </div>
    </footer> -->

      <!-- 공통푸터 -->
  <%@ include file="include/footer.jsp" %> 
</body>
</html>
