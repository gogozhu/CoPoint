function editor_setEnable(option) {
    var editor_area = document.getElementById('editor_area');
    if (option == true)
    {
        editor_area.removeAttribute('readOnly');
    }
    else
    {
        editor_area.setAttribute('readOnly','true');
    }
}

var event_listen_time;
var event_listen_callback;
function start_event_listen(_time, _callback) {
    event_listen_time = _time;
    event_listen_callback = _callback;
    setTimeout("event_listen_callback('hello');start_event_listen(event_listen_time,event_listen_callback);",event_listen_time);
}
