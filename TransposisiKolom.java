package ProjekKripto;

public class TransposisiKolom {

    // ======================================
    // Mengubah kunci (String) menjadi jumlah kolom
    // ======================================
    private static int ambilKolom(String kunci, int panjang) {
        if (kunci == null || !kunci.matches("[0-9]+")) {
            throw new IllegalArgumentException(
                    "Kunci harus berupa angka lebih dari 0."
            );
        }

        int kolom;
        try {
            kolom = Integer.parseInt(kunci);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Nilai kunci terlalu besar.", e
            );
        }

        if (kolom <= 0) {

            throw new IllegalArgumentException(
                    "Kunci harus berupa angka lebih dari 0."
            );
        }

        // Kolom lebih banyak dari panjang teks tidak mengubah hasil,
        // jadi dibatasi agar tidak boros memori / waktu.
        return Math.min(kolom, Math.max(1, panjang));
    }


    // ======================================
    // ENKRIPSI TRANSPOSISI KOLOM
    // ======================================
    public static String enkripsi(
            String teks,
            String kunci
    ) {
        if (teks == null) {
            throw new IllegalArgumentException("Teks tidak boleh null.");
        }

        int panjang = teks.length();

        int kolom = ambilKolom(kunci, panjang);

        StringBuilder hasil = new StringBuilder();

        // Teks ditulis per baris (kiri ke kanan, atas ke bawah),
        // lalu dibaca per kolom (atas ke bawah, kiri ke kanan).
        // Karakter ke-i berada di kolom (i % kolom).
        for (int j = 0; j < kolom; j++) {

            for (int i = j; i < panjang; i += kolom) {

                hasil.append(teks.charAt(i));
            }
        }

        return hasil.toString();
    }


    // ======================================
    // DEKRIPSI TRANSPOSISI KOLOM
    // ======================================
    public static String dekripsi(
            String teks,
            String kunci
    ) {
        if (teks == null) {
            throw new IllegalArgumentException("Teks tidak boleh null.");
        }

        int panjang = teks.length();

        int kolom = ambilKolom(kunci, panjang);

        /*
         * Menentukan jumlah karakter pada setiap kolom.
         *
         * Contoh:
         *
         * helloworld, kolom = 4
         *
         * h e l l
         * o w o r
         * l d
         *
         * Jumlah karakter:
         *
         * Kolom 1 = 3
         * Kolom 2 = 3
         * Kolom 3 = 2
         * Kolom 4 = 2
         *
         * Kolom ke-j (mulai dari 0) berisi:
         * panjang / kolom karakter, ditambah 1
         * jika j < panjang % kolom.
         */

        int dasar = panjang / kolom;

        int sisa = panjang % kolom;

        char[] hasil = new char[panjang];

        int index = 0;

        // Teks sandi dibaca per kolom, lalu dikembalikan
        // ke posisi aslinya: baris i, kolom j -> i * kolom + j
        for (int j = 0; j < kolom; j++) {

            int jumlahBarisKolom = dasar;

            if (j < sisa) {

                jumlahBarisKolom++;
            }

            for (int i = 0; i < jumlahBarisKolom; i++) {

                hasil[i * kolom + j] = teks.charAt(index);

                index++;
            }
        }

        return new String(hasil);
    }
}
