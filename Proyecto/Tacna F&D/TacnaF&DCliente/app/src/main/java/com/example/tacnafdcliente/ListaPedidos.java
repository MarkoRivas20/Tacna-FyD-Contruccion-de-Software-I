package com.example.tacnafdcliente;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.tacnafdcliente.Adaptador.PedidoAdapter;
import com.example.tacnafdcliente.Model.Establecimiento;
import com.example.tacnafdcliente.Model.Pedido;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;


public class ListaPedidos extends Fragment {


    public ListaPedidos() {
        // Required empty public constructor
    }
    private List<Pedido> Lista_Pedido = new ArrayList<Pedido>();

    Button Btnatras;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;

    private RecyclerView Recycler_View;
    private PedidoAdapter Adaptador;
    private RecyclerView.LayoutManager Layout_Manager;

    AlertDialog Alert_Dialog;

    ResultSet Result_Set;

    ArrayList<Establecimiento> Establecimientos;

    String Nombre_Cliente = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_lista_pedidos, container, false);

        inicializarfirebase();

        Nombre_Cliente = GetFromSharedPreferences("nombre") + " " + GetFromSharedPreferences("apellido");

        Alert_Dialog = new SpotsDialog.Builder()
                .setContext(getActivity())
                .setMessage("Espere")
                .setCancelable(false)
                .build();

        Recycler_View = (RecyclerView) v.findViewById(R.id.Recycler_ListaPedido) ;

        Btnatras = (Button) v.findViewById(R.id.btnatras);
        Btnatras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PantallaPrincipal pantallaPrincipal = new PantallaPrincipal();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, pantallaPrincipal);
                transaction.commit();

            }
        });

        BuscarEstablecimiento();


        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    if (keyCode == KeyEvent.KEYCODE_BACK)
                    {

                        return true;
                    }
                    else
                    {

                    }
                }
                else
                {

                }
                return false;
            }
        });

        databaseReference.child("Pedido").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Lista_Pedido.clear();
                for(DataSnapshot objdatasnapshot : dataSnapshot.getChildren())
                {
                    Pedido p = objdatasnapshot.getValue(Pedido.class);

                    if(p.getUsuario_Cliente().equals(Nombre_Cliente))
                    {
                        for(int i = 0; i < Establecimientos.size(); i++){

                            if(Establecimientos.get(i).getID_Establecimiento() == p.getID_Establecimiento()){
                                p.setNombre_Establecimiento(Establecimientos.get(i).getNombre());
                                Lista_Pedido.add(p);
                                break;

                            }

                        }
                    }
                    else
                    {

                    }

                    Recycler_View.setHasFixedSize(true);
                    Layout_Manager = new LinearLayoutManager(getActivity());
                    Recycler_View.setLayoutManager(Layout_Manager);

                    Adaptador = new PedidoAdapter(Lista_Pedido,getActivity());

                    Adaptador.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(Lista_Pedido.get(Recycler_View.getChildAdapterPosition(v)).getEstado().equals("En Curso"))
                            {
                                SavePedidoSharedPreferences(String.valueOf(Lista_Pedido.get(Recycler_View.getChildAdapterPosition(v)).getID_Pedido()),
                                        Lista_Pedido.get(Recycler_View.getChildAdapterPosition(v)).getUsuario_Cliente(),
                                        Lista_Pedido.get(Recycler_View.getChildAdapterPosition(v)).getDescripcion(),
                                        Lista_Pedido.get(Recycler_View.getChildAdapterPosition(v)).getFecha(),
                                        Lista_Pedido.get(Recycler_View.getChildAdapterPosition(v)).getDireccion_Destino(),
                                        String.valueOf(Lista_Pedido.get(Recycler_View.getChildAdapterPosition(v)).getPrecio_Total()),
                                        Lista_Pedido.get(Recycler_View.getChildAdapterPosition(v)).getPuntoGeografico_Destino());

                                SeguimientoPedido seguimientoPedido = new SeguimientoPedido();
                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                transaction.replace(R.id.contenedorfragment, seguimientoPedido);
                                transaction.commit();
                            }
                            else
                            {

                            }

                        }
                    });

                    Recycler_View.setAdapter(Adaptador);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return v;
    }

    private void inicializarfirebase(){
        FirebaseApp.initializeApp(getActivity().getApplicationContext());
        firebaseDatabase= FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();
        storageReference= FirebaseStorage.getInstance().getReference();
        firebaseStorage=FirebaseStorage.getInstance();
    }

    public Connection ConectarDB(){

        Connection cnn = null;
        try {

            StrictMode.ThreadPolicy politica = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(politica);

            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            //cnn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.168.0.2;databaseName=dbtacnafyd;user=sa;password=upt;");
            cnn= DriverManager.getConnection("jdbc:jtds:sqlserver://tacnafyd.database.windows.net:1433;databaseName=TacnaFyD;user=MarkoRivas;password=Tacna2018.;encrypt=true;trustServerCertificate=false;hostNameInCertificate=ContruccionI.database.windows.net;loginTimeout=30;");


        }catch (Exception e){

        }

        return cnn;
    }

    public void BuscarEstablecimiento(){

        try {
            Statement stm2 = ConectarDB().createStatement();
            Result_Set = stm2.executeQuery("select * from Establecimiento");
            Establecimientos = new ArrayList<>();
            while (Result_Set.next()){
                Establecimiento establecimiento = new Establecimiento();
                establecimiento.setID_Establecimiento(Result_Set.getInt(1));
                establecimiento.setNombre(Result_Set.getString(3));
                Establecimientos.add(establecimiento);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void SavePedidoSharedPreferences(String ID, String nombre_cliente, String descripcion_pedido, String fecha_pedido, String direccion_pedido,String precio_total, String Punto_Geografico){
        SharedPreferences sharedPref = getActivity().getSharedPreferences("info_pedido", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("ID", ID);
        editor.putString("nombre_cliente", nombre_cliente);
        editor.putString("descripcion_pedido", descripcion_pedido);
        editor.putString("fecha_pedido", fecha_pedido);
        editor.putString("direccion_pedido", direccion_pedido);
        editor.putString("precio_total", precio_total);
        editor.putString("Punto_Geografico", Punto_Geografico);
        editor.apply();
    }

    private String GetFromSharedPreferences (String Key){
        SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences("login_usuario", Context.MODE_PRIVATE);
        return sharedPref.getString(Key,"");
    }
}
