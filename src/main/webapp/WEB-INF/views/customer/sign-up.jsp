<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>FoodieTree</title>
    <link rel="stylesheet" href="/assets/css/common.css">
    <link rel="stylesheet" href="/assets/css/customer/customer-sign-up.css">
    <script defer src="/assets/js/utils/lodash.js"></script>
    <script defer type="module" src="/assets/js/customer/sign-up-event.js"></script>
</head>
<body>
<%@ include file="../include/spinner.jsp" %>
    <header>
        <div class="container">
            <div class="logo">FoodieTree</div>
            <div class="logo-img">
                <img src="/assets/img/img_2.png" alt="">
            </div>
        </div>
    </header>
<section class="input-area">
    <form action="/customer/sign-up" method="post">
        <div class="container">
            <div class="id-wrapper">
                <h2>회원 등록을 위한 이메일을 입력해주세요!</h2>
                <input type="text" id="input-id" name="customerId" placeholder="이메일을 입력해주세요">
                <button id="id-get-code-btn" class="disable" disabled>인증코드 받기</button>
                <div class="id-verify-wrapper" style="display: none">
                    <h2>해당 이메일로 인증코드가 전송되었습니다</h2>
                    <h3>인증코드를 입력해주세요!</h3>
                    <input type="text" id="id-verify-code" name="customerId" placeholder="인증코드를 입력해주세요">
                    <span id="countdown"></span>
                    <button id="id-verify-btn">이메일 인증번호 확인</button>
                    <button id="id-btn" style="display: none">계속</button>
                </div>
            </div>
            <div class="pass-wrapper none">
                <div class="pass">
                    <h2>계속해서 비밀번호를 입력해주세요!</h2>
                    <input type="password" id="input-pw" name="customerPassword" placeholder="비밀번호를 입력해주세요">
                </div>
                <div class="pass-check">
                    <input disabled type="password" id="input-pw-chk" name="customerPasswordChk" placeholder="비밀번호를 확인해주세요">
                    <div class="wrapper">
                        <button id="prev-btn">이전</button>
                        <button id="pass-btn" style="background-color: gray">계속</button>
                    </div>
                </div>
            </div>
            <div class="food-wrapper none">
                <h2>선호하는 음식을 선택해주세요!</h2>
                <p>(최대 3 종류)</p>
                <div class="foods">
                    <div class="food-item">
                        <input type="checkbox" name="food" value="한식" id="korean">
                        <label for="korean">
                            <span>한식</span>
                            <img src="/assets/img/korean.jpg" alt="">
                        </label>
                    </div>
                    <div class="food-item">
                        <input type="checkbox" name="food" value="중식" id="chinese">
                        <label for="chinese">
                            <span>중식</span>
                            <img src="/assets/img/chinese.jpg" alt="">
                        </label>
                    </div>
                    <div class="food-item">
                        <input type="checkbox" name="food" value="일식" id="japanese">
                        <label for="japanese">
                            <span>일식</span>
                            <img src="/assets/img/japanese.jpg" alt="">
                        </label>
                    </div>
                    <div class="food-item">
                        <input type="checkbox" name="food" value="양식" id="western">
                        <label for="western">
                            <span>양식</span>
                            <img src="/assets/img/western.jpg" alt="">
                        </label>
                    </div>
                    <div class="food-item">
                        <input type="checkbox" name="food" value="디저트" id="dessert">
                        <label for="dessert">
                            <span>디저트</span>
                            <img src="/assets/img/dessert.jpg" alt="">
                        </label>
                    </div>
                    <div class="food-item">
                        <input type="checkbox" name="food" value="카페" id="cafe">
                        <label for="cafe">
                            <span>카페</span>
                            <img src="/assets/img/cafe.jpg" alt="">
                        </label>
                    </div>
                    <div class="food-item">
                        <input type="checkbox" name="food" value="기타" id="etc">
                        <label for="etc">
                            <span>기타</span>
                            <img src="/assets/img/etc.jpg" alt="">
                        </label>
                    </div>
                </div>
                <div class="skip-or-next-wrapper">
                    <button id="btn-food">계속</button>
                    <button id="skip-btn-food">건너뛰기</button>
                </div>
            </div>
        </div>
        <div class="location-wrapper none">
            <h2>선호하는 지역을 선택해주세요!</h2>
            <p>(최대 3 곳)</p>
            <div class="locations">
                <div id="map"></div>
            </div>
            <div class="skip-or-next-wrapper">
                <button id="btn-location">계속</button>
                <button id="skip-btn-location">건너뛰기</button>
            </div>
        </div>
    </form>
</section>
<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=${kakaoApiKey}"></script>
<script>
  document.addEventListener('DOMContentLoaded', () => {
    const $btnFood = document.getElementById('btn-food');
    const $skipBtnFood = document.getElementById('skip-btn-food');

        const container = document.getElementById('map');
        const options = {
            center: new kakao.maps.LatLng(33.450701, 126.570667),
            level: 3
        };
        let map;

        $btnFood.addEventListener('click', (e) => {
            e.preventDefault();
            const $food = document.querySelectorAll('input[name="food"]:checked');
            if ($food.length === 0) {
                alert('선호하는 음식을 1개 이상 선택해주세요.');
                return;
            }
            if ($food.length > 3) {
                alert('선호하는 음식은 최대 3개까지 선택 가능합니다.');
                return;
            }
            document.querySelector('.food-wrapper').classList.add('none');
            document.querySelector('.location-wrapper').classList.remove('none');
            map = new kakao.maps.Map(container, options);
        });

        document.querySelector('.foods').addEventListener('click', (e) => {
            const $foodItem = e.target.closest('.food-item');
            if (e.target.checked) {
                $foodItem.classList.add('checked');
            } else {
                $foodItem.classList.remove('checked');
            }
        });

        $skipBtnFood.addEventListener('click', (e) => {
            // 체크된 음식을 해제하기
            const $food = document.querySelectorAll('input[name="food"]:checked');
            $food.forEach($f => $f.checked = false);
            map = new kakao.maps.Map(container, options);
        });
    });
</script>
<script>
    document.addEventListener('DOMContentLoaded', () => {
        const $btnLocation = document.getElementById('btn-location');
        const $skipBtnLocation = document.getElementById('skip-btn-location');

        $btnLocation.addEventListener('click', (e) => {
            e.preventDefault();
            const $checkedLocations = document.querySelectorAll('.location-item.checked');
            if ($checkedLocations.length === 0) {
                alert('선호하는 지역을 1개 이상 선택해주세요.');
                return;
            }
            if ($checkedLocations.length > 3) {
                alert('선호하는 지역은 최대 3개까지 선택 가능합니다.');
                return;
            }
            document.querySelector('form').submit();
        });

        $skipBtnLocation.addEventListener('click', (e) => {
            const $locations = document.querySelectorAll('.location-item');
            $locations.forEach($location => $location.classList.remove('checked'));
            document.querySelector('form').submit();
        });
    });
</script>
</body>
</html>
