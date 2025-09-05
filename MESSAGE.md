Toy project v.5
---

# 기본 약어 정의

```properties
  PLAYER_ONE = PO               // 1 player
  PLAYER_TWO = PT               // 2 player
  BLANK = B                     // 블랭크
  PLAYER_ONE_BLOCK = POB        // 1 player의 뒤집힌 돌
  PLAYER_TWO_BLOCK = PTB        // 2 player의 뒤집힌 돌
```

# 메시지 타입 (C: Client -> S: Server, S: Server -> C: Client)

*   `ENTER` (C -> S): 플레이어가 방에 입장
*   `LEAVE` (C -> S): 플레이어가 방에서 퇴장
*   `READY` (C -> S): 플레이어가 게임 준비 완료/취소
*   `GAME` (C -> S): 플레이어가 보드에 숫자를 놓는 행동
*   `CHAT` (C -> S): 채팅 메시지 전송
*   `GAME_UPDATE` (S -> C): 게임 상태 전체 업데이트 (가장 중요한 메시지)
*   `ERROR` (S -> C): 잘못된 행동에 대한 에러 알림
*   `CHAT` / 일반 텍스트 (S -> C): 서버 공지 또는 채팅 메시지 전달

---

# JSON 메시지 형식

## 1. 클라이언트 -> 서버 (C -> S)

*   **기본 형식 (입장, 퇴장, 준비 등)**
    ```json
    {
      "chatRoomId": "현재 접속한 룸 ID",
      "type": "ENTER",
      "writer": "USER_NM"
    }
    ```

*   **게임 플레이 (`GAME`)**
    ```json
    {
      "chatRoomId": "현재 접속한 룸 ID",
      "type": "GAME",
      "writer": "USER_NM",
      "message": "{cellIndex}##{numberValue}" // 예: "16##5" (16번 칸에 5를 놓음)
    }
    ```

*   **채팅 (`CHAT`)**
    ```json
    {
      "chatRoomId": "현재 접속한 룸 ID",
      "type": "CHAT",
      "writer": "USER_NM",
      "message": "채팅 내용"
    }
    ```

## 2. 서버 -> 클라이언트 (S -> C)

*   **게임 상태 전체 업데이트 (`GAME_UPDATE`)**
    > 게임 시작 시, 그리고 매 턴이 끝날 때마다 전송됩니다.
    ```json
    {
      "type": "GAME_UPDATE",
      "boardState": [
        {"type":"B","pod":0,"i":0,"j":0,"value":0},
        {"type":"PO","pod":0,"i":0,"j":1,"value":5},
        {"type":"PTB","pod":0,"i":0,"j":2,"value":3}
      ], // 49개 전체 보드 상태
      "playerHands": {
        "PO": {"1": 2, "2": 1, "5": 0, ...},
        "PT": {"1": 1, "3": 2, "7": 1, ...}
      }, // 각 플레이어의 남은 숫자 카드
      "currentPlayer": "PO", // 현재 턴인 플레이어
      "scores": {
        "PO": 15,
        "PT": 22
      }, // 현재 점수
      "playerNames": {
        "PO": "UserA",
        "PT": "UserB"
      }, // 플레이어 타입과 이름 매핑
      "gameOver": false, // 게임 종료 여부
      "winner": null // "PO", "PT", "DRAW" 또는 null
    }
    ```

*   **오류 알림 (`ERROR`)**
    > 자신의 턴이 아닐 때 플레이하는 등 잘못된 행동을 했을 때 해당 플레이어에게만 전송됩니다.
    ```json
    {
        "type": "ERROR",
        "message": "규칙에 맞지 않는 플레이입니다."
    }
    ```

*   **서버 메시지 및 채팅 전달**
    > 입장/퇴장/준비 등 시스템 메시지나 다른 유저의 채팅을 전달합니다.
    ```json
    // 단순 텍스트 형태
    "UserA님이 입장했습니다"

    // 또는 채팅 메시지 형태
    {
        "writer": "UserB",
        "msg": "안녕하세요"
    }
    ```