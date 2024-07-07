import {checkDupId, sendVerificationCodeForSignUp, startCountdown, stopCountdown, sendVerifyCode} from "./sign-up.js";
import {checkIdInput, checkPwChkInput, checkPwInput} from "../validation.js";

const $inputId = document.getElementById('input-id');
const $idGetCodeBtn = document.getElementById('id-get-code-btn');
const $idVerifyWrapper = document.querySelector('.id-verify-wrapper');
const $idWrapper = document.querySelector('.id-wrapper');
const $passWrapper = document.querySelector('.pass-wrapper');
const $idChk = document.querySelector('.id-wrapper h2');
const $spinner = document.querySelector(".loader");

$inputId.addEventListener("keyup", _.debounce(async (e) => {
  const check1 = checkIdInput(e.target.value, $idChk);
  if (!check1) {
    return;
  }
  const check2 = await checkDupId($inputId.value);
  if (!check2) {
    $idChk.textContent = '이미 사용중인 이메일입니다.';
    $idChk.style.color = 'red';
    return;
  }
  $idChk.innerHTML = '<b class="warning">[사용가능합니다]</b>';
  $idChk.style.color = 'green';
  $idGetCodeBtn.disabled = false;
  $idGetCodeBtn.classList.remove("disable");
}, 1000));

$idGetCodeBtn.addEventListener("click", async (e) => {
  e.preventDefault();
  $spinner.classList.remove("none");
  const result = await sendVerificationCodeForSignUp($inputId.value);
  $spinner.classList.add("none");
  if (result) {
    startCountdown(300);
    $inputId.setAttribute('readonly', true);
    $idVerifyWrapper.style.display = 'block';
    $idGetCodeBtn.style.display = 'none';
  } else{
    console.error('Failed to send verification code');
    alert("잠시후 다시 이용해주세요.")
  }
});

document.getElementById('id-verify-btn').addEventListener("click", async (e) => {
  e.preventDefault();
  const code = document.getElementById('id-verify-code').value;
  const result = await sendVerifyCode($inputId.value, code);

  if(result){
    alert("인증되었습니다!")
    stopCountdown();
    $idWrapper.classList.add('none');
    $passWrapper.classList.remove('none');
  } else{
    console.error('Failed to verify code');
  }
});

document.addEventListener('DOMContentLoaded', () => {
  const $idBtn = document.getElementById('id-btn');
  const $passBtn = document.getElementById('pass-btn');
  const $idWrapper = document.querySelector('.id-wrapper');
  const $passWrapper = document.querySelector('.pass-wrapper');
  const $inputId = document.getElementById('input-id');
  const $inputPw = document.getElementById('input-pw');
  const $prevBtn = document.getElementById('prev-btn');
  const $h2Pass = document.querySelector('.pass h2');
  const $inputPwChk = document.getElementById('input-pw-chk');

  checkPwInput($inputPw, $inputPwChk, $h2Pass, $passBtn);
  checkPwChkInput($inputPw, $inputPwChk, $h2Pass, $passBtn);

  $prevBtn.addEventListener('click', (e) => {
    e.preventDefault();
    $idWrapper.classList.remove('none');
    $passWrapper.classList.add('none');
  });

  $passBtn.addEventListener('click', (e) => {
    e.preventDefault();
    const $password = document.getElementById('input-pw');
    const $passwordChk = document.getElementById('input-pw-chk');

    if ($password.value !== $passwordChk.value) {
      alert('비밀번호가 일치하지 않습니다.');
      return;
    }
    document.querySelector('.pass-wrapper').classList.add('none');
    document.querySelector('.food-wrapper').classList.remove('none');
  });
});

