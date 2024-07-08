<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c"
           uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>FoodieTree</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css"
          rel="stylesheet"
          integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
          crossorigin="anonymous">
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css"
          integrity="sha512-SnH5WK+bZxgPHs44uWIX+LLJAJ9/2PkPKZ5QiAj6Ta86w+fsb2TkcmfRyVX3pBnMFcV7oQPJkl9QevSCWr3W6A=="
          crossorigin="anonymous" referrerpolicy="no-referrer"/>
          <!-- font link -->
    <link href="https://fonts.googleapis.com/css2?family=Francois+One&family=Margarine&family=Nanum+Gothic&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="/assets/css/common.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/swiper@11/swiper-bundle.min.css"/>
    <link rel="stylesheet" href="/assets/css/product/main.css">
    <link rel="stylesheet" href="/assets/css/product/modal.css">
    <link rel="stylesheet" href="/assets/css/product/like-btn.css">

    <style>
      .rectangle {
        width: 100px;
        height: 100px;
        background-color: red;
      }

      .circle {
        width: 100px;
        height: 100px;
        background-color: blue;
        border-radius: 50%;
      }

      .triangle {
        width: 0;
        height: 0;
        border-left: 50px solid transparent;
        border-right: 50px solid transparent;
        border-bottom: 100px solid green;
      }
    </style>
</head>
<body>

