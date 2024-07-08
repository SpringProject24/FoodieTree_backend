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
        .delete-btn {
            cursor: pointer;
            color: red;
        }

        .modal {
            display: none; /* 모달을 기본적으로 숨깁니다. */
            position: fixed;
            z-index: 1;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            overflow: auto;
            background-color: rgb(0,0,0);
            background-color: rgba(0,0,0,0.4);
        }
        .modal-content {
            background-color: #fefefe;
            margin: 15% auto;
            padding: 20px;
            border: 1px solid #888;
            width: 80%;
        }
        .close {
            color: #aaa;
            float: right;
            font-size: 28px;
            font-weight: bold;
        }
        .close:hover,
        .close:focus {
            color: black;
            text-decoration: none;
            cursor: pointer;
        }
    </style>
</head>
<body>
<form action="/customer/mypage" id="customer-mypage-main" method="post">
    <div class="container">
        <div class="profile">
            <img src="${customerMyPageDto.profileImage}" alt="Customer profile image">
            <p class="edit-btn" onclick="editField('profileImage')">✏️</p>
            <label for="nickname"></label>
            <input id="nickname" placeholder="${customerMyPageDto.nickname}"></input>
            <span class="edit-submit-btn" onclick="fetchUpdates('nickname', document.getElementById('nickname').value)">️✅</span>
            <p class="edit-btn">✏️</p>
            <p>${customerMyPageDto.customerId}</p>
            <label for="customerPhoneNumber"></label>
            <input id="customerPhoneNumber" placeholder="${customerMyPageDto.customerPhoneNumber}"></input>
            <span class="edit-submit-btn" onclick="fetchUpdates('customer_phone_number', document.getElementById('customerPhoneNumber').value)">️✅</span>
            <p class="edit-btn">✏️</p>
            <button id="reset-pw-btn">비밀번호 재설정</button>
            <a href="/customer/mypage">
                <h4>마이페이지</h4>
            </a>
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
                    <li onclick="deleteItem('preferredFood', '${food.preferredFood}')">
                        <img id="food-img" src="${food.foodImage}" alt="선호음식이미지">
                        <span>${food.preferredFood}</span>
                        <span class="delete-btn">️❌</span>
                    </li>
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
<!-- 비밀번호 재설정 모달 -->
<div id="resetPasswordModal" class="modal">
    <div class="modal-content">
        <span class="close" onclick="closeModal()">&times;</span> <!-- X 버튼 추가 -->
        <h2>비밀번호 재설정</h2>
        <div id="emailStep">
            <p>인증번호를 받으세요.</p>
            <button id="sendVerificationCodeBtn" onclick="sendVerificationCode()">인증번호 받기</button>
        </div>
        <div id="codeStep" class="hidden">
            <p>인증번호를 입력하세요.</p>
            <input type="text" id="verificationCode" maxlength="6">
            <button onclick="verifyCode()">인증하기</button>
            <div id="verificationResult"></div>
        </div>
        <div id="countdown"></div>
    </div>
</div>

<!-- 비밀번호 재설정 입력 모달 -->
<div id="newPasswordModal" class="modal">
    <div class="modal-content">
        <span class="close" onclick="closeNewPwModal()">&times;</span> <!-- X 버튼 추가 -->
        <h2>새 비밀번호 설정</h2>
        <div class="pass">
            <input id="new-password-input" type="password" name="password" placeholder="새 비밀번호를 입력해주세요" onkeyup="debounceCheckPassword()">
        </div>
        <div class="pass-check">
            <input id="new-password-check" type="password" name="password-chk" placeholder="새 비밀번호를 다시 입력해주세요" onkeyup="debounceCheckPassword()">
            <div class="wrapper">
                <button id="submit-new-pw" onclick="updatePassword()" disabled>비밀번호 재설정하기</button>
            </div>
        </div>
        <div id="password-match-status"></div> <!-- 비밀번호 일치 여부 표시 -->
    </div>
</div>

