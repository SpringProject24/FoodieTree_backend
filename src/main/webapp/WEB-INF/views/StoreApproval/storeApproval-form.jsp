<%@ page contentType="text/html; charset=UTF-8" language="java" %> <%@ taglib
prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE html>
<html lang="ko">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>FoodieTree for 가게</title>
    <link rel="stylesheet" href="/assets/css/common.css" />
    <link rel="stylesheet" href="/assets/css/storeApproval-form.css" />
    <script src="/js/storeApproval-form.js"></script>
  </head>
  <body>
    <header>
      <div class="container">
        <h1>FoodieTree <span>for 가게</span></h1>
        <div class="logo-img">
          <img src="/assets/img/img_2.png" alt="" />
        </div>
        <div class="wrapper">
          <div class="signin"><a href="/sign-in">로그인</a></div>
          <div class="signup"><a href="/customer/sign-up">회원가입</a></div>
        </div>
      </div>
    </header>

    <main>
      <section class="registration">

        <h2>푸디트리와 지구를 위한 한걸음 함께 해보아요!</h2>

        <form
          action="/storeMyPage/storeApproval-result"
          method="post"
          enctype="multipart/form-data"
        >
          <div class="form-group">
            <label for="storeLicenseNumber">사업자 번호 :</label>
            <input
              type="text"
              id="storeLicenseNumber"
              name="storeLicenseNumber"
              placeholder="사업자 번호를 필수 입력 값입니다."
              required
            />
          </div>
          <div class="form-group">
            <label for="businessName">상호명 :</label>
            <input
              type="text"
              id="storeName"
              name="storeName"
              placeholder="상호명은 필수 입력 값입니다."
              required
            />
          </div>
          <div class="form-group">
            <label for="address">가게 주소 :</label>
            <input
              type="text"
              id="address"
              name="address"
              placeholder="가게 주소는 필수 입력 값입니다."
              required
            />
          </div>
          <div class="form-group">
            <label for="businessNumber">가게 번호 :</label>
            <input
              type="text"
              id="businessNumber"
              name="businessNumber"
              placeholder="사업자 번호를 필수 입력 값입니다."
              required
            />
          </div>
          <div class="form-group">
            <label for="category">업종 :</label>
            <select id="category" name="category" required>
              <option value="">업종을 선택하세요</option>
              <option value="KOREAN">한식</option>
              <option value="WESTERN">양식</option>
              <option value="CHINESE">중식</option>
              <option value="JAPANESE">일식</option>
              <option value="CAFE">카페</option>
              <option value="DESSERT">디저트</option>
              <option value="ELSE">기타</option>
            </select>
          </div>

          <button class="btn-approval" type="submit">가게 등록하기</button>
        </form>
      </section>
    </main>

    <!-- 관련 이미지 자리는 태그 div -> img로 바꿔서 첨부할 것 -->
    <div class="info-container">
      <div class="related-images">
        <div class="related-image1">관련 이미지</div>
        <div class="related-image2">관련 이미지</div>
      </div>
      <div class="related-info">
        <h2>관련 설명</h2>
        <p>ex) 우리 foodieTree는 지구를 지키기 위한 작은 움직임으로~</p>
      </div>
    </div>

    <div class="slide-container">
        <button class="slide-button left" onclick="moveSlide(-1)">&#10094;</button>
        <div class="slides">
            <div class="slide">
                <div class="slide-image">관련 이미지</div>
                <h3>이점 1</h3>
                <p>간단 설명</p>
            </div>
            <div class="slide">
                <div class="slide-image">관련 이미지</div>
                <h3>이점 1</h3>
                <p>간단 설명</p>
            </div>
            <div class="slide">
                <div class="slide-image">관련 이미지</div>
                <h3>이점 1</h3>
                <p>간단 설명</p>
            </div>
        </div>
        <button class="slide-button right" onclick="moveSlide(1)">&#10095;</button>
    </div>

    <footer>
      <div class="container">
        <p>&copy; 2024 FoodieTree. All rights reserved.</p>
      </div>
    </footer>
    
  </body>
</html>
