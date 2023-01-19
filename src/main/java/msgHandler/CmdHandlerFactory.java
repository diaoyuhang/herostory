package msgHandler;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandlerContext;
import msg.GameMsgProtocol;
import org.apache.log4j.Logger;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class CmdHandlerFactory {
    static Logger logger = Logger.getLogger(CmdHandlerFactory.class);
    public static final Map<Class, ICmdHandler> cmdMap = new HashMap<>();

    private CmdHandlerFactory() {
    }

    static {

        Class<?>[] declaredClasses = GameMsgProtocol.class.getDeclaredClasses();

        HashMap<String, Class> simpleNameAndClassMap = new HashMap<>();
        for (Class clazz : declaredClasses) {
            String clazzName = clazz.getSimpleName();

            if (GeneratedMessageV3.class.isAssignableFrom(clazz)) {
                simpleNameAndClassMap.put(clazzName, clazz);
            }
        }

        initCmdMap(simpleNameAndClassMap);
    }

    public static void handler(ChannelHandlerContext channelHandlerContext, Object o) {
        ICmdHandler iCmdHandler = cmdMap.get(o.getClass());

        if (iCmdHandler instanceof UserAttkCmdHandler) {
            //存在并发问题的交给单线程处理
            MainSingleThreadProcessor.instance.addRunnable(new Runnable() {
                @Override
                public void run() {
                    iCmdHandler.handler(channelHandlerContext, cast(o));
                }
            });
        }else{
            iCmdHandler.handler(channelHandlerContext, cast(o));
        }
    }

    private static <TCmd extends GeneratedMessageV3> TCmd cast(Object o) {
        if (o == null) {
            return null;
        }
        return (TCmd) o;
    }

    private static void initCmdMap(HashMap<String, Class> simpleNameAndClassMap) {
        String basicPath = CmdHandlerFactory.class.getPackageName().replace('.', File.separatorChar);
        URL url = CmdHandlerFactory.class.getClassLoader().getResource(basicPath);
        String protocol = url.getProtocol();

        try {
            //如果是从jar包中获取class
            if (protocol.equals("jar")) {
                JarFile jarFile = ((JarURLConnection) url.openConnection()).getJarFile();

                Enumeration<JarEntry> entries = jarFile.entries();
                String pk = CmdHandlerFactory.class.getPackageName().replace('.', '/');

                while (entries.hasMoreElements()) {
                    JarEntry jarEntry = entries.nextElement();
                    //得到class的路径,比如com/one/two.class or com/one
                    String realName = jarEntry.getRealName();
                    //必须是class，并且开头是以basicPath开头
                    if (realName.endsWith(".class") && realName.startsWith(pk)) {
                        String classPath = realName.replace('/', '.').replace(".class", "");
                        judgeInstanceClass(simpleNameAndClassMap, classPath);
                    }

                }

            } else { //普通file走这个逻辑
                Queue<File> queue = new LinkedList<>();

                File file = new File(url.toURI());
                String filePath = file.getPath();

                int index = filePath.indexOf(basicPath);
                queue.add(file);
                while (!queue.isEmpty()) {
                    File poll = queue.poll();
                    if (poll.isDirectory()) {
                        File[] files = poll.listFiles();
                        queue.addAll(Arrays.asList(files));
                    } else {
                        String clazzPath = poll.getPath().substring(index).replace(File.separatorChar, '.').replace(".class", "");
                        judgeInstanceClass(simpleNameAndClassMap, clazzPath);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("初始化cmdMap失败", e);
        }
    }

    private static void judgeInstanceClass(HashMap<String, Class> simpleNameAndClassMap, String clazzPath) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Class<?> clazz = Class.forName(clazzPath);
        //class必须是ICmdHandler子类
        if (ICmdHandler.class.isAssignableFrom(clazz) && !clazz.isInterface()) {
            String name = clazz.getSimpleName().replace("Handler", "");
            if (simpleNameAndClassMap.containsKey(name)) {
                cmdMap.put(simpleNameAndClassMap.get(name), (ICmdHandler) clazz.getConstructor().newInstance());
            }
        }
    }
}
