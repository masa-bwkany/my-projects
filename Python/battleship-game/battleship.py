# import streamz
import helper

def init_board(rows, columns):
    # return the initial board
    board = []
    for i in range(rows):
        board.append([])
        for j in range(columns):
            board[i].append(helper.WATER)
    return board


def comp_cell_loc(name):
    # return if the input cell loc are valid
    if len(name) >= 2 and name[0].isalpha():
        if name[1:].isnumeric():
            return True
    return False


def cell_loc(name):
    # return if the input cell loc are valid
    if helper.is_int(name[1:]):
        RowNum = int(name[1:]) - 1
        ColNum = ord(name[0].upper()) - 65
        return RowNum, ColNum
    else:
        return 200, 1


def valid_ship(board, size, loc):
    # return if the ships are valid to add to the board
    if size > len(board):
        return False
    if size > (len(board) - loc[0]):
        return False
    if loc[0] > len(board) - 1 or loc[1] > len(board[0]) - 1:
        return False
    row = loc[0]
    col = loc[1]
    for i in range(size):
        if board[row][col] == helper.SHIP:
            return False
        row += 1
    return True



def create_player_board(rows, columns, ship_sizes):
    # return the player board
    player_board = init_board(rows, columns)
    if ship_sizes == ():
        return player_board
    for i in range(len(ship_sizes)):
        helper.print_board(player_board)
        place = cell_loc(helper.get_input("Enter top coordinate for ship of size" + str(ship_sizes[i]) + ": "))
        while not valid_ship(player_board, ship_sizes[i], place):
            print("not a valid location")
            place = cell_loc(helper.get_input("Enter top coordinate for ship of size" + str(ship_sizes[i]) + ": "))
        row = place[0]
        col = place[1]
        for j in range(ship_sizes[i]):
            player_board[row][col] = helper.SHIP
            row += 1
    return player_board


def fire_torpedo(board, loc):
    # check if the bomb was in the water or on the ship
    if loc[0] > len(board) - 1 or loc[1] > len(board[0]) - 1:
        return board
    if board[loc[0]][loc[1]] == helper.WATER:
        board[loc[0]][loc[1]] = helper.HIT_WATER
    if board[loc[0]][loc[1]] == helper.SHIP:
        board[loc[0]][loc[1]] = helper.HIT_SHIP
    return board

def comp_valid_loc(board, size):
    # return if the location are valid to add
    comp_board = []
    for i in range(helper.NUM_ROWS):
        for j in range(helper.NUM_COLUMNS):
            loc = (i, j)
            if valid_ship(board, size, loc) == True:
                comp_board.append(loc)
    return comp_board


def comp_add_ship(board, loc, size):
    # add the shipes to the computer board
    row = loc[0]
    col = loc[1]
    for i in range(size):
        board[row][col] = helper.SHIP
        row+=1
    return board


def comp_board():
    # return the computer board
    CompBoard = init_board(helper.NUM_ROWS, helper.NUM_COLUMNS)
    for i in helper.SHIP_SIZES:
        valid_cell = comp_valid_loc(CompBoard, i)
        if len(valid_cell) > 0:
            new_cell = helper.choose_ship_location(CompBoard, i, set(valid_cell))
            comp_add_ship(CompBoard, new_cell, i)
    return CompBoard


def existing_in_board(loc):
    #check if the loc are in board
    row = loc[0]
    col = loc[1]
    if (row < helper.NUM_ROWS and row >= 0) and (col < (helper.NUM_COLUMNS) and col >= 0):
        return True
    return False


def player_turn(player_board, CompBoard, hidden_board):
    # now is the player turn
    tar = True
    while tar == True:
        cell = helper.get_input("choose place: ")
        if comp_cell_loc(cell) == False:
            print("not a valid place")
            continue
        loc = cell_loc(cell)
        row = loc[0]
        col = loc[1]
        if existing_in_board(loc) == False:
            print("not a valid place")
            continue
        tar = False
        if CompBoard[row][col] == helper.SHIP:
            hidden_board[row][col] = helper.HIT_SHIP
        if CompBoard[row][col] == helper.WATER:
            hidden_board[row][col] = helper.HIT_WATER
        fire_torpedo(CompBoard, loc)

    return player_board, CompBoard, hidden_board


def valid_cells(player_board):
    # return the valid cells for the player
    cell = []
    for i in range(helper.NUM_ROWS):
        for j in range(helper.NUM_COLUMNS):
            loc = (i, j)
            if player_board[i][j] != helper.HIT_WATER and player_board[i][j] != helper.HIT_SHIP:
                cell.append(loc)
    return cell


def comp_turn(board2, hidden_board_p):
    # noww is the computer turn
    loc = valid_cells(board2)
    targ = helper.choose_torpedo_target(hidden_board_p, set(loc))
    if board2[targ[0]][targ[1]] == helper.SHIP:
        hidden_board_p[targ[0]][targ[1]] = helper.HIT_SHIP
    else:
        hidden_board_p[targ[0]][targ[1]] = helper.HIT_WATER
    fire_torpedo(board2, targ)

    return board2, hidden_board_p


def end(board):
    # return if the game is over
    for i in range(helper.NUM_ROWS):
        for j in range(helper.NUM_COLUMNS):
            if board[i][j] == helper.SHIP:
                return False
    return True


def game():
    # play the game
    if len(helper.SHIP_SIZES) == 0:
        return
    player_board = create_player_board(helper.NUM_ROWS, helper.NUM_COLUMNS, helper.SHIP_SIZES)
    hidden_board = init_board(helper.NUM_ROWS, helper.NUM_COLUMNS)
    hidden_board_p = init_board(helper.NUM_ROWS, helper.NUM_COLUMNS)
    computer_board = comp_board()
    loser = end(computer_board)
    comp_lose = end(computer_board)
    while loser == False and comp_lose == False:
        helper.print_board(player_board, hidden_board)
        player_board, computer_board, hidden_board = player_turn(player_board, computer_board, hidden_board)
        player_board, hidden_board_p = comp_turn(player_board, hidden_board_p)
        loser = end(player_board)
        comp_lose = end(computer_board)
    helper.print_board(player_board, computer_board)

    if loser == True and comp_lose == True:
        return "!!"
    elif loser == True:
        return "the winner is computer"
    elif comp_lose == True:
        return "the winner is player"

def main():
    # the game
    res = game()
    if res is None:
        return
    char = ""
    while char != "N":
        char = helper.get_input(res + "play again")
        if char == "Y":
            res = game()
        elif char == "N":
            return
        else:
            print("invalid char")




if __name__ == "__main__":
    main()
