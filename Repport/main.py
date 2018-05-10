import os
import sys
import re
import shutil
#####################################
#Function to return the number of distinc values in a valid CNF format file
def distinctOccurInFile(outLines):
    allVar=[]
    for i in range(1,len(outLines)):
        spl=re.split("\s+",outLines[i].replace("-",""))
        # print spl
        for col in spl :
            if re.match("[1-9]+",col) and (col not in allVar) :
                # print col
                allVar.append(col)
    return(len(allVar))
######################################

import FormulaToCNF
#insert here any propositional logic formula
dimacs = FormulaToCNF.getDIMACS(sys.argv[2],False,False)
print dimacs
outF = open("out.cnf", "w+r")
outF.write(dimacs)
outF.seek(0)

inPutFile = str(sys.argv[1])
#Opening the file where the knowledge base is.
file = open(inPutFile,"r")
#Opening the output file which will containt the KB and the negation of the formula in CNF
wfile = open("new.cnf","w")
#Var to store the file new.cnf
outLines=[]
for line in file.readlines():
    outLines.append(line)
for line in outF.readlines():
    outLines.append(line)

#Modifying the header of the file to adjust the number of Variables/Clauses depending on the input formula
splitF = re.split("\s+",outLines[0]);
if len(sys.argv)>2 :
    splitF[3] = str(int(splitF[3])+len(sys.argv)-2)
splitF[2]=str(distinctOccurInFile(outLines))
outLines[0]=" ".join(splitF)+"\n"
#Writing the new KB into the new.cnf file
for str in outLines:
    wfile.write(str)
wfile.close()
file.close()
outF.close()
#Run the SAT solver with the new KB
os.system(" ./ubcsat -alg saps -solve -i new.cnf" )
