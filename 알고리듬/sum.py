"""
n개의 수의 총 합을 재귀함수로 구현한다.
"""

def sum(nlist): #n개의 수를 합해주는 재귀 함수
    if len(nlist) == 1: #nlist의 길이가 1이면 재귀 탈출
        return nlist[0]
    return nlist.pop(1) + sum(nlist) #nlist의 첫번째 요소를 빼서 나머지를 재귀 함수로 다시 넣어 합해서 리턴한다.

numlist = list(map(int,input("n개의 수를 입력해주세요").split())) #n개의 수를 입력받아서 정수형 list형태로 저장한다.
print(sum(numlist))
