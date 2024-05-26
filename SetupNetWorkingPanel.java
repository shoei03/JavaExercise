import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class SetupNetWorkingPanel extends JPanel{
    
    public static String IPAdress;
	public static Socket socket;
	public static PrintWriter out;

    public SetupNetWorkingPanel() {

        //サーバのIPアドレスの入力ダイアログを開く
		IPAdress = JOptionPane.showInputDialog(null,"IPアドレスを入力してください","IPアドレスの入力",JOptionPane.QUESTION_MESSAGE);
		if(IPAdress.equals("")){
			IPAdress = "127.0.0.1";//IPアドレスがないときは，"localhost"に接続する
		}

		//サーバに接続する
		try {
			//"localhost"は，自分内部への接続．localhostを接続先のIP Address（"133.42.155.201"形式）に設定すると他のPCのサーバと通信できる
			//10000はポート番号．IP Addressで接続するPCを決めて，ポート番号でそのPC上動作するプログラムを特定する
			socket = new Socket(IPAdress, 10000);
		} catch (UnknownHostException e) {
			System.err.println("ホストの IP アドレスが判定できません: " + e);
		} catch (IOException e) {
			System.err.println("エラーが発生しました: " + e);
		}

		try {
			out = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			System.err.println("エラーが発生しました: " + e);
		}
    }
}
