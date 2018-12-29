class Score_Cell extends Cell {
  int x_dimension;
  Score_Cell(int y, int x, int width, int x_dimension) {
    super(y, x, width);
    this.x_dimension = x_dimension;
  }

  void display() {
    strokeWeight(3);
    fill(237, 244, 159);
    rect(x, y, x_dimension, width);
    fill(0);
    strokeWeight(1);
    textSize(55);
    textAlign(LEFT, CENTER);
    text("Score : " + value, 75, 65);
  }
}
