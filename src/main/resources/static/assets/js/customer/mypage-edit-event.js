import {
    fetchUpdates,
    openModal,
    closeModal,
    sendVerificationCode,
    verifyCode,
    openNewPwModal,
    closeNewPwModal,
    debounceCheckPassword,
    updatePassword, deleteItem,

} from "./mypage-edit.js";

document.getElementById('update-nickname').addEventListener('click', async (e) => {
    const $parent = e.target.closest('.input-wrapper');
    const $id = $parent.querySelector('#nickname');
    const res = await fetchUpdates($id.id, $id.value);
    if (res) {
        alert('닉네임이 변경되었습니다.');
    } else {
        alert('닉네임 변경에 실패했습니다. 잠시 후 다시 시도해주세요.');
    }
});

document.getElementById('update-phone').addEventListener('click', async (e) => {
    const $parent = e.target.closest('.input-wrapper');
    const $id = $parent.querySelector('#customer-phone-number');
    const res = await fetchUpdates($id.id, $id.value);
    if (res) {
        alert('휴대폰번호가 변경되었습니다.');
    } else {
        alert('휴대폰번호 변경에 실패했습니다. 잠시 후 다시 시도해주세요.');
    }
});

document.getElementById('send-verification-code-btn').addEventListener('click', async (e) => {
    const res = await sendVerificationCode();
    if (res) {
        document.getElementById('step-email').classList.add('hidden');
        document.getElementById('step-code').classList.remove('hidden');
    } else {
        alert('인증번호 발송에 실패했습니다. 잠시 후 다시 시도해주세요.');
    }
});

document.getElementById('verification-code-btn').addEventListener('click', async (e) => {
    const code = document.getElementById('verification-code').value;
    const res = await verifyCode(code);
    if (res.ok) {
        openNewPwModal();
    }
    document.getElementById('verification-result').textContent = res.result;
});

document.getElementById('new-password-input').addEventListener('keyup', debounceCheckPassword);
document.getElementById('new-password-check').addEventListener('keyup', debounceCheckPassword);

document.getElementById('update-new-pw-btn').addEventListener('click', async (e) => {
    const newPassword = document.getElementById('new-password-input').value;
    const newPasswordCheck = document.getElementById('new-password-check').value;
    const res = await updatePassword(newPassword, newPasswordCheck);
    if (res) {
        alert('비밀번호가 변경되었습니다.');
        closeNewPwModal();
        closeModal();
    } else{
        alert('비밀번호 변경에 실패했습니다. 잠시 후 다시 시도해주세요.');
    }
});

document.getElementById('preferred-area').addEventListener('click', async (e) => {
    if (e.target.matches('i') === false) return;
    const $li = e.target.closest('li');
    const $span = $li.querySelector('span');
    const res = await deleteItem('preferredArea', $span.textContent);
    if (res) {
        $li.remove();
    } else {
        alert('선호지역 삭제에 실패했습니다. 잠시 후 다시 시도해주세요.');
    }
});

document.getElementById('preferred-food').addEventListener('click', async (e) => {
    if (e.target.matches('i') === false) return;
    const $li = e.target.closest('li');
    const $span = $li.querySelector('span');
    const res = await deleteItem('preferredFood', $span.textContent);
    if (res) {
        $li.remove();
    } else {
        alert('선호음식 삭제에 실패했습니다. 잠시 후 다시 시도해주세요.');
    }
});

document.getElementById('preferred-store').addEventListener('click', async (e) => {
    if (e.target.matches('i') === false) return;
    const $li = e.target.closest('li');
    const res = await deleteItem('favStore', $li.id);
    if (res) {
        $li.remove();
    } else {
        alert('최애가게 삭제에 실패했습니다. 잠시 후 다시 시도해주세요.');
    }
});

document.getElementById('update-pass-btn').addEventListener('click', openModal);
document.getElementById('close-modal-btn').addEventListener('click', closeModal);
document.getElementById('close-new-modal-btn').addEventListener('click', closeNewPwModal);