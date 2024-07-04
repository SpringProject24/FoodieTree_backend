import {
    fetchUpdates,
    openModal,
    closeModal,
    sendVerificationCode,

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
    const $id = $parent.querySelector('#phone');
    const res = await fetchUpdates($id.id, $id.value);
    if (res) {
        alert('휴대폰번호가 변경되었습니다.');
    } else {
        alert('휴대폰번호 변경에 실패했습니다. 잠시 후 다시 시도해주세요.');
    }
});

document.getElementById('update-pass-btn').addEventListener('click', openModal);
document.getElementById('close-modal-btn').addEventListener('click', closeModal);
document.getElementById('send-verification-code-btn').addEventListener('click', sendVerificationCode);