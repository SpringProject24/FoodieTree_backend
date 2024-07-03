<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>FoodieTree</title>
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css"
          integrity="sha512-SnH5WK+bZxgPHs44uWIX+LLJAJ9/2PkPKZ5QiAj6Ta86w+fsb2TkcmfRyVX3pBnMFcV7oQPJkl9QevSCWr3W6A=="
          crossorigin="anonymous" referrerpolicy="no-referrer"/>
    <link rel="stylesheet" href="/assets/css/common.css">
    <link rel="stylesheet" href="/assets/css/customer/customer-mypage.css">
    <link rel="stylesheet" href="/assets/css/store/calendar.css">
    <link rel="stylesheet" href="/assets/css/reservation/reservation-detail-modal.css">
    <script defer src="/assets/js/store/store-mypage.js"></script>
</head>
<body>
<style>
    #calendar {
        display: grid;
        grid-template-columns: repeat(7, 1fr);
        gap: 10px;
        max-width: 600px;
        margin: auto;
    }

    #calendar div {
        padding: 20px;
        background-color: #f0f0f0;
        text-align: center;
        cursor: pointer;
    }

    #events {
        max-width: 600px;
        margin: 20px auto;
        padding: 20px;
        background-color: #fafafa;
        border: 1px solid #ddd;
    }

    .stats{
        display: flex;
        flex-direction: column;
        align-items: center;
        margin-top: 20px;
    }
</style>
<header>
    <div class="container">
        <div class="logo"><h1>FoodieTree</h1></div>
        <div class="logo-img">
            <img src="/assets/img/img_2.png" alt="">
        </div>
    </div>
</header>
<section class="my-page-area">
    <div class="container">
        <div class="profile">
            <a href="#" id="avatar">
                <img src="${storeInfo.storeImg ? storeInfo.storeImg : '/assets/img/western.jpg'}"
                     alt="Customer profile image">
            </a>
            <h2>${storeInfo.storeName}</h2>
            <p>${storeInfo.storeId}</p>
            <ul class="nav">
                <li class="nav-item"><a class="nav-link" href="/store/mypage/main">마이페이지</a></li>
                <li class="nav-item"><a class="nav-link" href="/store/mypage/edit/main">개인정보수정</a></li>
                <div class="stats">
                    <div>${stats.coTwo}kg의 이산화탄소 배출을 줄였습니다</div>
                    <div>지금까지 ${stats.customerCnt}명의 손님을 만났어요</div>
                </div>
            </ul>
        </div>
        <div class="info">
            <div class="info-box">
                <div class="title">
                    <h3 class="title-text">예약 내역</h3>
                    <div class="info-wrapper reservation">
                        <ul class="reservation-list">
                            <c:forEach var="reservation" items="${reservations}" varStatus="status">
                                <li id="reservation-${status.index}" class="reservation-item" data-reservation-id="${reservation.reservationId}">
                                    <div class="item">
                                        <div class="img-wrapper">
                                            <div class="img-box">
                                                <img src="${reservation.profileImage != null ? reservation.profileImage : "/assets/img/western.jpg"}"
                                                     alt="profile Image"/>
                                            </div>
                                            <c:if test="${reservation.status == 'CANCELED'}">
                                                <i class="fa-solid fa-circle-xmark canceled"></i>
                                            </c:if>
                                            <c:if test="${reservation.status == 'RESERVED'}">
                                                <i class="fa-solid fa-spinner loading"></i>
                                            </c:if>
                                            <c:if test="${reservation.status == 'PICKEDUP'}">
                                                <i class="fa-solid fa-circle-check done"></i>
                                            </c:if>
                                        </div>
                                        <span>${reservation.nickname}님이 ${reservation.status}</span>
                                    </div>
                                    <div class="item">
                                        <span>${reservation.status}</span>
                                    </div>
                                    <div class="item">
                                        <span>${reservation.pickupTime}</span>
                                    </div>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>
                </div>
            </div>
            <div id="product-count">
                <div class="title">
                    <h3 class="title-text">오늘의 랜덤박스 현황</h3>
                    <div id="count"></div>
                    <div id="today-picked-up"></div>
                    <div id="today-ready-picked-up"></div>
                    <div id="remain"></div>
                    <br>
                    <button id="product-update-btn">수량업데이트 하기</button>
                </div>
            </div>

            <div id="calendar-header">
                <div class="title">
                    <h3 class="title-text">가게 스케줄 조정</h3>
                    <button id="prev-month">이전 달</button>
                    <span id="current-month"></span>
                    <button id="next-month">다음 달</button>
                    <div id="calendar"></div>
                </div>
            </div>

        </div>
    </div>
</section>

<!-- 픽업예정, 예약내역 모달 창 -->
<div id="reservation-modal" class="modal">
    <div class="modal-content">
        <div>
            <span class="close">&times;</span>
            <h2>예약 상세 내역</h2>
            <div id="modal-store-reservation-details"></div>
        </div>
    </div>
</div>


<!-- 캘린더 모달 창 -->
<div id="store-calendar-modal" class="modal">
    <div class="modal-content">
        <span class="close">&times;</span>
        <div id="schedule">
            <h2>가게 문 몇시에 열고 닫는지, 오늘 문 닫을건지</h2>
            <div id="modal-schedule-details"></div>
        </div>
    </div>
</div>

<!-- 수량 추가 모달 창 -->
<div id="product-add-modal" class="modal">
    <div class="modal-content">
        <span class="close">&times;</span>
        <div id="product-count-status">
            <h2>수량 변경 오늘 등록된 수량, 남은 수량 보여주고 몇개로 업데이트할건지 물어보고 받음</h2>
            <div id="modal-product-count-details"></div>
        </div>
    </div>
</div>

<script>

</script>
</body>
</html>
