package com.arlania.world.content.gim.container;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import com.arlania.model.Item;

/**
 * A container holds a group of items.
 * 
 * @author Graham Edgecombe
 * 
 */
public class Container {

    public Container copy()
    {
        Container copy = new Container(type, capacity);
        for (int index = 0; index < items.length; index++)
            copy.items[index] = items[index];
        return copy;
    }
	
	/**
	 * The type of container.
	 * 
	 * @author Graham Edgecombe
	 * 
	 */
	public enum Type {

		/**
		 * A standard container such as inventory.
		 */
		STANDARD,

		/**
		 * A container which always stacks, e.g. the bank, regardless of the
		 * item.
		 */
		ALWAYS_STACK,

		/**
		 * A container which never stacks, e.g. items on death, regardless of
		 * the item.
		 */
		NEVER_STACK;

	}

	/**
	 * The capacity of this container.
	 */
	private int capacity;

	/**
	 * The items in this container.
	 */
	private Item[] items;

	/**
	 * A list of listeners.
	 */
	private List<ContainerListener> listeners = new LinkedList<ContainerListener>();

	/**
	 * The container type.
	 */
	private Type type;

	public Type getType() { return type; }

	/**
	 * Firing events flag.
	 */
	private boolean firingEvents = true;

	/**
	 * Creates the container with the specified capacity.
	 * 
	 * @param type
	 *            The type of this container.
	 * @param capacity
	 *            The capacity of this container.
	 */
	public Container(Type type, int capacity) {
		this.type = type;
		this.capacity = capacity;
		this.items = new Item[capacity];
	}

	/**
	 * Sets the firing events flag.
	 * 
	 * @param firingEvents
	 *            The flag.
	 */
	public void setFiringEvents(boolean firingEvents) {
		this.firingEvents = firingEvents;
	}

	/**
	 * Checks the firing events flag.
	 * 
	 * @return <code>true</code> if events are fired, <code>false</code> if not.
	 */
	public boolean isFiringEvents() {
		return firingEvents;
	}

	/**
	 * Gets the listeners of this container.
	 * 
	 * @return The listeners of this container.
	 */
	public Collection<ContainerListener> getListeners() {
		return Collections.unmodifiableCollection(listeners);
	}

	/**
	 * Adds a listener.
	 * 
	 * @param listener
	 *            The listener to add.
	 */
	public void addListener(ContainerListener listener) {
		listeners.add(listener);
		listener.itemsChanged(this);
	}

