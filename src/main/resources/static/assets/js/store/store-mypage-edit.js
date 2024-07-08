import {
    checkPwInput,
    checkPwChkInput
} from "../validation.js";

const BASE_URL = window.location.origin;
// const customerId = '${sessionScope.login.customerId}'; // Replace this with the actual customer ID

const $inputPw = document.getElementById('new-password-input');
const $inputPwChk = document.getElementById('new-password-check');
const pwMessage = document.querySelector(('.pass-check h2'));
// const submitBtn = document.getElementById('submit-new-pw');

const newPassword = $inputPw.value;
const newPasswordCheck = $inputPwChk.value;
const statusElement = document.getElementById('password-match-status');
const submitBtn = document.getElementById('submit-new-pw');

const $verificationSendBtn = document.getElementById('sendVerificationCodeBtn');
const $verifyCodeBtn = document.getElementById('verifyCodeBtn');
const countdownElement = document.getElementById('countdown');

const errorMessage = document.getElementById('error-message');
const productCntInput = document.getElementById('product-cnt-input');
const productCntErrorMessage = document.getElementById('product-cnt-error-message');
const startTimeInput = document.getElementById('pickup-start-time');
const endTimeInput = document.getElementById('pickup-end-time');
const checkBtns = document.querySelectorAll('.fa-square-check');
const bnumInput = document.getElementById('business-number-input');
const bnumErrorMessage = document.getElementById('business-num-error-message');


let type;
let countdownInterval;
let debounceTimeout;



function editField(fieldId) {
    type = fieldId;
}

async function fetchCountUpdates(value) {
    try {
        const response = await fetch(`${BASE_URL}/store/mypage/edit/update/productCnt`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(value)
        });

        if (response.ok) {
            console.log('Update successful');
        } else {
            const errorText = await response.text();
            console.error('Update failed:', errorText);
        }
    } catch (error) {
        console.error('Error updating data:', error);
    }

}

async function fetchPriceUpdates(value) {
    try {
        const response = await fetch(`${BASE_URL}/store/mypage/edit/update/price`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(value)
        });

        if (response.ok) {
            console.log('Update successful');
        } else {
            const errorText = await response.text();
            console.error('Update failed:', errorText);
        }
    } catch (error) {
        console.error('Error updating data:', error);
    }
}



async function fetchUpdates(type, value) {
    const payload = {
        type: type,
        value: value
    };
    console.log('Updates to be sent:', payload); // Debugging line

    try {
        const response = await fetch(`${BASE_URL}/store/mypage/edit/update/info`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload)
        });

        if (response.ok) {
            console.log('Update successful');
        } else {
            const errorText = await response.text();
            console.error('Update failed:', errorText);
        }
    } catch (error) {
        console.error('Error updating data:', error);
    }
}

async function fetchTimeUpdates(type, value) {
    let time = 'openAt';

    if (type === 'closedAt'){
        time = 'closedAt';
    }
    try {
        const response = await fetch(`${BASE_URL}/store/mypage/edit/update/${time}`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(value)
        });

        if (response.ok) {
            console.log('Update successful');
        } else {
            const errorText = await response.text();
            console.error('Update failed:', errorText);
        }
    } catch (error) {
        console.error('Error updating data:', error);
    }
}

function handleKeyUp(event, fieldId) {
    event.preventDefault();
    if (event.key === 'Enter') {
        const element = event.target;
        const value = element.innerText;
        console.log(element);
        console.log(value);
        element.blur(); // Remove focus to trigger the update
        fetchUpdates(fieldId, value);
    }
}

// 비밀번호 재설정 모달 관련 함수
function openModal(e) {
    e.preventDefault();
    document.getElementById('resetPasswordModal').style.display = 'block';
}

function closeModal() {
    document.getElementById('resetPasswordModal').style.display = 'none';
}

// 비밀번호 재설정 입력 모달 관련 함수
function openNewPwModal() {
    // e.preventDefault();
    document.getElementById('newPasswordModal').style.display = 'block';
}

function closeNewPwModal() {
    document.getElementById('newPasswordModal').style.display = 'none';
}

// X 버튼 클릭 시 모달 닫기
document.addEventListener('DOMContentLoaded', function() {
    const closeButtons = document.querySelectorAll('.close');
    closeButtons.forEach(button => button.addEventListener('click', closeModal));
    closeButtons.forEach(button => button.addEventListener('click', closeNewPwModal));

    // 모달 바깥 클릭 시 모달 닫기
    window.onclick = function(event) {
        const resetModal = document.getElementById('resetPasswordModal');
        const newPwModal = document.getElementById('newPasswordModal');
        if (event.target === resetModal) {
            closeModal();
        }
        if (event.target === newPwModal) {
            closeNewPwModal();
        }
    };
});

