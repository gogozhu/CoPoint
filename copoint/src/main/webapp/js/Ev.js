//监听事件,将事件压入队列
function bindEvent(obj, events, fn){

    obj.listeners = obj.listeners || {};
    obj.listeners[events] = obj.listeners[events] || [];
    obj.listeners[events].push(fn);

    // console.log(obj.listeners)
    // if (obj.addEventListener) {
    //     obj.addEventListener(events,fn);
    // }else{
    //     obj.attachEvent("on" + events, fn);
    // }
}

function fireEvent(obj, events){
    for (var i = 0; i < obj.listeners[events].length; i++) {
        obj.listeners[events][i](); //执行绑定的操作就可以了；
    };
}