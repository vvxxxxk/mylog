document.getElementById('startButton').addEventListener('click', async function() {
    try {
        const response = await fetch('/api/check-session', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (response.ok) {
            const data = await response.json();
            if (data.status === true) {
                window.location.href = '/blog';
            } else {
                $('#loginModal').modal('show');
            }
        } else {
            $('#loginModal').modal('show');
        }
    } catch (error) {
        console.error('Error:', error);
        $('#loginModal').modal('show');
    }
});