document.addEventListener('DOMContentLoaded', () => {

    $verificationSendBtn.addEventListener('click', sendVerificationCode);

    async function sendVerificationCode() {
        checkPwInput($inputPw, $inputPwChk, pwMessage, submitBtn);
        checkPwChkInput($inputPw, $inputPwChk, pwMessage, submitBtn);
        try {
            const response = await fetch(`${BASE_URL}/email/sendVerificationCode`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ email: customerId,
                    purpose: 'reset'
                }) // Replace with actual email
            });

            if (response.ok) {
                startCountdown(300); // 5분(300초) 카운트다운 시작
                document.getElementById('emailStep').classList.add('hidden');
                document.getElementById('codeStep').classList.remove('hidden');
            } else {
                console.error('Failed to send verification code');
            }
        } catch (error) {
            console.error('Error sending verification code:', error);
        }
    }
    $verifyCodeBtn.addEventListener('click', verifyCode);
    async function verifyCode() {
        const code = document.getElementById('verificationCode').value;
        try {
            const response = await fetch('http://localhost:8083/email/verifyCode', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ email: customerId, code: code }) // Replace with actual email and code
            });
            console.log(response);

            if (response.ok) {
                const result = await response.text();
                document.getElementById('verificationResult').textContent = result;
                clearInterval(countdownInterval); // 인증 성공 시 타이머 멈춤
                openNewPwModal(); // 새로운 비밀번호 입력 모달 표시
            } else {
                console.error('Verification failed');
                document.getElementById('verificationResult').textContent = '실패';
            }
        } catch (error) {
            console.error('Error verifying code:', error);
            document.getElementById('verificationResult').innerText = '실패';
        }
    }

    function startCountdown(seconds) {
        countdownElement.textContent = `남은 시간: ${seconds}초`;

        countdownInterval = setInterval(() => {
            seconds -= 1;
            countdownElement.textContent = `남은 시간: ${seconds}초`;

            if (seconds <= 0) {
                clearInterval(countdownInterval);
                countdownElement.textContent = '시간 초과';
                closeModal(); // 모달 닫기
            }
        }, 1000);
    }

    function debounce(func, delay) {
        return function() {
            const context = this;
            const args = arguments;
            clearTimeout(debounceTimeout);
            debounceTimeout = setTimeout(() => func.apply(context, args), delay);
        };
    }

    function checkPasswordMatch() {

        if (newPassword && newPasswordCheck) {
            if (newPassword === newPasswordCheck) {
                statusElement.textContent = '비밀번호가 일치합니다.';
                statusElement.style.color = 'green';
                submitBtn.disabled = false; // Enable the button when passwords match
            } else {
                statusElement.textContent = '비밀번호가 일치하지 않습니다.';
                statusElement.style.color = 'red';
                submitBtn.disabled = true; // Disable the button when passwords don't match
            }
        } else {
            statusElement.textContent = '';
            submitBtn.disabled = true; // Disable the button if any field is empty
        }
    }

    const debounceCheckPassword = debounce(checkPasswordMatch, 1000);

    $submitBtn.addEventListener('click', updatePassword);

    async function updatePassword() {

        if (newPassword !== newPasswordCheck) {
            alert('비밀번호가 일치하지 않습니다. 다시 입력해주세요.');
            return;
        }

        try {
            const response = await fetch(`${BASE_URL}/store/mypage/edit/update/password`, {
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(newPassword)
            });

            if (response.ok) {
                alert('비밀번호가 성공적으로 변경되었습니다.');
                closeNewPwModal();
                closeModal();
            } else {
                const errorText = await response.text();
                console.error('Password update failed:', errorText);
                alert('비밀번호 변경에 실패했습니다.');
            }
        } catch (error) {
            console.error('Error updating password:', error);
            alert('비밀번호 변경 중 오류가 발생했습니다.');
        }
    }
});


const $btn = document.getElementById('reset-pw-btn');
$btn.addEventListener('click', openModal);

const $submitBtn = document.getElementById('submit-new-pw');
$submitBtn.addEventListener('click', openNewPwModal);


// 시간을 분으로 변환하는 함수
const timeToMinutes = (time) => {
    const [hours, minutes] = time.split(':').map(Number);
    return hours * 60 + minutes;
};

