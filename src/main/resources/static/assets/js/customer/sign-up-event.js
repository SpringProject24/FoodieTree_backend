import {stopCountdown} from "./mypage-edit.js";
import {checkDupId, sendVerificationCodeForSignUp} from "./sign-up.js";
import {checkIdInput, checkPwChkInput, checkPwInput} from "../validation.js";

const $inputId = document.getElementById('input-id');
const $idGetCodeBtn = document.getElementById('id-get-code-btn');
const $idVerifyWrapper = document.querySelector('.id-verify-wrapper');
const $idVerifyBtn = document.getElementById('id-verify-btn');
const $idBtn = document.getElementById('id-btn');

$inputId.addEventListener("keyup", _.debounce((e) => {
  const $idChk = document.querySelector('.id-wrapper h2');
  const check = checkIdInput(e.target.value, $idChk);
  if (check) {
    $idGetCodeBtn.disabled = false;
    $idGetCodeBtn.classList.remove("disable");
  }
}, 1000));

$idGetCodeBtn.addEventListener("click", async (e) => {
  e.preventDefault();
  const result = await sendVerificationCodeForSignUp($idInput.value);

  if (result) {
    startCountdown(300);
    $idInput.setAttribute('readonly', true);
    $idVerifyWrapper.style.display = 'block';
    $idGetCodeBtn.style.display = 'none';
  } else{
    console.error('Failed to send verification code');
    alert("잠시후 다시 이용해주세요.")
  }
});

document.getElementById('id-verify-btn').addEventListener("click", async () => {
  const code = document.getElementById('id-verify-code').value;
  const result = await verifyCode(code);

  if(result){
    alert("인증되었습니다!")
    stopCountdown();
    $idBtn.style.display = 'block';
    $idVerifyBtn.style.display = 'none';
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
  const $h2Id = document.querySelector('.id-wrapper h2');
  const $h2Pass = document.querySelector('.pass h2');
  const $inputPwChk = document.getElementById('input-pw-chk');

  checkPwInput($inputPw, $inputPwChk, $h2Pass, $passBtn);
  checkPwChkInput($inputPw, $inputPwChk, $h2Pass, $passBtn);

  $idBtn.addEventListener('click', async (e) => {
    e.preventDefault();
    const result = await checkDupId($inputId.value);
    if (result) {
      $h2Id.textContent = '이미 사용중인 이메일입니다.';
      $h2Id.style.color = 'red';
      return;
    }
    $idWrapper.classList.add('none');
    $passWrapper.classList.remove('none');
  });

  $prevBtn.addEventListener('click', (e) => {
    e.preventDefault();
    $idWrapper.classList.remove('none');
    $passWrapper.classList.add('none');
  });

  $passBtn.addEventListener('click', (e) => {
    e.preventDefault();
    const $password = document.getElementById('input-pw').value;
    const $passwordChk = document.getElementById('input-pw-chk').value;

    if ($password !== $passwordChk) {
      alert('비밀번호가 일치하지 않습니다.');
      return;
    }
    document.querySelector('.pass-wrapper').classList.add('none');
    document.querySelector('.food-wrapper').classList.remove('none');
  });
});

