package com.example.defangfang.getipinfoonandroid;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    static final String TAG = "MainActivity";
    public static final int UPDATE_TEXT = 1;
    Button mButton;
    TextView textview;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case UPDATE_TEXT:
                    IPInfomation ipinfo = new IPInfomation(MainActivity.this);
                    String ipadd = ipinfo.getWIFILocalIpAdress();
                    String macadd = ipinfo.getMacAddress();
                    //String hostip = ipinfo.getHostAddress();
                    Log.i(TAG, "onClick: sfafdasf");
                    //textview.setText("内网ip为："+ipadd+"\nMAC地址为："+macadd);
                    String hostip = (String) msg.obj;
                    //String ipinfo = (String)textview.getText();
                    textview.setText("内网ip为："+ipadd+"\nMAC地址为："+macadd+"\n网关地址为："+hostip);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton = (Button) findViewById(R.id.button);
        textview = (TextView) findViewById(R.id.textView);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        URL infoUrl = null;
                        InputStream inStream = null;
                        Log.i(TAG, "haha");
                        try
                        {
                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder().url("http://1212.ip138.com/ic.asp").build();
                            Response response = client.newCall(request).execute();
                            String responseData = response.body().string();
                            responseData = Html.fromHtml(responseData).toString();
                            String hostIp = responseData.substring(responseData.indexOf("[") + 1, responseData.indexOf("]"));
                            Log.i(TAG, hostIp);
                            Message message = new Message();
                            message.what = UPDATE_TEXT;
                            message.obj = hostIp;
                            mHandler.sendMessage(message);
                        }
                        catch(MalformedURLException e) {
                            e.printStackTrace();
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                        String IP = "";
                        try
                        {
                            String address = "http://ip.taobao.com/service/getIpInfo2.php?ip=myip";
                            URL url = new URL(address);
                            HttpURLConnection connection = (HttpURLConnection) url
                                    .openConnection();
                            connection.setUseCaches(false);

                            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK)
                            {
                                InputStream in = connection.getInputStream();

// 将流转化为字符串
                                BufferedReader reader = new BufferedReader(
                                        new InputStreamReader(in));

                                String tmpString = "";
                                StringBuilder retJSON = new StringBuilder();
                                while ((tmpString = reader.readLine()) != null)
                                {
                                    retJSON.append(tmpString + "\n");
                                }
                                Log.i(TAG, retJSON.toString());
                                JSONObject jsonObject = new JSONObject(retJSON.toString());
                                String code = jsonObject.getString("code");
                                if (code.equals("0"))
                                {
                                    JSONObject data = jsonObject.getJSONObject("data");
                                    IP = data.getString("ip") + "(" + data.getString("country")
                                            + data.getString("area") + "区"
                                            + data.getString("region") + data.getString("city")
                                            + data.getString("isp") + ")";

                                    Log.e(TAG, "您的IP地址是：" + IP);
                                }
                                else
                                {
                                    IP = "";
                                    Log.e(TAG, "IP接口异常，无法获取IP地址！");
                                }
                            }
                            else
                            {
                                IP = "";
                                Log.e(TAG, "网络连接异常，无法获取IP地址！");
                            }
                        }
                        catch (Exception e)
                        {
                            IP = "";
                            Log.e(TAG, "获取IP地址时出现异常，异常信息是：" + e.toString());
                        }
                    }
                }).start();

            }
        });
        Log.i(TAG, "onCreate: ");
    }
}
