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
    var xhrOnProgress = function (fun) {
        xhrOnProgress.onprogress = fun; //绑定监听
        //使用闭包实现监听绑
        return function () {
            //通过$.ajaxSettings.xhr();获得XMLHttpRequest对象
            var xhr = $.ajaxSettings.xhr();
            //判断监听函数是否为函数
            if (typeof xhrOnProgress.onprogress !== 'function')
                return xhr;
            //如果有监听函数并且xhr对象支持绑定时就把监听函数绑定上去
            if (xhrOnProgress.onprogress && xhr.upload) {
                xhr.upload.onprogress = xhrOnProgress.onprogress;
            }
            return xhr;
        }
    };
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
        accept: 'file',
        multiple: true,
        size: 1024*20,
        data: {
            dirIds: function () {
                return $('#tvFPId').text();
            }
        },
        choose: function (obj) {
            layer.load(2);
        },
        before: function (obj) {
            layer.load(2);
   /*         proIndex = layer.open({
                type: 1,
                title: "上传进度",
                //closeBtn: 0, //不显示关闭按钮
                skin: 'layui-layer-demo',
                area: ['420px', 'auto'],
                content: '' +
                    '<div style="margin: 10px 20px;">' +
                    '   <div class="layui-progress layui-progress-big" lay-showpercent="true" lay-filter="uploadfile">' +
                    '       <div class="layui-progress-bar" lay-percent="0%" id="uploadfile"></div>' +
                    '   </div>' +
                    '   <p>' +
                    '       <span id="uploadfilemsg">正在上传</span>' +
                    '   </p>' +
                    '</div>',
                success: function (layero, index) {
                    layer.setTop(layero); //重点2
                }
            });
            element.render();*/
        }, /*progress: function (n, elem) {
            //查询上传进度
            var intervalId = setInterval(function () {
                $.get('/file/percent', {}, function (data) {
                    var percent = data;
                    if (percent >= 100) {
                        clearInterval(intervalId);
                        percent = 100;
                    }
                    $("#uploadfile").attr("lay-percent", data + '%');
                    element.render();
                });
            }, 100);
        },*/
        done: function (res, index, upload) {
            layer.closeAll('loading');
            if (res.code !== 200) {
                layer.msg(res.msg, {icon: 2});
               // layer.close(proIndex);
               // $("#uploadfilemsg").text("上传失败");
            } else {
                layer.msg(res.msg, {icon: 1});
                //$("#uploadfilemsg").text("上传完成");
               // layer.close(proIndex);
                renderList();
                //重置进度条
                //resetPercent();
            }
        },
        error: function () {
            layer.closeAll('loading');
            layer.close(proIndex);
            layer.msg('上传失败', {icon: 2});
        }
    });


    //重置进度条
    function resetPercent(){
        $.get('/file/percent/reset', {}, function (res) {
            console.log("重置进度条");
        });
    }


    $('#btnUploadSharding').click(function () {
        layer.open({
            type: 1,
            area: ['1000px', '600px'],
            title: '分片上传',
            content: $('#uploadShardModel').html(),
            shade: [0.6, '#393D49'],
            closeBtn: 2,
            success: function (layero, index0) {
                var uploadListView = $('#uploadList')
                    ,uploadListIns = upload.render({
                    elem: '#uploadShardList'
                    ,url: '/file/uploadSharding'
                    ,accept: 'file'
                    ,multiple: false
                    ,auto: false
                    ,bindAction: '#testListAction'
                    ,data: {
                        dirIds: function () {
                            return $('#tvFPId').text();
                        }
                    }
                    , xhr: xhrOnProgress,
                    progress: function (value) {
                        element.progress('uploadList', value + '%')//设置页面进度条
                    },
                    xhr: function (index, e) {
                        //查询上传进度
                        var intervalId = setInterval(function () {
                            $.get('/file/percent', {}, function (data) {
                                var percent = data;
                                if (percent >= 100) {
                                    clearInterval(intervalId);
                                    percent = 100;
                                }
                                element.progress('progress_' + index + '', percent + '%');
                            });
                        }, 100);
                    },
                    choose: function(obj){
                        var files = this.files = obj.pushFile();
                        //读取本地文件
                        obj.preview(function(index, file, result){
                            console.log(index);
                            var tr = $(['<tr id="upload-'+ index +'">'
                                ,'<td>'+ file.name +'</td>'
                                ,'<td>'+ (file.size/1024).toFixed(1) +'kb</td>'
                                ,'<td>等待上传</td>'
                                ,'<td>' +
                                '   <div class="layui-progress" lay-showPercent="true" lay-filter="progress_'+index+'">'+
                                '       <div class="layui-progress-bar" lay-percent="0%"></div>' +
                                '   </div>' +
                                ' </td>'
                                ,'<td>'
                                ,'<button class="layui-btn layui-btn-xs demo-reload layui-hide">重传</button>'
                                ,'<button class="layui-btn layui-btn-xs layui-btn-danger demo-delete">删除</button>'
                                ,'</td>'
                                ,'</tr>'].join(''));

                            //单个重传
                            tr.find('.demo-reload').on('click', function(){
                                obj.upload(index, file);
                            });

                            //删除
                            tr.find('.demo-delete').on('click', function(){
                                delete files[index]; //删除对应的文件
                                tr.remove();
                                uploadListIns.config.elem.next()[0].value = ''; //清空 input file 值，以免删除后出现同名文件不可选
                            });
                            uploadListView.append(tr);
                            element.render();
                        });
                    },
                    done: function(res, index, upload){
                        if (res.code === 200) { //上传成功
                            var tr = uploadListView.find('tr#upload-'+ index)
                                ,tds = tr.children();
                            tds.eq(2).html('<span style="color: #5FB878;">上传成功</span>');
                            tds.eq(4).html(''); //清空操作
                            //重置进度条
                            resetPercent();
                            renderList();
                            layer.close(index0);
                            return delete this.files[index]; //删除文件队列已经上传成功的文件
                        }
                        this.error(res.msg, index, upload);
                    },
                    error: function(msg,index, upload){
                        var tr = uploadListView.find('tr#upload-'+ index)
                            ,tds = tr.children();
                        tds.eq(2).html('<span style="color: #FF5722;">上传失败</span>');
                        tds.eq(4).find('.demo-reload').removeClass('layui-hide'); //显示重传
                        layer.msg(msg, {icon: 2});
                    }
                });
            }
        })
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
