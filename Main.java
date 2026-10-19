package ProjekKripto;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        System.out.println("======================================");
        System.out.println("       PROGRAM KRIPTOGRAFI OOP");
        System.out.println("======================================");

        System.out.print("Masukkan plaintext/ciphertext (spasi akan dihapus): ");
        String teks = input.nextLine().replaceAll("\\s+", "");

        int jumlahProses = 0;

        while (jumlahProses < 3) {

            System.out.println("\n======================================");
            System.out.println("Teks saat ini : " + teks);
            System.out.println(
                    "Proses ke-" + (jumlahProses + 1) + " dari 3"
            );
            System.out.println("======================================");

            System.out.println("1. Enkripsi Vigenere");
            System.out.println("2. Dekripsi Vigenere");
            System.out.println("3. Enkripsi Transposisi Kolom");
            System.out.println("4. Dekripsi Transposisi Kolom");
            System.out.println("5. Enkripsi ROT13");
            System.out.println("6. Dekripsi ROT13");
            System.out.println("7. Selesai");

            System.out.print("Pilih menu (1-7): ");

            int pilihan;

            try {
                pilihan = Integer.parseInt(input.nextLine());

            } catch (NumberFormatException e) {
                System.out.println("Input harus berupa angka!");
                continue;
            }

            if (pilihan == 7) {
                break;
            }

            if (pilihan < 1 || pilihan > 7) {
                System.out.println("Pilihan tidak tersedia!");
                continue;
            }

            String kunci;

            // ======================================
            // VIGENERE
            // ======================================
            if (pilihan == 1 || pilihan == 2) {

                System.out.print("Masukkan kunci Vigenere: ");
                kunci = input.nextLine();

                if (kunci.isEmpty()) {
                    System.out.println(
                            "Kunci tidak boleh kosong!"
                    );
                    continue;
                }

                // Kunci boleh berisi huruf dan angka
                if (!kunci.matches("[a-zA-Z0-9]+")) {
                    System.out.println(
                            "Kunci Vigenere hanya boleh berisi huruf dan angka!"
                    );
                    continue;
                }

                if (pilihan == 1) {
                    teks = Vigenere.enkripsi(teks, kunci);
                } else {
                    teks = Vigenere.dekripsi(teks, kunci);
                }
            }

            // ======================================
            // TRANSPOSISI KOLOM
            // ======================================
            else if (pilihan == 3 || pilihan == 4) {

                System.out.print(
                        "Masukkan jumlah kolom sebagai kunci: "
                );

                kunci = input.nextLine();

                try {

                    int nilaiKunci = Integer.parseInt(kunci);

                    if (nilaiKunci <= 0) {
                        System.out.println(
                                "Kunci harus lebih dari 0!"
                        );
                        continue;
                    }

                    if (pilihan == 3) {

                        teks = TransposisiKolom.enkripsi(
                                teks,
                                kunci
                        );

                    } else {

                        teks = TransposisiKolom.dekripsi(
                                teks,
                                kunci
                        );
                    }

                } catch (NumberFormatException e) {

                    System.out.println(
                            "Kunci harus berupa angka!"
                    );

                    continue;
                }
            }

            // ======================================
            // ROT13
            // ======================================
            else if (pilihan == 5) {

                System.out.println(
                        "ROT13 tidak membutuhkan kunci."
                );

                teks = ROT13.enkripsi(teks);
            }

            else if (pilihan == 6) {

                System.out.println(
                        "ROT13 tidak membutuhkan kunci."
                );

                teks = ROT13.dekripsi(teks);
            }

            jumlahProses++;

            System.out.println("\n--------------------------------------");
            System.out.println(
                    "Hasil proses ke-" + jumlahProses + ":"
            );
            System.out.println(teks);
            System.out.println("--------------------------------------");

            if (jumlahProses == 3) {

                System.out.println(
                        "\nBatas maksimal 3 proses telah tercapai."
                );

                break;
            }

            System.out.println(
                    "Anda masih dapat melakukan "
                            + (3 - jumlahProses)
                            + " proses lagi."
            );
        }

        System.out.println("\n======================================");
        System.out.println("Teks akhir : " + teks);
        System.out.println("Program selesai.");
        System.out.println("======================================");

        input.close();
    }
}