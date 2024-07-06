const BASE_URL = window.location.origin;

export async function sendVerificationCodeForSignUp(customerId) {
    try {
        const response = await fetch(`${BASE_URL}/email/sendVerificationCode`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                email: customerId,
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
        const response = await fetch(`${BASE_URL}customer/check?type=account&keyword=${id}`);
        const result = await response.json();
        if (result) {
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