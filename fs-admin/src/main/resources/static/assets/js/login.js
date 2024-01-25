layui.use(['jquery', 'layer', 'form'], function () {
    var $ = layui.jquery;
    var layer = layui.layer;
    var form = layui.form;

    $('.login-wrapper').removeClass('layui-hide');

    // 校验两次密码是否一致
    form.verify({
        confirmPass: function (value) {
            if ($('input[name=password]').val() !== value)
                return '两次密码输入不一致！';
        }
    });

    // 登录表单提交
    form.on('submit(loginSubmit)', function (obj) {
        obj.field.rememberMe = !!obj.field.remember;
        layer.load(2);
        $.ajax({
            url: 'login',
            type: 'POST',
            data: JSON.stringify(obj.field),
            contentType: 'application/json',
            dataType: 'json',
            success: function (res) {
                if (200 === res.code) {
                    layer.msg('登录成功', {icon: 1, time: 1500}, function () {
                        location.replace('./');
                    });
                } else {
                    layer.closeAll('loading');
                    layer.msg(res.msg, {icon: 5, time: 500});
                    $('img.login-captcha').trigger('click');
                }
            }
        });
        return false;
    });

    // 注册表单提交
    form.on('submit(regSubmit)', function (obj) {
        layer.load(2);
        $.ajax({
            url: 'reg',
            type: 'POST',
            data: JSON.stringify(obj.field),
            contentType: 'application/json',
            dataType: 'json',
            success: function (res) {
                if (200 === res.code) {
                    layer.msg(res.msg, {icon: 1, time: 1500}, function () {
                        location.replace('/login');
                    });
                } else {
                    layer.closeAll('loading');
                    layer.msg(res.msg, {icon: 5});
                    $('img.login-captcha').trigger('click');
                }
            }
        });
        return false;
    });

    /* 图形验证码 */
    var captchaUrl = '/assets/captcha';
    $('img.login-captcha').click(function () {
        this.src = captchaUrl + '?t=' + (new Date).getTime();
    }).trigger('click');

    // 完善密码表单提交
    form.on('submit(informationPassSubmit)', function (obj) {
        layer.load(2);
        obj.field.avatar = $('#avatar').attr('src');
        console.log(obj.field);
        $.post('/informationPass', obj.field, function (res) {
            if (200 === res.code) {
                layer.msg(res.msg, {icon: 1, time: 1500}, function () {
                    location.replace('/index');
                });
            } else {
                layer.closeAll('loading');
                layer.msg(res.msg, {icon: 5});
            }
        }, 'JSON');
        return false;
    });

});
