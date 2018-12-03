$(function() {
    var registerUrl = '/o2o/local/registerlocal';
    $('#submit').click(function() {
        var username = $('#username').val();
        var password = $('#password').val();
        var confirmPassword = $('#confirmPassword').val();
        var phone = $('#phone').val();
        var email = $('#email').val();
        var name = $('#name').val();
        var thumbnail = $('#small-img')[0].files[0];
        var verifyCodeActual = $('#j_captcha').val();
        if (!verifyCodeActual) {
            $.toast('请输入验证码！');
            return;
        }
        if(password != confirmPassword) {
            $.toast('两次输入的新密码不一致!');
            return;
        }
        var localAuth = {};
        var personInfo = {};
        localAuth.username = username;
        localAuth.password = password;
        personInfo.phone = phone;
        personInfo.email = email;
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
                    $.toast('提交成功！');
                    window.location.href = '/o2o/local/login';
                } else {
                    $.toast('提交失败！');
                    $('#captcha_img').click();
                }
            }
        });
    });
});
