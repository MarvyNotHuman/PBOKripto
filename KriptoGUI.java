package ProjekKripto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.List;

/**
 * GUI Swing untuk Program Kriptografi OOP.
 * Memakai kelas Vigenere, TransposisiKolom, dan ROT13 tanpa perubahan.
 * Aturan sama dengan Main: spasi dihapus, maksimal 3 proses berantai.
 */
public class KriptoGUI extends JFrame {

    private static final int MAKS_PROSES = 3;

    private static final String[] MENU = {
            "Enkripsi Vigenere",
            "Dekripsi Vigenere",
            "Enkripsi Transposisi Kolom",
            "Dekripsi Transposisi Kolom",
            "Enkripsi ROT13",
            "Dekripsi ROT13"
    };

    // ---- komponen ----
    private final JTextArea areaInput = new JTextArea(3, 40);
    private final JButton btnMulai = new JButton("Mulai");

    private final JLabel lblStatus = new JLabel("Masukkan teks lalu klik Mulai.");
    private final JTextArea areaTeksSaatIni = new JTextArea(3, 40);

    private final JComboBox<String> cmbMenu = new JComboBox<>(MENU);
    private final JLabel lblKunci = new JLabel("Kunci:");
    private final JTextField txtKunci = new JTextField(20);
    private final JButton btnProses = new JButton("Proses");
    private final JButton btnSelesai = new JButton("Selesai");

    private final JButton btnSalin = new JButton("Salin Hasil");
    private final JButton btnSalinKunci = new JButton("Salin Kunci");
    private final JButton btnReset = new JButton("Reset");

