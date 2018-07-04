var stompClient;
var sessionId;

function onConnect(socket, stompClient) {

    // 获取 websocket 连接的 sessionId
    const arr = socket._transport.url.split('/');
    sessionId = arr[arr.length-1];
    if (sessionId === "websocket") {
        sessionId = arr[arr.length-2];
    }
    socket_sessionID = sessionId;
    console.log("connected, session id: " + sessionId);

    // 订阅广播消息
    const subscription_broadcast = stompClient.subscribe('/topic/getResponse',
        function (response) {
            if (response.body) {
                console.log("【广播】" + response.body);
            } else {
                console.log("收到一个空消息");
            }
        });

    // 订阅私人消息
    const subscription_personal = stompClient.subscribe('/user/' + sessionId + '/personal',
        function (response) {
            if (response.body) {
                console.log("【私人消息】" + response.body);
            } else {
                console.log("收到一个空消息");
            }
        });

    const subscription_property = stompClient.subscribe('/user/' + sessionId + '/property',
        function (response) {
            if (response.body) {
                console.log("【property】" + response.body);
                AUDIO_PERIDO = JSON.parse(response.body).audioPeriod;
                DESIRED_RATE = JSON.parse(response.body).desiredRate;
                console.log("【audioPeriod】" + AUDIO_PERIDO);
                console.log("【desiredRate】" + DESIRED_RATE);
                fireEvent(stompClient,'propertySetup');
            } else {
                console.log("收到一个空消息");
            }
        });

    // 订阅异常消息
    const subscription_errors = stompClient.subscribe('/user/' + sessionId + '/errors',
        function (response) {
            if (response.body) {
                console.log("【异常消息】" + response.body);
            } else {
                console.log("收到一个空消息");
            }
        });

    const headers = {
    };
    const body = {
        'message': 'hello'
    };
    stompClient.send("/check_in", headers, JSON.stringify(body));
    // stompClient.send("/chat", headers, JSON.stringify(body));
    // stompClient.send("/speak", headers, JSON.stringify(body));

    // audio_callback(get_audio);
}

function Socket(url,callback) {
    var socket = new SockJS(url);
    stompClient = Stomp.over(socket);

    stompClient.connect(
        {},
        // 连接成功回掉函数
        function (frame)  {
            onConnect(socket, stompClient);
            fireEvent(stompClient,'customConnected');
        }
    );

    return stompClient;
}

function audio_callback(callback) {
    // 订阅音频通话
    const subscription_audio = stompClient.subscribe('/user/' + sessionId + '/callback',
        function (response) {
            if (response.body) {
                console.log('【异常消息】');
                callback(response.body);
            } else {
                console.log("收到一个空消息");
            }
        });
}

function sendEvent(event) {
    const headers = {
    };
    const body = {
        'message': event
    };
    if (stompClient.connected)
    {
        stompClient.send("/event", headers, JSON.stringify(body));
    }
}

//消息封装函数
function messagePackage(message) {
    /*
     message{
     userName : xx,
     timeSign : 22:12:44,
     content : abc
     }
     */
    var element_section = $("<section></section>");
    var element_section_p1 = $("<p></p>");
    var element_section_p1_user = $("<span></span>");
    var element_section_p1_time = $("<time></time>");
    var element_section_p2_content = $("<p></p>");
    element_section.addClass("message");
    element_section_p1.addClass("header");
    element_section_p2_content.addClass("content");
    element_section_p1_user.text(message.username);
    element_section_p1_time.text(message.timeSign);
    element_section_p2_content.text(message.content);
    element_section_p1.append(element_section_p1_user);
    element_section_p1.append(element_section_p1_time);
    element_section.append(element_section_p1);
    element_section.append(element_section_p2_content);
    return element_section;
}

//用于消息处理的函数
/*
jsonData : {
    type:1(聊天信息)||2(用户列表更新信息),
    username(1,2,3):xx,
    timeSign(1):xx:xx:xx,
    content(1):xxxxxxxxxxxxx,
}
 */

function messageHandle(event) {
    console.log(event.data.type);
    var jsonStr = event.data;
    var data = JSON.parse(jsonStr);
    var $message = null;
    switch(data.type) {
        //更新聊天显示框
        case 1:
            if(data.username == currentUser) return;
            $message = messagePackage({
                username : data.username,
                timeSign : data.timeSign,
                content : data.content
            });
            $show.append($message);
            //让滚动条自动滚到底
            $show.get(0).scrollTop = $show.get(0).scrollHeight;
            break;
        //向已经在线的用户发送用户列表更新信息
        case 2:
            var $userName = $("<p></p>");
            $userName.text(data.username);
            $("#usersInfo").append($userName);

            break;
        //将所有已经在线的用户信息发送给刚加入的用户
        case 3:
            var usernames = data.usernames;
            var $usersInfo = $("#usersInfo");
            $usersInfo.empty();
            for(var i= 0,len=usernames.length;i<len;i++) {
                var $userName = $("<p></p>");
                $userName.text(usernames[i]);
                $usersInfo.append($userName);
            }

            break;
        //删除用户信息
        case 4:
            var $usersInfo = $("#usersInfo");
            $usersInfo.find(":contains("+data.username+")").remove();
    }

}

function getTime() {
    var currentTime = {};
    var raw = new Date();
    currentTime.date = raw.getFullYear() + "-" + raw.getMonth() + "-" + raw.getDate();
    currentTime.time = raw.getHours() + ":" + raw.getMinutes() + ":" + String(raw.getMilliseconds()).slice(0,2);
    return currentTime;
}