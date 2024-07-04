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
    <link rel="stylesheet" href="/assets/css/customer/customer-mypage-edit.css">
    <script defer src="/assets/js/store/store-mypage.js"></script>
    <script defer src="/assets/js/store/store-mypage-edit.js"></script>
</head>
<body>
<header>
    <div class="container">
        <div class="logo"><h1>FoodieTree</h1></div>
        <div class="logo-img">
            <img src="/assets/img/img_2.png" alt="logo">
        </div>
    </div>
</header>
<section class="my-page-area">
    <div class="container">
        <div class="profile">
            <h2>${customerMyPageDto.customerId}</h2>
            <li class="nav-item"><a class="nav-link" href="/store/mypage/main">마이페이지</a></li>
            <li class="nav-item"><a class="nav-link" href="/store/mypage/edit/main">개인정보수정</a></li>
            <div class="stats">
                <div>${stats.coTwo}kg의 이산화탄소 배출을 줄였습니다</div>
                <div>지금까지 ${stats.customerCnt}명의 손님을 만났어요</div>
            </div>
        </div>
        <div class="edit">
            <div class="edit-box">
                <div class="title">
                    <h3 class="title-text">내프로필</h3>
                </div>
                <div class="edit-wrapper">
                    <div class="input-area">
                        <div class="input-wrapper">
                            <div class="icon"><i class="fa-solid fa-user"></i></div>
                            <div>${storeInfo.storeName}</div>
                        </div>
                        <div class="input-wrapper">
                            <div class="icon"><i class="far fa-clock"></i></div>
                            <div>픽업 시작 시간
                                <label>
                                <input type="time" value="${storeInfo.openAt}"/>
                            </label>
                                <i class="fa-regular fa-square-check"
                                   style="color: #45a049; font-size: 25px; cursor: pointer"></i>
                            </div>
                        </div>
                        <div class="input-wrapper">
                            <div class="icon"><i class="far fa-clock"></i></div>
                            <div>픽업 마감 시간
                                <label>
                                    <input type="time" value="${storeInfo.closedAt}"/>
                                </label>
                                <i class="fa-regular fa-square-check"
                                   style="color: #45a049; font-size: 25px; cursor: pointer"></i>
                                </div>
                        </div>
                        <div class="input-wrapper">
                            <div class="icon"><i class="fa-solid fa-user"></i></div>
                            <div>기본 수량 값
                                <label>
                                    <input type="number" value="${storeInfo.productCnt}" min="1"/>
                                </label>
                                <i class="fa-regular fa-square-check"
                                   style="color: #45a049; font-size: 25px; cursor: pointer"></i>
                            </div>
                        </div>


                        <div class="input-wrapper">
                            <i class="fas fa-dollar-sign"></i>
                            <select id="price">
                                <option value="3900">3900</option>
                                <option value="5900">5900</option>
                                <option value="7900">7900</option>
                            </select>
                            <i class="fa-regular fa-square-check"
                                                 style="color: #45a049; font-size: 25px; cursor: pointer"></i>
                        </div>
                        <div class="input-wrapper">
                            <div class="icon"><i class="fa-solid fa-key"></i></div>
                            <button class="btn">비밀번호 재설정</button>
                        </div>
                    </div>
                    <div class="image-wrapper">
                        <input type="file" name="profileImage" id="profileImage" accept="image/*"
                               style="display: none;">
                        <a href="#" id="avatar" class="before">
                            <i class="fa-solid fa-pen-to-square"></i>
                            <img
                                    src="${storeInfo.storeImg ? storeInfo.storeImg : '/assets/img/western.jpg'}"
                                    alt="Customer profile image">
                        </a>
                        <button id="profile_btn" class="btn" type="submit" value="프로필 변경"
                                style="display: none;">이미지 변경
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

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
    const avatar = document.getElementById('avatar');
    const storeImg = document.getElementById('profileImage');
    const $ImgBtn = document.getElementById('profile_btn');

    avatar.addEventListener('click', () => {
        storeImg.click();
    });
    profileImage.addEventListener('change', () => {
        console.log(storeImg.files[0]);
        avatar.querySelector('img').src = URL.createObjectURL(storeImg.files[0]);
        $ImgBtn.style.display = 'block';
        avatar.classList.remove('before');
    });

    $ImgBtn.addEventListener('click', () => {
        requestProfileImg();
    });

    const requestProfileImg = async () => {
        const formData = new FormData();
        formData.append('storeImg', storeImg.files[0]);
        //   비동기 요청
        const response = await fetch('/store/mypage-edit', {
            method: 'POST',
            body: formData
        });
        const result = await response.json();
        console.log(result);
    };

    const storeInfo = {
        price: "${storeInfo.price}" // 예시 값을 설정합니다.
    };

    const priceSelect = document.getElementById('price');

    // storeInfo.price 값이 유효한 옵션인지 확인합니다.
    const validPrices = ["3900", "5900", "7900", "8900"];
    if (validPrices.includes(storeInfo.price)) {
        priceSelect.value = storeInfo.price;
    } else {
        // 유효한 값이 아닌 경우 기본값을 설정합니다 (예: 첫 번째 옵션).
        priceSelect.value = validPrices[0];
    }
</script>
</body>
</html>
