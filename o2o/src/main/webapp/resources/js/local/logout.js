$(function () {
    $('#log-out').click(function () {
        //清除session
        $.ajax({
            url : "/o2o/local/logout",
            type : 'POST',
            async : false,
            cache : false,
            dataType : 'json',
            success : function(data) {
                if (data.success) {
                    //清除成功后退出到登录界面
                    window.location.href = '/o2o/local/login';
                    return false;
                }
            },
            error : function(data, error) {
                alert(error);
            }
        });
    });
});


