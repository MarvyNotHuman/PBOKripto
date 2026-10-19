package ProjekKripto;

public class ROT13 {

    // ======================================
    // PROSES ROT13
    // ======================================
    public static String proses(String teks) {
        if (teks == null) {
            throw new IllegalArgumentException("Teks tidak boleh null.");
        }

        StringBuilder hasil = new StringBuilder();

        for (char c : teks.toCharArray()) {

            // Huruf kapital A-Z
            if (c >= 'A' && c <= 'Z') {

                c = (char) (
                        (c - 'A' + 13) % 26
                                + 'A'
                );
            }

            // Huruf kecil a-z
            else if (c >= 'a' && c <= 'z') {

                c = (char) (
                        (c - 'a' + 13) % 26
                                + 'a'
                );
            }

            hasil.append(c);
        }

        return hasil.toString();
    }


    // ======================================
    // ENKRIPSI
    // ======================================
    public static String enkripsi(String teks) {

        return proses(teks);
    }


    // ======================================
    // DEKRIPSI
    // ======================================
    public static String dekripsi(String teks) {

        return proses(teks);
    }
}
