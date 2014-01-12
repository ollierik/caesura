package caesura.audio;

public class Allocator {

	private int first_inactive;
	private int[] list;
	private int size;

	public Allocator(int n) {
		list = new int[n];
		init(n);
	}

	public void init(int n) {
		size = n;
		for (int i=0; i<size; i++) {
			list[i] = i;
		}
	}

	/**
	 * Attemps to allocate new entry.
	 * @return index of the new entry, -1 if couldn't be done
	 */
	public int add() {
		if (this.first_inactive >= size)
			return -1;

		// new entry is the next free grain, i
		int i = this.list[this.first_inactive];
		this.first_inactive++;
		return i;
	}

	/**
	 * Remove the r defined.
	 * @param r the entry to be removed
	 */
	public void remove(int r) {
		jvst.wrapper.VSTPluginAdapter.log("remove");
		this.first_inactive--;
		int tempIndex = this.list[r];
		this.list[r] = this.list[this.first_inactive];
		this.list[this.first_inactive] = tempIndex;
	}

	public int getNumberOfActives() {
		return this.first_inactive;
	}
	
	public int getAllocation(int i) {
		return list[i];
	}

	public int getMaxSize() {
		return size;
	}
}
