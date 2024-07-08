<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8" %> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>FoodieTree</title>
    <!-- <link rel="stylesheet" href="/assets/css/common.css"> -->
    <link rel="stylesheet" href="/assets/css/common.css" />
    <link rel="stylesheet" href="/assets/css/product/productApproval-form.css" />
    <!-- 구글폰트 -->
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link
      href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100..900&display=swap"
      rel="stylesheet"
    />
    <!-- 구글폰트2 -->
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link
      href="https://fonts.googleapis.com/css2?family=Gaegu&display=swap"
      rel="stylesheet"
    />
    <!-- 아이콘 -->
    <!-- Add the following <link> to the <head> of your HTML. -->
    <!-- <link rel="stylesheet" href="https://cdn.linearicons.com/free/1.0.0/icon-font.min.css"> -->
  </head>
  <body>
    <!-- 공통헤더 -->
    <%@ include file="../include/header.jsp" %>

    <main>
      <section class="registration">

        <h2>푸디트리와 지구를 위한 한걸음 함께 해보아요!</h2>

        <form
          action="/store/product"
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
            <select id="price" name="price" required>
              <option value="">상품 가격을 선택하세요</option>
              <option value="3900">3900</option>
              <option value="5900">5900</option>
              <option value="7900">7900</option>
            </select>
          </div>


                    

          <button class="btn-approval" type="submit">상품 등록하기</button>
        </form>
      </section>
    </main>
    <!-- 공통 푸터 -->
    <%@ include file="../include/footer.jsp" %>

    <script>
      const $productApproval = document.querySelector(".btn-approval");
      $productApproval.addEventListener("click", () => {
        location.href = "/store/mypage/main";
      });

    document.addEventListener("DOMContentLoaded", function() {
    const imageInput = document.getElementById("proImage");
    const imagePreview = document.getElementById("image-preview");
    const imageUploadLabel = document.querySelector('.image-upload label');
    const imageUpload = document.querySelector('.image-upload');
    
  
    imageInput.addEventListener("change", handleFiles);
    
  
    imageUpload.addEventListener("dragover", function(event) {
        event.preventDefault();
        imageUpload.classList.add("dragover");
    });
  
    imageUpload.addEventListener("dragleave", function() {
        imageUpload.classList.remove("dragover");
    });
  
    imageUpload.addEventListener("drop", function(event) {
        event.preventDefault();
        imageUpload.classList.remove("dragover");
        const files = event.dataTransfer.files;
        handleFiles({ target: { files: files } });
    });
  
    function handleFiles(event) {
        const files = event.target.files;
        imagePreview.innerHTML = "";
        
        for (const file of files) {
            const reader = new FileReader();
            
            reader.onload = function(e) {
                const imgElement = document.createElement("img");
                imgElement.src = e.target.result;
                imagePreview.appendChild(imgElement);
            };
  
            reader.readAsDataURL(file);
        }
  
        if (files.length > 0) {
            imageUploadLabel.style.display = 'none';
        } else {
            imageUploadLabel.style.display = 'flex';
        }
    }
  });
  
    </script>
    
  </body>
</html>
