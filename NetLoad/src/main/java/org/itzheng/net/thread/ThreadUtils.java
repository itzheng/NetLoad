package org.itzheng.net.thread;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Title:线程池工具,就是系统自带的线程池简单封装<br>
 * Description: <br>
 * Company: <br>
 *
 * @author ZhengYongdong
 * @email ItZheng@ZoHo.com
 * @date 2016/8/26 0026
 */
public class ThreadUtils {
    private ThreadUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    private static ExecutorService threadPool;

    /**
     * 获取线程池
     *
     * @return
     */
    private static ExecutorService getThreadPool() {
        if (threadPool == null)
            threadPool = Executors.newCachedThreadPool();
        return threadPool;
    }

    /**
     * 在子线程执行
     *
     * @param command
     */
    public static void execute(Runnable command) {
        getThreadPool().execute(command);
    }

    /**
     * 在子线程中延迟执行
     *
     * @param r
     * @param delayMillis
     */
    public static void executeDelayed(final Runnable r, final long delayMillis) {
        execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(delayMillis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                execute(r);
            }
        });
    }

    /**
     * 在UI线程中执行
     *
     * @param command
     */
    public static void runOnUiThread(final Runnable command) {
        execute(new Runnable() {
            @Override
            public void run() {
                //Only one Looper may be created per thread
                if (Looper.myLooper() == null)
                    Looper.prepare();
                new Handler(Looper.getMainLooper()).post(command);
            }
        });
    }

    /**
     * 在主线程延迟执行
     *
     * @param command
     * @param delayMillis
     */
    public static void runOnUiThreadDelayed(final Runnable command, final long delayMillis) {
        executeDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(command);
            }
        }, delayMillis);
    }

    /**
     * 是否在主线程
     *
     * @return
     */
    public static boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }
}
