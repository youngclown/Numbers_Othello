
let webSocket;
let roomId = document.getElementById('roomId').value;
let userNm = document.getElementById('userName').innerHTML;
connect();
document.getElementById("send").addEventListener("click", function () {
    send();
})

var check = false;
var numberChoice = 0;

function sendNumber(number) {
    if (check === true) {
        webSocket.send(JSON.stringify({
            chatRoomId: roomId,
            type: 'GAME',
            writer: userNm,
            message: number + "##" + numberChoice
        }));
    } else {
        const chatroom = document.getElementById("chatroom");
        chatroom.innerHTML = chatroom.innerHTML + "<br>" + "숫자 버튼을 선택해주세요";

    }
}

function choiceNumber(number) {
    check = true;
    numberChoice = number;
}


function connect() {
    webSocket = new WebSocket("ws://localhost:8080/chat");
    webSocket.onopen = onOpen;
    webSocket.onclose = onClose;
    webSocket.onmessage = onMessage;
}

function disconnect() {
    webSocket.send(JSON.stringify({chatRoomId: roomId, type: 'LEAVE', writer: userNm}));
    webSocket.close();
}

function send() {
    const msg = document.getElementById("message").value;
    webSocket.send(JSON.stringify({chatRoomId: roomId, type: 'CHAT', writer: userNm, message: msg}));
    document.getElementById("message").value = "";
}

function onOpen() {
    webSocket.send(JSON.stringify({chatRoomId: roomId, type: 'ENTER', writer: userNm}));
}


function onMessage(e) {
    const data = JSON.parse(e.data);
    console.log(data);
    let type = data.type;

    // const data = JSON.stringify(e.data);

    if (type === 'GAME') {

    } else if (type === 'GAME_RULE') {
        let game = data.game;
        for (let i = 0; i < game.lenght; i++) {
            game[i]
        }


    } else if (type === 'GAME_SCOPE') {


    }

    // if (data.indexOf("GR^^")) {
    //     document.getElementById("roomUserCount").value = "";
    // } else {
    const chatroom = document.getElementById("chatroom");
    // chatroom.innerHTML = chatroom.innerHTML + "<br>" + data;
    // }

    // document.getElementById('send'+i).innerHTML;
}

function onClose() {
    disconnect();
}