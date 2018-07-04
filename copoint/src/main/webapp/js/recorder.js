video = document.getElementById("live")
audio = document.getElementById("live_a")

var base = new Base64();
var context = new AudioContext();
const maxBufferSize = 16384;
var desiredRate;
var bufferSize;
var ratio;
var audio_send_interval;
var bufferSize4Sending;
console.log("sampleRate : " + context.sampleRate);
var audioBuffer = context.createBuffer(2, maxBufferSize, context.sampleRate);
var audioQueue = [];
var playQueue = [];



function start_send_audio() {
    var counter = 0,
        start = new Date().getTime();
    function send_audio() {
        // console.log(audioQueue.length);
        if (audioQueue.length >= bufferSize4Sending)
        {
            var sendDataStr = "";
            for (var i = 0; i < bufferSize4Sending; i++) {
                sendDataStr += String.fromCharCode(audioQueue.shift());
            }
            var result = base.encode(sendDataStr);
            const headers = {
            };
            const body = {
                'message': result
            };
            stompClient.send("/audio", headers, JSON.stringify(body));
        }
        var real = (counter * audio_send_interval),
            ideal = (new Date().getTime() - start);
        var diff = (ideal - real) % audio_send_interval;
        window.setTimeout(function() { send_audio(); }, (audio_send_interval - diff));
        // console.log(diff);
    }
    window.setTimeout(function() { send_audio(); }, audio_send_interval);
}

var tempBuffer = context.createBuffer(2, maxBufferSize, context.sampleRate);
function recycle_play_audio() {
    // console.log(playQueue.length);
    if (playQueue.length >= bufferSize)
    {
        var maxSample = Math.ceil(playQueue.length * ratio);
        // console.log(Math.floor(maxSample/ratio));
        tempBuffer = context.createBuffer(2, maxSample, context.sampleRate);
        var arrayBuffer = [];
        for (var i = 0; i < Math.ceil(maxSample/ratio); i++) {
            arrayBuffer.push(playQueue.shift());
        }
        for (var channel = 0; channel < tempBuffer.numberOfChannels; channel++) {
            var audioData = tempBuffer.getChannelData(channel);
            for (var sample = 0; sample < tempBuffer.length; sample++) {
                // make output equal to the same as the input
                audioData[sample] = (arrayBuffer[Math.ceil(sample/ratio)] - 128) / 128.0;
            }
        }
    }
    var source = context.createBufferSource();
    // set the buffer in the AudioBufferSourceNode
    source.buffer = tempBuffer;
    // connect the AudioBufferSourceNode to the
    // destination so we can hear the sound
    source.connect(context.destination);
    // start the source playing
    source.start();
    source.onended = function () {
        // console.log('playback finished');
        window.setTimeout(function() { recycle_play_audio(); }, 0);
    };
}

