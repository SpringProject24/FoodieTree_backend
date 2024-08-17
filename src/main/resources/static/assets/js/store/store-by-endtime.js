window.onload = function() {
    fetch('/storeLists/by-product-end-time')
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            console.log('End time data:', data); // 데이터 확인용
            renderStoresByEndTime(data);
        })
        .catch(error => console.error('Error:', error));
};

function renderStoresByEndTime(storeList) {
    const endTimeSoonContainer = document.querySelector('.end-time-soon-section');
    endTimeSoonContainer.innerHTML = '';

    if (storeList.length === 0) {
        endTimeSoonContainer.innerHTML = '<p>이런 ! 현재 픽업 가능한 가게가 없어요.</p>';
        return;
    }

    storeList.forEach(store => {
        const storeItem = document.createElement('div');
        storeItem.className = `storeItem ${store.productCnt == 0 ? 'low-stock' : ''}`;

        const imgUrl = store.storeImg && store.storeImg.startsWith('http')
            ? store.storeImg
            : store.storeImg ? `${window.location.origin}${store.storeImg}` : '/assets/img/defaultImage.jpg';

        // HH:mm 형식으로 시간 추출
        const [hours, minutes] = store.remainingTime.split(':').slice(0, 2);
        const formattedTime = `${hours}시간 ${minutes}분 남았어요!`;

        storeItem.innerHTML = `
<!--            <div class="category">${store.category}</div>-->
            <img src="${imgUrl}" alt="${store.storeName}" onerror="this.onerror=null; this.src='/assets/img/defaultImage.jpg';">
            <p class="storeName">${store.storeName}</p>
            <span class="storePrice">${store.price}원</span>
            <span class="productCnt">${store.productCnt}개 남았어요!</span>
            <span class="remainingTime">${formattedTime}</span>
            <span class="reputation"> ✰ 4.5 </span>
            <span class="store-area">(${store.address})</span>
            ${store.productCnt == 0 ? '<div class="overlay">SOLD OUT</div>' : ''}
        `;

        endTimeSoonContainer.appendChild(storeItem);
    });
}