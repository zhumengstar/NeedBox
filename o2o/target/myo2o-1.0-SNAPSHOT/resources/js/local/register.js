$(function() {
    var registerUrl = '/o2o/local/registerlocal';
    $('#submit').click(function() {
        var username = $('#username').val();
        var password = $('#password').val();
        var confirmPassword = $('#confirmPassword').val();
        var name = $('#name').val();
        var thumbnail = $('#small-img')[0].files[0];
        var verifyCodeActual = $('#j_captcha').val();
        if (!verifyCodeActual) {
            $.toast('提交失败！请输入验证码');
            return;
        }
        if(password != confirmPassword) {
            $.toast('提交失败！两次输入的密码不一致');
            $('#captcha_img').click();
            return;
        }
        var localAuth = {};
        var personInfo = {};
        localAuth.username = username;
        localAuth.password = password;
        personInfo.name = name;
        localAuth.personInfo = personInfo;
        console.log(thumbnail);

        var formData = new FormData();
        formData.append('thumbnail', thumbnail);
        formData.append('localAuthStr', JSON.stringify(localAuth));
        formData.append("verifyCodeActual", verifyCodeActual);
        $.ajax({
            url : registerUrl,
            type : 'POST',
            data : formData,
            contentType : false,
            processData : false,
            cache : false,
            success : function(data) {
                if (data.success) {
                    $.toast('注册成功！');
                    window.location.href = '/o2o/local/login';
                } else {
                    $.toast('注册失败！' + data.errMsg);
                    $('#captcha_img').click();
                }
            }
        });
    });
});
