static class Row_Manipulator { 
  static Cell[] shift_row_left(Cell[] row) {
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

  static Cell[] shift_row_right(Cell[] row) {
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

  static Cell[] colapse(String direction, Cell row[], Cell score) {
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


  static int[] get_values(Cell[] row) {
    int[] values = new int[row.length];
    for (int i = 0; i < row.length; i++) {
      values[i] = row[i].value;
    }
    return values;
  }
}
