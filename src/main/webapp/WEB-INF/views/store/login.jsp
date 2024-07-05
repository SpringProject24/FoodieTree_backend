<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>Insert title here</title>
  </head>
  <body>
    <form action="/store/sign-in" name="sign-in" method="post" id="signInForm">
      <table class="table">
          <tr>
              <td class="text-left">
                  <p><strong>아이디를 입력해주세요.</strong>&nbsp;&nbsp;&nbsp;<span id="idCheck"></span></p>
              </td>
          </tr>
          <tr>
              <td>
                  <input type="text" name="account" id="signInId"
                         class="form-control tooltipstered" 
                         required="required" 
                         placeholder="최대 10자">
              </td>
          </tr>
          <tr>
              <td class="text-left">
                  <p><strong>비밀번호를 입력해주세요.</strong>&nbsp;&nbsp;&nbsp;<span id="pwCheck"></span></p>
              </td>
          </tr>
          <tr>
              <td>
                  <input type="password" size="17" maxlength="20" id="signInPw"
                         name="password" class="form-control tooltipstered"
                         required="required"
                         placeholder="최소 8자">
              </td>
          </tr>

          <!-- 자동 로그인 체크박스 -->
          <tr>
              <td>
                  <label class="auto-label" for="auto-login">
                      <span><i class="fas fa-sign-in-alt"></i>자동 로그인</span>
                      <input type="checkbox" id="auto-login" name="autoLogin">
                  </label>
              </td>
          </tr>

          <tr>
              <td class="text-center">
                  <p><strong>로그인하셔서 더 많은 서비스를 이용해보세요!</strong></p>
              </td>
          </tr>
          <tr>
              <td class="text-center" colspan="2">
                  <input type="submit" value="로그인" class="btn form-control tooltipstered" id="signIn-btn">
              </td>
          </tr>
          <tr>
              <td class="text-center" colspan="2">
                  <a id="sign-up-btn" class="btn form-control tooltipstered" href="/store/sing-up">
                      회원가입
                  </a>
              </td>
          </tr>
          <tr>
              <td class="text-center" colspan="2">
                  <a id="custom-login-btn" href="#">
                      <img src="//mud-kage.kakao.com/14/dn/btqbjxsO6vP/KPiGpdnsubSq3a0PHEGUK1/o.jpg"
                           width="300"/>
                  </a>
              </td>
          </tr>
      </table>
  </form>

    <!-- 공통푸터 -->
    <%@ include file="include/footer.jsp" %> 
  </body>
</html>
