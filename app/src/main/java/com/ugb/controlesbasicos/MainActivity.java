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
    Button btnLongitud;

    conversores miObj = new conversores();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tbh = findViewById(R.id.tbhConversor);
        tbh.setup();
        tbh.addTab(tbh.newTabSpec("LON").setContent(R.id.tabLongitud).setIndicator("LONGITUD", null));


        btnLongitud = findViewById(R.id.btnConvertirLongitud);
        btnLongitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    spn = findViewById(R.id.spnDElongitud);
                    int de = spn.getSelectedItemPosition();
                    spn = findViewById(R.id.spnAlongitud);
                    int a = spn.getSelectedItemPosition();
                    tempval = findViewById(R.id.txtCantidadLongitud);
                    double cantidad = Double.parseDouble(tempval.getText().toString());
                    double resp = miObj.convertir(0, de, a, cantidad);
                    Toast.makeText(getApplicationContext(), "Respuesta:" + resp, Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
