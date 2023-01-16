package broadcast;

import msg.GameMsgProtocol;
import pojo.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class UserInfosUtils {
    private static final Map<Integer, User> users = new HashMap<>();

    private UserInfosUtils() {
    }

    public static void put(Integer userId, User user){
        users.put(userId,user);
    }

    public static void remove(Integer userId){
        users.remove(userId);
    };

    public static Collection<User> userList(){
        return users.values();
    }

    public static User get(Integer userId){
        return users.get(userId);
    }
}
