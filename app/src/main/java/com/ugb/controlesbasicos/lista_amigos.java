package com.ugb.controlesbasicos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class lista_amigos extends AppCompatActivity {
    Bundle parametros = new Bundle();
    FloatingActionButton btnAgregarProductos;
    ListView lts;
    Cursor cProductos;
    amigos misClientes;
    DB db;
    final ArrayList<amigos> alProductos = new ArrayList<amigos>();
    final ArrayList<amigos> alProductosCopy = new ArrayList<amigos>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_amigos);

        btnAgregarProductos = findViewById(R.id.fabAgregarProductos);
        btnAgregarProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parametros.putString("accion", "nuevo");
                abrirActividad(parametros);
            }
        });
        obtenerDatosProducto();
        buscarProductos();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mimenu, menu);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        cProductos.moveToPosition(info.position);
        menu.setHeaderTitle(cProductos.getString(1)); //1 es el codigo
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        try {
            if (item.getItemId() == R.id.mnxAgregar) {
                parametros.putString("accion", "nuevo");
                abrirActividad(parametros);

            } else if (item.getItemId() == R.id.mnxModificar) {
                String[] producto = {
                        cProductos.getString(0), //idProductos
                        cProductos.getString(1), //codigo
                        cProductos.getString(2), //descripcion
                        cProductos.getString(3), //marca
                        cProductos.getString(4), //presentacion
                        cProductos.getString(5), //precio
                        cProductos.getString(6), //foto

                };
                parametros.putString("accion", "modificar");
                parametros.putStringArray("amigos", producto);
                abrirActividad(parametros);

            }else if (item.getItemId()==R.id.mnxEliminar){

                eliminarProductos();

            }
            return true;
        }catch (Exception e){
            mostrarMsg("Error al seleccionar una opcion del mennu: "+ e.getMessage());
            return super.onContextItemSelected(item);
        }
    }
    private void eliminarProductos(){
        try{
            AlertDialog.Builder confirmar = new AlertDialog.Builder(lista_amigos.this);
            confirmar.setTitle("Estas seguro de eliminar a: ");
            confirmar.setMessage(cProductos.getString(1)); //1 es el codigo
            confirmar.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String respuesta = db.administrar_amigos("eliminar", new String[]{cProductos.getString(0)});//0 es el idProductos
                    if(respuesta.equals("ok")){
                        mostrarMsg("Producto eliminado con exito");
                        obtenerDatosProducto();
                    }else{
                        mostrarMsg("Error al eliminar el producto: "+ respuesta);
                    }
                }
            });
            confirmar.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            confirmar.create().show();
        }catch (Exception e){
            mostrarMsg("Error al eliminar producto: "+ e.getMessage());
        }
    }
    private void abrirActividad(Bundle parametros){
        Intent abrirActividad = new Intent(getApplicationContext(), MainActivity.class);
        abrirActividad.putExtras(parametros);
        startActivity(abrirActividad);
    }
    private void obtenerDatosProducto(){
        try {
            alProductos.clear();
            alProductosCopy.clear();

            db = new DB(lista_amigos.this, "", null, 1);
            cProductos = db.consultar_amigos();

            if( cProductos.moveToFirst() ){
                lts = findViewById(R.id.ltsProductos);
                do{
                    misClientes = new amigos(
                            cProductos.getString(0),//idProducto
                            cProductos.getString(1),//codigo
                            cProductos.getString(2),//descripcion
                            cProductos.getString(3),//marca
                            cProductos.getString(4),//presentacion
                            cProductos.getString(5),//precio
                            cProductos.getString(6)//foto

                    );
                    alProductos.add(misClientes);
                }while(cProductos.moveToNext());
                alProductosCopy.addAll(alProductos);

                adaptadorImagenes adImagenes = new adaptadorImagenes(lista_amigos.this, alProductos);
                lts.setAdapter(adImagenes);

                registerForContextMenu(lts);
            }else{
                mostrarMsg("No hay Datos de productos que mostrar.");
            }
        }catch (Exception e){
            mostrarMsg("Error al mostrar datos: "+ e.getMessage());
        }
    }
    private void buscarProductos(){
        TextView tempVal;
        tempVal = findViewById(R.id.txtBuscarProductos);
        tempVal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    alProductos.clear();
                    String valor = tempVal.getText().toString().trim().toLowerCase();
                    if( valor.length()<=0 ){
                        alProductos.addAll(alProductosCopy);
                    }else{
                        for (amigos productos : alProductosCopy){
                            String codigo = productos.getCodigo();
                            String descripcion = productos.getDescripcion();
                            String marca = productos.getMarca();
                            String presentacion = productos.getPresentacion();
                            String precio = productos.getPrecio();
                            if(codigo.toLowerCase().trim().contains(valor) ||
                                    descripcion.toLowerCase().trim().contains(valor) ||
                                    marca.trim().contains(valor) ||
                                    presentacion.trim().toLowerCase().contains(valor) ||
                                    precio.trim().contains(valor)){
                                alProductos.add(productos);
                            }
                        }
                        adaptadorImagenes adImagenes = new adaptadorImagenes(getApplicationContext(), alProductos);
                        lts.setAdapter(adImagenes);
                    }
                }catch (Exception e){
                    mostrarMsg("Error al buscar: "+ e.getMessage());
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {


            }
        });
    }
    private void mostrarMsg(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}