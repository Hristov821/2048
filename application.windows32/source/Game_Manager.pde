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

  void spawn() {
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
      int seed = int(random(free_spots_x.size()));
      int chance = int(random(10));
      if (chance > 1) {
        grid[free_spots_x.get(seed)][free_spots_y.get(seed)].value = 2;
      } else {
        grid[free_spots_x.get(seed)][free_spots_y.get(seed)].value = 4;
      }
    }
  }

  void move_left() {
    for (int i = 0; i < gm.grid.length; i++) {
      grid[i] = Row_Manipulator.shift_row_left(grid[i]);
      grid[i] = Row_Manipulator.colapse("LEFT", grid[i], score);
      grid[i] = Row_Manipulator.shift_row_left(grid[i]);
    }
  }

  void move_right() {
    for (int i = 0; i < gm.grid.length; i++) {
      grid[i] = Row_Manipulator.shift_row_right(grid[i]);
      grid[i] = Row_Manipulator.colapse("RIGHT", grid[i], score);
      grid[i] = Row_Manipulator.shift_row_right(grid[i]);
    }
  }

  void move_up() {
    grid = Grid_Manipulator.transpose(grid);
    grid = Grid_Manipulator.swapRows(grid);      
    move_left();
    grid = Grid_Manipulator.swapRows(grid);
    grid = Grid_Manipulator.transpose(grid);
  }


  void move_down() {
    grid = Grid_Manipulator.transpose(grid);
    grid = Grid_Manipulator.swapRows(grid);      
    move_right();
    grid = Grid_Manipulator.swapRows(grid);
    grid = Grid_Manipulator.transpose(grid);
  }


  void game_state() {
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
  void initialize_grid() {
    for (int i = 0; i< this.grid.length; i++) {
      for (int j =0; j<this.grid[i].length; j++) {
        this.grid[i][j].value = 0;
      }
    }
  }

  void initialize_grid(int width_of_block) {
    for (int i = 0; i< grid.length; i++) {
      for (int j =0; j<grid[i].length; j++) {
        grid[i][j] = new Cell(i+1, j, width_of_block);
      }
    }
  }

  void display_grid() {
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
