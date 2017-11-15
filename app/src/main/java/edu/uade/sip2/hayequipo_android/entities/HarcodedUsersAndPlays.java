package edu.uade.sip2.hayequipo_android.entities;

import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Usuario on 06/11/2017.
 */

public class HarcodedUsersAndPlays {

    private static ArrayList<Usuario> usuarios = new ArrayList<>();;
    private static ArrayList<Partido> partidos = new ArrayList<>();;
    private static Map<Integer,Integer> partidos_usuarios = new HashMap<>();


    public HarcodedUsersAndPlays(){
        usuarios = new ArrayList<>();
        partidos = new ArrayList<>();


    }

    public static void agregarUsuariosHarcodeados(){
        Usuario usr = new Usuario(1,"pepe");
        Usuario usr2 = new Usuario(2,"pocho");
        Usuario usr3 = new Usuario(3,"tito");
        usuarios.add(usr);
        usuarios.add(usr2);
        usuarios.add(usr3);
    }


    public static int buscarUsuario(String nombre){
        for(Usuario u : usuarios){
            if(u.getNombre().equals(nombre)){
                return u.getId();
            }
        }

        return -1;
    }



    public static boolean agregarUsuario(int idPartido,int idUsuario){


        Log.e("lo que llego","partido id:"+idPartido+" "+"idUsr: "+idUsuario);
        Log.e("tamaño partidos","size:"+partidos_usuarios.size());

       if(idUsuario!=-1) {

           // si encuentro que ya esta en el partido no lo agrego
           for (Map.Entry<Integer, Integer> entry : partidos_usuarios.entrySet()) {
               Log.e("lo que llego","partido id:"+idPartido+" "+"idUsr: "+idUsuario);
               Log.e("lo que tira el mapa","partido id:"+entry.getKey()+" "+"idUsr: "+entry.getValue());
               if (entry.getKey().equals(idPartido)) {
                   if (entry.getValue()!= null && entry.getValue().equals(idUsuario)) {
                       return false;
                   }
               }

           }
           partidos_usuarios.put(idPartido, idUsuario);
           sumarParticipante(idPartido);
           return true;
       }else{
           Log.e("error","usuario no valido");
           return false;
       }
    }


    private static void sumarParticipante(int idPartido){

        Partido partido = null;
        for(Partido p: partidos){
           if(p.getId()==idPartido){
               partido = p;
           }
        }
        if(partido!=null){
            partido.setParticipantes(partido.getParticipantes()+1);
        }
    }

    public static void agregarPartido(Partido p){
        partidos.add(p);
        partidos_usuarios.put(p.getId(),null);
        Log.e("partido agregado","cant: "+partidos_usuarios.size());
    }

    public static ArrayList<Partido> obtenerPartidos(){
        return partidos;
    }

    public static Partido getPartido(int id){
        for(Partido p : partidos){
            if(p.getId()==id){
                return p;
            }
        }
        return null;
    }

    public int existeUsuario(int idPartido) {

         return 1;

    }

    public static int getIdPartido(int i) {

        if (partidos == null){
            return i;
    }else {


            return partidos.size()+1;
        }

    }


    public static boolean cambiarPartido(int mi_partido_actual, String desc, String lugar, String fecha, String hora) {


        Partido partido = null;
        for(Partido p : partidos){
            if(p.getId()==mi_partido_actual){
                partido = p;
            }
        }

        if(partido==null) return false;

        partido.setDescripcion(desc);
        partido.setLugar(lugar);
        partido.setFecha(fecha);
        partido.setHora(hora);

        return true;
    }
}
