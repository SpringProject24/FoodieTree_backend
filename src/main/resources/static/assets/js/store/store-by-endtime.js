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

function formatRemainingTime(seconds) {
    const hours = Math.floor(seconds / 3600);
    const minutes = Math.floor((seconds % 3600) / 60);
    const secs = seconds % 60;
    return `${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}:${String(secs).padStart(2, '0')}`;
}

function renderStoresByEndTime(storeList) {
    const endTimeSoonContainer = document.querySelector('.end-time-soon-section');
    endTimeSoonContainer.innerHTML = '';

    if (storeList.length === 0) {
        endTimeSoonContainer.innerHTML = '<p>No stores available.</p>';
        return;
    }

    storeList.forEach(store => {
        const storeItem = document.createElement('div');
        storeItem.className = `storeItem ${store.productCnt == 0 ? 'low-stock' : ''}`;

        const imgUrl = store.storeImg && store.storeImg.startsWith('http')
            ? store.storeImg
            : store.storeImg ? `${window.location.origin}${store.storeImg}` : '/assets/img/defaultImage.jpg';

        const remainingSeconds = parseInt(store.remainingTime.split(':')[1], 10); // extract seconds
        const formattedTime = formatRemainingTime(remainingSeconds);

        storeItem.innerHTML = `
            <div class="category">${store.category}</div>
            <img src="${imgUrl}" alt="${store.storeName}" onerror="this.onerror=null; this.src='/assets/img/defaultImage.jpg';">
            <p class="storeName">${store.storeName}</p>
            <span class="storePrice">가격: ${store.price}</span>
            <span class="productCnt">수량: ${store.productCnt}</span>
            <span class="remainingTime">남은 시간: ${formattedTime}</span>
            ${store.productCnt == 0 ? '<div class="overlay">SOLD OUT</div>' : ''}
        `;

        endTimeSoonContainer.appendChild(storeItem);
    });
}