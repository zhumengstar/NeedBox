$(function () {
    //绑定帐号的controller url
    var bindUrl = '/o2o/local/bindlocalauth';
    //从地址栏的URL中获取usertype
    //userType=1则为前端展示系统 其余为店家管理系统
    var userType = getQueryString("userType");

    $('#submit').click(function () {
        //获取输入的帐号和密码
        var username = $('#username').val();
        var password = $('#password').val();
        var verifyCodeActual = $('#j_captcha').val();
        var needVerify = false;
        if(!verifyCodeActual) {
            $.toast('请输入验证码!');
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
                    $.toast("绑定成功!");
                    if(userType == 1) {
                        window.location.href = '/o2o/frontend/index';
                    } else {
                        window.location.href = '/o2o/shopadmin/shoplist';
                    }
                } else {
                    $.toast("提交失败!" + data.errMsg);
                    $('#captcha_img').click();
                }
            }
        });
    });
});