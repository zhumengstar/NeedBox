$(function () {
    //登录验证的controller url
    var loginUrl = '/o2o/local/logincheck';
    var loginCount = 0;

    $('#submit').click(function () {
        var username = $('#username').val();
        var password = $('#password').val();
        var verifyCodeActual = $('#j_captcha').val();
        var needVerify = false;
        //如果登录三次都失败
        if(loginCount >= 3) {
            if(!verifyCodeActual) {
                $.toast('提交失败！请输入验证码');
                return;
            } else {
                needVerify = true;
            }
        }
        $.ajax({
            url:loginUrl,
            async:false,
            cache:false,
            type:'POST',
            dataType:'json',
            data: {
                username:username,
                password:password,
                verifyCodeActual:verifyCodeActual,
                needVerify:needVerify
            },
            success:function (data) {
                if (data.success) {
                    $.toast("登录成功！");
                    window.location.href = '/o2o/frontend/index';
                } else {
                    $.toast("登录失败！" + data.errMsg);
                    loginCount++;
                    if (loginCount >= 3) {
                        $('#verifyPart').show();
                    }
                }
            }
        });
    });
});