	/**
	 * Removes a listener.
	 * 
	 * @param listener
	 *            The listener to remove.
	 */
	public void removeListener(ContainerListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Removes all listeners.
	 */
	public void removeAllListeners() {
		listeners.clear();
	}

	/**
	 * Shifts all items to the top left of the container leaving no gaps.
	 */
	public void shift() {
		Item[] old = items;
		items = new Item[capacity];
		int newIndex = 0;
		for (int i = 0; i < items.length; i++) {
			if (old[i] != null) {
				items[newIndex] = old[i];
				newIndex++;
			}
		}
		if (firingEvents) {
			fireItemsChanged();
		}
	}

	/**
	 * Gets the next free slot.
	 * 
	 * @return The slot, or <code>-1</code> if there are no available slots.
	 */
	public int freeSlot() {
		for (int i = 0; i < items.length; i++) {
			if (items[i] == null || items[i].getId() == -1) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Counts number of empty slots
	 * 
	 * @return Number of empty slots
	 */
	public int emptySlots() {
		int empty = 0;
		for (Item item : items) {
			if (item == null || item.getId() == -1) {
				empty++;
			}
		}
		return empty;
	}
	
	public Item[] getItems() {
		return items;
	}

	/**
	 * Attempts to add an item into the next free slot.
	 * 
	 * @param item
	 *            The item.
	 * @return <code>true</code> if the item was added, <code>false</code> if
	 *         not.
	 */
	public boolean add(Item item) {
		return add(item, -1);
	}
	
	public boolean add(Item[] items) {
		for(Item item : items) {
			if(freeSlots() <= 0)
				return false;
			
			add(item, -1);
		}
		return true;
	}

	/**
	 * Attempts to add a specific slot.
	 * 
	 * @param item
	 *            The item.
	 * @param slot
	 *            The desired slot.
	 * @return <code>true</code> if the item was added, <code>false</code> if
	 *         not.
	 */
	public boolean add(Item item, int slot) {
		if (item == null) {
			return false;
		}
		int newSlot = slot > -1 ? slot : freeSlot();
		if (((item.getDefinition().isStackable() || type.equals(Type.ALWAYS_STACK)) && item.getId() != 11854) && !type.equals(Type.NEVER_STACK)) {
			if (getCount(item.getId()) > 0) {
				newSlot = getSlotById(item.getId());
			}
		}
		if (newSlot == -1) {
			// the free slot is -1
			return false;
		}
		if (get(newSlot) != null) {
			newSlot = freeSlot();
		}
		// && (!type.equals(Type.NEVER_STACK) || !(item.getToken() != null && item.getToken().equals("")))
		boolean ignoreStack = false;
		
		if (((item.getDefinition().isStackable() || type.equals(Type.ALWAYS_STACK)) && item.getId() != 11854) && !type.equals(Type.NEVER_STACK) && !ignoreStack) {
			for (int i = 0; i < items.length; i++) {
				if (items[i] != null && items[i].getId() == item.getId()) {
					int totalCount = item.getAmount() + items[i].getAmount();
					if (totalCount >= Integer.MAX_VALUE || totalCount < 1) {
						return false;
					}
					//System.out.println("bbbbbb");
					set(i, new Item(items[i].getId(), items[i].getAmount() + item.getAmount()));
					return true;
				}
			}
			if (newSlot == -1) {
				return false;
			} else {
				set(slot > -1 ? newSlot : freeSlot(), item);
				return true;
			}
		} else {
			int slots = freeSlots();
			if(item.getAmount() > slots)
				item.setAmount(slots);
			if (slots >= item.getAmount()) {
				boolean b = firingEvents;
				firingEvents = false;
				try {
					int copy = item.getAmount();
					for (int i = 0; i < copy; i++) {
						if(!item.getDefinition().isStackable())
							item.setAmount(1);
						set(slot > -1 ? newSlot : freeSlot(), item);
					}
					if (b) {
						fireItemsChanged();
					}
					return true;
				} finally {
					firingEvents = b;
				}
			} else {
				return false;
			}
		}
	}
	/*public boolean add(Item item, int slot) {
		if (item == null) {
			return false;
		}
		int newSlot = slot > -1 ? slot : freeSlot();
		if (((item.getDefinition().isStackable() || type.equals(Type.ALWAYS_STACK)) && item.getId() != 11854) && !type.equals(Type.NEVER_STACK)) {
			if (getCount(item.getId()) > 0) {
				newSlot = getSlotById(item.getId());
			}
		}
		if (newSlot == -1) {
			// the free slot is -1
			return false;
		}
		if (get(newSlot) != null) {
			newSlot = freeSlot();
		}
		if (((item.getDefinition().isStackable() || type.equals(Type.ALWAYS_STACK)) && item.getId() != 11854) && !type.equals(Type.NEVER_STACK)) {
			for (int i = 0; i < items.length; i++) {
				if (items[i] != null && items[i].getId() == item.getId()) {
					int totalCount = item.getAmount() + items[i].getAmount();
					if (totalCount >= Integer.MAX_VALUE || totalCount < 1) {
						return false;
					}
					set(i, new Item(items[i].getId(), items[i].getAmount() + item.getAmount()));
					return true;
				}
			}
			if (newSlot == -1) {
				return false;
			} else {
				set(slot > -1 ? newSlot : freeSlot(), item);
				return true;
			}
		} else {
			int slots = freeSlots();
			if(item.getAmount() > slots)
				item.setAmount(slots);
			if (slots >= item.getAmount()) {
				boolean b = firingEvents;
				firingEvents = false;
				try {
					for (int i = 0; i < item.getAmount(); i++) {
						set(slot > -1 ? newSlot : freeSlot(), new Item(item.getId()));
					}
					if (b) {
						fireItemsChanged();
					}
					return true;
				} finally {
					firingEvents = b;
				}
			} else {
				return false;
			}
		}
	}*/

	/**
	 * Gets the number of free slots.
	 * 
	 * @return The number of free slots.
	 */
	public int freeSlots() {
		return capacity - size();
	}
	
	public int getNextFreeSlot() {
		int slot = 0;
		for(Item item : items) {
			if(item == null)
				continue;
			if(item != null)
				slot++;
		}
		return slot;
	}

	/**
	 * Gets an item.
	 * 
	 * @param index
	 *            The position in the container.
	 * @return The item.
	 */
	public Item get(int index) {
		if (index == -1) {
			return null;
		}
		return items[index];
	}
	
	public int getId(int slot) {
		Item item = get(slot);
		if (item != null) {
			return item.getId();
		}
		return -1;
	}
	
	/**
	 * Get item by slot
	 * @param slot
	 * @return
	 */
	public Item getBySlot(int slot) {
		int s = 0;
		for (Item item : items) {
			if(s == slot)
				return item;
			s++;
		}
		return null;
	}
	
	/**
	 * Gets an item by id.
	 * 
	 * @param id
	 *            The id.
	 * @return The item, or <code>null</code> if it could not be found.
	 */
	public Item getById(int id) {
		for (Item item : items) {
			if (item == null) {
				continue;
			}
			if (item.getId() == id) {
				return item;
			}
		}
		return null;
	}

	/**
	 * Gets a slot by id.
	 * 
	 * @param id
	 *            The id.
	 * @return The slot, or <code>-1</code> if it could not be found.
	 */
	public int getSlotById(int id) {
		for (int i = 0; i < items.length; i++) {
			if (items[i] == null) {
				continue;
			}
			if (items[i].getId() == id) { ///* && items[i].getToken() != null && items[i].getToken().equalsIgnoreCase("")*/
				return i;
			}
		}
		return -1;
	}

	public int getSlotByIdAndHigherThenZero(int id) {
		for (int i = 0; i < items.length; i++) {
			if (items[i] == null) {
				continue;
			}
			if (items[i].getId() == id && items[i].getAmount() > 0) { ///* && items[i].getToken() != null && items[i].getToken().equalsIgnoreCase("")*/
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Sets an item.
	 * 
	 * @param index
	 *            The position in the container.
	 * @param item
	 *            The item.
	 */
	public void set(int index, Item item) {
		items[index] = item;
		if (firingEvents) {
			fireItemChanged(index);
		}
	}

	public void replace(int item, int item2) {
		remove(new Item(item));
		add(new Item(item2));
	}

	/**
	 * Gets the capacity of this container.
	 * 
	 * @return The capacity of this container.
	 */
	public int capacity() {
		return capacity;
	}

	/**
	 * Gets the size of this container.
	 * 
	 * @return The size of this container.
	 */
	public int size() {
		int size = 0;
		for (Item item : items) {
			if (item != null) {
				size++;
			}
		}
		return size;
	}

	/**
	 * Clears this container.
	 */
	public void clear() {
		items = new Item[items.length];
		if (firingEvents) {
			fireItemsChanged();
		}
	}

	/**
	 * Returns an array representing this container.
	 * 
	 * @return The array.
	 */
	public Item[] toArray() {
		return items;
	}

	/**
	 * Checks if a slot is used.
	 * 
	 * @param slot
	 *            The slot.
	 * @return <code>true</code> if an item is present, <code>false</code>
	 *         otherwise.
	 */
	public boolean isSlotUsed(int slot) {
		return items[slot] != null;
	}

	/**
	 * Checks if a slot is free.
	 * 
	 * @param slot
	 *            The slot.
	 * @return <code>true</code> if an item is not present, <code>false</code>
	 *         otherwise.
	 */
	public boolean isSlotFree(int slot) {
		return items[slot] == null;
	}

	/**
	 * Removes an item.
	 * 
	 * @param item
	 *            The item to remove.
	 * @return The number of items removed.
	 */
	public int remove(Item item) {
		return remove(item, -1, false);
	}

	/**
	 * Removes an item, allowing zeros.
	 * 
	 * @param item
	 *            The item.
	 * @return The number of items removed.
	 */
	public int removeOrZero(Item item) {
		return remove(item, -1, true);
	}

	/**
	 * Removes an item.
	 * 
	 * @param item
	 *            The item.
	 * @param preferredSlot
	 *            The preferred slot.
	 * @return The number of items removed.
	 */
	public int remove(Item item, int preferredSlot) {
		return remove(item, preferredSlot, false);
	}

	/**
	 * Removes an item.
	 * 
	 * @param item
	 *            The item to remove.
	 * @param preferredSlot
	 *            The preferred slot.
	 * @param allowZero
	 *            If a zero amount item should be allowed.
	 * @return The number of items removed.
	 */
	public int remove(Item item, int preferredSlot, boolean allowZero) {
		if (item == null) {
			return -1;
		}
		int removed = 0;
		if ((item.getDefinition().isStackable() || type.equals(Type.ALWAYS_STACK)) && !type.equals(Type.NEVER_STACK)) {
			int slot = getSlotById(item.getId());
			Item stack = get(slot);
			if (stack == null) {
				return -1;
			}
			if (stack.getAmount() > item.getAmount()) {
				removed = item.getAmount();
				set(slot, new Item(stack.getId(), stack.getAmount() - item.getAmount()));
			} else {
				removed = stack.getAmount();
				boolean hasToken = false;
				if(hasToken && alreadyHasPlaceholder(item.getId())) {
					allowZero = false;
					slot = preferredSlot;
				} else if(hasToken && !alreadyHasPlaceholder(item.getId()))
					slot = preferredSlot;

				set(slot, allowZero ? new Item(stack.getId(), 0) : hasToken ? new Item(-1) : null);
			}
		} else {
			for (int i = 0; i < item.getAmount(); i++) {
				int slot = getSlotById(item.getId());
				if (i == 0 && preferredSlot != -1) {
					Item inSlot = get(preferredSlot);
					if (inSlot.getId() == item.getId()) {
						slot = preferredSlot;
					}
				}
				if (slot != -1) {
					removed++;
					set(slot, null);
				} else {
					break;
				}
			}
		}
		return removed;
	}

	/**
	 * Transfers an item from one container to another.
	 * 
	 * @param from
	 *            The container to transfer from.
	 * @param to
	 *            The container to transfer to.
	 * @param fromSlot
	 *            The slot in the original container.
	 * @param id
	 *            The item id.
	 * @return A flag indicating if the transfer was successful.
	 */
	public static boolean transfer(Container from, Container to, int fromSlot, int id) {
		Item fromItem = from.get(fromSlot);
		if (fromItem == null || fromItem.getId() != id) {
			return false;
		}
		if (to.add(fromItem)) {
			from.set(fromSlot, null);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Swaps two items.
	 * 
	 * @param fromSlot
	 *            From slot.
	 * @param toSlot
	 *            To slot.
	 */
	public void swap(int fromSlot, int toSlot) {
		Item temp = get(fromSlot);
		boolean b = firingEvents;
		firingEvents = false;
		try {
			set(fromSlot, get(toSlot));
			set(toSlot, temp);
			if (b) {
				fireItemsChanged(new int[]{fromSlot, toSlot});
			}
		} finally {
			firingEvents = b;
		}
	}

	/**
	 * Gets the total amount of an item, including the items in stacks.
	 * 
	 * @param id
	 *            The id.
	 * @return The amount.
	 */
	public int getCount(int id) {
		int total = 0;
		for (Item item : items) {
			if (item != null) {
				if (item.getId() == id) {
					total += item.getAmount();
				}
			}
		}
		return total;
	}
	
	public int getCount(int[] ids) {
		int total = 0;
		for (Item item : items) {
			if (item != null) {
				for(int i = 0; i < ids.length; i++) {
					if (item.getId() == ids[i]) {
						total += item.getAmount();
					}
				}
			}
		}
		return total;
	}
	
	public int getCount() {
		int total = 0;
		for (Item item : items) {
			if(item == null)
				continue;
			if(item != null || item.getId() == -1)
				total++;
		}
		return total;
	}
	
	public int getSpaces() {
		int total = 0;
		for (Item item : items) {
			if(item == null || item.getId() == -1)
				continue;
			if(item != null)
				total++;
		}
		return total;
	}
	
	public int getPlaceholderCount() {
		int total = 0;
		for (Item item : items) {
			if(item == null)
				continue;
			if(item != null && item.getAmount() == 0)
				total++;
		}
		return total;
	}
	
	/**
	 * Inserts an item.
	 * 
	 * @param fromSlot
	 *            The old slot.
	 * @param toSlot
	 *            The new slot.
	 */
	public void insert(int fromSlot, int toSlot) {
		// we reset the item in the from slot
		Item from = items[fromSlot];
		if (from == null) {
			return;
		}
		items[fromSlot] = null;
		// find which direction to shift in
		if (fromSlot > toSlot) {
			int shiftFrom = toSlot;
			int shiftTo = fromSlot;
			for (int i = toSlot + 1; i < fromSlot; i++) {
				if (items[i] == null) {
					shiftTo = i;
					break;
				}
			}
			Item[] slice = new Item[shiftTo - shiftFrom];
			System.arraycopy(items, shiftFrom, slice, 0, slice.length);
			System.arraycopy(slice, 0, items, shiftFrom + 1, slice.length);
		} else {
			int sliceStart = fromSlot + 1;
			int sliceEnd = toSlot;
			for (int i = sliceEnd - 1; i >= sliceStart; i--) {
				if (items[i] == null) {
					sliceStart = i;
					break;
				}
			}
			Item[] slice = new Item[sliceEnd - sliceStart + 1];
			System.arraycopy(items, sliceStart, slice, 0, slice.length);
			System.arraycopy(slice, 0, items, sliceStart - 1, slice.length);
		}
		// now fill in the target slot
		items[toSlot] = from;
		if (firingEvents) {
			fireItemsChanged();
		}
	}

	/**
	 * Fires an item changed event.
	 * 
	 * @param slot
	 *            The slot that changed.
	 */
	public void fireItemChanged(int slot) {
		for (ContainerListener listener : listeners) {
			listener.itemChanged(this, slot);
		}
	}

	/**
	 * Fires an items changed event.
	 */
	public void fireItemsChanged() {
		for (ContainerListener listener : listeners) {
			listener.itemsChanged(this);
		}
	}

	/**
	 * Fires an items changed event.
	 * 
	 * @param slots
	 *            The slots that changed.
	 */
	public void fireItemsChanged(int[] slots) {
		for (ContainerListener listener : listeners) {
			listener.itemsChanged(this, slots);
		}
	}

	/**
	 * Checks if the container contains the specified item.
	 * 
	 * @param id
	 *            The item id.
	 * @return <code>true</code> if so, <code>false</code> if not.
	 */
	public boolean contains(int id) {
		return getSlotById(id) != -1;
	}
	
	public boolean contains(Item contains) {
		for (Item item : items) {
			if (item == null) {
				continue;
			}
			if (item.getId() == contains.getId()) {
				return true;
			}
		}
		return false;
	}
	
	public boolean contains(int id, int amount) {
		if (!contains(id))
			return false;
		return getCount(id) >= amount;
	}
	
	public boolean containsAll(int... ids) {
		return Arrays.stream(ids).allMatch(id -> contains(id));
	}
	
	public boolean containsAll(Item... items) {
		return Arrays.stream(items).filter(Objects::nonNull).allMatch(item -> contains(item.getId()));
	}
	
	public boolean containsAnyAmount(int amount, int... ids) {
		for(int i : ids) {
			if(contains(i) && getCount(i) >= amount)
				return true;
		}
		
		return false;
	}
	
	public int grabId(int... ids) {
		for(int i : ids)
			if(contains(i))
				return i;
		return -1;
	}
	
	public boolean containsAny(int... ids) {
		return Arrays.stream(ids).anyMatch(id -> contains(id));
	}
	
	public boolean containsAny(Item... items) {
		for(Item item : items) {
			if(contains(item))
				return true;
		}
		return false;
	}

	/**
	 * Checks if there is room in the inventory for an item.
	 * 
	 * @param item
	 *            The item.
	 * @return <code>true</code> if so, <code>false</code> if not.
	 */
	public boolean hasRoomFor(Item item) {
		if ((item.getDefinition().isStackable() || type.equals(Type.ALWAYS_STACK)) && !type.equals(Type.NEVER_STACK)) {
			for (Item item2 : items) {
				if (item2 != null && item2.getId() == item.getId()) {
					int totalCount = item.getAmount() + item2.getAmount();
					if (totalCount >= Integer.MAX_VALUE || totalCount < 1) {
						return false;
					}
					return true;
				}
			}
			int slot = freeSlot();
			return slot != -1;
		} else {
			int slots = freeSlots();
			return slots >= item.getAmount();
		}

	}

	/**
	 * Sets the containers contents.
	 * 
	 * @param items
	 *            The contents to set.
	 */
	public void setItems(Item[] items) {
		clear();
		for (int i = 0; i < items.length; i++) {
			this.items[i] = items[i];
		}
	}

	public boolean alreadyHasPlaceholder(int itemId) {
		for (Item item : items) {
			if (item == null) continue;
			if (item.getId() == itemId && item.getAmount() == 0)
				return true;
		}
		return false;
	}

}
