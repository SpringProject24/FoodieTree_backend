<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- JavaScript 파일 임포트 -->
<script defer type="module" src="/assets/js/store/store-by-endtime.js"></script>
<script defer type="module" src="/assets/js/store/store-by-product-cnt.js"></script>


<%--      co2 saver--%>
<div id="co2-saver-list">
    <img src="${pageContext.request.contextPath}/assets/img/main-quote/co2saversans.png" alt="Co2 Saver" id="co2-title-img">
    <span class="sub-title">Co2 감소에 가장 많이 기여한 가게 . </span>
    <div class="list">
        <div class="list-container co2-saver-section">
        </div>
    </div>
</div>
<%--   오늘 마감 임박 --%>
<div id="end-soon-list">
    <img src="${pageContext.request.contextPath}/assets/img/main-quote/endsoongreensans.png" alt="It Will End Soon" id="end-title-img">
    <span  class="sub-title"> 곧 마감되는 상품이에요 ! 서두르세요 !  </span>
    <div class="list">
        <div class="list-container end-time-soon-section">
        </div>.
    </div>
</div>







