
let webSocket;
let roomId = document.getElementById('roomId').value;
let userNm = document.getElementById('userName').innerHTML;
connect();
document.getElementById("send").addEventListener("click", function () {
    send();
})

document.getElementById("ready").addEventListener("click", function () {
    ready();
})

let check = false;
let numberChoice = 0;

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

/* 준비완료(WATTING), 대기(READY) */
function ready() {
    const msg = document.getElementById("ready").value;
    webSocket.send(JSON.stringify({chatRoomId: roomId, type: 'READY', writer: userNm, message: msg}));
    // ------- 대기중....
}

function onOpen() {
    webSocket.send(JSON.stringify({chatRoomId: roomId, type: 'ENTER', writer: userNm}));
}


function onMessage(e) {
    const data = JSON.parse(e.data);
    console.log(data);
    let type = data.type;
    if (type === 'GAME') {
        let game = data.game;
        for (let i = 0; i < game.length; i++) {
            if(game[i].type === 'PO') {
                document.getElementById("send"+i).innerHTML = "PO";
            } else if(game[i].type === 'PT') {
                document.getElementById("send"+i).innerHTML = "PT";
            } else if(game[i].type === 'B') {
                document.getElementById("send"+i).innerHTML = "B";
            } else if(game[i].type === 'POB') {
                document.getElementById("send"+i).innerHTML = "POB";
            } else if(game[i].type === 'PTB') {
                document.getElementById("send"+i).innerHTML = "PTB";
            }
        }
    } else if (type === 'GAME_RULE') {

    } else if (type === 'CHAT') {
        const chatroom = document.getElementById("chatroom");
        chatroom.innerHTML = chatroom.innerHTML + "<br>" + data;
    } else if (type === 'GAME_SCOPE') {
        let game = data.game;
        for (let i = 0; i < game.length; i++) {
            document.getElementById(game[i].user).innerHTML = game[i].score;
        }
    }
}

function onClose() {
    disconnect();
}