import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import javax.swing.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class sketch_2048 extends PApplet {

java.awt.Insets insets;
Game_Manager  gm;
//surface.setSize(23, 23);
int[][] old_grid;
boolean grid_changed = false;
boolean key_released = false;
JSONObject Colours;


public void setup()
{
  
  surface.setTitle("2 0 4 8");
  Colours = loadJSONObject("Colours.json");
  gm = new Game_Manager(4, 4, 150, 2048);
  gm.spawn();
  gm.spawn();
  gm.display_grid();
  old_grid = Grid_Manipulator.copyGrid_values(gm.grid, old_grid);
  noLoop();
}

public void draw() {
  gm.display_grid();
}


public void keyPressed() {
  boolean played = true;
  old_grid = Grid_Manipulator.copyGrid_values(gm.grid, old_grid);
  if (keyCode == DOWN) {
    gm.move_down();
  } else if (keyCode == UP) {  
    gm.move_up();
  } else if (keyCode == RIGHT) {
    ;
    gm.move_right();
  } else if (keyCode == LEFT) {
    gm.move_left();
  } else {
    played = false;
  }

  if (played == true && Grid_Manipulator.grid_changed(gm.grid, old_grid) == true) {
    gm.spawn();
  }  
  redraw();
}

public void keyReleased() {
  if (keyCode == LEFT || keyCode == RIGHT || keyCode == UP || keyCode == DOWN) {
    gm.game_state();
  }
}

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

  public String toString() {
    return ""+value;
  }
}
class Game_Manager {  
  Cell[][] grid;
  Score_Cell score;
  Menu menu;
  int max_block;
  Game_Manager(int num_row, int num_cols, int width_of_cells, int max_block) {
    grid = new Cell[num_row][num_cols];
    score = new Score_Cell(0, 0, width_of_cells, 600);
    menu = new Menu();
    this.max_block = max_block;
    grid = new Cell[num_row][num_cols];
    initialize_grid(width_of_cells);
  }

  public void spawn() {
    ArrayList<Integer> free_spots_x = new ArrayList<Integer>();
    ArrayList<Integer> free_spots_y = new ArrayList<Integer>();
    for (int i = 0; i< 4; i++) {
      for (int j =0; j<4; j++) {
        if (grid[i][j].value == 0) {
          free_spots_x.add(i);
          free_spots_y.add(j);
        }
      }
    }

    if (free_spots_x.size() > 0 ) {
      int seed = PApplet.parseInt(random(free_spots_x.size()));
      int chance = PApplet.parseInt(random(10));
      if (chance > 1) {
        grid[free_spots_x.get(seed)][free_spots_y.get(seed)].value = 2;
      } else {
        grid[free_spots_x.get(seed)][free_spots_y.get(seed)].value = 4;
      }
    }
  }

  public void move_left() {
    for (int i = 0; i < gm.grid.length; i++) {
      grid[i] = Row_Manipulator.shift_row_left(grid[i]);
      grid[i] = Row_Manipulator.colapse("LEFT", grid[i], score);
      grid[i] = Row_Manipulator.shift_row_left(grid[i]);
    }
  }

  public void move_right() {
    for (int i = 0; i < gm.grid.length; i++) {
      grid[i] = Row_Manipulator.shift_row_right(grid[i]);
      grid[i] = Row_Manipulator.colapse("RIGHT", grid[i], score);
      grid[i] = Row_Manipulator.shift_row_right(grid[i]);
    }
  }

  public void move_up() {
    grid = Grid_Manipulator.transpose(grid);
    grid = Grid_Manipulator.swapRows(grid);      
    move_left();
    grid = Grid_Manipulator.swapRows(grid);
    grid = Grid_Manipulator.transpose(grid);
  }


  public void move_down() {
    grid = Grid_Manipulator.transpose(grid);
    grid = Grid_Manipulator.swapRows(grid);      
    move_right();
    grid = Grid_Manipulator.swapRows(grid);
    grid = Grid_Manipulator.transpose(grid);
  }


  public void game_state() {
    redraw();
    if (Grid_Manipulator.game_over(grid) == true) {
      String choice = menu.you_lost_menu();
      if(choice == null){
        exit();
      }
      else if (choice.equals("New Game")) {
        initialize_grid();
        score.value = 0;
        display_grid();
      } else if (choice.equals("Exit")) {
        exit();
      }
    }
    else if (Grid_Manipulator.win_game(grid, max_block) == true) {
      String choice = menu.you_win();
      if(choice == null){
        exit();
      }
      else if (choice.equals("New Game")) {
        initialize_grid();
        score.value = 0;
      } 
      else if (choice.equals("Continue")) {
        max_block = -1;
      } 
      else if (choice.equals("Exit")) {
        exit();
      }
    }
  }
  public void initialize_grid() {
    for (int i = 0; i< this.grid.length; i++) {
      for (int j =0; j<this.grid[i].length; j++) {
        this.grid[i][j].value = 0;
      }
    }
  }

  public void initialize_grid(int width_of_block) {
    for (int i = 0; i< grid.length; i++) {
      for (int j =0; j<grid[i].length; j++) {
        grid[i][j] = new Cell(i+1, j, width_of_block);
      }
    }
  }

