package heart_of_the_wildlands.inventory;


public class Inventory {
    private InventoryNode head;
    private InventoryNode tail;
    private int size;

    // Menambahkan item kedalam inventory
    public void addItem(String itemName, int quantity, String iconPath) {
        InventoryNode current = head;

        // Menambahkan kuantitas item jika item sudah tersedia
        while (current != null) {
            if (current.itemName.equals(itemName)) {
                current.quantity += quantity;
                return;
            }
            current = current.next;
        }

        // Membuat node baru jika item tidak ditemukan didalam inventory
        InventoryNode newNode = new InventoryNode(itemName, quantity, iconPath);
        if (head == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
    }


    // Menghapus item berdasarkan namanya
    public boolean removeItem(String itemName) {
        InventoryNode current = head;
        while (current != null) {
            if (current.itemName.equals(itemName)) {
                if (current == head) head = current.next;
                if (current == tail) tail = current.prev;
                if (current.prev != null) current.prev.next = current.next;
                if (current.next != null) current.next.prev = current.prev;
                size--;
                return true;
            }
            current = current.next;
        }
        return false;
    }

    // Menampilkan isi dari inventorynya
    public void printInventory() {
        InventoryNode current = head;
        System.out.println("Inventory:");
        while (current != null) {
            System.out.println("- " + current.itemName + " (x" + current.quantity + ")");
            current = current.next;
        }
    }


    public InventoryNode getHead() {
        return head;
    }

    public void swapItems(InventoryNode nodeA, InventoryNode nodeB) {
        if (nodeA == null || nodeB == null || nodeA == nodeB) return;

        // Fitur untuk memindahkan item ke node lain
        String tempName = nodeA.getItemName();
        int tempQuantity = nodeA.getQuantity();
        String tempIconPath = nodeA.getIconPath();

        nodeA.setItemName(nodeB.getItemName());
        nodeA.setQuantity(nodeB.getQuantity());
        nodeA.setIconPath(nodeB.getIconPath());

        nodeB.setItemName(tempName);
        nodeB.setQuantity(tempQuantity);
        nodeB.setIconPath(tempIconPath);
    }

    public InventoryNode findItem(String itemName) {
        InventoryNode current = head;
        while (current != null) {
            if (current.getItemName().equals(itemName)) return current;
            current = current.next;
        }
        return null;
    }


    public int getSize() {
        return size;
    }
}
