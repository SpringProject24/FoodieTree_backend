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
<%--    <link rel="stylesheet" href="/assets/css/customer/customer-mypage-edit.css">--%>

    <link rel="stylesheet" href="/assets/css/reservation/reservation-detail-modal.css">
    <script defer src="/assets/js/reservation.js"></script>
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
        .my-page-area .container .profile ul li.nav-item a.nav-link.mypage-link{
            position: relative;
            top: 50px;
        }
        .my-page-area .container .profile ul li.nav-item a.nav-link.edit-link{
            position: relative;
            bottom: -90px;
        }

        .title{
            padding: 15px 15px 0;
            margin: 20px 20px 0;
        }

        .my-page-area .container .info .info-wrapper{
            width: 979px;
            margin-left: 36px;
            border-radius: 0 0 15px 15px;
        }

        .reservation-item{
            border-radius: 15px;
            flex-wrap: wrap;
            height: 72px;
            /*width: 90%;*/
            margin: 0 7px 10px 7px;
        }

        .stats{
            margin-top: 40px;
        }

    </style>
</head>
<body>

<header>
    <div class="container">
        <div class="logo margarine-regular"><h1>FoodieTree</h1></div>
        <div class="logo-img">
            <img src="/assets/img/img_2.png" alt="">
        </div>
    </div>
</header>
<section class="my-page-area">
    <div class="container">
        <div class="profile">
            <a href="#" id="avatar">
                <img src="${customerMyPageDto.profileImage !=null ? customerMyPageDto.profileImage : '/assets/img/western.jpg'}"
                     alt="Customer profile image">
            </a>
            <h2>${customerMyPageDto.nickname}</h2>
            <p>${customerMyPageDto.customerId}</p>
            <ul class="nav">
                <li class="nav-item"><a class="nav-link mypage-link" href="mypage">마이페이지</a></li>
                <li class="nav-item"><a class="nav-link edit-link" href="mypage-edit">개인정보수정</a></li>
            </ul>
            <div class="stats">
                <div id="carbon" class="stats-box">
                    <img src="/assets/img/mypage-carbon.png" alt="leaf">
                    <div>${stats.coTwo}kg의 이산화탄소 배출을 줄였습니다</div>
                </div>
                <div id="community" class="stats-box">
                    <img src="/assets/img/mypage-pigbank.png" alt="community">
                    <div>지금까지 ${stats.money}원을 아꼈어요</div>
                </div>
            </div>
        </div>
        <div class="info">
            <div class="info-box">
                <div class="title">
                    <h3 class="title-text">예약 내역</h3>
                </div>

                <div class="info-wrapper reservation">
                    <ul class="reservation-list">
                        <c:forEach var="reservation" items="${reservations}" varStatus="status">
                            <li id="reservation-${status.index}" class="reservation-item">
                                <div class="item">
                                    <div class="img-wrapper">
                                        <div class="img-box">
                                            <img src="${reservation.storeImg != null ? reservation.storeImg : "/assets/img/western.jpg"}"
                                                 alt="Store Image"/>
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
                                    <span>${reservation.storeName}</span>
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
            <div class="info-box">
                <div class="title">
                    <h3 class="title-text">선호 지역</h3>
                </div>
                <div class="info-wrapper">
                    <ul class="info-list area">
                        <c:forEach var="area" items="${customerMyPageDto.preferredArea}">
                            <li>${area}</li>
                        </c:forEach>
                    </ul>
                </div>
            </div>
            <div class="info-box">
                <div class="title">
                    <h3 class="title-text">선호 음식</h3>
                </div>
                <div class="info-wrapper">
                    <ul class="info-list food">
                        <c:forEach var="food" items="${customerMyPageDto.preferredFood}">
                            <li>
                                <div class="img-box">
                                    <img src="${food.foodImage}" alt="선호음식이미지"/>
                                </div>
                                <span>${food.preferredFood}</span>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
            </div>
            <div class="info-box">
                <div class="title">
                    <h3 class="title-text">최애 가게</h3>
                </div>
                <div class="info-wrapper">
                    <ul class="info-list store">
                        <c:forEach var="store" items="${customerMyPageDto.favStore}">
                            <li id="${store.storeId}">
                                <div class="img-box">
                                    <img src="${store.storeImg ? store.storeImg : "/assets/img/japanese.jpg"}" alt="최애가게이미지">
                                </div>
                                <span>${store.storeName}</span>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
            </div>
            <div class="info-box">
                <div class="title">
                    <h3 class="title-text">이슈 내역</h3>
                </div>
                <div class="info-wrapper">
                    <ul class="issue-list">
                        <c:forEach var="issue" items="${issues}">
                            <li class="issue-item">
                                <span>${issue.issueCategory.issueName}</span>
                                <span>${issue.issueText}</span>
                                <span>${issue.issueStatus}</span>
                                <span>${issue.cancelIssueAt}</span>
                                <span>${issue.storeName}</span>
                                <span>${issue.nickname}</span>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</section>

<!-- 모달 창 -->
<div id="reservation-modal" class="modal">
    <div class="modal-content">
        <span class="close">&times;</span>
        <h2>예약 상세 내역</h2>
        <div id="modal-details"></div>
    </div>
</div>

<!-- 모달 창 -->
<div id="cancel-modal" class="modal">
    <div class="modal-content">
        <span class="close">&times;</span>
        <h2>취소 수수료 고지</h2>
        <div id="modal-cancel"></div>
    </div>
</div>

<script>
    const customerId = '${sessionScope.login.customerId}';
</script>

<!-- <%@ include file="../include/footer.jsp" %> -->

</body>
</html>
