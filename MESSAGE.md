Toy project v.5
---

# 기본 약어 정의

```
  PLAYER_ONE = PO
  PLAYER_TWO = PT
  BLANK = B
  PLAYER_ONE_BLOCK = POB
  PLAYER_TWO_BLOCK = PTB
  GAME_RULE = GR
  GAME_SCOPE = GS
```

# sample json

1. 기본 형태 

```
  {
    chatRoomId : '현재 접속한 룸 ID',
    type : 'GAME', // CHAT, GAME, LEAVE, ENTER 등
    writer : 'USER_NM', // 사용자 
  }
```


1. GAME 전체 상황 체크 예제 (Master room)

```
  {
    chatRoomId : '현재 접속한 룸 ID',
    type : 'GAME_RULE', // CHAT, GAME, LEAVE, ENTER 등
    writer : '사용자코드값', // 사용자 
    game : [{"pos":"0","target":"B","number":"0"},
            {"pos":"1","target":"POB","number":"1"},
            {"pos":"1","target":"PO","number":"3"},
            .....
            {"pos":"49","target":"B","number":"0"}]} // 변경되는 값만 보내거나 {{CHECKERBOARD_SIZE}} 의 제곱에 대한 전체 데이터를 제공
  }
```

2. 점수를 체크 예제

```
  {
    chatRoomId : '현재 접속한 룸 ID',
    type : 'GAME_SCOPE', // CHAT, GAME, LEAVE, ENTER 등
    writer : '사용자코드값', // 사용자 
    game : [{"pos":"PO","score":"0"},
            {"pos":"PT","score":"0"}]} // 플레이어 1, 플레이어 2에 대한 값 처리
  }
```

3. 