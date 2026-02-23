from typing import Tuple, List, Dict

Coordinates = Tuple[int, int]

class Car:
    """
    Add a class description here.
    Write briefly about the purpose of the class.
    """
    def __init__(self, name: str, length: int, location: Coordinates,
                 orientation: int) -> None:
        """
        A constructor for a Car object.
        :param name: A string representing the car's name.
        :param length: A positive int representing the car's length.
        :param location: A tuple representing the car's head location (row,col).
        :param orientation: One of either 0 (VERTICAL) or 1 (HORIZONTAL).
        """
        # Note that this function is required in your Car implementation.
        # implement your code and erase the "pass"
        self.name=name
        self.length=length
        self.location=location
        self.orientation=orientation



    def car_coordinates(self) -> List[Coordinates]:
        """
        :return: A list of coordinates the car is in.
        """
        # implement your code and erase the "pass"
        coordinat=[]
        for i in range(self.length):
            if self.orientation==0:
                coordinat.append((self.location[0]+i,self.location[1]))
            else:
                coordinat.append((self.location[0],self.location[1]+i))
        return coordinat

    def possible_moves(self) -> Dict[str, str]:
        """
        :return: A dictionary of strings describing possible movements
                 permitted by this car.
        """
        # For this car type, keys are from 'udrl'
        # The keys for vertical cars are 'u' and 'd'.
        # The keys for horizontal cars are 'l' and 'r'.
        # You may choose appropriate strings to describe each movements.
        # For example: a car that supports the commands 'f', 'd', 'a' may return
        # the following dictionary:
        # {'f': "cause the car to fly and reach the Moon",
        #  'd': "cause the car to dig and reach the core of Earth",
        #  'a': "another unknown action"}
        #
        # implement your code and erase the "pass"
        if self.orientation==0:
            return {"u":"up","d":"down"}
        else:
            return {"l":"left","r":"right"}


    def movement_requirements(self, move_key: str) -> List[Coordinates]:
        """
        :param move_key: A string representing the key of the required move.
        :return: A list of cell locations which must be empty in order for
                 this move to be legal.
        """
        # For example, a car in locations [(1,2),(2,2)] requires [(3,2)] to
        # be empty in order to move down (with a key 'd').
        # implement your code and erase the "pass"
        req=[]
        if move_key not in self.possible_moves():
            return []
        if self.orientation==0:
            if move_key=="u":
                req.append((self.location[0]-1,self.location[1]))
            elif move_key=="d":
                req.append((self.location[0]+self.length,self.location[1]))
        else:
            if move_key=="l":
                req.append((self.location[0],self.location[1]-1))
            elif move_key=="r":
                req.append((self.location[0],self.location[1]+self.length))
        return req


    def move(self, move_key: str) -> bool:
        """
        This function moves the car.
        :param move_key: A string representing the key of the required move.
        :return: True upon success, False otherwise
        """
        # implement your code and erase the "pass"
        if move_key in self.possible_moves():
            if move_key=="u":
                self.location=(self.location[0]-1,self.location[1])
            elif move_key=="d":
                self.location=(self.location[0]+1,self.location[1])
            elif move_key=="l":
                self.location=(self.location[0],self.location[1]-1)
            elif move_key=="r":
                self.location=(self.location[0],self.location[1]+1)
            return True
        return False



    def get_name(self) -> str:
        """
        :return: The name of this car.
        """
        # implement your code and erase the "pass"
        return self.name
