import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Menu extends JFrame {

    public static void main(String[] args) {

        // Buat object window
        Menu window = new Menu();

        // Atur ukuran window
        window.setSize(480, 540);

        // Letakkan window di tengah layar
        window.setLocationRelativeTo(null);

        // Isi window
        window.setContentPane(window.mainPanel);

        // Ubah warna background
        window.getContentPane().setBackground(Color.white);

        // Tampilkan window
        window.setVisible(true);

        // Agar program bisa berhenti saat window ditutup
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // Variabel untuk menyimpan status baris yang dipilih pada tabel
    private int selectedIndex = -1;

        // === Data Mahasiswa ===
    // List untuk menampung semua objek Mahasiswa
    private ArrayList<Mahasiswa> listMahasiswa;

    // Objek database untuk koneksi dan operasi database
    private DB_Database database;

        // === Komponen GUI ===
    // Panel utama yang menampung semua elemen UI
    private JPanel mainPanel;

        // === Input Data Mahasiswa ===
    // Field input untuk NIM mahasiswa
    private JTextField nimField;
    // Field input untuk nama mahasiswa
    private JTextField namaField;
    // Combo box untuk memilih jenis kelamin mahasiswa (misalnya "Laki-laki" atau "Perempuan")
    private JComboBox jenisKelaminComboBox;

        // === Tabel Mahasiswa ===
    // Tabel yang menampilkan daftar mahasiswa
    private JTable mahasiswaTable;

        // === Tombol Aksi ===
    // Tombol untuk menambahkan atau memperbarui data mahasiswa
    private JButton addUpdateButton;
    // Tombol untuk membatalkan aksi atau membersihkan input
    private JButton cancelButton;
    // Tombol untuk menghapus data mahasiswa yang dipilih
    private JButton deleteButton;

        // === Label untuk Form ===
    private JLabel titleLabel;  // Label judul aplikasi atau form
    private JLabel nimLabel;  // Label untuk field NIM
    private JLabel namaLabel;  // Label untuk field Nama
    private JLabel jenisKelaminLabel;  // Label untuk field jenis kelamin

        // === Jalur Masuk Mahasiswa ===
    // Radio button untuk memilih jalur masuk mahasiswa (SNBP)
    private JRadioButton SNBP;
    // Radio button untuk memilih jalur masuk mahasiswa (SNBT)
    private JRadioButton SNBT;
    // Radio button untuk memilih jalur masuk mahasiswa (Mandiri)
    private JRadioButton Mandiri;
    // Grup untuk mengelompokkan radio button jalur masuk agar hanya satu yang bisa dipilih
    private ButtonGroup jalurMasukGroup;

    public Menu() {
        // Inisialisasi listMahasiswa sebagai ArrayList kosong untuk menyimpan data mahasiswa
        listMahasiswa = new ArrayList<>();

        // Inisialisasi objek database untuk koneksi ke database
        database = new DB_Database();

        // Set model tabel mahasiswa dengan data dari database
        mahasiswaTable.setModel(setTable());

        // Ubah styling title agar lebih besar dan tebal
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));

        // Mengatur isi combo box untuk pilihan jenis kelamin
        String[] jenisKelaminData = {"", "Laki-laki", "Perempuan"};
        jenisKelaminComboBox.setModel(new DefaultComboBoxModel<>(jenisKelaminData));

        // Mengelompokkan radio button agar hanya satu yang bisa dipilih dalam satu waktu
        jalurMasukGroup = new ButtonGroup();
        jalurMasukGroup.add(SNBP);
        jalurMasukGroup.add(SNBT);
        jalurMasukGroup.add(Mandiri);

        // Sembunyikan tombol delete saat pertama kali aplikasi dijalankan
        deleteButton.setVisible(false);

        // Event listener untuk tombol add/update
        addUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedIndex == -1) {
                    // Jika tidak ada baris yang dipilih, tambahkan data baru
                    InsertData();
                } else {
                    // Jika ada baris yang dipilih, lakukan update data
                    updateData();
                }
            }
        });

        // Event listener untuk tombol delete
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Hanya hapus data jika ada baris yang dipilih
                if (selectedIndex >= 0) {
                    deleteData();
                }
            }
        });

        // Event listener untuk tombol cancel
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Membersihkan form input
                clearForm();
            }
        });

        // Event listener saat tabel mahasiswa diklik
        mahasiswaTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Ubah selectedIndex sesuai dengan baris yang diklik
                selectedIndex = mahasiswaTable.getSelectedRow();
                // Tampilkan indeks yang dipilih di console
                System.out.println("Selected Index: " + selectedIndex);

                // Ambil data dari baris yang diklik
                String selectedNim = mahasiswaTable.getModel().getValueAt(selectedIndex, 1).toString();
                String selectedNama = mahasiswaTable.getModel().getValueAt(selectedIndex, 2).toString();
                String selectedJenisKelamin = mahasiswaTable.getModel().getValueAt(selectedIndex, 3).toString();
                String selectedJalurMasuk = mahasiswaTable.getModel().getValueAt(selectedIndex, 4).toString();

                // Masukkan data ke dalam field input
                nimField.setText(selectedNim);
                namaField.setText(selectedNama);
                jenisKelaminComboBox.setSelectedItem(selectedJenisKelamin);

                // Atur radio button sesuai jalur masuk mahasiswa
                if (selectedJalurMasuk.equals("SNBP")) {
                    SNBP.setSelected(true);
                } else if (selectedJalurMasuk.equals("SNBT")) {
                    SNBT.setSelected(true);
                } else if (selectedJalurMasuk.equals("Mandiri")) {
                    Mandiri.setSelected(true);
                }

                // Ubah tombol "Add" menjadi "Update"
                addUpdateButton.setText("Update");

                // Tampilkan tombol delete
                deleteButton.setVisible(true);
            }
        });
    }



    // Method untuk mengatur model tabel mahasiswa
    public final DefaultTableModel setTable() {
        // Tentukan nama kolom untuk tabel
        Object[] column = {"No", "NIM", "Nama", "Jenis Kelamin", "Jalur Masuk"};

        // Buat objek DefaultTableModel dengan kolom yang telah didefinisikan
        DefaultTableModel temp = new DefaultTableModel(null, column);

        try {
            // Eksekusi query untuk mengambil data dari tabel mahasiswa
            ResultSet resultSet = database.selectQuery("SELECT * FROM mahasiswa");

            int i = 0; // Variabel untuk nomor urut
            while (resultSet.next()) {
                // Buat array untuk menyimpan data dari setiap baris
                Object[] row = new Object[5];

                row[0] = i + 1; // Nomor urut
                row[1] = resultSet.getString("nim"); // Ambil NIM dari database
                row[2] = resultSet.getString("nama"); // Ambil Nama dari database
                row[3] = resultSet.getString("jenis_kelamin"); // Ambil Jenis Kelamin
                row[4] = resultSet.getString("jalur_masuk"); // Ambil Jalur Masuk

                temp.addRow(row); // Tambahkan data ke dalam model tabel
                i++; // Increment nomor urut
            }
        } catch (SQLException e) {
            // Tangani error jika terjadi kesalahan dalam query
            throw new RuntimeException(e);
        }

        return temp; // Kembalikan model tabel yang sudah diisi data
    }

    // Method untuk menambahkan data mahasiswa baru ke database
    public void InsertData() {
        // Ambil nilai dari textfield dan combo box
        String nim = nimField.getText();
        String nama = namaField.getText();
        String jenisKelamin = jenisKelaminComboBox.getSelectedItem().toString();
        String jalurMasuk = getSelectedJalurMasuk(); // Mengambil data dari radio button

        // Validasi input: Pastikan semua field terisi
        if (nim.isEmpty() || nama.isEmpty() || jenisKelamin.isEmpty() || jalurMasuk.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Harap lengkapi semua data!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Pengecekan apakah NIM sudah ada di database
        String checkQuery = "SELECT * FROM mahasiswa WHERE nim = '" + nim + "'";
        ResultSet rs = database.selectQuery(checkQuery);
        try {
            if (rs.next()) { // Jika ada hasil, berarti NIM sudah terdaftar
                JOptionPane.showMessageDialog(null, "NIM sudah terdaftar! Gunakan NIM lain.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (SQLException e) {
            // Tangani error jika terjadi kesalahan dalam pengecekan
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat mengecek NIM!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Jika NIM belum ada, masukkan data baru ke dalam database
        String sql = "INSERT INTO mahasiswa VALUES (null, '" + nim + "', '" + nama + "', '" + jenisKelamin + "', '" + jalurMasuk + "');";
        database.insertUpdateDeleteQuery(sql);

        // Perbarui tabel setelah menambahkan data baru
        mahasiswaTable.setModel(setTable());

        // Bersihkan form input setelah insert
        clearForm();

        // Beri feedback kepada pengguna bahwa data berhasil ditambahkan
        JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
        System.out.println("Insert berhasil!");
    }

    // Metode untuk mendapatkan jalur masuk dari radio button yang dipilih
    private String getSelectedJalurMasuk() {
        if (SNBP.isSelected()) {
            return "SNBP"; // Jika SNBP dipilih
        } else if (SNBT.isSelected()) {
            return "SNBT"; // Jika SNBT dipilih
        } else if (Mandiri.isSelected()) {
            return "Mandiri"; // Jika Mandiri dipilih
        }
        return " "; // Jika tidak ada radio button yang dipilih
    }

    // Metode untuk memperbarui data mahasiswa yang dipilih
    public void updateData() {
        // Ambil data dari form input
        String selectedNim = nimField.getText();
        String selectedNama = namaField.getText();
        String selectedJenisKelamin = jenisKelaminComboBox.getSelectedItem().toString();
        String selectedJalurMasuk = getSelectedJalurMasuk(); // Ambil dari radio button

        // Validasi input: Pastikan semua field terisi
        if (selectedNim.isEmpty() || selectedNama.isEmpty() || selectedJenisKelamin.isEmpty() || selectedJalurMasuk.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Harap lengkapi semua data!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Ambil NIM lama berdasarkan baris yang dipilih di tabel
            String oldNim = mahasiswaTable.getModel().getValueAt(selectedIndex, 1).toString();

            // Cek apakah NIM baru sudah digunakan oleh mahasiswa lain (tidak boleh duplikat)
            String checkQuery = "SELECT COUNT(*) FROM mahasiswa WHERE nim = '" + selectedNim + "' AND nim != '" + oldNim + "'";
            ResultSet rs = database.selectQuery(checkQuery);

            if (rs.next() && rs.getInt(1) > 0) {
                JOptionPane.showMessageDialog(null, "NIM sudah digunakan oleh mahasiswa lain!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Jika NIM valid, lakukan update data di database
            String updateQuery = "UPDATE mahasiswa SET " +
                    "nim = '" + selectedNim + "', " +
                    "nama = '" + selectedNama + "', " +
                    "jenis_kelamin = '" + selectedJenisKelamin + "', " +
                    "jalur_masuk = '" + selectedJalurMasuk + "' " +
                    "WHERE nim = '" + oldNim + "'";

            database.insertUpdateDeleteQuery(updateQuery); // Eksekusi query update

            // Perbarui tampilan tabel setelah update
            mahasiswaTable.setModel(setTable());

            // Bersihkan form input setelah update
            clearForm();

            // Feedback ke pengguna
            System.out.println("Update berhasil!");
            JOptionPane.showMessageDialog(null, "Data berhasil diubah!");

        } catch (SQLException e) {
            // Tangani error jika terjadi kesalahan saat update data
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat mengupdate data", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Metode untuk menghapus data mahasiswa yang dipilih
    public void deleteData() {
        // Tampilkan dialog konfirmasi sebelum menghapus data
        int confirm = JOptionPane.showConfirmDialog(null,
                "Apakah Anda yakin ingin menghapus data ini?", // Pesan konfirmasi
                "Konfirmasi Hapus", // Judul dialog
                JOptionPane.YES_NO_OPTION, // Opsi pilihan (Yes/No)
                JOptionPane.WARNING_MESSAGE); // Ikon peringatan

        // Jika pengguna memilih "YES"
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Ambil NIM dari baris yang dipilih di tabel
                String selectedNim = mahasiswaTable.getModel().getValueAt(selectedIndex, 1).toString();

                // Buat query SQL untuk menghapus data dari database berdasarkan NIM
                String deleteQuery = "DELETE FROM mahasiswa WHERE nim = '" + selectedNim + "'";
                database.insertUpdateDeleteQuery(deleteQuery); // Eksekusi query delete

                // Perbarui tampilan tabel setelah data dihapus
                mahasiswaTable.setModel(setTable());

                // Kosongkan form input setelah penghapusan data
                clearForm();

                // Feedback ke pengguna
                System.out.println("Delete berhasil!");
                JOptionPane.showMessageDialog(null, "Data berhasil dihapus!");
            } catch (Exception e) {
                // Tangani error jika terjadi kesalahan saat menghapus data
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat menghapus data", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // Jika pengguna memilih "NO", penghapusan dibatalkan
            System.out.println("Penghapusan dibatalkan.");
        }
    }

    // Metode untuk membersihkan form input
    public void clearForm() {
        // Kosongkan semua textfield
        nimField.setText("");
        namaField.setText("");

        // Reset combo box ke pilihan pertama
        jenisKelaminComboBox.setSelectedIndex(0);

        // Kosongkan pilihan radio button dalam ButtonGroup
        jalurMasukGroup.clearSelection();

        // Ubah teks tombol "Update" menjadi "Add" kembali
        addUpdateButton.setText("Add");

        // Sembunyikan tombol delete setelah form dikosongkan
        deleteButton.setVisible(false);

        // Reset selectedIndex ke -1 (tidak ada baris yang dipilih)
        selectedIndex = -1;
    }
}
