import {checkCustomerId, sendCodeToEmail, sendVerifyCode, updatePassword } from "./sign-in.js";
import {checkPw, checkPwChk } from "../validation.js";

const $verifyWrapper = document.querySelector('.verify-wrapper');
const $inputId = document.getElementById('input-id');
const $inputPw = document.getElementById('input-pw');
const $inputPwChk = document.getElementById('input-pw-chk');
const $inputPassWrapper = document.querySelector('.input-pass-wrapper');
const $p = document.querySelector('.input-pass-wrapper p');
const $updateNewPwBtn = document.getElementById('update-new-pw-btn');

document.getElementById('find-pw-a').addEventListener('click', (e) => {
    e.preventDefault();
    const $modal = document.getElementById('exampleModal');
    const target = $btnWrapper.querySelector('.checked')

    if (target.id === 'store-btn') {
        $modal.classList.add('store')
    } else if (target.id === 'customer-btn') {
        $modal.classList.add('customer')
    }
});

document.getElementById('check-id-btn').addEventListener('click', async (e) => {
    const res = await checkCustomerId($inputId.value);

    if (res) {
        $verifyWrapper.classList.remove('hidden');
    } else {
        alert("계정이 없습니다.");
    }
});

document.getElementById('send-verification-code-btn').addEventListener('click', async (e) => {
    const res = await sendCodeToEmail($inputId.value);

    if (res) {
        document.getElementById('step-email').classList.add('hidden');
        document.getElementById('step-code').classList.remove('hidden');
    } else {
        alert('인증번호 발송에 실패했습니다. 잠시 후 다시 시도해주세요.');
    }
});

document.getElementById('verification-code-btn').addEventListener('click', async (e) => {
    const code = document.getElementById('verification-code').value;
    const res = await sendVerifyCode($inputId.value, code);

    if (res.ok) {
        alert("인증에 성공했습니다");
        document.querySelector(".input-id-wrapper").classList.add('hidden');
        $inputPassWrapper.classList.remove('hidden');
        return ;
    } else {
        alert("인증번호가 틀립니다!");
    }
    document.getElementById('verification-result').textContent = res.result;
});

$inputPw.addEventListener('keyup', (e) => {
    const res = checkPw(e.target.value);

    if (res.ok) {
        $p.innerHTML = res.msg;
        $inputPwChk.disabled = false;
        return ;
    }
    $p.innerHTML = res.msg;
})

$inputPwChk.addEventListener('keyup', (e) => {
    const res = checkPwChk($inputPw.value, $inputPwChk.value);

    if (res.ok) {
        $p.innerHTML = res.msg;
        $updateNewPwBtn.disabled = false;
        return ;
    }
    $p.innerHTML = res.msg;
});

$updateNewPwBtn.addEventListener('click', async (e) => {
    $inputPw.disabled = true;
    $inputPwChk.disabled = true;
    const res = await updatePassword($inputId.value, $inputPwChk.value);

    if (res) {
        alert("비밀번호가 재설정 되었습니다.");
        document.querySelector("button.btn-secondary").click();
    } else {
        alert("잠시 후 다시 시도해주세요.");
    }
});