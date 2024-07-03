// ========= 전역 변수 =========
const BASE_URL = 'http://localhost:8083';

const calendarElement = document.getElementById('calendar');
const statusElement = document.getElementById('status');
const currentMonthElement = document.getElementById('current-month');
const prevMonthButton = document.getElementById('prev-month');
const nextMonthButton = document.getElementById('next-month');

const scheduleModal = document.getElementById('store-calendar-modal');
const $closeModalButtons = document.querySelectorAll('.close');
const modalDetailsElement = document.getElementById('modal-schedule-details');

let today = new Date();
let currentYear = today.getFullYear();
let currentMonth = today.getMonth();

// ========= 함수 =========

function updateCalendar(year, month) {
    calendarElement.innerHTML = '';

    // 요일 헤더 생성
    const daysOfWeek = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];
    const headerRow = document.createElement('div');
    headerRow.classList.add('calendar-header');
    daysOfWeek.forEach(day => {
        const dayElement = document.createElement('div');
        dayElement.textContent = day;
        dayElement.classList.add('calendar-day-header');
        headerRow.appendChild(dayElement);
    });
    calendarElement.appendChild(headerRow);

    const date = new Date(year, month);
    const firstDay = new Date(year, month, 1).getDay();
    const daysInMonth = new Date(year, month + 1, 0).getDate();
    currentMonthElement.textContent = date.toLocaleDateString('default', { year: 'numeric', month: 'long' });

    // 빈 칸 추가
    for (let i = 0; i < firstDay; i++) {
        const emptyCell = document.createElement('div');
        emptyCell.classList.add('calendar-day-empty');
        calendarElement.appendChild(emptyCell);
    }

    // 날짜 추가
    for (let i = 1; i <= daysInMonth; i++) {
        const dayElement = document.createElement('div');
        dayElement.textContent = i;
        dayElement.classList.add('calendar-day');
        if (year === today.getFullYear() && month === today.getMonth() && i === today.getDate()) {
            dayElement.classList.add('today');
        }
        dayElement.addEventListener('click', () => showModal(year, month, i));
        calendarElement.appendChild(dayElement);
    }
}

async function showModal(year, month, day) {
    const dateString = `${year}-${String(month + 1).padStart(2, '0')}-${String(day).padStart(2, '0')}`;
    let tag = ``;
    console.log(dateString);
    try {
        const response = await fetch(`${BASE_URL}/store/mypage/main/calendar/modal/${dateString}?storeId=aaa@aaa.com`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const res = await response.json();
        console.log("pickupTime, openAt, productCnt, canceledByStoreAt");
        console.log(res);

        tag = `<div>pickupTime: ${res.pickupTime || 'N/A'}</div>
               <div>openAt: ${res.openAt || 'N/A'}</div>
               <div>productCnt: ${res.productCnt || 'N/A'}</div>
               <div>canceledByStoreAt: ${res.canceledByStoreAt || 'N/A'}</div>`;
        modalDetailsElement.innerHTML = `${dateString}의 정보` + tag;
        scheduleModal.style.display = 'block';
    } catch (error) {
        console.error('Error fetching calendar data:', error);
    }
}

// 모달 닫기 =================
function closeModal(modal) {
    modal.style.display = 'none';
}

$closeModalButtons.forEach((button) => {
    button.addEventListener('click', () => {
        const modal = button.closest('.modal');
        closeModal(modal);
    });
});

window.addEventListener('click', (event) => {
    if (event.target === scheduleModal) {
        closeModal(scheduleModal);
    }
});

// ========= 함수 실행 =========

document.addEventListener('DOMContentLoaded', () => {
    prevMonthButton.addEventListener('click', () => {
        currentMonth--;
        if (currentMonth < 0) {
            currentMonth = 11;
            currentYear--;
        }
        updateCalendar(currentYear, currentMonth);
    });

    nextMonthButton.addEventListener('click', () => {
        currentMonth++;
        if (currentMonth > 11) {
            currentMonth = 0;
            currentYear++;
        }
        updateCalendar(currentYear, currentMonth);
    });

    updateCalendar(currentYear, currentMonth);

    // fetchReservations(); // 초기 예약 데이터 로드
    // window.addEventListener('scroll', setupInfiniteScroll); // 무한 스크롤 설정
    // cancelReservationClickEvent(); // 예약 취소 이벤트 설정
    // pickUpClickEvent(); // 픽업 완료 이벤트 설정
});
