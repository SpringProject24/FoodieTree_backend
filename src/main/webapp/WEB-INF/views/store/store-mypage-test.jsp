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
                <li class="nav-item"><a class="nav-link" href="mypage">마이페이지</a></li>
                <li class="nav-item"><a class="nav-link" href="mypage-edit">개인정보수정</a></li>
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
                                <li id="reservation-${status.index}" class="reservation-item">
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
                    <h3 class="title-text">오늘의 랜덤박스 수량</h3>
                </div>
                <button id="decrease">감소</button>
                <span id="count">${storeInfo.productCnt}</span>
                <button id="increase">증가</button>
                <button id="update">수량업데이트 하기</button>
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

<script>

    const BASE_URL = 'http://localhost:8083';

    document.addEventListener('DOMContentLoaded', () => {
        const calendarElement = document.getElementById('calendar');
        const statusElement = document.getElementById('status');
        const currentMonthElement = document.getElementById('current-month');
        const prevMonthButton = document.getElementById('prev-month');
        const nextMonthButton = document.getElementById('next-month');

        let today = new Date();
        let currentYear = today.getFullYear();
        let currentMonth = today.getMonth();

        function updateCalendar(year, month) {
            calendarElement.innerHTML = '';
            const date = new Date(year, month);
            const daysInMonth = new Date(year, month + 1, 0).getDate();
            currentMonthElement.textContent = date.toLocaleDateString('default', { year: 'numeric', month: 'long' });

            for (let i = 1; i <= daysInMonth; i++) {
                const dayElement = document.createElement('div');
                dayElement.textContent = i;
                dayElement.classList.add('day');
                if (year === today.getFullYear() && month === today.getMonth() && i === today.getDate()) {
                    dayElement.classList.add('today');
                }
                dayElement.addEventListener('click', () => closeStoreForDay(year, month, i));
                calendarElement.appendChild(dayElement);
            }
        }

        async function closeStoreForDay(year, month, day) {
            const dateString = `\${year}-\${String(month + 1).padStart(2, '0')}-\${String(day).padStart(2, '0')}`;
            const response = await fetch(`\${BASE_URL}/store/mypage/main/calendar`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ date: dateString })
            });

            if (response.ok) {
                statusElement.textContent = `The store will be closed on \${dateString}.`;
            } else {
                statusElement.textContent = `Failed to close the store on \${dateString}.`;
            }
        }

        prevMonthButton.addEventListener('click', () => {
            currentMonth--;
            if (currentMonth < 0) {
                currentMonth = 11;
                currentYear--;
            }
            updateCalendar(currentYear, currentMonth);
        });

        nextMonthButton.addEventListener('click', () => {
            currentMonth++;
            if (currentMonth > 11) {
                currentMonth = 0;
                currentYear++;
            }
            updateCalendar(currentYear, currentMonth);
        });

        updateCalendar(currentYear, currentMonth);
    });

</script>
</body>
</html>