  public void display_grid() {
    beginShape();
    score.display();
    score.display();
    for (int i = 0; i< 4; i++) {
      for (int j =0; j<4; j++) {
        grid[i][j].display();
      }
    }
    endShape(CLOSE);
  }
} 
static class Grid_Manipulator {
  public static Cell[][] transpose(Cell[][] grid) {
    for (int i = 0; i <grid.length; i++) {
      for (int j = i; j <grid[0].length; j++) {
        int value = grid[i][j].value;
        grid[i][j].value = grid[j][i].value;
        grid[j][i].value = value;
      }
    }
    return grid;
  }

  public static Cell[][] swapRows(Cell[][] grid) {
    for (int  i = 0, k = grid.length - 1; i < k; ++i, --k) {
      Cell[] x = grid[i];
      grid[i] = grid[k];
      grid[k] = x;
    }    
    return grid;
  }


  public static int[][] copyGrid_values(Cell[][] grid, int[][] old_grid) {
    old_grid = new int[grid.length][grid[0].length];
    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[i].length; j++) {
        old_grid[i][j] = grid[i][j].value;
      }
    }
    return old_grid;
  }

  public static boolean grid_changed(Cell[][] grid, int [][] old_grid) {
    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[i].length; j++) {
        if (old_grid[i][j] != grid[i][j].value) {
          return true;
        }
      }
    }

    return false;
  }



  public static boolean win_game(Cell[][] grid, int max_block) {
    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j<grid[i].length; j++) {
        if (grid[i][j].value == max_block) {
          return true;
        }
      }
    }
    return false;
  }


  public static boolean game_over(Cell[][] grid) {
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        if (grid[i][j].value == 0) {
          return false;
        }
        if (i != 3 && grid[i][j].value == grid[i + 1][j].value) {
          return false;
        }
        if (j != 3 && grid[i][j].value == grid[i][j + 1].value) {
          return false;
        }
      }
    }
    return true;
  }
}


class Menu {  

  public String you_lost_menu() {
    String[] possibilities = {"New Game", "Exit"};

    String choice = (String)JOptionPane.showInputDialog(
      frame, 
      "Start new game or exit.", 
      "YOU LOST", 
      JOptionPane.WARNING_MESSAGE, 
      null, 
      possibilities, possibilities[0]);
    return choice;
  }


  public String you_win() {
    String[] possibilities = {"New Game", "Continue", "Exit"};

    String choice = (String)JOptionPane.showInputDialog(
      frame, 
      "Start new game or Continiue or  exit.", 
      "YOU WON", 
      JOptionPane.WARNING_MESSAGE, 
      null, 
      possibilities, possibilities[0]);
    return choice;
  }
}
static class Row_Manipulator { 
  public static Cell[] shift_row_left(Cell[] row) {
    ArrayList<Cell> not_zeros = new ArrayList<Cell>();
    for (int i = 0; i < row.length; i++) {
      if (row[i].value > 0) {
        not_zeros.add(row[i]);
      }
    }
    for (int i = 0; i < not_zeros.size(); i++) {
      row[i].value =  not_zeros.get(i).value;
    }
    for (int i = not_zeros.size(); i < row.length; i++) {
      row[i].value = 0;
    }
    return row;
  }

  public static Cell[] shift_row_right(Cell[] row) {
    ArrayList<Cell> not_zeros = new ArrayList<Cell>();
    for (int i = 0; i < row.length; i++) {
      if (row[i].value > 0) {
        not_zeros.add(row[i]);
      }
    }
    int j = 0;
    int size = not_zeros.size();
    int diff = row.length - size;
    int[] values = new int[size];

    //for(int i = 0 ;i< size;i++){ // this for loop is silly but when in the below for i get the values from the arrylist the arraylist is changed
    //  values[i] = not_zeros.get(i).value;
    //}
    values = Row_Manipulator.get_values(not_zeros.toArray(new Cell[size]));
    for (int i = diff; i<row.length; i++) {
      row[i].value = values[j];
      j+=1;
    }
    for (int i =0; i<diff; i++) {
      row[i].value = 0;
    }

    return row;
  }

  public static Cell[] colapse(String direction, Cell row[], Cell score) {
    if (direction.equalsIgnoreCase("RIGHT")) {
      for (int i = row.length - 1; i >= 1; i--) {
        Cell a = row[i];
        Cell b = row[i - 1];
        if (a.value == b.value) {
          row[i].value = a.value + b.value;
          score.value += row[i].value;
          row[i - 1].value = 0;
        }
      }
    } else if (direction.equalsIgnoreCase("LEFT")) {
      for (int i = 0; i <row.length - 1; i++) {
        Cell a = row[i];
        Cell b = row[i + 1];
        if (a.value == b.value) {
          row[i].value = a.value + b.value;
          score.value += row[i].value;
          row[i+1].value = 0;
        }
      }
    }
    return row;
  }


  public static int[] get_values(Cell[] row) {
    int[] values = new int[row.length];
    for (int i = 0; i < row.length; i++) {
      values[i] = row[i].value;
    }
    return values;
  }
}
class Score_Cell extends Cell {
  int x_dimension;
  Score_Cell(int y, int x, int width, int x_dimension) {
    super(y, x, width);
    this.x_dimension = x_dimension;
  }

  public void display() {
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
  public void settings() {  size(604, 754); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "sketch_2048" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
