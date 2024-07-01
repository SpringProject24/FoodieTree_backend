<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>FoodieTree</title>
        <link rel="stylesheet" href="/assets/css/common.css">
        <link rel="stylesheet" href="/assets/css/sign-in.css">
</head>
<body>
<header>
    <div class="container">
        <div class="logo">FoodieTree ${login.customerId}</div>
        <div class="logo-img">
            <img src="/assets/img/img_2.png" alt="">
        </div>
        <div>
            <c:if test="${login == null}">
                <li><a href="/customer/sign-up">Sign Up</a></li>
                <li><a href="/customer/sign-in">Sign In</a></li>
              </c:if>
          
              <c:if test="${login != null}">
                <li><a href="#">My Page</a></li>
                <li><a href="#">Sign Out</a></li>
              </c:if>
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
                        <input type="text" name="customerId" placeholder="아이디">
                        <input type="password" name="customerPassword" placeholder="비밀번호">
                    </div>
                    <button type="submit" id="sign-in-btn">로그인</button>
                </div>
            </form>
        </div>
    </div>
    <div class="sub-wrapper">
        <a id="find-pw-a" href="#">비밀번호 찾기</a>
        <a id="sign-up-a" href="#">회원가입</a>
    </div>
</section>
<script>
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

    document.querySelector('#find-pw-a').addEventListener('click', (e) => {
        e.preventDefault();
        if ($btnWrapper.querySelector('.checked').id === 'store-btn') {
            location.href = '/store/find-pw';
        } else if ($btnWrapper.querySelector('.checked').id === 'customer-btn') {
            location.href = '/find-pw';
        }
    });

    document.querySelector('#sign-up-a').addEventListener('click', (e) => {
        e.preventDefault();
        if ($btnWrapper.querySelector('.checked').id === 'store-btn') {
            location.href = '/store/sign-up';
        } else if ($btnWrapper.querySelector('.checked').id === 'customer-btn') {
            location.href = '/sign-up';
        }
    });

</script>
</body>
</html>