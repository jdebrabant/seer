package ca.pfv.spmf.highutility.two_phase_algorithm;
import java.util.ArrayList;
import java.util.List;


/**
 * This class represents a set of itemsets.
 * They are ordered by size. For example, "level 1" means the itemsets of size 1 (containing 1 item).
 * @author Philippe Fournier-Viger 
 */
public class Itemsets {
	private final List<List<Itemset>> levels = new ArrayList<List<Itemset>>();  // itemset class� par taille
	private int itemsetsCount=0;
	public String name;
	
	public Itemsets(String name){
		this.name = name;
		levels.add(new ArrayList<Itemset>()); // We create an empty level 0 by default.
	}
	
	public void printItemsets(int nbObject){
		System.out.println(" ------- " + name + " -------");
		int patternCount=0;
		int levelCount=0;
		for(List<Itemset> level : levels){
			System.out.println("  L" + levelCount + " ");
			for(Itemset itemset : level){
				System.out.print("  pattern " + patternCount + "  ");
				itemset.print();
				System.out.print("support : " + itemset.getSupportRelatifFormatted(nbObject));
				System.out.print(" utility : " + itemset.getUtility());
				System.out.print(" tidset : " + itemset.getTransactionsIds());
				patternCount++;
				System.out.println("");
			}
			levelCount++;
		}
		System.out.println(" --------------------------------");
	}
	
	
	
	public void addItemset(Itemset itemset, int k){
		while(levels.size() <= k){
			levels.add(new ArrayList<Itemset>());
		}
		levels.get(k).add(itemset);
		itemsetsCount++;
	}

	public List<List<Itemset>> getLevels() {
		return levels;
	}

	public int getItemsetsCount() {
		return itemsetsCount;
	}
	
	public void decreaseCount(){
		itemsetsCount--;
	}
	
}
