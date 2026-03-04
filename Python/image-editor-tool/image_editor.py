#################################################################
# FILE : image_editor.py
# WRITER : masa bwakny , massa.bwakny , 213852759
# EXERCISE : intro2cs ex6 2024
# DESCRIPTION: A simple program that...
# STUDENTS I DISCUSSED THE EXERCISE WITH: Bugs Bunny, b_bunny.
#								 	    Daffy Duck, duck_daffy.
# WEB PAGES I USED: www.looneytunes.com/lola_bunny
# NOTES: ...
#################################################################

##############################################################################
#                                   Imports                                  #
##############################################################################
from ex6_helper import *
from typing import Optional


##############################################################################
#                                  Functions                                 #
##############################################################################

def separate_channels(image: ColoredImage) -> List[SingleChannelImage]:
    #this function separates the RGB channel to 3 different list. each list contains the numbers for the specified list(red/ green/ blue)
    res=[]
    n= len(image[0][0])
    if n>=1:
        red = [[color[0] for color in row] for row in image]
        res.append(red)
    if n>=2:
        green = [[color[1] for color in row] for row in image]
        res.append(green)
    if n==3:
        blue = [[color[2] for color in row] for row in image]
        res.append(blue)
    return res

def combine_channels(channels: List[SingleChannelImage]) -> ColoredImage:
    ##this function combines the lists
    len_channels = len(channels)
    image = [[[channels[n][i][j] for n in range(len_channels)] for j in range(len(channels[0][0]))] for i in
             range(len(channels[0]))]
    return image



def RGB2grayscale(colored_image: ColoredImage) -> SingleChannelImage:
    #this function recieves a colored image and returns it in grayscale
    WB_image = [[(round(image[0] * 0.299 + image[1] * 0.587 + image[2] * 0.114)) for image in mat] for mat in
                colored_image]
    return WB_image

def blur_kernel(size: int) -> Kernel:
    #this function returns a list of kernels that can be applied in apply kernel
    ker_image = [[1 / (size ** 2) for i in range(size)] for j in range(size)]
    return ker_image

def help(value, min_value=0, max_value=255):
    return max(min_value, min(value, max_value))

def value(image, x, y, i, j):
    if 0 <= x < len(image) and 0 <= y < len(image[0]):
        return image[x][y]
    else:
        return image[i][j]
def apply_kernel(image: SingleChannelImage, kernel: Kernel) -> SingleChannelImage:
    #blurs image
    n = len(kernel) // 2
    height = len(image)
    width = len(image[0])
    result_image = []

    for i in range(height):
        row_result = []

        for j in range(width):
            sum = 0

            for k in range(len(kernel)):
                for m in range(len(kernel[0])):
                    x = i + k - n
                    y = j + m - n
                    pixel_value = value(image, x, y, i, j)
                    sum += pixel_value * kernel[k][m]

            pixel = help(round(sum))
            row_result.append(pixel)

        result_image.append(row_result)

    return result_image
def bilinear_interpolation(image: SingleChannelImage, y: float, x: float) -> int:
    #bilinear interpolation
    row = len(image)
    col = len(image[0])
    x1 = int(x)
    x2 = min(x1 + 1, col - 1)
    y1 = int(y)
    y2 = min(y1 + 1, row - 1)
    a = image[y1][x1]
    b = image[y2][x1]
    c = image[y1][x2]
    d = image[y2][x2]
    Delta_x = x - x1
    Delta_y = y - y1
    Eq1 = (1 - Delta_x) * a * (1 - Delta_y)
    Eq2 = (Delta_y) * b * (1 - Delta_x)
    Eq3 = (Delta_x) * c * (1 - Delta_y)
    Eq4 = (Delta_y) * (Delta_x) * d
    res= Eq1+Eq2+Eq3+Eq4
    return int(round(res))


def resize(image: SingleChannelImage, new_height: int, new_width: int) -> SingleChannelImage:
    #resizes the given grayscale image
    new_image = [[0 for i in range(new_width)] for j in range(new_height)]
    new_image[0][0] = image[0][0]
    new_image[new_height-1][0]=image[len(image)-1][0]
    new_image[0][new_width-1] = image[0][len(image[0])-1]
    new_image[new_height-1][new_width - 1] = image[len(image)-1][len(image[0])-1]

    for i in range(new_height):
        for j in range(new_width):
            if (i==0 and j==0)or (i==new_height-1 and j==0) or (i==0 and j==new_width-1) or (i==new_height-1 and j==new_width-1):
                continue
            else:
                new_i = (len(image) - 1) * i / (new_height - 1)
                new_j = (len(image[0]) - 1) * j / (new_width - 1)
                new_image[i][j] = bilinear_interpolation(image, new_i, new_j)

    return new_image

def rotate_90(image: Image, direction: str) -> Image:
    #rotates said image in 90 degrees (right or left)
    if direction == "R":
        return [list(reversed(i)) for i in zip(*image)]
    if direction == "L":
        return [list(i) for i in zip(*image)][::-1]

