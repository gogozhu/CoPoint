<%--
  Created by IntelliJ IDEA.
  User: YYH
  Date: 2018/6/13
  Time: 17:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Etherpad jQuery Plugin Example</title>


    <link rel='stylesheet' href='font-awesome-4.3.0/css/font-awesome.min.css'>

    <!-- 引入外部bootstrap的css文件(压缩版) -->
    <link rel="stylesheet" href="bootstrap-4.1.1-dist/css/bootstrap.min.css">
    <%--jquery--%>
    <script src="http://code.jquery.com/jquery-latest.min.js" type="text/javascript"></script>
    <!-- NO.2 再引入js文件(压缩版) -->

    <script src="bootstrap-4.1.1-dist/js/bootstrap.min.js"></script>
<%--etherpad--%>
    <script src="etherpad-lite-jquery-plugin/js/etherpad.js"></script>
    <%--zTree--%>
    <link rel="stylesheet" href="zTree_v3/css/demo.css" type="text/css">
    <%--<link rel="stylesheet" href="/res/zTree_v3/css/zTreeStyle/zTreeStyle.css" type="text/css">--%>
    <link rel="stylesheet" href="zTree_v3/css/metroStyle/metroStyle.css" type="text/css">
    <%--<link rel="stylesheet" href="/res/zTree_v3/css/awesomeStyle/awesomeStyle.css" type="text/css">--%>
    <%--<script type="text/javascript" src="/res/zTree_v3/js/jquery-1.4.4.min.js"></script>--%>
    <script type="text/javascript" src="zTree_v3/js/jquery.ztree.core.js"></script>
    <script type="text/javascript" src="zTree_v3/js/jquery.ztree.excheck.js"></script>
    <script type="text/javascript" src="zTree_v3/js/jquery.ztree.exedit.js"></script>
    <script src="jQuery-BootPopup/bootpopup.min.js"></script>
    <script src="https://cdn.bootcss.com/sockjs-client/1.1.2/sockjs.min.js"></script>
    <script src="https://cdn.bootcss.com/stomp.js/2.3.3/stomp.min.js"></script>
    <script src="http://www.webrtc-experiment.com/RecordRTC.js"></script>
    <SCRIPT type="text/javascript">
        var pad_params = {
            'padId':"",
            'host'             : '${etherpadHost}',          // the host and port of the Etherpad instance, by default the foundation will host your pads for you
            'baseUrl'          : '/p/',                      // The base URL of the pads
            'showControls'     : 'true',                      // If you want to show controls IE bold, italic, etc.
            'showChat'         : 'false',                      // If you want to show the chat button or not
            'showLineNumbers'  : 'false',                      // If you want to show the line numbers or not
            'userName'         : 'userName',                  // The username you want to pass to the pad
            'useMonospaceFont' : 'false',                      // Use monospaced fonts
            'noColors'         : 'false',                      // Disable background colors on author text
            'userColor'        : 'false',                      // The background color of this authors text in hex format IE #000
            'hideQRCode'       : 'true',                      // Hide QR code
            'alwaysShowChat'   : 'false',                      // Always show the chat on the UI
            'width'            : '100%',                        // The width of the embedded IFrame
            'height'           : '100%',                        // The height of the embedded IFrame
            'border'           : '0',                          // The width of the border (make sure to append px to a numerical value)
            'borderStyle'      : 'solid',                     // The CSS style of the border [none, dotted, dashed, solid, double, groove, ridge, inset, outset]
            'plugins'          : '{}',                         // The options related to the plugins, not to the basic Etherpad configuration
            'rtl'              : 'false'                       // Show right to left text}
        };
        var setting = {
            view: {
                addHoverDom: addHoverDom,
                removeHoverDom: removeHoverDom,
                selectedMulti: false
            },
            edit: {
                enable: true,
                editNameSelectAll: true,
                showRemoveBtn: showRemoveBtn,
                showRenameBtn: showRenameBtn
            },
            data: {
                simpleData: {
                    enable: true
                }
            },
            callback: {
                beforeDrag: beforeDrag,
                beforeEditName: beforeEditName,
                beforeRemove: beforeRemove,
                beforeRename: beforeRename,
                onRemove: onRemove,
                onRename: onRename,
                onDblClick: onDblClick
            }
        };
        var sessionID = "null";
        var socket_sessionID = "null";
        var log, className = "dark";
        function beforeDrag(treeId, treeNodes) {
            return false;
        }
        function beforeEditName(treeId, treeNode) {
            className = (className === "dark" ? "":"dark");
            showLog("[ "+getTime()+" beforeEditName ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.name);
            var zTree = $.fn.zTree.getZTreeObj("treeDemo");
            zTree.selectNode(treeNode);
            setTimeout(function() {
                if (confirm("Start node '" + treeNode.name + "' editorial status?")) {
                    setTimeout(function() {
                        zTree.editName(treeNode);
                    }, 0);
                }
            }, 0);
            return false;
        }
        function beforeRemove(treeId, treeNode) {
            className = (className === "dark" ? "":"dark");
            showLog("[ "+getTime()+" beforeRemove ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.name);
            var zTree = $.fn.zTree.getZTreeObj("treeDemo");
            zTree.selectNode(treeNode);
            return confirm("Confirm delete node '" + treeNode.name + "' it?");
        }
        function onRemove(e, treeId, treeNode) {
            showLog("[ "+getTime()+" onRemove ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.name);
        }
        function beforeRename(treeId, treeNode, newName, isCancel) {
            className = (className === "dark" ? "":"dark");
            showLog((isCancel ? "<span style='color:red'>":"") + "[ "+getTime()+" beforeRename ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.name + (isCancel ? "</span>":""));
            if (newName.length == 0) {
                setTimeout(function() {
                    var zTree = $.fn.zTree.getZTreeObj("treeDemo");
                    zTree.cancelEditName();
                    alert("Node name can not be empty.");
                }, 0);
                return false;
            }
            return true;
        }
        function onRename(e, treeId, treeNode, isCancel) {
            showLog((isCancel ? "<span style='color:red'>":"") + "[ "+getTime()+" onRename ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.name + (isCancel ? "</span>":""));
        }
        function onDblClick(e, treeId, treeNode, isCancel) {
            var temp = treeNode.name.split(".");
            if (temp[temp.length-1] === "cp") {
                console.log(treeNode ? treeNode.tId + ", " + treeNode.getParentNode().name + "--" + treeNode.name : "isRoot");
                var temp = treeNode.name.split(".");
                temp.pop();
                const body = {
                    'preSessionID': sessionID,
                    'groupName': treeNode.getParentNode().name,
                    'padName': temp.join(".")
                };
                $.get('${pageContext.request.contextPath}/getEtherpad',body,function(response,status,xhr){
                    sessionID = response.sessionID;
                    SetCookie("sessionID="+sessionID);
                    console.log(document.cookie);
                    pad_params.padId = response.padId;
                    pad_params.userName = getCookie("userName");
                    $('#examplePadBasic').pad(pad_params);
                });
            }
        }
        function showRemoveBtn(treeId, treeNode) {
            if(treeNode.name==="Myself" || treeNode.name==="Other" )  {
                return false;
            }
            else {
                return true;
            }
        }
        function showRenameBtn(treeId, treeNode) {
            //            m
            if(treeNode.name==="Myself" || treeNode.name==="Other" )
            {
                return false;
            }else
            {
                return true;
            }
        }
        function showLog(str) {
            if (!log) log = $("#log");
            log.append("<li class='"+className+"'>"+str+"</li>");
            if(log.children("li").length > 8) {
                log.get(0).removeChild(log.children("li")[0]);
            }
        }
        function getTime() {
            var now= new Date(),
                h=now.getHours(),
                m=now.getMinutes(),
                s=now.getSeconds(),
                ms=now.getMilliseconds();
            return (h+":"+m+":"+s+ " " +ms);
        }
        function getCookie(name){
            var strcookie = document.cookie;//获取cookie字符串
            var arrcookie = strcookie.split("; ");//分割
//遍历匹配
            for ( var i = 0; i < arrcookie.length; i++) {
                var arr = arrcookie[i].split("=");
                if (arr[0] == name){
                    return arr[1];
                }
            }
            return "";
        }
        var newCount = 1;
        function addHoverDom(treeId, treeNode) {
            // m
            var s = treeNode.name.split('.')
            if (s[s.length-1] === "cp" || treeNode.name==="Other") {
                return
            }
            if (s[s.length-1] !== "cp" && treeNode.name !== "Myself" && treeNode.name !== "Other") {
                var aObj = $("#" + treeNode.tId + "_a");
                if ($("#shareBtn_"+treeNode.tId).length>0) return;
                var editStr = "<span class='button share' id='shareBtn_" + treeNode.tId
                    + "' title='share' onfocus='this.blur();'></span>";
                aObj.append(editStr);
                var btn = $("#shareBtn_"+treeNode.tId);
                if (btn) btn.bind("click", function(){
//                    var input = document.getElementById("clipBoardContent");
//                    input.value = treeNode.name;
//                    input.select();
//                    document.execCommand("copy");
                    bootpopup.alert(treeNode.name, "请分享已下文本给指定好友");
                });
            }
            var sObj = $("#" + treeNode.tId + "_span");
            if (treeNode.editNameFlag || $("#addBtn_"+treeNode.tId).length>0) return;
            var addStr = "<span class='button add' id='addBtn_" + treeNode.tId
                + "' title='add node' onfocus='this.blur();'></span>";
            sObj.after(addStr);
            var btn = $("#addBtn_"+treeNode.tId);
            if (btn) btn.bind("click", function(){
                var zTree = $.fn.zTree.getZTreeObj("treeDemo");
                //create group
                if(treeNode.name==="Myself")
                {
                    bootpopup({
                        title: "New Group",
                        size: "large",
                        showclose: false,
                        size_labels: "col-sm-2",
                        size_inputs: "col-sm-10",
                        content: [
                            { p: {text: "Please enter Group Name:"}},
                            { input: {type: "text",  name: "groupName", id: "groupName", placeholder: "Description for image", value: "groupName"}},
                        ],
                        before: function(window) { },
                        dismiss: function(event) {  },
                        cancel: function(data, array, event) {  },
                        ok: function(data, array, event) {
                            $.ajax({
                                url: "${pageContext.request.contextPath}/group",
                                type : "put",    // 此处发送的是PUT请求
                                data : {
                                    authorName : getCookie("userName"),
                                    groupName : data['groupName'] + "@"+getCookie("userName")
                                },
                                success : function(data){
                                    console.log(0);
                                },
                                dataType : "text",
                                error : function(data){
                                    console.log(1);
                                }
                            });

                            zTree.addNodes(treeNode, {id:(100 + newCount), pId:treeNode.id, name:data['groupName'] + "@"+getCookie("userName")});
                            newCount++
                        },
                        complete: function() {  },
                    });
                }else
                {
                    bootpopup({
                        title: "New Pad",
                        size: "large",
                        showclose: false,
                        size_labels: "col-sm-2",
                        size_inputs: "col-sm-10",
                        content: [
                            { p: {text: "Please enter Pad Name:"}},
                            { input: {type: "text",  name: "groupPad", id: "groupPad", placeholder: "Description for image", value: "groupPad"}},
                        ],
                        before: function(window) { },
                        dismiss: function(event) {  },
                        cancel: function(data, array, event) {  },
                        ok: function(data, array, event) {
                            $.ajax({
                                url: "${pageContext.request.contextPath}/pad",
                                type : "put",    // 此处发送的是PUT请求
                                data : {
                                    groupName : treeNode.name,
                                    groupPad : data['groupPad'],
                                },
                                success : function(response){
                                    console.log(response);
                                    if (response === "ok") {
                                        zTree.addNodes(treeNode, {id:(100 + newCount), pId:treeNode.id, name:data['groupPad']+".cp"});
                                        newCount++
                                    }
                                },
                                dataType : "text",
                                error : function(data){
                                    console.log(1);
                                }
                            })
                        },
                        complete: function() {  },
                    });
                }
                return false;
            });
        }
        function removeHoverDom(treeId, treeNode) {
            var s = treeNode.name.split('.');
            if (s[s.length-1] === "cp" || treeNode.name==="Other") {
                return
            }
            if (s[s.length-1] !== "cp" && treeNode.name !== "Myself" && treeNode.name !== "Other") {
                $("#shareBtn_"+treeNode.tId).unbind().remove();
            }
            $("#addBtn_"+treeNode.tId).unbind().remove();
        }
        function selectAll() {
            var zTree = $.fn.zTree.getZTreeObj("treeDemo");
            zTree.setting.edit.editNameSelectAll =  $("#selectAll").attr("checked");
        }
        var zNodes;
        $(function(){
            $.ajax({
                async:false,
                cache:false,
                type:'POST',
                //dataType:"String",
                url:'${pageContext.request.contextPath}/TestZTree',
                success:function(data){
                    zNodes = data;
                },error:function(){
                    alert("请求失败");
                }
            });
        });
        //        https://blog.csdn.net/BMLovey/article/details/50762221
        $(document).ready(function(){
            treeNodes = eval("(" + zNodes + ")");  //将string类型转换成json
            console.log(treeNodes)
            $.fn.zTree.init($("#treeDemo"), setting, treeNodes);
//            $("#treeDiv").height($(window).height());//根据网页可视高度设置树的高度,配合 treeDemo的style一起使用
        });

        function access2group() {
            var groupName = document.getElementById("group_url").value;
            const body = {
                'groupName': groupName,
            };
            $.get('${pageContext.request.contextPath}/access2group',body,function(response,status,xhr){
                alert(response);
                $.ajax({
                    async:false,
                    cache:false,
                    type:'POST',
                    //dataType:"String",
                    url:'${pageContext.request.contextPath}/TestZTree',
                    success:function(data){
                        treeNodes = eval("(" + data + ")");  //将string类型转换成json
                        console.log(treeNodes)
                        $.fn.zTree.init($("#treeDemo"), setting, treeNodes);
                    },error:function(){
                        alert("请求失败");
                    }
                });
            });
        }
    </SCRIPT>
    <%--http://www.divcss5.com/wenji/w588.shtml--%>
    <style>
        .div-nav{ float:top;width:99%;height:50px;border:0px solid #000 }
        .ztree_wrap{ margin-top: 1px;width:99%;height:92%;border:0px solid #000}
        .zTreebg{ float: left;width:20%;height:99%;border:0px solid #F00}
        .div-pad{ float:right;width:79%;height:99%;border:1px solid whitesmoke;}
        .li_style {list-style-type:none;}
    </style>
</head>

<body>
<div class="div-nav">
    <nav class="navbar navbar-expand-lg  navbar-light bg-light">
        <span class="navbar-brand mb-0 h1">Copoint</span>
        <form class="form-inline">
            <button class="btn btn-light" type="button" onclick="access2group()"> <i class="fa fa-share"></i></button>
        </form>
        <input type='text' id='group_url' value='请输入分享链接' />
    </nav>
</div>
<div class="ztree_wrap" >
        <%--zTree--%>
    <div class="zTreebg" id="treeDiv" >
        <ul id="treeDemo" class="ztree" style="width:100%;height:98%;overflow-y:auto;background: white;border:1px solid whitesmoke "></ul>
    </div>
    <div id="examplePadBasic" class="div-pad"></div>
</div>
<div>
    <audio id="live_a" autoplay></audio>
</div>
<%--<input type='text' style="width:50px;height:1px;" id='clipBoardContent' value='' />--%>

<script type="text/javascript">
    window.onbeforeunload  = function() {
        $.ajax({
            async:false,
            cache:false,
            type:'POST',
            data:{
                sessionID: sessionID,
                socket_sessionID: socket_sessionID
            },
            dataType:"String",
            url:'${pageContext.request.contextPath}/leavePage',
            success:function(data){
            },error:function(){
            }
        });
    };
    $(document).ready(function(){
//        cookie
        <%--console.log("${sessionID}")--%>
        <%--SetCookie("sessionID="+"${sessionID}")--%>
        <%--console.log(document.cookie)--%>
        SetCookie("userName="+"${userName}")
    });
    function SetCookie(value){
        var Days = 1;  //cookie 将被保存一天
        var exp = new Date(); //获得当前时间
        exp.setTime(exp.getTime() + Days*24*60*60*1000); //换成毫秒
        document.cookie = value + ";expires=" + exp.toGMTString() + ";path=/";
    }
</script>
<script type="text/javascript">
    // The most basic example
    pad_params.padId = "${readmePadId}";
    $('#examplePadBasic').pad(pad_params); // sets the pad id and puts the pad in the div
</script>
<script type="text/javascript" src="js/Base64.js"></script>
<script type="text/javascript" src="js/Ev.js"></script>
<script type="text/javascript" src="js/socket_msg.js"></script>
<script type="text/javascript" src="js/editor.js"></script>
<script type="text/javascript" src="js/recorder.js"></script>
<script type="text/javascript" src="js/initialize.js"></script>
</body>
</html>
