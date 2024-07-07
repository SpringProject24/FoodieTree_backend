<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>FoodieTree</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css"
          rel="stylesheet"
          integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
          crossorigin="anonymous">
    <link rel="stylesheet" href="/assets/css/common.css">
    <link rel="stylesheet" href="/assets/css/sign-in.css">
    <script defer src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM"
            crossorigin="anonymous"></script>
    <script defer type="module" src="/assets/js/customer/sign-in-event.js"></script>
    <style>
        .hidden {
            display: none;
        }

        .input-pass-wrapper input {
            display: block;
        }
    </style>
</head>
<body>

<!-- Modal -->
<div class="modal fade" id="exampleModal" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="exampleModalLabel"
     aria-hidden="true">
    <div class="modal-dialog modal-dialog-scrollable">
        <div class="modal-content">
            <div class="modal-header">
                Forget PassWord?
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div class="input-id-wrapper">
                    <p>아이디를 입력하세요.</p>
                    <input type="text" id="input-id" placeholder="아이디를 입력하세요(ex foodietree@gmail.com)">
                    <button id="check-id-btn">확인</button>
                    <div class="verify-wrapper hidden">
                        <div id="step-email">
                            <p>인증번호를 받으세요.</p>
                            <button id="send-verification-code-btn">인증번호 받기</button>
                        </div>
                        <div id="step-code" class="hidden">
                            <p>인증번호를 입력하세요.</p>
                            <input type="text" id="verification-code" maxlength="6">
                            <button id="verification-code-btn">인증하기</button>
                            <div id="verification-result"></div>
                        </div>
                        <div id="countdown"></div>
                    </div>
                </div>
                <div class="input-pass-wrapper hidden">
                    <p>새로운 비밀번호를 입력하세요.</p>
                    <input type="password" id="input-pw" placeholder="비밀번호를 입력하세요">
                    <input type="password" id="input-pw-chk" placeholder="비밀번호를 확인하세요" disabled>
                    <button id="update-new-pw-btn" disabled>변경하기</button>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>

<header>
    <div class="container">
        <div class="logo">FoodieTree ${login.customerId}</div>
        <div class="logo-img">
            <img src="/assets/img/img_2.png" alt="">
        </div>
    </div>
</header>
<section class="input-area">
    <div class="btn-wrapper">
        <button class="btn checked" id="customer-btn">사용자 회원</button>
        <button class="btn" id="store-btn">가게 회원</button>
    </div>
    <div class="form-wrapper">
        <div class="sign-in">
            <form action="#" method="post">
                <div class="container">
                    <div class="input-wrapper">
                        <input type="text" name="customerId" placeholder="아이디" required>
                        <input type="password" name="customerPassword" placeholder="비밀번호" required>
                    </div>
                    <tr>
                        <td>
                            <label class="auto-label" for="auto-login">
                                <span><i class="fas fa-sign-in-alt"></i>자동 로그인</span>
                                <input type="checkbox" id="auto-login" name="autoLogin">
                            </label>
                        </td>
                    </tr>
                    <button type="submit" id="sign-in-btn">로그인</button>
                </div>
            </form>
        </div>
    </div>
    <div class="sub-wrapper">
        <a id="find-pw-a" href="#" data-bs-toggle="modal" data-bs-target="#exampleModal">Forget PassWord?</a>
        <a id="sign-up-a" href="#">Sign Up</a>
    </div>
</section>
<script>
    const params = new URLSearchParams(window.location.search);
    const message = params.get('message');

    if (message === 'signin-fail') {
        alert('아이디나 비밀번호를 확인해주세요!');
    } else if (message === "signin-required") {
      alert("로그인이 필요한 서비스입니다.");
    }
    const newUrl = window.location.origin + window.location.pathname;

    // history.replaceState(state, title, url) : 브라우저 조작
    history.replaceState(null, null, newUrl);

    const $formSignIn = document.querySelector('form[method=post]');
    const $btnWrapper = document.querySelector('.btn-wrapper');
    const $signInBtn = document.querySelector('#sign-in-btn');


    $btnWrapper.addEventListener('click', (e) => {
        if (e.target.tagName !== 'BUTTON') return;
        [...$btnWrapper.children].forEach($btn => $btn.classList.remove('checked'));
        e.target.classList.add('checked');
    });

    $signInBtn.addEventListener('click', async (e) => {
        e.preventDefault();
        if ($btnWrapper.querySelector('.checked').id === 'store-btn') {
            $formSignIn.action = '/store/sign-in';
        } else if ($btnWrapper.querySelector('.checked').id === 'customer-btn') {
            $formSignIn.action = '/customer/sign-in';
        }
        $formSignIn.submit();
    });

    document.querySelector('#sign-up-a').addEventListener('click', (e) => {
        e.preventDefault();
        if ($btnWrapper.querySelector('.checked').id === 'store-btn') {
            location.href = '/store/sign-up';
        } else if ($btnWrapper.querySelector('.checked').id === 'customer-btn') {
            location.href = '/customer/sign-up';
        }
    });

</script>
    <!-- 공통푸터 -->
    <%@ include file="include/footer.jsp" %> 
</body>
</html>