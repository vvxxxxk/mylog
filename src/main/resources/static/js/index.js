document.getElementById('signButton').addEventListener('click', async function() {
    const accessToken = localStorage.getItem('accessToken');

    // 사용자가 로그인한 상태고 토큰이 유효한 경우
    if (accessToken && !isTokenExpired(accessToken)) {
        window.location.href = '/blog';
    }
    // 사용자가 로그인했지만 토큰이 만료된 경우
    else if (accessToken && isTokenExpired(accessToken)) {
        try {
            const newToken = await reissueToken();
             window.location.href = '/blog';

        } catch (error) {
            console.error(error);
            $('#loginModal').modal('show');
        }
    } else {
        // 사용자가 로그인되지 않은 상태면 로그인 모달 창 표시
        $('#loginModal').modal('show');
    }
});