// 시간 비교 및 유효성 검사 함수
const validateTimes = () => {
    const startTime = startTimeInput.value;
    const endTime = endTimeInput.value;

    if (startTime && endTime) {
        const startTimeInMinutes = timeToMinutes(startTime);
        const endTimeInMinutes = timeToMinutes(endTime);

        if (startTimeInMinutes >= endTimeInMinutes) {
            errorMessage.style.display = 'block';
            errorMessage.textContent = '픽업 시작 시간은 픽업 마감 시간보다 늦어야 합니다.';
            return false;
        } else if (Math.abs(endTimeInMinutes - startTimeInMinutes) < 30) {
            errorMessage.style.display = 'block';
            errorMessage.textContent = '픽업 가능 시간은 30분 이상이어야 합니다.';
            return false;
        } else {
            errorMessage.style.display = 'none';
            return true;
        }
    }
};

// 기본 수량 값 입력 필드의 유효성 검사 함수
const validateProductCnt = () => {
    const cnt = parseInt(productCntInput.value, 10);

    if (cnt <= 0) {
        productCntErrorMessage.style.display = 'block';
        productCntErrorMessage.textContent = '기본 수량 값은 0 이하일 수 없습니다.';
        return false;
    } else {
        productCntErrorMessage.style.display = 'none';
        return true;
    }
};

// 입력 필드 값 변경 시 유효성 검사 함수를 호출합니다.
productCntInput.addEventListener('change', () => {
    validateProductCnt();
});

bnumInput.addEventListener('change', () => {
    validateBusinessNumber();
});

// 가게 전화번호 유효성 검사 함수
// 가게 전화번호 유효성 검사 함수
const validateBusinessNumber = () => {
    const bnum = bnumInput.value.replace(/-/g, ''); // 입력된 값에서 '-'를 제거합니다.
    const bnumPattern1 = /^\d{3}-\d{2}-\d{5}$/;
    const bnumPattern2 = /^\d{3}-\d{3}-\d{4}$/;
    const bnumPattern3 = /^\d{2}-\d{4}-\d{4}$/;
    const numericOnlyPattern = /^\d{10,}$/; // 10자리 이상인 숫자 패턴

    if (bnumPattern1.test(bnum) || bnumPattern2.test(bnum) || bnumPattern3.test(bnum) || numericOnlyPattern.test(bnum)) {
        bnumErrorMessage.style.display = 'none';
        return true;
    } else {
        bnumErrorMessage.style.display = 'block';
        bnumErrorMessage.textContent = '전화번호 형식에 맞지 않습니다.';
        return false;
    }
};

// 시간 입력 필드 값 변경 시 유효성 검사 함수를 호출합니다.
startTimeInput.addEventListener('input', validateTimes);
endTimeInput.addEventListener('input', validateTimes);

// 체크 버튼에 클릭 이벤트 리스너를 추가합니다.
checkBtns.forEach(checkBtn => {
    checkBtn.addEventListener('click', async (e) => {
        const inputWrapper = e.target.closest('.input-wrapper');
        const input = inputWrapper.querySelector('input');

        if (e.target.classList.contains('business-num')) {
            if (!validateBusinessNumber()) {
                alert('전화번호 형식에 맞지 않습니다. 다시 입력해주세요. 전화번호는 10자리 이상의 숫자여야 합니다.');
                return;
            }
            const value = bnumInput.value;
            fetchUpdates('business_number', value);
        }

        if (e.target.classList.contains('price-update')) {
            const value = document.getElementById('price').value;
            fetchPriceUpdates(value);
        }

        if (e.target.classList.contains('product-cnt')) {
            // 기본 수량 값 유효성 검사를 수행합니다.
            if (!validateProductCnt()) {
                alert('수량은 0이 될 수 없습니다. 수량을 0으로 설정하려면 가게 문을 닫으세요');
                return; // 유효하지 않으면 중단합니다.
            }

            const value = productCntInput.value;
            fetchCountUpdates(value); // 기본 수량 값 업데이트 함수 호출
        }

        if (e.target.classList.contains('time-set')) {
            // 시간 유효성 검사를 수행합니다.
            if (!validateTimes()) {
                alert('픽업 가능 시간 또는 픽업 마감 시간을 다시 확인해주세요.');
                return; // 유효하지 않으면 중단합니다.
            }

            // 시간에 따라 API 업데이트 함수를 호출합니다.
            const startTime = startTimeInput.value;
            const endTime = endTimeInput.value;

            if (input.id === 'pickup-start-time') {
                fetchTimeUpdates('openAt', startTime);
            } else if (input.id === 'pickup-end-time') {
                fetchTimeUpdates('closedAt', endTime);
            }
        }
    });
});
