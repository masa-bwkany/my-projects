from board import Board
import helper
from car import *
import sys
class Game:
    """
    Add a class description here.
    Write briefly about the purpose of the class.
    """

    def __init__(self, board: Board) -> None:
        """
        Initialize a new Game object.
        :param board: An object of type board
        """
        # You may assume board follows the API
        # implement your code and erase the "pass"
        self.board = board
        self.legal_moves = {"u", "l", "d", "r"}
        self.legal_colors = set((['Y', 'B', 'O', 'G', 'W', 'R']))

    def __single_turn(self):
        """
        Note - this function is here to guide you and it is *not mandatory*
        to implement it. 

        The function runs one round of the game :
            1. Get user's input of: what color car to move, and what 
                direction to move it.
            2. Check if the input is valid.
            3. Try moving car according to user's input.

        Before and after every stage of a turn, you may print additional 
        information for the user, e.g., printing the board. In particular,
        you may support additional features, (e.g., hints) as long as they
        don't interfere with the API.
        """
        # implement your code and erase the "pass"
        Input = input("Enter car color and direction:").strip()
        if Input == '!':
            print("Game ended by the player.")
            return False

        if len(Input) != 3 or Input[1] != ',':
            print("Invalid input.")
            return True

        color, direc = Input.split(',')
        if color not in self.legal_colors or direc not in self.legal_moves:
            print("Invalid car color or direction.")
            return True

        if not self.board.move_car(color, direc):
            print("Move not possible.")
            return True

        return True

    def play(self) -> None:
        """
        The main driver of the Game. Manages the game until completion.
        :return: None
        """
        # implement your code and erase the "pass"
        tar = self.board.target_location()
        while True:
            print(self.board)
            if self.board.cell_content(tar) is not None:
                print("Congratulations!")
                break
            if not self.__single_turn():
                break

def help(board):
    try:
        data = helper.load_json(sys.argv[1])
        for n, d in data.items():
            length, location, orientation = d
            if n in ['Y', 'B', 'O', 'G', 'W', 'R'] and orientation in [0, 1] and length in [2, 3, 4]:
                car = Car(n, length, location, orientation)
                if board.add_car(car):
                    print(f"Car {n} added successfully.")
                else:
                    print(f"Failed to add car {n}.")
    except ValueError:
        return "Value Error"


if __name__== "__main__":
    # Your code here
    # All access to files, non API constructors, and such must be in this
    # section, or in functions called from this section.
    # implement your code and erase the "pass"
    board = Board()
    help(board)
    game = Game(board)
    game.play()
