<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>FoodieTree for 소비자</title>
    <style>
        #food-img {
            width: 150px;
            height: 150px;
            border-radius: 20%;
        }

        body {
            font-family: Arial, sans-serif;
        }
        .container {
            display: flex;
        }
        .profile, .info {
            border: 1px solid #ccc;
            padding: 10px;
            margin: 10px;
        }
        .profile {
            width: 30%;
        }
        .info {
            width: 65%;
        }
        .profile img {
            width: 100px;
            height: 100px;
            border-radius: 50%;
        }
        .reservation-list, .issue-list {
            border-top: 1px solid #ccc;
            padding-top: 10px;
        }
        .reservation-item, .issue-item {
            display: flex;
            justify-content: space-between;
            border: 1px solid #ccc;
            margin: 5px 0;
            padding: 10px;
        }
        .stats {
            display: flex;
            justify-content: space-around;
            margin-top: 20px;
        }
        .stats div {
            border: 1px solid #ccc;
            padding: 20px;
            width: 45%;
        }
    </style>
</head>
<body>
<form action="/customer/mypage" id="customer-mypage-main" method="post">
    <div class="container">
        <div class="profile">
            <img src="${customerMyPageDto.profileImage}" alt="Customer profile image">
            <h3>${customerMyPageDto.nickname}</h3>
            <p>${customerMyPageDto.customerId}</p>
            <h4>마이페이지</h4>
            <a href="/customer/customer-mypage-edit">
                <p>개인정보수정</p>
            </a>
        </div>
        <div class="info">
            <h3>예약 내역</h3>
            <div class="reservation-list">
                <c:forEach var="reservation" items="${reservations}">
                    <div class="reservation-item">
                        <img src="${reservation.storeImg}" alt="Store Image"/>
                        <span>${reservation.storeName}</span>
                        <span>${reservation.status}</span>
                        <span>${reservation.pickUpTime}</span>
                    </div>
                </c:forEach>
            </div>
            <h4>선호 지역</h4>
            <ul>
                <c:forEach var="area" items="${customerMyPageDto.preferredArea}">
                    <li>${area}</li>
                </c:forEach>
            </ul>
            <h4>선호 음식</h4>
            <ul>
                <c:forEach var="food" items="${customerMyPageDto.preferredFood}">
                    <img id="food-img" src="${food.foodImage}" alt="선호음식이미지">
                    <li>${food.preferredFood}</li>
                </c:forEach>
            </ul>
            <h4>최애 가게</h4>
            <ul>
                <c:forEach var="area" items="${customerMyPageDto.favStore}">
                    <li>
                        <img src="${area.storeImg}" alt="최애가게이미지">
                        <span>${area.storeName}</span>
                    </li>
                </c:forEach>
            </ul>
            <h3>이슈 내역</h3>
            <div class="issue-list">
                <c:forEach var="issue" items="${issues}">
                    <div class="issue-item">
                        <span>${issue.issueCategory.issueName}</span>
                        <span>${issue.issueText}</span>
                        <span>${issue.issueStatus}</span>
                        <span>${issue.cancelIssueAt}</span>
                        <span>${issue.storeName}</span>
                        <span>${issue.nickname}</span>
                    </div>
                </c:forEach>
            </div>
            <div class="stats">
                <div>10kg의 음쓰를 줄였습니다</div>
                <div>지금까지 10만원을 아꼈어요</div>
            </div>
        </div>
    </div>
</form>
</body>
</html>
