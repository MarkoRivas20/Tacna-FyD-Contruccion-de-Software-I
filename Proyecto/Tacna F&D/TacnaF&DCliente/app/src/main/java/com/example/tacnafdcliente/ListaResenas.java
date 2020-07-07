package com.example.tacnafdcliente;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.widget.ViewUtils;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tacnafdcliente.Adaptador.ReseñasAdapter;
import com.example.tacnafdcliente.Model.Cupon;
import com.example.tacnafdcliente.Model.Reseñas;
import com.squareup.picasso.Picasso;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import dmax.dialog.SpotsDialog;


public class ListaResenas extends Fragment {


    public ListaResenas() {
        // Required empty public constructor
    }

    String Nombre = "";
    String Distrito = "";
    String Categoria = "";
    String Capacidad = "";

    String bid_establecimiento = "";
    String bnombre = "";
    String bdistrito = "";
    String bcategoria = "";
    String bdireccion = "";
    String btelefono = "";
    String bdescripcion = "";
    String bcapacidad = "";
    String btotalresenas = "";
    String bpuntuacion = "";
    String burl_imagen_logo = "";
    String bpuntogeografico = "";
    String bestado = "";

    boolean Booleano = false;

    ImageView Img_Logo;

    Button Btndescripcion;
    Button Btnperfil;
    Button Btnprincipal;
    Button Btnruta;
    Button Btncupon;
    Button Btnmenu;
    Button Btnpublicar;


    TextView Txtnombre;
    TextView Lbltotal;
    TextView Lblpuntuacion;

    RatingBar Ratingbar_Calificacion_Total;
    RatingBar Ratingbar_Calificacion;

    EditText Txtcomentario;

    private RecyclerView Recycler_View;
    private ReseñasAdapter Adaptador;
    private RecyclerView.LayoutManager Layout_Manager;

    List<Reseñas> Items_Resena;

    List<Cupon> Items_Cupon;

    ProgressBar Progress_Bar_1;
    ProgressBar Progress_Bar_2;
    ProgressBar Progress_Bar_3;
    ProgressBar Progress_Bar_4;
    ProgressBar Progress_Bar_5;

    int Contador1 = 0;
    int Contador2 = 0;
    int Contador3 = 0;
    int Contador4 = 0;
    int Contador5 = 0;

    ResultSet Result_Set_1;
    ResultSet Result_Set_2;
    ResultSet Result_Set_3;
    AlertDialog Alert_Dialog;

    String Mi_Comentario = "";
    Float Mi_Puntuacion = (float) 0.0;

    String Fecha_Actual = "";
    String Id_Usuario = "";

    Double Puntuacion_Total = 0.0;

    int Total_Resenas = 0;

    final Bundle bundle2 = new Bundle();

