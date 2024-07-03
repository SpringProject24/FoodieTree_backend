<%@ page contentType="text/html; charset=UTF-8" language="java" %> <%@ taglib
prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>FoodieTree for 가게</title>
    <link rel="stylesheet" href="/assets/css/common.css" />
    <link rel="stylesheet" href="/assets/css/productApproval-form.css" />
    <script src="/js/productApproval-form.js"></script>
  </head>
  <body>
    <header>
      <div class="container">
        <h1>FoodieTree <span>for 상품</span></h1>
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
            <div class="image-upload">
              <input type="file" id="proImage" name="proImage" accept="image/*" multiple required>
              <label for="proImage">상품 사진 업로드 (Drag & Drop)</label>
              <div id="image-preview"></div>
             </div>
          </div>
          <div class="form-group">
            <label for="productCnt">상품 수량 :</label>
            <input
              type="text"
              id="productCnt"
              name="productCnt"
              placeholder="수량은 필수 입력 값입니다."
              required
            />
          </div>
          <div class="form-group">
            <label for="price">상품 가격 :</label>
            <input
              type="text"
              id="price"
              name="price"
              placeholder="가게 주소는 필수 입력 값입니다."
              required
            />
          </div>

          <button class="btn-approval" type="submit">상품 등록하기</button>
        </form>
      </section>
    </main>


    <footer>
      <div class="container">
        <p>&copy; 2024 FoodieTree. All rights reserved.</p>
      </div>
    </footer>
    
  </body>
</html>
