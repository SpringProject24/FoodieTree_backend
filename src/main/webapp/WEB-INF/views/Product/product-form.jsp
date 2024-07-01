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
        action="/store/product"
        method="post"
        enctype="multipart/form-data"
      >

        <label for="proImage">상품 사진:</label>
        <input type="file" id="proImage" name="proImage" multiple required />

        <label for="productCnt">수량:</label>
        <input type="text" id="productCnt" name="productCnt" required />

        <label for="price">가격:</label>
        <input type="text" id="price" name="price" required />

        <input type="submit" value="상품 등록" />
      </form>
    </div>

    
  </body>
</html>
