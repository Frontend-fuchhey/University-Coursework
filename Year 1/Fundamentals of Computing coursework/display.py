#display function to diaplay medicines in forammated order
def display_stock(inventory):
    print("")
    print("-"*95)
    print(f"| {'Id':<4} | {'Name':<15} | {'Mg':8} | {'Brand':<15} | {'Stock':<8} | {'Price(Tab)':<10} | {'Price(Strip)'} |")
    print("-"*95)

    #to generate unique id of medicines
    for index, med in enumerate(inventory, start=1):
        print(f"| {index:<4} | {med['name']:<15} | {med['miligram']:<8} | {med['brand']:<15} | {med['stock']:<8} | {med['rate(tab)']:<10.2f} | {med['rate(strip)']:<10.2f}   |")
        
    print("-"*95) 

    # prevent from executing directly (pause) just to improve user experience not any functional use 
    input("\n Current stock press ENTER to continue")