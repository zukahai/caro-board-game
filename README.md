## <p align="center"> Làm Game Caro Cở bản Bằng Java </p>
<p align="center"> <img src="https://github.com/zukahai/HaiZuka/blob/master/Images/Caro-Game/1.png" alt="Tieude" /> </p>

## Làm Game Cờ Caro Đơn Giản Bằng Java

Cờ caro là một trò chơi rất phổ biến với tất cả mọi lứa tuổi, Đây là một trò chơi đối kháng giữa hai người, thường được dùng nó để giải trí sau những giờ làm việc căng thẳng. Việc code game trên một nền tảng nào đó sẽ giúp các bạn thực hành kỹ năng lập trình của mình rất tốt.

<p align="center"> <img src="https://github.com/zukahai/HaiZuka/blob/master/Images/Caro-Game/2.png" alt="blog" /> </p>

Trò chơi này được chơi trên bàn cờ gồm các ô vuông nằm trên cách hàng và các cột. Hai bên sẽ thay phiên nhau tích vào những ô vuông chưa được đánh ở trên bàn cờ. Ký hiệu mỗi nước đi của từng người là X hoặc O.
Người chơi sẽ phải dùng chiến thuật và kinh nghiệm để tạo thành một hàng ngang, hàng dọc, hoặc đường chéo có đủ 5 quân cờ của mình, (có thể áp dụng luật chặn hai đầu hoặc không), người chiến thắng là người tạo được hàng, cột hoặc đường chéo đủ 5 nước đi của mình trước.
Để chiến thắng, bạn cần tạo ra những nước cờ hiểm và độc, điểm đặc biệt của Caro, là bạn rất dễ bị thua nếu không để ý từng nước đi của đối phương mặc dù bạn có chiến thuật tốt hơn.
Đây là một trò chơi giúp bạn giải trí sau những giờ làm việc và học tập căng thẳng hay những thời gian rảnh cùng với bạn bè.

### Thiết kế giao diện và những dữ liệu cần thiết.

#### 1. Thiết kế giao diện.

Đây là một trong những trò chơi cơ bản nên phần giao diện của nó cũng hết sức đơn giản.

Bạn có thể tạo ra một mảng chứa các button gồm column cột và row hàng.

```Java
private JButton b[][] = new JButton[column + 2][row + 2];
```

Chúng ta có thể tạo thêm một số button để thực hiện thêm một số chức năng như new game, undo, exit, ...

<p align="center"> <img src="https://github.com/zukahai/HaiZuka/blob/master/Images/Caro-Game/3.png" alt="blog" /> </p>

#### 2. Những dữ liệu cần thiết.

##### 2.1 Kiểm soát những ô đã đánh.

Mỗi ô trống trong game caro chỉ được đánh một lần (không được phép đánh những ô đã đánh rồi) nên ta cần dùng một mảng 2 chiều tick có kiểu bool để kiểm soát những ô đã đánh.

```Java
	boolean tick[][] = new boolean[column + 2][row + 2];
```
Ta có thể hiểu được rằng nếu tick[i][j] nhận giá trị true nếu ô vuông ở hàng i, cột j chưa được đánh.

##### 2.2 Kiểm soát lượt đánh.

Hai người chơi sẽ đánh xen kẽ nhau, ví dụ, nếu lượt đánh này là của người đánh X, thì lượt sau sẽ là lượt của người đánh O và ngược lại.

Để kiểm soát việc này chúng ta làm khá đơn giản, chỉ cần sử dụng một biến count để đếm số bước đánh, nếu số bước đánh chẵn thì là lượt của X, còn lẻ thì là lượt đánh của O.

##### 2.3 Hỗ trợ đánh lại.

Với trờ chơi này, có thể do vội vàng hoặc không tập trung mà chúng ta đánh sai ô mà mình cần đánh, nếu như bạn xin phép thành công đối thủ cho bạn được đánh lại, thì bạn có thể xóa bước vừa đánh và đánh lại bước khác.

Để hỗ trợ cho tính năng trên chúng ta cần lưu lại tạo độ của các bước đã đánh theo thứ tự, ta có thể sử dụng hai mảng một chiều để thực hiện điều này.

```Java
	int xUndo[] = new int[column * row];
	int yUndo[] = new int[column * row];
```

