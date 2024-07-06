<%@ page contentType="text/html; charset=UTF-8" language="java" %> <%@ taglib
prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>상품 목록</title>
</head>
<body>

    <h1>상품 등록 내역입니다.</h1>

    <ul>
        <li># 상품 이미지: ${productDto.proImage}</li>
        <li># 설정 수량: ${productDto.productCnt}원</li>
        <li># 설정 가격: ${productDto.price}원</li>
    </ul>


      <!-- 공통푸터 -->
  <%@ include file="include/footer.jsp" %> 
</body>
</html>
