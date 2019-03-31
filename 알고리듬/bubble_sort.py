"""
bubble sort를 재귀함수로 구현한다.

버블 정렬은 2중 루프를 보통 쓰는데
bubble_sort2함수를 구현후 반복문이 하나 남아서 어떻게 없앨지 고민을 많이 했다....
"""

def bubble_sort2(nlist):  # nlist의 가장 큰 값을 제일 뒤로 보내는 재귀함수
    if len(nlist) ==1: #nlist 길이가 1이면 재귀 탈출
        return nlist
    else:
        if nlist[0] > nlist[1]: #리스트의 0,1번째 요소를 비교후 swap시켜준다.
            nlist[0], nlist[1] = nlist[1], nlist[0]
        return [nlist.pop(0)] + bubble_sort2(nlist) #제일앞에 값은 이미 크기비교를 했으므로 빼고 다시 재귀함수를 돌림

def bubble_sort(nlist): # nlist를 n번만큼 bubble_sort2를 시키는 재귀함수
    if len(nlist) == 1: #nlist 길이가 1이면 재귀 탈출
        return nlist
    else:
        nlist = bubble_sort2(nlist)
        tmp = nlist.pop(-1)
        return bubble_sort(nlist) + [tmp] # bubble_sort2를 시켜서 제일 마지막값이 가장 크므로 빼고 다시 재귀함수를 돌림

numlist = list(map(int,input("수를 입력하세요").split())) #n개의 수를 입력받아서 정수형 list형태로 저장한다.
numlist = bubble_sort(numlist)
print(numlist)
