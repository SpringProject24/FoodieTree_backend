<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="list">
    <div>
        <h2 class="title">Save it before it's too late</h2>
        <script>
            window.onload = function() {
                fetch('/storeLists/by-product-count')
                    .then(response => {
                        if (!response.ok) {
                            throw new Error('Network response was not ok');
                        }
                        return response.json();
                    })
                    .then(data => {
                        console.log(data);  // 데이터 확인용
                        renderStores(data);
                    })
                    .catch(error => console.error('Error:', error));
            };

            function renderStores(storeList) {
                const topSellingContainer = document.querySelector('.favorite-store-list');
                topSellingContainer.innerHTML = '';

                if (storeList.length === 0) {
                    topSellingContainer.innerHTML = '<p>No stores available.</p>';
                    return;
                }

                storeList.forEach(store => {
                    const storeItem = document.createElement('div');
                    storeItem.className = `storeItem ${store.productCnt == 0 ? 'low-stock' : ''}`;

                    // 이미지 URL이 상대 경로일 경우, 전체 URL로 변환
                    const imgUrl = store.storeImg.startsWith('http') ? store.storeImg : `${window.location.origin}${store.storeImg}`;

                    storeItem.innerHTML = `
                        <div class="category">\${store.category}</div>
                        <img src="\${imgUrl}" alt="\${store.storeName}" onerror="this.onerror=null; this.src='/assets/img/defaultImage.jpg';">
                        <p class="storeName">\${store.storeName}</p>
                        <span class="storePrice">가격: \${store.price}</span>
                        <span class="productCnt">수량: \${store.productCnt}</span>
                        \${store.productCnt == 0 ? '<div class="overlay">SOLD OUT</div>' : ''}
                    `;

                    topSellingContainer.appendChild(storeItem);
                });
            }
        </script>
    </div>

    <div>
        <div class="list">
            <h2 class="title">CO2 Saver</h2>
            <div class="favorite-store-list">
                <!-- 여기에 CO2 Saver 가게 정보가 동적으로 추가됩니다 -->
            </div>
        </div>

        <div class="list">
            <h2 class="title">It will end soon!</h2>
            <div class="favorite-store-list">
                <!-- 여기에 마감 임박 가게 정보가 동적으로 추가됩니다 -->
            </div>
        </div>
    </div>
</div>