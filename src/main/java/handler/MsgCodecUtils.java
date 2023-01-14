package handler;

import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Internal;
import com.google.protobuf.Message;
import msg.GameMsgProtocol;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public final class MsgCodecUtils {

    private MsgCodecUtils() {
    }

    private static final HashMap<Integer, GeneratedMessageV3> codeMsgBuildMap = new HashMap<>();
    private static final HashMap<Class, Integer> msgCodeMap = new HashMap<>();

    static {
        GameMsgProtocol.MsgCode[] msgCodes = GameMsgProtocol.MsgCode.values();

        Class<?>[] declaredClasses = GameMsgProtocol.class.getDeclaredClasses();

        for (Class clazz : declaredClasses) {
            String clazzName = clazz.getSimpleName();

            if (!GeneratedMessageV3.class.isAssignableFrom(clazz)) {
                continue;
            }

            for (GameMsgProtocol.MsgCode msgCodeEnum : msgCodes) {
                String msgCodeEnumName = msgCodeEnum.name();
                String codeName = msgCodeEnumName.replace("_", "");

                if (codeName.equalsIgnoreCase(clazzName)) {

                    try {
                        GeneratedMessageV3 instance = (GeneratedMessageV3) clazz.getDeclaredMethod("getDefaultInstance").invoke(clazz);
                        codeMsgBuildMap.put(msgCodeEnum.getNumber(), instance);
                    } catch (Exception e) {
                        System.out.println("异常" + e.getMessage());
                    }

                    msgCodeMap.put(clazz,msgCodeEnum.getNumber());
                }
            }
        }
        System.out.println("初始化完成");
    }

    public static Message.Builder getMsgBuildFromMsgCode(int msgCode) {

        return codeMsgBuildMap.get(msgCode).newBuilderForType();
    }

    public static int getMsgCodeFromGeneratedMessageV3(Object msg) {
        if (!msgCodeMap.containsKey(msg.getClass())) {
            return -1;
        }
        return msgCodeMap.get(msg.getClass());

    }
}
