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
    <script defer src="/assets/js/store/store-mypage-edit.js"></script>

    <style>

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
                                <input id="pickup-start-time" type="time" value="${storeInfo.openAt}"/>
                            </label>
                                <i class="time-set fa-regular fa-square-check"
                                   style="color: #45a049; font-size: 25px; cursor: pointer"></i>
                            </div>
                        </div>
                        <div class="input-wrapper">
                            <div class="icon"><i class="far fa-clock"></i></div>
                            <div>픽업 마감 시간
                                <label>
                                    <input id="pickup-end-time" type="time" value="${storeInfo.closedAt}"/>
                                </label>
                                <i class="time-set fa-regular fa-square-check"
                                   style="color: #45a049; font-size: 25px; cursor: pointer"></i>
                                </div>
                        </div>
                        <div id="error-message" style="color: red; display: none;">픽업 시작 시간은 픽업 마감 시간보다 늦을 수 없습니다.</div>
                        <div class="input-wrapper">
                            <div class="icon"><i class="fa-solid fa-user"></i></div>
                            <div>기본 수량 값
                                <label>
                                    <input id="product-cnt-input" type="number" value="${storeInfo.productCnt}" min="1"/>
                                </label>
                                <i class="product-cnt fa-regular fa-square-check"
                                   style="color: #45a049; font-size: 25px; cursor: pointer"></i>
                            </div>
                        </div>
                        <div id="product-cnt-error-message" style="display: none; color: red;"></div>

                        <div class="input-wrapper">
                            <i class="fas fa-phone-alt"></i>
                            <div>가게 전화번호
                                <label>
                                    <input id="business-number-input" value="${storeInfo.businessNumber}" min="1"/>
                                </label>
                                <i class="business-num fa-regular fa-square-check"
                                   style="color: #45a049; font-size: 25px; cursor: pointer"></i>
                            </div>
                        </div>
                        <div id="business-num-error-message" style="display: none; color: red;"></div>


                        <div class="input-wrapper">
                            <i class="fas fa-dollar-sign"></i>
                            <select id="price">
                                <option value="3900">3900</option>
                                <option value="5900">5900</option>
                                <option value="7900">7900</option>
                            </select>
                            <i class="price-update fa-regular fa-square-check"
                                                 style="color: #45a049; font-size: 25px; cursor: pointer"></i>
                        </div>
                        <div class="input-wrapper">
                            <div class="icon"><i class="fa-solid fa-key"></i></div>
                            <button class="btn" id="reset-pw-btn">비밀번호 재설정</button>
                        </div>
                    </div>
                    <div class="image-wrapper">
                        <input type="file" name="profileImage" id="profileImage" accept="image/*"
                               style="display: none;">
                        <a href="#" id="avatar" class="before">
                            <i class="fa-solid fa-pen-to-square"></i>
                            <img
                                    src="${storeInfo.storeImg != null ? storeInfo.storeImg : '/assets/img/western.jpg'}"
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
        const response = await fetch('/store/mypage/edit/update/img', {
            method: 'POST',
            body: formData
        });
        const result = await response.json();
        alert("가게 이미지가 성공적으로 업데이트 되었습니다.");
        $ImgBtn.style.display = 'none'; // 이미지 업데이트 후 버튼 숨김
        console.log(result);
    };

    const storeInfo = {
        price: "${storeInfo.price}" // 예시 값을 설정합니다.
    };

    const priceSelect = document.getElementById('price');

    // storeInfo.price 값이 유효한 옵션인지 확인합니다.
    const validPrices = ["3900", "5900", "7900"];
    if (validPrices.includes(storeInfo.price)) {
        priceSelect.value = storeInfo.price;
    } else {
        // 유효한 값이 아닌 경우 기본값을 설정합니다 (예: 첫 번째 옵션).
        priceSelect.value = validPrices[0];
    }
</script>
</body>
</html>
