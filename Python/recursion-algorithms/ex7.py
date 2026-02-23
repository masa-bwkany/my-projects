
from ex7_helper import *
from typing import *
# sys.setrecursionlimit(5000)
def mult(x: N, y: int) -> N: # Multiplies two numbers x and y using recursive addition
    if y==0:
        return 0
    return add(x,mult(x,subtract_1(y)))
# print(mult(3,4))

def is_even(n: int) -> bool:# Checks if a number n is even using recursion

    if n==0:
        return True
    if n==1:
        return False
    return is_even(subtract_1(subtract_1(n)))
# print(is_even(807))
def log_mult(x: N, y: int) -> N: # Multiplies two numbers x and y using a logarithmic approach and recursion

    if y==0:
        return 0
    if is_odd(y):
        return add(x,log_mult(add(x,x),divide_by_2(y)))
    else:
        return log_mult(add(x,x),divide_by_2(y))
# print(log_mult(5,5))
def subtract(x,y): # Subtracts y from x using recursive subtraction
    if y==0:
        return x
    return subtract(subtract_1(x),subtract_1(y))
def divide_by_b(x,b):# Divides x by b using recursive subtraction and addition
    if x<b:
        return 0
    return add(1,divide_by_b(subtract(x,b),b))
# print(divide_by_b(21,3))

def is_power(b: int, x: int) -> bool:# Checks if x is a power of b using recursive division

    if x==1:
        return True
    if x == 0:
        return b == 0 or b == 1
    if x<b:
        return False
    if subtract(x,mult(b,divide_by_b(x,b)))!=0:
        return False
    return is_power(b,divide_by_b(x,b))
# print(is_power(0,0))
# print(is_power(7,73999993))
def dell_first(s):# Deletes the first character of a string s

    if len(s)==1:
        return ""
    return helper(s,1,"")
def helper(s,i,t):# Helper function to delete the first character of a string s

    if i ==len(s):
        return t
    return helper(s,add(i,1),append_to_end(t,s[i]))
# Reverses a string s using recursion

def reverse(s: str) -> str: #################################
    if len(s)==0:
        return s
    res=dell_first(s)
    return append_to_end(reverse(res),s[0])

# print(reverse(""))

# Solves the Tower of Hanoi problem using recursion
def play_hanoi(Hanoi:Any, n: int, src:Any, dest:Any, temp:Any):
    if n<=0:
        return
    elif n==1:
        Hanoi.move(src,dest)
    else:
        play_hanoi(Hanoi,n-1,src,temp,dest)
        Hanoi.move(src,dest)
        play_hanoi(Hanoi,n-1,temp,dest,src)


# print(number_of_onse(13))
def help(n):# Helper function to count the number of 1s in a number n

    if n==0:
        return 0
    if n%10==1:
        curr_one=1
    else:
        curr_one=0
    return curr_one+help(n//10)

def number_of_ones(n: int) -> int:# Counts the number of 1s in all numbers from 0 to n using recursion

    if n==0:
        return 0
    if n<0:
        n=-n
    return help(n)+number_of_ones(n-1)
def del_help(lst,i,lst2):# Helper function to delete the first element of a list lst

    if i==len(lst):
        return lst2
    lst2.append(lst[i])
    return del_help(lst,add(i,1),lst2)

def del_first(lst):# Deletes the first element of a list lst

    if len(lst)==0:
        return lst
    if len(lst)==1:
        return []
    return del_help(lst,1,[])

# Compares two 2D lists l1 and l2 for equality
def compare_2d_lists(l1: List[List[int]], l2: List[List[int]]) -> bool: ###############

    if not l1 and not l2:
        return True
    if not l1 or not l2:
        return False
    if l1[0] != l2[0]:
        return False
    if isinstance(l1[0], list) and isinstance(l2[0], list):
        if len(l1[0]) != len(l2[0]):
            return False
        if not compare_2d_lists(l1[0], l2[0]):
            return False

    return compare_2d_lists(del_first(l1), del_first(l2))
list3 = [1, [2, 3], 4]
list4 = [1, [2, 3], 4]
l1=[1,2]
l2=[1,2]
print(compare_2d_lists(l1,l2))
print(compare_2d_lists(list3,list4))
print(compare_2d_lists([[1,2],[4,5,6]],[[1,2],[4,5,8]]))


def deep_copy (lst,index,lst2):# Helper function to create a deep copy of a list lst
    if index==len(lst):
        return lst2
    lst2.append(lst[index])
    return deep_copy(lst,add(index,1),lst2)
def deep_help(lst):# Creates a deep copy of a list lst
    return deep_copy(lst,0,[])
def magic_list(n: int) -> List[Any]:# Creates a magic list of depth n using recursion
    if n==0:
        return []
    else:
        prev=magic_list(n-1)
        new_lst=deep_help(prev)
        prev.append(new_lst)
        return prev
# print(magic_list(3))
