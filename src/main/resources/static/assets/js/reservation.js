// =========== 전역 변수 ==============
const customerId = "test@gmail.com";
const BASE_URL = "http://localhost:8083";

const $reservationList = document.querySelector('.reservation-list');

// 모달 관련 요소
const $reservationModal = document.getElementById('reservation-modal');
const $modalDetails = document.getElementById('modal-details');

// 추가된 모달 요소
const $cancelModal = document.getElementById('cancel-modal');
const $cancellationModal = document.getElementById('cancellation-modal');
const $closeModalButtons = document.querySelectorAll('.close');

let currentPage = 1;
let isFetching = false; // 데이터 불러오는 중에는 더 가져오지 않도록 제어
let totalReservations = 0;
let loadedReservations = 0;

// =========== 함수 정의 ================
function appendReservations(reservations) {
    let tag = '';
    let statusInfo = '';

    if(reservations && reservations.length > 0){
        reservations.forEach(({reservationId , storeImg, storeName, status, pickupTime, pickedUpAt, cancelReservationAt, reservationTime}) => {

            let t1 = formatDate(pickupTime);
            let t2 = formatDate(pickedUpAt);
            let t3 = formatDate(cancelReservationAt);
            let t4 = formatDate(reservationTime);

            if (status === 'RESERVED') {
                statusInfo = `${t1}까지 픽업해주세요`;
            }
            if (status === 'PICKEDUP') {
                statusInfo = `${t2}에 픽업하셨습니다`;
            }
            if (status === 'CANCELED') {
                statusInfo = `${t3}에 취소하셨습니다`;
            }
            if (status === 'NOSHOW'){
                statusInfo = `${t1}에 미방문으로 취소되었습니다`;
            }
            tag += `
            <div class="reservation-item" data-reservation-id="${reservationId}">
                <img src="${storeImg}" alt="Store Image"/>
                <span>${storeName}</span>
                <span>${status}</span>
                <span>${statusInfo}</span>
                ${status === 'RESERVED' ? '<button class="reservation-cancel-btn">예약 취소</button>' : ''}
            </div>
            `;
        });
    } else {
        tag = `<div class='reservation-list'>예약 내역이 없습니다.</div>`;
    }

    $reservationList.innerHTML = tag;
    console.log('appendReservations() 실행');
}

async function fetchReservations() {
    if (isFetching) return;
    isFetching = true;

    try {
        const res = await fetch(`${BASE_URL}/reservation/${customerId}`);
        const reservations = await res.json();
        console.log(reservations);
        appendReservations(reservations);

    } catch (error) {
        console.error('Error fetching reservations:', error);
    } finally {
        isFetching = false;
    }
}

function setupInfiniteScroll() {
    if ((window.innerHeight + window.scrollY) >= document.body.offsetHeight - 50 && !isFetching) {
        fetchReservations();
    }
}

function removeInfiniteScroll() {
    $reservationList.removeEventListener('scroll', setupInfiniteScroll);
}

function formatDate(isoDate) {
    const date = new Date(isoDate);

    // 월 이름 매핑
    const months = ['JAN', 'FEB', 'MAR', 'APR', 'MAY', 'JUN', 'JUL', 'AUG', 'SEP', 'OCT', 'NOV', 'DEC'];
    const monthName = months[date.getMonth()];

    // 시간 및 분 추출
    const hours = date.getHours();
    const minutes = ('0' + date.getMinutes()).slice(-2); // 두 자리로 맞추기 위해

    // 결과 문자열 구성
    const formattedDate = `${monthName} ${date.getDate()} / ${hours}시 ${minutes}분`;

    return formattedDate;
}


// 서버에서 예약 취소 가능 여부 확인
const fetchIsCancelAllowed = async (reservationId) => {
    const res = await fetch(`${BASE_URL}/reservation/${reservationId}/check/cancel`);
    console.log(reservationId);
    if (res.status !== 200) {
        console.error('Error checking cancel allowed:', await res.text());
        return false;
    }
    return await res.json();
};

// 예약 취소
const fetchCancelReservation = async (reservationId) => {
    console.log(reservationId);
    const res = await fetch(`${BASE_URL}/reservation/${reservationId}/cancel`, {
        method: 'PATCH'
    });

    if (res.status !== 200) {
        alert('취소에 실패했습니다!');
        return;
    }

    $reservationList.scrollTo(0, 0); // 취소 후 페이지 상단으로 이동
    await fetchReservations();
};

