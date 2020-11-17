Toy project v.5
---

# 기본 약어 정의

```properties
  PLAYER_ONE = PO               // 1 player
  PLAYER_TWO = PT               // 2 player
  BLANK = B                     // 블랭크
  PLAYER_ONE_BLOCK = POB        // 1 player 블랭크
  PLAYER_TWO_BLOCK = PTB        // 1 player 블랭크
  GAME_RULE = GR
  GAME_SCOPE = GS
```

# sample json

1. 기본 형태 

```json
  {
    chatRoomId : '현재 접속한 룸 ID',
    type : 'GAME', // CHAT, GAME, LEAVE, ENTER, READY 등
    writer : 'USER_NM', // 사용자 
  }
```

```json
  {
    chatRoomId : '현재 접속한 룸 ID',
    type : 'MESSAGE', // CHAT, GAME, LEAVE, ENTER, READY 등
    writer : 'USER_NM', // 사용자
    msg : '내용', // 내용 
  }
```



3. GAME 전체 상황 체크 예제 (Master room), GAME TARGETING!

```json
  {
    chatRoomId : '현재 접속한 룸 ID',
    type : 'GAME_RULE', // CHAT, GAME, LEAVE, ENTER 등
    writer : '사용자코드값', // 사용자 
    game : [{"pos":"0","type":"B","value":"0","i":0,"j": 0},
            {"pos":"1","type":"POB","value":"1","i":0,"j": 1},
            {"pos":"2","type":"PO","value":"3","i":0,"j": 2},
            ...{중략}...,
            {"pos":"48","type":"PO","value":"1","i":6,"j": 5},
            {"pos":"49","type":"B","value":"0","i":6,"j": 6}]} // 변경되는 값만 보내거나 {{CHECKERBOARD_SIZE}} 의 제곱에 대한 전체 데이터를 제공
  }
```

4. 점수를 체크 예제

```json
  {
    chatRoomId : '현재 접속한 룸 ID',
    type : 'GAME_SCOPE', // CHAT, GAME, LEAVE, ENTER, READY 등
    writer : '사용자코드값', // 사용자 
    game : [{"user":"PO","score":"0"},
            {"user":"PT","score":"0"}]} // 플레이어 1, 플레이어 2에 대한 값 처리
  }
```

