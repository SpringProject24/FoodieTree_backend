<%@ page contentType="text/html; charset=UTF-8" language="java" %> <%@ taglib
prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>업체 등록</title>
    <style>
      body {
        font-family: Arial, sans-serif;
        margin: 20px;
        padding: 0;
      }
      .container {
        max-width: 600px;
        margin: 0 auto;
        padding: 20px;
        border: 1px solid #ddd;
        border-radius: 5px;
        box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
      }
      h1 {
        text-align: center;
      }
      label {
        display: block;
        margin-bottom: 8px;
        font-weight: bold;
      }
      input[type="text"],
      input[type="tel"],
      select {
        width: 100%;
        padding: 8px;
        margin-bottom: 16px;
        border: 1px solid #ddd;
        border-radius: 4px;
      }
      input[type="file"] {
        margin-bottom: 16px;
      }
      input[type="submit"] {
        width: 100%;
        padding: 10px;
        background-color: #4caf50;
        color: white;
        border: none;
        border-radius: 4px;
        cursor: pointer;
      }
      input[type="submit"]:hover {
        background-color: #45a049;
      }
    </style>
  </head>
  <body>
    <div class="container">
      <h1>업체 등록</h1>
      <form
        action="/storeMyPage/storeApproval-result"
        method="post"
        enctype="multipart/form-data"
      >
      <!-- <label for="storeId">회원 아이디:</label>
      <input type="text" id="storeId" name="storeId" required /> -->

        <label for="businessName">상호명:</label>
        <input type="text" id="storeName" name="storeName" required />

        <label for="address">주소:</label>
        <input type="text" id="address" name="address" required />

        <label for="businessNumber">가게 전화번호:</label>
        <input type="tel" id="businessNumber" name="businessNumber" required />

        <label for="category">업종:</label>
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

        <label for="storeLicenseNumber">사업자등록번호:</label>
        <input
          type="text"
          id="storeLicenseNumber"
          name="storeLicenseNumber"
          required
        />

        <input type="submit" value="가게 등록" />
      </form>
    </div>

    <script>

        let timeout;

        

    </script>
    
  </body>
</html>
