// const BASE_URL = 'http://localhost:8083';
const customerId = 'sji4205@naver.com'; // Replace this with the actual customer ID

let type;
let countdownInterval;
let debounceTimeout;

function editField(fieldId) {
    type = fieldId;
}

async function fetchUpdates(type, value) {
    const payload = {
        type: type,
        value: value
    };
    console.log('Updates to be sent:', payload); // Debugging line

    try {
        const response = await fetch(`${BASE_URL}/store/mypage/update`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify([payload])
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


async function sendVerificationCode() {
    try {
        const response = await fetch(`${BASE_URL}/email/sendVerificationCode`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ email: customerId }) // Replace with actual email
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
    const countdownElement = document.getElementById('countdown');
    countdownElement.textContent = `남은 시간: \${seconds}초`;

    countdownInterval = setInterval(() => {
        seconds -= 1;
        countdownElement.textContent = `남은 시간: \${seconds}초`;

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
    const newPassword = document.getElementById('new-password-input').value;
    const newPasswordCheck = document.getElementById('new-password-check').value;
    const statusElement = document.getElementById('password-match-status');
    const submitBtn = document.getElementById('submit-new-pw');

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

async function updatePassword() {
    const newPassword = document.getElementById('new-password-input').value;
    const newPasswordCheck = document.getElementById('new-password-check').value;

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
            body: JSON.stringify({ type: 'password', value: newPassword })
        });

        if (response.ok) {
            alert('비밀번호가 성공적으로 변경되었습니다.');
            closeNewPwModal();
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

const $btn = document.getElementById('reset-pw-btn');
$btn.addEventListener('click', openModal);

const $submitBtn = document.getElementById('submit-new-pw');
$submitBtn.addEventListener('click', openNewPwModal);

document.getElementById('price-btn').addEventListener('click', e => {
    // 선택된 옵션의 값을 가져옵니다.
    console.log("clicked");

    const selectedPrice = document.getElementById('price').value;
    console.log(selectedPrice);

    // input 요소의 값을 선택된 값으로 설정합니다.
    document.getElementById('price-input').value = selectedPrice;

});