<!-- Modal -->
<div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel"
     aria-hidden="true">
    <div class="modal-dialog modal-dialog-scrollable">
        <div class="modal-content">
            <div class="modal-header">
                <div class="wrapper">
                    <!--                    heart animation -->
                    <div class="heart-container" title="Like">
                        <input type="checkbox" class="checkbox" id="Give-It-An-Id">
                        <div class="svg-container">
                            <svg viewBox="0 0 24 24" class="svg-outline" xmlns="http://www.w3.org/2000/svg">
                                <path d="M17.5,1.917a6.4,6.4,0,0,0-5.5,3.3,6.4,6.4,0,0,0-5.5-3.3A6.8,6.8,0,0,0,0,8.967c0,4.547,4.786,9.513,8.8,12.88a4.974,4.974,0,0,0,6.4,0C19.214,18.48,24,13.514,24,8.967A6.8,6.8,0,0,0,17.5,1.917Zm-3.585,18.4a2.973,2.973,0,0,1-3.83,0C4.947,16.006,2,11.87,2,8.967a4.8,4.8,0,0,1,4.5-5.05A4.8,4.8,0,0,1,11,8.967a1,1,0,0,0,2,0,4.8,4.8,0,0,1,4.5-5.05A4.8,4.8,0,0,1,22,8.967C22,11.87,19.053,16.006,13.915,20.313Z">
                                </path>
                            </svg>
                            <svg viewBox="0 0 24 24" class="svg-filled" xmlns="http://www.w3.org/2000/svg">
                                <path d="M17.5,1.917a6.4,6.4,0,0,0-5.5,3.3,6.4,6.4,0,0,0-5.5-3.3A6.8,6.8,0,0,0,0,8.967c0,4.547,4.786,9.513,8.8,12.88a4.974,4.974,0,0,0,6.4,0C19.214,18.48,24,13.514,24,8.967A6.8,6.8,0,0,0,17.5,1.917Z">
                                </path>
                            </svg>
                            <svg class="svg-celebrate" width="100" height="100" xmlns="http://www.w3.org/2000/svg">
                                <polygon points="10,10 20,20"></polygon>
                                <polygon points="10,50 20,50"></polygon>
                                <polygon points="20,80 30,70"></polygon>
                                <polygon points="90,10 80,20"></polygon>
                                <polygon points="90,50 80,50"></polygon>
                                <polygon points="80,80 70,70"></polygon>
                            </svg>
                        </div>
                    </div>
                    <!--                    heart animation end -->
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close">
                        <i class="fa-solid fa-xmark"></i>
                    </button>
                </div>
                <div class="wrapper info">
                    <div id="product-cnt">
                        1개 남음
                    </div>
                    <div class="store-info">
                        <div class="img-box">
                            <img id="store-img" alt="">   <!-- 가게 사진-->
                        </div>
                        <h6 id="store-name">가게이름</h6>
                    </div>
                </div>
            </div>
            <div class="modal-body">
                <div class="detail">
                    <div class="prod-info-wrapper">
                        <div class="prod-info">
                            <div id="prod-category">
                                <i class="fa-solid fa-basket-shopping"></i>
                                <span>음식종류</span>
                            </div>
                            <div id="prod-rate">
                                <i class="fa-solid fa-star"></i>
                                <span>평점</span>
                            </div>
                            <div id="pickup-time">
                                <i class="fa-solid fa-clock"></i>
                                <span>픽업시간</span>
                            </div>
                        </div>
                        <div class="prod-info">
                            <div id="prod-price">
                                17000 원
                            </div>
                            <div id="prod-discount">
                                4900 원
                            </div>
                        </div>
                    </div>
                </div>
                <div class="detail">
                    <div class="store-area wrapper">
                        <i class="fa-solid fa-location-dot"></i>
                        <p id="store-area">서울시 마포구 대현동</p>
                    </div>
                </div>
                <div class="detail">
                    <div class="desc-area wrapper">
                        <svg xmlns="http://www.w3.org/2000/svg" shape-rendering="geometricPrecision" text-rendering="geometricPrecision" image-rendering="optimizeQuality" fill-rule="evenodd" clip-rule="evenodd" viewBox="0 0 482 511.93"><path fill-rule="nonzero" d="m277.15 355.47-129.39-67.81L115.79 327c47.18 24.94 89.8 47.83 137 72.79l24.36-44.32zM191.5 208.38c4.84 0 8.77 3.92 8.77 8.76s-3.93 8.77-8.77 8.77-8.76-3.93-8.76-8.77 3.92-8.76 8.76-8.76zm185.52 9.13c4.84 0 8.76 3.92 8.76 8.76s-3.92 8.77-8.76 8.77c-4.85 0-8.77-3.93-8.77-8.77s3.92-8.76 8.77-8.76zm74.65-148.53c5.38 0 9.74 4.36 9.74 9.75 0 5.38-4.36 9.74-9.74 9.74s-9.74-4.36-9.74-9.74c0-5.39 4.36-9.75 9.74-9.75zm-274.01 9.88c6.35 0 11.49 5.15 11.49 11.5s-5.14 11.49-11.49 11.49c-6.35 0-11.5-5.14-11.5-11.49 0-6.35 5.15-11.5 11.5-11.5zm-34.45 89.91c7.17 0 12.98 5.81 12.98 12.98 0 7.16-5.81 12.97-12.98 12.97-7.16 0-12.97-5.81-12.97-12.97 0-7.17 5.81-12.98 12.97-12.98zM231.86 0l17.02 29.17 32.89-2.76-21 24.78 12.54 30.75-30.13-12.48-24.72 21.12 2.21-32.72-28.62-17.44 33.19-7.38L231.86 0zm118.07 161.29c4.84 0 8.77 3.92 8.77 8.77 0 4.84-3.93 8.76-8.77 8.76s-8.77-3.92-8.77-8.76c0-4.85 3.93-8.77 8.77-8.77zm82.72 38.11-1.25-.35c2.88-10.22 1.58-20.22-3.91-30-5.48-9.79-13.33-16.12-23.54-19l.35-1.25c10.22 2.88 20.21 1.58 30-3.93 9.79-5.5 16.13-13.34 18.99-23.52l1.26.35c-2.88 10.22-1.58 20.21 3.9 30 5.49 9.79 13.34 16.12 23.55 19l-.35 1.25c-10.22-2.88-20.22-1.58-30 3.9-9.79 5.49-16.12 13.34-19 23.55zm-61.45-98.38h-1.62c0-13.19-4.93-24.73-14.8-34.59-9.86-9.87-21.4-14.8-34.6-14.8v-1.62c13.2 0 24.74-4.93 34.6-14.82 9.87-9.89 14.8-21.42 14.8-34.58h1.62c0 13.2 4.93 24.73 14.8 34.6 9.87 9.87 21.4 14.8 34.6 14.8v1.62c-13.2 0-24.73 4.93-34.6 14.8-9.87 9.86-14.8 21.4-14.8 34.59zM68.87 128.06l-1.44.75c-6.12-11.69-15.84-19.62-29.16-23.78s-25.82-3.18-37.52 2.94L0 106.54c11.7-6.13 19.62-15.85 23.77-29.18 4.15-13.34 3.17-25.85-2.93-37.5l1.43-.75C28.4 50.8 38.12 58.73 51.44 62.89c13.31 4.16 25.82 3.18 37.51-2.94l.75 1.43C78.01 67.5 70.08 77.23 65.92 90.54c-4.16 13.32-3.18 25.83 2.95 37.52zM291.4 287.62a6.511 6.511 0 0 1-5.98 3.69c-1.08.75-2.4 1.17-3.81 1.14-.48-.01-.94-.07-1.39-.18a6.5 6.5 0 0 1-8.2-3.94c-14.91-41.24-38.21-76.53-67.05-105.6-31.26-31.53-69.05-55.8-109.73-72.53-3.32-1.36-4.9-5.16-3.54-8.48 1.37-3.32 5.17-4.9 8.48-3.54 42.24 17.37 81.5 42.6 114.01 75.38 20.91 21.08 39.03 45.29 53.37 72.53L244.59 98.46c-.55-3.55 1.88-6.87 5.42-7.41a6.495 6.495 0 0 1 7.41 5.42l22.67 145.62c2.81-14.15 6.76-28.09 11.66-41.7 10.98-30.51 26.68-59.38 44.9-85.52a6.495 6.495 0 0 1 9.06-1.63c2.96 2.05 3.68 6.11 1.63 9.06-17.66 25.35-32.83 53.21-43.36 82.47-7.01 19.47-11.99 39.6-14.31 60.05 10.23-15.34 25.53-33.46 45.01-50.01 20.18-17.15 44.95-32.73 73.34-41.94 3.42-1.11 7.09.76 8.2 4.18a6.507 6.507 0 0 1-4.18 8.2c-26.61 8.64-49.91 23.31-68.95 39.49-26 22.09-43.97 46.81-51.69 62.88zM187.97 261.3l-30.77 14.87 126.99 66.84 127.01-66.97-27.9-13.98c4.13-3.04 8.31-5.97 12.49-8.8l23.44 10.08 37.73-35.71-12.34-6.54c3.18-2.29 6.18-4.55 8.94-6.82l19.76 10.44c.44.24.85.54 1.22.92a4.982 4.982 0 0 1-.06 7.06l-44.81 44.02 43.4 51.94a5.015 5.015 0 0 1-.63 7.04c-.32.26-.66.49-1.02.66l-38.96 20.6v77.21a5.01 5.01 0 0 1-3.09 4.62l-140.86 71.5a4.97 4.97 0 0 1-3.7 1.65c-1.96 0-3.65-1.12-4.47-2.76l-141.13-70.55a4.984 4.984 0 0 1-2.76-4.46l-.02-76.83-39.69-20.98c-.36-.17-.69-.4-1.01-.66a4.997 4.997 0 0 1-.63-7.04l42.28-50.59-49.05-46.48c-1.82-2.07-1.6-5.22.47-7.04.31-.26.62-.49.96-.66l22.88-12.95c3.05 2.91 5.97 5.39 8.53 7.34l-15.75 8.8 41.39 37.97 36.57-17.7c1.5 4.72 3.04 9.43 4.59 13.96zm232.81 27.53-126.5 63.38 27.88 48.05 133.28-69.41-34.66-42.02z"/></svg>
                        <p>
                            서프라이즈백은 그 날에 남은 맛있는 음식들이 랜덤으로 채워져 있어요!
                        </p>
                    </div>
                </div>
                <div class="detail">
                    <div class="nutrient-allergy-area wrapper">
                        <p>
                            영양 및 알러지 정보
                        </p>
                    </div>
                </div>
                <div class="detail">
                    <div class="rate-area wrapper">
                        <p>
                            평점
                        </p>
                        <div class="rate">
                            4.5 / 5.0
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button id="reservation-btn" type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#exampleModal1">예약하기</button>

            </div>
        </div>
    </div>
