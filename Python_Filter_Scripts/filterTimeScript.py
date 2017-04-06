import csv

def formatString(row):
    list  = []
    list.append(row[0])
    list.append(str(round(float(row[1]),4)))
    list.append(str(round(float(row[2]),4)))
    list.append(str(round(float(row[3]),4)))
    return list
with open('rawdata2.csv','rb') as csvfile,open('outputFile2.csv','wb') as output :
    reader = csv.reader(csvfile)
    writer = csv.writer(output)
    stored = 0
    accum = 0
    for row in reader:  #print row[0]
        epoch = int(row[0])
        difference = epoch - stored
        accum += difference
        if accum > 95:
            accum = 0
            writer.writerow((formatString(row)))
        stored = int(row[0])


