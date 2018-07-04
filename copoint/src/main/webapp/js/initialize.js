function socketConnected() {
    console.log("【socketConnected】");
    audio_callback(get_audio);
}

function propertySetup() {
    console.log("【propertySetup】");
    start_recorder();
}

var stompClient = new Socket("websocket",messageHandle);

bindEvent(stompClient,'customConnected',socketConnected);
bindEvent(stompClient,'propertySetup',propertySetup);

// start_event_listen(2000, sendEvent);


