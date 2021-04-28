package com.chat.number.exception;

/**
 * Skill 이 오류났을 때 발생하는 에러 처리
 */
public class NonSkillException extends BaseException {

    private static final long serialVersionUID = 1668048243860787851L;

    public NonSkillException() {
    }

    public NonSkillException(String message) {
        super(message);
    }
}
