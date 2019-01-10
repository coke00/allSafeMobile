package com.example.juancarlos.eppcontrol;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EntregaActivity extends AppCompatActivity {
    private Button btnPop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrega);

        btnPop = (Button)findViewById(R.id.btnPop);
        btnPop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EntregaActivity.this,PopUpActivity.class));
            }
        });
    }
}
