layui.define(['element'], function (exports) {
    let element = layui.element;
    const $ = layui.jquery;

    let MOD_NAME = 'tabrightmenu';
    let RIGHTMENUMOD = function () {
        this.v = '1.0.0';
        this.author = 'TangHanF';
        this.email = 'guofu_gh@163.com';
        this.filter = '';//Tab选项卡的事件过滤

    };
    String.prototype.format = function () {
        if (arguments.length == 0) return this;
        let param = arguments[0];
        let s = this;
        if (typeof(param) == 'object') {
            for (var key in param) s = s.replace(new RegExp("\\{" + key + "\\}", "g"), param[key]);
            return s;
        } else {
            for (var i = 0; i < arguments.length; i++) s = s.replace(new RegExp("\\{" + i + "\\}", "g"), arguments[i]);
            return s;
        }
    }

    function createStyle(ulClassName) {
        let style = '.{name} {position: absolute;width: 110px;z-index: 9999;display: none;background-color: #fff;padding: 2px;color: #333;border: 1px solid #eee;border-radius: 2px;cursor: pointer;}.{name} li {text-align: center;display: block;height: 30px;line-height: 32px;}.{name} li:hover {background-color: #666;color: #fff;}'
            .format({name: ulClassName});
        return style;
    }

    /**
     * 初始化
     */
    RIGHTMENUMOD.prototype.render = function (opt) {
        createStyle();

        if (!opt.container) {
            console.error("[ERROR]使用rightmenu组件需要制定'container'属性！");
            return;
        }
        if (!opt.filter) {
            console.error("[ERROR]使用rightmenu组件需要制定'filter'属性！");
            return;
        }
        this.filter = opt.filter;

        let defaultNavArr = [
            {eventName: 'closethis', title: '关闭当前'},
            {eventName: 'closeall', title: '关闭所有'},
            {eventName: 'closeothers', title: '关闭其它'},
            {eventName: 'closeleft', title: '关闭左侧所有'},
            {eventName: 'closeright', title: '关闭右侧所有'},
            {eventName: 'refresh', title: '刷新当前页'},
        ];
        opt = opt || {};
        opt.navArr = opt.navArr || defaultNavArr;
        CreateRightMenu(opt);
    };


    /**
     * 创建右键菜单项目
     * @param rightMenuConfig
     * @constructor
     */
    function CreateRightMenu(rightMenuConfig) {
        // 使用"filter"属性作为css样式名称
        $("<style></style>").text(createStyle(rightMenuConfig.filter)).appendTo($("head"));
        let li = '';
        $.each(rightMenuConfig.navArr, function (index, conf) {
            // li += '<li data-type="' + conf.eventName + '"><i class="layui-icon layui-icon-face-smile"></i> ' + conf.title + '</li>';
            li += '<li data-type="{eventName}"><i class="layui-icon {icon}"></i>{title}</li>'
                .format({eventName:conf.eventName,icon:conf.icon?conf.icon:"",title:conf.title});
        })
        let tmpHtml = '<ul class="{className}">{liStr} </ul>'.format({liStr: li, className: rightMenuConfig.filter})
        $(rightMenuConfig.container).after(tmpHtml);
        _CustomRightClick(rightMenuConfig);
    }


    /**
     * 绑定右键菜单
     * @constructor
     */
    function _CustomRightClick(rightMenuConfig) {

        //屏蔽Tab右键
        $("#" + rightmenuObj.filter + ' li').on('contextmenu', function () {
            return false;
        })
        $('#' + rightmenuObj.filter + ',' + rightmenuObj.filter + ' li').click(function () {
            $('.' + rightMenuConfig.filter).hide();
        });
        console.log("[事件绑定，元素]" + rightmenuObj.filter + ' li')
        $("#" + rightmenuObj.filter + ' li').on('contextmenu', function (e) {
            let popupmenu = $("." + rightMenuConfig.filter);
            let leftValue = ($(document).width() - e.clientX) < popupmenu.width() ? (e.clientX - popupmenu.width()) : e.clientX;
            let topValue = ($(document).height() - e.clientY) < popupmenu.height() ? (e.clientY - popupmenu.height()) : e.clientY;
            popupmenu.css({left: leftValue, top: topValue}).show();
            return false;
        });
        // 点击空白处隐藏弹出菜单
        $(document).click(function (event) {
            event.stopPropagation();
            $("." + rightMenuConfig.filter).hide();
        });


        /**
         * 注册tab右键菜单点击事件
         */
        console.log("[注册tab右键菜单点击事件]" + '.' + rightMenuConfig.filter + ' li');
        $('.' + rightMenuConfig.filter + ' li').click(function () {
            let currentActiveTabID = $("#" + rightMenuConfig.filter + ">li[class='layui-this']").attr('lay-id');// 获取当前激活的选项卡ID
            let tabtitle = $("#" + rightMenuConfig.filter + " li");
            let allTabIDArr = [];
            $.each(tabtitle, function (i) {
                allTabIDArr[i] = $(this).attr("lay-id");
            })

            // 事件处理
            switch ($(this).attr("data-type")) {
                case "closethis"://关闭当前，如果开始了tab可关闭，实际意义不大
                    tabDelete(currentActiveTabID, rightMenuConfig.filter);
                    break;
                case "closeall"://关闭所有
                    tabDeleteAll(allTabIDArr, rightMenuConfig.filter);
                    break;
                case "closeothers"://关闭非当前
                    $.each(allTabIDArr, function (i) {
                        let tmpTabID = allTabIDArr[i];
                        if (currentActiveTabID != tmpTabID)
                            tabDelete(tmpTabID, rightMenuConfig.filter);
                    })
                    break;
                case "closeleft"://关闭左侧全部
                    let indexCloseleft = allTabIDArr.indexOf(currentActiveTabID);
                    tabDeleteAll(allTabIDArr.slice(0, indexCloseleft));
                    break;
                case "closeright"://关闭右侧全部
                    let indexCloseright = allTabIDArr.indexOf(currentActiveTabID);
                    tabDeleteAll(allTabIDArr.slice(indexCloseright + 1));
                    break;
                case "refresh":
                    // 重新加载iFrame，实现更新。注意：如果你不是使用的iFrame则无效，具体刷新实现自行处理
                    // document.getElementById(currentActiveTabID).contentWindow.location.reload(true);
                    refreshiFrame();
                    break;
                default:
                    let currentTitle = $("#" + rightMenuConfig.filter + ">li[class='layui-this']").text();
                    rightMenuConfig.registMethod[$(this).attr("data-type")](currentActiveTabID, currentTitle, rightMenuConfig.container, $(this)[0]);

            }
            $('.rightmenu').hide();
        })
    }

    let tabDelete = function (id, currentNav) {
        console.log("删除的TabID：" + id + ",所属组：" + currentNav);
        element.tabDelete(currentNav, id);//删除
    }
    let tabDeleteAll = function (ids, currentNav) {
        $.each(ids, function (i, item) {
            element.tabDelete(currentNav, item);
        })
    }
    /**
     * 重新加载iFrame，实现更新
     * @returns {boolean}
     */
    let refreshiFrame = function () {
        let $curFrame = $('iframe:visible');
        $curFrame.attr("src", $curFrame.attr("src"));
        return false;
    }

    let rightmenuObj = new RIGHTMENUMOD();
    exports(MOD_NAME, rightmenuObj);
})