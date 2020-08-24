package com.example.nfc_comm;
import androidx.appcompat.app.AppCompatActivity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    // アダプタを扱うための変数
    private NfcAdapter mNfcAdapter;
    public SharedPreferences dataStore;

    public String IPADDR;
    public int PORT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // アダプタのインスタンスを取得
        mNfcAdapter = android.nfc.NfcAdapter.getDefaultAdapter(this);
        dataStore  =  getSharedPreferences("DataStore", MODE_PRIVATE); //変数保存用

        IPADDR = dataStore.getString("ipaddr", "192.168.0.104");
        PORT = Integer.parseInt(dataStore.getString("port", "12345"));

        TextView port_text = findViewById(R.id.port);
        port_text.setText("ポート番号:"+PORT);
        TextView ip_text = findViewById(R.id.ipaddr);
        ip_text.setText("IPアドレス:"+IPADDR);


    }

    @Override
    protected void onResume(){

        super.onResume();

        // NFCがかざされたときの設定
        Intent intent = new Intent(this, this.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        // ほかのアプリを開かないようにする
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        mNfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);

    }

    @Override
    protected void onPause(){
        super.onPause();

        // Activityがバックグラウンドになったときは、受け取らない
        mNfcAdapter.disableForegroundDispatch(this);

    }

    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);

        // NFCのUIDを取得
        final byte[] uid = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
        // 表示
        Toast.makeText(this, Arrays.toString(uid), Toast.LENGTH_SHORT).show();
        Runnable sender = new Runnable() {
            @Override
            public void run() {
                String address = IPADDR;
                int port = PORT;
                Socket socket = null;
                try {
                    socket = new Socket(address, port);
                    PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
                    String text = Arrays.toString(uid);
                    pw.println("{ \"data\": " + text + " }\n");
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (socket != null) {
                    try {
                        socket.close();
                        socket = null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Thread th = new Thread(sender);
        th.start();

    }

    // オプションメニューを作成する
    public boolean onCreateOptionsMenu(Menu menu){
        // menuにcustom_menuレイアウトを適用
        getMenuInflater().inflate(R.menu.custom_menu, menu);
        // オプションメニュー表示する場合はtrue
        return true;
    }
    // メニュー選択時の処理　今回はトースト表示
    public boolean onOptionsItemSelected(MenuItem menuItem){
        Toast toast;

        // 押されたメニューのIDで処理を振り分ける
        switch (menuItem.getItemId()){
            case R.id.action_ipaddr:
                // ダイアログクラスをインスタンス化
                IPADDR_INPUT ipaddr_dialog = new IPADDR_INPUT();
                // 表示  getFagmentManager()は固定、sampleは識別タグ
                ipaddr_dialog.show(getFragmentManager(), "ipaddr");

                break;

            case R.id.action_port:
                // ダイアログクラスをインスタンス化
                PORT_INPUT port_dialog = new PORT_INPUT();
                // 表示  getFagmentManager()は固定、sampleは識別タグ
                port_dialog.show(getFragmentManager(), "port");

                break;
            default:
                break;
        }


        return true;
    }

}
