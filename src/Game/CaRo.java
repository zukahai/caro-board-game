package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Caro extends JFrame implements ActionListener {
	int column = 20, row = 30, count = 0;
	int xUndo[] = new int[column * row];
	int yUndo[] = new int[column * row];
	boolean tick[][] = new boolean[column + 2][row + 2];
	boolean win = false;
	int Size = 0;
	Container cn;
	JPanel pn, pn2;
	JLabel lb;
	JButton newGame_bt, undo_bt, exit_bt;
	private JButton b[][] = new JButton[column + 2][row + 2];
	private int data[][] = new int[column + 2][row + 2];

	public Caro(String s) {
		super(s);
		cn = this.getContentPane();
		pn = new JPanel();
		pn.setLayout(new GridLayout(column, row));
		for (int i = 0; i <= column + 1; i++)
			for (int j = 0; j <= row + 1; j++) {
				b[i][j] = new JButton();
				b[i][j].setActionCommand(i + " " + j);
				b[i][j].setIcon(getIcon("h"));
				b[i][j].addActionListener(this);
				data[i][j] = 0;
				tick[i][j] = true;
			}
		for (int i = 1; i <= column; i++)
			for (int j = 1; j <= row; j++)
				pn.add(b[i][j]);
		lb = new JLabel("X Đánh Trước");
		newGame_bt = new JButton("New Game");
		undo_bt = new JButton("Undo");
		exit_bt = new JButton("Exit");
		newGame_bt.addActionListener(this);
		undo_bt.addActionListener(this);
		exit_bt.addActionListener(this);
		exit_bt.setForeground(Color.RED);
		cn.add(pn);
		pn2 = new JPanel();
		pn2.setLayout(new FlowLayout());
		pn2.add(lb);
		pn2.add(newGame_bt);
		pn2.add(undo_bt);
		pn2.add(exit_bt);
		cn.add(pn2, "North");
		this.setVisible(true);
		this.setSize(1000, 700);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		undo_bt.setEnabled(false);
	}

	public boolean checkWin(int i, int j) {
		int d = 0, k = i, h;
		// kiểm tra hàng
		while (data[k][j] == data[i][j] && k < column) {
			d++;
			k++;
		}
		k = i - 1;
		while (data[k][j] == data[i][j]) {
			d++;
			k--;
		}
		if (d > 4)
			return true;
		d = 0;
		h = j;
		// kiểm tra cột
		while (data[i][h] == data[i][j]) {
			d++;
			h++;
		}
		h = j - 1;
		while (data[i][h] == data[i][j]) {
			d++;
			h--;
		}
		if (d > 4)
			return true;
		// kiểm tra đường chéo 1
		h = i;
		k = j;
		d = 0;
		while (data[i][j] == data[h][k]) {
			d++;
			h++;
			k++;
		}
		h = i - 1;
		k = j - 1;
		while (data[i][j] == data[h][k]) {
			d++;
			h--;
			k--;
		}
		if (d > 4)
			return true;
		// kiểm tra đường chéo 2
		h = i;
		k = j;
		d = 0;
		while (data[i][j] == data[h][k]) {
			d++;
			h++;
			k--;
		}
		h = i - 1;
		k = j + 1;
		while (data[i][j] == data[h][k]) {
			d++;
			h--;
			k++;
		}
		if (d > 4)
			return true;
		// nếu không đương chéo nào thỏa mãn thì trả về false.
		return false;
	}

	public void undo() {
		if (Size > 0) {
			b[xUndo[Size - 1]][yUndo[Size - 1]].setIcon(getIcon("h"));
			data[xUndo[Size - 1]][yUndo[Size - 1]] = 0;
			b[xUndo[Size - 1]][yUndo[Size - 1]].setActionCommand(xUndo[Size - 1] + " " + yUndo[Size - 1]);
			tick[xUndo[Size - 1]][yUndo[Size - 1]] = true;
			count--;
			if (count % 2 == 0)
				lb.setText("Lượt Của X");
			else
				lb.setText("Lượt Của O");
			Size--;
			b[xUndo[Size - 1]][yUndo[Size - 1]].setBackground(Color.gray);
			if (Size == 0)
				undo_bt.setEnabled(false);
		}
	}

	public void addPoint(int i, int j) {
		xUndo[Size] = i;
		yUndo[Size] = j;
		Size++;
		if (count % 2 == 0) {
			b[i][j].setIcon(getIcon("x"));
			data[i][j] = 1;
			lb.setText("Lượt Của O");
		} else {
			b[i][j].setIcon(getIcon("o"));
			data[i][j] = 2;
			lb.setText("Lượt Của X");
		}
		tick[i][j] = false;
		count = 1 - count;
		b[i][j].setBackground(Color.GRAY);
		undo_bt.setEnabled(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "New Game") {
			new Caro("GAME CARO");
			this.dispose();
		} else if (e.getActionCommand() == "Undo") {
			undo();
		} else if (e.getActionCommand() == "Exit") {
			System.exit(0);
			;
		} else {
			if (win == true)
				return;
			String s = e.getActionCommand();
			int k = s.indexOf(32);
			int i = Integer.parseInt(s.substring(0, k));
			int j = Integer.parseInt(s.substring(k + 1, s.length()));
			if (tick[i][j]) {
				addPoint(i, j);
			}
			if (checkWin(i, j)) {
				lb.setBackground(Color.MAGENTA);
				lb.setText((data[i][j] == 1 ? "X" : "O") + " WIN");
				win = true;
				undo_bt.setEnabled(false);
				newGame_bt.setBackground(Color.YELLOW);
				JOptionPane.showMessageDialog(this, data[i][j] == 1 ? "X WIN" : "O WIN");
			}
		}
	}

	public Icon getIcon(String name) {
		int width = 30;
		int height = 30;
		Image image = new ImageIcon(getClass().getResource("/icons/" + name + ".png")).getImage();
		Icon icon = new ImageIcon(image.getScaledInstance(width, height, image.SCALE_SMOOTH));
		return icon;
	}

	public static void main(String[] args) {
		new Caro("GAME CARO");
	}
}