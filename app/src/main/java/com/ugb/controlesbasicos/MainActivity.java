package com.ugb.controlesbasicos;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TabHost tbh;
    TextView tempval;
    Spinner spn;
    Button btnArea;
    conversores miObj = new conversores();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tbh = findViewById(android.R.id.tabcontent); // Corrección aquí
        tbh.setup();
        tbh.addTab(tbh.newTabSpec("ARA").setContent(R.id.tabArea).setIndicator("AREA", null));

        btnArea = findViewById(R.id.btnConvertirArea);
        btnArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    spn = findViewById(R.id.spnDEArea);
                    int de = spn.getSelectedItemPosition();
                    spn = findViewById(R.id.spnAArea);
                    int a = spn.getSelectedItemPosition();
                    tempval = findViewById(R.id.txtCantidadArea);
                    double cantidad = Double.parseDouble(tempval.getText().toString());
                    double resp = miObj.convertir(0, de, a, cantidad);
                    Toast.makeText(getApplicationContext(), "Respuesta:" + resp, Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    class conversores {
        double[][] valores = {
               //area
                {1, 10.7639, 1.431, 1.19599, 0.000247105, 0.0000132, 9.2903e-6}

        };
        public double convertir(int opcion, int de, int a, double cantidad) {
            return valores[opcion][a] / valores[opcion][de] * cantidad;
        }
    }
}