<script>
    const BASE_URL = 'http://localhost:8083/customer';
    const customerId = 'sji4205@naver.com'; // Replace this with the actual customer ID

    let type;
    let countdownInterval;
    let debounceTimeout;

    function editField(fieldId) {
        type = fieldId;
    }

    async function fetchUpdates(type, value) {
        const payload = {
            type: type,
            value: value
        };
        console.log('Updates to be sent:', payload); // Debugging line

        try {
            const response = await fetch(`\${BASE_URL}/\${customerId}/update`, {
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify([payload])
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

    async function deleteItem(type, value) {

        const payload = {
            type: type,
            value: value
        };

        try {
            const response = await fetch(`\${BASE_URL}/\${customerId}/delete`, {
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify([payload])
            });

            if (response.ok) {
                console.log('Delete successful');
                // Remove the item from the DOM
                const listItem = document.querySelector(`[onclick="deleteItem('\${type}', '\${value}')"]`);
                if (listItem) listItem.remove();
            } else {
                const errorText = await response.text();
                console.error('Delete failed:', errorText);
            }
        } catch (error) {
            console.error('Error deleting item:', error);
        }
    }
    function handleKeyUp(event, fieldId) {
            event.preventDefault();
        if (event.key === 'Enter') {
            const element = event.target;
            const value = element.innerText;
            console.log(element);
            console.log(value);
            element.blur(); // Remove focus to trigger the update
            fetchUpdates(fieldId, value);
        }
    }


    // 비밀번호 재설정 모달 관련 함수
    function openModal(e) {
        e.preventDefault();
        document.getElementById('resetPasswordModal').style.display = 'block';
    }

    function closeModal() {
        document.getElementById('resetPasswordModal').style.display = 'none';
    }

    // 비밀번호 재설정 입력 모달 관련 함수
    function openNewPwModal() {
        // e.preventDefault();
        document.getElementById('newPasswordModal').style.display = 'block';
    }

    function closeNewPwModal() {
        document.getElementById('newPasswordModal').style.display = 'none';
    }

    // X 버튼 클릭 시 모달 닫기
    document.addEventListener('DOMContentLoaded', function() {
        const closeButtons = document.querySelectorAll('.close');
        closeButtons.forEach(button => button.addEventListener('click', closeModal));
        closeButtons.forEach(button => button.addEventListener('click', closeNewPwModal));

        // 모달 바깥 클릭 시 모달 닫기
        window.onclick = function(event) {
            const resetModal = document.getElementById('resetPasswordModal');
            const newPwModal = document.getElementById('newPasswordModal');
            if (event.target === resetModal) {
                closeModal();
            }
            if (event.target === newPwModal) {
                closeNewPwModal();
            }
        };
    });


    async function sendVerificationCode() {
        try {
            const response = await fetch(`http://localhost:8083/email/sendVerificationCode`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ email: customerId,
                    purpose: 'reset'
                }) // Replace with actual email
            });

            if (response.ok) {
                startCountdown(300); // 5분(300초) 카운트다운 시작
                document.getElementById('emailStep').classList.add('hidden');
                document.getElementById('codeStep').classList.remove('hidden');
            } else {
                console.error('Failed to send verification code');
            }
        } catch (error) {
            console.error('Error sending verification code:', error);
        }
    }

    async function verifyCode() {
        const code = document.getElementById('verificationCode').value;
        try {
            const response = await fetch('http://localhost:8083/email/verifyCode', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ email: customerId, code: code }) // Replace with actual email and code
            });
            console.log(response);

            if (response.ok) {
                const result = await response.text();
                document.getElementById('verificationResult').textContent = result;
                clearInterval(countdownInterval); // 인증 성공 시 타이머 멈춤
                openNewPwModal(); // 새로운 비밀번호 입력 모달 표시
            } else {
                console.error('Verification failed');
                document.getElementById('verificationResult').textContent = '실패';
            }
        } catch (error) {
            console.error('Error verifying code:', error);
            document.getElementById('verificationResult').innerText = '실패';
        }
    }

    function startCountdown(seconds) {
        const countdownElement = document.getElementById('countdown');
        countdownElement.textContent = `남은 시간: \${seconds}초`;

        countdownInterval = setInterval(() => {
            seconds -= 1;
            countdownElement.textContent = `남은 시간: \${seconds}초`;

            if (seconds <= 0) {
                clearInterval(countdownInterval);
                countdownElement.textContent = '시간 초과';
                closeModal(); // 모달 닫기
            }
        }, 1000);
    }

    function debounce(func, delay) {
        return function() {
            const context = this;
            const args = arguments;
            clearTimeout(debounceTimeout);
            debounceTimeout = setTimeout(() => func.apply(context, args), delay);
        };
    }

    function checkPasswordMatch() {
        const newPassword = document.getElementById('new-password-input').value;
        const newPasswordCheck = document.getElementById('new-password-check').value;
        const statusElement = document.getElementById('password-match-status');
        const submitBtn = document.getElementById('submit-new-pw');

        if (newPassword && newPasswordCheck) {
            if (newPassword === newPasswordCheck) {
                statusElement.textContent = '비밀번호가 일치합니다.';
                statusElement.style.color = 'green';
                submitBtn.disabled = false; // Enable the button when passwords match
            } else {
                statusElement.textContent = '비밀번호가 일치하지 않습니다.';
                statusElement.style.color = 'red';
                submitBtn.disabled = true; // Disable the button when passwords don't match
            }
        } else {
            statusElement.textContent = '';
            submitBtn.disabled = true; // Disable the button if any field is empty
        }
    }

    const debounceCheckPassword = debounce(checkPasswordMatch, 1000);

    async function updatePassword() {
        const newPassword = document.getElementById('new-password-input').value;
        const newPasswordCheck = document.getElementById('new-password-check').value;

        if (newPassword !== newPasswordCheck) {
            alert('비밀번호가 일치하지 않습니다. 다시 입력해주세요.');
            return;
        }

        try {
            const response = await fetch(`\${BASE_URL}/\${customerId}/update/password`, {
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ type: 'password', value: newPassword })
            });

            if (response.ok) {
                alert('비밀번호가 성공적으로 변경되었습니다.');
                closeNewPwModal();
            } else {
                const errorText = await response.text();
                console.error('Password update failed:', errorText);
                alert('비밀번호 변경에 실패했습니다.');
            }
        } catch (error) {
            console.error('Error updating password:', error);
            alert('비밀번호 변경 중 오류가 발생했습니다.');
        }
    }

    const $btn = document.getElementById('reset-pw-btn');
    $btn.addEventListener('click', openModal);

    const $submitBtn = document.getElementById('submit-new-pw');
    $submitBtn.addEventListener('click', openNewPwModal);
</script>
</body>
</html>
