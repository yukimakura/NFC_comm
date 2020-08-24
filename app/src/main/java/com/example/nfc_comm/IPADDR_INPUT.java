package com.example.nfc_comm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;


public class IPADDR_INPUT extends DialogFragment {

    // ダイアログが生成された時に呼ばれるメソッド ※必須
    public Dialog onCreateDialog(Bundle savedInstanceState){
        // ダイアログ生成  AlertDialogのBuilderクラスを指定してインスタンス化します
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        // タイトル設定
        dialogBuilder.setTitle("IPアドレス入力");
        // 表示する文章設定
        dialogBuilder.setMessage("サーバーのIPアドレスを入力");

        // 入力フィールド作成
        final EditText editText = new EditText(getActivity());
        dialogBuilder.setView(editText);

        // OKボタン作成
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // editTextの内容を元画面に反映する
                // editTextから値を取得
                String returnValue = editText.getText().toString();
                // MainActivityのインスタンスを取得
                MainActivity mainActivity = (MainActivity) getActivity();
                // ipaddressをセーブ
                SharedPreferences.Editor ipaddr_editor = mainActivity.dataStore.edit();
                ipaddr_editor.putString("ipaddr", returnValue);
                ipaddr_editor.apply();

                Toast.makeText(mainActivity, "変更を反映するにはアプリを再起動してください", Toast.LENGTH_SHORT).show();
            }
        });

        // キャンセル作成
        dialogBuilder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 何もしないで閉じる
            }
        });

        // dialogBulderを返す
        return dialogBuilder.create();
    }
}