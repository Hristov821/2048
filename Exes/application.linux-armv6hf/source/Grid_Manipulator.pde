static class Grid_Manipulator {
  static Cell[][] transpose(Cell[][] grid) {
    for (int i = 0; i <grid.length; i++) {
      for (int j = i; j <grid[0].length; j++) {
        int value = grid[i][j].value;
        grid[i][j].value = grid[j][i].value;
        grid[j][i].value = value;
      }
    }
    return grid;
  }

  static Cell[][] swapRows(Cell[][] grid) {
    for (int  i = 0, k = grid.length - 1; i < k; ++i, --k) {
      Cell[] x = grid[i];
      grid[i] = grid[k];
      grid[k] = x;
    }    
    return grid;
  }


  static int[][] copyGrid_values(Cell[][] grid, int[][] old_grid) {
    old_grid = new int[grid.length][grid[0].length];
    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[i].length; j++) {
        old_grid[i][j] = grid[i][j].value;
      }
    }
    return old_grid;
  }

  static boolean grid_changed(Cell[][] grid, int [][] old_grid) {
    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[i].length; j++) {
        if (old_grid[i][j] != grid[i][j].value) {
          return true;
        }
      }
    }

    return false;
  }



  static boolean win_game(Cell[][] grid, int max_block) {
    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j<grid[i].length; j++) {
        if (grid[i][j].value == max_block) {
          return true;
        }
      }
    }
    return false;
  }


  static boolean game_over(Cell[][] grid) {
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
