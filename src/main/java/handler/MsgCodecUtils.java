package handler;

import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.InvalidProtocolBufferException;
import msg.GameMsgProtocol;

public class MsgCodecUtils {

    public static GeneratedMessageV3 getGeneratedMessageV3FromMsgCode(byte[] msgBody, short msgCode) throws InvalidProtocolBufferException {
        switch (msgCode) {
            case GameMsgProtocol.MsgCode.USER_ENTRY_CMD_VALUE:
                return GameMsgProtocol.UserEntryCmd.parseFrom(msgBody);
            case GameMsgProtocol.MsgCode.WHO_ELSE_IS_HERE_CMD_VALUE:
                return GameMsgProtocol.WhoElseIsHereCmd.parseFrom(msgBody);
            case GameMsgProtocol.MsgCode.USER_MOVE_TO_CMD_VALUE:
                return GameMsgProtocol.UserMoveToCmd.parseFrom(msgBody);
            default:
                return null;
        }
    }

    public static short getMsgCodeFromGeneratedMessageV3(Object msg) {
        if (msg instanceof GameMsgProtocol.UserEntryResult) {
            return GameMsgProtocol.MsgCode.USER_ENTRY_RESULT_VALUE;
        } else if (msg instanceof GameMsgProtocol.WhoElseIsHereResult) {
            return GameMsgProtocol.MsgCode.WHO_ELSE_IS_HERE_RESULT_VALUE;
        } else if (msg instanceof GameMsgProtocol.UserQuitResult) {
            return GameMsgProtocol.MsgCode.USER_QUIT_RESULT_VALUE;
        } else if (msg instanceof GameMsgProtocol.UserMoveToResult) {
            return GameMsgProtocol.MsgCode.USER_MOVE_TO_RESULT_VALUE;
        } else {
            return -1;
        }
    }
}
