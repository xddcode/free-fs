layui.config({
    base: '../assets/modules/'
}).extend({
    contextMenu: 'contextMenu',
    dtree: 'dtree/dtree'
}).use(['jquery', 'layer', 'element', 'upload', 'laytpl', 'util', 'contextMenu', 'form', 'dtree'], function () {
    var $ = layui.jquery;
    var layer = layui.layer;
    var element = layui.element;
    var upload = layui.upload;
    var laytpl = layui.laytpl;
    var util = layui.util;
    var contextMenu = layui.contextMenu;
    var form = layui.form;
    var dtree = layui.dtree;

    // 渲染文件列表
    function renderList(dirIds) {
        if (!dirIds) {
            dirIds = $('#tvFPId').text();
        }
        layer.load(2);
        $.get('/file', {
            dirIds: dirIds
        }, function (res) {
            layer.closeAll('loading');
            if (res.code === 200) {
                laytpl(fileTpl.innerHTML).render(res.data, function (html) {
                    $('.file-list').html(html);
                });
            } else {
                layer.msg(res.msg, {icon: 2});
            }
        });
    }

    renderList();

    // 右键菜单
    contextMenu.bind('.file-list', [{
        icon: 'layui-icon layui-icon-app',
        name: '新建文件夹',
        click: function (e, event) {
            layer.open({
                type: 1,
                area: ['450px', '200px'],
                title: '新建文件夹',
                content: $("#addFolderModel").html(),
                shade: 0,
                closeBtn: 2,
                success: function (layero, index) {
                    //重置表单
                    $("#addFolderForm")[0].reset();
                    //新增文件夹监听提交
                    form.on('submit(addFolderBtn)', function (data) {
                        var name = $('#folderName').val();
                        var dirIds = $('#tvFPId').text();
                        layer.load(2);
                        $.post('/file/addFolder', {
                            name: name,
                            dirIds: dirIds
                        }, function (res) {
                            layer.closeAll('loading');
                            if (res.code === 200) {
                                layer.closeAll();
                                layer.msg(res.msg, {icon: 1});
                                renderList();
                            } else {
                                layer.msg(res.msg, {icon: 2});
                            }
                        });
                        return false;
                    });
                    //新增文件夹取消按钮事件
                    $('#closeFolderBtn').click(function () {
                        layer.close(index);
                    });
                }
            });
        }
    }]);

    // 上传文件事件
    var proIndex;
    upload.render({
        elem: '#btnUpload',
        url: '/file',
        size: 102400,
        data: {
            dirIds: function () {
                return $('#tvFPId').text();
            }
        },
        choose: function (obj) {
            layer.load(2);
        },
        before: function (obj) {
            proIndex = layer.open({
                type: 1,
                title: "上传进度",
                //closeBtn: 0, //不显示关闭按钮
                skin: 'layui-layer-demo',
                area: ['420px', 'auto'],
                content: '<div style="margin: 10px 20px;"><div class="layui-progress layui-progress-big" lay-showpercent="true" lay-filter="uploadfile"><div class="layui-progress-bar" lay-percent="" id="uploadfile"></div></div><p><span id="uploadfilemsg">正在上传</span></p></div>',
                success: function (layero, index) {
                    layer.setTop(layero); //重点2
                }
            });
            element.render();
        }, progress: function (n, elem) {
            //上传进度回调 获取进度百分比
            var percent = n + '%';
            $("#uploadfile").attr("lay-percent", percent);
            element.render();
        },
        done: function (res, index, upload) {
            layer.closeAll('loading');
            if (res.code !== 200) {
                layer.msg(res.msg, {icon: 2});
                layer.close(proIndex);
                $("#uploadfilemsg").text("上传失败");
            } else {
                layer.msg(res.msg, {icon: 1});
                $("#uploadfilemsg").text("上传完成");
                layer.close(proIndex);
                renderList();
            }
        },
        error: function () {
            layer.closeAll('loading');
            layer.close(proIndex);
            layer.msg('上传失败', {icon: 2});
        },
        accept: 'file'
    });

    // 刷新
    $('#btnRefresh').click(function () {
        renderList();
    });

    var DTree;
    //目录管理
    $('#dirDilaog').click(function () {
        layer.open({
            type: 1,
            area: ['450px', '700px'],
            title: '目录管理',
            content: $('#dirModel').html(),
            shade: [0.6, '#393D49'],
            closeBtn: 2,
            success: function (layero, index) {
                DTree = dtree.render({
                    obj: $(layero).find("#dirTree"),
                    width: "86%",
                    height: "500",
                    method: "get",
                    url: "/file/getTree",
                    record: true,
                    toolbar: true,
                    toolbarShow: [],
                    toolbarWay: "fixed",
                    formatter: {
                        title: function(data) {
                            if (data.isDir){
                                var s = data.title;
                                if (data.children){
                                    s += ' <span style=\'color:blue\'>(' + data.children.length + ')</span>';
                                }
                                return s;
                            }
                        }
                    },
                    toolbarExt: [
                        {
                            toolbarId: "moveNode",
                            icon: "dtree-icon-move-down",
                            title: "移动",
                            handler: function (node) {
                                var param = dtree.getChildParam(DTree, node.nodeId);
                                var ids = [];
                                var nodeMoveids = [];
                                //禁用所选目录的子集目录
                                for (var k in param) {
                                    if (param[k].recordData.isDir){
                                        ids.push(param[k].nodeId);
                                    }
                                    nodeMoveids.push(param[k].nodeId);
                                }
                                if (node.recordData.isDir) {
                                    ids.push(node.nodeId);
                                }
                                nodeMoveids.push(node.nodeId);
                                openMoveModel(node.context,ids,nodeMoveids);
                            }
                        },
                        {
                            toolbarId: "editNode",
                            icon: "dtree-icon-bianji",
                            title: "编辑",
                            handler: function (node) {
                                openEditName(node.context,node.nodeId,node.recordData.isDir);
                            }
                        }, {
                            toolbarId: "delNode",
                            icon: "dtree-icon-roundclose",
                            title: "删除",
                            handler: function (node) {
                                var index = layer.confirm('上级文件夹删除会把所有子文件也一起删除，你可要想好？', {
                                    btn: ['想好了', '再想想']
                                }, function () {
                                    layer.load(2);
                                    $.post('/file/deleteByIds', {id: node.nodeId}, function (res) {
                                        layer.closeAll('loading');
                                        if (res.code === 200) {
                                            layer.msg(res.msg, {icon: 1});
                                            DTree.refreshTree();
                                            getDirs(node.nodeId);
                                        } else {
                                            layer.msg(res.msg, {icon: 2});
                                        }
                                    });
                                }, function () {
                                    layer.close(index);
                                });
                            }
                        }
                    ],
                    dataStyle: "layuiStyle",
                    response: {message: "msg", statusCode: 200},
                    done: function (data, obj, first) {
                        if (first) {
                            $("#searchDirTreeBtn").unbind("click");
                            $("#searchDirTreeBtn").click(function () {
                                var value = $("#searchDirInput").val();
                                if (value) {
                                    var flag = DTree.searchNode(value);
                                    if (!flag) {
                                        layer.msg("该名称节点不存在！", {icon: 5});
                                    }
                                } else {
                                    DTree.menubarMethod().refreshTree();
                                }
                            });
                        }
                    }
                });

                //刷新树
                $('#refreshDirTreeBtn').click(function () {
                    DTree.menubarMethod().refreshTree();
                });

                //目录管理表单取消按钮事件
                $('#dirCloseBtn').click(function () {
                    layer.close(index);
                });
            }
        });
    });

    var mUrl;
    var name;
    var id;
    // 列表点击事件
    $('body').on('click', '.file-list-item', function (e) {
        var isDir = $(this).data('dir');
        name = $(this).data('name');
        id = $(this).data('id');
        mUrl = $(this).data('url');
        $('#copy').attr('data-clipboard-text', mUrl);
        if (isDir) {
            getDirs(id);
        } else {
            var $target = $(this).find('.file-list-img');
            $('#dropdownFile').css({
                'top': $target.offset().top + 90,
                'left': $target.offset().left + 25
            });
            $('#dropdownFile').addClass('dropdown-opened');
            if (e !== void 0) {
                e.preventDefault();
                e.stopPropagation();
            }
        }
    });

    //获取目录
    function getDirs(id){
        $.get('/file/getDirs', {id: id}, function (res) {
            $('#tvFP').text(res.data.dirs === '' ? '/' : res.data.dirs);
            $('#tvFPId').text(res.data.dirIds === '' ? '/' : res.data.dirIds);
            renderList(res.data.dirIds);
        });
    }

    // 返回上级
    $('#btnBack').click(function () {
        var cDir = $('#tvFP').text();
        var cDirId = $('#tvFPId').text();
        if (cDir === '/') {
            layer.msg('已经是根目录', {icon: 2})
        } else {
            cDir = cDir.substring(0, cDir.lastIndexOf('/'));
            cDirId = cDirId.substring(0, cDirId.lastIndexOf('/'));
            if (!cDir) {
                cDir = '/';
                cDirId = '/';
            }
            $('#tvFP').text(cDir);
            $('#tvFPId').text(cDirId);
            renderList(cDirId);
        }
    });

    // 点击空白隐藏下拉框
    $('html').off('click.dropdown').on('click.dropdown', function () {
        $('#copy').attr('data-clipboard-text', '');
        $('#dropdownFile').removeClass('dropdown-opened');
    });

    // 打开
    $('#open').click(function () {
        window.open(mUrl);
    });

    //移动按钮点击事件
    $('#move').click(function () {
        var nodeMoveids = [];
        nodeMoveids.push(id);
        openMoveModel(name, '', nodeMoveids);
    });

    //移动表单弹窗
    function openMoveModel(name, nodeIds, nodeMoveids) {
        layer.open({
            type: 1,
            area: ['450px', '700px'],
            title: '将 ' + name + ' 移动到:',
            content: $('#moveModel').html(),
            shade: [0.6, '#393D49'],
            closeBtn: 2,
            success: function (layero, index) {
                var DirDTree = dtree.render({
                    obj: $(layero).find("#moveDirTree"),
                    width: "86%",
                    height: "500",
                    url: "/file/getDirTree",
                    method: "get",
                    checkbar: true,
                    record: true,
                    checkbarType: "only",
                    dataStyle: "layuiStyle",
                    response: {message: "msg", statusCode: 200},
                    done: function (data, obj, first) {
                        //设置节点禁用
                        if (nodeIds.length > 0) {
                            DirDTree.setDisabledNodes(nodeIds);
                        }
                        if (first) {
                            $("#searchMoveTreeBtn").unbind("click");
                            $("#searchMoveTreeBtn").click(function () {
                                var value = $("#searchMoveInput").val();
                                if (value) {
                                    var flag = DirDTree.searchNode(value);
                                    if (!flag) {
                                        layer.msg("该名称节点不存在！", {icon: 5});
                                    }
                                } else {
                                    DirDTree.menubarMethod().refreshTree();
                                }
                            });
                        }
                    }
                });

                //刷新树
                $('#refreshMoveTreeBtn').click(function () {
                    DirDTree.menubarMethod().refreshTree();
                });

                // 绑定节点点击事件
                dtree.on("node(moveDirTree)", function (obj) {
                    var nodeId = obj.param.nodeId;
                    DirDTree.clickNodeCheckbar(nodeId);
                });

                //文件移动表单提交
                form.on('submit(moveSubmitBtn)', function (data) {
                    var params = dtree.getCheckbarNodesParam("moveDirTree");
                    if (params.length === 0) {
                        layer.msg("请选择一个要移动到的目录！", {icon: 2});
                        return false;
                    }
                    var jsonStr = {
                        "ids": nodeMoveids.join(","),
                        "parentId": params[0].nodeId
                    };
                    layer.load(2);
                    $.post('/file/move', jsonStr, function (res) {
                        layer.closeAll('loading');
                        if (res.code === 200) {
                            layer.close(index);
                            layer.msg(res.msg, {icon: 1});
                            DTree.refreshTree();
                            getDirs(params[0].nodeId);
                        } else {
                            layer.msg(res.msg, {icon: 2});
                        }
                    });
                    return false;
                });

                //移动弹窗取消按钮事件
                $('#moveCloseBtn').click(function () {
                    layer.close(index);
                });
            }
        });
    }

    //下载
    $('#download').click(function () {
        window.location.href = "/file/downLoad?url=" + mUrl;
    });

    //重命名
    $('#rename').click(function () {
        openEditName(name,id,false);
    });

    //打开重命名弹窗
    function openEditName(name,id,isDir) {
        layer.open({
            type: 1,
            area: ['450px', '250px'],
            title: '重命名',
            content: $("#editRenameModel").html(),
            closeBtn: 2,
            success: function (layero, index) {
                //表单赋值
                form.val("editTreeNodeForm", {
                    "id": id,
                    "name": name,
                    "rename": ""
                });
                //重命名表单提交
                form.on('submit(editTreeNodeBtn)', function (data) {
                    layer.load(2);
                    $.post('/file/updateByName', data.field, function (res) {
                        layer.closeAll('loading');
                        if (res.code === 200) {
                            layer.close(index);
                            layer.msg(res.msg, {icon: 1});
                            if (DTree) {
                                DTree.refreshTree();
                            }
                            if (isDir){
                                getDirs(data.field.id);
                            } else{
                                renderList();
                            }
                        } else {
                            layer.msg(res.msg, {icon: 2});
                        }
                    });
                    return false;
                });
                //重命名表单取消按钮事件
                $('#closeRenameBtn').click(function () {
                    layer.close(index);
                });
            }
        });
    }

    // 删除
    $('#del').click(function () {
        layer.confirm('确定要删除此文件吗？', function () {
            layer.load(2);
            $.post('/file/deleteFile', {
                url: mUrl
            }, function (res) {
                layer.closeAll('loading');
                if (res.code === 200) {
                    layer.msg(res.msg, {icon: 1});
                    renderList();
                } else {
                    layer.msg(res.msg, {icon: 2});
                }
            });
        });
    });
    // 复制
    var clipboard = new ClipboardJS('#copy');
    clipboard.on('success', function (e) {
        e.clearSelection();
        layer.msg('文件地址已复制', {icon: 1});
    });
    clipboard.on('error', function (e) {
        layer.msg('复制失败，请手动复制', {icon: 2});
    });

    // 弹窗模式
    $('#btnDilaog').click(function () {
        layer.open({
            type: 2,
            title: '选择文件',
            content: '/fileChoose',
            area: ['600px', '420px'],
            offset: '50px',
            shade: .1,
            fixed: false,
            resize: false,
            end: function () {
                if (typeof(mFsUrls) !== "undefined" && mFsUrls.length > 0) {
                    layer.msg('你选择了：' + JSON.stringify(mFsUrls), {icon: 1});
                    mFsUrls = undefined;
                }
            }
        });
    });

    //
    $('#aMY').on('mouseenter', function () {
        layer.tips('前往码云查看', this, {tips: [1, '#333333'], time: -1});
    }).on('mouseleave', function () {
        layer.closeAll('tips');
    });

    //全屏
    $('#fullScreen').click(function () {
        var v = "layui-icon-screen-full", p = "layui-icon-screen-restore";
        var n = $(this).find("i");
        var s = document.fullscreenElement || document.msFullscreenElement || document.mozFullScreenElement || document.webkitFullscreenElement || false;
        if (s) {
            var r = document.exitFullscreen || document.webkitExitFullscreen || document.mozCancelFullScreen || document.msExitFullscreen;
            if (r) {
                r.call(document)
            } else {
                if (window.ActiveXObject) {
                    var u = new ActiveXObject("WScript.Shell");
                    u && u.SendKeys("{F11}")
                }
            }
            n.addClass(v).removeClass(p)
        } else {
            var o = document.documentElement;
            var q = o.requestFullscreen || o.webkitRequestFullscreen || o.mozRequestFullScreen || o.msRequestFullscreen;
            if (q) {
                q.call(o)
            } else {
                if (window.ActiveXObject) {
                    var u = new ActiveXObject("WScript.Shell");
                    u && u.SendKeys("{F11}")
                }
            }
            n.addClass(p).removeClass(v)
        }
    });

    //退出
    $('#logout').click(function () {
        layer.confirm("确定要退出登录吗？", {title: "温馨提示", skin: "layui-layer-admin"}, function () {
            var url = $("#logout").data('url');
            location.replace(url ? url : "/")
        })
    });

});
