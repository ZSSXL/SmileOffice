/**
 * @author ZSS
 * @date 2021/9/16
 * @desc 登录
 */
$("#login-btn").click(function () {
    let loginName = $("#login-name").val();
    let password = $("#password").val();

    let data = {loginName, password};
    login(data);
});

/* 登录 */
function login(data) {
    $.ajax({
        url: "/user/login",
        contentType: "application/json;charset=utf-8",
        type: "POST",
        dataType: "json",
        data: JSON.stringify(data),
        success: function (result) {
            console.log(result);
            analysisResult(result);
        }
    });
}

/* 解析数据 */
function analysisResult(result) {
    if (result.status === 0) {
        localStorage.setItem("smile-office-current-page", "document");
        localStorage.setItem("smile-office-token", result.data);
        Notiflix.Notify.Success("登录成功");
        window.location.href = "/index";
    } else {
        Notiflix.Notify.Failure("登录失败: " + result.msg);
        $("#password").val("").focus();
    }
}