package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// Lớp Caro mở rộng từ JFrame và triển khai ActionListener để xử lý giao diện người dùng và sự kiện
public class Caro extends JFrame implements ActionListener {
	int column = 20, row = 30, count = 0; // Kích thước của lưới và bộ đếm lượt chơi
	int xUndo[] = new int[column * row]; // Lưu trữ tọa độ x của các nước đi để hoàn tác
	int yUndo[] = new int[column * row]; // Lưu trữ tọa độ y của các nước đi để hoàn tác
	boolean tick[][] = new boolean[column + 2][row + 2]; // Theo dõi ô nào đã được nhấp
	boolean win = false; // Cờ để kiểm tra nếu có ai đó thắng
	int Size = 0; // Kích thước của ngăn xếp hoàn tác
	Container cn; // Container chính
	JPanel pn, pn2; // Các panel cho bảng và các điều khiển
	JLabel lb; // Nhãn để hiển thị lượt của người chơi hiện tại
	JButton newGame_bt, undo_bt, exit_bt; // Các nút điều khiển
	private JButton b[][] = new JButton[column + 2][row + 2]; // Các nút cho lưới
	private int data[][] = new int[column + 2][row + 2]; // Lưu trữ trạng thái của mỗi ô

	// Hàm khởi tạo để khởi động trò chơi
	public Caro(String s) {
		super(s);
		cn = this.getContentPane();
		pn = new JPanel();
		pn.setLayout(new GridLayout(column, row)); // Thiết lập lưới với số cột và hàng
		for (int i = 0; i <= column + 1; i++)
			for (int j = 0; j <= row + 1; j++) {
				b[i][j] = new JButton(); // Tạo các nút cho lưới
				b[i][j].setActionCommand(i + " " + j); // Thiết lập lệnh hành động cho mỗi nút
				b[i][j].setIcon(getIcon("h")); // Đặt biểu tượng mặc định cho các nút
				b[i][j].addActionListener(this); // Thêm trình lắng nghe sự kiện cho các nút
				data[i][j] = 0; // Khởi tạo dữ liệu ô là 0
				tick[i][j] = true; // Đánh dấu ô là có thể nhấp
			}
		for (int i = 1; i <= column; i++)
			for (int j = 1; j <= row; j++)
				pn.add(b[i][j]); // Thêm các nút vào panel
		lb = new JLabel("X Đánh Trước"); // Nhãn hiển thị lượt của người chơi
		newGame_bt = new JButton("New Game"); // Nút bắt đầu trò chơi mới
		undo_bt = new JButton("Undo"); // Nút hoàn tác
		exit_bt = new JButton("Exit"); // Nút thoát
		newGame_bt.addActionListener(this); // Thêm trình lắng nghe sự kiện cho nút bắt đầu trò chơi mới
		undo_bt.addActionListener(this); // Thêm trình lắng nghe sự kiện cho nút hoàn tác
		exit_bt.addActionListener(this); // Thêm trình lắng nghe sự kiện cho nút thoát
		exit_bt.setForeground(Color.RED); // Đặt màu chữ cho nút thoát
		cn.add(pn); // Thêm panel vào container chính
		pn2 = new JPanel();
		pn2.setLayout(new FlowLayout()); // Thiết lập layout cho panel điều khiển
		pn2.add(lb); // Thêm nhãn vào panel điều khiển
		pn2.add(newGame_bt); // Thêm nút bắt đầu trò chơi mới vào panel điều khiển
		pn2.add(undo_bt); // Thêm nút hoàn tác vào panel điều khiển
		pn2.add(exit_bt); // Thêm nút thoát vào panel điều khiển
		cn.add(pn2, "North"); // Thêm panel điều khiển vào container chính, phía bắc
		this.setVisible(true); // Hiển thị JFrame
		this.setSize(950, 700); // Đặt kích thước JFrame
		this.setLocationRelativeTo(null); // Đặt JFrame ở giữa màn hình
		this.setDefaultCloseOperation(EXIT_ON_CLOSE); // Đặt hành động khi đóng JFrame
		undo_bt.setEnabled(false); // Vô hiệu hóa nút hoàn tác ban đầu
		this.setResizable(false); // Không cho phép thay đổi kích thước JFrame
	}