</div>

<header>
    <div class="container">
        <div class="logo-wrapper">
            <a href="/" class="logo margarine-regular"><h1>FoodieTree</h1></a>
        </div>
        <div class="input-wrapper">
            <button><i class="fa-solid fa-search"></i></button>
            <input type="text" placeholder="Search">
        </div>
        <ul class="profile-wrapper">
            <li>
                <a href="#" class="profile img-box">
                    <img src="/assets/img/western.jpg" alt="">
                </a>
            </li>
            <li>
                <a href="/customer/mypage">마이페이지</a>
            </li>
            <li>
                <a href="/customer/signout">로그아웃</a>
            </li>
        </ul>
    </div>
</header>

<section class="container category">
    <div class="swiper category-list">
        <div class="swiper-wrapper">
            <div class="swiper-slide">
                <div class="item">
                    <div class="img-box">
                        <img src="/assets/img/western.jpg" alt="">
                    </div>
                    <p>양식</p>
                </div>
            </div>
            <div class="swiper-slide">
                <div class="item">
                    <div class="img-box">
                        <img src="/assets/img/cafe.jpg" alt="">
                    </div>
                    <p>카페</p>
                </div>
            </div>
            <div class="swiper-slide">
                <div class="item">
                    <div class="img-box">
                        <img src="/assets/img/dessert.jpg" alt="">
                    </div>
                    <p>디저트</p>
                </div>
            </div>
            <div class="swiper-slide">
                <div class="item">
                    <div class="img-box">
                        <img src="/assets/img/japanese.jpg" alt="">
                    </div>
                    <p>일식</p>
                </div>
            </div>
            <div class="swiper-slide">
                <div class="item">
                    <div class="img-box">
                        <img src="/assets/img/chinese.jpg" alt="">
                    </div>
                    <p>중식</p>
                </div>
            </div>
            <div class="swiper-slide">
                <div class="item">
                    <div class="img-box">
                        <img src="/assets/img/korean.jpg" alt="">
                    </div>
                    <p>한식</p>
                </div>
            </div>
            <div class="swiper-slide">
                <div class="item">
                    <div class="img-box">
                        <img src="/assets/img/etc.jpg" alt="">
                    </div>
                    <p>기타</p>
                </div>
            </div>
        </div>
    </div>
