import os
import display

def get_next_note_number(folder="restock_notes"):
    """
    Calculates number of files in the restock_notes directory to generate the next unique id.
    """
    if os.path.exists(folder) == False:
        return 1
    
    files = os.listdir(folder)
    next_number = len(files) + 1
    return next_number

def restock_medicine(inventory, save_function):
    """
    Processes restocking procedure, updates the memory and outputs the confirmation table in proper format.
    """
    vendor_name = input("Enter Vendor/Supplier Name: ").strip()
    
    
    #Rename filename manually (without using .replace function shortcut)
    clean_vendor = ""
    for char in vendor_name:
        if char == " ":
            clean_vendor = clean_vendor + "_"
        else:
            clean_vendor = clean_vendor + char
    
    # Display current stock for user reference
    display.display_stock(inventory)
    
    try:
        # Get ID and convert to index
        user_input_id = input("\nEnter Medicine ID to restock: ")
        choice = int(user_input_id) - 1
        
        # Validation for ID range
        if choice < 0 or choice >= len(inventory):
            print("Invalid ID. Restock cancelled.")
            return

        # Get Quantity of restocking items
        input_qty = input("Enter quantity of tablets to add: ")
        qty_to_add = int(input_qty)
        
        #if quantity is less then zero show the error
        if qty_to_add <= 0:
            print("Quantity must be greater than zero.")
            return


        old_stock = inventory[choice]['stock']
        new_stock = old_stock + qty_to_add
        inventory[choice]['stock'] = new_stock
        
        # Determine file path
        note_no = get_next_note_number("restock_notes")
        filename = "restock_notes/" + clean_vendor + "_Restock_" + str(note_no) + ".txt"
        
        # formatted tabular format display for output
        border = "============================================="
        
        # for proper alighnment and clean UI i have used .format function
        header_row = "| {:<20} | {:<18} |".format("Description", "Entry")
        separator  = "|----------------------|--------------------|"
        
        # Print and update dynamically  in file and output screen
        final_output = "\n" + border + "\n"
        final_output = final_output + "|{:^43}|".format("RESTOCK CONFIRMATION") + "\n"
        final_output = final_output + border + "\n"
        final_output = final_output + header_row + "\n"
        final_output = final_output + separator + "\n"
        final_output = final_output + "| {:<20} | {:<18} |".format("Vendor", vendor_name) + "\n"
        final_output = final_output + "| {:<20} | {:<18} |".format("Medicine", inventory[choice]['name']) + "\n"
        final_output = final_output + "| {:<20} | {:<18} |".format("Quantity Added", str(qty_to_add)) + "\n"
        final_output = final_output + "| {:<20} | {:<18} |".format("New Total Stock", str(inventory[choice]['stock'])) + "\n"
        final_output = final_output + border + "\n"

        # Display to User
        print(final_output)

        # file not found exception handling
        try:
            with open(filename, "w") as f:
                f.write(final_output)
            print("Success: Restock note saved as " + filename)
        except FileNotFoundError:
            print(" Folder 'restock_notes' not found. File was not saved.")
            
        # Update the master inventory.txt file
        save_function(inventory)

    except ValueError:
        print("Please enter numerical values for ID and Quantity.")