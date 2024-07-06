document.getElementById('find-pw-a').addEventListener('click', async (e) => {
    e.preventDefault();
    const target = $btnWrapper.querySelector('.checked')
    if (target.id === 'store-btn') {
        // location.href = '/store/find-pw';
    } else if (target.id === 'customer-btn') {
        // location.href = '/customer/find-pw';
    }
});
