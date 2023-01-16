package pojo;

import msg.GameMsgProtocol;

public class User {

    private Integer userId;
    private String heroAvatar;

    private GameMsgProtocol.WhoElseIsHereResult.UserInfo.MoveState moveState;

    public User() {
    }

    public GameMsgProtocol.WhoElseIsHereResult.UserInfo.MoveState getMoveState() {
        return moveState;
    }

    public void setMoveState(GameMsgProtocol.WhoElseIsHereResult.UserInfo.MoveState moveState) {
        this.moveState = moveState;
    }

    public User(Integer userId, String heroAvatar) {
        this.userId = userId;
        this.heroAvatar = heroAvatar;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getHeroAvatar() {
        return heroAvatar;
    }

    public void setHeroAvatar(String heroAvatar) {
        this.heroAvatar = heroAvatar;
    }
}
