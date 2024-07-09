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
    <script src="/js/storeApproval.js"></script>
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
          action="/storeMyPage/productApproval-form"
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
        <img src="https://images.unsplash.com/photo-1460819739742-50e4486578f5?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"class="related-image1">
        <img src="https://images.unsplash.com/photo-1495147466023-ac5c588e2e94?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D" class="related-image2">
      </div>
      <div class="related-info">
        <h2>관련 설명</h2>
        <p>우리의 FoodieTree의 시스템을 통해 음식물 쓰레기를 줄이고, 자원을 보다 효율적으로 사용하며, 환경 보호에 기여하고자 합니다.</p>
        <p>유통기한이 가까워지면 아직 먹을 수 있는 많은 음식들이 불필요하게 버려지는 현실을 바꾸기 위해, FoodieTree는 이러한 음식을 저렴한 가격에 제공하여 소비자들에게 경제적인 혜택을 주고 음식점에는 비용 절감의 기회를 제공합니다.</p>
        <p>FoodieTree의 목표는 환경을 보호하고 음식물 낭비를 줄이며 지속 가능한 식문화를 조성하는 것입니다. 고객 여러분도 FoodieTree와 함께 환경 보호에 동참해 주세요.</p>
      </div>
    </div>
    
    <div class="slide-container">
        <div class="slides">
            <div class="slide">
                <img src="https://images.unsplash.com/photo-1529003600303-bd51f39627fb?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D" class="slide-image">
                <h3>폐기물 감소</h3>
                <p>정확한 양의 음식을 제공하여 음식물 낭비를 최소화</p>
            </div>
            <div class="slide">
                <img src="https://images.unsplash.com/photo-1512485800893-b08ec1ea59b1?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D" class="slide-image">
                <h3>긍정적 이미지</h3>
                <p>환경을 생각하는 가게로서 긍정적인 이미지를 구축하여 고객의 신뢰 향상.</p>
            </div>
            <div class="slide">
                <img src="https://images.unsplash.com/photo-1519248200454-8f2590ed22b7?q=80&w=2075&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D" class="slide-image">
                <h3>잠재고객 유입</h3>
                <p>시스템을 통해 부담없이 호기심으로 가게를 이용할 수 있어 잠재고객 유입 증가</p>
            </div>
        </div>
    </div>

    <footer>
      <div class="container">
        <p>&copy; 2024 FoodieTree. All rights reserved.</p>
      </div>
    </footer>
    
  </body>
</html>
