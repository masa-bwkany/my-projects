from typing import Tuple, List, Optional
from car import Car

Coordinates = Tuple[int, int]

class Board:
    """
    Add a class description here.
    Write briefly about the purpose of the class.
    """

    def __init__(self) -> None:
        """
        A constructor for a Board object.
        """
        # Note that this function is required in your Board implementation.
        # implement your code and erase the "pass"
        self.board_size = 7
        self.target_cord = (3, 7)
        self.cars = {}
        self.board = []
        for i in range(7):
            row = []
            for j in range(7):
                row += ["-"]
            row += ["*"]
            self.board.append(row)
        self.board[3][7] = 'E'

    def __str__(self) -> str:
        """
        This function is called when a board object is to be printed.
        :return: A string representing the current status of the board.
        """
        # The game may assume this function returns a reasonable representation
        # of the board for printing, but may not assume details about it.
        # implement your code and erase the "pass"
        board = ""
        for i in range(7):
            for j in range(7):
                board += self.board[i][j] + " "
            board += self.board[i][7] + "\n"
        return board
    def cell_list(self) -> List[Coordinates]:
        """
        This function returns the coordinates of cells in this board.
        :return: list of coordinates.
        """
        # In this board, returns a list containing the cells in the square
        # from (0,0) to (6,6) and the target cell (3,7)
        # implement your code and erase the "pass"
        cell = []
        for i in range(7):
            for j in range(7):
                cell.append((i, j))
        cell.append((3, 7))
        return cell

    def possible_moves(self) -> List[Tuple[str, str, str]]:
        """
        This function returns the legal moves of all cars in this board.
        :return: list of tuples of the form (name, move_key, description)
                 representing legal moves. The description should briefly
                 explain what is the movement represented by move_key.
        """
        # From the provided example car_config.json file, the return value could be
        # [('O','d',"description"), ('R','r',"description"), ('O','u',"description")]
        # implement your code and erase the "pass"
        possible_moves = []
        for c in self.cars:
            car = self.cars[c]
            moves = car.possible_moves()
            for m in moves:
                requ = car.movement_requirements(m)
                if not requ:
                    continue
                legal = all(self.help(req) and self.cell_content(req) is None for req in requ)
                if legal:
                    possible_moves.append((c, m, moves[m]))
        return possible_moves

    def target_location(self) -> Coordinates:
        """
        This function returns the coordinates of the location that should be
        filled for victory.
        :return: (row, col) of the goal location.
        """
        # In this board, returns (3,7)
        # implement your code and erase the "pass"
        return (3,7)

    def cell_content(self, coordinates: Coordinates) -> Optional[str]:
        """
        Checks if the given coordinates are empty.
        :param coordinates: tuple of (row, col) of the coordinates to check.
        :return: The name of the car in "coordinates", None if it's empty.
        """
        # implement your code and erase the "pass"
        if not (0 <= coordinates[0] < self.board_size and 0 <= coordinates[1] <= self.board_size):
            if coordinates != self.target_location():
                return None
        for name, car in self.cars.items():
            if coordinates in car.car_coordinates():
                return name
        return None

    def add_car(self, car: Car) -> bool:
        """
        Adds a car to the game.
        :param car: car object to add.
        :return: True upon success, False if failed.
        """
        # Remember to consider all the reasons adding a car can fail.
        # You may assume the car is a legal car object following the API.
        # implement your code and erase the "pass"
        if car.get_name() in self.cars:
            return False
        coord = car.car_coordinates()
        for c in coord:
            if c[0] < 0 or c[0] >= self.board_size or c[1] < 0 or c[1] >= self.board_size:
                if not (c[1] == self.board_size and c[0] == self.target_location()[0]):
                    return False
            if self.cell_content(c) is not None:
                return False
        for c in coord:
            self.board[c[0]][c[1]] = car.get_name()
        self.cars[car.get_name()] = car
        return True

    def new_board(self):
        for i in range(7):
            for j in range(7):
                self.board[i][j] = "_"
        for c in self.cars:
            car = self.cars[c]
            coord = car.car_coordinates()
            for c in coord:
                self.board[c[0]][c[1]] = car.get_name()
        self.board[3][7]="E"

    def move_car(self, name: str, move_key: str) -> bool:
        """
        Moves car one step in a given direction.
        :param name: name of the car to move.
        :param move_key: the key of the required move.
        :return: True upon success, False otherwise.
        """
        # implement your code and erase the "pass"
        if name not in self.cars:
            return False
        car = self.cars[name]
        requ = car.movement_requirements(move_key)
        if not requ or not all(self.help(req) and self.cell_content(req) is None for req in requ):
            return False
        if not car.move(move_key):
            return False
        self.new_board()
        return True

    def help(self, coordinate: Coordinates) -> bool:
        """
        Checks if the given coordinates are within the bounds of the board.
        :param coordinate: A tuple of (row, col).
        :return: True if within bounds, False otherwise.
        """
        return 0 <= coordinate[0] < self.board_size and 0 <= coordinate[1] <= self.board_size
