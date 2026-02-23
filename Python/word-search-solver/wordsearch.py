def read_wordlist(filename):
    #this function receives a file of names and returns a list of names
    f=open(filename,'r')
    res=f.readlines()
    for i in range(len(res)):
        res[i]=res[i].strip()
    f.close()
    #print(res)
    return res

def read_matrix(filename):
    #this function receives a file of matrix and returns a matrix
    res=[]
    f=open(filename,'r')
    for i in f.readlines():
        line=[]
        i=i.strip()
        for j in i.split(","):
            line.append(j)
        res.append(line)
    f.close()
    return res

def search_d(matrix,word):
    #search down a matrix and return a list of te words that begin in the same first letter and the same length
    lst=[]
    row, col =len(matrix),len(matrix[0])
   # n, m = char_place(word, matrix)
    for i in range(row):
        for j in range(col):
            res = ""
            if matrix[i][j]==word[0] and len(matrix)- i >= len(word):
                for w in range(len(word)):
                    res+=matrix[i+w][j]
                #print(res)
                lst.append(res)

    return lst
def search_u(matrix,word):
    # search up a matrix and return a list of te words that begin in the same first letter and the same length
    lst=[]
    row, col =len(matrix),len(matrix[0])
    for i in range(row):
        for j in range(col):
            res = ""
            if matrix[i][j]==word[0] and i+1>= len(word):
                for w in range(len(word)):
                    res+=matrix[i-w][j]
                lst.append(res)
            #print(lst)

    return lst

def search_r(matrix,word):
    # search right a matrix and return a list of te words that begin in the same first letter and the same length
    lst=[]
    row, col =len(matrix),len(matrix[0])
    for i in range(row):
        for j in range(col):
            res = ""
            if matrix[i][j]==word[0] and len(matrix[0])-j>=len(word):
                for w in range(len(word)):
                    res+=matrix[i][j+w]
                lst.append(res)

    return lst

def search_l(matrix,word):
    # search left a matrix and return a list of te words that begin in the same first letter and the same length
    lst=[]
    row, col =len(matrix),len(matrix[0])
    for i in range(row):
        for j in range(col):
            res = ""
            if matrix[i][j]==word[0] and len(word) <= j + 1:
                for w in range(len(word)):
                    res+=matrix[i][j-w]
                lst.append(res)

    return lst

def search_w(matrix,word):
    # search up right a matrix and return a list of te words that begin in the same first letter and the same length
    lst=[]
    row, col =len(matrix),len(matrix[0])
    for i in range(row):
        for j in range(col):
            res = ""
            if matrix[i][j]==word[0] and len(matrix[0])-j>=len(word) and i+1>= len(word):
                for w in range(len(word)):
                    res+=matrix[i-w][j+w]
                lst.append(res)
    return lst
def search_x(matrix,word):
    # search up Left a matrix and return a list of te words that begin in the same first letter and the same length
    lst=[]
    row, col =len(matrix),len(matrix[0])
    for i in range(row):
        for j in range(col):
            res = ""
            if matrix[i][j]==word[0] and i+1>= len(word) and len(word) <= j + 1:
                for w in range(len(word)):
                    res+=matrix[i-w][j-w]
                lst.append(res)

    return lst

def search_y(matrix,word):
    # search down right a matrix and return a list of te words that begin in the same first letter and the same length
    lst=[]
    row, col =len(matrix),len(matrix[0])
    for i in range(row):
        for j in range(col):
            res = ""
            if matrix[i][j]==word[0] and len(matrix[0])-j>=len(word) and len(matrix)- i >= len(word):
                for w in range(len(word)):
                    res+=matrix[i+w][j+w]
                lst.append(res)
    return lst

def search_z(matrix,word):
    # search down left a matrix and return a list of te words that begin in the same first letter and the same length
    lst=[]
    row, col =len(matrix),len(matrix[0])
    for i in range(row):
        for j in range(col):
            res = ""
            if matrix[i][j]==word[0] and len(word) <= j + 1 and len(matrix)- i >= len(word):
                for w in range(len(word)):
                    res+=matrix[i+w][j-w]
                lst.append(res)
    return lst

def find_words(word_list,matrix,directions):
    # finds the words from the list in the matrix with the given direction
    found_word={}
    for word in word_list:
        for char in "".join(set(directions)):
            if char=="d":
                res=search_d(matrix,word)
                if word in res:
                    cnt = res.count(word)
                    found_word[word]=found_word.get(word,0)+cnt
            if char=="u":
                res=search_u(matrix,word)
                if word in res:
                    cnt=res.count(word)
                    found_word[word]=found_word.get(word,0)+cnt
            if char=="r":
                res=search_r(matrix,word)
                if word in res:
                    cnt=res.count(word)
                    found_word[word]=found_word.get(word,0)+cnt
            if char=="l":
                res=search_l(matrix,word)
                if word in res:
                    cnt=res.count(word)
                    found_word[word]=found_word.get(word,0)+cnt
            if char=="w":
                res=search_w(matrix,word)
                if word in res:
                    cnt=res.count(word)
                    found_word[word]=found_word.get(word,0)+cnt
            if char=="x":
                res=search_x(matrix,word)
                if word in res:
                    cnt = res.count(word)
                    found_word[word]=found_word.get(word,0)+cnt
            if char=="y":
                res=search_y(matrix,word)
                if word in res:
                    cnt = res.count(word)
                    found_word[word]=found_word.get(word,0)+cnt
            if char=="z":
                res=search_z(matrix,word)
                if word in res:
                    cnt = res.count(word)
                    found_word[word]=found_word.get(word,0)+cnt
    return list(found_word.items())
print(find_words("dog"))

def write_output(results,filename):
    #writes output in a file
    output=open(filename,"a")
    for word,cnt in results:
        output.write(f"{word},{cnt}\n")
    output.close()

def main():
    import sys
    if len(sys.argv) != 5:
        print("not a valid input")
        sys.exit()
    words_file = sys.argv[1]
    mat_file = sys.argv[2]
    output_file = sys.argv[3]
    direct = sys.argv[4]
    try:
        word_list = read_wordlist(words_file)
    except:
        print("this file is not exist")
        sys.exit()
    try:
        mat = read_matrix(mat_file)
    except:
        print("this file is not exist")
        sys.exit()
    for i in direct:
        if i not in "udrlwxyz":
            print("not a valid direction input")
            sys.exit()
    results = find_words(word_list, mat, direct)
    write_output(results, output_file)

if __name__ == "__main__":
    main()
