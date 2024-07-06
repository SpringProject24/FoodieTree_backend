import {checkPwChkInput, checkPwInput} from "./validation.js";


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
        // 이메일 형식
        const emailRegExp = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
        if (!emailRegExp.test($inputId.value)) {
            $h2Id.textContent = '이메일 형식이 올바르지 않습니다.';
            $h2Id.style.color = 'red';
            return;
        }

        const res = await fetch(`http://localhost:8083/customer/check?type=account&keyword=${$inputId.value}`);
        const result = await res.json();
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

    // Other script sections can be added similarly within DOMContentLoaded
});