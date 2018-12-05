$(function () {
    //绑定帐号的controller url
    var bindUrl = '/o2o/local/bindlocalauth';

    $('#submit').click(function () {
        //获取输入的帐号和密码
        var username = $('#username').val();
        var password = $('#password').val();
        var confirmPassword = $('#confirmPassword').val();
        var verifyCodeActual = $('#j_captcha').val();
        if(!verifyCodeActual) {
            $.toast('提交失败！请输入验证码');
            return;
        }
        if(password != confirmPassword) {
            $.toast('提交失败！两次输入的密码不一致');
            $('#captcha_img').click();
            return;
        }
        //访问后台,绑定账号
        $.ajax({
            url:bindUrl,
            async:false,
            cache:false,
            type:'POST',
            dataType:'json',
            data: {
                username:username,
                password:password,
                verifyCodeActual:verifyCodeActual
            },
            success:function (data) {
                if(data.success) {
                    $.toast("绑定成功！");
                    window.location.href = '/o2o/frontend/index';
                } else {
                    $.toast("提交失败！" + data.errMsg);
                    $('#captcha_img').click();
                }
            }
        });
    });
});