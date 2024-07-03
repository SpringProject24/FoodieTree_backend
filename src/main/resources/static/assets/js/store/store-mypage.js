// ========= 전역 변수 =========
const BASE_URL = 'http://localhost:8083';

const calendarElement = document.getElementById('calendar');
const currentMonthElement = document.getElementById('current-month');
const prevMonthButton = document.getElementById('prev-month');
const nextMonthButton = document.getElementById('next-month');

const scheduleModal = document.getElementById('store-calendar-modal');
const modalDetailsElement = document.getElementById('modal-schedule-details');
const $closeModalButtons = document.querySelectorAll('.close');

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
        const response = await fetch(`${BASE_URL}/store/mypage/main/calendar/modal/${dateString}`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const res = await response.json();
        console.log("pickupTime, openAt, productCnt, canceledByStoreAt");
        console.log(res);

        tag = `<div>pickupTime: ${res.pickupTime || 'N/A'}</div>
               <div>openAt: ${res.openAt || 'N/A'}</div>
               <div>productCnt: ${res.productCnt || 'N/A'}</div>
               <div>canceledByStoreAt: ${res.canceledByStoreAt || 'N/A'}</div>
                <div>closedAt: ${res.closedAt || 'N/A'}</div>`;

        const selectedDate = new Date(year, month, day);
        const today = new Date();
        today.setHours(0, 0, 0, 0); // Compare dates without time
        let $storeCloseButton = ''; // 버튼 초기화
        let $setPickUpTimeButton = ''; // 버튼 초기화
        if (selectedDate > today) {
            $storeCloseButton = '<button id="store-holiday-btn">휴무일로 지정하기</button>';
            $setPickUpTimeButton = '<button id="set-pickup-time-btn">픽업 시간 설정하기</button>';
        }

        modalDetailsElement.innerHTML = `${dateString}의 정보` + tag + $storeCloseButton +'<br>'+ $setPickUpTimeButton;
        scheduleModal.style.display = 'block';

        // 버튼 이벤트 리스너 추가
        const button = modalDetailsElement.querySelector('#store-holiday-btn');
        if (button) {
            button.addEventListener('click', async () => {
                try {
                    console.log('휴무일로 지정하기 버튼 클릭')
                    console.log(dateString);
                    const response = await fetch(`${BASE_URL}/store/mypage/main/calendar/setHoliday`, {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify({
                            holidayDate: dateString // 휴무일로 지정할 날짜
                        })
                    });

                    const result = await response.json();
                    if (result === true) {
                        console.log('휴무일로 지정되었습니다.');
                        // 여기에 필요한 UI 업데이트 로직 추가
                        // 버튼 내용 변경
                        button.textContent = '휴무일 지정 취소하기';
                        // 예를 들어, 달력에서 휴무일로 지정된 날짜에 표시 변경 등
                        updateCalendar(currentYear, currentMonth); // 예시로 달력 업데이트
                    } else {
                        console.error('휴무일로 지정 실패');
                        // 실패 시 처리 로직 추가
                    }

                } catch (error) {
                    console.error('Error setting holiday:', error);
                    // 에러 처리 로직 추가
                }
            });
        }

        const pickupSettingBtn = modalDetailsElement.querySelector('#set-pickup-time-btn');
        if(pickupSettingBtn){
            pickupSettingBtn.addEventListener('click', async () => {
                try{
                    pickupSettingBtn.textContent = '확인';

                    // <input type="time"> 요소 추가
                    const inputTime = document.createElement('input');
                    inputTime.setAttribute('type', 'time');
                    inputTime.setAttribute('id', 'pickup-time-input');

                    const response = await fetch(`${BASE_URL}/store/mypage/main/calendar/modal/${dateString}`);
                    if (!response.ok) {
                        throw new Error(`HTTP error! status: ${response.status}`);
                    }
                    const res = await response.json();
                    console.log("pickupTime, openAt, productCnt, canceledByStoreAt, closedAt");
                    console.log(res);

                    // 오픈 시간 1시간 이후의 시간을 선택할 수 있도록 설정
                    // const now = new Date();
                    const hours = res.openAt.split(':')[0] * 1 + 1;
                    const minutes = res.openAt.split(':')[1];

                    const maxHours = res.closedAt.split(':')[0];
                    const maxMinutes = res.closedAt.split(':')[1];
                    inputTime.setAttribute('min', `${hours}:${minutes}`);
                    inputTime.setAttribute('max', `${maxHours}:${maxMinutes}`);

                    // 추가된 input 요소를 삽입할 위치를 찾아서 추가합니다.
                    const buttonContainer = button.parentNode;
                    buttonContainer.appendChild(inputTime);

                }
                catch (error) {
                    console.error('Error setting pickup time:', error);
                    // 에러 처리 로직 추가
                }
            });
        }

    } catch (error) {
        console.error('Error fetching schedule:', error);
        // 에러 처리 로직 추가
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
});
