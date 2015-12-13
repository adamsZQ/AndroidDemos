package bigorange.demo5;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {
    private TextView show;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        show = (TextView) findViewById(R.id.show);
        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownTask task = new DownTask(MainActivity.this);
                try {
                    task.execute(new URL("http://www.baidu.com"));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    class DownTask extends AsyncTask<URL, Integer, String> {
        private ProgressDialog progressDialog;
        private int hasRead = 0;
        private Context mContext;

        public DownTask(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(URL... params) {
            StringBuilder stringBuilder = new StringBuilder();
            try {
                URLConnection connection = params[0].openConnection();
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), "utf-8"));
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                    hasRead++;
                    publishProgress(hasRead);
                }
                return stringBuilder.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            show.setText(result);
            progressDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setTitle("任务正在执行中");
            progressDialog.setMessage("任务正在执行中，敬请期待...");
            // 设置对话框不能用"取消"按钮关闭
            progressDialog.setCancelable(false);
            progressDialog.setMax(202);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            // 设置对话框的进度条是否显示进度
            progressDialog.setIndeterminate(false);
            progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            show.setText("已经读取了【" + values[0] + "】行！");
            progressDialog.setProgress(values[0]);
        }
    }

}
