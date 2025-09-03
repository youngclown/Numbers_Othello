let webSocket;
let roomId = document.getElementById('roomId').value;
let userNm = document.getElementById('userName').innerHTML;
const BOARD_SIZE = 7; // 서버와 동일한 보드 크기

// 동적 보드 생성
function buildBoard() {
    const board = document.getElementById('board');
    if (!board) return;
    board.innerHTML = '';
    for (let i = 0; i < BOARD_SIZE * BOARD_SIZE; i++) {
        const btn = document.createElement('button');
        btn.className = 'cell-btn';
        btn.id = 'send' + i;
        btn.onclick = function () { sendNumber(String(i)); };
        // 초기 빈칸을 렌더
        const inner = document.createElement('div');
        inner.className = 'blank';
        btn.appendChild(inner);
        board.appendChild(btn);
    }
}

buildBoard();

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
        const status = document.getElementById("statusText");
        if (status) status.innerText = "서버 응답 대기 중...";
    } else {
        const chatroom = document.getElementById("chatroom");
        chatroom.innerHTML = chatroom.innerHTML + "<br>" + "숫자 버튼을 선택해주세요";
    }
}

function choiceNumber(number) {
    check = true;
    numberChoice = number;
    const sel = document.getElementById("selectedNumber");
    if (sel) sel.innerText = String(number);
    const status = document.getElementById("statusText");
    if (status) status.innerText = "배치할 위치를 클릭하세요";
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
            if (game[i].type === 'PO') {
                document.getElementById("send" + i).innerHTML = '<div class="player-one">'+game[i].value+'</div>';
            } else if (game[i].type === 'PT') {
                document.getElementById("send" + i).innerHTML = '<div class="player-two">'+game[i].value+'</div>';
            } else if (game[i].type === 'B') {
                document.getElementById("send" + i).innerHTML = '<div class="blank"></div>';
            } else if (game[i].type === 'POB') {
                document.getElementById("send" + i).innerHTML = '<div class="player-one-block"></div>';
            } else if (game[i].type === 'PTB') {
                document.getElementById("send" + i).innerHTML = '<div class="player-two-block"></div>';
            }
        }
        const status = document.getElementById('statusText');
        if (status) status.innerText = '배치가 반영되었습니다.';
    } else if (type === 'MESSAGE') {
        const chatroom = document.getElementById('chatroom');
        if (data.msg) {
            chatroom.innerHTML = chatroom.innerHTML + '<br>' + (data.writer ? (data.writer + ': ') : '') + data.msg;
        } else if (typeof data === 'string') {
            chatroom.innerHTML = chatroom.innerHTML + '<br>' + data;
        }
        const status = document.getElementById('statusText');
        if (status && data.msg) status.innerText = data.msg;
    } else if (type === 'CHAT') {
        const chatroom = document.getElementById('chatroom');
        if (data.msg) {
            chatroom.innerHTML = chatroom.innerHTML + '<br>' + (data.writer ? (data.writer + ': ') : '') + data.msg;
        } else {
            chatroom.innerHTML = chatroom.innerHTML + '<br>' + JSON.stringify(data);
        }
    } else if (type === 'GAME_SCOPE') {
        let game = data.game;
        for (let i = 0; i < game.length; i++) {
            document.getElementById(game[i].user).innerHTML = game[i].score;
        }
    } else {
        // Fallback: server sometimes sends plain strings (e.g., ENTER/READY messages)
        if (typeof data === 'string') {
            const chatroom = document.getElementById('chatroom');
            chatroom.innerHTML = chatroom.innerHTML + '<br>' + data;
        }
    }
}

function onClose() {
    disconnect();
}
