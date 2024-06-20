layui.use(['jquery', 'layer', 'form'], function () {
    var $ = layui.jquery;
    var layer = layui.layer;
    var form = layui.form;

    $('.login-wrapper').removeClass('layui-hide');

    // 校验两次密码是否一致
    form.verify({
        password: function (value) {
            // 6到12位
            if (!/^[\S]{6,12}$/.test(value)) {
                return '密码必须6到12位，不能包含空格';
            }
        },
        confirmPassword: function (value) {
            if ($('input[name=password]').val() !== value)
                return '两次密码输入不一致！';
        }
    });

    // 登录表单提交
    form.on('submit(loginSubmit)', function (obj) {
        obj.field.rememberMe = !!obj.field.remember;
        layer.load(2);
        $.ajax({
            url: ctxPath + 'login',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(obj.field),
            dataType: 'json',
            success: function (res) {
                if (200 === res.code) {
                    layer.msg('登录成功', {icon: 1, time: 1000}, function () {
                        location.replace('./');
                    });
                } else {
                    layer.closeAll('loading');
                    layer.msg(res.msg, {icon: 5, time: 500});
                }
            },
            error: function (xhr, status, error) {
                // 处理请求失败的情况
                console.error('请求失败:', status, error);
                layer.closeAll('loading');
                layer.msg('请求失败，请稍后重试', {icon: 5, time: 500});
            }
        });
        return false;
    });

    // 注册表单提交
    form.on('submit(regSubmit)', function (obj) {
        layer.load(2);
        $.post(ctxPath + 'register', obj.field, function (res) {
            if (200 === res.code) {
                layer.msg(res.msg, {icon: 1, time: 1500}, function () {
                    location.replace('/login');
                });
            } else {
                layer.closeAll('loading');
                layer.msg(res.msg, {icon: 5});
            }
        }, 'JSON');
        return false;
    });

    // 完善密码表单提交
    form.on('submit(informationPassSubmit)', function (obj) {
        layer.load(2);
        obj.field.avatar = $('#avatar').attr('src');
        $.post(ctxPath + 'informationPass', obj.field, function (res) {
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
