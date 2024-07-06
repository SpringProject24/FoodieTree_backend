const BASE_URL = 'http://localhost:8083';

let type;
let countdownInterval;
let debounceTimeout;

function editField(fieldId) {
    type = fieldId;
}

export async function fetchUpdates(type, value) {
    const payload = {
        type: type,
        value: value
    };
    console.log('Updates to be sent:', payload); // Debugging line

    try {
        const response = await fetch(`${BASE_URL}/customer/${customerId}/update`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify([payload])
        });

        if (response.ok) {
            console.log('Update successful');
            return true;
        } else {
            const errorText = await response.text();
            console.error('Update failed:', errorText);
            return false;
        }
    } catch (error) {
        console.error('Error updating data:', error);
        return false;
    }
}

export async function deleteItem(type, value) {
    const payload = {
        type: type,
        value: value
    };

    try {
        const response = await fetch(`${BASE_URL}/customer/${customerId}/delete`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify([payload])
        });

        if (response.ok) {
            console.log('Delete successful');
            return true;
        } else {
            const errorText = await response.text();
            console.error('Delete failed:', errorText);
            return false;
        }
    } catch (error) {
        console.error('Error deleting item:', error);
        return false;
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
export function openModal(e) {
    e.preventDefault();
    document.getElementById('resetPasswordModal').style.display = 'block';
}

export function closeModal() {
    document.getElementById('resetPasswordModal').style.display = 'none';
}

// 비밀번호 재설정 입력 모달 관련 함수
export function openNewPwModal() {
    document.getElementById('newPasswordModal').style.display = 'block';
}

export function closeNewPwModal() {
    document.getElementById('newPasswordModal').style.display = 'none';
}

export async function sendVerificationCode() {
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
            return true;
        } else {
            console.error('Failed to send verification code');
            return false;
        }
    } catch (error) {
        console.error('Error sending verification code:', error);
        return false;
    }
}

export async function verifyCode(code) {
    try {
        const response = await fetch(`${BASE_URL}/email/verifyCode`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ email: customerId, code: code }) // Replace with actual email and code
        });
        console.log(response);

        if (response.ok) {
            stopCountdown();
            return { ok: true, result: await response.text() };
        } else {
            console.error('Verification failed');
            return { ok: false, result: '실패' };
        }
    } catch (error) {
        console.error('Error verifying code:', error);
        return { ok: false, result: '실패' };
    }
}

function startCountdown(seconds) {
    const countdownElement = document.getElementById('countdown');
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

function stopCountdown() {
    clearInterval(countdownInterval);
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
    const submitBtn = document.getElementById('update-new-pw-btn');

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

export const debounceCheckPassword = debounce(checkPasswordMatch, 1000);

export async function updatePassword(newPassword, newPasswordCheck) {
    try {
        const response = await fetch(`${BASE_URL}/customer/${customerId}/update/password`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ type: 'password', value: newPassword })
        });

        if (response.ok) {
            return true;
        } else {
            const errorText = await response.text();
            console.error('Password update failed:', errorText);
            return false;
        }
    } catch (error) {
        console.error('Error updating password:', error);
        return false;
    }
}

