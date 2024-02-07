document.getElementById("login").onclick = function (e) {
    let email = document.getElementById("email");
    let password = document.getElementById("password");
    let msg = document.getElementById("msg");
    if (email.value.length < 6) {
        msg.innerHTML = "Email length will be more 6 symbols !";
        e.preventDefault();
    }
    if (password.value.length < 6) {
        msg.innerHTML = "Password length will be more 6 symbols !";
        e.preventDefault();
    }
}