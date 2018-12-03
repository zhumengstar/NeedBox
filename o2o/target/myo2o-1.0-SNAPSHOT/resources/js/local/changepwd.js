$(function () {
    //修改平台密码的controller url
    var url = '/o2o/local/changelocalpwd';
    //从地址栏的URL里获取usertype
    var usertype = getQueryString('usertype');
    $('#submit').click(function () {
        var username = $('#username').val();
        var password = $('#password').val();
        var newPassword = $('#newPassword').val();
        var confirmPassword = $('#confirmPassword').val();
        var verifyCodeActual = $('#j_captcha').val();
        if(!verifyCodeActual) {
            $.toast('请输入验证码!');
            return;
        }
        if(newPassword != password) {
            $.toast('新密码不能和原密码相同!');
            $('#captcha_img').click();
            return;
        }
        if(newPassword != confirmPassword) {
            $.toast('两次输入的新密码不一致!');
            $('#captcha_img').click();
            return;
        }

        var formData = new FormData();
        formData.append("username", username);
        formData.append("password", password);
        formData.append("newPassword", newPassword);
        formData.append("verifyCodeActual", verifyCodeActual);

        $.ajax({
            url: url,
            type: 'POST',
            data: formData,
            contentType: false,
            processData: false,
            cache: false,
            success: function (data) {
                if (data.success) {
                    $.toast("提交成功!");
                    window.location.href = '/o2o/local/login';
                } else {
                    $.toast("提交失败!" + data.errMsg);
                    $('#captcha_img').click();
                }
            }
        });
    });
});