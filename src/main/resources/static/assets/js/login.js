layui.use(['jquery', 'layer', 'form'], function () {
    var $ = layui.jquery;
    var layer = layui.layer;
    var form = layui.form;

    $('.login-wrapper').removeClass('layui-hide');

    // 登录表单提交
    form.on('submit(loginSubmit)', function (obj) {
        obj.field.rememberMe = !!obj.field.remember;
        layer.load(2);
        $.post('login', obj.field, function (res) {
            if (200 === res.code) {
                layer.msg('登录成功', {icon: 1, time: 1500}, function () {
                    location.replace('./');
                });
            } else {
                layer.closeAll('loading');
                layer.msg(res.msg, {icon: 5});
                $('img.login-captcha').trigger('click');
            }
        }, 'JSON');
        return false;
    });

    // 注册表单提交
    form.on('submit(regSubmit)', function (obj) {
        layer.load(2);
        $.post('reg', obj.field, function (res) {
            if (200 === res.code) {
                layer.msg(res.msg, {icon: 1, time: 1500}, function () {
                    location.replace('/login');
                });
            } else {
                layer.closeAll('loading');
                layer.msg(res.msg, {icon: 5});
                $('img.login-captcha').trigger('click');
            }
        }, 'JSON');
        return false;
    });

    /* 图形验证码 */
    var captchaUrl = '/assets/captcha';
    $('img.login-captcha').click(function () {
        this.src = captchaUrl + '?t=' + (new Date).getTime();
    }).trigger('click');

});
