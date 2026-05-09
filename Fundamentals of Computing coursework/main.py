'''
Fundamentals of Computing coursework,
Medicine management system 
'''

#imports from seperate files
import choices
import display
import sales
import restock
#main menu
def main():
    #load the medicine to the program
    inventory=choices.inventory_read()

    while True:
        print("\n"+"="*60)
        print(f"{'MEDSTORE MEDICINE MANAGEMENT SYSTEM':^60}")         
        print("="*60)
        print("1. View Stock")
        print("2. Sell Medicine")
        print("3. Restock medicine")
        print("4. Exit")
        print("-"* 60)
        print(" ")
        choice = input("Enter choice (1-4) : ")

        # choice 1 - see what is in the stock
        if choice=="1":
            #calling display function to display inventory 
            display.display_stock(inventory)

        elif choice =="2":
            #calling function to sell medicine 
            sales.sell_medicine(inventory,choices.save_inventory)

        elif choice =="3":
            #calling function to restock medicine
            restock.restock_medicine(inventory,choices.save_inventory)

        elif choice=="4":
            print("closing system Medstore Management system")
            break

        #terminate wrong input
        else:
            print("ENTER 1/2/3 or 4 for execution of the program ")
            #prevent from execution directly
            input("Press Enter to try again...")

# Boiler-plate code
if __name__=="__main__":
    main()
       

              

                                           