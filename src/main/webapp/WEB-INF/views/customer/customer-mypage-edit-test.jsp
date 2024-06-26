<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>FoodieTree for 소비자</title>
    <style>
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
        .delete-btn {
            cursor: pointer;
            color: red;
        }
    </style>
</head>
<body>
<form action="/customer/mypage-main" id="customer-mypage-main" method="post">
    <div class="container">
        <div class="profile">
            <img src="${customerMyPageDto.profileImage}" alt="Customer profile image">
            <p class="edit-btn" onclick="editField('profileImage')">✏️</p>
            <h3 id="nickname" contenteditable="true" oninput="debounceCheckNickname()">${customerMyPageDto.nickname}</h3>
            <p class="edit-btn" onclick="editField('nickname')">✏️</p>
            <p>${customerMyPageDto.customerId}</p>
            <h4>마이페이지</h4>
            <p id="nickname-status"></p> <!-- Status message for nickname validation -->
        </div>
        <div class="info">
            <h4>선호 지역</h4>
            <ul id="preferredArea">
                <c:forEach var="area" items="${customerMyPageDto.preferredArea}">
                    <li onclick="deleteItem('preferredArea', '${area}')">${area} <span class="delete-btn">❌</span></li>
                </c:forEach>
            </ul>
            <h4>선호 음식</h4>
            <ul id="preferredFood">
                <c:forEach var="food" items="${customerMyPageDto.preferredFood}">
                    <li onclick="deleteItem('preferredFood', '${food.foodKoreanName}')">
                        <img src="${food.foodImage}" alt="선호음식이미지">
                        <span>${food.foodKoreanName}</span>
                        <span class="delete-btn">️❌</span>
                    </li>
<%--                    <li onclick="deleteItem('preferredFood', '${food}')">${food} <span class="delete-btn">❌</span></li>--%>
                </c:forEach>
            </ul>
            <h4>최애 가게</h4>
            <ul id="favStore">
                <c:forEach var="store" items="${customerMyPageDto.favStore}">
                    <li onclick="deleteItem('favStore', '${store.storeId}')">
                        <img src="${store.storeImg}" alt="최애가게이미지">
                        <span>${store.storeName}</span>
                        <span class="delete-btn">❤️</span>
                    </li>
                </c:forEach>
            </ul>
            <div class="stats">
                <div>10kg의 음쓰를 줄였습니다</div>
                <div>지금까지 10만원을 아꼈어요</div>
            </div>
        </div>
    </div>
</form>

<script>
    const BASE_URL = 'https://localhost:8083/customer';
    const customerId = 'test@gmail.com'; // Replace this with the actual customer ID

    let type;

    function editField(fieldId) {
        type = fieldId;
    }

    document.addEventListener('keyup', async function (event) {
        if (event.key === 'Enter' || type) {
            event.preventDefault();
            await fetchUpdates();
        }
    });

    async function fetchUpdates() {

        const customerId = 'test@gmail.com'; // Replace this with the actual customer ID
        const elements = document.querySelectorAll('[contenteditable="true"]');

        const updates = Array.from(elements).map(el => ({
            type: el.parentElement.id,
            value: el.innerText
        }));

        console.log('Updates to be sent:', updates); // Debugging line

        try {
            const response = await fetch(`${BASE_URL}/${customerId}/update`, {
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(updates)
            });

            if (response.ok) {
                console.log('Update successful');
            } else {
                const errorText = await response.text();
                console.error('Update failed:', errorText);
            }
        } catch (error) {
            console.error('Error updating data:', error);
        }
    }

    // Debounce function to delay the nickname check
    function debounce(func, wait) {
        let timeout;
        return function (...args) {
            clearTimeout(timeout);
            timeout = setTimeout(() => func.apply(this, args), wait);
        };
    }

    const checkNickname = async () => {
        const nicknameElement = document.getElementById('nickname');
        const nickname = nicknameElement.innerText;
        const statusElement = document.getElementById('nickname-status');

        try {
            const response = await fetch(`${BASE_URL}/check?nickname=${nickname}`);
            if (response.ok) {
                const result = await response.json();
                if (result) {
                    statusElement.innerText = "사용 가능한 닉네임입니다.";
                    statusElement.style.color = "green";
                } else {
                    statusElement.innerText = "이미 사용 중인 닉네임입니다.";
                    statusElement.style.color = "red";
                }
            } else {
                statusElement.innerText = "닉네임 확인 실패.";
                statusElement.style.color = "red";
            }
        } catch (error) {
            statusElement.innerText = "서버 오류.";
            statusElement.style.color = "red";
            console.error('Error checking nickname:', error);
        }
    };

    const debounceCheckNickname = debounce(checkNickname, 1000);

    async function deleteItem(type, value) {

        const payload = {
            type: type,
            value: value
        };

        try {
            const response = await fetch(`${BASE_URL}/${customerId}/delete`, {
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify([payload])
            });

            if (response.ok) {
                console.log('Delete successful');
                // Remove the item from the DOM
                const listItem = document.querySelector(`[onclick="deleteItem('${type}', '${value}')"]`);
                if (listItem) listItem.remove();
            } else {
                const errorText = await response.text();
                console.error('Delete failed:', errorText);
            }
        } catch (error) {
            console.error('Error deleting item:', error);
        }
    }

</script>
</body>
</html>