    int Nro_Cupones = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_lista_resenas, container, false);

        Id_Usuario = GetFromSharedPreferences("ID");
        Nro_Cupones = GetNroCuponesFromSharedPreferences("NroCupones");

        Recycler_View = (RecyclerView) v.findViewById(R.id.Recycler_resenas) ;
        Progress_Bar_1 = (ProgressBar) v.findViewById(R.id.progress);
        Progress_Bar_2 = (ProgressBar) v.findViewById(R.id.progress2);
        Progress_Bar_3 = (ProgressBar) v.findViewById(R.id.progress3);
        Progress_Bar_4 = (ProgressBar) v.findViewById(R.id.progress4);
        Progress_Bar_5 = (ProgressBar) v.findViewById(R.id.progress5);

        Txtcomentario = (EditText) v.findViewById(R.id.txtcomentario);
        Ratingbar_Calificacion = (RatingBar) v.findViewById(R.id.rbcalificacion);
        Ratingbar_Calificacion_Total = (RatingBar) v.findViewById(R.id.ratingbarlistaresenas);
        Lblpuntuacion = (TextView) v.findViewById(R.id.lblpuntuacion);
        Lbltotal = (TextView) v.findViewById(R.id.lbltotal);

        Alert_Dialog = new SpotsDialog.Builder()
                .setContext(getActivity())
                .setMessage("Espere")
                .setCancelable(false)
                .build();

        Bundle bundle = getArguments();

        bid_establecimiento = bundle.getString("id_establecimiento");
        bnombre = bundle.getString("nombre");
        bdistrito = bundle.getString("distrito");
        bcategoria = bundle.getString("categoria");
        bdireccion = bundle.getString("direccion");
        btelefono = bundle.getString("telefono");
        bdescripcion = bundle.getString("descripcion");
        bcapacidad = bundle.getString("capacidad");
        btotalresenas = bundle.getString("totalresenas");
        bpuntuacion = bundle.getString("puntuacion");
        burl_imagen_logo = bundle.getString("url_imagen_logo");
        bpuntogeografico = bundle.getString("puntogeografico");
        bestado = bundle.getString("estado");
        Nombre = bundle.getString("nombreb");
        Distrito = bundle.getString("distritob");
        Categoria = bundle.getString("categoriab");
        Capacidad = bundle.getString("capacidadb");
        Booleano = bundle.getBoolean("banderaresena");
        Mi_Comentario = bundle.getString("micomentario");
        Mi_Puntuacion = bundle.getFloat("mipuntuacion");

        Img_Logo = (ImageView) v.findViewById(R.id.imglogo);

        Picasso.with(getContext()).load(burl_imagen_logo).into(Img_Logo);

        Txtnombre = (TextView) v.findViewById(R.id.lblnombre);
        Txtnombre.setText(bnombre);

        Lbltotal.setText(btotalresenas);
        Lblpuntuacion.setText(bpuntuacion);
        Ratingbar_Calificacion_Total.setRating(Float.parseFloat(bpuntuacion));



        bundle2.putString("id_establecimiento", bid_establecimiento);
        bundle2.putString("nombre", bnombre);
        bundle2.putString("distrito", bdistrito);
        bundle2.putString("categoria", bcategoria);
        bundle2.putString("direccion", bdireccion);
        bundle2.putString("telefono", btelefono);
        bundle2.putString("descripcion", bdescripcion);
        bundle2.putString("capacidad", bcapacidad);
        bundle2.putString("totalresenas", btotalresenas);
        bundle2.putString("puntuacion", bpuntuacion);
        bundle2.putString("url_imagen_logo", burl_imagen_logo);
        bundle2.putString("puntogeografico", bpuntogeografico);
        bundle2.putString("estado", bestado);
        bundle2.putString("nombreb", Nombre);
        bundle2.putString("distritob", Distrito);
        bundle2.putString("categoriab", Categoria);
        bundle2.putString("capacidadb", Capacidad);
        bundle2.putBoolean("banderaresena", Booleano);
        bundle2.putString("micomentario", Mi_Comentario);
        bundle2.putFloat("mipuntuacion", Mi_Puntuacion);

        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    if (keyCode == KeyEvent.KEYCODE_BACK)
                    {

                        Bundle bundle3 = new Bundle();
                        bundle3.putString("nombre", Nombre);
                        bundle3.putString("distrito", Distrito);
                        bundle3.putString("categoria", Categoria);
                        bundle3.putString("capacidad", Capacidad);

                        ListaEstablecimiento fragmentEstablecimiento = new ListaEstablecimiento();

                        fragmentEstablecimiento.setArguments(bundle3);
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.contenedorfragment, fragmentEstablecimiento);
                        transaction.commit();

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

        Btnperfil = (Button) v.findViewById(R.id.btnperfil);
        Btnperfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerfilUsuario perfilUsuario = new PerfilUsuario();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, perfilUsuario);
                transaction.commit();

            }
        });

        Btnprincipal = (Button) v.findViewById(R.id.btnprincipal);
        Btnprincipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PantallaPrincipal pantallaPrincipal = new PantallaPrincipal();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, pantallaPrincipal);
                transaction.commit();
            }
        });

        Btndescripcion = (Button) v.findViewById(R.id.btndescripcion);
        Btndescripcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                PerfilEstablecimiento perfilEstablecimiento = new PerfilEstablecimiento();
                perfilEstablecimiento.setArguments(bundle2);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, perfilEstablecimiento);
                transaction.commit();
            }
        });

        Btnruta = (Button) v.findViewById(R.id.btnruta);
        Btnruta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                RutaMapa rutaMapa = new RutaMapa();
                rutaMapa.setArguments(bundle2);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, rutaMapa);
                transaction.commit();
            }
        });

        Btncupon = (Button) v.findViewById(R.id.btncupon);
        Btncupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ListaCupon listaCupon = new ListaCupon();
                listaCupon.setArguments(bundle2);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, listaCupon);
                transaction.commit();
            }
        });

        Btnmenu = (Button) v.findViewById(R.id.btnmenu);
        Btnmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ListaItemsMenu listaItemsMenu = new ListaItemsMenu();
                listaItemsMenu.setArguments(bundle2);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, listaItemsMenu);
                transaction.commit();
            }
        });

        String pattern = "dd/MM/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Fecha_Actual = simpleDateFormat.format(new Date());

        Btnpublicar = (Button)v.findViewById(R.id.btnpublicar);
        Btnpublicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Txtcomentario.length() == 0 || Ratingbar_Calificacion.getRating() == 0.0)
                {
                    if (Txtcomentario.length() == 0)
                    {
                        Txtcomentario.setError("Campo Requerido");
                    }
                    else
                    {

                    }
                    if (Ratingbar_Calificacion.getRating() == 0.0)
                    {
                        Toast.makeText(getActivity(),"Puntuacion Requerida", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {

                    }
                }
                else
                {

                    new RegistrarResena (getActivity()).execute(new String[]{"Ingresar"});
                }

            }
        });


        new ListarResenaPorEstablecimiento (getActivity()).execute(new String[]{"Listar"});


        return v;
    }



    public Connection ConnectionDB(){

        Connection cnn = null;
        try {

            StrictMode.ThreadPolicy politica = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(politica);

            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            cnn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.168.0.2;databaseName=dbtacnafyd;user=sa;password=upt;");


        }catch (Exception e){

        }

        return cnn;
    }

    public class ListarResenaPorEstablecimiento extends AsyncTask<String, Integer, Boolean> {


        private Context mContext = null;

        ListarResenaPorEstablecimiento (Context context){
            mContext=context;
        }

        @Override
        protected Boolean doInBackground (String... strings) {




            try {

                Statement stm2 = ConnectionDB().createStatement();
                Result_Set_1 = stm2.executeQuery("select r.ID_Resena,u.Nombre+' '+u.Apellido as Nombre ,r.ID_Establecimiento,r.Fecha,r.Descripcion,r.Calificacion " +
                        "from Resena r inner join Usuario_Cliente u on r.ID_Usuario_Cliente=u.ID_Usuario_Cliente where ID_Establecimiento=" + bid_establecimiento);

            }catch (Exception e){
                Log.e("Error", e.toString());
            }

            return true;
        }

        @Override
        protected  void onPreExecute(){

            Alert_Dialog.show();

        }

        @Override
        protected  void onPostExecute (Boolean result){

            RellenarLista();

            Alert_Dialog.dismiss();


        }


    }

    public void RellenarLista(){
        try {


            Items_Resena = new ArrayList<>();

            while (Result_Set_1.next())
            {
                Reseñas reseñas = new Reseñas();
                reseñas.setID_Resena(Result_Set_1.getInt(1));
                reseñas.setID_Usuario_Cliente(Result_Set_1.getString(2));
                reseñas.setID_Establecimiento(Result_Set_1.getInt(3));
                reseñas.setFecha(Result_Set_1.getDate(4));
                reseñas.setDescripcion(Result_Set_1.getString(5));
                reseñas.setCalificacion(Result_Set_1.getDouble(6));

                Items_Resena.add(reseñas);

                if (Result_Set_1.getDouble(6 ) <= 1.0)
                {
                    Contador1++;
                }
                else if (Result_Set_1.getDouble(6) <= 2.0)
                {
                    Contador2++;
                }
                else if (Result_Set_1.getDouble(6) <= 3.0)
                {
                    Contador3++;
                }
                else if (Result_Set_1.getDouble(6) <= 4.0)
                {
                    Contador4++;
                }
                else if (Result_Set_1.getDouble(6) <= 5.0)
                {
                    Contador5++;
                }


            }


            Recycler_View.setHasFixedSize(true);
            Layout_Manager = new LinearLayoutManager(getActivity());
            Recycler_View.setLayoutManager(Layout_Manager);

            Adaptador = new ReseñasAdapter(Items_Resena, getActivity());


            Recycler_View.setAdapter(Adaptador);


            Progress_Bar_1.setMax(Items_Resena.size());
            Progress_Bar_2.setMax(Items_Resena.size());
            Progress_Bar_3.setMax(Items_Resena.size());
            Progress_Bar_4.setMax(Items_Resena.size());
            Progress_Bar_5.setMax(Items_Resena.size());

            Progress_Bar_1.setProgress(Contador1);
            Progress_Bar_2.setProgress(Contador2);
            Progress_Bar_3.setProgress(Contador3);
            Progress_Bar_4.setProgress(Contador4);
            Progress_Bar_5.setProgress(Contador5);




        }catch (Exception e){
            Log.e("Error", e.toString());
        }
    }

    private String GetFromSharedPreferences (String Key){
        SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences("login_usuario", Context.MODE_PRIVATE);
        return sharedPref.getString(Key,"");
    }

    private void SaveNroCuponesSharedPreferences (int nrocupones){
        SharedPreferences sharedPref = getActivity().getSharedPreferences("Numero_Cupones", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("NroCupones", nrocupones);

        editor.apply();
    }

    private int GetNroCuponesFromSharedPreferences (String Key){
        SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences("Numero_Cupones", Context.MODE_PRIVATE);
        return sharedPref.getInt(Key,0);
    }

    public class RegistrarResena extends AsyncTask<String, Integer, Boolean> {


        private Context mContext2 = null;

        RegistrarResena (Context context2){
            mContext2 = context2;
        }

        @Override
        protected Boolean doInBackground (String... strings) {


            try {

                Statement stm=ConnectionDB().createStatement();
                stm.execute("insert into Resena(ID_Usuario_Cliente,ID_Establecimiento,Descripcion,Calificacion,Fecha) " +
                        "values (" + Id_Usuario+"," + bid_establecimiento+",'" + Txtcomentario.getText().toString() +"',"
                        + Ratingbar_Calificacion.getRating()+",'" + Fecha_Actual+"')");

            }catch (Exception e){
                Log.e("Error", e.toString());
            }

            return true;
        }

        @Override
        protected  void onPreExecute(){

            Alert_Dialog.show();

        }

        @Override
        protected  void onPostExecute (Boolean result){



            new ObtenerTodasResenasPorEstablecimiento (getActivity()).execute(new String[]{"Buscaractual"});

            //mDialog.dismiss();


        }


    }

    public class ObtenerTodasResenasPorEstablecimiento extends AsyncTask <String, Integer, Boolean> {


        private Context mContext3 = null;

        ObtenerTodasResenasPorEstablecimiento (Context context3){
            mContext3=context3;
        }

        @Override
        protected Boolean doInBackground (String... strings) {


            try {

                Statement stm3 = ConnectionDB().createStatement();
                Result_Set_2 = stm3.executeQuery("select * from Resena where ID_Establecimiento=" + bid_establecimiento);


            }catch (Exception e){
                Log.e("Error", e.toString());
            }

            return true;
        }

        @Override
        protected  void onPreExecute(){

            //mDialog.show();

        }

        @Override
        protected  void onPostExecute (Boolean result){



            new ActualizarEstablecimiento (getActivity()).execute(new String[]{"Actualizarresena"});


            //mDialog.dismiss();


        }


    }

    public class ActualizarEstablecimiento extends AsyncTask <String, Integer, Boolean> {


        private Context mContext4 = null;

        ActualizarEstablecimiento (Context context4){
            mContext4=context4;
        }

        @Override
        protected Boolean doInBackground (String... strings) {


            try {

                    while (Result_Set_2.next())
                    {

                        Puntuacion_Total = Puntuacion_Total+Result_Set_2.getDouble(5);
                        Total_Resenas++;
                    }
                    if (Total_Resenas == 0)
                    {
                        Puntuacion_Total = 0.0;
                    }
                    else
                    {
                        Puntuacion_Total = Puntuacion_Total / (Total_Resenas * 1.0);
                    }


                Statement stm=ConnectionDB().createStatement();
                stm.execute("Update Establecimiento set TotalReseñas=" + Total_Resenas+", Puntuacion=" + Puntuacion_Total+
                        " where ID_Establecimiento=" + bid_establecimiento);



            }catch (Exception e){
                Log.e("Error", e.toString());
            }

            return true;
        }

        @Override
        protected  void onPreExecute(){

            //mDialog.show();

        }

        @Override
        protected  void onPostExecute (Boolean result){



            Nro_Cupones++;
            SaveNroCuponesSharedPreferences(Nro_Cupones);

            if (Nro_Cupones >= 5)
            {


                new ListarCuponPorEstado (getActivity()).execute(new String[]{"buscarcu"});


            }
            else
            {

                DecimalFormat df = new DecimalFormat("#.0");

                bundle2.putString("totalresenas", String.valueOf(Total_Resenas));
                bundle2.putString("puntuacion", String.valueOf(df.format(Puntuacion_Total)));
                bundle2.putFloat("mipuntuacion", Ratingbar_Calificacion.getRating());
                bundle2.putString("micomentario", Txtcomentario.getText().toString());
                bundle2.putInt("banderaresena", 1);

                Alert_Dialog.dismiss();

                ListaResenas2 listaResenas2 = new ListaResenas2();
                listaResenas2.setArguments(bundle2);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, listaResenas2);
                transaction.commit();


            }


        }


    }

    public class ListarCuponPorEstado extends AsyncTask <String, Integer, Boolean> {


        private Context mContext5 = null;

        ListarCuponPorEstado (Context context5){
            mContext5 = context5;
        }

        @Override
        protected Boolean doInBackground (String... strings) {


            try {

                Statement stm5 = ConnectionDB().createStatement();
                Result_Set_3 = stm5.executeQuery("Select * from Cupon where Estado='Activo'");



            }catch (Exception e){
                Log.e("Error", e.toString());
            }

            return true;
        }

        @Override
        protected  void onPreExecute(){

            //mDialog.show();

        }

        @Override
        protected  void onPostExecute (Boolean result){

            try {

                Items_Cupon = new ArrayList<>();

                while (Result_Set_3.next())
                {
                    Cupon cupon = new Cupon();
                    cupon.setID_Cupon(Result_Set_3.getInt(1));
                    cupon.setID_Establecimiento(Result_Set_3.getInt(2));
                    cupon.setTitulo(Result_Set_3.getString(3));
                    cupon.setUrl_Imagen(Result_Set_3.getString(4));
                    cupon.setDescripcion(Result_Set_3.getString(5));
                    cupon.setFecha_Inicio(Result_Set_3.getDate(6));
                    cupon.setFecha_Final(Result_Set_3.getDate(7));
                    cupon.setEstado(Result_Set_3.getString(8));
                    Items_Cupon.add(cupon);
                }

            }catch (Exception e){
                Log.e("Error", e.toString());
            }


            new RegistrarCuponUsuario (getActivity()).execute(new String[]{"Obtenercuponalazar"});
            //mDialog.dismiss();


        }


    }

    public class RegistrarCuponUsuario extends AsyncTask <String, Integer, Boolean> {


        private Context mContext6 = null;

        RegistrarCuponUsuario (Context context6){
            mContext6 = context6;
        }

        @Override
        protected Boolean doInBackground (String... strings) {


            try {

                int numeroalazar = (int) (Math.random() * Items_Cupon.size());
                String ID_Cupon_alazar = String.valueOf(Items_Cupon.get(numeroalazar).getID_Cupon());

                String pattern = "dd/MM/yyyy";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                Fecha_Actual = simpleDateFormat.format(new Date());


                Statement stm4 = ConnectionDB().createStatement();
                stm4.execute("Insert into Cupon_Usuario(ID_Cupon,ID_Usuario_Cliente,Fecha,Estado) " +
                        "values(" + ID_Cupon_alazar+"," + Id_Usuario + ",'" + Fecha_Actual + "','Activo')");




            }catch (Exception e){
                Log.e("Error", e.toString());
            }

            return true;
        }

        @Override
        protected  void onPreExecute(){

            //mDialog.show();

        }

        @Override
        protected  void onPostExecute (Boolean result){

            try {


                Nro_Cupones = 0;
                SaveNroCuponesSharedPreferences(Nro_Cupones);


            }catch (Exception e){
                Log.e("Error",e.toString());
            }

            DecimalFormat df = new DecimalFormat("#.0");

            bundle2.putString("totalresenas", String.valueOf(Total_Resenas));
            bundle2.putString("puntuacion", String.valueOf(df.format(Puntuacion_Total)));
            bundle2.putFloat("mipuntuacion", Ratingbar_Calificacion.getRating());
            bundle2.putString("micomentario", Txtcomentario.getText().toString());
            bundle2.putInt("banderaresena", 1);

            Alert_Dialog.dismiss();

            ListaResenas2 listaResenas2 = new ListaResenas2();
            listaResenas2.setArguments(bundle2);

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.contenedorfragment, listaResenas2);
            transaction.commit();


        }


    }
}