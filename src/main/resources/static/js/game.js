let webSocket;
const roomId = document.getElementById('roomId').value;
const userNm = document.getElementById('userName').innerHTML;
const BOARD_SIZE = 7; // 서버와 동일한 보드 크기

let myPlayerType = null; // "PO" or "PT"
let numberChoice = 0;
let isMyTurn = false;

// 초기 설정
document.addEventListener("DOMContentLoaded", function() {
    buildBoard();
    connect();
    document.getElementById("send").addEventListener("click", sendChatMessage);
    document.getElementById("ready").addEventListener("click", sendReadySignal);
});

function buildBoard() {
    const board = document.getElementById('board');
    if (!board) return;
    board.innerHTML = '';
    board.style.pointerEvents = 'none'; // 초기에는 클릭 비활성화
    for (let i = 0; i < BOARD_SIZE * BOARD_SIZE; i++) {
        const btn = document.createElement('button');
        btn.className = 'cell-btn';
        btn.id = 'send' + i;
        btn.onclick = function () { sendNumber(String(i)); };
        const inner = document.createElement('div');
        inner.className = 'blank';
        btn.appendChild(inner);
        board.appendChild(btn);
    }
}

function connect() {
    webSocket = new WebSocket("ws://localhost:8080/chat");
    webSocket.onopen = onOpen;
    webSocket.onclose = onClose;
    webSocket.onmessage = onMessage;
}

function onOpen() {
    webSocket.send(JSON.stringify({chatRoomId: roomId, type: 'ENTER', writer: userNm}));
}

function onMessage(e) {
    const data = JSON.parse(e.data);
    console.log("Received data:", data);

    if (data.type === 'GAME_UPDATE') {
        updateUI(data);
    } else if (data.type === 'ERROR') {
        alert(data.message);
        // 에러 발생 시 턴 상태를 원래대로 복구
        document.getElementById('board').style.pointerEvents = isMyTurn ? 'auto' : 'none';
    } else {
        // CHAT, ENTER, LEAVE 등 기타 메시지 처리
        const chatroom = document.getElementById('chatroom');
        let messageText = '';
        if (data.msg) { // 서버가 보내는 ChatMessage 형식
            messageText = (data.writer ? (data.writer + ': ') : '') + data.msg;
        } else if (typeof data === 'string') { // 단순 텍스트 메시지
            messageText = data;
        } else { // 기타 객체 (디버깅용)
            messageText = JSON.stringify(data);
        }
        chatroom.innerHTML = chatroom.innerHTML + '<br>' + messageText;
        chatroom.scrollTop = chatroom.scrollHeight;
    }
}

function updateUI(data) {
    // 1. 내 플레이어 타입 확인 (최초 1회)
    if (!myPlayerType) {
        for (const type in data.playerNames) {
            if (data.playerNames[type] === userNm) {
                myPlayerType = type;
                break;
            }
        }
    }

    // 2. 턴 상태 업데이트
    isMyTurn = data.currentPlayer === myPlayerType && !data.gameOver;
    updateTurnIndicator(data.currentPlayer, data.playerNames, data.gameOver);

    // 3. 보드 업데이트
    updateBoard(data.boardState);

    // 4. 점수 업데이트
    updateScores(data.scores);

    // 5. 플레이어 패(손) 업데이트
    if (myPlayerType) {
        updatePlayerHands(data.playerHands[myPlayerType], myPlayerType);
    }

    // 6. 게임 종료 처리
    if (data.gameOver) {
        handleGameOver(data.winner, data.playerNames);
    }
}

function updateBoard(boardState) {
    for (let i = 0; i < boardState.length; i++) {
        const cell = boardState[i];
        let content = '';
        switch (cell.type) {
            case 'PO': content = `<div class="player-one">${cell.value}</div>`; break;
            case 'PT': content = `<div class="player-two">${cell.value}</div>`; break;
            case 'POB': content = `<div class="player-one flipped">${cell.value}</div>`; break;
            case 'PTB': content = `<div class="player-two flipped">${cell.value}</div>`; break;
            default: content = `<div class="blank"></div>`; break;
        }
        document.getElementById("send" + i).innerHTML = content;
    }
}

function updateTurnIndicator(currentPlayer, playerNames, isGameOver) {
    const status = document.getElementById("statusText");
    const board = document.getElementById('board');
    if (isGameOver) {
        board.style.pointerEvents = 'none';
        return;
    }

    if (myPlayerType === currentPlayer) {
        status.innerText = "당신의 턴입니다. 숫자를 선택하세요.";
        board.style.pointerEvents = 'auto';
    } else {
        status.innerText = `${playerNames[currentPlayer]} 님의 턴을 기다리는 중...`;
        board.style.pointerEvents = 'none';
    }
}

function updateScores(scores) {
    document.getElementById('PO').innerText = scores.PO || 0;
    document.getElementById('PT').innerText = scores.PT || 0;
}

function updatePlayerHands(hand, playerType) {
    const p_type = (playerType === 'PO') ? 'one' : 'two';
    for (let i = 1; i <= 7; i++) {
        const count = hand[i] || 0;
        const countElement = document.getElementById(`${['one','two','three','four','five','six','seven'][i-1]}_${p_type}`);
        const buttonElement = countElement.closest('tr').previousElementSibling.querySelector(`button:nth-child(${i})`);

        if (countElement) countElement.innerText = count;
        if (buttonElement) buttonElement.disabled = (count === 0);
    }
}

function handleGameOver(winner, playerNames) {
    const status = document.getElementById("statusText");
    if (winner === myPlayerType) {
        status.innerText = "게임 종료: 당신이 승리했습니다!";
    } else if (winner === 'DRAW') {
        status.innerText = "게임 종료: 무승부입니다.";
    } else {
        status.innerText = `게임 종료: ${playerNames[winner]} 님의 승리입니다.`;
    }
}

function choiceNumber(number) {
    numberChoice = number;
    const sel = document.getElementById("selectedNumber");
    if (sel) sel.innerText = String(number);
    const status = document.getElementById("statusText");
    if (status && isMyTurn) status.innerText = "배치할 위치를 클릭하세요.";
}

function sendNumber(number) {
    if (!isMyTurn) {
        alert("당신의 턴이 아닙니다.");
        return;
    }
    if (numberChoice === 0) {
        alert("먼저 사용할 숫자를 선택해주세요.");
        return;
    }

    webSocket.send(JSON.stringify({
        chatRoomId: roomId,
        type: 'GAME',
        writer: userNm,
        message: `${number}##${numberChoice}`
    }));

    // 서버 응답을 기다리는 동안 UI 즉시 비활성화
    document.getElementById('board').style.pointerEvents = 'none';
    document.getElementById("statusText").innerText = "서버 응답 대기 중...";
    numberChoice = 0; // 선택 초기화
    document.getElementById("selectedNumber").innerText = "없음";
}

function sendReadySignal() {
    webSocket.send(JSON.stringify({chatRoomId: roomId, type: 'READY', writer: userNm}));
}

function sendChatMessage() {
    const msg = document.getElementById("message").value;
    if (msg.trim() !== '') {
        webSocket.send(JSON.stringify({chatRoomId: roomId, type: 'CHAT', writer: userNm, message: msg}));
        document.getElementById("message").value = "";
    }
}

function disconnect() {
    if (webSocket) {
        webSocket.send(JSON.stringify({chatRoomId: roomId, type: 'LEAVE', writer: userNm}));
        webSocket.close();
    }
}

function onClose() {
    // disconnect(); // 페이지를 벗어날 때만 disconnect 호출되도록 변경
}

window.addEventListener('beforeunload', disconnect);