    private final DefaultTableModel modelRiwayat = new DefaultTableModel(
            new String[]{"Proses", "Metode", "Kunci", "Hasil"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    // ---- state ----
    private String teks = "";
    private int jumlahProses = 0;
    private boolean sudahMulai = false;
    private final List<Integer> riwayatMenu = new ArrayList<>();
    private final List<String> riwayatKunci = new ArrayList<>();

    public KriptoGUI() {
        super("Program Kriptografi OOP");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        root.add(buatPanelAtas(), BorderLayout.NORTH);
        root.add(buatPanelTengah(), BorderLayout.CENTER);
        root.add(buatPanelBawah(), BorderLayout.SOUTH);
        setContentPane(root);

        cmbMenu.addActionListener(e -> perbaruiKunci());
        btnMulai.addActionListener(e -> mulai());
        btnProses.addActionListener(e -> proses());
        btnSelesai.addActionListener(e -> selesai());
        btnSalin.addActionListener(e -> salin());
        btnSalinKunci.addActionListener(e -> salinKunci());
        btnReset.addActionListener(e -> reset());
        getRootPane().setDefaultButton(btnMulai);

        keadaAwal();
        pack();
        setMinimumSize(new Dimension(680, 560));
        setLocationRelativeTo(null);
    }

    // ======================================
    // Penyusunan tampilan
    // ======================================
    private JPanel buatPanelAtas() {
        JPanel p = new JPanel(new BorderLayout(6, 6));
        p.setBorder(BorderFactory.createTitledBorder(
                "1. Plaintext / Ciphertext (spasi akan dihapus)"));

        areaInput.setLineWrap(true);
        areaInput.setWrapStyleWord(true);
        p.add(new JScrollPane(areaInput), BorderLayout.CENTER);

        JPanel tombol = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        tombol.add(btnMulai);
        p.add(tombol, BorderLayout.SOUTH);
        return p;
    }

    private JPanel buatPanelTengah() {
        JPanel p = new JPanel(new BorderLayout(8, 8));

        // --- teks saat ini + pemilihan proses ---
        JPanel atas = new JPanel(new BorderLayout(6, 6));
        atas.setBorder(BorderFactory.createTitledBorder("2. Proses"));

        lblStatus.setFont(lblStatus.getFont().deriveFont(Font.BOLD));
        atas.add(lblStatus, BorderLayout.NORTH);

        areaTeksSaatIni.setEditable(false);
        areaTeksSaatIni.setLineWrap(true);
        areaTeksSaatIni.setWrapStyleWord(false);
        areaTeksSaatIni.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        JScrollPane sp = new JScrollPane(areaTeksSaatIni);
        sp.setBorder(BorderFactory.createTitledBorder("Teks saat ini"));
        atas.add(sp, BorderLayout.CENTER);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(3, 4, 3, 4);
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0; c.gridy = 0;
        form.add(new JLabel("Metode:"), c);
        c.gridx = 1; c.fill = GridBagConstraints.HORIZONTAL; c.weightx = 1;
        form.add(cmbMenu, c);

        c.gridx = 0; c.gridy = 1; c.fill = GridBagConstraints.NONE; c.weightx = 0;
        form.add(lblKunci, c);
        c.gridx = 1; c.fill = GridBagConstraints.HORIZONTAL; c.weightx = 1;
        form.add(txtKunci, c);

        JPanel aksi = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        aksi.add(btnProses);
        aksi.add(btnSelesai);
        c.gridx = 1; c.gridy = 2; c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.EAST;
        form.add(aksi, c);

        atas.add(form, BorderLayout.SOUTH);
        p.add(atas, BorderLayout.NORTH);

        // --- riwayat ---
        JTable tabel = new JTable(modelRiwayat);
        tabel.setFillsViewportHeight(true);
        tabel.getColumnModel().getColumn(0).setMaxWidth(60);
        tabel.getColumnModel().getColumn(1).setPreferredWidth(170);
        tabel.getColumnModel().getColumn(2).setPreferredWidth(90);
        tabel.getColumnModel().getColumn(3).setPreferredWidth(320);
        JScrollPane spTabel = new JScrollPane(tabel);
        spTabel.setBorder(BorderFactory.createTitledBorder("Riwayat proses"));
        spTabel.setPreferredSize(new Dimension(600, 130));
        p.add(spTabel, BorderLayout.CENTER);

        return p;
    }

    private JPanel buatPanelBawah() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        p.add(btnSalin);
        p.add(btnSalinKunci);
        p.add(btnReset);
        return p;
    }

    // ======================================
    // Logika
    // ======================================
    private void keadaAwal() {
        teks = "";
        jumlahProses = 0;
        sudahMulai = false;
        modelRiwayat.setRowCount(0);
        riwayatMenu.clear();
        riwayatKunci.clear();
        btnSalinKunci.setEnabled(false);
        areaInput.setText("");
        areaInput.setEditable(true);
        areaTeksSaatIni.setText("");
        lblStatus.setText("Masukkan teks lalu klik Mulai.");
        btnMulai.setEnabled(true);
        aturKontrolProses(false);
        btnSalin.setEnabled(false);
        perbaruiKunci();
        areaInput.requestFocusInWindow();
    }

    private void aturKontrolProses(boolean aktif) {
        cmbMenu.setEnabled(aktif);
        btnProses.setEnabled(aktif);
        btnSelesai.setEnabled(aktif);
        txtKunci.setEnabled(aktif && perluKunci());
        lblKunci.setEnabled(aktif && perluKunci());
    }

    private boolean perluKunci() {
        return cmbMenu.getSelectedIndex() < 4; // ROT13 tidak butuh kunci
    }

    private void perbaruiKunci() {
        int idx = cmbMenu.getSelectedIndex();
        boolean aktif = sudahMulai && jumlahProses < MAKS_PROSES;

        if (idx <= 1) {
            lblKunci.setText("Kunci Vigenere (huruf/angka):");
        } else if (idx <= 3) {
            lblKunci.setText("Jumlah kolom (angka > 0):");
        } else {
            lblKunci.setText("Kunci:");
            txtKunci.setText("");
        }
        txtKunci.setEnabled(aktif && perluKunci());
        lblKunci.setEnabled(aktif && perluKunci());
    }

    private void mulai() {
        String masuk = areaInput.getText().replaceAll("\\s+", "");
        if (masuk.isEmpty()) {
            pesan("Teks tidak boleh kosong!");
            return;
        }
        teks = masuk;
        sudahMulai = true;
        areaInput.setText(masuk);
        areaInput.setEditable(false);
        btnMulai.setEnabled(false);
        aturKontrolProses(true);
        btnSalin.setEnabled(true);
        perbaruiTampilan();
        txtKunci.requestFocusInWindow();
    }

    private void proses() {
        if (!sudahMulai || jumlahProses >= MAKS_PROSES) {
            return;
        }

        int pilihan = cmbMenu.getSelectedIndex() + 1; // 1..6 seperti Main
        String kunci = txtKunci.getText();
        String hasil;

        try {
            switch (pilihan) {
                case 1:
                case 2:
                    if (kunci.isEmpty()) {
                        pesan("Kunci tidak boleh kosong!");
                        return;
                    }
                    if (!kunci.matches("[a-zA-Z0-9]+")) {
                        pesan("Kunci Vigenere hanya boleh berisi huruf dan angka!");
                        return;
                    }
                    hasil = (pilihan == 1)
                            ? Vigenere.enkripsi(teks, kunci)
                            : Vigenere.dekripsi(teks, kunci);
                    break;

                case 3:
                case 4:
                    int nilai;
                    try {
                        nilai = Integer.parseInt(kunci.trim());
                    } catch (NumberFormatException e) {
                        pesan("Kunci harus berupa angka!");
                        return;
                    }
                    if (nilai <= 0) {
                        pesan("Kunci harus lebih dari 0!");
                        return;
                    }
                    kunci = kunci.trim();
                    hasil = (pilihan == 3)
                            ? TransposisiKolom.enkripsi(teks, kunci)
                            : TransposisiKolom.dekripsi(teks, kunci);
                    break;

                case 5:
                    hasil = ROT13.enkripsi(teks);
                    kunci = "-";
                    break;

                default:
                    hasil = ROT13.dekripsi(teks);
                    kunci = "-";
                    break;
            }
        } catch (IllegalArgumentException e) {
            pesan(e.getMessage());
            return;
        }

        teks = hasil;
        jumlahProses++;
        riwayatMenu.add(pilihan - 1);
        riwayatKunci.add(kunci);
        btnSalinKunci.setEnabled(true);
        modelRiwayat.addRow(new Object[]{
                jumlahProses, MENU[pilihan - 1], kunci, hasil
        });
        txtKunci.setText("");

        if (jumlahProses >= MAKS_PROSES) {
            selesai();
        } else {
            perbaruiTampilan();
        }
    }

    private void selesai() {
        aturKontrolProses(false);
        lblStatus.setText("Selesai setelah " + jumlahProses
                + " proses. Teks akhir ada di bawah.");
        areaTeksSaatIni.setText(teks);
        btnSalin.setEnabled(true);
    }

    private void perbaruiTampilan() {
        areaTeksSaatIni.setText(teks);
        areaTeksSaatIni.setCaretPosition(0);
        lblStatus.setText("Proses ke-" + (jumlahProses + 1)
                + " dari " + MAKS_PROSES
                + "  (sisa " + (MAKS_PROSES - jumlahProses) + ")");
    }

    private void salin() {
        if (teks.isEmpty()) {
            return;
        }
        Toolkit.getDefaultToolkit().getSystemClipboard()
                .setContents(new StringSelection(teks), null);
        JOptionPane.showMessageDialog(this, "Teks disalin ke clipboard.",
                "Disalin", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Menyalin daftar metode + kunci yang dipakai, beserta urutan
     * pengembaliannya (dibalik, dan Enkripsi diganti Dekripsi).
     */
    private void salinKunci() {
        if (riwayatMenu.isEmpty()) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=== KUNCI KRIPTOGRAFI ===\n");
        sb.append("(Teks diproses tanpa spasi)\n\n");

        sb.append("Urutan proses yang dilakukan:\n");
        for (int i = 0; i < riwayatMenu.size(); i++) {
            sb.append(i + 1).append(". ")
                    .append(MENU[riwayatMenu.get(i)])
                    .append(" | Kunci: ").append(riwayatKunci.get(i))
                    .append("\n");
        }

        sb.append("\nUntuk mengembalikan teks, lakukan urutan dibalik:\n");
        int no = 1;
        for (int i = riwayatMenu.size() - 1; i >= 0; i--) {
            // 0<->1, 2<->3, 4<->5 : enkripsi <-> dekripsi
            int kebalikan = riwayatMenu.get(i) ^ 1;
            sb.append(no++).append(". ")
                    .append(MENU[kebalikan])
                    .append(" | Kunci: ").append(riwayatKunci.get(i))
                    .append("\n");
        }

        Toolkit.getDefaultToolkit().getSystemClipboard()
                .setContents(new StringSelection(sb.toString()), null);

        JTextArea pratinjau = new JTextArea(sb.toString());
        pratinjau.setEditable(false);
        pratinjau.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane spPratinjau = new JScrollPane(pratinjau);
        spPratinjau.setPreferredSize(new Dimension(480, 220));
        JOptionPane.showMessageDialog(this, spPratinjau,
                "Kunci disalin ke clipboard",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void reset() {
        keadaAwal();
    }

    private void pesan(String isi) {
        JOptionPane.showMessageDialog(this, isi, "Peringatan",
                JOptionPane.WARNING_MESSAGE);
    }

    // ======================================
    // Titik masuk
    // ======================================
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // pakai tampilan bawaan
        }
        SwingUtilities.invokeLater(() -> new KriptoGUI().setVisible(true));
    }
}