
class Cell {
  protected int x;
  protected int y;
  protected int width;
  protected int value;

  public Cell(int y, int x, int width) {
    this.width = width;
    this.x = x*width+1;
    this.y = y*width+1;
    this.value = 0;
  }

  public Cell(int y, int x, int width, int value) {
    this.width = width;
    this.x = x * this.width+1;
    this.y = y * this.width+1;
    this.value = value;
  }

  public boolean equals (Cell other) {
    if (this.value == other.value) {
      return true;
    }
    return false;
  }

  public void display() {
    strokeWeight(3);
    String cl = Colours.getString(""+value);
    int r = Integer.parseInt(cl.substring(0, 2), 16);
    int g = Integer.parseInt(cl.substring(2, 4), 16);
    int b = Integer.parseInt(cl.substring(4, 6), 16);
    fill(r, g, b);
    rect(x, y, width, width);
    fill(0);
    strokeWeight(1);

    if (value < 10000) {
      textSize(60);
    } else if (value < 100000) {
      textSize(45);
    } else {
      textSize(35);
    }
    textAlign(CENTER, CENTER);
    text(value, x+(width/2), y+(width/2));
  }

  String toString() {
    return ""+value;
  }
}
