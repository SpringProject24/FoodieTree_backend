import {sendVerificationCode, stopCountdown} from "./mypage-edit.js";

const $verifySection = document.querySelector('.id-verify-wrapper');
const $verifySendBtn = document.getElementById('id-get-code-btn');
const $idBtn = document.getElementById('id-btn');
const $idInput = document.getElementById('input-id');
const $verifyBtn = document.getElementById('id-verify-btn');

$verifySection.style.display = 'none';
$verifySendBtn.style.display = 'block';
$idBtn.style.display = 'none';

document.getElementById('input-id').addEventListener("keyup", {

});

$verifySendBtn.addEventListener("click", async () => {
  console.log(customerId);
  $verifySection.style.display = 'block';
  const res = await sendVerificationCode();
  console.log(res);
  if (res.ok) {
    startCountdown(300);
    $idInput.setAttribute('readonly', true);
    $verifySection.style.display = 'block';
    $verifySendBtn.style.display = 'none';
    return true;
  }else{
    console.error('Failed to send verification code');
    return false;
  }
});

document.getElementById('id-verify-btn').addEventListener("click", async () => {
  const code = document.getElementById('id-verify-code').value;
  const res = await verifyCode(code);

  if(res.ok){
    stopCountdown();
    $idBtn.style.display = 'block';
    $verifyBtn.style.display = 'none';
    return true;
  }else{
    console.error('Failed to verify code');
    return false;
  }
});

