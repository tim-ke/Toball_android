package com.occity.toball;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "FirestoreExample";

    private FirebaseFirestore db ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        FirebaseApp.initializeApp(this);  // 確保已初始化 Firebase
        db = FirebaseFirestore.getInstance();// 初始化 Firestore
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //登入按鈕
        Button button = findViewById(R.id.button);
        EditText ed_account = findViewById(R.id.account);
        EditText ed_password = findViewById(R.id.password);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(MainActivity.this, "test", Toast.LENGTH_LONG).show();

                // 讀取指定 ID 的文件
                db.collection("users").document("ADMIN")
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {

                                // 取得 Firestore 中的帳號和密碼
                                String storedAccount = documentSnapshot.getString("account");
                                String storedPassword = documentSnapshot.getString("password");
                                String storedName = documentSnapshot.getString("name");

                                // 取得 EditText 中的輸入
                                String inputAccount = ed_account.getText().toString();
                                String inputPassword = ed_password.getText().toString();


                                if(inputPassword.equals(storedAccount) && inputAccount.equals(storedPassword)){
                                    // 登入成功，跳轉到下一個頁面並傳遞值
                                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                    intent.putExtra("account", inputAccount);
                                    intent.putExtra("password", inputPassword);
                                    intent.putExtra("name", storedName);
                                    startActivity(intent);
                                    finish(); // 結束當前頁面
                                }else{
                                    Toast.makeText(MainActivity.this, "帳號或密碼錯誤", Toast.LENGTH_LONG).show();
                                    Log.d(TAG, "帳號或密碼錯誤");
                                    // 提示用戶帳號或密碼錯誤
                                }
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        })
                        .addOnFailureListener(e ->
                                Log.w(TAG, "Error getting document", e)
                        );



//                    finish();
            }
        });

    }
}