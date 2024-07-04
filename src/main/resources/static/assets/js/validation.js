import {debounce} from "./utils.js";
const $idInput = document.getElementById("user_id");
const infoChk  = {
  account : false,
  pw : false,
  name : false,
  email : false
};
const $submitBtn = document.getElementById("signup-btn");

$submitBtn.disabled = true;
async function fetchDuplicateCheck(type, keyword) {
  const res = await fetch(`http://localhost:8383/members/check-identity?type=${type}&keyword=${keyword}`);
  let flag = await res.json();
  console.log(flag);
  return flag.result;
}

/**
 *
 * @param $idInput 검증할 태그
 * @param $idChk 메시지 표시할 태그
 */
const checkIdInput = ($idInput, $idChk) => {
  $idInput.addEventListener("keyup", async function (e) {
    const idPattern = /^[A-Za-z0-9_\.\-]+@[A-Za-z0-9\-]+\.[A-Za-z0-9\-]+$/;
    const value = e.target.value;

    if (value.trim() === "") {
      $idChk.innerHTML = '<b class="warning">[아이디를 입력해주세요]</b>';
    } else if (!idPattern.test(value)) {
      $idChk.innerHTML = '<b class="warning">[아이디는 4~14자의 영문 대소문자와 숫자로만 입력해주세요]</b>';
    }
  });
}

const checkPwInput = ($pwInput, $pwChk, $pwChk2) => {
  $pwInput.addEventListener("keyup", function (e) {
    const passwordPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{8,20}$/;
    const value = e.target.value;
    $pwChkInput.disabled = true;
    $pwChk2.innerHTML = "";
    if (value.trim() === "") {
      $pwChk.innerHTML = '<b class="warning">[비밀번호를 입력해주세요]</b>';
    } else if (!passwordPattern.test(value)) {
      $pwChk.innerHTML = '<b class="warning">[비밀번호는 8~20자의 영문 대소문자, 숫자, 특수문자를 포함해주세요]</b>';
    } else {
      $pwChkInput.disabled = false;
      $pwChk.innerHTML = '<b class="success">[사용 가능한 비밀번호입니다.]</b>';
    }
    checkInfo();
  });
}


const $pwInput = document.getElementById("password");
const $pwChkInput = document.getElementById("password_check");
const $pwChk2 = document.getElementById("pwChk2");
$pwChkInput.disabled = true;

$pwInput.addEventListener("keyup", function (e) {
  const passwordPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!,@,#,$,%,^,&,*,?,_,~]).{8,20}$/;
  const value = e.target.value;
  const $pwChk = document.getElementById("pwChk");
  $pwChkInput.disabled = true;
  $pwChk2.innerHTML = "";
  if (value.trim() === "") {
    $pwChk.innerHTML = '<b class="warning">[비밀번호를 입력해주세요]</b>';
  } else if (!passwordPattern.test(value)) {
    $pwChk.innerHTML = '<b class="warning">[비밀번호는 8~20자의 영문 대소문자, 숫자, 특수문자를 포함해주세요]</b>';
  } else {
    $pwChkInput.disabled = false;
    $pwChk.innerHTML = '<b class="success">[사용 가능한 비밀번호입니다.]</b>';
  }
  checkInfo();
});

$pwChkInput.addEventListener("keyup", function (e) {
  const value = e.target.value;
  if (value === $pwInput.value) {
    $pwChk2.innerHTML = '<b class="success">[비밀번호가 일치합니다.]</b>';
    infoChk.pw = true;
  } else {
    $pwChk2.innerHTML = '<b class="warning">[비밀번호가 일치하지 않습니다.]</b>';
    infoChk.pw = false;
  }
  checkInfo();
});

const $nameInput = document.getElementById("user_name");

$nameInput.addEventListener("keyup", function (e) {
  const namePattern = /^[가-힣]{2,8}$/;
  const value = e.target.value;
  const $nameChk = document.getElementById("nameChk");
  if (value.trim() === "") {
    $nameChk.innerHTML = '<b class="warning">[이름을 입력해주세요]</b>';
    infoChk.name = false;
  } else if (!namePattern.test(value)) {
    $nameChk.innerHTML = '<b class="warning">[이름은 2~8자의 한글로만 입력해주세요]</b>';
    infoChk.name = false;
  } else {
    $nameChk.innerHTML = '<b class="success">[사용 가능한 이름입니다.]</b>';
    infoChk.name = true;
  }
  checkInfo();

});

const $emailInput = document.getElementById("user_email");

$emailInput.addEventListener("keyup", debounce(async function (e) {
  const emailPattern = /^[A-Za-z0-9_\.\-]+@[A-Za-z0-9\-]+\.[A-Za-z0-9\-]+$/;
  const value = e.target.value;
  const $emailChk = document.getElementById("emailChk");
  if (value.trim() === "") {
    $emailChk.innerHTML = '<b class="warning">[이메일을 입력해주세요]</b>';
    infoChk.email = false;
  } else if (!emailPattern.test(value)) {
    $emailChk.innerHTML = '<b class="warning">[이메일 형식에 맞게 입력해주세요]</b>';
    infoChk.email = false;
  } else {
    if (await fetchDuplicateCheck("email", value)) {
      $emailChk.innerHTML = '<b class="warning">[중복된 이메일 입니다]</b>';
      infoChk.email = false;
    } else {
      $emailChk.innerHTML = '<b class="success">[사용 가능한 이메일입니다.]</b>';
      infoChk.email = true;
    }
  }
  checkInfo();
}, 500));

function checkInfo() {
  $submitBtn.disabled = true;
  $submitBtn.style.backgroundColor = "gray";
  for (let key in infoChk) {
    if (!infoChk[key]) {
      return;
    }
  }
  $submitBtn.disabled = false;
  $submitBtn.style.backgroundColor = "orange";

}


