const BASE_URL = window.location.origin;

let countdownInterval;

export async function sendVerificationCodeForSignUp(storeId) {
    try {
        const response = await fetch(`${BASE_URL}/email/sendVerificationCode`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                email: storeId,
                purpose: 'signup'
            }),
        });
        if (response.ok) {
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

export async function checkDupId(id){
    try {
        const response = await fetch(`${BASE_URL}/store/check?type=account&keyword=${id}`);
        const result = await response.json();
        if (!result) {
            return true;
        } else {
            console.error('이미 가입된 아이디');
            return false;
        }
    } catch (error) {
        console.error('Error:', error);
        return false;
    }
}

export function startCountdown(seconds) {
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

export function stopCountdown() {
    clearInterval(countdownInterval);
}

export async function sendVerifyCode(id, code) {
    try {
        const response = await fetch(`${BASE_URL}/email/verifyCode?purpose=signup`, {
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