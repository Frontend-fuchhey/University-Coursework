import os
import display

def get_next_invoice_number(folder="invoices"):
    # Create the folder if it doesn't exist
    if not os.path.exists(folder):
        os.makedirs(folder)
        return 1
    files = os.listdir(folder)
    return len(files) + 1

def sell_medicine(inventory, save_function):
    customer_name = input("Enter Customer Name: ").strip()
    clean_name = customer_name.replace(" ", "_")
    cart = []
    grand_total = 0

    while True:
        display.display_stock(inventory)
        try:
            choice = int(input("\nEnter Medicine ID to sell: ")) - 1
            if choice < 0 or choice >= len(inventory):
                print("Invalid ID selection.")
                continue
                
            med = inventory[choice]
            
            unit = input(f"Sell {med['name']} by (tablet/strip): ").lower().strip()
            qty = int(input(f"Enter quantity of {unit}s: "))

            total_tabs = qty * med['tabsperstrip'] if unit == "strip" else qty

            if total_tabs <= med['stock']:
                if unit == "tablet":
                    num_strips = qty // med['tabsperstrip']
                    num_tabs = qty % med['tabsperstrip']
                else:
                    num_strips = qty
                    num_tabs = 0

                subtotal = (num_strips * med['rate(strip)']) + (num_tabs * med['rate(tab)'])
                
                # Apply 5% discount for 2 or more strips
                discount = subtotal * 0.05 if (unit == "strip" and qty >= 2) else 0
                final_item_price = subtotal - discount

                med['stock'] -= total_tabs
                cart.append({
                    "name": med['name'],
                    "qty": qty,
                    "unit": unit,
                    "subtotal": subtotal,
                    "discount": discount,
                    "total": final_item_price
                })
                grand_total += final_item_price
                print(f"Added {med['name']} to cart.")
            else:
                print(f"Not enough stock! (Available: {med['stock']})")
        except (ValueError, IndexError):
            print("Invalid input.")

        if input("\nAdd another item to this bill? (y/n): ").lower() != 'y':
            break

    if cart:
        inv_no = get_next_invoice_number("invoices")
        
        
        filename = f"invoices/{clean_name}_Invoice_{inv_no}.txt"
        
        bill_output = f"\n{'='*45}\n"
        bill_output += f"{'MEDSTORE INVOICE #' + str(inv_no):^45}\n"
        bill_output += f"{'='*45}\n"
        bill_output += f"Customer: {customer_name}\n"
        bill_output += f"{'-'*45}\n"
        bill_output += f"{'Item':<18} {'Qty':<8} {'Disc':<8} {'Total':<10}\n"
        bill_output += f"{'-'*45}\n"
        
        for item in cart:
            bill_output += f"{item['name']:<18} {item['qty']} {item['unit']:<4} "
            bill_output += f"Rs.{item['discount']:>5.2f}  Rs.{item['total']:>8.2f}\n"
        
        bill_output += f"{'-'*45}\n"
        bill_output += f"{'GRAND TOTAL:':<30} Rs.{grand_total:>8.2f}\n"
        bill_output += f"{'='*45}\n"

        print(bill_output)

        # Save to the specific folder
        with open(filename, "w") as f:
            f.write(bill_output)
        
        save_function(inventory)
        print(f"Success! Invoice saved in 'invoices' folder as: {filename}")