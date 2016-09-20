package com.xxzx.wifisocketdemo;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class SocketService extends Service {

    private static final String SERVER_IP = "192.168.4.1";
    private static final int SERVER_PORT = 333;
    private Socket mSocket;
    private BufferedReader mBufferedReader;
    private BufferedWriter mBufferedWriter;


    //封装启动\关闭方式为服务自身的静态方法
    public static void startMe(Context context) {
        Intent service = new Intent(context, SocketService.class);
        context.startService(service);
    }

    public static void stopMe(Context context) {
        context.stopService(new Intent(context, SocketService.class));
    }

    public SocketService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //注册总线
        Logger.d("服务创建！");
        EventBus.getDefault().register(this);
    }

    @Override
    public int onStartCommand(Intent intent, final int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //连接服务器
                    mSocket = new Socket(SERVER_IP, SERVER_PORT);
                    Logger.d("创建套接字成功");
                    mBufferedReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
                    mBufferedWriter = new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream()));
                } catch (IOException e) {
                    e.printStackTrace();
                    Logger.d("创建套接字连接失败");
                }
                //从bufferedreader中不断读取服务器传来的数据
                while (true) {
                    try {
                        String serverMsgStr;    //服务器返回数据
                        while ((serverMsgStr = mBufferedReader.readLine()) != null) {

                            Logger.d("已经从服务器接收到数据"+serverMsgStr );
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Subscribe
    public void onPostMsgEvent(PostMsgEvent event) {
        Logger.d("收到UI传来的数据"+event.toString());
        if (event!=null) {
            //向socket主机提交数据
            String postMsgStr = event.getMessage() + "\n";
            try {
                mBufferedWriter.write(postMsgStr);
                mBufferedWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            mBufferedReader.close();
            mBufferedWriter.close();
            mSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
            Logger.d("Socket结束故障");
        }
    }
}
