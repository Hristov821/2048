java.awt.Insets insets;
Game_Manager  gm;
//surface.setSize(23, 23);
int[][] old_grid;
boolean grid_changed = false;
boolean key_released = false;
JSONObject Colours;


void setup()
{
  size(604, 754);
  surface.setTitle("2 0 4 8");
  Colours = loadJSONObject("Colours.json");
  gm = new Game_Manager(4, 4, 150, 2048);
  gm.spawn();
  gm.spawn();
  gm.display_grid();
  old_grid = Grid_Manipulator.copyGrid_values(gm.grid, old_grid);
  noLoop();
}

void draw() {
  gm.display_grid();
}


void keyPressed() {
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

void keyReleased() {
  if (keyCode == LEFT || keyCode == RIGHT || keyCode == UP || keyCode == DOWN) {
    gm.game_state();
  }
}
