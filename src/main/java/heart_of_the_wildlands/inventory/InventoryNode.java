package heart_of_the_wildlands.inventory;

public class InventoryNode {
    String itemName;
    int quantity;
    String iconPath;

    InventoryNode prev;
    InventoryNode next;

    public InventoryNode(String itemName, int quantity, String iconPath) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.iconPath = iconPath;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public InventoryNode getPrev() {
        return prev;
    }

    public void setPrev(InventoryNode prev) {
        this.prev = prev;
    }

    public InventoryNode getNext() {
        return next;
    }

    public void setNext(InventoryNode next) {
        this.next = next;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

}
