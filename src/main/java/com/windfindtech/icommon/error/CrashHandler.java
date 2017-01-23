package com.windfindtech.icommon.error;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import com.windfindtech.icommon.R;
import com.windfindtech.icommon.jsondata.webservice.BaseResponse;
import com.windfindtech.icommon.jsondata.webservice.WSErrorResponse;
import com.windfindtech.icommon.logger.WFTLogger;
import com.windfindtech.icommon.webservice.WSCallback;

import org.pmw.tinylog.Logger;

import java.io.File;
import java.lang.Thread.UncaughtExceptionHandler;

public class CrashHandler implements UncaughtExceptionHandler {
    //private final static String TAG = "UncaughtExceptionHandler";
//    private static final int DEEP_STACK_DEPTH = 15;
//    private static final int SHALLOW_STACK_DEPTH = 5;
    private static CrashHandler s_instance;
    private Thread.UncaughtExceptionHandler m_defaultHandler;
    private Context m_context;

    private CrashHandler() {
    }

    /**
     * the CrashHandler to manipulate all critical errors in app
     */
    public static CrashHandler getInstance() {
        if (s_instance == null)
            s_instance = new CrashHandler();
        return s_instance;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        if (!handleException(throwable) && m_defaultHandler != null) {
            // 未处理的交给默认handler处理
            m_defaultHandler.uncaughtException(thread, throwable);
        }
        else {
            // Sleep a while before exit app
            try {
                Thread.sleep(6000);
            }
            catch (InterruptedException e) {
                Logger.error(e);
            }
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(10);
        }
    }

    private boolean handleException(final Throwable ex) {
        if (ex == null) {
            return true;
        }
//        final String msg = String.format("%s: %s", ex.getClass().getSimpleName(), ex.getMessage());
        // 显示Toast提示错误信息，然后等待
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(m_context, m_context.getString(R.string.fatal_error), Toast.LENGTH_LONG).show();
                Logger.error("oops~.~, we meet a crash");
//                logger.error(String.format("Exception: %s", msg));

                Throwable cause = ex;
                while (cause != null && cause.getClass() != null) {
                    Logger.error(cause);
//                    Logger.error(String.format("%s %s", cause.getClass().getSimpleName(), cause.toString()));
//                    StackTraceElement[] stack_elems = cause.getStackTrace();
//                    int depth = DEEP_STACK_DEPTH;
////                    if(stack_elems.length > 0){
////                        if(stack_elems[0].toString().startsWith(m_context.getString(R.string.company_package_name_prefix))){
////                            depth = DEEP_STACK_DEPTH;
////                        }
////                    }
//                    depth = stack_elems.length > depth ? depth : stack_elems.length;
//                    for (int i = 0; i < depth; i++) {
//                        Logger.error(stack_elems[i].toString());
//                    }
                    cause = cause.getCause();
                }

                final File log_file = WFTLogger.getLatestLogFile();
                if (log_file != null) {
                    ErrorSubmit.sendAppLogs(log_file, new WSCallback<BaseResponse>() {
                        @Override
                        public void onSuccess(BaseResponse ret) {

                        }

                        @Override
                        public void onFailed(WSErrorResponse reason) {

                        }
                    });
                }
//                logger.error(String.format("Exception: %s \r\n%s", msg, ex.get));
                Looper.loop();
            }
        }).start();
        return true;
    }

    public void init(Context context) {
        m_context = context;
        m_defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }
}
