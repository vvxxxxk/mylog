document.getElementById('loginForm').addEventListener('submit', function(event) {
    event.preventDefault();

    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const redirectUrl = window.location.href;

    fetch('/api/auth/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ email: email, password: password })
    })
    .then(response => {
        if (response.ok) {
            return response.text();
        } else {
            throw new Error('아이디/비밀번호를 잘못 입력하였거나 탈퇴한 회원입니다.');
        }
    })
    .then(message => {
        window.location.href = redirectUrl;
    })
    .catch((error) => {
        alert('아이디/비밀번호를 잘못 입력하였거나 탈퇴한 회원입니다.');
    });
});

document.getElementById('logoutButton').addEventListener('click', function(event) {
    event.preventDefault();
    logout();
});

function logout() {
    fetch('/api/auth/logout', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (response.ok) {
            return response.json();
        } else {
            throw new Error('로그아웃에 실패하였습니다');
        }
    })
    .then(data => {
        if (data.status == true) {
            window.location.href = '/main';
        } else {
            alert('로그아웃에 실패하였습니다: ' + data.message);
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('로그아웃에 실패하였습니다');
    });
}