package com.project.rafael.kizunaapp.database;

import com.project.rafael.kizunaapp.objects.Contacts;
import com.project.rafael.kizunaapp.objects.RegDia;
import com.project.rafael.kizunaapp.objects.RegHora;
import com.project.rafael.kizunaapp.objects.Users;

import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by RAFAEL on 21/03/2017.
 */

public class SentenciasSQL {
    public static int NextUserID(){
        Realm realm= Realm.getDefaultInstance();
        long cantidad= realm.where(Users.class).count();
        return (int)cantidad+1;
    }

    public static void GuardarEstadoUser(final String user,final Integer sign,final  double temp,final  int ppm,final  int o2,final boolean cond){
        Realm realm=Realm.getDefaultInstance();
        //final Users users=realm.where(Users.class).equalTo("userName",user).findFirst();

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgrealm) {
                Users users=bgrealm.where(Users.class).equalTo("userName",user).findFirst();
                users.setSignal(sign);
                if (cond) {
                    users.setTemp(temp);
                    users.setPpm(ppm);
                    users.setO2(o2);
                    //si no hay desconexion
                    if(o2>20) {
                        Calendar calendar = Calendar.getInstance();
                        int año = calendar.get(Calendar.YEAR);
                        int mes=calendar.get(Calendar.MONTH);
                        int dia = calendar.get(Calendar.DAY_OF_MONTH);
                        int hora = calendar.get(Calendar.HOUR_OF_DAY);

                        boolean cond2 = true;
                        for (RegDia regDia : users.getRegDias()) {
                            if (regDia.getDia() == dia && regDia.getMes()==mes && regDia.getAño() == año) {
                                boolean cond1 = true;
                                for (RegHora regHora : regDia.getRegHoras()) {
                                    if (regHora.getHora() == hora) {
                                        regHora.setSumO2(regHora.getSumO2() + o2);
                                        regHora.setSumPPM(regHora.getSumPPM() + ppm);
                                        regHora.setSumTemp(regHora.getSumTemp() + temp);
                                        regHora.setContO2(regHora.getContO2() + 1);
                                        regHora.setContPPM(regHora.getContPPM() + 1);
                                        regHora.setContTemp(regHora.getContTemp() + 1);
                                        cond1 = false;
                                        break;
                                    }
                                }
                                if (cond1) {
                                    RegHora regHora1 = new RegHora();
                                    regHora1.setHora(hora);
                                    regHora1.setSumO2(o2);
                                    regHora1.setSumPPM(ppm);
                                    regHora1.setSumTemp(temp);
                                    regHora1.setContO2(1);
                                    regHora1.setContPPM(1);
                                    regHora1.setContTemp(1);
                                    bgrealm.copyToRealm(regHora1);
                                    regDia.getRegHoras().add(regHora1);
                                }
                                cond2 = false;
                                break;
                            }
                        }
                        if (cond2) {
                            RegHora regHora2 = new RegHora();
                            regHora2.setHora(hora);
                            regHora2.setSumO2(o2);
                            regHora2.setSumPPM(ppm);
                            regHora2.setSumTemp(temp);
                            regHora2.setContO2(1);
                            regHora2.setContPPM(1);
                            regHora2.setContTemp(1);
                            bgrealm.copyToRealm(regHora2);

                            RegDia regDia1 = new RegDia();
                            regDia1.setDia(dia);
                            regDia1.setMes(mes);
                            regDia1.setAño(año);
                            bgrealm.copyToRealm(regDia1);

                            regDia1.getRegHoras().add(regHora2);
                            users.getRegDias().add(regDia1);
                        }
                    }
                }
            }
        },null,null);
        /*try {
            realm.beginTransaction();
            users.setSignal(sign);
            if (cond) {
                users.setTemp(temp);
                users.setPpm(ppm);
                users.setO2(o2);
            }
            realm.commitTransaction();
        }catch (Exception e){
            realm.cancelTransaction();
        }*/
    }

    /*
    public static int NextContactID(){
        Realm realm= Realm.getDefaultInstance();
        long cantidad= realm.where(Contacts.class).count();
        return (int)cantidad+1;
    }*/

    /*
    public static int ObtenerContactID(String nombre, String telf){
        Realm realm= Realm.getDefaultInstance();
        return realm.where(Contacts.class).equalTo("nombreContc",nombre).equalTo("numTel",telf).findFirst().getIdContact();
    }*/

    public static void RegistrarUser(Users users){
        Realm realm= Realm.getDefaultInstance();
        try {
            realm.beginTransaction();
            realm.copyToRealm(users);
            realm.commitTransaction();
        }catch (Exception e){
            realm.cancelTransaction();
        }
    }

    public static RealmList<RegHora> ObtenerRegistrodelDia(String user,int dia,int mes, int año){
        Realm realm=Realm.getDefaultInstance();
        for(RegDia regDia:realm.where(Users.class).equalTo("userName",user).findFirst().getRegDias()){
            if (regDia.getDia()==dia && regDia.getMes()==mes && regDia.getAño()==año){
                return regDia.getRegHoras();
            }
        }
        return null;
    }
    /*
    public static void AgregarContactRealm(Contacts contacts){
        Realm realm= Realm.getDefaultInstance();
        try {
            realm.beginTransaction();
            realm.copyToRealm(contacts);
            realm.commitTransaction();
        }catch (Exception e){
            realm.cancelTransaction();
        }
    }
    */

    public static boolean ValidarUserNameDisp(String userName){
        Realm realm= Realm.getDefaultInstance();
        if(realm.where(Users.class).equalTo("userName",userName).findFirst()==null)
            return true;
        else
            return false;
    }

    public static boolean ValidarUser(String userName,String Pass){
        Realm realm= Realm.getDefaultInstance();
        Users users= realm.where(Users.class).equalTo("userName",userName).findFirst();
        if (users!=null && Pass.equals(users.getPass())){
            return true;
        }else
            return false;
    }

    public static int ValidaryAgregarContactenRealm(Contacts contact){
        Realm realm= Realm.getDefaultInstance();
        RealmResults<Contacts> contactslist= realm.where(Contacts.class).findAll();
        for(Contacts contacts: contactslist){
            if (contacts.getNombreContc().equals(contact.getNombreContc())&&contacts.getNumTel().equals(contact.getNumTel()))
                return contacts.getIdContact();
        }
        try {
            realm.beginTransaction();
            long cantidad= realm.where(Contacts.class).count();
            contact.setIdContact((int)cantidad+1);
            realm.copyToRealm(contact);
            realm.commitTransaction();
        }catch (Exception e){
            realm.cancelTransaction();
        }
        return contact.getIdContact();
        /*
        Realm realm= Realm.getDefaultInstance();
        Contacts contacts= realm.where(Contacts.class).equalTo("nombreContc",nombre).equalTo("numTel",telf).findFirst();
        if (contacts==null ){
            return true;
        }else
            return false;
            */
    }

    public static boolean ValidaryAgregarContactenUser(String user, Contacts contacts){
        Realm realm= Realm.getDefaultInstance();
        Users users= realm.where(Users.class).equalTo("userName",user).findFirst();
        for(Contacts contacts1: users.getListContacts()){
            if (contacts1.getNombreContc().equals(contacts.getNombreContc())&&contacts1.getNumTel().equals(contacts.getNumTel())&&contacts1.getIdContact()==(contacts.getIdContact()))
                return false;
        }

        try {
            realm.beginTransaction();
            users.getListContacts().add(contacts);
            realm.commitTransaction();
        }catch (Exception e){
            realm.cancelTransaction();
        }
        return true;
    }


    public static Users ObtenerUsuario(String user){
        Realm realm=Realm.getDefaultInstance();
        return realm.where(Users.class).equalTo("userName",user).findFirst();
    }

    public static void ActualizarUsuario(String user, int edad, String dir, String afec, String ocup, int cod,String ruta,String nombre,String apellido,String correo){
        Realm realm=Realm.getDefaultInstance();
        Users users= realm.where(Users.class).equalTo("userName",user).findFirst();
        try {
            realm.beginTransaction();
            users.setEdad(edad);
            users.setDireccion(dir);
            users.setAfecciones(afec);
            users.setOcupacion(ocup);
            users.setCodDisp(cod);
            users.setRutaImag(ruta);
            users.setNombre(nombre);
            users.setApellido(apellido);
            users.setCorreo(correo);
            realm.commitTransaction();
        }catch (Exception e){
            realm.cancelTransaction();
        }
    }

    public static void DefinirMAC(String user,String MAC){
        Realm realm=Realm.getDefaultInstance();
        Users users=realm.where(Users.class).equalTo("userName",user).findFirst();
        try {
            realm.beginTransaction();
            users.setMACdisp(MAC);
            realm.commitTransaction();
        }catch (Exception e){
            realm.cancelTransaction();
        }
    }

    public static RealmList<Contacts> ObtenerListaContacts(String user){
        Realm realm= Realm.getDefaultInstance();
        return realm.where(Users.class).equalTo("userName",user).findFirst().getListContacts();
    }

    public  static void EliminarContactenUser(String user,int pos){
        Realm realm = Realm.getDefaultInstance();
        Users users= realm.where(Users.class).equalTo("userName",user).findFirst();
        try {
            realm.beginTransaction();
            users.getListContacts().remove(pos);
            realm.commitTransaction();
        }catch (Exception e){
            realm.cancelTransaction();
        }
    }
}