	// Phương thức kiểm tra chiến thắng
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
		// nếu không đường chéo nào thỏa mãn thì trả về false.
		return false;
	}

	// Phương thức hoàn tác
	public void undo() {
		if (Size > 0) {
			b[xUndo[Size - 1]][yUndo[Size - 1]].setIcon(getIcon("h")); // Đặt lại biểu tượng mặc định
			data[xUndo[Size - 1]][yUndo[Size - 1]] = 0; // Đặt lại dữ liệu ô là 0
			b[xUndo[Size - 1]][yUndo[Size - 1]].setActionCommand(xUndo[Size - 1] + " " + yUndo[Size - 1]); // Thiết lập lại lệnh hành động
			tick[xUndo[Size - 1]][yUndo[Size - 1]] = true; // Đánh dấu ô là có thể nhấp
			count--; // Giảm bộ đếm lượt chơi
			if (count % 2 == 0)
				lb.setText("Lượt Của X");
			else
				lb.setText("Lượt Của O");
			Size--; // Giảm kích thước ngăn xếp hoàn tác
			b[xUndo[Size - 1]][yUndo[Size - 1]].setBackground(Color.gray); // Đặt lại màu nền của ô
			if (Size == 0)
				undo_bt.setEnabled(false); // Vô hiệu hóa nút hoàn tác nếu không còn nước đi nào để hoàn tác
		}
	}

	// Phương thức thêm điểm
	public void addPoint(int i, int j) {
		xUndo[Size] = i; // Lưu tọa độ x của nước đi
		yUndo[Size] = j; // Lưu tọa độ y của nước đi
		Size++; // Tăng kích thước ngăn xếp hoàn tác
		if (count % 2 == 0) {
			b[i][j].setIcon(getIcon("x")); // Đặt biểu tượng X cho nút
			data[i][j] = 1; // Đặt dữ liệu ô là 1
			lb.setText("Lượt Của O"); // Hiển thị lượt của O
		} else {
			b[i][j].setIcon(getIcon("o")); // Đặt biểu tượng O cho nút
			data[i][j] = 2; // Đặt dữ liệu ô là 2
			lb.setText("Lượt Của X"); // Hiển thị lượt của X
		}
		tick[i][j] = false; // Đánh dấu ô là không thể nhấp
		count = 1 - count; // Đổi lượt chơi
		b[i][j].setBackground(Color.GRAY); // Đặt màu nền của ô là màu xám
		undo_bt.setEnabled(true); // Kích hoạt nút hoàn tác
	}

	// Phương thức xử lý sự kiện
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "New Game") {
			new Caro("GAME CARO"); // Bắt đầu trò chơi mới
			this.dispose(); // Đóng cửa sổ hiện tại
		} else if (e.getActionCommand() == "Undo") {
			undo(); // Thực hiện hoàn tác
		} else if (e.getActionCommand() == "Exit") {
			System.exit(0); // Thoát chương trình
		} else {
			if (win == true)
				return; // Nếu đã có người thắng thì không làm gì
			String s = e.getActionCommand();
			int k = s.indexOf(32);
			int i = Integer.parseInt(s.substring(0, k));
			int j = Integer.parseInt(s.substring(k + 1, s.length()));
			if (tick[i][j]) {
				addPoint(i, j); // Thêm điểm nếu ô có thể nhấp
			}
			if (checkWin(i, j)) {
				lb.setBackground(Color.MAGENTA); // Đặt nền nhãn là màu tím
				lb.setText((data[i][j] == 1 ? "X" : "O") + " WIN"); // Hiển thị người thắng
				win = true; // Đặt cờ thắng
				undo_bt.setEnabled(false); // Vô hiệu hóa nút hoàn tác
				newGame_bt.setBackground(Color.YELLOW); // Đặt nền nút bắt đầu trò chơi mới là màu vàng
				JOptionPane.showMessageDialog(this, data[i][j] == 1 ? "X WIN" : "O WIN"); // Hiển thị thông báo người thắng
			}
		}
	}

	// Phương thức lấy biểu tượng
	public Icon getIcon(String name) {
		int width = 30;
		int height = 30;
		Image image = new ImageIcon(getClass().getResource("/icons/" + name + ".png")).getImage();
		Icon icon = new ImageIcon(image.getScaledInstance(width, height, image.SCALE_SMOOTH));
		return icon; // Trả về biểu tượng
	}

	// Phương thức main để chạy chương trình
	public static void main(String[] args) {
		new Caro("GAME CARO");
	}
}
