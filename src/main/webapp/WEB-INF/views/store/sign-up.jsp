<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>FoodieTree</title>
    <link rel="stylesheet" href="/assets/css/common.css">
    <link rel="stylesheet" href="/assets/css/store/sign-up.css">
    <script defer src="/assets/js/utils/lodash.js"></script>
    <script defer type="module" src="/assets/js/store/sign-up-event.js"></script>
</head>
<body>
<%@ include file="../include/spinner.jsp" %>
<header>
    <div class="container">
        <div class="logo margarine-regula">FoodieTree</div>
        <div class="logo-img">
            <img src="/assets/img/img_2.png" alt="Logo">
        </div>
    </div>
</header>
<section class="input-area">
    <form action="/store/sign-up" method="post">
        <div class="container">
            <div class="id-wrapper">
                <h2>회원 등록을 위한 이메일을 입력해주세요!</h2>
                <input type="text" id="input-id" name="account" placeholder="이메일을 입력해주세요">
                <button id="id-get-code-btn" class="disable" disabled>인증코드 받기</button>
                <div class="id-verify-wrapper" style="display: none">
                    <h2>해당 이메일로 인증코드가 전송되었습니다</h2>
                    <h3>인증코드를 입력해주세요!</h3>
                    <input type="text" id="id-verify-code" placeholder="인증코드를 입력해주세요">
                    <span id="countdown"></span>
                    <button id="id-verify-btn">이메일 인증번호 확인</button>
                    <button id="id-btn" style="display: none">계속</button>
                </div>
            </div>
            <div class="pass-wrapper none">
                <div class="pass">
                    <h2>계속해서 비밀번호를 입력해주세요!</h2>
                    <input id="input-pw" type="password" name="password" placeholder="비밀번호를 입력해주세요">
                </div>
                <div class="pass-check">
                    <input id="input-pw-chk" type="password" name="password-chk" placeholder="비밀번호를 확인해주세요">
                    <div class="wrapper">
                        <button id="prev-btn">이전</button>
                        <button id="pass-btn" class="disable" disabled>계속</button>
                    </div>
                </div>
            </div>
        </div>
    </form>
</section>
  <!-- 공통푸터 -->
  <%@ include file="../include/footer.jsp" %>
<script>
    const params = new URLSearchParams(window.location.search);
    const message = params.get('message');

    if (message === 'signup-fail') {
        alert('잠시 후 다시 시도해주세요');
    }
    const newUrl = window.location.origin + window.location.pathname;
    history.replaceState(null, null, newUrl);
</script>

  <!-- 공통푸터 -->
  <%@ include file="../include/footer.jsp" %>
</body>
</html>
