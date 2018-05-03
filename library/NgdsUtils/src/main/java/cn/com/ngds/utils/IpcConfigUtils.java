package cn.com.ngds.utils;

import android.os.Environment;
import android.util.JsonReader;
import android.util.JsonWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * 跨进程配置工具类，当前主要用于大厅与云适配游戏的配置同步（云适配游戏读取大厅的配置）。
 * <p>
 * 因为涉及跨进程配置读取，所以选择将配置保存在外部存储的 json 文件中。
 *
 * @author 李剑波
 * @date 17/5/22
 */

public class IpcConfigUtils {

    private static final String CFG_FILE_NAME = "cfg.json";
    private static final String CFG_IS_SPP_ON = "is_spp_on";

    /**
     * 读取配置
     */
    public static IpcConfig readIpcConfig() {
        IpcConfig config = new IpcConfig();
        try {
            File file = new File(Environment.getExternalStorageDirectory(), CFG_FILE_NAME);
            InputStream inputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            JsonReader reader = new JsonReader(inputStreamReader);
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (CFG_IS_SPP_ON.equals(name)) {
                    config.isSppOn = reader.nextBoolean();
                }
            }
            reader.endObject();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return config;
    }

    /**
     * 写入配置
     */
    public static void writeIpcConfig(IpcConfig config) {
        try {
            File file = new File(Environment.getExternalStorageDirectory(), CFG_FILE_NAME);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            file.setReadable(true, false);
            file.setWritable(true, false);
            file.setExecutable(true, false);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter =
                    new OutputStreamWriter(fileOutputStream, "UTF-8");
            JsonWriter writer = new JsonWriter(outputStreamWriter);
            writer.setIndent("  ");
            writer.beginObject();
            writer.name(CFG_IS_SPP_ON).value(config.isSppOn);
            writer.endObject();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * SPP是否开启，默认为关闭。
     */
    public static boolean isSppOn() {
        IpcConfig config = readIpcConfig();
        return config.isSppOn;
    }

    /**
     * 设置SPP开启状态。
     */
    public static void setIsSppOn(boolean isSppOn) {
        IpcConfig config = readIpcConfig();
        config.isSppOn = isSppOn;
        writeIpcConfig(config);
    }

    public static class IpcConfig {
        public boolean isSppOn;
    }

}
