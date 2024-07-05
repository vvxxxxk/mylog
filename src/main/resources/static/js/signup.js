  let isEmailVerified = false;
  let isPasswordVerified = false;
  let isNicknameChecked = false;

  <!-- 이메일 인증 버튼 클릭 시 이메일 유효성 검사 -->
  document.getElementById('verifyEmailButton').addEventListener('click', function() {
    const email = document.getElementById('email').value;
    const emailField = document.getElementById('email');

    let isFlag = false;
    if (!emailField.checkValidity()) {
      emailField.classList.add('is-invalid');
      isFlag = false;
    } else {
      emailField.classList.remove('is-invalid');
      isFlag = true;
    }

    if (isFlag) {
      fetch("/api/check-email", {
        method: "POST",
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ email: email })
      })
      .then(response => response.json())
      .then(data => {
        if (data.status == true) {
          emailField.classList.remove('is-invalid');
          $('#emailVerifyModal').modal('show');
        } else {
          emailField.classList.add('is-invalid');
        }
      })
      .catch(error => {
        console.error('Error:', error);
        alert('서버와의 통신 중 오류가 발생했습니다.');
      });
    }
  });

  <!-- 인증 코드 전송 -->
  document.getElementById('sendAuthCodeButton').addEventListener('click', async function(event) {
    event.preventDefault();
    const email = document.getElementById('email').value;
    fetch("/api/send-authcode", {
      method: "POST",
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ email: email })
    })
    .then(response => response.json())
    .then(data => {
    })
    .catch(error => {
      console.error('Error:', error);
      alert('서버와의 통신 중 오류가 발생했습니다.');
    });

    alert('인증 코드를 전송했습니다. 인증 코드를 입력해주세요.');
  });

  <!-- 인증코드 필드에 입력 시 인증코드 유효성 검사 -->
  document.getElementById('authcode').addEventListener('input', function() {
    const authcode = document.getElementById('authcode').value;
    const authcodeField = document.getElementById('authcode');
    const verifyButton = document.getElementById('verifyButton');

    // 인증코드 정규표현식
    const authcodeRegex = /^[0-9]{6}$/;
    if (authcodeRegex.test(authcode)) {
      document.getElementById('authcodeFeedback').style.display = 'none';
      authcodeField.classList.remove('is-invalid');
      verifyButton.disabled = false;
    } else {
      document.getElementById('authcodeFeedback').style.display = 'block';
      authcodeField.classList.add('is-invalid');
      verifyButton.disabled = true;
    }
  });

  <!-- 이메일 검증 -->
  function verifyEmail() {
      const email = document.getElementById('email').value;
      const authcode = document.getElementById('authcode').value;

      fetch("/api/verify-authcode", {
        method: "POST",
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ email: email, authcode: authcode})
      })
      .then(response => response.json())
      .then(data => {
        if (data.status == true) {
          document.getElementById('email').readOnly = true;
          document.getElementById('email').classList.add('verified');
          isEmailVerified = true;
          $('#emailVerifyModal').modal('hide');
        } else {
          alert('인증 코드가 올바르지 않습니다. 다시 시도해주세요.');
        }
      })
      .catch(error => {
        console.error('Error:', error);
        alert('서버와의 통신 중 오류가 발생했습니다.');
      });
  }

  <!-- 비밀번호 필드 입력 시 비밀번호 유효성 검사 -->
  document.getElementById('password').addEventListener('input', function() {
    const password = document.getElementById('password').value;
    const passwordField = document.getElementById('password');
    const confirmPassword = document.getElementById('confirmPassword').value;
    const confirmPasswordField = document.getElementById('confirmPassword');

    // 비밀번호 정규표현식
    const passwordRegex = /^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*?_]).{8,16}$/;

    if (passwordRegex.test(password)) {
      document.getElementById('passwordFeedback').style.display = 'none';
      passwordField.classList.remove('is-invalid');
    } else {
      document.getElementById('passwordFeedback').style.display = 'block';
      passwordField.classList.add('is-invalid');
    }

    if (password == confirmPassword) {
      document.getElementById('confirmPasswordFeedback').style.display = 'none';
      confirmPasswordField.classList.remove('is-invalid');
      isPasswordVerified = true;
    } else {
      document.getElementById('confirmPasswordFeedback').style.display = 'block';
      confirmPasswordField.classList.add('is-invalid');
      isPasswordVerified = false;
    }
  });

  <!-- 비밀번호 필드와 비밀번호 확인 필드 일치 여부 유효성 검사 -->
  document.getElementById('confirmPassword').addEventListener('input', function() {
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirmPassword').value;
    const confirmPasswordField = document.getElementById('confirmPassword');

    if (password == confirmPassword) {
      document.getElementById('confirmPasswordFeedback').style.display = 'none';
      confirmPasswordField.classList.remove('is-invalid');
      isPasswordVerified = true;
    } else {
      document.getElementById('confirmPasswordFeedback').style.display = 'block';
      confirmPasswordField.classList.add('is-invalid');
      isPasswordVerified = false;
    }
  });

  <!-- 닉네임 필드 입력 시 검증 변수 초기화 -->
  document.getElementById('nickname').addEventListener('input', function() {
    isNicknameChecked = false;
  });

  <!-- 닉네임 중복확인 -->
  function checkNickname() {
    const nickname = document.getElementById('nickname').value;
    fetch("/api/check-nickname", {
      method: "POST",
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ nickname: nickname })
    })
    .then(response => response.json())
    .then(data => {
      if (data.status == true) {
        alert('사용 가능한 닉네임 입니다.');
        isNicknameChecked = true;
      } else {
        alert('중복된 닉네임입니다. 다른 닉네임을 입력해주세요.');
        isNicknameChecked = false;
      }
    })
    .catch(error => {
      console.error('Error:', error);
      alert('서버와의 통신 중 오류가 발생했습니다.');
    });
  }

  <!-- 회원가입 폼 제출 전 검증 -->
  document.getElementById('signupForm').addEventListener('submit', function(event) {
    // 임시로 이메일 인증 없이 회원가입 가능
    isEmailVerified = true;
    if(!isEmailVerified) {
      alert('이메일 인증은 필수입니다.');
      event.preventDefault();
    } else if (!isPasswordVerified) {
      document.getElementById('passwordFeedback').style.display = 'block';
      document.getElementById('password').classList.add('is-invalid');
      document.getElementById('confirmPasswordFeedback').style.display = 'block';
      document.getElementById('confirmPassword').classList.add('is-invalid');
      event.preventDefault();
    } else if (!isNicknameChecked) {
      alert('닉네임 중복 확인은 필수입니다.');
      event.preventDefault();
    }
  });