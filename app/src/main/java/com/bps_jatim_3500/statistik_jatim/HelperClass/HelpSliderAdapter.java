package com.bps_jatim_3500.statistik_jatim.HelperClass;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.bps_jatim_3500.statistik_jatim.R;



public class HelpSliderAdapter extends PagerAdapter {
    Context context;

    LayoutInflater layoutInflater;
    public HelpSliderAdapter(Context contect) {
        this.context = contect;
    }

    int images[]={
            R.drawable.ic_help_1,
            R.drawable.ic_help_2,
            R.drawable.ic_help_3,
            R.drawable.ic_help_4
    };
    String headings[]={
            "Tentang Aplikasi",
            "Tentang BPS",
            "Hubungi Kami",
            "Pelayanan Data"
    };
    String desc[]={
            "<p> <b>&nbsp&nbsp&nbsp&nbsp&nbsp Statistik Jatim</b> adalah salah satu sarana layanan data di BPS Provinsi Jawa Timur berbasis android. Aplikasi ini merupakan integrasi dari beberapa layanan data yang ada disediakan oleh BPS Provinsi Jawa Timur yaitu <b>DAVITA</b> <i>(Data Virtual Assistant-Chatbot berbasis Whatsapp)</i>, <b>ChatUs</b><i> (Layanan Konsultasi Data dengan Petugas Pelayanan Statistik Terpadu)</i>, <b>Data Corner</b><i> (Layanan Pencarian Data dalam Konten Publikasi dengan teknologi Elastic Search)</i>. Konten utama pada aplikasi ini berasal dari website BPS Jawa Timur dan BPS Kabupaten/Kota di Wilayah Provinsi Jawa Timur.\n" +
                    "\n" +
                    "<br>&nbsp&nbsp&nbsp&nbsp&nbsp Selain integrasi dari layanan di BPS Provinsi Jawa Timur fitur utama aplikasi ini yaitu Menu <b>Pubikasi</b>, <b>Berita</b>, <b>Berita Resmi Statistik</b>, <b>Tabel-Tabel Statistik</b>. Menu Publikasi berisi publikasi-publikasi statistik yang telah di terbitkan oleh BPS. " +
                    "Menu Berita berisi kegiatan-kegiatan yang dilaksanakan oleh BPS baik kegiatan statistik ataupun kegaitan-kegiatan pendukung lainnya yang berhubungan dengan aktivitas BPS dalam menyediakan Statistik. " +
                    "Menu Berita Resmi Statistik (BRS) berisi rilis resmi indikator-indikator statistik yang telah dirilis oleh BPS dan diurutkan dari BRS yag terbaru. " +
                    "Menu Data berisi daftar data dalam bentuk tabel yang dikelompokkan dalam berberapa subjek.  Pada setiap menu tersebut, pengguna dapat melakukan pencarian dengan memasukkan keyword dan memilih Kabupaten/Kota yang ingin ditampilkan.\n" +
                    "\n" +
                    "<br>&nbsp&nbsp&nbsp&nbsp&nbsp Pengembangan aplikasi ini terus dilakukan. Kami terbuka jika  ada kritik, saran, kolaborasi ataupun permintaan untuk replikasi aplikasi ini dapat disampaikan melalui email : bps3500@bps.go.id.\n" +
                    "\n" +
                    "\n" +
                    "<i><br><br></br><b>Tim Pengembang Statistik Jatim\n" +
                    "<br>BPS Provinsi Jawa Timur</b></i></p> ",
            "<p><b>&nbsp&nbsp&nbsp&nbsp&nbsp Badan Pusat Statistik</b> adalah Lembaga Pemerintah Non-Kementrian yang bertanggung jawab langsung kepada Presiden. " +
                    "Sebelumnya, BPS merupakan Biro Pusat Statistik, yang dibentuk berdasarkan UU Nomor 6 Tahun 1960 " +
                    "tentang Sensus dan UU Nomer 7 Tahun 1960 tentang Statistik. Sebagai pengganti kedua UU tersebut " +
                    "ditetapkan UU Nomor 16 Tahun 1997 tentang Statistik. Berdasarkan UU ini yang ditindaklanjuti dengan " +
                    "peraturan perundangan dibawahnya, secara formal nama Biro Pusat Statistik diganti menjadi Badan Pusat Statistik.\n" +
                    "<br></br> &nbsp&nbsp&nbsp&nbsp&nbsp Dengan mempertimbangkan capaian kinerja, memperhatikan aspirasi masyarakat, potensi dan permasalahan, serta mewujudkan Visi Presiden dan Wakil Presiden maka visi Badan Pusat Statistik untuk tahun 2020-2024 adalah:\n" +
                    "<br><b>&nbsp&nbsp&nbsp&nbsp “Penyedia Data Statistik Berkualitas untuk Indonesia Maju”</br>\n" +
                    "<br>&nbsp&nbsp&nbsp&nbsp (“Provider of Qualified Statistical Data for Advanced Indonesia”)</b>\n" +
                    "\n" +
                    "<br>&nbsp&nbsp&nbsp&nbsp&nbsp Dalam visi yang baru tersebut berarti bahwa BPS berperan dalam penyediaan data statistik nasional maupun internasional, untuk menghasilkan statistik yang mempunyai kebenaran akurat dan menggambarkan keadaan yang sebenarnya, dalam rangka mendukung Indonesia Maju.\n" +
                    "<br>&nbsp&nbsp&nbsp&nbsp&nbsp Dengan visi baru ini, eksistensi BPS sebagai penyedia data dan informasi statistik menjadi semakin penting, karena memegang peran dan pengaruh sentral dalam penyediaan statistik berkualitas tidak hanya di Indonesia, melainkan juga di tingkat dunia. Dengan visi tersebut juga, semakin menguatkan peran BPS sebagai pembina data statistik.</p>",
            "<p><b>Alamat Kantor : </b><br>" +
                    "&nbsp&nbsp Jalan Raya Kendangsari Industri No. 43 - 44 Surabaya 60292<p>" +
            "<p><b>Telepon & Fax : </b><br>\n" +
                    "&nbsp&nbsp Telp. (031) 8439343, Fax (031) 8494007, 8471143<p>" +
            "<p><b>Email : </b><br>\n" +
                    "&nbsp&nbsp bps3500@bps.go.id"+
            "<p><b>Website : </b><br>\n" +
                    "&nbsp&nbsp http://jatim.bps.go.id"+
            "<p><b>Media Sosial : </b><br>\n" +
                    "&nbsp&nbsp Instagram --- @bpsjatim<br>"+
                    "&nbsp&nbsp Facebook----- @bpsjatim<br>"+
                    "&nbsp&nbsp Twitter-------- @bpsjatim<br>"+
                    "&nbsp&nbsp Youtube----- BPS Provinsi Jawa timur",
            "<p>&nbsp&nbsp&nbsp&nbsp BPS Provinsi Jawa Timur menyediakan beberapa akses layanan data kepada pengguna data, diantaanya adalah : </p>"+
            "<p><b>Pelayanan Statistik Terpadu (PST) </b> yaitu pusat pelayanan data yang memberikan pelayanan " +
                    "terpadu satu pintu di BPS Provinsi Jawa Timur. Pengguna data dapat mengunjungi PST " +
                    "di Jam kerja di Kantor BPS Provinsi Jawa Timur dengan alamat Jalan Raya Kendangsari Industri No. 43 - 44 Surabaya" +
                    "</p>"+
                    "<p><b>Layanan <i>online</i></b> melalui beberapa aplikasi yaitu <b>website http://jatim.bps.go.id</b> " +
                    ", <b> Aplikasi Statistik Jatim</b>, <b> Davita</b> , <b> Chat Us</b>, dan <b> Data Corner </b> </p>"+
                    "<p><b>Layanan data melalui e-mail</b> yang ditujukan kepada email pelayanan kami yaitu di perpustakaan3500@bps.go.id. </p>",
    };


    @Override
    public int getCount() {
        return headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout)object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater=(LayoutInflater)context.getSystemService((context.LAYOUT_INFLATER_SERVICE));
        View view =layoutInflater.inflate(R.layout.activity_help_onboarding_item, container, false);

        //Hooks From Design
        ImageView imageView=view.findViewById(R.id.heading_image);
        TextView headingView=view.findViewById(R.id.heading_text);
        TextView descView=view.findViewById(R.id.desc_text);

        imageView.setImageResource(images[position]);
        headingView.setText(headings[position]);
        descView.setText(Html.fromHtml(desc[position]));

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout)object);
    }
}
