<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
    <title>CoPoint</title>
    <link rel="stylesheet" type="text/css" href="simditor/styles/simditor.css" />
    <script type="text/javascript" src="simditor/scripts/jquery.min.js"></script>
    <script type="text/javascript" src="simditor/scripts/module.js"></script>
    <script type="text/javascript" src="simditor/scripts/hotkeys.js"></script>
    <script type="text/javascript" src="simditor/scripts/uploader.js"></script>
    <script type="text/javascript" src="simditor/scripts/simditor.js"></script>
    <script src="https://cdn.bootcss.com/sockjs-client/1.1.2/sockjs.min.js"></script>
    <script src="https://cdn.bootcss.com/stomp.js/2.3.3/stomp.min.js"></script>
    <script src="http://www.webrtc-experiment.com/RecordRTC.js"></script>
</head>

<body>
    <div>
        <audio id="live_a" autoplay></audio>
    </div>

</body>

<textarea id="editor_area" placeholder="Hello CoPoint! Write something." autofocus></textarea>
<script>
    var AUDIO_PERIDO = 0,
        DESIRED_RATE = 0;
</script>
<script type="text/javascript" src="js/Base64.js"></script>
<script type="text/javascript" src="js/Ev.js"></script>
<script type="text/javascript" src="js/socket_msg.js"></script>
<script type="text/javascript" src="js/editor.js"></script>
<script type="text/javascript" src="js/recorder.js"></script>
<script type="text/javascript" src="js/initialize.js"></script>
</html>