</section>
<section class="container category">
    <div class="swiper myswiper1">
        <div class="title-wrapper">
            <h2>선호하는 음식</h2>
            <div class="swiper-button-prev">
                <i class="fa-solid fa-arrow-left"></i>
            </div>
            <div class="swiper-button-next">
                <i class="fa-solid fa-arrow-right"></i>
            </div>
        </div>
        <div class="swiper-wrapper">
            <c:forEach var="item" items="${findByFood}">
                <div class="swiper-slide">
                    <div class="item" data-bs-toggle="modal" data-bs-target="#exampleModal">
                        <div class="store-img-box">
                            <img src="/assets/img/western.jpg" alt="">
                        </div>
                        <div class="store-info">
                            <h3>가게 이름 : ${item.storeName}</h3>
                            <p>픽업 시간 : ${item.formattedPickupTime}</p>
                            <div class="wrapper">
                                <p>평점 / 거리</p>
                                <p>가격 : ${item.price}</p>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>

    </div>
</section>

<section class="container category">
    <div class="swiper myswiper2">
        <div class="title-wrapper">
            <h2>선호하는 지역</h2>
            <div class="btn-wrapper">
                <div class="swiper-button-prev-b circle">
                    <i class="fa-solid fa-arrow-left"></i>
                </div>
                <div class="swiper-button-next-b circle">
                    <i class="fa-solid fa-arrow-right"></i>
                </div>
            </div>
        </div>
        <div class="swiper-wrapper">


            <c:forEach var="item" items="${findByArea}">
                <div class="swiper-slide">
                    <div class="item" data-bs-toggle="modal" data-bs-target="#exampleModal">
                        <div class="store-img-box">
                            <img src="/assets/img/western.jpg" alt="">
                        </div>
                        <div class="store-info">
                            <h3>가게 이름 : ${item.storeName}</h3>
                            <p>픽업시간 : ${item.formattedPickupTime}</p>
                            <div class="wrapper">
                                <p>평점 / 거리</p>
                                <p>가격 : ${item.price}</p>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>

            <div class="swiper-slide">
                <div class="circle"></div>
            </div>
            <div class="swiper-slide">
                <div class="rectangle"></div>
            </div>
            <div class="swiper-slide">
                <div class="circle"></div>
            </div>
            <div class="swiper-slide">
                <div class="triangle"></div>
            </div>
            <div class="swiper-slide">
                <div class="circle"></div>
            </div>
            <div class="swiper-slide">
                <div class="rectangle"></div>
            </div>
            <div class="swiper-slide">
                <div class="circle"></div>
            </div>
            <div class="swiper-slide">
                <div class="triangle"></div>
            </div>
            <div class="swiper-slide">
                <div class="circle"></div>
            </div>
        </div>
    </div>
