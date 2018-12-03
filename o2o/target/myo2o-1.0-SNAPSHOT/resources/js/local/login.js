$(function () {
    //登录验证的controller url
    var loginUrl = '/o2o/local/logincheck';

    //登录次数,三次失败后自动弹出验证码要求输入
    var loginCount = 0;

    $('#submit').click(function () {
        //获取输入的帐号
        var username = $('#username').val();
        //获取输入的密码
        var password = $('#password').val();
        //获取验证码信息
        var verifyCodeActual = $('#j_captcha').val();
        //是否需要验证码验证,默认为false,不需要
        var needVerify = false;
        //如果登录三次都失败
        if(loginCount >= 3) {
            //那么就需要验证码校验了
            if(!verifyCodeActual) {
                $.toast('请输入验证码!');
                return;
            } else {
                needVerify = true;
            }
        }
        //访问后台进行登录验证
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
                    $.toast("登录成功!");
                    window.location.href = '/o2o/frontend/index';
                } else {
                    $.toast("登录失败!" + data.errMsg);
                    loginCount++;
                    if (loginCount >= 3) {
                        $('#verifyPart').show();
                    }
                }
            }
        });
    });
});