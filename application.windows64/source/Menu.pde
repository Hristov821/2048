
import javax.swing.*;
class Menu {  

  String you_lost_menu() {
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


  String you_win() {
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
