package com.example.bottommenu.HelperClass;

import java.util.ArrayList;
import java.util.List;

public class SpinnerList {

    public SpinnerList(){

    }
    public List<String> spinnerArray (){
        List<String> isi =  new ArrayList<String>();
        isi.add("Provinsi Jawa Timur");
        isi.add("Pacitan");
        isi.add("Ponorogo");
        isi.add("Trenggalek");
        isi.add("Tulungagung");
        isi.add("Blitar");
        isi.add("Kediri");
        isi.add("Malang");
        isi.add("Lumajang");
        isi.add("Jember");
        isi.add("Banyuwangi");
        isi.add("Bondowoso");
        isi.add("Situbondo");
        isi.add("Probolinggo");
        isi.add("Pasuruan");
        isi.add("Sidoarjo");
        isi.add("Mojokerto");
        isi.add("Jombang");
        isi.add("Nganjuk");
        isi.add("Madiun");
        isi.add("Magetan");
        isi.add("Ngawi");
        isi.add("Bojonegoro");
        isi.add("Tuban");
        isi.add("Lamongan");
        isi.add("Gresik");
        isi.add("Bangkalan");
        isi.add("Sampang");
        isi.add("Pamekasan");
        isi.add("Sumenep");
        isi.add("Kota Kediri");
        isi.add("Kota Blitar");
        isi.add("Kota Malang");
        isi.add("Kota Probolinggo");
        isi.add("Kota Pasuruan");
        isi.add("Kota Mojokerto");
        isi.add("Kota Madiun");
        isi.add("Kota Surabaya");
        isi.add("Kota Batu");

        return isi;
    }

    public String getkdKab(String nmKab){
        String kdKab="3500";
        switch (nmKab){
            case "Provinsi Jawa Timur":
                kdKab="3500";
                break;
            case "Pacitan":
                kdKab="3501";
                break;
            case "Ponorogo":
                kdKab="3502";
                break;
            case "Trenggalek":
                kdKab="3503";
                break;
            case "Tulungagung":
                kdKab="3504";
                break;
            case "Blitar":
                kdKab="3505";
                break;
            case "Kediri":
                kdKab="3506";
                break;
            case "Malang":
                kdKab="3507";
                break;
            case "Lumajang":
                kdKab="3508";
                break;
            case "Jember":
                kdKab="3509";
                break;
            case "Banyuwangi":
                kdKab="3510";
                break;
            case "Bondowoso":
                kdKab="3511";
                break;
            case "Situbondo":
                kdKab="3512";
                break;
            case "Probolinggo":
                kdKab="3513";
                break;
            case "Pasuruan":
                kdKab="3514";
                break;
            case "Sidoarjo":
                kdKab="3515";
                break;
            case "Mojokerto":
                kdKab="3516";
                break;
            case "Jombang":
                kdKab="3517";
                break;
            case "Nganjuk":
                kdKab="3518";
                break;
            case "Madiun":
                kdKab="3519";
                break;
            case "Magetan":
                kdKab="3520";
                break;
            case "Ngawi":
                kdKab="3521";
                break;
            case "Bojonegoro":
                kdKab="3522";
                break;
            case "Tuban":
                kdKab="3523";
                break;
             case "Lamongan":
                kdKab="3524";
                break;
             case "Gresik":
                kdKab="3525";
                break;
             case "Bangkalan":
                kdKab="3526";
                break;
             case "Sampang":
                kdKab="3527";
                break;
             case "Pamekasan":
                kdKab="3528";
                break;
             case "Sumenep":
                kdKab="3529";
                break;
            case "Kota Kediri":
                kdKab="3571";
                break;
            case "Kota Blitar":
                kdKab="3572";
                break;
            case "Kota Malang":
                kdKab="3573";
                break;
            case "Kota Probolinggo":
                kdKab="3574";
                break;
            case "Kota Pasuruan":
                kdKab="3575";
                break;
            case "Kota Mojokerto":
                kdKab="3576";
                break;
            case "Kota Madiun":
                kdKab="3577";
                break;
            case "Kota Surabaya":
                kdKab="3578";
                break;
            case "Kota Batu":
                kdKab="3579";
                break;
        }
        return kdKab;
    }

    public String getkdK(String nmKab){
        String bahasa="eng";
        switch (nmKab) {
            case "Indonesia":
                bahasa = "ind";
                break;
            case "Inggris":
                bahasa = "eng";
                break;
        }
        return bahasa;
    }

    public Integer getChildBahasa(String nmKab) {
        int idPosition = 0;
        switch (nmKab) {
            case "Indonesia":
                idPosition = 0;
                break;
            case "Inggris":
                idPosition = 1;
                break;
        }
        return idPosition;
    }

    public Integer getIdPosition(String nmKab){
        int idPosition=0;
        switch (nmKab){
            case "Provinsi Jawa Timur":
                idPosition=0;
                break;
            case "Pacitan":
                idPosition=1;
                break;
            case "Ponorogo":
                idPosition=2;
                break;
            case "Trenggalek":
                idPosition=3;
                break;
            case "Tulungagung":
                idPosition=4;
                break;
            case "Blitar":
                idPosition=5;
                break;
            case "Kediri":
                idPosition=6;
                break;
            case "Malang":
                idPosition=7;
                break;
            case "Lumajang":
                idPosition=8;
                break;
            case "Jember":
                idPosition=9;
                break;
            case "Banyuwangi":
                idPosition=10;
                break;
            case "Bondowoso":
                idPosition=11;
                break;
            case "Situbondo":
                idPosition=12;
                break;
            case "Probolinggo":
                idPosition=13;
                break;
            case "Pasuruan":
                idPosition=14;
                break;
            case "Sidoarjo":
                idPosition=15;
                break;
            case "Mojokerto":
                idPosition=16;
                break;
            case "Jombang":
                idPosition=17;
                break;
            case "Nganjuk":
                idPosition=18;
                break;
            case "Madiun":
                idPosition=19;
                break;
            case "Magetan":
                idPosition=20;
                break;
            case "Ngawi":
                idPosition=21;
                break;
            case "Bojonegoro":
                idPosition=22;
                break;
            case "Tuban":
                idPosition=23;
                break;
            case "Lamongan":
                idPosition=24;
                break;
            case "Gresik":
                idPosition=25;
                break;
            case "Bangkalan":
                idPosition=26;
                break;
            case "Sampang":
                idPosition=27;
                break;
            case "Pamekasan":
                idPosition=28;
                break;
            case "Sumenep":
                idPosition=29;
                break;
            case "Kota Kediri":
                idPosition=30;
                break;
            case "Kota Blitar":
                idPosition=31;
                break;
            case "Kota Malang":
                idPosition=32;
                break;
            case "Kota Probolinggo":
                idPosition=33;
                break;
            case "Kota Pasuruan":
                idPosition=34;
                break;
            case "Kota Mojokerto":
                idPosition=35;
                break;
            case "Kota Madiun":
                idPosition=36;
                break;
            case "Kota Surabaya":
                idPosition=37;
                break;
            case "Kota Batu":
                idPosition=38;
                break;
        }
        return idPosition;
    }
}
