"""
selection sort를 재귀함수로 구현한다.
"""

def selection_sort(nlist): #선택정렬 재귀함수
    if len(nlist) != 1:
        tmp = min(nlist) #nlist 중 가장 작은 값을 리스트 에서 빼서 제일 앞에 붙여 리턴한다.
        nlist.remove(tmp)
        return [tmp] + selection_sort(nlist)
    else:   #nlist의 길이가 1 이 되면 재귀를 탈출한다.
        return nlist

numlist = list(map(int,input("수를 입력하세요").split())) #n개의 수를 입력받아서 정수형 list형태로 저장한다.
numlist = selection_sort(numlist)
print(numlist)