// 예약 취소 처리 클릭 이벤트
function cancelReservationClickEvent(reservationId) {
    $reservationList.addEventListener('click',async e => {


        e.preventDefault();
        if (!e.target.matches('.reservation-cancel-btn')) return;

        const reservationId = e.target.closest('.reservation-item').dataset.reservationId;
        const isCancelAllowed = await fetchIsCancelAllowed(reservationId);

        const res = await fetch(`${BASE_URL}/reservation/${reservationId}/modal/detail`);
        const reservation = await res.json();
        let cancellationFee = reservation.price * 0.5;
        let tagWithFee = `<p>픽업시간 기준 1시간 이내로 예약 취소시 취소 수수료 50%가 부과됩니다. 정말 취소하시겠습니까?</p>
                <p>${cancellationFee} 취소수수료</p>
                <p>취소 수수료는 결제 금액에서 자동 차감됩니다.</p>`;

        let tagWithoutFee = `<p>정말 취소하시겠습니까?</p>
                <p>${reservation.storeName} 상품이 맞습니까?</p>
                <p>${reservation.price}는 자동 환불됩니다. </p>
                `;
        // 픽업시간이 1시간 이내라면 취소 수수료 모달 열기
        if (!isCancelAllowed) {
            document.getElementById('modal-cancel').innerHTML = tagWithFee + `<button id="confirm-cancel-btn">확인</button>`;
            openModalver2($cancelModal);

            document.getElementById('confirm-cancel-btn').addEventListener('click', () => {
                closeModal($cancelModal);
                fetchCancelReservation(reservationId);
            }, { once: true }); // 이벤트가 한 번만 발생하도록 설정
        } else {
            // 그 외에는 바로 취소
            document.getElementById('modal-cancel').innerHTML = tagWithoutFee + `<button id="confirm-cancel-btn">확인</button>`;
            openModalver2($cancelModal);

            document.getElementById('confirm-cancel-btn').addEventListener('click', () => {
                closeModal($cancelModal);
                fetchCancelReservation(reservationId);
            }, { once: true }); // 이벤트가 한 번만 발생하도록 설정
        }
    });
}


// 픽업 완료
const fetchConfirmPickUp = async (reservationId) => {
    console.log(reservationId);
    const res = await fetch(`${BASE_URL}/reservation/${reservationId}/pickup`, {
        method: 'PATCH'
    });

    if (res.status !== 200) {
        alert('픽업 완료에 실패했습니다!');
        return;
    }

    $reservationList.scrollTo(0, 0); // 픽업완료 후 페이지 상단으로 이동
    await fetchReservations();
};

// 픽업 완료 처리 클릭 이벤트
function pickUpClickEvent() {
    $reservationModal.addEventListener('click', e => {
        e.preventDefault();
        if (!e.target.matches('.picked-up-btn')) return;

        if (!confirm('가게에 도착해 물건을 픽업하셨나요? 픽업 완료 버튼 누를 시 환불 불가합니다')) return;

        const reservationId = e.target.closest('.reservation-detail-item').dataset.reservationId;

        fetchConfirmPickUp(reservationId);

        alert('픽업 완료되었습니다!');
        closeModal($reservationModal);
    });
}

function openModalver2(modal) {
    modal.style.display = "block";
}

// 모달 열기
async function openModal(reservationId) {

    const res = await fetch(`${BASE_URL}/reservation/${reservationId}/modal/detail`);
    const reservation = await res.json();
    console.log(reservation);


    // const reservationItem = document.querySelector(`.reservation-item[data-reservation-id="${reservationId}"]`);
    console.log(reservationId);
    if (!reservation) {
        console.error(`Reservation with ID ${reservation} not found.`);
        return;
    }

    // const storeImg = reservationItem.querySelector('img').src;
    // const storeName = reservationItem.querySelector('span:nth-child(2)').textContent;
    // const status = reservationItem.querySelector('span:nth-child(3)').textContent;
    // const statusInfo = reservationItem.querySelector('span:nth-child(4)').textContent;
    //
    // const reservationDetail = {
    //     storeImg: storeImg,
    //     storeName: storeName,
    //     status: status,
    //     statusInfo: statusInfo,
    //     // 여기에 필요한 시간 정보 등 추가
    // };

    // 모달에 데이터 추가
    $modalDetails.innerHTML = `
        <div class="reservation-detail-item" data-reservation-id="${reservationId}">
            <p>가게 이름: ${reservation.storeName}</p>
            <p>상태: ${reservation.status}</p>
            <p>${reservation.statusInfo}</p>
            <img src="${reservation.storeImg}" alt="Store Image"/>
            ${reservation.status === 'RESERVED' ? '<button class="picked-up-btn">픽업 확인 버튼</button>' : '<button style="background-color:gray;">수정 불가</button>'}
        </div>
    `;

    $reservationModal.style.display = "block";
}

// 모달 닫기 이벤트
function closeModal() {
    if (arguments.length === 0) {
        console.error('No modal element provided to closeModal function.');
        return;
    }

    for (let i = 0; i < arguments.length; i++) {
        const modalElement = arguments[i];
        if (modalElement) {
            modalElement.style.display = 'none';
        }
    }
}

// 모든 모달 닫기 버튼에 이벤트 추가
$closeModalButtons.forEach(button => {
    button.addEventListener('click', () => {
        const modal = button.closest('.modal');
        closeModal(modal);
    });
});

// 모달 외부 클릭 시 닫기
window.addEventListener('click', e => {
    if (e.target === $reservationModal) {
        closeModal($reservationModal);
    }
    if (e.target === $cancelModal) {
        closeModal($cancelModal);
    }
});

// 모달 열기 이벤트
$reservationList.addEventListener('click', e => {
    if (e.target.matches('.reservation-cancel-btn')) return; // 예약 취소 버튼일 경우 모달 열기 방지

    if (e.target.closest('.reservation-item')) {
        // openModal($reservationModal);
        openModal(e.target.closest('.reservation-item').dataset.reservationId);
    }
});

// =========== 실행 코드 ================
document.addEventListener('DOMContentLoaded', () => {
    fetchReservations(); // 초기 예약 데이터 로드
    window.addEventListener('scroll', setupInfiniteScroll); // 무한 스크롤 설정
    cancelReservationClickEvent(); // 예약 취소 이벤트 설정
    pickUpClickEvent(); // 픽업 완료 이벤트 설정
});