def get_edges(image: SingleChannelImage, blur_size: int, block_size: int, c: float) -> SingleChannelImage:
    #shows the absolute image(the edges which are fully black 0)
    blurred_image = apply_kernel(image, blur_kernel(blur_size))
    r = block_size // 2
    BWImg = [[0 for i in range(len(image[0]))] for j in range(len(image))]
    for i in range(len(image)):
        for j in range(len(image[0])):
            res = 0
            cnt = 0
            for k in range(-r, r + 1):
                for m in range(-r, r + 1):
                    x = i + k
                    y = j + m
                    res += value(blurred_image, x, y, i, j)
                    cnt += 1
            avg = res / cnt
            threshold = avg - c
            if blurred_image[i][j] < threshold:
                BWImg[i][j] = 0
            else:
                BWImg[i][j] = 255

    return BWImg

import math
def quantize(image: SingleChannelImage, N: int) -> SingleChannelImage:
    #quantizes the image with said dimentions (grayscale)
    new_image = [[0 for i in range(len(image[0]))] for j in range(len(image))]
    for i in range(len(image)):
        for j in range(len(image[0])):
            new_image[i][j] = round(math.floor((image[i][j]) * (N /256)) * (255 / (N - 1)))
    return new_image



def quantize_colored_image(image: ColoredImage, N: int) -> ColoredImage:
    # quantizes the image with said dimentions (RGB image)
    channel = separate_channels(image)
    new_channel = [quantize(i, N) for i in channel]
    return combine_channels(new_channel)
import sys
def choose_1(image):
    #option 1 which turns the image to grayscale
    if type(image[0][0]) == int:
        print("the image is already in Black and White")
    else:
        image = RGB2grayscale(image)
        print("the image in Black and white now!")
    return image
def choose_2(image):
    #blurs said image
    kernel_size = input("choose the kernel size")
    kernel_size = eval(kernel_size)
    if type(kernel_size) != int or kernel_size % 2 != 1 or kernel_size <= 0:

        print("the input is not valid")
    else:
        if isinstance(image[0][0], list):
            separate_image = separate_channels(image)
            blur_channel = [apply_kernel(chan, blur_kernel(kernel_size)) for chan in separate_image]
            image = combine_channels(blur_channel)
            print("the image is blurred now!")
        else:
            image = apply_kernel(image, blur_kernel(kernel_size))
            print("the image is blurred now!")
    return image
def choose_3(image):
    #resizes said image
    inp = input("write the height and the width")
    new_inp = inp.split(",")
    height = eval(new_inp[0])
    width = eval(new_inp[1])
    if type(height) != int or type(width) != int:
        print("not a valid input")
    else:
        if isinstance(image[0][0],int):
            image = resize(image, height, width)
            print("resized image")
        else:
            sep_img=separate_channels(image)
            for w in range(len(sep_img)):
                sep_img[w]=resize(sep_img[w],height,width)
            image=combine_channels(sep_img)
            print("resized image")
    return image
def choose_4(image):
    #rotates the image
    choosen_dire = input("write the direction")
    if choosen_dire != "R" or choosen_dire != "L":
        print("not a valid input")
    else:
        image = rotate_90(image, choosen_dire)
        print("rotated image")
    return image


def choose_5(image):
    #shows the edges of an image
    inp = input("choose the parameters")
    new_inp = inp.split(",")
    if len(new_inp) != 3:
        print("not a valid input")
    blur_size = eval(new_inp[0])
    block_size = eval(new_inp[1])
    c = eval(new_inp[2])
    if type(block_size) != int or type(blur_size) != int or blur_size % 2 != 1 or block_size % 2 != 1 or c <= 0 or block_size <= 0 or blur_size <= 0:
        print("not a valid input")
    if isinstance(image[0][0], list):
        image = RGB2grayscale(image)
        image = get_edges(image, blur_size, block_size, c)
        print("edges image")
    else:
        image = get_edges(image, blur_size, block_size,c)
    return image
def choose_6(image):
    #bilinear interpolation
    N = input("write the num")
    if type(eval(N)) != int or eval(N) <= 1:
        print("not a valid input")
    else:
        if isinstance(image[0][0], list):
            image = quantize_colored_image(image, eval(N))
        else:
            image = quantize(image, eval(N))
    return image

def main():
    #main
    if len(sys.argv)!=2:
        print("not a valid input")
        sys.exit()
    image_path=sys.argv[1]
    image=load_image(image_path)
    while True:
        print("choose number")
        print("1.convert to BlackWhite image")
        print("2.blur the image")
        print("3.change the size of the image")
        print("4.rotate the image 90 degree")
        print("5.get edges")
        print("6.quantize the image")
        print("7.show the image")
        print("8.exit from the program")
        choosen_num=input("enter the choosen number")
        if choosen_num not in ["1","2","3","4","5","6","7","8"]:
            print("not a valid input")
        if choosen_num=="1":
            image= choose_1(image)
        if choosen_num=="2":
            image=choose_2(image)
        if choosen_num=="3":
            image=choose_3(image)
        if choosen_num=="4":
            image=choose_4(image)
        if choosen_num=="5":
            image=choose_5(image)
        if choosen_num=="6":
            image=choose_6(image)
        if choosen_num=="7":
            show_image(image)
        if choosen_num=="8":
            save_file=input("choose the file").strip()
            save_image(image,save_file)
            print("image saved")
            break


if __name__ == '__main__':
    main()