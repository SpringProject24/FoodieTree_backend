
const BASE_URL = window.location.origin;
let countdownInterval;

export async function updatePassword(customerId, newPassword, newPasswordCheck) {
    try {
        const response = await fetch(`${BASE_URL}/customer/${customerId}/update/password`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ type: 'password', value: newPassword }),
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

export async function checkCustomerId(id) {
    try {
        const res = await fetch(`${BASE_URL}/customer/check?type=account&keyword=${id}`);
        return await res.json();
    } catch {
        console.error(data);
        return false;
    }
}

export async function sendCodeToEmail(id) {
    try {
        const response = await fetch(`${BASE_URL}/email/sendVerificationCode`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ email: id,
                purpose: 'reset'
            }) // Replace with actual email
        });

        if (response.ok) {
            startCountdown2(300); // 5분(300초) 카운트다운 시작
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

export async function sendVerifyCode(id, code) {
    try {
        const response = await fetch(`${BASE_URL}/email/verifyCode`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ email: id, code: code }) // Replace with actual email and code
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

function startCountdown2(seconds) {
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