package frame;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import helpers.Koneksi;

import javax.swing.*;
import java.sql.*;

public class KomoditasInputFrame extends JFrame{
    private JPanel mainPanel;
    private JTextField idTextField;
    private JTextField produksiTextField;
    private JButton batalButton;
    private JButton simpanButton;
    private JTextField hasilTextField;

    int id;

    public void setId(int id){
        this.id = id;
    }


    public KomoditasInputFrame(){
        simpanButton.addActionListener(e->{
            String nama_produksi = produksiTextField.getText();
            String hasil = hasilTextField.getText();
            if (nama_produksi.equals("")){
                JOptionPane.showMessageDialog(null,
                        "Isi Data Produksi",""+"Validasi Data Kosong",
                        JOptionPane.WARNING_MESSAGE);
                produksiTextField.requestFocus();
                return;
            }

            Connection c = Koneksi.getConnection();
            PreparedStatement ps;

            try {
            if(id == 0){
                String cekSQL = "SELECT * FROM produksi WHERE nama_produksi = ?";
                ps =c.prepareStatement(cekSQL);
                ps.setString(1,nama_produksi);
                ResultSet rs = ps.executeQuery();
                if (rs.next()){
                    JOptionPane.showMessageDialog(null,
                            "Data Produksi Sudah Ada","Validasi Data Sama",
                            JOptionPane.WARNING_MESSAGE);
                }else {
                    String insertSQL = "INSERT INTO produksi SET nama_produksi =?, hasil=?";
                    ps = c.prepareStatement(insertSQL);
                    ps.setString(1,nama_produksi);
                    ps.setString(2,hasil);
                    ps.executeUpdate();
                    dispose();
                  }


                }else{
                    String updateSQL = "UPDATE produksi SET nama_produksi = ?, hasil = ? WHERE id = ?";
                    ps = c.prepareStatement(updateSQL);
                    ps.setString(1,nama_produksi);
                    ps.setString(2,hasil);
                    ps.setInt(3, id);
                    ps.executeUpdate();
                    dispose();
            }

            }catch (SQLException ex){
                    throw new RuntimeException(ex);
                }

        });


        batalButton.addActionListener(e->{
            dispose();
        });
        init();

    }

    public void init(){
        setContentPane(mainPanel);
        setTitle("Input Data produksi");
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void isiKomponen(){
        Connection c = Koneksi.getConnection();
        String findSQL = "SELECT * FROM produksi WHERE id=?";
        PreparedStatement ps;

        try {
            ps = c.prepareStatement(findSQL);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                produksiTextField.setText(rs.getString("nama_produksi"));
                hasilTextField.setText(rs.getString("hasil"));

            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }


    }