function start_recorder() {
    audio_send_interval = AUDIO_PERIDO;
    desiredRate = DESIRED_RATE;
    bufferSize4Sending = desiredRate / (1000/audio_send_interval);
    bufferSize = Math.round(maxBufferSize * desiredRate / context.sampleRate);
    ratio = maxBufferSize / bufferSize;
    console.log("ratio : " + ratio);
    console.log("bufferSize : " + bufferSize);

    start_send_audio();
    recycle_play_audio();
    navigator.webkitGetUserMedia({audio:true},
        function(stream) {
            // audio.srcObject = stream;

            var audioInput = context.createMediaStreamSource(stream);
            var source = context.createBufferSource();
            var dest = context.createMediaStreamDestination();
            var myArrayBuffer = context.createBuffer(2, bufferSize, context.sampleRate);

            var inputScriptNode = context.createScriptProcessor(maxBufferSize, 1, 1);
            inputScriptNode.onaudioprocess = function(audioProcessingEvent) {
                // The input buffer is the song we loaded earlier
                var inputBuffer = audioProcessingEvent.inputBuffer;

                // The output buffer contains the samples that will be modified and played
                var outputBuffer = audioProcessingEvent.outputBuffer;

                // Loop through the output channels (in this case there is only one)
                for (var channel = 0; channel < outputBuffer.numberOfChannels; channel++) {
                    var inputData = inputBuffer.getChannelData(channel);
                    var outputData = outputBuffer.getChannelData(channel);
                    var tempData = myArrayBuffer.getChannelData(channel);
                    // Loop through the 4096 samples
                    for (var sample = 0; sample < inputBuffer.length; sample++) {
                        // make output equal to the same as the input
                        outputData[sample] = inputData[sample];
                        tempData[Math.ceil(sample/ratio)] = inputData[sample];
                        // tempData[sample] += ((Math.random() * 2) - 1) * 0.2;

                        // add noise to each output sample
                        // outputData[sample] += ((Math.random() * 2) - 1) * 0.2;
                    }
                }
                var tempData = myArrayBuffer.getChannelData(0);
                var i, l = tempData.length;
                var sendData = new Uint8Array(tempData.length);
                var sendDataStr = "";
                for (i = 0; i < l; i++) {
                    sendData[i] = Math.max(Math.min(parseInt(tempData[i] * 128), 128), -128) + 128;
                    sendDataStr += String.fromCharCode(sendData[i]);
                }
                audioQueue.push.apply(audioQueue,sendData);

                // console.log(Object.prototype.toString.call(tempData));
                // console.log(Math.max.apply(null, sendData));
                // console.log(Math.min.apply(null, sendData));
                // console.log(sendData.length);
                // var result = new TextDecoder("utf-8").decode(sendData);

                // var result = base.encode(sendDataStr);
                // var reader = new FileReader();
                // var base64data;
                // reader.readAsDataURL(new Blob([sendData.buffer]));
                // reader.onloadend = function() {
                //     base64data = reader.result;
                //     const headers = {
                //     };
                //     const body = {
                //         'message': result
                //     };
                //     stompClient.send("/audio", headers, JSON.stringify(body));
                //     // console.log(tempData.length)
                //     // console.log(base64data.length)
                // }
            };



            // create a javascript node
            var scriptNode = context.createScriptProcessor(maxBufferSize, 1, 1);
            // console.log(scriptNode.bufferSize);
            // specify the processing function
            scriptNode.onaudioprocess = function(audioProcessingEvent) {

                if (audioBuffer)
                {
                    console.log('null')
                }
                // The input buffer is the song we loaded earlier
                var inputBuffer = myArrayBuffer;

                // console.log(inputBuffer.length)
                // The output buffer contains the samples that will be modified and played
                var outputBuffer = audioProcessingEvent.outputBuffer;

                // Loop through the output channels (in this case there is only one)
                for (var channel = 0; channel < outputBuffer.numberOfChannels; channel++) {
                    var inputData = inputBuffer.getChannelData(channel);
                    var outputData = outputBuffer.getChannelData(channel);
                    // Loop through the 4096 samples
                    for (var sample = 0; sample < outputBuffer.length; sample++) {
                        // make output equal to the same as the input
                        outputData[sample] = inputData[Math.ceil(sample/ratio)];

                        // add noise to each output sample
                        // outputData[sample] += ((Math.random() * 2) - 1) * 0.2;
                    }
                }
            };
            // connect stream to our recorder
            audioInput.connect(inputScriptNode);
            inputScriptNode.connect(dest);
            source.connect(scriptNode);
            // connect our recorder to the previous destination
            // scriptNode.connect(context.destination);
            // scriptNode.connect(dest);
        },
        function(err) {
            console.log("Unable to get audio stream!")
        }
    );
}


function dataURLtoBlob(dataurl) {
    var arr = dataurl.split(','), mime = arr[0].match(/:(.*?);/)[1],
        bstr = atob(arr[1]), n = bstr.length, u8arr = new Uint8Array(n);
    while(n--){
        u8arr[n] = bstr.charCodeAt(n);
    }
    return new Blob([u8arr], {type:mime});
}

function get_audio(base64_audio) {
    // var arrayBuffer;
    // var blob = dataURLtoBlob(JSON.parse(base64_audio).message);
    // var reader = new FileReader();
    // reader.onloadend = function() {
    //     // arrayBuffer = reader.result;
    //     // for (var channel = 0; channel < audioBuffer.numberOfChannels; channel++) {
    //     //     var audioData = audioBuffer.getChannelData(channel);
    //     //     for (var sample = 0; sample < audioData.length; sample++) {
    //     //         // make output equal to the same as the input
    //     //         audioData[sample] = arrayBuffer[sample];
    //     //
    //     //         // add noise to each output sample
    //     //         // outputData[sample] += ((Math.random() * 2) - 1) * 0.2;
    //     //     }
    //     // }
    //     var arr = new Uint8Array(reader.result);
    //     console.log(reader.result);
    // };
    // reader.readAsArrayBuffer(blob);

    var arrayBuffer = base.decode2Uint8Array(JSON.parse(base64_audio).message);
    playQueue.push.apply(playQueue,arrayBuffer);
    // for (var channel = 0; channel < audioBuffer.numberOfChannels; channel++) {
    //     var audioData = audioBuffer.getChannelData(channel);
    //     for (var sample = 0; sample < audioBuffer.length; sample++) {
    //         // make output equal to the same as the input
    //         audioData[sample] = (arrayBuffer[Math.ceil(sample/ratio)] - 128) / 128.0;
    //     }
    // }
    // var source = context.createBufferSource();
    // // set the buffer in the AudioBufferSourceNode
    // source.buffer = audioBuffer;
    // // connect the AudioBufferSourceNode to the
    // // destination so we can hear the sound
    // source.connect(context.destination);
    // // start the source playing
    // source.start();
    // console.log(arrayBuffer.length);
}