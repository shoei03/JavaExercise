import javax.swing.*;
import java.awt.event.*;
import java.util.Random;

public class MyComponent implements MouseListener {

    public int cardInt;
    public ImageIcon boardIcon;
    public ImageIcon backIcon;
    public ImageIcon cardIcons[];
    public JButton card[];
    public int firstSelectedIndex = 0;
    public int secondSelectedIndex;
    public int firstSelectedPlace;
    public int secondSelectedPlace;
    public JPanel cardPanel;
    public JPanel mymatchedCardPanel;
    public JPanel yourmatchedCardPanel;
    public JLabel myscoreLabel;
    public JLabel yourscoreLabel;
    public JLabel turnLabel;
    public JLabel timerLabel;
    public int myScore, yourScore;
    public int myTurn;
    public JPanel gamePanel;

    public  void sendMessage(String msg) {
        SetupNetWorkingPanel.out.println(msg);
        SetupNetWorkingPanel.out.flush();
    }

    public void mouseClicked(MouseEvent e) {//ボタンをクリックしたときの処理
        // どのボタンが押されたかを識別するための部品を得る
        JButton theButton = (JButton)e.getComponent();//クリックしたオブジェクトを得る．型が違うのでキャストする
        String index = theButton.getActionCommand();//ボタンの配列の番号を取り出す
        String theText = theButton.getText();
        Icon theIcon = theButton.getIcon();//theIconには，現在のボタンに設定されたアイコンが入る

        // クリックされたボタンによって、処理を変える
        if (theIcon == backIcon && myTurn == 1) {
            String msg = "FRIP " + index + " " + theText;
            sendMessage(msg);
        } else if (theText.equals("シャッフル") && myTurn == 1 && firstSelectedIndex == 0) {
            Random rnd = new Random();
            int value = rnd.nextInt(101) + 1;
            String msg = "SHUFFLE " + value;
            sendMessage(msg);
        } else if (theText.equals("パス") && myTurn == 1 && firstSelectedIndex == 0) {
            String msg = "PASS";
            sendMessage(msg);
        } else if (theText.equals("タイトル")) {
            String msg = "TITLE";
            sendMessage(msg);
        }
    }
	
	
	public void mouseEntered(MouseEvent e) {//マウスがオブジェクトに入ったときの処理
	}
	
	public void mouseExited(MouseEvent e) {//マウスがオブジェクトから出たときの処理
	}
	
	public void mousePressed(MouseEvent e) {//マウスでオブジェクトを押したときの処理（クリックとの違いに注意）
	}
	
	public void mouseReleased(MouseEvent e) {//マウスで押していたオブジェクトを離したときの処理
	}
	
}
