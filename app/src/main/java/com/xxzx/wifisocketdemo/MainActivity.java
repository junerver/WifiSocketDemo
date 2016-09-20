package com.xxzx.wifisocketdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.etContent)
    EditText mEtContent;
    @BindView(R.id.btnSend)
    Button mBtnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SocketService.startMe(this);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnSend)
    public void onClick() {

        String postMsgStr=mEtContent.getText().toString().trim();
        //内容不为空则提交事件
        if (!TextUtils.isEmpty(postMsgStr)) {
            EventBus.getDefault().post(new PostMsgEvent(postMsgStr));
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SocketService.stopMe(this);
    }
}
