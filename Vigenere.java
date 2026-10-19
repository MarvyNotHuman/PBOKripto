package ProjekKripto;

public class Vigenere {

    // ======================================
    // Mengubah karakter kunci menjadi angka
    // ======================================
    public static int nilaiKunci(char c) {

        // Huruf A-Z / a-z = 0-25
        if (c >= 'A' && c <= 'Z') {

            return c - 'A';
        }

        if (c >= 'a' && c <= 'z') {

            return c - 'a';
        }

        // Angka 0-9 = 0-9
        if (c >= '0' && c <= '9') {

            return c - '0';
        }

        throw new IllegalArgumentException(
                "Kunci hanya boleh berisi huruf dan angka."
        );
    }


    // ======================================
    // ENKRIPSI VIGENERE
    // ======================================
    private static void validasiInput(String teks, String kunci) {
        if (teks == null) {
            throw new IllegalArgumentException("Teks tidak boleh null.");
        }
        if (kunci == null || kunci.isEmpty()) {
            throw new IllegalArgumentException(
                    "Kunci tidak boleh kosong."
            );
        }
        if (!kunci.matches("[a-zA-Z0-9]+")) {
            throw new IllegalArgumentException(
                    "Kunci hanya boleh berisi huruf dan angka."
            );
        }
    }

    public static String enkripsi(String teks, String kunci) {
        validasiInput(teks, kunci);

        StringBuilder hasil = new StringBuilder();

        int indexKunci = 0;

        for (char c : teks.toCharArray()) {

            if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {

                char dasar;

                if (Character.isUpperCase(c)) {
                    dasar = 'A';
                } else {
                    dasar = 'a';
                }

                int nilaiTeks = c - dasar;

                char karakterKunci =
                        kunci.charAt(
                                indexKunci % kunci.length()
                        );

                int nilaiKunci = nilaiKunci(karakterKunci);

                char karakter = (char) (
                        (nilaiTeks + nilaiKunci) % 26
                                + dasar
                );

                hasil.append(karakter);

                indexKunci++;

            } else {

                // Spasi, angka plaintext, tanda baca,
                // dan karakter lainnya tidak diubah
                hasil.append(c);
            }
        }

        return hasil.toString();
    }


    // ======================================
    // DEKRIPSI VIGENERE
    // ======================================
    public static String dekripsi(String teks, String kunci) {
        validasiInput(teks, kunci);

        StringBuilder hasil = new StringBuilder();

        int indexKunci = 0;

        for (char c : teks.toCharArray()) {

            if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {

                char dasar;

                if (Character.isUpperCase(c)) {
                    dasar = 'A';
                } else {
                    dasar = 'a';
                }

                int nilaiTeks = c - dasar;

                char karakterKunci =
                        kunci.charAt(
                                indexKunci % kunci.length()
                        );

                int nilaiKunci = nilaiKunci(karakterKunci);

                char karakter = (char) (
                        (nilaiTeks - nilaiKunci + 26) % 26
                                + dasar
                );

                hasil.append(karakter);

                indexKunci++;

            } else {

                hasil.append(c);
            }
        }

        return hasil.toString();
    }
}