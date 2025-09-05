package com.chat.number.domain;

import com.chat.number.model.GameUser;
import com.chat.number.service.GamePlayService;
import com.chat.number.type.NumberOthelloType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;

@Slf4j
@Getter
@Setter
public class GameRoom {
    private String roomId;
    private String name;
    private boolean gameStart = false;
    private int roomUserCount = 0;  // 현재 유저 확인
    private int roomMaxCount = 2;   // 룸 최대 갯수

    @JsonIgnore
    private Set<WebSocketSession> sessions = new HashSet<>();   // 세션 유저 정보
    private Map<String, GameUser> writeUser = new HashMap<>();     // 실제 유저 정보
    private GamePlayService gamePlayService = new GamePlayService();

    // 추가된 게임 상태 변수
    private Map<String, Map<Integer, Integer>> playerHands = new HashMap<>();
    private String currentPlayer; // "PO" 또는 "PT"

    /**
     * 게임 시작 시 호출되어 플레이어에게 역할을 배정하고 숫자 카드를 분배합니다.
     */
    public void initializeGame() {
        if (writeUser.size() < 2) {
            log.warn("Cannot start game with less than 2 players.");
            return;
        }

        // 플레이어 역할(PO, PT) 배정
        Iterator<GameUser> userIterator = writeUser.values().iterator();
        GameUser player1 = userIterator.next();
        player1.setType(NumberOthelloType.PLAYER_ONE.getValue());

        GameUser player2 = userIterator.next();
        player2.setType(NumberOthelloType.PLAYER_TWO.getValue());

        // 각 플레이어에게 카드 분배
        playerHands.put(NumberOthelloType.PLAYER_ONE.getValue(), createPlayerHand());
        playerHands.put(NumberOthelloType.PLAYER_TWO.getValue(), createPlayerHand());

        // 첫 턴 설정
        this.currentPlayer = NumberOthelloType.PLAYER_ONE.getValue();
        this.gameStart = true;
        log.info("Game initialized for room {}. Player1: {}, Player2: {}. Player 1's hand: {}, Player 2's hand: {}",
                roomId, player1.getUserName(), player2.getUserName(),
            playerHands.get(NumberOthelloType.PLAYER_ONE.getValue()), playerHands.get(NumberOthelloType.PLAYER_TWO.getValue()));
    }

    /**
     * README의 규칙에 따라 23개의 숫자 카드로 구성된 플레이어의 손을 생성합니다.
     * - 1~4는 최대 4개
     * - 5~7은 최대 3개
     * @return Map<Integer, Integer> key: 숫자, value: 개수
     */
    private Map<Integer, Integer> createPlayerHand() {
        Map<Integer, Integer> hand = new HashMap<>();
        Random random = new Random();
        final int totalCards = 23;
        int currentCards = 0;

        // 모든 카드를 1개씩 기본으로 추가하여 다양성 확보
        for (int i = 1; i <= 7; i++) {
            hand.put(i, 1);
        }
        currentCards = 7;

        // 나머지 카드를 규칙에 맞게 추가
        while (currentCards < totalCards) {
            int number = random.nextInt(7) + 1; // 1부터 7까지의 숫자
            int currentCount = hand.getOrDefault(number, 0);

            int maxCopies = (number >= 1 && number <= 4) ? 4 : 3;

            if (currentCount < maxCopies) {
                hand.put(number, currentCount + 1);
                currentCards++;
            }
        }
        return hand;
    }
}
