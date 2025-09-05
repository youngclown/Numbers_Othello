package com.chat.number.dto;

import com.chat.number.domain.GameRoom;
import com.chat.number.model.GameUser;
import com.chat.number.model.NumberOthello;
import com.chat.number.type.NumberOthelloType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
public class GameStatusDto {
    private String type = "GAME_UPDATE"; // 메시지 타입을 명시
    private List<NumberOthello> boardState;
    private Map<String, Map<Integer, Integer>> playerHands;
    private String currentPlayer;
    private Map<String, Integer> scores;
    private Map<String, String> playerNames; // <"PO", "userA">, <"PT", "userB">
    private boolean gameOver = false;
    private String winner; // "PO", "PT", or "DRAW"

    public GameStatusDto(GameRoom room) {
        this.boardState = room.getGamePlayService().getOthelloList();
        this.playerHands = room.getPlayerHands();
        this.currentPlayer = room.getCurrentPlayer();
        this.scores = Map.of(
                NumberOthelloType.PLAYER_ONE.getValue(), room.getGamePlayService().getScore(NumberOthelloType.PLAYER_ONE.getValue()),
                NumberOthelloType.PLAYER_TWO.getValue(), room.getGamePlayService().getScore(NumberOthelloType.PLAYER_TWO.getValue())
        );

        // "PO" 또는 "PT" 타입이 있는 유저만 필터링하여 playerNames 맵 생성
        this.playerNames = room.getWriteUser().values().stream()
                .filter(user -> user.getType() != null && 
                               (user.getType().equals(NumberOthelloType.PLAYER_ONE.getValue()) || 
                                user.getType().equals(NumberOthelloType.PLAYER_TWO.getValue())))
                .collect(Collectors.toMap(GameUser::getType, GameUser::getUserName));

        this.gameOver = !room.isGameStart();
        // 게임이 정상적으로 시작되었다가 끝난 경우에만 승자 판정
        if (this.gameOver && room.getPlayerHands() != null && !room.getPlayerHands().isEmpty()) {
            int scorePO = scores.getOrDefault(NumberOthelloType.PLAYER_ONE.getValue(), 0);
            int scorePT = scores.getOrDefault(NumberOthelloType.PLAYER_TWO.getValue(), 0);
            if (scorePO > scorePT) {
                this.winner = NumberOthelloType.PLAYER_ONE.getValue();
            } else if (scorePT > scorePO) {
                this.winner = NumberOthelloType.PLAYER_TWO.getValue();
            } else {
                this.winner = "DRAW";
            }
        }
    }
}