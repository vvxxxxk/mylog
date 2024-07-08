document.getElementById('loginForm').addEventListener('submit', function(event) {
    event.preventDefault();

    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    fetch('/api/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ email: email, password: password })
    })
    .then(response => {
        if (response.ok) {
            return response.text(); // 서버가 200 OK 응답을 보낸 경우
        } else {
            throw new Error('로그인에 실패하였습니다');
        }
    })
    .then(message => {
        //alert(message);
        window.location.href = `/`;
    })
    .catch((error) => {
        console.error('Error:', error);
        alert('로그인에 실패하였습니다');
    });
});

document.getElementById('logoutButton').addEventListener('click', function(event) {
    event.preventDefault();

    fetch('/api/logout', {
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
            window.location.href = '/';
        } else {
            alert('로그아웃에 실패하였습니다: ' + data.message);
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('로그아웃에 실패하였습니다');
    });
});