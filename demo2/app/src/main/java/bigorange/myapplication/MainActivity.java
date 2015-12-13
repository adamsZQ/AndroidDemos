package bigorange.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String UPPER_NUM = "upper";
    private EditText etNum;
    private Button button;
    private CalThread calThread;

    class CalThread extends Thread {
        public Handler mHandler;
        public void run() {
            Looper.prepare();
            mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    if (msg.what == 0x123) {
                        int upper = msg.getData().getInt(UPPER_NUM);
                        List<Integer> nums = new ArrayList<Integer>();
                        outer:
                        for (int i = 2; i <= upper; i++) {
                            for (int j = 2; j <= Math.sqrt(i); j++) {
                                if (i != 2 && i % j == 0) {
                                    continue outer;
                                }
                            }
                            nums.add(i);
                        }
                        Toast.makeText(MainActivity.this, nums.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            };
            Looper.loop();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etNum = (EditText) findViewById(R.id.etNum);
        button = (Button) findViewById(R.id.button);

        calThread = new CalThread();
        calThread.start();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                msg.what = 0x123;
                Bundle bundle = new Bundle();
                bundle.putInt(UPPER_NUM,
                        Integer.parseInt(etNum.getText().toString()));
                msg.setData(bundle);
                calThread.mHandler.sendMessage(msg);
            }
        });
    }

}