</section>

<section class="container category">
    <div class="swiper myswiper3">
        <div class="title-wrapper">
            <h2>최애 가게</h2>
            <div class="btn-wrapper">
                <div class="swiper-button-prev-b circle">
                    <i class="fa-solid fa-arrow-left"></i>
                </div>
                <div class="swiper-button-next-b circle">
                    <i class="fa-solid fa-arrow-right"></i>
                </div>
            </div>
        </div>
        <div class="swiper-wrapper">


            <c:forEach var="item" items="${findByLike}">
                <div class="swiper-slide">
                    <div class="item" data-bs-toggle="modal" data-bs-target="#exampleModal">
                        <div class="store-img-box">
                            <img src="
                /assets/img/western.jpg" alt="">
                        </div>
                        <div class="store-info">
                            <h3>가게 이름 : ${item.storeName}</h3>
                            <p>픽업 시간 : ${item.pickupTime}</p>
                            <div class="wrapper">
                                <p>평점 / 거리</p>
                                <p>가격 : ${item.price}</p>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>


            <div class="swiper-slide">
                <div class="item" data-bs-toggle="modal" data-bs-target="#exampleModal">
                    <div class="store-img-box">
                        <img src="
            /assets/img/cafe.jpg" alt="">
                    </div>
                    <div class="store-info">
                        <h3>가게 이름</h3>
                        <p>픽업 시간</p>
                        <div class="wrapper">
                            <p>평점 / 거리</p>
                            <p>가격</p>
                        </div>
                    </div>
                </div>
            </div>
            <div class="swiper-slide">
                <div class="item" data-bs-toggle="modal" data-bs-target="#exampleModal">
                    <div class="store-img-box">
                        <img src="
            /assets/img/dessert.jpg" alt="">
                    </div>
                    <div class="store-info">
                        <h3>가게 이름</h3>
                        <p>픽업 시간</p>
                        <div class="wrapper">
                            <p>평점 / 거리</p>
                            <p>가격</p>
                        </div>
                    </div>
                </div>
            </div>
            <div class="swiper-slide">
                <div class="item" data-bs-toggle="modal" data-bs-target="#exampleModal">
                    <div class="store-img-box">
                        <img src="
            /assets/img/japanese.jpg" alt="">
                    </div>
                    <div class="store-info">
                        <h3>가게 이름</h3>
                        <p>픽업 시간</p>
                        <div class="wrapper">
                            <p>평점 / 거리</p>
                            <p>가격</p>
                        </div>
                    </div>
                </div>
            </div>
            <div class="swiper-slide">
                <div class="item" data-bs-toggle="modal" data-bs-target="#exampleModal">
                    <div class="store-img-box">
                        <img src="
            /assets/img/chinese.jpg" alt="">
                    </div>
                    <div class="store-info">
                        <h3>가게 이름</h3>
                        <p>픽업 시간</p>
                        <div class="wrapper">
                            <p>평점 / 거리</p>
                            <p>가격</p>
                        </div>
                    </div>
                </div>
            </div>
            <div class="swiper-slide">
                <div class="item" data-bs-toggle="modal" data-bs-target="#exampleModal">
                    <div class="store-img-box">
                        <img src="
            /assets/img/korean.jpg" alt="">
                    </div>
                    <div class="store-info">
                        <h3>가게 이름</h3>
                        <p>픽업 시간</p>
                        <div class="wrapper">
                            <p>평점 / 거리</p>
                            <p>가격</p>
                        </div>
                    </div>
                </div>
            </div>
            <div class="swiper-slide">
                <div class="item" data-bs-toggle="modal" data-bs-target="#exampleModal">
                    <div class="store-img-box">
                        <img src="
            /assets/img/etc.jpg" alt="">
                    </div>
                    <div class="store-info">
                        <h3>가게 이름</h3>
                        <p>픽업 시간</p>
                        <div class="wrapper">
                            <p>평점 / 거리</p>
                            <p>가격</p>
                        </div>
                    </div>
                </div>
            </div>
            <div class="swiper-slide">
                <div class="circle"></div>
            </div>
            <div class="swiper-slide">
                <div class="rectangle"></div>
            </div>
            <div class="swiper-slide">
                <div class="circle"></div>
            </div>
            <div class="swiper-slide">
                <div class="triangle"></div>
            </div>
            <div class="swiper-slide">
                <div class="circle"></div>
            </div>
            <div class="swiper-slide">
                <div class="rectangle"></div>
            </div>
            <div class="swiper-slide">
                <div class="circle"></div>
            </div>
            <div class="swiper-slide">
                <div class="triangle"></div>
            </div>
            <div class="swiper-slide">
                <div class="circle"></div>
            </div>
        </div>
      </div>
    </section>

   <%@ include file="../include/footer.jsp" %>
  </body>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM"
        crossorigin="anonymous"></script>
  <script src="https://cdn.jsdelivr.net/npm/swiper@11/swiper-bundle.min.js"></script>

  <script>
    new Swiper(".category-list", {
      slidesPerView: 7,
      centralDirOffset: 1,
      navigation: {
        nextEl: ".swiper-button-next",
        prevEl: ".swiper-button-prev",
      },
    });

    new Swiper(".myswiper1", {
      spaceBetween: 20,
      slidesPerView: 4,
      slidesPerGroup: 4,
      loop: true,
      speed: 1200,
      navigation: {
        nextEl: ".swiper-button-next",
        prevEl: ".swiper-button-prev",
      },
    });

    new Swiper(".myswiper2", {
      spaceBetween: 20,
      slidesPerView: 5,
      slidesPerGroup: 5,
      speed: 1200,
      navigation: {
        nextEl: ".swiper-button-next-b",
        prevEl: ".swiper-button-prev-b",
      },
    });

  new Swiper('.myswiper3', {
    spaceBetween: 20,
    slidesPerView: 5,
    slidesPerGroup: 5,
    speed: 1200,
    navigation: {
      nextEl: '.swiper-button-next-b',
      prevEl: '.swiper-button-prev-b',
    }
  });
</script>
<script>
  document.querySelector('body').addEventListener('click', e => {
    if (!e.target.matches('.swiper-slide *')) {
      return;
    }
    const $modalBody = document.querySelector('.modal-content .modal-body');
    const gradient = 'linear-gradient(rgba(0, 0, 0, 0.2), rgba(0, 0, 0, 0.5))';
    const getImgSrc = e.target.closest('.item').querySelector('.store-img-box img').src;
    const imageUrl = `url('\${getImgSrc}')`;
    document.querySelector(
        '.modal-content .modal-header').style.background = `\${gradient}, \${imageUrl} no-repeat center center / cover`; // productImg
    document.getElementById('store-img').src = getImgSrc; // storeImg
  });

</script>
</html>
