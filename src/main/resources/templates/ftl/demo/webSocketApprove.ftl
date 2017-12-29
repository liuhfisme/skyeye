<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>view</title>
    <script src="/js/jquery-1.8.3.min.js" type="text/javascript" charset="utf-8"></script>
    <script src="/js/sockjs.min.js" type="text/javascript" charset="utf-8"></script>
    <script src="/js/stomp.min.js" type="text/javascript" charset="utf-8"></script>
</head>
<body onload="disconnect()">
<noscript><h2 style="color: #e80b0a;">Sorry，浏览器不支持WebSocket</h2></noscript>
<div>
    <div>
        <button id="connect" onclick="connect();">连接</button>
        <button id="disconnect" disabled="disabled" onclick="disconnect();">断开连接</button>
    </div>

    <div id="conversationDiv">
        <label>输入你的名字</label><input type="text" id="name"/>
        <button id="sendName" onclick="sendMsg();">发送</button>
        <p id="response"></p>
    </div>
</div>
<script type="text/javascript">
    var stompClient = null;
    var userId = '${userId}';
    var maxReConnCount=5;//最大重连次数
    var reConnCount = 0;//重连计数
    var timeout=20000;//超时时间，ms
    /**
     * 创建连接
     */
    function connect() {
//        var socket = new SockJS('/approveEndpoint');
//        stompClient = Stomp.over(socket);
        stompClient = Stomp.client("ws://127.0.0.1:9000/approverEndpoint", 'v11.stomp');
        stompClient.heartbeat.outgoing = 60000; // 客户端ping的频率，ms
        stompClient.heartbeat.incoming = 0;// 客户端pong的频率，如果为0表示不pong，也就是不接收服务端的信息，ms
        stompClient.connect({clientId: userId}, connectCallback, errorCallback);
    }
    /**
     * 连接成功后的回调函数
     */
    var connectCallback = function (frame) {
        setConnected(true);
        console.log('Connected:' + frame);
        var headers={};
//        headers.id="sub_"+userId;//自定义订阅的id
        //订阅审核者频道
        stompClient.subscribe('/user/' + userId + '/approverChannel', onMessage,headers);
    };
    /**
     * 连接出现异常后的回调函数
     */
    var errorCallback = function (frame) {
        console.log('Connect error：' + frame + ":" + stompClient.connected);
        if (reConnCount == maxReConnCount) {
            console.log("已尝试连接"+maxReConnCount+"次，放弃尝试");
        } else {
            setTimeout(function () {
                connect();
                reConnCount++;
            }, timeout);
        }
    };
    /**
     * 处理接收到的消息
     */
    var onMessage = function (frame) {
//        frame.ack();//通知服务端消息处理成功
//        frame.nack();//通知服务端消息处理失败
        showResponse(frame.body);
    };

    /**
     * 断开连接
     */
    function disconnect() {
        if (stompClient != null) {
            stompClient.disconnect(function () {
                console.log("See you next time!");
            });
        }
        setConnected(false);
        console.log('Disconnected....');
    }
    /**
     * 发送消息
     */
    function sendMsg() {
        var name = $('#name').val();
        //第一个参数是STOMP destination（必选），第二个参数是headers（可选），第三个参数是body（可选）
        stompClient.send("/app/ws/approveCall", {}, JSON.stringify({'name': name, 'userId': userId}));
    }

    function showResponse(message) {
        $("#response").html(message);
    }

    function setConnected(connected) {
        reConnCount = 0;
        document.getElementById("connect").disabled = connected;
        document.getElementById("disconnect").disabled = !connected;
        document.getElementById("conversationDiv").style.visibility = connected ? 'visible' : 'hidden';
//        $("#connect").disabled = connected;
//        $("#disconnect").disabled = !connected;
        $("#response").html();
    }

</script>
</body>
</html>

