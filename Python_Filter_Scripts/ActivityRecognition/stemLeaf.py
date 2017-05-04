import csv
your_list = []
with open('test.csv', 'rb') as f:
    reader = csv.reader(f)
    your_list = list(reader)


output = []
for nums in your_list:
    for leafs in nums[1].strip():
        output.append(nums[0] + leafs)

print output

f = open('RHYS2.csv', 'w')
for nums in output:
    print>> f.write(nums  + "\n")
#
# myfile = open("RHYSOUTPUT.csv", 'wb')
# wr = csv.writer(myfile)
# wr.writerow(output)