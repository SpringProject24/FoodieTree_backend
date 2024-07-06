export async function updatePassword(customerId, newPassword, newPasswordCheck) {
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