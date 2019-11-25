def canPlaceFlowers(flowerbed):
    nums = [0] + flowerbed + [0]
    print flowerbed
    print nums
    i = 1
    count = 0
    while i < len(flowerbed)+1:
        if nums[i-1] == 0 and nums[i] == 0 and nums[i+1] == 0:
            count += 1
            i += 2
        else:
            i += 1
    print count
    return count
arr = list(map(int,raw_input().split(',')))
sum = 0
print arr
if arr[0] == 2:
    arr[0]=1
    arr[1]=1
if arr[-1] == 2:
    arr[-1]=1
    arr[-2]=1
for i in range(1,len(arr)-1):
    if arr[i] == 2:
        arr[i-1]=1
        arr[i]=1
        arr[i+1]=1
count = canPlaceFlowers(arr)
print count