### Các phương thức xử lý.

#### 1. Kiểm tra chiến thắng.

Người chiến thắng trong game là người có thể đánh được một hàng, một cột, hoặc một đường chéo gồm nhiều hơn hoặc bằng 5 ký tự của mình.

Để tối ưu nhất, ta nên kiểm tra ngay sau một bước đánh là tốt nhất, cần kiểm tra xem bước đánh vừa rồi có tạo thành điều kiện để chiến thắng hay không.

```Java
	public boolean checkWin(int i, int j) {
		int d = 0, k = i, h;
		// kiểm tra hàng
		while (b[k][j].getText() == b[i][j].getText()) {
			d++;
			k++;
		}
		k = i - 1;
		while (b[k][j].getText() == b[i][j].getText()) {
			d++;
			k--;
		}
		if (d > 4) return true;
		d = 0; h = j;
		// kiểm tra cột
		while(b[i][h].getText() == b[i][j].getText()) {
			d++;
			h++;
		}
		h = j - 1;
		while(b[i][h].getText() == b[i][j].getText()) {
			d++;
			h--;
		}
		if (d > 4) return true;
		// kiểm tra đường chéo 1
		h = i; k = j; d = 0;
		while (b[i][j].getText() == b[h][k].getText()) {
			d++;
			h++;
			k++;
		}
		h = i - 1; k = j - 1;
		while (b[i][j].getText() == b[h][k].getText()) {
			d++;
			h--;
			k--;
		}
		if (d > 4) return true;
		// kiểm tra đường chéo 2
		h = i; k = j; d = 0;
		while (b[i][j].getText() == b[h][k].getText()) {
			d++;
			h++;
			k--;
		}
		h = i - 1; k = j + 1;
		while (b[i][j].getText() == b[h][k].getText()) {
			d++;
			h--;
			k++;
		}
		if (d > 4) return true;
		// nếu không đương chéo nào thỏa mãn thì trả về false.
		return false;
	}
```

#### 2. Đánh thêm một bước vào bàn cờ.

Như đã nói ở trên, việc đánh thêm một ký tự vào số lượng ô đã đánh và cần kiểm tra xem ô đang định đánh đã được đánh hay chưa.

```Java
public void addPoint(int i, int j) {
		if (Size > 0)
			b[xUndo[Size - 1]][yUndo[Size - 1]].setBackground(background_cl);
		xUndo[Size] = i;
		yUndo[Size] = j;
		Size++;
		if (count % 2 == 0) {
			b[i][j].setText("X");
			b[i][j].setForeground(x_cl);
			lb.setText("Lượt Của O");
		}
		else {
			b[i][j].setText("O");
			b[i][j].setForeground(y_cl);
			lb.setText("Lượt Của X");
		}
		tick[i][j] = false;
		count  = 1 - count;
		b[i][j].setBackground(Color.GRAY);
		undo_bt.setEnabled(true);
	}
```

#### 3. Đánh lại (undo).

Để đánh lại một bước nào đó chúng ta cần có dánh sách các bước đã đánh, cần lưu ý một đều là nếu chưa đánh bước nào thì không thể sử dụng tính năng này nhé.

```Java
	public void undo() {
		if (Size > 0) {
			b[xUndo[Size - 1]][yUndo[Size - 1]].setText(" ");
			b[xUndo[Size - 1]][yUndo[Size - 1]].setActionCommand(xUndo[Size - 1]+ " " + yUndo[Size - 1]);
			b[xUndo[Size - 1]][yUndo[Size - 1]].setBackground(background_cl);
			tick[xUndo[Size - 1]][yUndo[Size - 1]] = true;
			count--;
			if (count % 2 == 0) lb.setText("Lượt Của X"); 
				else lb.setText("Lượt Của O");
			Size--;
			if (Size == 0)
				undo_bt.setEnabled(false);
		}
	}
```

### Kết.

Trên đây là cách mà mình đã tạo ra một game Caro đơn giản, nếu nhận được sự ủng hộ từ các bạn, mình sẽ biết tiếp bài tạo ra game Caro mà các bạn có thể đánh với máy với các cấp độ khác nhau.




## <p align="center">  :tv: Thanks for whatching :earth_africa: </p>
