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

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Francois+One&family=Margarine&family=Nanum+Gothic&display=swap"
          rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Do+Hyeon&family=Jua&display=swap" rel="stylesheet">

    <link rel="stylesheet" href="/assets/css/common.css">
<%--    <link rel="stylesheet" href="/assets/css/customer/customer-mypage.css">--%>
    <link rel="stylesheet" href="/assets/css/customer/customer-mypage-edit.css">
<%--    <link rel="stylesheet" href="/assets/css/store/calendar.css">--%>
    <link rel="stylesheet" href="/assets/css/reservation/reservation-detail-modal.css">
    <script defer src="/assets/js/store/store-mypage.js"></script>
</head>
<body>
<style>
    @font-face {
        font-family: 'NIXGONM-Vb';
        src: url('https://fastly.jsdelivr.net/gh/projectnoonnu/noonfonts_six@1.2/NIXGONM-Vb.woff') format('woff');
        font-weight: normal;
        font-style: normal;
    }

    /*@font-face {*/
    /*    font-family: 'Ownglyph_noocar-Rg';*/
    /*    src: url('https://fastly.jsdelivr.net/gh/projectnoonnu/2405-2@1.0/Ownglyph_noocar-Rg.woff2') format('woff2');*/
    /*    font-weight: normal;*/
    /*    font-style: normal;*/
    /*}*/
    body {
        font-family: 'NIXGONM-Vb', 'Nanum Gothic', sans-serif;
        /*font-size: 25px;*/
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
                <img src="${storeInfo.storeImg != null? storeInfo.storeImg : '/assets/img/western.jpg'}"
                     alt="store image">
            </a>
            <h2>${storeInfo.storeName}</h2>
            <p>${storeInfo.storeId}</p>
            <ul class="nav">
                <li class="nav-item"><a class="nav-link mypage" href="/store/mypage/main">마이페이지</a></li>
                <li class="nav-item"><a class="nav-link edit" href="/store/mypage/edit/main">개인정보수정</a></li>
            </ul>
            <div class="stats">
                <div id="carbon" class="stats-box">
                    <img src="/assets/img/mypage-carbon.png" alt="leaf">
                    <div>${stats.coTwo}kg의 이산화탄소 배출을 줄였습니다</div>
                </div>
                <div id="community" class="stats-box">
                    <img src="/assets/img/mypage-community.png" alt="community">
                    <div>지금까지 ${stats.customerCnt}명의 손님을 만났어요</div>
                </div>
            </div>
        </div>
        <div class="info">
            <div class="info-box">
                <div class="title">
                    <h3 class="title-text">
                        <span>
                        예약 내역
                        </span>
                    </h3>
                    <div class="info-wrapper reservation">
                        <ul class="reservation-list">
                            <c:forEach var="reservation" items="${reservations}" varStatus="status">
                                <li id="reservation-${status.index}" class="reservation-item"
                                    data-reservation-id="${reservation.reservationId}"
                                    data-reservation-status="${reservation.status}">
                                    <div class="item">
                                        <div class="img-wrapper">
                                            <div class="img-box">
                                                <img src="${reservation.profileImage != null ? reservation.profileImage : "/assets/img/western.jpg"}"
                                                     alt="profile Image"/>
                                            </div>
                                            <c:if test="${reservation.status == 'CANCELED'}">
                                                <i class="fa-solid fa-circle-xmark canceled"></i>
                                            </c:if>
                                            <c:if test="${reservation.status == 'NOSHOW'}">
                                                <i class="fa-solid fa-circle-xmark canceled"></i>
                                            </c:if>
                                            <c:if test="${reservation.status == 'RESERVED'}">
                                                <i class="fa-solid fa-spinner loading"></i>
                                            </c:if>
                                            <c:if test="${reservation.status == 'PICKEDUP'}">
                                                <i class="fa-solid fa-circle-check done"></i>
                                            </c:if>
                                        </div>
                                        <span class="reservation-nickname"
                                              style="font-size: 18px">${reservation.nickname}<span>님께서</span></span>
                                    </div>
                                    <div class="item reservation-status">
                                        <c:if test="${reservation.status == 'CANCELED'}">
                                            <span>예약을 취소했어요</span>
                                            <span>${reservation.cancelReservationAtF}</span>
                                        </c:if>
                                        <c:if test="${reservation.status == 'NOSHOW'}">
                                            <span>미방문하여 예약이 취소됐어요</span>
                                            <span>${reservation.pickupTimeF}</span>
                                        </c:if>
                                        <c:if test="${reservation.status == 'RESERVED'}">
                                            <span>픽업하러 오는 중이에요!</span>
                                            <span>${reservation.pickupTimeF}</span>
                                        </c:if>
                                        <c:if test="${reservation.status == 'PICKEDUP'}">
                                            <span>픽업을 완료했어요</span>
                                            <span>${reservation.pickedUpAtF}</span>
                                        </c:if>
                                    </div>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>
                </div>
            </div>
            <div id="product-count">
                <div class="title">
                    <h3 class="title-text">
                        <span id="randombox-stock">
                            오늘의 랜덤박스 현황
                        </span>
                        <button id="product-update-btn">
                            랜덤박스 추가
                        </button>
                    </h3>
                    <div>

                        <section id="product-count-status-with-img">
                            <div class="status-img">
                                <img src="/assets/img/mypage-foods.png" alt="픽업이미지">
                                <div id="count"></div>
                            </div>
                            <div class="status-img">
                                <img src="/assets/img/mypage-pickedUp.png" alt="픽업이미지">
                                <div id="today-picked-up"></div>
                            </div>
                            <div class="status-img">
                                <img src="/assets/img/mypage-omw.png" alt="픽업이미지">
                                <div id="today-ready-picked-up"></div>
                            </div>
                            <div class="status-img">
                                <img src="/assets/img/free-icon-in-stock.png" alt="픽업이미지">
                                <div id="remain"></div>
                            </div>
                        </section>
                    </div>
                </div>
            </div>

            <div id="calendar-header">
                <div class="title">
                    <h3 class="title-text">
                        <span>
                        가게 스케줄 조정
                        </span>
                    </h3>
                    <div id="calendar-section">
                        <div class="calendar-month">
                            <button class="calendar-button" id="prev-month">
                                이전 달
                            </button>
                            <span id="current-month"></span>
                            <button class="calendar-button" id="next-month">
                                다음 달
                            </button>
                            <div class="day-description">
                                <span id="holiday-description">가게 쉬는 날</span>
                                <span id="today-description">오늘</span>
                            </div>
                        </div>
                        <div id="calendar"></div>
                    </div>
                </div>
            </div>

        </div>
    </div>
</section>

<!-- 픽업예정, 예약내역 모달 창 -->
<div id="reservation-modal" class="modal">
    <div class="modal-content">
        <div>
            <span class="close"><i class="fas fa-times"></i></span>
            <div id="modal-store-reservation-details"></div>
        </div>
    </div>
</div>


<!-- 캘린더 모달 창 -->
<div id="store-calendar-modal" class="modal">
    <div class="modal-content">
        <span class="close"><i class="fas fa-times"></i></span>
        <div id="schedule">
<%--            <h2>가게 문 몇시에 열고 닫는지, 오늘 문 닫을건지</h2>--%>
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
    document.addEventListener('DOMContentLoaded', function () {
        const reservationItems = document.querySelectorAll('.reservation-item');

        reservationItems.forEach(function (item) {
            let status = item.getAttribute('data-reservation-status');
            if (status === 'CANCELED') {
                item.classList.add('canceled');
            } else if (status === 'RESERVED') {
                item.classList.add('reserved');
            } else if (status === 'PICKEDUP') {
                item.classList.add('pickedup');
            } else {
                item.classList.add('noshow');
            }
        });
    });
</script>
</body>
</html>
