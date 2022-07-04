import frame.KomoditasViewFrame;
import helpers.Koneksi;

public class Main {
    public static void main(String[] args){
        Koneksi.getConnection();
        KomoditasViewFrame viewFrame = new KomoditasViewFrame();
        viewFrame.setVisible(true);
    }
}
