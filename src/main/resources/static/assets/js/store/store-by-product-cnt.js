document.addEventListener('DOMContentLoaded', function() {
    fetch('/storeLists/by-product-count')
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            console.log('Product count data:', data); // 데이터 확인용
            renderStoresByProductCount(data);
        })
        .catch(error => console.error('Error:', error));
});

function renderStoresByProductCount(storeList) {
    const co2SaverContainer = document.querySelector('.co2-saver-section');
    co2SaverContainer.innerHTML = '';

    if (storeList.length === 0) {
        co2SaverContainer.innerHTML = '<p>No stores available.</p>';
        return;
    }

    storeList.forEach(store => {
        const storeItem = document.createElement('div');
        storeItem.className = `storeItem ${store.productCnt == 0 ? 'low-stock' : ''}`;

        const imgUrl = store.storeImg && store.storeImg.startsWith('http')
            ? store.storeImg
            : store.storeImg ? `${window.location.origin}${store.storeImg}` : '/assets/img/defaultImage.jpg';

        storeItem.innerHTML = `
            <div class="category">${store.category}</div>
            <img src="${imgUrl}" alt="${store.storeName}" onerror="this.onerror=null; this.src='/assets/img/defaultImage.jpg';">
            <p class="storeName">${store.storeName}</p>
            <span class="storeCo2">🪴: ${store.coTwo}</span> 
            <span class="storePrice">가격: ${store.price}</span>
            <span class="productCnt">수량: ${store.productCnt}</span>
            ${store.productCnt == 0 ? '<div class="overlay">SOLD OUT</div>' : ''}
        `;

        co2SaverContainer.appendChild(storeItem);
    });
}