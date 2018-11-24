package space.cc.com.fragmenttest.activity.media;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import space.cc.com.fragmenttest.R;
import space.cc.com.fragmenttest.activity.BaseActivity;

public class NotificationDisplayActivity extends BaseActivity {

    private TextView notifyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_display);
        Intent intent=getIntent();
        String notifyMsg=intent.getStringExtra("notifyMsg");
        notifyText=findViewById(R.id.notifyText);
        notifyText.setText(notifyMsg);
    }
}
