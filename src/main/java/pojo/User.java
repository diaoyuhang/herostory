package pojo;

public class User {

    private Integer userId;
    private String heroAvatar;

    public User() {
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
