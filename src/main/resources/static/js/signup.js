/**
 * @author ZSS
 * @date 2021/9/16
 * @desc 注册
 */
$("#signup-btn").click(function () {
    let username = $("#signup-username").val();
    let loginName = $("#signup-login-name").val();
    if (username === "") {
        Notiflix.Notify.Warning("用户名不能为空!");
        return false;
    }
    if (loginName === "") {
        Notiflix.Notify.Warning("登录名不能为空!");
        return false;
    }

    let password = $("#signup-password").val();
    let confirmPassword = $("#signup-confirm-password").val();

    if (checkPassword(password, confirmPassword)){
        let data = {loginName, password, username};
        signup(data);
    }
});

/**
 * 校验密码
 */
function checkPassword(password, confirmPassword) {
    if (password === "") {
        Notiflix.Notify.Warning("密码不能为空!");
        return false;
    } else if (confirmPassword === "") {
        Notiflix.Notify.Warning("请确认密码!");
        return false;
    } else if (password !== confirmPassword) {
        Notiflix.Notify.Warning("确认密码不正确");
        $("#signup-confirm-password").val("");
        $("#signup-password").val("").focus();
        return false;
    } else {
        return true;
    }
}

/* 注册 */
function signup(data) {
    $.ajax({
        url: "/user/signup",
        contentType: "application/json;charset=utf-8",
        type: "POST",
        dataType: "json",
        data: JSON.stringify(data),
        success: function (result) {
            analysisResult(result);
        }
    });
}

/* 解析数据 */
function analysisResult(result) {
    if (result.status === 0) {
        Notiflix.Notify.Success("注册成功");
        window.location.href = "/signin";
    } else {
        Notiflix.Notify.Failure("注册失败: " + result.msg);
        $("#signup-confirm-password").val("");
        $("#signup-password").val("").focus();
    }
}