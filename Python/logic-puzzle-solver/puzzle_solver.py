from typing import List, Tuple, Set, Optional

# We define the types of a partial picture and a constraint (for type checking).
Picture = List[List[int]]
Constraint = Tuple[int, int, int]


def max_seen_cells(picture: Picture, row: int, col: int) -> int:
    if picture[row][col] ==0:
        return 0
    cnt = 1
    for i in range(col - 1, -1, -1):
        if picture[row][i] == 0 :
            break
        cnt += 1
    for i in range(col + 1, len(picture[row])):
        if picture[row][i] == 0:
            break
        cnt += 1
    for i in range(row - 1, -1, -1):
        if picture[i][col] == 0 :
            break
        cnt += 1
    for i in range(row + 1, len(picture)):
        if picture[i][col] == 0 :
            break
        cnt += 1
    return cnt

def min_seen_cells(picture: Picture, row: int, col: int) -> int:
    if picture[row][col] in (0, -1):
        return 0
    cnt = 1
    for i in range(col - 1, -1, -1):
        if picture[row][i] == 0 or picture[row][i] == -1:
            break
        cnt += 1
    for i in range(col + 1, len(picture[row])):
        if picture[row][i] == 0 or picture[row][i] == -1:
            break
        cnt += 1
    for i in range(row - 1, -1, -1):
        if picture[i][col] == 0 or picture[i][col] == -1:
            break
        cnt += 1
    for i in range(row + 1, len(picture)):
        if picture[i][col] == 0 or picture[i][col] == -1:
            break
        cnt += 1
    return cnt

def valid(picture,constraint):
    i,j,w=constraint
    min_val=min_seen_cells(picture,i,j)
    max_val=max_seen_cells(picture,i,j)
    if max_val==min_val==w:
        return 1
    elif min_val<=w<=max_val:
        return 2
    return 0

def check_constraints(picture: Picture, constraints_set: Set[Constraint]) -> int:
    fin_res=1
    for c in constraints_set:
        res=valid(picture,c)
        if res==0:
            return 0
        elif res==2:
            fin_res= 2
    return fin_res


def solve_puzzle(constraints_set: Set[Constraint], n: int, m: int) -> Optional[Picture]:
    def help(pic, row, col):
        if row == n:
            if check_constraints(pic, constraints_set) == 1:
                return pic
            else:
                return None
        new_row, new_col = (row + 1, 0) if col == m - 1 else (row, col + 1)
        for i in (1, 0):
            picture[row][col] = i
            if check_constraints(pic, constraints_set) != 0:
                result = help(pic, new_row, new_col)
                if result:
                    return result
        picture[row][col] = -1
        return None
    picture = [[-1 for _ in range(m)] for _ in range(n)]
    return help(picture, 0, 0)


def how_many_solutions(constraints_set: Set[Constraint], n: int, m: int) -> int:
    def count_solutions(pic, row, col) :
        if row == n:
            return 1 if check_constraints(pic, constraints_set) == 1 else 0

        new_row, new_col = (row, col + 1) if col + 1 < m else (row + 1, 0)
        cnt = 0

        for i in (0, 1):
            picture[row][col] = i
            if check_constraints(pic, constraints_set) != 0:
                cnt += count_solutions(pic, new_row, new_col)

        pic[row][col] = -1
        return cnt

    picture = [[-1 for _ in range(m)] for _ in range(n)]
    return count_solutions(picture, 0, 0)

def generate_puzzle(picture: Picture) -> Set[Constraint]:
    con_set=set()
    for i in range(len(picture)):
        for j in range(len(picture[0])):
            seen=min_seen_cells(picture,i,j)
            con_set.add((i,j,seen))
    def helper(const):
        return how_many_solutions(const,len(picture),len(picture[0]))<=1
    for i in range(len(picture)):
        for j in range(len(picture[0])):
            seen=min_seen_cells(picture,i,j)
            const=(i,j,seen)
            con_set.remove(const)
            if not helper(con_set):
                con_set.add(const)

    return con_set

# print(generate_puzzle([[1,0,0],[1,1,1]]))
