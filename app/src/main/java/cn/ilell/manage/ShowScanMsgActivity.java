package cn.ilell.manage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class ShowScanMsgActivity extends AppCompatActivity {

    private String code;
    private TextView tv;
    private Button btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_scan_msg);

        Intent intent = getIntent();
        code = intent.getStringExtra("code");

        initView();
        setListener();
        setValue();
    }

    public void initView() {
        tv = (TextView) findViewById(R.id.show_scan_msg);
        btn = (Button) findViewById(R.id.rescan);
    }

    public void setListener() {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowScanMsgActivity.this.finish();
            }
        });
    }

    public void setValue() {
        //Map<String, String> map = new HashMap<String, String>();
        //map.put("code", code);
        //String result = HttpXmlClient.post(MyConfig.SERVER_URL, map);
        tv.setText("条码: " + code);
    }
}
