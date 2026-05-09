'''
Main logic of overaall systeem is maintained` here 
'''

# load data from inventory.txt
def inventory_read():

    medicine_list=[] #empty list for medicine

    with open("inventory.txt","r") as file:#open file in read mode 
        for line in file:
        #at a time that line variable will hold one line at once 
            med=line.strip().split(",") #seperate the sentence into list of words ` , `

            #medicine ko dictionary
            medicine_info={
            "name":med[0],
            "miligram":med[1],
            "brand":med[2],
            "stock":int(med[3]),
            "rate(tab)":float(med[4]),
            "rate(strip)":float(med[5]),
            "tabsperstrip":int(med[6])
            }

            medicine_list.append(medicine_info)
    return medicine_list

#function to edit updated data in file 
def save_inventory(inventory):
    with open("inventory.txt","w") as file:
        for med in inventory:
            line = f"{med['name']},{med['miligram']},{med['brand']},{med['stock']},{med['rate(tab)']},{med['rate(strip)']},{med['tabsperstrip']}\n"
            file.write(line)


                 


