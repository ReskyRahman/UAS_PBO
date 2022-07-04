package frame;

import helpers.Koneksi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;

public class KomoditasViewFrame extends JFrame{
    private JPanel mainPanel;
    private JTextField cariTextField;
    private JButton cariButton;
    private JTable viewTable;
    private JButton tambahButton;
    private JButton ubahButton;
    private JButton hapusButton;
    private JButton batalButton;
    private JButton cetakButton;
    private JButton tutupButton;


    public KomoditasViewFrame(){
        ubahButton.addActionListener(e->{
            int barisTerpilih = viewTable.getSelectedRow();
            if(barisTerpilih<0){
                JOptionPane.showMessageDialog(null,
                        "Pilih dulu datanya",
                        "Validasi pilih data",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            TableModel tm = viewTable.getModel();
            int id = Integer.parseInt(tm.getValueAt(barisTerpilih,0).toString());
            KomoditasInputFrame inputFrame = new KomoditasInputFrame();
            inputFrame.setId(id);
            inputFrame.isiKomponen();
            inputFrame.setVisible(true);

        });


        tambahButton.addActionListener(e->{
        KomoditasInputFrame inputFrame = new KomoditasInputFrame();
        inputFrame.setVisible(true);

        });

        hapusButton.addActionListener(e->{
            int barisTerpilih = viewTable.getSelectedRow();
            if(barisTerpilih<0){
                JOptionPane.showMessageDialog(
                        null,
                        "Pilih dulu datanya",
                        "Validasi pilih data",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            int pilihan = JOptionPane.showConfirmDialog(
                    null,
                    "Yakin?",
                    "Konfirmasi hapus data",
                    JOptionPane.YES_NO_OPTION
            );

            if (pilihan == 0 ){
                TableModel tm = viewTable.getModel();
                String idString = tm.getValueAt(barisTerpilih,0).toString();
                int id = Integer.parseInt(idString);

                String deleteSQL = "DELETE FROM produksi WHERE id = ?";
                Connection c = Koneksi.getConnection();
                PreparedStatement ps;
                try {
                    ps = c.prepareStatement(deleteSQL);
                    ps.setInt(1,id);
                    ps.executeUpdate();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });


        cariButton.addActionListener(e -> {
            String keyword = cariTextField.getText();
            if (keyword.equals("")){
                JOptionPane.showMessageDialog(null,
                        "Isi Kata Kunci Pencarian",""+"Validasi Kata Kunci Pencarian Kosong",
                        JOptionPane.WARNING_MESSAGE);
                cariTextField.requestFocus();
                return;
            }

            Connection c = Koneksi.getConnection();
            keyword = "%" + cariTextField.getText() + "%";
            String searchSQL = "SELECT * FROM produksi WHERE nama_produksi like ?";


            try {
                PreparedStatement ps = c.prepareStatement(searchSQL);
                ps.setString(1, keyword);
                ResultSet rs = ps.executeQuery();
                DefaultTableModel dtm = (DefaultTableModel) viewTable.getModel();
                dtm.setRowCount(0);
                Object[] row = new Object[3];
                while (rs.next()){
                    row[0] = rs.getInt("id");
                    row[1] = rs.getString("nama_produksi");
                    row[2] = rs.getString("hasil");
                    dtm.addRow(row);
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
    });

    tutupButton.addActionListener(e->{
        dispose();
    });

    batalButton.addActionListener(e->{
        isiTabel();
    });
    addWindowListener(new WindowAdapter() {
        @Override
        public void windowActivated(WindowEvent e) {
            isiTabel();
        }
    });

        isiTabel();
        init();
    }

    public void init(){
        setContentPane(mainPanel);
        setTitle("Data Produksi");
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void isiTabel(){
        Connection c = Koneksi.getConnection();
        String selectSQL = "SELECT * FROM produksi";

        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(selectSQL);

            String[] header = {"Id","Produksi","Hasil"};
            DefaultTableModel dtm = new DefaultTableModel(header,0);
            viewTable.setModel(dtm);

            viewTable.getColumnModel().getColumn(0).setPreferredWidth(32);
            viewTable.getColumnModel().getColumn(0).setMinWidth(32);
            viewTable.getColumnModel().getColumn(0).setMaxWidth(32);

            Object[] row = new Object[3];
            while (rs.next()){
                row[0] = rs.getInt("id");
                row[1] = rs.getString("nama_produksi");
                row[2] = rs.getString("hasil");
                dtm.addRow(row);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
