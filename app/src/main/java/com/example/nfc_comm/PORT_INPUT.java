package com.example.nfc_comm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;


public class PORT_INPUT extends DialogFragment {

    // ダイアログが生成された時に呼ばれるメソッド ※必須
    public Dialog onCreateDialog(Bundle savedInstanceState){
        // ダイアログ生成  AlertDialogのBuilderクラスを指定してインスタンス化します
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        // タイトル設定
        dialogBuilder.setTitle("ポート番号入力");
        // 表示する文章設定
        dialogBuilder.setMessage("サーバーのアプリのポートを入力");

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
                try{
                    // portをセーブ
                    SharedPreferences.Editor port_editor = mainActivity.dataStore.edit();
                    port_editor.putString("port", Integer.toString(Integer.parseInt(returnValue)));
                    port_editor.apply();

                    Toast.makeText(mainActivity, "変更を反映するにはアプリを再起動してください", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Toast.makeText(mainActivity, "1025~65535の整数値を入力してください", Toast.LENGTH_SHORT).show();
                }

            }
        });

        // キャンセルボタン作成
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